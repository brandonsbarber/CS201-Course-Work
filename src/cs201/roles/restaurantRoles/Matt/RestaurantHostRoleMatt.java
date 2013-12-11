package cs201.roles.restaurantRoles.Matt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.roles.restaurant.Matt.HostGuiMatt;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelMatt;
import cs201.helper.Matt.TableMatt;
import cs201.interfaces.roles.restaurant.Matt.CustomerMatt;
import cs201.interfaces.roles.restaurant.Matt.HostMatt;
import cs201.interfaces.roles.restaurant.Matt.WaiterMatt;
import cs201.roles.restaurantRoles.RestaurantHostRole;
import cs201.trace.AlertLog;
import cs201.trace.AlertTag;

/**
 * Restaurant Host Agent
 */
public class RestaurantHostRoleMatt extends RestaurantHostRole implements HostMatt {
	private Semaphore atTargetPosition = new Semaphore(0); // used for animation
	private static final int NTABLES = 4;//a global for the number of tables. SHOULD BE A PERFECT SQUARE
	
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	private List<MyCustomer> waitingCustomers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	
	private Collection<TableMatt> tables;
	
	private HostGuiMatt gui;
	
	private List<MyWaiter> waiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
	private enum WaiterState { working, askingForBreak, onBreak };
	private int activeWaiters = 0;
	
	private List<CustomerMatt> bannedCustomers = Collections.synchronizedList(new ArrayList<CustomerMatt>());
	private enum CustomerState { arrived, waiting, goingToTable };
	private int numCustomers;
	
	private boolean timeToClose;
	
