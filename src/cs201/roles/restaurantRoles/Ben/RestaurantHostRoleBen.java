package cs201.roles.restaurantRoles.Ben;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import cs201.agents.PersonAgent.Intention;
import cs201.interfaces.roles.restaurant.Ben.CustomerBen;
import cs201.interfaces.roles.restaurant.Ben.HostBen;
import cs201.interfaces.roles.restaurant.Ben.WaiterBen;
import cs201.roles.restaurantRoles.RestaurantHostRole;
import cs201.trace.AlertLog;
import cs201.trace.AlertTag;

/**
 * Restaurant Host Agent
 * A Host is the manager of a restaurant who sees that all is proceeded as he wishes.
 */
public class RestaurantHostRoleBen extends RestaurantHostRole implements HostBen {
	public static final int NTABLES = 5;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<MyCustomer> customers = new ArrayList<MyCustomer>();
	
	// A list of waiters
	public List<MyWaiter> waiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
	
	public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented

	private String name;

	//public WaiterGui hostGui = null;
	
	private boolean closingTime = false;
	
	public RestaurantHostRoleBen() {
		this("");
	}

	public RestaurantHostRoleBen(String name) {
		super();

		this.name = name;
		// make some tables
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
		
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	/*
	public List getWaitingCustomers() {
		return customers;
	}
	*/

	public Collection getTables() {
		return tables;
	}
	
	/**
	 * Messages
	 */

	public void addWaiter(WaiterBen waiter) {
		MyWaiter newWaiter = new MyWaiter(waiter, WaiterState.working);
		waiters.add(newWaiter);
		
		// Let the waiter know what number it is in our list, so it can let its gui know
		// for positioning purposes
		newWaiter.waiter.setWaiterNumber(waiters.size() - 1);
		
		stateChanged();
	}

	public void msgIWantFood(CustomerBen cust) {
		// Add the hungry customer to our list, but as a MyCustomer, so we can keep track of him
		// His initial state is "waiting"
		customers.add(new MyCustomer(cust, CustomerState.arrived));
		stateChanged();
	}
	
	public void msgICantWait(CustomerBen cust) {
		// Find the customer
		MyCustomer mc = null;
		synchronized(customers) {
			for (MyCustomer customer : customers) {
					if (customer.customer == cust)
					mc = customer;
			}
		}
		
		mc.state = CustomerState.finished;
		stateChanged();
	}

	public void msgLeavingTable(CustomerBen cust) {
		// Mark the table as free
		for (Table table : tables) {
			if (table.getOccupant() == cust) {
				AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, "Host " + name, cust + " leaving table " + table);
				
				table.setUnoccupied();
			}
		}
		
		// The waiter is responsible for one less person
		synchronized(customers) {
			for (MyCustomer mc : customers) {
				if (mc.customer == cust) {
					mc.waiter.customerCount--;
					mc.state = CustomerState.finished;
				}
			}
		}
		
		
		stateChanged();

	}
	
	public void msgWantToGoOnBreak(WaiterBen w) {
		// The waiter wants to go on break, so mark him as such
		MyWaiter waiter = null;
		synchronized(waiters) {
			for (MyWaiter mw : waiters) {
				if (mw.waiter == w)
					waiter = mw;
			}
		}
		waiter.state = WaiterState.wantsABreak;
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, "Host " + name, waiter.waiter.getName() + " wants to go on break.");

