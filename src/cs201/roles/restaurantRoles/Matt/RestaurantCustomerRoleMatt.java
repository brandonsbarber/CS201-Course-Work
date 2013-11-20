package cs201.roles.restaurantRoles.Matt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import cs201.gui.roles.restaurant.Matt.CustomerGuiMatt;
import cs201.helper.Matt.MenuMatt;
import cs201.interfaces.roles.restaurant.RestaurantCustomerRole;
import cs201.interfaces.roles.restaurant.Matt.CashierMatt;
import cs201.interfaces.roles.restaurant.Matt.CustomerMatt;
import cs201.interfaces.roles.restaurant.Matt.HostMatt;
import cs201.interfaces.roles.restaurant.Matt.WaiterMatt;

import java.util.concurrent.Semaphore;

/**
 * Restaurant customer agent.
 */
public class RestaurantCustomerRoleMatt extends RestaurantCustomerRole implements CustomerMatt {
	private String name;
	private final int EATINGDURATION = 6500;
	private final int CHOOSINGDURATION = 2500;
	private final int WAITINGDURATION = 8000;
	private Timer customerTimer;
	private CustomerGuiMatt customerGui;

	// Agent correspondents
	private HostMatt host;
	private WaiterMatt waiter;
	private CashierMatt cashier;
	private MenuMatt menu;
	private String choice;
	
	private int attemptedOrders;
	private final double STARTINGMONEY = 15.00;
	private double money;
	private double checkAmount;
	
	private Semaphore atTargetPosition = new Semaphore(0, false); // used for animation

	private enum AgentState
	{ DoingNothing, WaitingInRestaurant, AboutToBeSeated, BeingSeated, readingMenu, Hailed, Ordering, Eating, WaitingForCheck, PayingCheck, Leaving };
	private AgentState state = AgentState.DoingNothing;

	private enum AgentEvent 
	{ none, gotHungry, waitedTooLong, followWaiter, seated, readyToOrder, cannotAffordAnything, tellWaiterOrder, foodArrived, doneEating, gotCheck, checkPaid };
	private AgentEvent event = AgentEvent.none;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 */
	public RestaurantCustomerRoleMatt(String name, RestaurantCashierRoleMatt cashier){
		super();
		this.name = name;
		this.cashier = cashier;
		this.money = STARTINGMONEY;
		this.checkAmount = 0;
		this.attemptedOrders = 0;
		
		switch (name) {
		case "broke": {
			this.money = 0;
			break;
		}
		case "cheap": {
			this.money = 5;
			break;
		}
		case "normal": {
			this.money = 12;
			break;
		}
		case "wealthy": {
			this.money = 1337;
			break;
		}
		}
	}

	public void setHost(HostMatt host) {
		this.host = host;
	}
	
	// Messages -------------------------------------------------------------
	@Override
	public void msgIsHungry() {
		System.out.println("Customer " + this.toString() + " is hungry.");
		event = AgentEvent.gotHungry;
		stateChanged();
	}
	
	@Override
	public void msgAboutToBeSeated() {
		state = AgentState.AboutToBeSeated;
		System.out.println("Customer " + name + " about to be seated.");
		stateChanged();
	}

	@Override
	public void msgFollowMeToTable(WaiterMatt waiter, MenuMatt menu) {
		this.waiter = waiter;
		event = AgentEvent.followWaiter;
		this.menu = menu;
		stateChanged();
	}

	@Override
	public void msgWhatWouldYouLike() {
		event = AgentEvent.tellWaiterOrder;
		stateChanged();
	}
	
	@Override
	public void msgHereIsYourFood(String choice) {
		event = AgentEvent.foodArrived;
		stateChanged();
	}
	
	@Override
	public void msgOutOfYourChoice(MenuMatt m) {
		this.menu = m;	
		event = AgentEvent.seated;
		state = AgentState.BeingSeated;
		stateChanged();
	}
	
	@Override
	public void msgHereIsYourCheck(double check) {
		checkAmount = check;
		event = AgentEvent.gotCheck;
		stateChanged();
	}
	
	@Override
	public void msgHereIsYourChange(double change) {
		money = change;
		event = AgentEvent.checkPaid;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine

		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry){
			state = AgentState.WaitingInRestaurant;
			GoToRestaurant();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.waitedTooLong) {
			state = AgentState.DoingNothing;
			WaitTimeTooLong();
			return true;
		}
		if (state == AgentState.AboutToBeSeated && event == AgentEvent.followWaiter){
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.seated){
			state = AgentState.readingMenu;
			ChooseFood();
			return true;
		}
		if (state == AgentState.readingMenu && event == AgentEvent.readyToOrder){
			state = AgentState.Hailed;
			HailWaiter();
			return true;
		}
		if (state == AgentState.readingMenu && event == AgentEvent.cannotAffordAnything){
			state = AgentState.Leaving;
			LeaveRestaurant();
			return true;
		}
		if (state == AgentState.Hailed && event == AgentEvent.tellWaiterOrder){
			state = AgentState.Ordering;
			OrderFood();
			return true;
		}
		if (state == AgentState.Ordering && event == AgentEvent.foodArrived){
			state = AgentState.Eating;
			EatFood();
			return true;
		}
		if (state == AgentState.Eating && event == AgentEvent.doneEating){
			state = AgentState.WaitingForCheck;
			AskForCheck();
			return true;
		}
		if (state == AgentState.WaitingForCheck && event == AgentEvent.gotCheck){
			state = AgentState.PayingCheck;
			PayCheck();
			return true;
		}
		if (state == AgentState.PayingCheck && event == AgentEvent.checkPaid){
			state = AgentState.Leaving;
			LeaveRestaurant();
			return true;
		}

