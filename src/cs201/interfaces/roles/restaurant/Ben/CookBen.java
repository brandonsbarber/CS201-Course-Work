package cs201.interfaces.roles.restaurant.Ben;

import java.util.List;

import cs201.structures.market.MarketStructure;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface CookBen {

	public void checkInventory();
	
	public void addMarket(MarketStructure m);

	public String getName();
	
	public void msgHereIsOrder(WaiterBen waiter, String choice, int table);
	
	//public void msgHereIsYourFood(List<Item> items);
	
	//public void msgCannotFulfill(List<Item> items);

}