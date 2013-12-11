package cs201.roles.restaurantRoles.Brandon;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import javax.swing.JOptionPane;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.roles.restaurant.Brandon.CustomerGuiBrandon;
import cs201.helper.Constants;
import cs201.helper.Brandon.MenuBrandon;
import cs201.interfaces.roles.restaurant.Brandon.CashierBrandon;
import cs201.interfaces.roles.restaurant.Brandon.CustomerBrandon;
import cs201.interfaces.roles.restaurant.Brandon.HostBrandon;
import cs201.interfaces.roles.restaurant.Brandon.WaiterBrandon;
import cs201.roles.restaurantRoles.RestaurantCustomerRole;
import cs201.trace.AlertLog;
import cs201.trace.AlertTag;

/**
 * Restaurant customer agent.
 */
public class RestaurantCustomerRoleBrandon extends RestaurantCustomerRole implements CustomerBrandon
{
	private static final int EAT_TIME = 5000;
	private static final int ORDER_DECISION_TIME = 5000;
	private boolean hungry;
	private WaiterBrandon w;
	private HostBrandon h;
	enum CustomerState {NoState,Waiting, Walking, Seated, Ready, Ordered, Eating, Ate,DoneEating,WaitingForBill,ReceivedBill, Leaving,Reorder,Told};
	private String order = "";
	private CustomerState s = CustomerState.NoState;
	private Semaphore orderWait = new Semaphore(0,true);
	private Semaphore changeWait = new Semaphore(0,true);
	
	private MenuBrandon menu;
	
	private CustomerGuiBrandon gui;
	
	private Timer timer = new Timer();
	
	private String name;
	
	private Semaphore animationPause = new Semaphore(0,true);
	private double billAmount;
	private CashierBrandon cashier;
	
	private double startingMoney;
	
	private static final int MAX_MONEY = 100;
	