		return false;
	}

	// Actions -------------------------------------------------------------
	private void GoToRestaurant() {
		DoGoToRestaurant();
		host.msgIWantToEat(this);
		DoGoToWaitingArea();
		DoWaitTimer();
	}
	
	private void WaitTimeTooLong() {
		host.msgWaitTimeTooLong(this);
		DoWaitTimeTooLong();
		event = AgentEvent.none;
		attemptedOrders = 0;
	}
	
	private void SitDown() {
		DoSitDown(); // contains animation
		event = AgentEvent.seated;
	}

	private void ChooseFood() {
		if (attemptedOrders >= 2) {
			System.out.println("Customer " + this.name + " tried to order twice and didn't receive anything, so he's leaving the restaurant.");
			LeaveRestaurant();
			return;
		}
		DoChooseFood();
		ChooseFoodTimer();
	}

	private void HailWaiter() {
		DoHailWaiter();
		waiter.msgReadyToOrder(this);
	}

	private void OrderFood() {
		DoOrderFood();
		attemptedOrders++;
		waiter.msgHereIsMyChoice(this, choice);
	}
	
	private void EatFood() {
		DoEatFood();
		EatFoodTimer();
	}
	
	private void AskForCheck() {
		DoAskForCheck();
		waiter.msgDoneEatingAndNeedCheck(this);
	}
	
	private void PayCheck() {
		DoPayCheck(); // contains animation
		cashier.msgPayCheck(this, money, checkAmount);
		money = 0;
	}
	
	private void LeaveRestaurant() {
		waiter.msgPayingAndLeaving(this);
		DoLeaveRestaurant(); // contains animation
		state = AgentState.DoingNothing;
		event = AgentEvent.none;
		attemptedOrders = 0;
	}

	// Utilities -------------------------------------------------------------
	private void DoGoToRestaurant() {
		System.out.println("Customer " + this.toString() + " going to restaurant.");
		customerGui.DoGoToRestaurant();
		try {
			atTargetPosition.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void DoGoToWaitingArea() {
		customerGui.DoGoToWaitingArea();
		try {
			atTargetPosition.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void DoWaitTimer() {
		customerTimer = new Timer(WAITINGDURATION, 
				new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				event = AgentEvent.waitedTooLong;
				stateChanged();
			}
		});
		customerTimer.setRepeats(false);
		customerTimer.start();
	}
	
	private void DoWaitTimeTooLong() {
		System.out.println("Customer " + this.name + " decided that he waited too long. He's leaving the restaurant.");
		
		customerGui.DoExitRestaurant();
		try {
			atTargetPosition.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Customer " + this.name + " has left the restaurant.");
	}
	
	private void DoSitDown() {
		System.out.println("Customer " + this.toString() + " is sitting down.");
		customerGui.Animate();
		try {
			atTargetPosition.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void DoChooseFood() {
		System.out.println("Customer " + this.toString() + " is choosing his food.");
		customerGui.setMessage("");
	}
	
	private void ChooseFoodTimer() {
		customerTimer = new Timer(CHOOSINGDURATION, 
				new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MenuMatt tempMenu = new MenuMatt();
				if (tempMenu.getPrice(name) != null) {
					choice = name;
				} else {
					choice = null;
					do {
						choice = menu.randomItem();
						if (menu.getPrice(choice) <= money) {
							break;
						}
						menu.removeItem(choice);
						choice = null;
					} while (menu.size() > 0);
					if (choice == null) {
						System.out.println("Customer " + name + " cannot afford anything! He is leaving the restaurant.");
						event = AgentEvent.cannotAffordAnything;
						stateChanged();
						return;
					}
				}
				
				event = AgentEvent.readyToOrder;
				stateChanged();
			}
		});
		customerTimer.setRepeats(false);
		customerTimer.start();
	}
	
	private void DoHailWaiter() {
		System.out.println("Customer " + this.toString() + " is hailing his waiter, " + waiter.toString() + ".");
	}
	
	private void DoOrderFood() {
		System.out.println("Customer " + this.toString() + " is ordering.");
		customerGui.setMessage(choice + "?");
	}
	
	private void DoEatFood() {
		System.out.println("Customer " + this.toString() + " is eating.");
		customerGui.setMessage(choice);
	}
	
	private void EatFoodTimer() {
		customerTimer = new Timer(EATINGDURATION, 
				new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				event = AgentEvent.doneEating;
				stateChanged();
			}
		});
		customerTimer.setRepeats(false);
		customerTimer.start();
	}
	
	private void DoAskForCheck() {
		System.out.println("Customer " + this.toString() + " is asking his waiter, " + waiter.toString() + ", for the bill.");
	}
	
	private void DoPayCheck() {
		System.out.println("Customer " + this.toString() + " is going to the Cashier, " + cashier.toString() + ", to pay his bill.");
		customerGui.DoGoToCashier();
		try {
			atTargetPosition.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void DoLeaveRestaurant() {
		System.out.println("Customer " + this.toString() + " is leaving the restaurant.");
		customerGui.DoExitRestaurant();
		try {
			atTargetPosition.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Customer " + this.toString() + " has left the restaurant.");
	}
	
	/**
	 * The CustomerGui informs the Customer that it is done animating to its destination, freeing up the CustomerAgent
	 */
	public void DoneAnimating() {
		atTargetPosition.release();
	}
	
	// Accessors -------------------------------------------------------------
	public void setGui(CustomerGuiMatt g) {
		customerGui = g;
	}

	@Override
	public CustomerGuiMatt getGui() {
		return customerGui;
	}
}

