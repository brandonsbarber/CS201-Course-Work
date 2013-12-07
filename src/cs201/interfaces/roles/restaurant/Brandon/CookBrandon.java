package cs201.interfaces.roles.restaurant.Brandon;

import java.util.HashMap;

public interface CookBrandon
{
	/**
	 * Message from a market indicating that some parts of the order cannot be fulfilled
	 * @param agent the market sending this message
	 * @param orderFulfill the food not provided by the market
	 */
	//public abstract void msgCannotFulfill(Market agent, HashMap<String,Integer> orderFulfill);
	
	/**
	 * Message from a market indicating that a food restock order has been received 
	 * @param agent the market sending the message
	 * @param orderFulfill the food order that is fulfilled
	 */
	//public abstract void msgReceivedOrder(Market agent, HashMap<String,Integer> orderFulfill);
	
	/**
	 * Signals that the waiter will present an order
	 * @param w waiter giving the order
	 * @param order the order being given
	 * @param table where the customer who ordered is sitting
	 */
	public abstract void msgPresentOrder(WaiterBrandon w, String order, int table);
	
	public void msgPickingUpOrder(WaiterBrandon w, int table);
}
