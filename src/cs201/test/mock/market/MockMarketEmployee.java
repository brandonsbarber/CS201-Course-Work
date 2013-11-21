package cs201.test.mock.market;

import java.util.List;

import cs201.interfaces.roles.market.MarketEmployee;
import cs201.interfaces.roles.market.MarketManager;
import cs201.roles.marketRoles.MarketManagerRole.ItemRequest;
import cs201.test.mock.Mock;

import cs201.test.mock.LoggedEvent;

public class MockMarketEmployee extends Mock implements MarketEmployee {

	public MockMarketEmployee(String name) {
		super(name);
	}

	public void msgRetrieveItems(MarketManager manager, List<ItemRequest> items, int id) {
		// Log the message
		log.add(new LoggedEvent("MarketEmployee: " + this.name + ": Received msgRetrieveItems."));
		
		// Immediately return the items to the manager
		manager.msgHereAreItems(this, items, id);
	}

}
