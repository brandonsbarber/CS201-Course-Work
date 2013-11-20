package cs201.interfaces.marketInterfaces;

import java.util.List;

import cs201.roles.marketRoles.MarketManagerRole.Item;

public interface MarketEmployee {

	public void msgRetrieveItems(MarketManager manager, List<Item> items, int id);
	
}
