package cs201.interfaces.roles.market;

import java.util.List;

import cs201.roles.marketRoles.MarketManagerRole.ItemRequest;

public interface MarketEmployee {
	
	public void msgRetrieveItems(MarketManager manager, List<ItemRequest> items, int id);
	
	/**
	 * Should return whether or not this MarketEmployee has a backing PersonAgent.
	 * This is used to determine whether the employee role has been fulfilled.
	 * A MockMarketEmployee should just return false
	 */
	public boolean hasAPerson();
	
	public void msgClosingTime();
	
}
