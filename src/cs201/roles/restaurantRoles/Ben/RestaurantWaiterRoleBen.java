package cs201.roles.restaurantRoles.Ben;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Semaphore;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.roles.restaurant.Ben.WaiterGuiBen;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelBen;
import cs201.helper.Ben.RestaurantRotatingStandBen;
import cs201.interfaces.roles.restaurant.Ben.CashierBen;
import cs201.interfaces.roles.restaurant.Ben.CookBen;
import cs201.interfaces.roles.restaurant.Ben.CustomerBen;
import cs201.interfaces.roles.restaurant.Ben.HostBen;
import cs201.interfaces.roles.restaurant.Ben.WaiterBen;
import cs201.roles.restaurantRoles.RestaurantWaiterRole;
import cs201.trace.AlertLog;
import cs201.trace.AlertTag;

/**
 * Restaurant Host Agent
 */
public abstract class RestaurantWaiterRoleBen extends RestaurantWaiterRole implements WaiterBen {
	// A global for the number of tables.
	public static final int NTABLES = 5;
	boolean foodShouldBeReady = false;
	/*
	 * Notice that we implement waitingCustomers using ArrayList, but type it 
	 * with List semantics.
	 */
	public List<MyCustomer> customers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	
	/*
	 * Note that tables is typed with Collection semantics.
	 * Later we will see how it is implemented.
	 */
	public Collection<Table> tables;

	/*
	 * Properties of this waiter
	 */
	private String name;
	public int waiterNumber;

	private Semaphore animating = new Semaphore(0,true);

	/*
	 * Flags
	 */
	private Boolean okayToBreakAfterCustomers = false;
	private Boolean onBreak = false;
	private boolean closingTime = false;
	private Boolean talkingToCustomer = false;
	
	/*
	 * Connections to other instances 
	 */
	protected CookBen cook = null;
	private HostBen host = null;
	private CashierBen cashier = null;
	private RestaurantAnimationPanelBen animPanel = null;
	protected RestaurantRotatingStandBen stand;
	public WaiterGuiBen waiterGui = null;
	
	public RestaurantWaiterRoleBen() {
		this("");
	}
	
