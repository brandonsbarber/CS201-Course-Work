package cs201.roles.restaurantRoles.Brandon;

import java.util.*;
import java.util.concurrent.Semaphore;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.roles.restaurant.Brandon.WaiterGuiBrandon;
import cs201.helper.Brandon.MenuBrandon;
import cs201.interfaces.roles.restaurant.Brandon.CashierBrandon;
import cs201.interfaces.roles.restaurant.Brandon.CookBrandon;
import cs201.interfaces.roles.restaurant.Brandon.CustomerBrandon;
import cs201.interfaces.roles.restaurant.Brandon.HostBrandon;
import cs201.interfaces.roles.restaurant.Brandon.WaiterBrandon;
import cs201.roles.restaurantRoles.RestaurantWaiterRole;

/**
 * A waiter agent for use in a restaurant application.
 * @author Brandon
 *
 */
public class RestaurantWaiterRoleBrandon extends RestaurantWaiterRole implements WaiterBrandon
{
	private List<MyCustomer> customers;
	
	enum CustomerState {Waiting, Seated, Ready, FoodDone, Eating, Done,GivenBill,Reorder};
	
	private Semaphore waitingForOrder;
	
	private HostBrandon host;
	
	private Map<String,Double> menu;
	
	private CookBrandon chef;
	private CashierBrandon cashier;
	
	private WaiterGuiBrandon gui;
	
	private Semaphore animationPause;
	private Semaphore billPause;
	private String name;
	
	enum WaiterState {Normal,WantsToBreak,AskedAboutBreak,CanBreak,Break, ComingOffBreak, Denied};
	private WaiterState s;
	
	public static int instanceCount = 0;
	private int idNumber;
	
	/**
	 * Constructs a WaiterAgent
	 * @param host the host of the restaurant
	 * @param menu the menu to be used by the waiter
	 * @param name the name of the waiter
	 */
	public RestaurantWaiterRoleBrandon(HostBrandon host, Map<String,Double> menu, String name)
	{
		idNumber = ++instanceCount;
		this.host = host;
		customers = Collections.synchronizedList(new ArrayList<MyCustomer>());
		waitingForOrder = new Semaphore(0,true);
		this.menu = menu;
		animationPause = new Semaphore(0,true);
		billPause = new Semaphore(0,true);
		this.name = name;
		s = WaiterState.Normal;
	}
	
	/**
	 * Gets the name of the waiter
	 * @return the name of the waiter
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Sets the GUI display of the waiter
	 * @param gui the WaiterGui to use with the agent
	 */
	public void setGui(WaiterGuiBrandon gui)
	{
		this.gui = gui;
		this.gui.setInstanceNumber(idNumber);
	}
	
	/**
	 * Gets the GUI display being used for the waiter
	 * @return the WaiterGui to use with the agent
	 */
	public WaiterGuiBrandon getGui()
	{
		return gui;
	}
	
	/**
	 * Sets the chef referenced by the waiter
	 * @param chef the chef to be used by the waiter
	 */
	public void setChef(CookBrandon chef)
	{
		this.chef = chef;
	}
	
	/**
	 * Sets the chef referenced by the waiter
	 * @param chef the chef to be used by the waiter
	 */
	public void setCashier(CashierBrandon cash)
	{
		this.cashier = cash;
	}
	
	/**
	 * Message from checkbox on GUI signaling to go on break
	 */
	public void msgGotBreak()
	{
		System.out.println(this+": GUI said to go on break");
		s = WaiterState.WantsToBreak;
		stateChanged();
	}
	
	/**
	 * Message to seat a customer
	 * @param c the customer to seat
	 * @param table the table to go to
	 */
	public void msgSeatCustomer(CustomerBrandon c, int table)
	{
		System.out.println(this+": Seating customer at "+table);
		customers.add(new MyCustomer(c,table,CustomerState.Waiting));
		stateChanged();
	}
	
