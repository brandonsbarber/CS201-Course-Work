package cs201.roles.restaurantRoles.Brandon;

import java.util.*;
import java.util.concurrent.Semaphore;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.roles.restaurant.Brandon.CashierGuiBrandon;
import cs201.gui.roles.restaurant.Brandon.HostGuiBrandon;
import cs201.interfaces.roles.restaurant.Brandon.CustomerBrandon;
import cs201.interfaces.roles.restaurant.Brandon.HostBrandon;
import cs201.interfaces.roles.restaurant.Brandon.WaiterBrandon;
import cs201.roles.restaurantRoles.RestaurantHostRole;
import cs201.trace.AlertLog;
import cs201.trace.AlertTag;

/**
 * Restaurant Host Agent
 */
public class RestaurantHostRoleBrandon extends RestaurantHostRole implements HostBrandon
{
	private String name;
	private List<Table> tables;
	private List<MyCustomer> waitingCustomers;
	private List<MyWaiter> waiters;
	
	enum WaiterState {Normal,AskedAboutBreak,WaitingOnBreak,Break};
	
	int numCustomers;
	private boolean closingTime = false;
	
	class MyCustomer
	{
		CustomerBrandon c;
		boolean informed;
		public MyCustomer(CustomerBrandon agent)
		{
			c = agent;
			informed = false;
		}
	}
	
	/**
	 * Constructs a new HostAgent
	 * @param name the name of the agent
	 * @param numTables the number of tables in the restaurant
	 */
	public RestaurantHostRoleBrandon(String name, int numTables)
	{
		super();
		this.name = name;
		
		tables = Collections.synchronizedList(new ArrayList<Table>());
		waitingCustomers = Collections.synchronizedList(new ArrayList<MyCustomer>());
		waiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
		
		for(int i = 0; i < numTables; i++)
		{
			tables.add(new Table(i+1));
		}
	}
	
	/**
	 * Gets the name of the Host
	 * @return the host's name
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Tells the host that a table was added to the restaurant
	 */
	public void addTable()
	{
		tables.add(new Table(tables.size()+1));
		stateChanged();
	}
	
	/**
	 * Signals the waiter that a WaiterAgent was added to the restaurant
	 * @param waiter the waiter who was added
	 */
	public void addWaiter(WaiterBrandon waiter)
	{
		waiters.add(new MyWaiter(waiter));
		stateChanged();
	}
	
