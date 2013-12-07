package cs201.interfaces.roles.restaurant.Ben;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface WaiterBen {

	public void setWaiterNumber(int number);

	public void msgPleaseSeatCustomer(CustomerBen cust, int table);
	
	public void msgReadyToOrder(CustomerBen cust);
	
	public void msgOrderReady(String choice, int table);
	
	public void msgHereIsChoice(CustomerBen cust, String choice);
	
	public void msgLeavingTable(CustomerBen cust);
	
	public void msgOutOf(String choice, int table);
	
	public void msgGoOnBreak();
	
	public void msgContinueWorking();
	
	public void msgOkayForBreak();
	
	public void msgCantGoOnBreak();
	
	public void msgHereIsCheck(float amount, CustomerBen customer);
	
	public void msgAtDestination();
	
	public String getName();
	
	public int getWaiterNumber();

}