	/**
	 * Signals that a customer is ready to order
	 * @param c the customer who is ready
	 */
	public void msgReadyToOrder(CustomerBrandon c)
	{
		System.out.println(this+": "+c.getName()+" is ready to order");
		for(int i = 0; i < customers.size(); i++)
		{
			if(customers.get(i).c == c)
			{
				customers.get(i).s = CustomerState.Ready;
				stateChanged();
				return;
			}
		}
	}
	
	/**
	 * Messages the order from the customer
	 * @param c the customer who is ordering
	 * @param choice the choice of the customer
	 */
	public void msgGiveOrder(CustomerBrandon c, String choice)
	{
		System.out.println(this+": Got order "+choice+" from "+c.getName());
		for(int i = 0; i < customers.size(); i++)
		{
			if(customers.get(i).c == c)
			{
				customers.get(i).choice = choice;
			}
		}
		waitingForOrder.release();
		stateChanged();
	}
	
	/**
	 * Signals that a given food order is done cooking
	 * @param table the table at which the ordering customer sits
	 */
	public void msgOrderDone(int table)
	{
		System.out.println(this+": An order is done for table "+table);
		for(int i = 0; i < customers.size(); i++)
		{
			if(customers.get(i).t == table)
			{
				customers.get(i).s = CustomerState.FoodDone;
				stateChanged();
				return;
			}
		}
	}
	
	/**
	 * Signals that the given customer is done eating
	 * @param c the customer who ate
	 */
	public void msgDoneEating(CustomerBrandon c)
	{
		System.out.println(this+": A customer was done eating "+c.getName());
		for(int i = 0; i < customers.size(); i++)
		{
			if(customers.get(i).c == c)
			{
				customers.get(i).s = CustomerState.Done;
				stateChanged();
				return;
			}
		}
	}
	
	/**
	 * Message from the GUI that the waiter reached his destination
	 */
	public void msgReachedDestination()
	{
		System.out.println(this+": Animation done playing");
		animationPause.release();
	}
	
	/**
	 * Told that a customer's order is out
	 * @param choice the order that has run out
	 * @param table the table where the order occured
	 */
	public void msgOutOfFood(String choice, int table)
	{
		menu.remove(choice);
		System.out.println(this+"Received updated menu. Menu now has :"+menu);
		
		for(MyCustomer cust : customers)
		{
			if(cust.t == table)
			{
				cust.s = CustomerState.Reorder;
				cust.choice = "";
			}
		}
		stateChanged();
	}
	
	/**
	 * Told whether he can go on break
	 * @param b whether he can go on break
	 */
	public void msgCanGoOnBreak(boolean b)
	{
		System.out.println(this+": Can "+(b?"":"not")+" go on break");
		if(b)
		{
			s = WaiterState.CanBreak;
		}
		else
		{
			s = WaiterState.Denied;
		}
		stateChanged();
	}

	/**
	 * Message from the GUI to end break
	 */
	public void msgGoOffBreak()
	{
		System.out.println(this+": Going off break");
		s = WaiterState.ComingOffBreak;
		stateChanged();
	}
	
	/**
	 * Presents a bill to the waiter for giving to a customer
	 * @param cust the customer to give the bill to
	 * @param price the price to be paid
	 */
	public void msgGiveWaiterBill(CustomerBrandon cust, double price)
	{
		System.out.println(this+": Received bill from cashier");
		for(int i = 0; i < customers.size(); i++)
		{
			if(customers.get(i).c == cust)
			{
				customers.get(i).billAmount = price;
			}
		}
		billPause.release();
	}
	
	/**
	 * Informed that a customer is leaving
	 * @param cust the customer that has been given a bill
	 */
	public void msgCustomerLeaving(CustomerBrandon cust)
	{
		System.out.println(this+": Told that customer is leaving.");
		for(int i = 0; i < customers.size(); i++)
		{
			if(customers.get(i).c == cust)
			{
				customers.get(i).s = CustomerState.GivenBill;
			}
		}
		stateChanged();
	}
	
