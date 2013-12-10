package cs201.interfaces.roles.restaurant.Ben;

import cs201.roles.marketRoles.MarketManagerRole.ItemRequest;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface CashierBen {

	public void msgComputeCheckForOrder(String choice, WaiterBen waiter, CustomerBen cust);
	
	public void msgIOrderedFromMarket(ItemRequest request);
	
	public void msgHereIsPayment(CustomerBen cust, float amount);
	
	public void msgICantPay(CustomerBen cust);
	
	public String getName();

}