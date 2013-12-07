package cs201.interfaces.roles.restaurant.Brandon;

import java.util.TimerTask;

public interface CashierBrandon
{
	/**
	 * Signals that the waiter will present an order
	 * @param w waiter giving the order
	 * @param order the order being given
	 * @param table where the customer who ordered is sitting
	 */
	public abstract void msgAskForBill (WaiterBrandon w, String order, int table, CustomerBrandon cust);
	
	/**
	 * Receives a payment from a customer
	 * @param cust the customer paying the bill
	 * @param billAmount the amount paid by the customer
	 */
	public abstract void msgPay(CustomerBrandon cust, double billAmount);

	//public abstract void msgGiveMarketBill(Market market, double amount);
}
