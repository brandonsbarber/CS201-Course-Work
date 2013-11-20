package cs201.roles.restaurantRoles.Matt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import cs201.helper.Matt.TableMatt;
import cs201.interfaces.roles.restaurant.RestaurantHostRole;
import cs201.interfaces.roles.restaurant.Matt.CustomerMatt;
import cs201.interfaces.roles.restaurant.Matt.HostMatt;
import cs201.interfaces.roles.restaurant.Matt.WaiterMatt;

/**
 * Restaurant Host Agent
 */
public class RestaurantHostRoleMatt extends RestaurantHostRole implements HostMatt {
	private static final int NTABLES = 4;//a global for the number of tables. SHOULD BE A PERFECT SQUARE
	
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	private List<CustomerMatt> waitingCustomers = Collections.synchronizedList(new ArrayList<CustomerMatt>());
	private List<Integer> waitingCustomerIDs = Collections.synchronizedList(new ArrayList<Integer>());
	
	private Collection<TableMatt> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	
	private List<MyWaiter> waiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
	private enum WaiterState { working, askingForBreak, onBreak };
	private int activeWaiters = 0;
	
	private List<CustomerMatt> bannedCustomers = Collections.synchronizedList(new ArrayList<CustomerMatt>());
	
	public RestaurantHostRoleMatt() {
		super();
		
		tables = Collections.synchronizedList(new ArrayList<TableMatt>(NTABLES));
		
		int width = (int)Math.round(Math.sqrt(NTABLES));
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				TableMatt tempTable = new TableMatt(i*width + j + 1);
				tempTable.setPos(150 + i*150, 150+j*150);
				tables.add(tempTable);
			}
		}
	}

	/** 
	 * Returns this list of waiting customers this HostAgent is keeping track of
	 * @return List<Customer> representing this HostAgent's waiting customers
	 */
	public List<CustomerMatt> getWaitingCustomers() {
		return waitingCustomers;
	}
	
	/**
	 * Returns the list of all IDs given to waiting customers
	 * @return List<Integer> representing every ID currently assigned to a waiting customer
	 */
	public List<Integer> getWaitingCustomerIDs() {
		return waitingCustomerIDs;
	}

	/**
	 * Returns the list of tables this HostAgent is keeping track of
	 * @return Collection<Table> representing this HostAgent's waiting customers
	 */
	public Collection<TableMatt> getTables() {
		return tables;
	}
	
	// Messages -------------------------------------------------------------
	@Override
	public void msgIWantToEat(CustomerMatt c) {
		if (!bannedCustomers.contains(c)) {
			Integer ID = AssignCustomerID();
			int x = 40 + (ID % 2) * 25;
			int y = 40 + (ID / 2) * 25;
			c.getGui().SetWaitingArea(x, y);
			c.getGui().Animate();
			waitingCustomers.add(c);
		} else {
			System.out.println("Host " + this.getName() + " says that Customer" + c.toString() + " cannot enter the restaurant because he was banned");
		}
		stateChanged();
	}
	
	@Override
	public void msgWaitTimeTooLong(CustomerMatt c) {
		int index = waitingCustomers.indexOf(c);
		waitingCustomerIDs.remove(index);
		waitingCustomers.remove(c);
		stateChanged();
	}
	
	@Override
	public void msgCustomerRetrievedFromWaitingArea() {
		waitingCustomerIDs.remove(0);
		stateChanged();
	}

	@Override
	public void msgTableIsFree(WaiterMatt w, int tNum) {
		synchronized(tables) {
			for (TableMatt table : tables) {
				if (table.tableNum() == tNum) {
					table.setUnoccupied();
					break;
				}
			}
		}
		
		synchronized(waiters) {
			for (MyWaiter m : waiters) {
				if (m.waiter == w) {
					m.numberOfCustomers--;
					break;
				}
			}
		}
		stateChanged();
	}
	
	@Override
	public void msgPermissionForBreak(WaiterMatt w) {
		synchronized(waiters) {
			for (MyWaiter m : waiters) {
				if (m.waiter == w) {
					m.state = WaiterState.askingForBreak;
					stateChanged();
					return;
				}
			}
		}
	}
	
	@Override
	public void msgOffBreak(WaiterMatt w) {
		synchronized(waiters) {
			for (MyWaiter m : waiters) {
				if (m.waiter == w) {
					m.state = WaiterState.working;
					activeWaiters++;
					stateChanged();
					return;
				}
			}
		}
	}
	
	@Override
	public void msgBanThisCustomer(CustomerMatt c) {
		bannedCustomers.add(c);
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		synchronized(waiters) {
			for (MyWaiter m : waiters) {
				if (m.state == WaiterState.askingForBreak) {
					PutWaiterOnBreak(m, activeWaiters > 1);
					return true;
				}
			}
		}
		
		synchronized(tables) {
			for (TableMatt table : tables) {
				if (!table.isOccupied()) {
					if (!waitingCustomers.isEmpty()) {
						if (waiters.size() == 0) return false;
						int leastCustomers = Integer.MAX_VALUE;
						MyWaiter tempWaiter = null;
						
						synchronized(waiters) {
							for (MyWaiter w : waiters) {
								if (w.state != WaiterState.onBreak && w.numberOfCustomers < leastCustomers) {
									leastCustomers = w.numberOfCustomers;
									tempWaiter = w;
								}
							}
						}
						
						CallWaiter(tempWaiter, table);
						return true;
					}
				}
			}
		}
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions -------------------------------------------------------------
	private void CallWaiter(MyWaiter m, TableMatt t) {
		t.setOccupant(waitingCustomers.get(0));
		waitingCustomers.get(0).msgAboutToBeSeated();
		m.waiter.msgSeatCustomer(t.tableNum(), waitingCustomers.remove(0));
		m.numberOfCustomers++;
		DoCallWaiter(m.waiter, t);
	}
	
	private void PutWaiterOnBreak(MyWaiter m, boolean breakAllowed) {
		DoPutWaiterOnBreak(m, breakAllowed);
		if (breakAllowed) {
			m.state = WaiterState.onBreak;
			activeWaiters--;
		} else {
			m.state = WaiterState.working;
		}
		m.waiter.msgBreakAllowed(breakAllowed);
	}

	// Utilities -------------------------------------------------------------
	private void DoCallWaiter(WaiterMatt w, TableMatt t) {
		System.out.println(this.toString() + " telling waiter " + w.toString() + " to seat a customer.");
	}
	
	private void DoPutWaiterOnBreak(MyWaiter m, boolean breakAllowed) {
		if (breakAllowed) {
			System.out.println("Waiter " + m.waiter.toString() + " is allowed to go on break.");
		} else {
			System.out.println("Waiter " + m.waiter.toString() + " is not allowed to go on break.");
		}
	}
	
	private Integer AssignCustomerID() {
		synchronized(waitingCustomerIDs) {
			Integer max = 0;
			for (Integer i : waitingCustomerIDs) {
				if (i > max) {
					max = i;
				}
			}
			
			for (Integer i = 0; i <= max; i++) {
				if (!waitingCustomerIDs.contains(i)) {
					waitingCustomerIDs.add(i);
					return i;
				}
			}
			
			waitingCustomerIDs.add(max + 1);
			return max + 1;
		}
	}
	
	/**
	 * Adds a new Waiter to this HostAgent's list of available Waiters
	 * @param waiter The new Waiter
	 */
	public void addWaiter(WaiterMatt waiter) {
		waiters.add(new MyWaiter(waiter));
		activeWaiters++;
		stateChanged();
	}
	
	private class MyWaiter {
		private WaiterMatt waiter;
		private WaiterState state;
		private int numberOfCustomers;
		
		public MyWaiter(WaiterMatt w) {
			this.waiter = w;
			this.state = WaiterState.working;
			this.numberOfCustomers = 0;
		}
	}
}