	/**
	 * Creates a new CustomerAgent
	 * @param name the name of the customer
	 * @param h the host of the restaurant
	 */
	public RestaurantCustomerRoleBrandon(String name, HostBrandon h)
	{
		this.name = name;
		this.h = h;
		if(name.contains("Broke") || name.contains("Flake"))
		{
			startingMoney = 0;
		}
		else
		{
			try
			{
				startingMoney = Double.parseDouble(name);
			}
			catch(NumberFormatException e)
			{
				double money = (Math.random()*MAX_MONEY)+10;
				int moneyRound = (int)(money*100);
				startingMoney = ((double)moneyRound)/100;
			}
		}
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,"Initialized with $"+startingMoney);
	}
	
	/**
	 * Sets the GUI for the customer to use
	 * @param g the gui for the customer
	 */
	public void setGui(CustomerGuiBrandon g)
	{
		gui = g;
	}
	
	/**
	 * Gets the GUI representation of the customer
	 * @return the GUI representation of the customer
	 */
	public CustomerGuiBrandon getGui()
	{
		return gui;
	}
	
	/**
	 * Gets the name of the customer
	 * @return the name of the customer
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Signals that the customer is now hungry
	 */
	public void gotHungry()
	{
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,"I am hungry.");
		hungry = true;
		stateChanged();
	}
	
	/**
	 * Signals for the customer to follow the given waiter
	 * @param w the waiter to follow
	 * @param m the menu for the customer to use
	 */
	public void msgFollowMe(WaiterBrandon w, MenuBrandon m)
	{
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,"Told to follow "+w.getName());
		this.w = w;
		this.menu = m.clone();
		s = CustomerState.Walking;
		stateChanged();
	}
	
	/**
	 * Signals that the waiter is ready to take an order
	 */
	public void msgReadyToTakeOrder()
	{
		orderWait.release();
		stateChanged();
	}
	
	/**
	 * Signals that a customer can receive their order
	 */
	public void msgPresentFood()
	{
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,"I have received my food.");
		s = CustomerState.Eating;
		stateChanged();
	}
	
	/**
	 * Message from the GUI where customer reaches destination
	 */
	public void msgReachedDestination()
	{
		AlertLog.getInstance().logDebug(AlertTag.RESTAURANT,""+this,"I have reached my animation destination.");
		animationPause.release();
	}
	
	/**
	 * The customer must reorder from the new menu
	 * @param newMenu the new menu to order from
	 */
	public void msgHereIsMenu(MenuBrandon newMenu)
	{
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,"I need to reorder from "+newMenu.getFood());
		s = CustomerState.Reorder;
		menu = newMenu.clone();
		stateChanged();
	}
	

	/**
	 * The customer is given the bill and told to go to the cashier
	 * @param billAmount the amount to be paid
	 * @param cash the cashier to pay the bill to
	 */
	public void msgPresentBill(double billAmount,CashierBrandon cash)
	{
		this.billAmount = billAmount;
		s = CustomerState.ReceivedBill;
		cashier = cash;
		stateChanged();
	}

	/**
	 * The customer receives change from the cashier
	 * @param change the amount of change received
	 */
	public void msgGiveChange(double change)
	{
		startingMoney += change;
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,"Just received "+change+" in change. I now have "+startingMoney);
		changeWait.release();
	}
	
	/**
	 * The customer is informed that the restaurant is full
	 */
	public void msgInformedFull()
	{
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,"I was told the restaurant was full.");
		s = CustomerState.Told;
		stateChanged();
	}
	
	
	@Override
	public boolean pickAndExecuteAnAction()
	{
		if(s == CustomerState.Told)
		{
			toldAboutFull();
			return true;
		}
		else if(s == CustomerState.Reorder)
		{
			reorder();
			return true;
		}	
		else if(s == CustomerState.Walking)
		{
			goToTable();
			return true;
		}
		else if(s == CustomerState.Seated && !order.equals(""))
		{
			signalReady();
			return true;
		}
		else if(s == CustomerState.Eating)
		{
			eat();
			return true;
		}
		else if(s == CustomerState.DoneEating)
		{
			doneEating();
			return true;
		}
		else if(s == CustomerState.ReceivedBill)
		{
			payBill();
			return true;
		}
		else if(s == CustomerState.Leaving)
		{
			leave();
			return true;
		}
		else if(s == CustomerState.NoState && hungry)
		{
			goToRestaurant();
			return true;
		}
		return false;
	}

	private void payBill()
	{
		gui.doSetBill(billAmount);
		gui.doGoToCashier();
		try
		{
			animationPause.acquire();
		}
		catch(InterruptedException e)
		{
			AlertLog.getInstance().logError(AlertTag.RESTAURANT,""+this,"Issue with animation.");
		}
		double currentAmount = startingMoney;
		
		cashier.msgPay(this,startingMoney);
		startingMoney -= currentAmount;
		try
		{
			changeWait.acquire();
		}
		catch(InterruptedException e)
		{
			AlertLog.getInstance().logError(AlertTag.RESTAURANT,""+this,"Issue waiting for change.");
		}
		
		s = CustomerState.Leaving;
		gui.doRemoveLabel();
	}

	private void doneEating()
	{
		gui.doRemoveLabel();
		w.msgDoneEating(this);
		s = CustomerState.WaitingForBill;
	}

	private void reorder()
	{
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,"Preparing to reorder.");
		gui.doRemoveLabel();
		order = "";
		s = CustomerState.Seated;
		
		final RestaurantCustomerRoleBrandon cust = this;
		
		timer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				Map<String,Double> food = menu.getFood();
				
				ArrayList<String> affordableFoods = new ArrayList<String>();
				for(String f : food.keySet())
				{
					if(name.contains("Flake") || food.get(f) <= startingMoney)
					{
						affordableFoods.add(f);
					}
				}
				
				if(affordableFoods.size() > 0)
				{
					order = affordableFoods.get((int)(Math.random()*affordableFoods.size()));
				}
				else
				{
					//Customer cannot afford anything on the menu
					AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,"There is no food that I can afford!");
					s = CustomerState.Leaving;
					w.msgCustomerLeaving(cust);
				}
				stateChanged();
			}
		}, (long)(ORDER_DECISION_TIME*Constants.ANIMATION_SPEED_FACTOR));
	}

	private void goToRestaurant()
	{
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,"Going to restaurant.");
		gui.doGoToRestaurant();
		try
		{
			animationPause.acquire();
		}
		catch(InterruptedException e)
		{
			AlertLog.getInstance().logError(AlertTag.RESTAURANT,""+this,"Error with going to restaurant.");
		}
		
		s = CustomerState.Waiting;
		h.msgArrived(this);
	}
	
	private void goToTable()
	{
		gui.doGoToTable();
		try
		{
			animationPause.acquire();
		}
		catch(InterruptedException e)
		{
			AlertLog.getInstance().logError(AlertTag.RESTAURANT,""+this,"Error with customer going to table.");
		}
		
		final CustomerBrandon cust = this;
		
		s = CustomerState.Seated;
		timer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				Map<String,Double> food = menu.getFood();
				
				ArrayList<String> affordableFoods = new ArrayList<String>();
				for(String f : food.keySet())
				{
					if(name.contains("Flake") || food.get(f) <= startingMoney)
					{
						affordableFoods.add(f);
					}
				}
				
				if(food.keySet().contains(name))
				{
					order = name;
				}
				else if(affordableFoods.size() > 0)
				{
					order = affordableFoods.get((int)(Math.random()*affordableFoods.size()));
				}
				else
				{
					//Customer cannot afford anything on the menu
					AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,"Cannot afford anything.");
					s = CustomerState.Leaving;
					w.msgCustomerLeaving(cust);
				}
				stateChanged();
			}
		}, ORDER_DECISION_TIME);
	}
	
	private void signalReady()
	{
		gui.doSelectOrder(order);
		
		s = CustomerState.Ready;
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,"Signaling that I am ready to order.");
		w.msgReadyToOrder(this);
		try
		{
			orderWait.acquire();
		}
		catch(InterruptedException e)
		{
			AlertLog.getInstance().logError(AlertTag.RESTAURANT,""+this,"An error has occurred in a customer signalReady() method.");
		}
		s = CustomerState.Ordered;
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,"I am going to order "+order);
		w.msgGiveOrder(this, order);
	}
	
	private void eat()
	{
		gui.doReceiveFood(order);
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,"I am eating my food.");
		s = CustomerState.Ate;
		timer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				s = CustomerState.DoneEating;
				stateChanged();
			}
		}, (long)(EAT_TIME*Constants.ANIMATION_SPEED_FACTOR));
	}
	
	private void leave()
	{
		order = "";
		
		billAmount = 0;
		this.getPerson().setHungerLevel(0);
		
		this.getPerson().setMoney(startingMoney);
		
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,"I am leaving the restaurant");
		hungry = false;
		s = CustomerState.NoState;
		
		gui.doLeaveRestaurant();
		try
		{
			animationPause.acquire();
		}
		catch(InterruptedException e)
		{
			AlertLog.getInstance().logError(AlertTag.RESTAURANT,""+this,"Problem with customer leaving restaurant");
		}
		gui.didLeaveRestaurant();
		h.msgLeft(this);
		
		this.isActive = false;
		this.gui.setPresent(false);
	}
	
	/**
	 * String representation of the customer
	 * @return the string of the customer
	 */
	public String toString()
	{
		return "Customer "+getName();
	}

	private void toldAboutFull()
	{
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,"I was told about the restaurant being full.");
		if(Math.random() > .5 || name.equals("Leaving"))
		{
			AlertLog.getInstance().logDebug(AlertTag.RESTAURANT,""+this,"FULL RESTAURANT: LEAVING");
			hungry = false;
			h.msgNotWaiting(this);
			s = CustomerState.Leaving;
		}
		else
		{
			AlertLog.getInstance().logDebug(AlertTag.RESTAURANT,""+this,"FULL RESTAURANT: STAYING");
			s = CustomerState.Waiting;
		}
	}

	@Override
	public void startInteraction(Intention intent) {
		if (intent == Intention.RestaurantCustomer) {
			this.gui.setPresent(true);
			gotHungry();
			startingMoney = this.getPerson().getMoney();
		}
	}

	@Override
	public void msgClosingTime() {
		//Do Nothing
	}
}

