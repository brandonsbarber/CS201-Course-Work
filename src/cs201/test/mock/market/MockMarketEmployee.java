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
		log.add(new LoggedEvent("MarketEmployee: " + this.name + ": Received msgRetrieveItems."));
	}

}
