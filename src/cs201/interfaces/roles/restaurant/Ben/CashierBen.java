package cs201.interfaces.roles.restaurant.Ben;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface CashierBen {

	public void msgComputeCheckForOrder(String choice, WaiterBen waiter, CustomerBen cust);
	
	public void msgHereIsPayment(CustomerBen cust, float amount);
	
	public void msgICantPay(CustomerBen cust);
	
	public String getName();

}