		stateChanged();
	}
	
	public void msgGoingBackToWork(WaiterBen w) {
		// The waiter is done with his break
		MyWaiter waiter = null;
		synchronized(waiters) {
			for (MyWaiter mw : waiters) {
				if (mw.waiter == w)
					waiter = mw;
			}
		}
		waiter.state = WaiterState.working;
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, "Host " + name, waiter.waiter.getName() + " is going back to work.");
		
		stateChanged();
	}
	
	public void msgClosingTime() {
		closingTime = true;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	
	public boolean pickAndExecuteAnAction() {
		/*
		for (Table table : tables) {
			if (!table.isOccupied()) {
				for (MyCustomer customer : customers) {
					if (customer.state == CustomerState.waiting) {
						seatCustomer(customer, table);
						return true;
					}
				}
			}
		}
		*/
		
		// If it is time to close and we don't have any more customers, we can close the restaurant
		if (closingTime && getNumberOfActiveCustomers() == 0) {
			CloseRestaurant();
			return true;
		} else if (closingTime) {
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, "Host " + name, "It's time to close, but I can't shut the restaurant down yet. We have " + getNumberOfActiveCustomers() + " customer(s).");
		}

		/* Think of this next rule as:
        Does there exist a table and customer,
        so that table is unoccupied and customer is waiting.
        If so seat him at the table.
		 */
		synchronized(customers) {
			for (MyCustomer customer : customers) {
				if (customer.state == CustomerState.waiting || 
					customer.state == CustomerState.arrived) {
					for (Table table : tables) {
						if (!table.isOccupied()) {
							seatCustomer(customer, table);
							return true;
						}
					}
					if (customer.state == CustomerState.arrived) letCustomerKnowThereIsWait(customer);
					return true;
				}
			}
		}
		
		// If a waiter wants to go on break, check and see if it is possible
		synchronized(waiters) {
			for (MyWaiter waiter : waiters) {
				if (waiter.state == WaiterState.wantsABreak) {
					decideIfWaiterCanBreak(waiter);
					return true;
				}
			}
		}
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	/**
	 * Actions
	 */

	private void seatCustomer(MyCustomer customer, Table table) {
		// Find the free waiter that is serving the least amount of customers
		int minCustomers = -1;
		MyWaiter theWaiter = null;
		synchronized(waiters) {
			for (MyWaiter waiter : waiters) {
				if (waiter.state == WaiterState.working) {
					if (minCustomers == -1 || waiter.customerCount < minCustomers) {
						minCustomers = waiter.customerCount;
						theWaiter = waiter;
					}
				}
			}
		}
		if (theWaiter == null) return; // If we were unable to find a waiter, exit the action
		
		// Tell the waiter to seat the customer at the correct table
		theWaiter.waiter.msgPleaseSeatCustomer(customer.customer, table.tableNumber);
		customer.waiter = theWaiter;
		
		// Mark the table as occupied
		table.setOccupant(customer.customer);
		
		// The waiter is responsible for one more person
		theWaiter.customerCount++;
		
		// We no longer need to worry about the customer, as he is in the waiter's hands
		customer.state = CustomerState.eating;
	}
	
	private void letCustomerKnowThereIsWait(MyCustomer customer) {
		// Let the customer know he'll have to wait a bit
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, "Host " + name, "Sorry " + customer.customer.getName() + ", but we're at capacity at the moment. Do you mind waiting?");

		customer.customer.msgItWillBeAwhile();
		
		customer.state = CustomerState.waiting;
	}
	
	private void decideIfWaiterCanBreak(MyWaiter waiter) {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, "Host " + name, "Deciding if " + waiter.waiter.getName() + " can go on break.");

		// We'll let him go on break, unless...
		Boolean decision = true;
		
		// there aren't enough working waiters
		int workingWaiters = 0;
		synchronized(waiters) {
			for (MyWaiter mw : waiters)
				if (mw.state != WaiterState.onBreak)
					workingWaiters++;
		}
		if (workingWaiters <= 1) decision = false;
		
		if (waiters.size() <= 1)
			decision = false;
		
		if (decision) {
			waiter.waiter.msgOkayForBreak();
			waiter.state = WaiterState.onBreak;
		}
		else {
			waiter.waiter.msgCantGoOnBreak();
			waiter.state = WaiterState.working;
		}
	}
	
	private void CloseRestaurant() {
		this.restaurant.setOpen(false);
		this.restaurant.closingTime();
		this.isActive = false;
		this.myPerson.goOffWork();
		this.myPerson.removeRole(this);
		this.waiters.clear();
		this.myPerson = null;
	}

	// The animation DoXYZ() routines
	/*
	private void DoSeatCustomer(CustomerAgent customer, Table table) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		print("Seating " + customer + " at " + table);
		hostGui.DoBringToTable(customer, table.tableNumber); 

	}
	*/

	/**
	 * Utilities
	 */

	/*
	public void setGui(WaiterGui gui) {
		hostGui = gui;
	}

	public WaiterGui getGui() {
		return hostGui;
	}
	*/

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
	private enum CustomerState {arrived, waiting, eating, finished};
	private class MyCustomer {
		CustomerBen customer;
		CustomerState state;
		MyWaiter waiter;
		
		public MyCustomer(CustomerBen c, CustomerState s) {
			customer = c;
			state = s;
			waiter = null;
		}
	}
	
	private int getNumberOfActiveCustomers() {
		int count = 0;
		synchronized(customers) {
			for (MyCustomer customer : customers) {
				if (customer.state != CustomerState.finished) {
					count++;
				}
			}
		}
		return count;
	}
	
	// A class to keep track of our waiters
	private enum WaiterState {working, wantsABreak, onBreak};
	private class MyWaiter {
		WaiterBen waiter;
		WaiterState state;
		int customerCount;
		
		public MyWaiter(WaiterBen w, WaiterState s) {
			waiter = w;
			state = s;
		}
	}

	public void startInteraction(Intention intent) {
		closingTime = false;
	}

}