	public RestaurantWaiterRoleBen(String name) {
		super();

		this.name = name;
		
		// Make some tables
		tables = Collections.synchronizedList(new ArrayList<Table>(NTABLES));
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));
		}
	}
	
	/**
	 * Set connections to other instances
	 */

	public void setCook(CookBen c) {
		cook = c;
	}
	
	public void setHost(HostBen h) {
		host = h;
	}
	
	public void setCashier(CashierBen c) {
		cashier = c;
	}
	
	public void setAnimPanel(RestaurantAnimationPanelBen p) {
		animPanel = p;
	}
	
	public void setRotatingStand(RestaurantRotatingStandBen s) {
		stand = s;
	}
	
	/**
	 * Messages
	 */
	
	public void msgPleaseSeatCustomer(CustomerBen cust, int table) {
		customers.add(new MyCustomer(cust, table, CustomerState.waiting));
		stateChanged();
	}
	
	public void msgReadyToOrder(CustomerBen cust) {		
		// Find the customer in our list
		MyCustomer readyCustomer = null;
		synchronized(customers) {
			for (MyCustomer customer : customers) {
				if (customer.customer == cust) {
					readyCustomer = customer;
				}
			}
		}
		
		// Mark him as ready to order
		readyCustomer.state = CustomerState.readyToOrder;
		
		stateChanged();
	}
	
	public void msgOrderReady(String choice, int table) {
		
		// Find the customer in our list
		MyCustomer theCustomer = null;
		synchronized(customers) {
			for (MyCustomer customer : customers) {
				if (customer.table == table && customer.choice == choice) {
					theCustomer = customer;
				}
			}
		}
		
		// Mark his food as ready
		theCustomer.state = CustomerState.orderReady;
		foodShouldBeReady = true;
		
		stateChanged();
	}
	
	public void msgHereIsChoice(CustomerBen cust, String choice) {
		// Find the customer in our list
		MyCustomer orderingCustomer = null;
		synchronized(customers) {
			for (MyCustomer customer : customers) {
				if (customer.customer == cust) {
					orderingCustomer = customer;
				}
			}
		}
		
		// Take down his order
		orderingCustomer.choice = choice;
		orderingCustomer.state = CustomerState.ordered;
		
		// We're done talking for now
		talkingToCustomer = false;
		
		stateChanged();
	}

	public void msgLeavingTable(CustomerBen cust) {
		host.msgLeavingTable(cust);
		
		synchronized(customers) {
			for (MyCustomer mc : customers) {
				if (mc.customer == cust) {
					mc.state = CustomerState.finished;
					break;
				}
			}
		}
		
		for (Table table : tables) {
			if (table.getOccupant() == cust) {
				AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, "Waiter " + name, cust + " leaving table " + table);

				table.setUnoccupied();
			}
		}
		
		stateChanged();
	}
	
	// Sent by the cook to let the waiter know he's out of food
	public void msgOutOf(String choice, int table) {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, "Waiter " + name, cook.getName() + " just told me that he's out of " + choice);
		
		// Find the customer who ordered it
		MyCustomer customer = null;
		synchronized(customers) {
			for (MyCustomer mc : customers) {
				if (mc.table == table && mc.choice == choice)
					customer = mc;
			}
		}
		
		// Mark him as having to re-order
		customer.state = CustomerState.outOfOrder;
		
		stateChanged();
	}
	
	// Sent by the GUI
	public void msgGoOnBreak() {
		attemptToGoOnBreak();
	}
	
	public void msgContinueWorking() {
		okayToBreakAfterCustomers = false;
		onBreak = false;
		host.msgGoingBackToWork(this);
		waiterGui.WaiterWorking();
		
		stateChanged();
	}
	
	public void msgOkayForBreak() {
		// We're allowed to go on break! Once we finish our customers..
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, "Waiter " + name, "I'm allowed to go on break!");

		okayToBreakAfterCustomers = true;
		
		stateChanged();
	}
	
	public void msgCantGoOnBreak() {
		// We're not allowed to go on break. Oh well...
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, "Waiter " + name, "I'm not allowed to go on break...");

		okayToBreakAfterCustomers = false;
		
		stateChanged();
	}
	
	// Sent from the cashier when a customer's check is ready
	public void msgHereIsCheck(float amount, CustomerBen cust) {
		// Find the customer in our list of customers
		MyCustomer customer = null;
		synchronized(customers) {
			for (MyCustomer mc : customers) {
				if (mc.customer == cust)
					customer = mc;
			}
		}
		
		// Take down the check and mark that its ready to be brought to the customer
		customer.check = amount;
		customer.state = CustomerState.checkReady;
				
		stateChanged();
	}

	// Sent from the animation to let us know the animation has concluded
	public void msgAtDestination() {
		animating.release();
		//stateChanged();
	}
	
	public void msgClosingTime() {
		closingTime = true;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	
	public boolean pickAndExecuteAnAction() {
		System.out.println("Waiter scheduler about to be run...");
		if (foodShouldBeReady) System.out.println("About to run scheduler...");
		
		// Leave the restaurant when its time to close
		if (closingTime) {
			leaveRestaurant();
			return true;
		}
	
		// If we're on break, there's nothing to do
		if (onBreak) return false;
		
		// If we're talking to a customer, just go to sleep and wait for his message to wake us up
		if (talkingToCustomer) {
			return false;
		}

		// If there is a waiting customer, seat him
		synchronized(customers) {
			for (MyCustomer customer : customers) {
				if (customer.state == CustomerState.waiting) {
					seatCustomer(customer);
					return true;
				}
			}
		}
		
		// If there is a customer ready to order, walk to him
		synchronized(customers) {
			for (MyCustomer customer : customers) {
				if (customer.state == CustomerState.readyToOrder) {
					takeOrder(customer);
					return true;
				}
			}
		}
		
		// If there is a customer who has ordered, go place his order
		synchronized(customers) {
			for (MyCustomer customer : customers) {
				if (customer.state == CustomerState.ordered) {
					placeOrder(customer);
					return true;
				}
			}
		}
		
		// If there is a customer whose food is ready, bring him his order
		//if (foodShouldBeReady) Do("Checking for ready food...");
		synchronized(customers) {
			for (MyCustomer customer : customers) {
				if (customer.state == CustomerState.orderReady) {
					bringOrderToCustomer(customer);
					return true;
				}
			}
		}
		
		// If there is a customer whose check is ready, bring it to him
		synchronized(customers) {
			for (MyCustomer customer : customers) {
				if (customer.state == CustomerState.checkReady) {
					bringCheckToCustomer(customer);
					return true;
				}
			}
		}
		
		// If there is a customer whose order was out, let him re-order
		synchronized(customers) {
			for (MyCustomer customer : customers) {
				if (customer.state == CustomerState.outOfOrder) {
					tellCustomerOutOfFood(customer);
					return true;
				}
			}
		}
		
		// If we've finished our customers and we've been okayed to go on break, go on break
		if (okayToBreakAfterCustomers) {
			int customerCount = 0;
			synchronized(customers) {
				for (MyCustomer customer : customers) {
					if (customer.state != CustomerState.finished)
						customerCount++;
				}
			}
			if (customerCount == 0) {
				goOnBreak();
				return true;
			}
		}
		
		// If there is nothing else to do, return home
		DoReturnHome();

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	/**
	 * Actions
	 */

	private void seatCustomer(MyCustomer customer) {
		// First we need to return home to pick the customer up
		DoGoHome();
		
		// Let him know what table he'll be sitting at, and tell him to follow us
		int table = customer.table;
		customer.customer.msgFollowMeToTable(table, this, new Menu());
		
		DoSeatCustomer(customer.customer, table);
		customer.state = CustomerState.seated;
		
		// HACK - if the waiter is named "tired" he'll go on break
		if (name.toLowerCase().equals("tired")) {
			attemptToGoOnBreak();
		}
	}
	
	private void takeOrder(MyCustomer customer) {
		// Walk to the customer's table
		waiterGui.DoWalkToTable(customer.table);
		pauseForAnimation();
		
		// Ask the customer what he would like
		customer.customer.msgWhatWouldYouLike();
		customer.state = CustomerState.askedToOrder;
		
		// Don't just walk away from the customer!
		talkingToCustomer = true;
	}
	
	protected abstract void placeOrder(MyCustomer customer);
	
	private void bringOrderToCustomer(MyCustomer customer) {
		// First walk to the cook
		waiterGui.DoWalkToPlatingArea();		
		pauseForAnimation();
		
		// Change the waiter's icon to represent the food he's holding
		waiterGui.setIconText(customer.choice == "Chicken" ? "C" : "ST");
		
		// Remove the item from the plating area
		animPanel.platingArea.removeItem(customer.choice == "Chicken" ? "C" : "ST");
		
		// Now walk to the customer
		waiterGui.DoWalkToTable(customer.table);
		pauseForAnimation();
		
		// Give the customer his food
		customer.customer.msgHereIsYourFood();
		
		// Remove the waiter's icon
		waiterGui.removeIcon();
		
		// The customer is now eating
		customer.state = CustomerState.eating;
		
		// Give the cashier the customer's order to compute
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, "Waiter " + name, "Giving " + cashier.getName() + " the order to compute the check.");

		cashier.msgComputeCheckForOrder(customer.choice, this, customer.customer);
	}
	
	private void bringCheckToCustomer(MyCustomer customer) {
		// First, walk to the customer
		waiterGui.DoWalkToTable(customer.table);
		pauseForAnimation();
		
		// Let him know his check is ready
		customer.customer.msgHereIsCheck(customer.check);
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, "Waiter " + name, "Here is your check for $" + customer.check);
		
		// Mark him as having the check
		customer.state = CustomerState.hasCheck;
	}
	
	private void tellCustomerOutOfFood(MyCustomer customer) {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, "Waiter " + name, "Sorry, " + customer.customer.getName() + " but we're out of " + customer.choice);

		// First, walk to the customer
		waiterGui.DoWalkToTable(customer.table);
		pauseForAnimation();
		
		// Let him know the restaurant is out of his chosen food, and give him a new menu
		Menu newMenu = new Menu();
		newMenu.remove(customer.choice);	// remove his previous choice
		customer.customer.msgOutOf(newMenu);
		
		// Mark him as seated
		customer.state = CustomerState.seated;
	}
	
	private void attemptToGoOnBreak() {
		// Let the host know we're trying to go on break
		host.msgWantToGoOnBreak(this);
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, "Waiter " + name, "I'd like to go on break. Letting " + host.getName() + " know");
	}
	
	private void goOnBreak() {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, "Waiter " + name, "Going on break!");
		
		waiterGui.WaiterOnBreak();
		onBreak = true;
		// Take a smoke break
		waiterGui.DoWalkToBreakArea();		
		pauseForAnimation();
	}
	
	private void leaveRestaurant() {
		this.isActive = false;
		this.myPerson.goOffWork();
		this.myPerson.removeRole(this);
		this.myPerson = null;
		DoLeaveRestaurant();
		this.waiterGui.setPresent(false);
	}
	
	/**
	 * Animation routines
	 */
	
	private void DoSeatCustomer(CustomerBen customer, int table) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, "Waiter " + name, "Seating " + customer + " at " + table);

		waiterGui.DoWalkToTable(table); 
		pauseForAnimation();
		System.out.println("Finished pausing for animation...");

	}
	
	// Call this method if the waiter is returning home out of boredom (nothing else to do)
	private void DoReturnHome() {
		waiterGui.DoWalkToHomeIfBored();
	}
	
	// Call this method if the waiter needs to return home to pick someone up
	private void DoGoHome() {
		waiterGui.DoWalkToHome();
		pauseForAnimation();
	}
	
	private void DoLeaveRestaurant() {
		waiterGui.DoLeaveRestaurant();
	}
	
	protected void pauseForAnimation() {
		try {
			animating.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Utilities
	 */
	
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public Collection getTables() {
		return tables;
	}

	public void setGui(WaiterGuiBen gui) {
		waiterGui = gui;
	}

	public WaiterGuiBen getGui() {
		return waiterGui;
	}
	
	public Boolean onBreak() {
		
		return onBreak;
	}

	public class Menu {
		Map<String, Float> choices = new HashMap<String, Float>();
		
		public Menu() {
			choices.put("Chicken", 6.99f);
			choices.put("Steak", 11.99f);
		}
		public String getRandom() {
			Random random = new Random();
			List<String> keys = new ArrayList<String>(choices.keySet());
			String randomKey = keys.get(random.nextInt(keys.size()));
			return randomKey;
		}
		public String itemUnderPrice(float price) {
			List<String> affordableItems = new ArrayList<String>();
			Iterator it = choices.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pairs = (Map.Entry)it.next();
				if ((Float)pairs.getValue() <= price)
					affordableItems.add((String)pairs.getKey());
			}
			Random random = new Random();
			if (affordableItems.size() == 0) return null;
			return affordableItems.get(random.nextInt(affordableItems.size()));
		}
		public Boolean itemOnMenu(String item) {
			Float thePrice = choices.get(item);
			return (thePrice != null);
		}
		public Float getPrice(String item) {
			return choices.get(item);
		}
		public void remove(String foodItem) {
			choices.remove(foodItem);
		}
	}
	
	private class Table {
		CustomerBen occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(CustomerBen cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		CustomerBen getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
	}
	
	// A class to keep track of our customers
	protected enum CustomerState {waiting, seated, readyToOrder, askedToOrder, ordered, 
								placedOrder, outOfOrder, orderReady, eating, checkReady,
								hasCheck, finished};
	protected class MyCustomer {
		CustomerBen customer;
		int table;
		String choice;
		CustomerState state;
		float check;
		
		public MyCustomer(CustomerBen c, int t, CustomerState s) {
			customer = c;
			state = s;
			table = t;
		}
	}

	public void setWaiterNumber(int number) {
		waiterNumber = number;
	}

	public void startInteraction(Intention intent) {
		waiterGui.DoEnterRestaurant();
		this.waiterGui.setPresent(true);
		closingTime = false;
	}

	public int getWaiterNumber() {
		return waiterNumber;
	}
}