	@Override
	public boolean pickAndExecuteAnAction()
	{
		if(s == WaiterState.WantsToBreak)
		{
			System.out.println("Asking about break");
			askAboutBreak();
			return true;
		}
		else if(s == WaiterState.CanBreak && customers.size() == 0)
		{
			System.out.println("Can break!");
			goOnBreak();
			return true;
		}
		else if(s == WaiterState.ComingOffBreak)
		{
			comeOffBreak();
			return true;
		}
		else if(s == WaiterState.Denied)
		{
			processDenial();
			return true;
		}
		
		//Returns off-screen if there are no customers
		if(customers.size() == 0 && s != WaiterState.Break)
		{
			stayFrosty();
			return false;
		}
		
		//Finds if any assigned customers must reorder
		for(MyCustomer c : customers)
		{
			if(c.s == CustomerState.Reorder)
			{
				presentMenu(c);
				return true;
			}
		}
		//Finds if any assigned customers are waiting to be seated
		for(MyCustomer c : customers)
		{
			if(c.s == CustomerState.Waiting)
			{
				showCustomerToTable(c);
				return true;
			}
		}
			
		//Finds if any customers are ready to order
		for(MyCustomer c : customers)
		{
			if(c.s == CustomerState.Ready && c.choice.equals(""))
			{
				takeOrder(c);
				return true;
			}
		}
		//Finds if any customers' food has been completed
		for(MyCustomer c : customers)
		{
			if(c.s == CustomerState.FoodDone)
			{
				giveFood(c);
				return true;
			}
		}
		//Finds if any customers are done
		for(MyCustomer c : customers)
		{
			if(c.s == CustomerState.Done)
			{
				getBill(c);
				return true;
			}
		}
		
		for(MyCustomer c : customers)
		{
			if(c.s == CustomerState.GivenBill)
			{
				clearTable(c);
				return true;
			}
		}
		//Waiter goes and chills if there's nothing to do but he has customers
		if(customers.size() != 0)
		{
			stayFrosty();
			return false;
		}
		
		return false;
	}
	
	private void stayFrosty()
	{
		gui.doChillOut();
		System.out.println("CHILLING");
		try
		{
			animationPause.acquire();
		}
		catch(InterruptedException e)
		{
			System.out.println(this+": Problem pausing the animation.");
		}
		System.out.println("HELLO");
	}

	private void processDenial()
	{
		System.out.println(this+": My break was denied");
		s = WaiterState.Normal;
		gui.breakDenied();
	}

	private void comeOffBreak()
	{
		System.out.println(this+": I am coming off break");
		s = WaiterState.Normal;
		host.msgOffBreak(this);
	}

	private void goOnBreak()
	{
		s = WaiterState.Break;
		host.msgOnBreak(this);
		System.out.println(this+": I am now going on break");
		gui.doGoOnBreak();
		try
		{
			animationPause.acquire();
		}
		catch(InterruptedException e)
		{
			System.out.println(this+": Error with going to waiting area.");
		}
		
	}

	private void askAboutBreak()
	{
		System.out.println(this+": Asked about break");
		host.msgWantToBreak(this);
		s = WaiterState.AskedAboutBreak;
	}

	private void presentMenu(MyCustomer c)
	{
		System.out.println(this+": Asked "+c.c.getName()+" to reorder.");
		c.c.msgHereIsMenu(new MenuBrandon(menu));
		c.s = CustomerState.Seated;
	}

	private void showCustomerToTable(MyCustomer c)
	{
		gui.doGoToWaitingArea();
		try
		{
			animationPause.acquire();
		}
		catch(InterruptedException e)
		{
			System.out.println(this+": Error with going to waiting area.");
		}
		
		gui.doSeat(c.c,c.t);
		System.out.println(this+": Telling "+c.c.getName()+" to follow me");
		c.c.msgFollowMe(this,new MenuBrandon(menu).clone());
		try
		{
			animationPause.acquire();
		}
		catch(InterruptedException e)
		{
			System.out.println(this+": Error when showing customer to their seat.");
		}
		c.s = CustomerState.Seated;
	}
	
