package cs201.interfaces.roles.restaurant.Ben;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface HostBen {
		
	public void addWaiter(WaiterBen waiter);
	
	public void msgIWantFood(CustomerBen cust);
	
	public void msgICantWait(CustomerBen cust);

	public void msgLeavingTable(CustomerBen cust);
	
	public void msgWantToGoOnBreak(WaiterBen w);
	
	public void msgGoingBackToWork(WaiterBen w);

	public String getName();

}