	public RestaurantHostRoleMatt() {
		super();
		
		tables = Collections.synchronizedList(new ArrayList<TableMatt>(NTABLES));
		numCustomers = 0;
		timeToClose = false;
		gui = null;
		
		int width = (int)Math.round(Math.sqrt(NTABLES));
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				TableMatt tempTable = new TableMatt(i*width + j + 1);
				tempTable.setPos((int)(RestaurantAnimationPanelMatt.WINDOWX * .3f) + i*(int)(RestaurantAnimationPanelMatt.WINDOWX * .3f),
						(int)(RestaurantAnimationPanelMatt.WINDOWY * .3f) + j*(int)(RestaurantAnimationPanelMatt.WINDOWY * .3f));
				tables.add(tempTable);
			}
		}
	}

	/** 
	 * Returns this list of waiting customers this HostAgent is keeping track of
	 * @return List<Customer> representing this HostAgent's waiting customers
	 */
	public List<MyCustomer> getWaitingCustomers() {
		return waitingCustomers;
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
	public void msgClosingTime() {
		timeToClose = true;
		stateChanged();
	}
	
	@Override
	public void msgIWantToEat(CustomerMatt c) {		
		if (!bannedCustomers.contains(c)) {
			synchronized(waitingCustomers) {
				MyCustomer cust = new MyCustomer(c);
				int ID = AssignCustomerID(cust);
				AlertLog.getInstance().logDebug(AlertTag.RESTAURANT, getName(), "ID received for " + c + ": " + ID);
				int x = (int)(RestaurantAnimationPanelMatt.WINDOWX * .08f) + (ID % 2) * (int)(RestaurantAnimationPanelMatt.WINDOWX * .05f);
				int y = (int)(RestaurantAnimationPanelMatt.WINDOWY * .08f) + (ID / 2) * (int)(RestaurantAnimationPanelMatt.WINDOWY * .05f);
				c.getGui().SetWaitingArea(x, y);
				c.getGui().Animate();
				waitingCustomers.add(cust);
				cust.state = CustomerState.waiting;
				numCustomers++;
			}
		} else {
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), c.toString() + " cannot enter the restaurant because he was banned");
			return;
		}
		stateChanged();
	}
	
	@Override
	public void msgWaitTimeTooLong(CustomerMatt c) {
		synchronized(waitingCustomers) {
			for (MyCustomer m : waitingCustomers) {
				if (m.customer == c) {
					waitingCustomers.remove(m);
					numCustomers--;
					break;
				}
			}
		}
		stateChanged();
	}
	
	@Override
	public void msgCustomerRetrievedFromWaitingArea(CustomerMatt c) {
		synchronized(waitingCustomers) {
			for (MyCustomer m : waitingCustomers) {
				if (m.customer == c) {
					waitingCustomers.remove(m);
					break;
				}
			}
		}
		stateChanged();
	}

	@Override
	public void msgTableIsFree(WaiterMatt w, int tNum) {
		numCustomers--;
		
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
		if (timeToClose && numCustomers == 0) {
			CloseRestaurant();
			return true;
		}
		
		synchronized(waiters) {
			for (MyWaiter m : waiters) {
				if (m.state == WaiterState.askingForBreak) {
					PutWaiterOnBreak(m, activeWaiters > 1);
					return true;
				}
			}
		}
		
		if (this.restaurant.getOpen()) {
			synchronized(tables) {
				for (TableMatt table : tables) {
					if (!table.isOccupied()) {
						synchronized(waitingCustomers) {
							for (MyCustomer c : waitingCustomers) {
								if (c.state == CustomerState.waiting) {
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
									
									CallWaiter(tempWaiter, table, c);
									return true;
								}
							}
						}
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
	private void CloseRestaurant() {
		this.restaurant.closingTime();
		this.restaurant.setOpen(false);
		this.isActive = false;
		this.myPerson.goOffWork();
		this.myPerson.removeRole(this);
		this.waiters.clear();
		DoCloseRestaurant();
		this.myPerson = null;
		this.restaurant.updateInfoPanel();
		this.gui.setPresent(false);
	}
	
	private synchronized void CallWaiter(MyWaiter m, TableMatt t, MyCustomer c) {
		t.setOccupant(c.customer);
		c.customer.msgAboutToBeSeated();
		c.state = CustomerState.goingToTable;
		m.waiter.msgSeatCustomer(t.tableNum(), c.customer);
		m.numberOfCustomers++;
		DoCallWaiter(m.waiter, t, c.customer);
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
	private void DoCloseRestaurant() {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), "Closing down the restaurant.");
		gui.goToLocation(RestaurantAnimationPanelMatt.RESTAURANT_ENTRANCE_X, RestaurantAnimationPanelMatt.RESTAURANT_ENTRANCE_Y);
		try {
			atTargetPosition.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void DoCallWaiter(WaiterMatt w, TableMatt t, CustomerMatt c) {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), "Telling " + w.toString() + " to seat " + c + " at Table " + t.tableNum() + ".");
	}
	
	private void DoPutWaiterOnBreak(MyWaiter m, boolean breakAllowed) {
		if (breakAllowed) {
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), m.waiter.toString() + " is allowed to go on break.");
		} else {
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), m.waiter.toString() + " is not allowed to go on break.");
		}
	}
	
	private int AssignCustomerID(MyCustomer c) {
		synchronized(waitingCustomers) {
			int max = 0;
			for (MyCustomer m : waitingCustomers) {
				if (m.id > max) {
					max = m.id;
				}
			}
			
			intCheck: for (int i = 0; i <= max; i++) {
				for (MyCustomer m : waitingCustomers) {
					if (m.id == i) {
						continue intCheck;
					}
				}
				c.id = i;
				return i;
			}
			
			c.id = max + 1;
			return max + 1;
		}
	}
	
	/**
	 * Adds a new Waiter to this HostAgent's list of available Waiters
	 * @param waiter The new Waiter
	 */
	public void addWaiter(WaiterMatt waiter) {
		AlertLog.getInstance().logDebug(AlertTag.RESTAURANT, getName(), "Adding waiter.");
		waiters.add(new MyWaiter(waiter));
		activeWaiters++;
		AlertLog.getInstance().logDebug(AlertTag.RESTAURANT, getName(), "Total waiters: " + activeWaiters);
		stateChanged();
	}
	
	public int getNumActiveWaiters() {
		return waiters.size();
	}
	
	public List<RestaurantWaiterRoleMatt> getActiveWaiters() {
		List<RestaurantWaiterRoleMatt> active = new ArrayList<RestaurantWaiterRoleMatt>();
		for (MyWaiter m : waiters) {
			active.add((RestaurantWaiterRoleMatt) m.waiter);
		}
		return active;
	}
	
	private class MyWaiter {
		WaiterMatt waiter;
		WaiterState state;
		int numberOfCustomers;
		
		public MyWaiter(WaiterMatt w) {
			this.waiter = w;
			this.state = WaiterState.working;
			this.numberOfCustomers = 0;
		}
	}
	
	private class MyCustomer {
		CustomerMatt customer;
		Integer id;
		CustomerState state;
		
		public MyCustomer(CustomerMatt m) {
			this.customer = m;
			this.id = -1;
			this.state = CustomerState.arrived;
		}
	}
	
	/**
	 * The HostGui tells this HostAgent that it has reached its destination, freeing up this HostAgent
	 * to continue working
	 */
	public void DoneAnimating() {
		atTargetPosition.release();
	}
	
	public void setGui(HostGuiMatt g) {
		gui = g;
	}

	public HostGuiMatt getGui() {
		return gui;
	}

	@Override
	public void startInteraction(Intention intent) {
		this.restaurant.updateInfoPanel();
		this.gui.setPresent(true);
		timeToClose = false;
		gui.goToDesk();
		try {
			atTargetPosition.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}