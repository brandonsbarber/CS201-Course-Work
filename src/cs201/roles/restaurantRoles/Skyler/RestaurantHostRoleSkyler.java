package cs201.roles.restaurantRoles.Skyler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.roles.restaurant.Skyler.HostGuiSkyler;
import cs201.gui.roles.restaurant.Skyler.WaiterGuiSkyler;
import cs201.interfaces.roles.restaurant.Skyler.CustomerSkyler;
import cs201.interfaces.roles.restaurant.Skyler.HostSkyler;
import cs201.interfaces.roles.restaurant.Skyler.WaiterSkyler;
import cs201.roles.restaurantRoles.RestaurantHostRole;
import cs201.roles.restaurantRoles.Skyler.RestaurantWaiterRoleSkyler.BreakState;

public class RestaurantHostRoleSkyler extends RestaurantHostRole implements
		HostSkyler {
	
	public static final int NTABLES = 3;
	
	public List<CustomerSkyler> waitingCustomers
	= Collections.synchronizedList(new ArrayList<CustomerSkyler>());
	public List<RestaurantWaiterRoleSkyler> waiters = Collections.synchronizedList(new ArrayList<RestaurantWaiterRoleSkyler>());
	public Collection<Table> tables;
	private boolean closingTime = false;
	
	int numCustomers = 0;
	String name;
	
	//private Semaphore atTable = new Semaphore(0,true);
	private HostGuiSkyler gui;

	public RestaurantHostRoleSkyler() {
		this("");
	}
	
	public RestaurantHostRoleSkyler(String name) {
		super();

		this.name = name;
		// make some tables
		tables = Collections.synchronizedList(new ArrayList<Table>(NTABLES));
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collection
		}
	}
	
	public void setGui(HostGuiSkyler hgui) {
		gui = hgui;
	}
	
	public String getName() {
		return myPerson.getName();
	}

	@Override
	public void msgWaiterReady(WaiterSkyler w) {
		waiters.add((RestaurantWaiterRoleSkyler)w);
		stateChanged();
	}

	@Override
	public void msgIWantFood(CustomerSkyler cust) {
		waitingCustomers.add(cust);
		numCustomers++;
		stateChanged();
	}

	@Override
	public void msgIWantABreak(WaiterSkyler w) {
		stateChanged();
	}

	@Override
	public void msgBackFromBreak(WaiterSkyler w) {
		//Do("Welcome back, "+w.getName());
		stateChanged();
	}

	@Override
	public void msgTableFree(int tableNum) {
		//Do("Table "+tableNum+" free.");
		for (Table t : tables) {
			if (t.tableNumber == tableNum) {
				t.setUnoccupied();
				stateChanged();
			}
		}
		numCustomers--;
	}

	@Override
	public void startInteraction(Intention intent) {
		this.restaurant.updateInfoPanel();
		this.gui.setPresent(true);
		closingTime = false;
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		if (closingTime && numCustomers==0) {
			closeRestaurant();
		}
		synchronized(waiters) {
			for (RestaurantWaiterRoleSkyler w1: waiters) {
				if (w1.breakRequest == RestaurantWaiterRoleSkyler.BreakState.requested) {
					//CHECK THAT THERE ARE MORE THAN ONE WAITERS NOT ON BREAK
					//IF THERE ARE, PUT REQUESTING ONE ON BREAK.
					//IF NOT, REJECT REQUEST (message back to waiter).
					int i = 0;
					for (RestaurantWaiterRoleSkyler w2: waiters) {
						if (w2.breakRequest == RestaurantWaiterRoleSkyler.BreakState.noRequest || w2.breakRequest == RestaurantWaiterRoleSkyler.BreakState.denied)
							i++;
					}
					if (i>0) {
						//Do("Okay, "+w1.getName()+", go take your break when you are done with your customers.");
						w1.msgBreakApproved();
					}
					else {
						//Do("Sorry, "+w1.getName()+", but you'll have to wait to go on break.");
						w1.msgBreakDenied();
					}
					return true;
				}
			}
			}//end synch
			
			synchronized(tables) {
				for (Table table : tables) {
				if (!table.isOccupied()) {
					if (!waiters.isEmpty()) { //Currently just assigns waiters if they exist
						if (!waitingCustomers.isEmpty()) {
							for (RestaurantWaiterRoleSkyler w : waiters) {
								if (w.getNumCustomers()==0 && w.breakRequest != BreakState.approved && w.breakRequest != BreakState.onBreak){
									assignCustomer(waitingCustomers.get(0), w, table);
									return true;
								}
							} //looks through waiters to find one who has no customers currently.
							for (RestaurantWaiterRoleSkyler w : waiters) {
								if (w.state == RestaurantWaiterRoleSkyler.AgentState.Free && w.breakRequest != BreakState.approved && w.breakRequest != BreakState.onBreak){
									assignCustomer(waitingCustomers.get(0), w, table);
									return true;
								}
							} //looks through waiters to find one who is available. If none are available, choose a random waiter.
							int rand = (int)(Math.random()*waiters.size());
							if(waiters.get(rand).breakRequest != BreakState.approved && waiters.get(rand).breakRequest != BreakState.onBreak)
							assignCustomer(waitingCustomers.get(0), waiters.get(rand), table);
							//if the randomly chosen waiter is on break or their break has been approved, do not assign.
						return true;//return true to the abstract agent to reinvoke the scheduler.
						}
					}
				}
			}
			}//end synch
		return false;
	}
	
	public List getWaitingCustomers() {
		return waitingCustomers;
	}
	
	public int getNTables() {
		return NTABLES;
	}
	
	private void assignCustomer(CustomerSkyler customer, WaiterSkyler waiter, Table table) {
		waiter.msgSeatAtTable(customer, table.tableNumber);
		table.setOccupant(customer);
		waitingCustomers.remove(customer);
	}
	
	@Override
	public void msgClosingTime() {
		// TODO Auto-generated method stub
		closingTime = true;
		stateChanged();
	}
	
	private class Table {
		CustomerSkyler occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(CustomerSkyler cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		CustomerSkyler getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
	}

	public void addWaiter(WaiterSkyler w) {
		waiters.add((RestaurantWaiterRoleSkyler)w);
		stateChanged();
	}
	
	private void closeRestaurant() {
		restaurant.closingTime();
		restaurant.setOpen(false);
		waiters.clear();
		myPerson.goOffWork();
		myPerson.removeRole(this);
		myPerson = null;
		isActive = false;
		gui.setPresent(false);
		this.restaurant.updateInfoPanel();
	}

}