	private void takeOrder(MyCustomer c)
	{
		gui.doGoToTable(c.t);
		try
		{
			animationPause.acquire();
		}
		catch(InterruptedException e)
		{
			System.out.println(this+": Problem in Waiter takeOrder animation!");
		}
		System.out.println(this+": Taking order from "+c.c.getName());
		c.c.msgReadyToTakeOrder();
		try
		{
			System.out.println("BLOCKING ON WAITING FOR ORDER");
			waitingForOrder.acquire();
			System.out.println("DONE BLOCKING ON WAITING FOR ORDER");
		}
		catch(InterruptedException e)
		{
			System.out.println(this+": Error with waiter waiting for order.");
		}
		gui.doCarryOrder(c.choice);
		
		gui.doGoToKitchen(c.t);
		try
		{
			animationPause.acquire();
		}
		catch(InterruptedException e)
		{
			System.out.println(this+": Error with waiter going to kitchen");
		}
		
		System.out.println(this+": Giving order to cook");
		chef.msgPresentOrder(this, c.choice, c.t);
		
		gui.doDropCarry();
	}
	
	private void giveFood(MyCustomer c)
	{
		gui.doGoToKitchen(c.t);
		try
		{
			animationPause.acquire();
		}
		catch(InterruptedException e)
		{
			System.out.println(this+": Error with waiter going to kitchen to pick up food.");
		}
		gui.doCarryFood(c.choice);
		chef.msgPickingUpOrder(this,c.t);
		
		gui.doGoToTable(c.t);
		try
		{
			animationPause.acquire();
		}
		catch(InterruptedException e)
		{
			System.out.println(this+": Error with waiter going to table with food.");
		}
		
		System.out.println(this+": Presenting order to "+c.c.getName());
		c.c.msgPresentFood();
		c.s = CustomerState.Eating;
		
		gui.doDropCarry();
	}
	
	private void clearTable(MyCustomer c)
	{
		System.out.println(this+": Clearing table "+c.t);
		host.msgCustomerLeft(this,c.t);
		for(int i = 0; i < customers.size(); i++)
		{
			if(customers.get(i).c == c.c)
			{
				customers.remove(i);
				return;
			}
		}
	}
	
	private class MyCustomer
	{
		CustomerBrandon c;
		int t;
		CustomerState s;
		String choice;
		double billAmount;
		
		public MyCustomer(CustomerBrandon c, int table, CustomerState state)
		{
			this.c = c;
			t = table;
			s = state;
			choice = "";
		}
	}
	
	private void getBill(MyCustomer c)
	{
		System.out.println(this+": Going to get bill for "+c.c);
		
		gui.doGoToCashier();
		try
		{
			animationPause.acquire();
		}
		catch(InterruptedException e)
		{
			System.out.println(this+": Issue with animation.");
		}
		cashier.msgAskForBill(this, c.choice, c.t, c.c);
		
		try
		{
			billPause.acquire();
		}
		catch(InterruptedException e)
		{
			System.out.println(this+": Issue with animation.");
		}
		
		gui.doGoToTable(c.t);
		try
		{
			animationPause.acquire();
		}
		catch(InterruptedException e)
		{
			System.out.println(this+": Issue with animation.");
		}
		
		c.c.msgPresentBill(c.billAmount,cashier);
		c.s = CustomerState.GivenBill;
	}
	
	/**
	 * String representation of the waiter
	 * @return a string of the waiter
	 */
	public String toString()
	{
		return "Waiter "+getName();
	}
	
	@Override
	public void startInteraction(Intention intent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgClosingTime() {
		// TODO Auto-generated method stub
		
	}
}

	