	/**
	 * Signals that a customer arrived to the restaurant
	 * @param c the customer who arrives
	 */
	public void msgArrived(CustomerBrandon c)
	{
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,c.getName()+" has arrived.");
		waitingCustomers.add(new MyCustomer(c));
		numCustomers++;
		stateChanged();
	}
	
	/**
	 * Signals that a customer under the care of the given waiter has left
	 * @param w the waiter who was responsible for the customer
	 * @param table where the customer was sitting
	 */
	public void msgCustomerLeft(WaiterBrandon w, int table)
	{
		numCustomers--;
		tables.get(table-1).c = null;
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,""+w.getName()+" now has one fewer customer. Table "+table+" is clear");
		
		for(int i = 0; i < waiters.size(); i++)
		{
			MyWaiter wAgent = waiters.get(i);
			if(wAgent.w == w)
			{
				wAgent.numCustomers--;
				stateChanged();
				break;
			}
		}
	}
	
	/**
	 * Message received from a waiter asking to go on break
	 * @param agent the waiter asking to go on break
	 */
	public void msgWantToBreak(WaiterBrandon agent)
	{
		for(MyWaiter a : waiters)
		{
			if(a.w == agent)
			{
				AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,""+agent.getName()+" asked about break");

				a.s = WaiterState.AskedAboutBreak;
				break;
			}
		}
		stateChanged();
	}

	/**
	 * A message received from a waiter saying that they are on break
	 * @param waiterAgent the agent going on break
	 */
	public void msgOnBreak(WaiterBrandon waiterAgent)
	{
		for(MyWaiter a : waiters)
		{
			if(a.w == waiterAgent)
			{
				AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,""+waiterAgent.getName()+" is on break");

				a.s = WaiterState.Break;
				AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,a.w.getName()+" is on break");
				break;
			}
		}
		stateChanged();
	}

	/**
	 * A message indicating that a waiter is ready to work again
	 * @param waiterAgent the waiter coming off of break
	 */
	public void msgOffBreak(WaiterBrandon waiterAgent)
	{
		for(MyWaiter w : waiters)
		{
			if(w.w == waiterAgent)
			{
				AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,""+waiterAgent.getName()+" is off break");
				w.s = WaiterState.Normal; 
				break;
			}
		}
		stateChanged();
	}
	
	/**
	 * Informed by a customer that they no longer want to wait for a table
	 * @param customerAgent the customer who is no longer waiting
	 */
	public void msgNotWaiting(CustomerBrandon customerAgent)
	{
		for(int i = 0; i < waitingCustomers.size(); i++)
		{
			if(waitingCustomers.get(i).c == customerAgent)
			{
				waitingCustomers.remove(i);
				break;
			}
		}
		stateChanged();
	}
	
	@Override
	public boolean pickAndExecuteAnAction()
	{
		if(closingTime && numCustomers == 0)
		{
			closeRestaurant();
			return true;
		}
		for(MyWaiter w : waiters)
		{
			if(w.s == WaiterState.AskedAboutBreak)
			{
				processBreak(w);
				return true;
			}
		}
		if(waitingCustomers.size() != 0)
		{
			boolean fullTables = false;
			for(Table t : tables)
			{
				fullTables = fullTables || t.c != null;
				
				if(t.c == null && waiters.size() != 0)
				{
					dispatchWaiter(waitingCustomers.remove(0).c,t.num);
					return true;
				}
			}
			if(fullTables || waitingCustomers.size() > tables.size())
			{
				AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,"THE TABLES ARE FULL");
				for(MyCustomer cust : waitingCustomers)
				{
					if(!cust.informed)
					{
						inform(cust);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private void closeRestaurant() {
		this.restaurant.closingTime();
		this.restaurant.setOpen(false);
		this.isActive = false;
		this.myPerson.goOffWork();
		this.myPerson.removeRole(this);
		this.waiters.clear();
		//DoCloseRestaurant();
		this.myPerson = null;
		this.gui.setPresent(false);
	}

	private void inform(MyCustomer cust)
	{
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,"Telling "+cust.c.getName()+" that tables are full.");
		cust.informed = true;
		cust.c.msgInformedFull();		
	}

	private void processBreak(MyWaiter w)
	{
		if(lastWaiter())
		{
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,"Denying break to "+w.w.getName());
			w.w.msgCanGoOnBreak(false);
			w.s = WaiterState.Normal;
		}
		else
		{
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,"Granting break to "+w.w.getName());

			w.w.msgCanGoOnBreak(true);
			w.s = WaiterState.WaitingOnBreak;
		}
	}

	private boolean lastWaiter()
	{
		int count = 0;
		for(MyWaiter w : waiters)
		{
			if(w.s == WaiterState.Normal || w.s == WaiterState.AskedAboutBreak)
			{
				count++;
			}
		}
		return count == 1;
	}

	private void dispatchWaiter(CustomerBrandon customer, int table)
	{
		if(waiters.size() == 0)
		{
			return;
		}
		int firstIndex = 0;
		while(firstIndex < waiters.size() && waiters.get(firstIndex).s != WaiterState.Normal)
		{
			firstIndex++;
		}
		if(firstIndex == waiters.size())
		{
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,"All waiters on break...");

			return;
		}
		
		int minCustomers = waiters.get(firstIndex).numCustomers;
		int minIndex = firstIndex;
		for(int i = firstIndex+1; i < waiters.size(); i++)
		{
			if(waiters.get(i).numCustomers < minCustomers && waiters.get(i).s == WaiterState.Normal)
			{
				minIndex = i;
				minCustomers = waiters.get(i).numCustomers;
			}
		}
		tables.get(table-1).c = customer;
		waiters.get(minIndex).numCustomers++;
		WaiterBrandon waiter = waiters.get(minIndex).w;
		waiter.msgSeatCustomer(customer, table);
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,"Assigned "+customer.getName()+" to "+waiter.getName());
	}
	
	private class MyWaiter
	{
		WaiterBrandon w;
		int numCustomers = 0;
		WaiterState s;
		
		public MyWaiter(WaiterBrandon w)
		{
			this.w = w;
			s = WaiterState.Normal;
		}
	}
	
	private class Table
	{
		int num;
		CustomerBrandon c = null;
		
		public Table(int num)
		{
			this.num = num;
		}
	}
	
	/**
	 * String representation of the host
	 * @return the representation of the host
	 */
	public String toString()
	{
		return "Host "+getName();
	}

	@Override
	public void startInteraction(Intention intent) {
		this.gui.setPresent(true);
		closingTime = false;
		/*gui.goToDesk();
		try {
			atTargetPosition.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
	}

	@Override
	public void msgClosingTime() {
		closingTime = true;
		stateChanged();
	}

	public void msgReachedDestination() {
		animationPause.release();
	}
	
	Semaphore animationPause = new Semaphore(0,true);

	HostGuiBrandon gui;
	
	public void setGui(HostGuiBrandon hostGui) {
		this.gui = hostGui;
	}

	public HostGuiBrandon getGui() {
		return gui;
	}
}

