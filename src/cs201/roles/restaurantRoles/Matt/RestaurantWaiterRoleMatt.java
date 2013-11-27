package cs201.roles.restaurantRoles.Matt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.swing.Timer;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.roles.restaurant.Matt.WaiterGuiMatt;
import cs201.helper.Matt.MenuMatt;
import cs201.helper.Matt.RestaurantRotatingStand;
import cs201.helper.Matt.TableMatt;
import cs201.interfaces.roles.restaurant.Matt.CustomerMatt;
import cs201.interfaces.roles.restaurant.Matt.WaiterMatt;
import cs201.roles.restaurantRoles.RestaurantWaiterRole;

/**
 * Restaurant Waiter Agent
 */
public abstract class RestaurantWaiterRoleMatt extends RestaurantWaiterRole implements WaiterMatt {
	private Semaphore atTargetPosition = new Semaphore(0); // used for animation
	private WaiterGuiMatt waiterGui;
	private List<MyCustomer> myCustomers;
	protected enum CustomerState { none, waiting, readyToOrder, ordered, foodReady, outOfFood, askedForCheck, waiterHasCheck, leaving };
	private enum WaiterState { none, askingForBreak, waitingForResponse, onBreak, breakNotAllowed, breakOver };
	private WaiterState state = WaiterState.none;
	private Timer breakTimer;
	private final int BREAKTIME = 15000; // 15 seconds
	private boolean closingTime = false;
	protected RestaurantRotatingStand stand;
	

	public RestaurantWaiterRoleMatt(RestaurantCookRoleMatt cook, RestaurantHostRoleMatt host, RestaurantCashierRoleMatt cashier) {
		super();

		myCustomers = Collections.synchronizedList(new ArrayList<MyCustomer>());
		this.stand = null;
	}
	
	public RestaurantWaiterRoleMatt() {
		super();

		myCustomers = Collections.synchronizedList(new ArrayList<MyCustomer>());
		this.stand = null;
	}
	
	// Messages -------------------------------------------------------------
	@Override
	public void msgClosingTime() {
		closingTime = true;
		stateChanged();
	}
	
	@Override
	public void msgSeatCustomer(int tNum, CustomerMatt c) {
		myCustomers.add(new MyCustomer(c, tNum));
		stateChanged();
	}
	
	@Override
	public void msgReadyToOrder(CustomerMatt c) {
		for (MyCustomer m : myCustomers) {
			if (m.customer == c) {
				m.state = CustomerState.readyToOrder;
				stateChanged();
				return;
			}
		}
	}
	
	@Override
	public void msgHereIsMyChoice(CustomerMatt c, String choice) {
		for (MyCustomer m : myCustomers) {
			if (m.customer == c) {
				m.choice = choice;
				m.state = CustomerState.ordered;
				stateChanged();
				return;
			}
		}
	}
	
	@Override
	public void msgOrderIsReady(String choice, int tableNum) {
		for (MyCustomer m : myCustomers) {
			if (m.tableNumber == tableNum) {
				m.state = CustomerState.foodReady;
				stateChanged();
				return;
			}
		}
	}
	
	@Override
	public void msgOutOfFood(String choice, int tableNum) {
		for (MyCustomer m : myCustomers) {
			if (m.tableNumber == tableNum) {
				m.state = CustomerState.outOfFood;
				stateChanged();
				return;
			}
		}
	}
	
	@Override
	public void msgDoneEatingAndNeedCheck(CustomerMatt c) {
		for (MyCustomer m : myCustomers) {
			if (m.customer == c) {
				m.state = CustomerState.askedForCheck;
				stateChanged();
				return;
			}
		}
	}
	
	@Override
	public void msgHereIsCheck(CustomerMatt c, double check) {
		for (MyCustomer m : myCustomers) {
			if (m.customer == c) {
				m.state = CustomerState.waiterHasCheck;
				m.checkAmount = check;
				stateChanged();
				return;
			}
		}
	}
	
