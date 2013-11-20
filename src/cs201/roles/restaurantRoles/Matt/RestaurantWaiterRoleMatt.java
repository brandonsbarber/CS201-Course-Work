package cs201.roles.restaurantRoles.Matt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.Timer;

import cs201.gui.roles.restaurant.Matt.WaiterGuiMatt;
import cs201.helper.Matt.MenuMatt;
import cs201.helper.Matt.TableMatt;
import cs201.interfaces.roles.restaurant.RestaurantWaiterRole;
import cs201.interfaces.roles.restaurant.Matt.CustomerMatt;
import cs201.interfaces.roles.restaurant.Matt.WaiterMatt;

import java.util.concurrent.Semaphore;

/**
 * Restaurant Waiter Agent
 */
public class RestaurantWaiterRoleMatt extends RestaurantWaiterRole implements WaiterMatt {
	private RestaurantCookRoleMatt cook;
	private RestaurantHostRoleMatt host;
	private RestaurantCashierRoleMatt cashier;
	private Semaphore atTargetPosition = new Semaphore(0); // used for animation
	private WaiterGuiMatt waiterGui;
	private List<MyCustomer> myCustomers;
	private enum CustomerState { none, waiting, readyToOrder, ordered, foodReady, outOfFood, askedForCheck, waiterHasCheck, leaving };
	private enum WaiterState { none, askingForBreak, waitingForResponse, onBreak, breakNotAllowed, breakOver };
	private WaiterState state = WaiterState.none;
	private Timer breakTimer;
	private final int BREAKTIME = 15000; // 15 seconds
	

	public RestaurantWaiterRoleMatt(RestaurantCookRoleMatt cook, RestaurantHostRoleMatt host, RestaurantCashierRoleMatt cashier) {
		super();

		this.cook = cook;
		this.host = host;
		this.cashier = cashier;
		myCustomers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	}
	
	// Messages -------------------------------------------------------------
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
	private void AskForBreak() {
		DoAskForBreak();
		state = WaiterState.waitingForResponse;
		host.msgPermissionForBreak(this);
	}
	
	private void GoOnBreak() {
		DoGoOnBreak();
		BreakTimer();
	}
	
	private void GoOffBreak() {
		DoGoOffBreak();
		state = WaiterState.none;
		host.msgOffBreak(this);
	}
	
	private void SeatCustomer(MyCustomer m) {
		DoGoToCustomerWaiting(m);
		m.customer.msgFollowMeToTable(this, new MenuMatt());
		host.msgCustomerRetrievedFromWaitingArea();
		DoSeatCustomer(m);
		m.state = CustomerState.none;
	}
	
	private void TakeCustomerOrder(MyCustomer m) {
		DoTakeCustomerOrder(m);
		m.customer.msgWhatWouldYouLike();
		m.state = CustomerState.none;
	}
	
	private void GiveOrderToCook(MyCustomer m) {
		m.state = CustomerState.none;
		DoGiveOrderToCook(m);
		cook.msgHereIsAnOrder(this, m.choice, m.tableNumber);
	}
	
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
		cashier.msgComputeCheck(this, m.customer, m.choice);
	}
	
	private void GiveCustomerCheck(MyCustomer m) {
		DoGiveCustomerCheck(m);
		m.state = CustomerState.none;
		m.customer.msgHereIsYourCheck(m.checkAmount);
	}
	
	private void CustomerLeaving(MyCustomer m) {
		DoCustomerLeaving(m);
		host.msgTableIsFree(this, m.tableNumber);
		myCustomers.remove(m);
	}

	// Utilities -------------------------------------------------------------
	private void DoAskForBreak() {
		System.out.println("Waiter " + this.toString() + " asking host " + host.getName() + " to go on break.");
		waiterGui.setWaitingForBreakResponse();
	}
	
	private void DoGoOnBreak() {
		System.out.println("Waiter " + this.toString() + " going on break.");
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
		System.out.println("Waiter " + this.toString() + "'s break is over. Returning to work.");
	}
	
	private void DoBreakNotAllowed() {
		System.out.println("Waiter " + this.toString() + " says his break was denied. :(");
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
	
	private void DoGoToCustomer(MyCustomer m) {
		waiterGui.GoToCustomer(m.customer);
		try {
			atTargetPosition.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void DoSeatCustomer(MyCustomer m) {
		System.out.println("Waiter " + this.toString() + " seating customer " + m.customer.toString() + ".");	
		
		for (TableMatt t : host.getTables()) {
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
		System.out.println("Waiter " + this.toString() + " taking customer " + m.customer.toString() + "'s order.");
		waiterGui.GoToCustomer(m.customer);
		try {
			atTargetPosition.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void DoGiveOrderToCook(MyCustomer m) {
		System.out.println("Waiter " + this.toString() + " giving customer " + m.customer.toString() + "'s order to the cook.");
		waiterGui.GoToLocation(cs201.gui.structures.restaurant.RestaurantAnimationPanelMatt.COOKINGAREA_X,
								cs201.gui.structures.restaurant.RestaurantAnimationPanelMatt.COOKINGAREA_Y);
		try {
			atTargetPosition.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void DoRetakeCustomerOrder(MyCustomer m) {
		System.out.println("Waiter " + this.toString() + " retaking customer " + m.customer.toString() + "'s order");
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
		System.out.println("Waiter " + this.toString() + " bringing food to customer " + m.customer.toString() + ".");
		waiterGui.GoToLocation(cs201.gui.structures.restaurant.RestaurantAnimationPanelMatt.PLATINGAREA_X,
								cs201.gui.structures.restaurant.RestaurantAnimationPanelMatt.PLATINGAREA_Y);
		try {
			atTargetPosition.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		cook.msgOrderPickedUp(m.choice);
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
		System.out.println("Waiter " + this.toString() + " getting check for " + m.customer.toString() + ".");
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
		System.out.println("Waiter " + this.toString() + " retrieving check from " + cashier.toString() + " for customer " + m.customer.toString() + ".");
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
		System.out.println("Waiter " + this.toString() + " telling host that customer " + m.customer.toString() + " is leaving.");
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
	 * The WaiterGui tells this WaiterAgent that it has reached its destination, freeing up this WaiterAgent
	 * to continue working
	 */
	public void DoneAnimating() {
		atTargetPosition.release();
	}
	
	private class MyCustomer {
		private CustomerMatt customer;
		private int tableNumber;
		private String choice;
		private CustomerState state;
		private double checkAmount;
		
		public MyCustomer(CustomerMatt customer, int tableNum) {
			this.customer = customer;
			this.tableNumber = tableNum;
			this.state = CustomerState.waiting;
			this.choice = null;
			this.checkAmount = 0;
		}
	}
}

