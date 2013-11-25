package cs201.interfaces.roles.market;

import java.util.List;

import cs201.agents.PersonAgent;
import cs201.roles.marketRoles.MarketManagerRole.ItemRequest;

public interface MarketEmployee {
	
	public void msgRetrieveItems(MarketManager manager, List<ItemRequest> items, int id);
	
}