	@Override
	public void msgPayingAndLeaving(CustomerMatt c) {
		for (MyCustomer m : myCustomers) {
			if (m.customer == c) {
				m.state = CustomerState.leaving;
				stateChanged();
				return;
			}
		}
	}
	
	@Override
	public void msgAskToGoOnBreak() {
		state = WaiterState.askingForBreak;
		stateChanged();
	}
	
	@Override
	public void msgBreakAllowed(boolean breakAllowed) {
		if (breakAllowed) {
			state = WaiterState.onBreak;
		} else {
			state = WaiterState.breakNotAllowed;
		}
		
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		if (closingTime) {
			LeaveRestaurant();
			return true;
		}
		
		if (state == WaiterState.askingForBreak) {
			AskForBreak();
			return true;
		}
		if (state == WaiterState.breakOver) {
			GoOffBreak();
			return true;
		}
		if (state == WaiterState.breakNotAllowed) {
			DoBreakNotAllowed();
			return true;
		}
		
		try {
			for (MyCustomer m : myCustomers) {
				if (m.state == CustomerState.waiting) {
					SeatCustomer(m);
					return true;
				}
			}
			for (MyCustomer m : myCustomers) {
				if (m.state == CustomerState.readyToOrder) {
					TakeCustomerOrder(m);
					return true;
				}
			}
			for (MyCustomer m : myCustomers) {
				if (m.state == CustomerState.ordered) {
					GiveOrderToCook(m);
					return true;
				}
			}
			for (MyCustomer m : myCustomers) {
				if (m.state == CustomerState.outOfFood) {
					RetakeCustomerOrder(m);
					return true;
				}
			}
			for (MyCustomer m : myCustomers) {
				if (m.state == CustomerState.foodReady) {
					FeedCustomer(m);
					return true;
				}
			}
			for (MyCustomer m : myCustomers) {
				if (m.state == CustomerState.askedForCheck) {
					GetCustomerCheck(m);
					return true;
				}
			}
			for (MyCustomer m : myCustomers) {
				if (m.state == CustomerState.waiterHasCheck) {
					GiveCustomerCheck(m);
					return true;
				}
			}
			for (MyCustomer m : myCustomers) {
				if (m.state == CustomerState.leaving) {
					CustomerLeaving(m);
					return true;
				}
			}
			
			if (myCustomers.isEmpty() && state == WaiterState.onBreak) {
				GoOnBreak();
				return false;
			}
		} catch (ConcurrentModificationException e) {
			return true;
		}
		
		DoGoToEntrance();
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions -------------------------------------------------------------
	private void LeaveRestaurant() {
		this.isActive = false;
		this.myPerson.goOffWork();
		this.myPerson.removeRole(this);
		this.myPerson = null;
		DoLeaveRestaurant();
		this.waiterGui.setPresent(false);
	}
	
	private void AskForBreak() {
		DoAskForBreak();
		state = WaiterState.waitingForResponse;
		((RestaurantHostRoleMatt) restaurant.getHost()).msgPermissionForBreak(this);
	}
	
	private void GoOnBreak() {
		DoGoOnBreak();
		BreakTimer();
	}
	
	private void GoOffBreak() {
		DoGoOffBreak();
		state = WaiterState.none;
		((RestaurantHostRoleMatt) restaurant.getHost()).msgOffBreak(this);
	}
	
	private void SeatCustomer(MyCustomer m) {
		DoGoToCustomerWaiting(m);
		m.customer.msgFollowMeToTable(this, new MenuMatt());
		((RestaurantHostRoleMatt) restaurant.getHost()).msgCustomerRetrievedFromWaitingArea();
		DoSeatCustomer(m);
		m.state = CustomerState.none;
	}
	
	private void TakeCustomerOrder(MyCustomer m) {
		DoTakeCustomerOrder(m);
		m.customer.msgWhatWouldYouLike();
		m.state = CustomerState.none;
	}
	
	protected abstract void GiveOrderToCook(MyCustomer m);
	
	private void RetakeCustomerOrder(MyCustomer m) {
		DoRetakeCustomerOrder(m);
		MenuMatt menu = new MenuMatt();
		menu.removeItem(m.choice);
		m.customer.msgOutOfYourChoice(menu);
		m.state = CustomerState.none;
	}
	
	private void FeedCustomer(MyCustomer m) {
		DoFeedCustomer(m);
		m.state = CustomerState.none;
		m.customer.msgHereIsYourFood(m.choice);
	}
	
	private void GetCustomerCheck(MyCustomer m) {
		DoGetCustomerCheck(m);
		m.state = CustomerState.none;
		((RestaurantCashierRoleMatt) this.restaurant.getCashier()).msgComputeCheck(this, m.customer, m.choice);
	}
	
	private void GiveCustomerCheck(MyCustomer m) {
		DoGiveCustomerCheck(m);
		m.state = CustomerState.none;
		m.customer.msgHereIsYourCheck(m.checkAmount);
	}
	
	private void CustomerLeaving(MyCustomer m) {
		DoCustomerLeaving(m);
		((RestaurantHostRoleMatt) restaurant.getHost()).msgTableIsFree(this, m.tableNumber);
		myCustomers.remove(m);
	}

	// Utilities -------------------------------------------------------------
	private void DoLeaveRestaurant() {
		// TODO leave restaurant animation
		Do("Leaving work.");
	}
	
	private void DoAskForBreak() {
		Do("Asking " + (RestaurantHostRoleMatt) restaurant.getHost() + " to go on break.");
		waiterGui.setWaitingForBreakResponse();
	}
	
	private void DoGoOnBreak() {
		Do("Going on break.");
		waiterGui.GoToBreakPosition();
	}
	
	private void BreakTimer() {
		breakTimer = new Timer(BREAKTIME,
				new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				state = WaiterState.breakOver;
				waiterGui.setOffBreak();
				stateChanged();
			}
		});
		breakTimer.setRepeats(false);
		breakTimer.start();
	}
	
	private void DoGoOffBreak() {
		Do("Break is over. Returning to work.");
	}
	
	private void DoBreakNotAllowed() {
		Do("Break was denied. :(");
		state = WaiterState.none;
		waiterGui.setOffBreak();
	}
	
	private void DoGoToCustomerWaiting(MyCustomer m) {
		waiterGui.GoToCustomerWaiting(m.customer);
		try {
			atTargetPosition.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void DoSeatCustomer(MyCustomer m) {
		Do("Seating " + m.customer.toString() + ".");
		
		for (TableMatt t : ((RestaurantHostRoleMatt) restaurant.getHost()).getTables()) {
			if (t.tableNum() == m.tableNumber) {
				m.customer.getGui().DoGoToSeat(t.X(), t.Y());
				waiterGui.GoToLocation(t.X(), t.Y());
				break;
			}
		}
		
		try {
			atTargetPosition.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void DoTakeCustomerOrder(MyCustomer m) {
		Do("Taking " + m.customer.toString() + "'s order.");
		waiterGui.GoToCustomer(m.customer);
		try {
			atTargetPosition.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected void DoGiveOrderToCook(MyCustomer m) {
		Do("Giving " + m.customer.toString() + "'s order to the cook.");
		waiterGui.GoToLocation(cs201.gui.structures.restaurant.RestaurantAnimationPanelMatt.COOKINGAREA_X,
								cs201.gui.structures.restaurant.RestaurantAnimationPanelMatt.COOKINGAREA_Y);
		waiterGui.setMessage(m.choice + "?");
		try {
			atTargetPosition.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		waiterGui.setMessage("");
	}
	
	protected void DoPutOrderOnStand(MyCustomer m) {
		Do("Putting " + m.customer.toString() + "'s order on rotating stand.");
		waiterGui.GoToLocation(cs201.helper.Matt.RestaurantRotatingStand.STANDX,
								cs201.helper.Matt.RestaurantRotatingStand.STANDY);
		waiterGui.setMessage(m.choice + "?");
		try {
			atTargetPosition.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		waiterGui.setMessage("");
	}
	
	private void DoRetakeCustomerOrder(MyCustomer m) {
		Do("Retaking " + m.customer.toString() + "'s order");
		waiterGui.setMessage("!" + m.choice + "!");
		waiterGui.GoToCustomer(m.customer);
		try {
			atTargetPosition.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		waiterGui.setMessage("");
	}
	
	private void DoFeedCustomer(MyCustomer m) {
		Do("Bringing food to " + m.customer.toString() + ".");
		waiterGui.GoToLocation(cs201.gui.structures.restaurant.RestaurantAnimationPanelMatt.PLATINGAREA_X,
								cs201.gui.structures.restaurant.RestaurantAnimationPanelMatt.PLATINGAREA_Y);
		try {
			atTargetPosition.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		((RestaurantCookRoleMatt) restaurant.getCook()).msgOrderPickedUp(m.choice);
		waiterGui.GoToCustomer(m.customer);
		waiterGui.setMessage(m.choice);
		try {
			atTargetPosition.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		waiterGui.setMessage("");
	}
	
	private void DoGetCustomerCheck(MyCustomer m) {
		Do("Getting check for " + m.customer.toString() + ".");
		waiterGui.GoToCustomer(m.customer);
		waiterGui.setMessage("");
		try {
			atTargetPosition.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		waiterGui.GoToLocation(cs201.gui.roles.restaurant.Matt.CashierGuiMatt.CASHIERX,
				cs201.gui.roles.restaurant.Matt.CashierGuiMatt.CASHIERY + cs201.gui.roles.restaurant.Matt.CashierGuiMatt.CASHIERSIZE);
		waiterGui.setMessage("$$");
		try {
			atTargetPosition.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		waiterGui.setMessage("");
	}
	
	private void DoGiveCustomerCheck(MyCustomer m) {
		Do("Retrieving check from " + ((RestaurantCashierRoleMatt) this.restaurant.getCashier()).toString() + " for customer " + m.customer.toString() + ".");
		waiterGui.GoToLocation(cs201.gui.roles.restaurant.Matt.CashierGuiMatt.CASHIERX,
								cs201.gui.roles.restaurant.Matt.CashierGuiMatt.CASHIERY + cs201.gui.roles.restaurant.Matt.CashierGuiMatt.CASHIERSIZE);
		waiterGui.setMessage("");
		try {
			atTargetPosition.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		waiterGui.GoToCustomer(m.customer);
		waiterGui.setMessage("$$");
		try {
			atTargetPosition.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		waiterGui.setMessage("");
	}
	
	private void DoCustomerLeaving(MyCustomer m) {
		Do("Telling host that " + m.customer.toString() + " is leaving.");
	}
	
	private void DoGoToEntrance() {
		waiterGui.GoToWaitingPosition();
	}
	
	// Utilities -------------------------------------------------------------
	/**
	 * Sets this WaiterAgent's gui with a WaiterGui
	 * @param g The WaiterGui to be set
	 */
	public void setGui(WaiterGuiMatt g) {
		waiterGui = g;
	}

	/**
	 * Returns this WaiterAgent's gui
	 * @return A WaiterGui
	 */
	public WaiterGuiMatt getGui() {
		return waiterGui;
	}
	
	/**
	 * Sets this Waiter's reference to the Restaurant's rotating stand
	 * @param stand The Rotating Stand
	 */
	public void setRotatingStand(RestaurantRotatingStand stand) {
		this.stand = stand;
	}
	
	/**
	 * The WaiterGui tells this WaiterAgent that it has reached its destination, freeing up this WaiterAgent
	 * to continue working
	 */
	public void DoneAnimating() {
		atTargetPosition.release();
	}
	
	protected class MyCustomer {
		CustomerMatt customer;
		int tableNumber;
		String choice;
		CustomerState state;
		double checkAmount;
		
		public MyCustomer(CustomerMatt customer, int tableNum) {
			this.customer = customer;
			this.tableNumber = tableNum;
			this.state = CustomerState.waiting;
			this.choice = null;
			this.checkAmount = 0;
		}
	}

	@Override
	public void startInteraction(Intention intent) {
		// TODO maybe animate into restaurant?
		this.waiterGui.setPresent(true);
		closingTime = false;
	}

}

