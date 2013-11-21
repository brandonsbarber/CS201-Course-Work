package cs201.test.mock.market;

import java.util.List;

import cs201.agents.PersonAgent.Intention;
import cs201.interfaces.roles.market.MarketConsumer;
import cs201.interfaces.roles.market.MarketManager;
import cs201.roles.marketRoles.MarketManagerRole.ItemRequest;
import cs201.test.mock.LoggedEvent;
import cs201.test.mock.Mock;

public class MockMarketConsumer extends Mock implements MarketConsumer {

	public MockMarketConsumer(String name) {
		super(name);
	}

	/*
	public void msgRetrieveItems(MarketManager manager, List<ItemRequest> items, int id) {
		log.add(new LoggedEvent("MarketEmployee: " + this.name + ": Received msgRetrieveItems."));
	}
	*/

	public void msgHereIsYourTotal(MarketManager manager, float amount) {

	}

	public void msgHereAreYourItems(List<ItemRequest> items) {
		log.add(new LoggedEvent("MarketConsumer: " + this.name + ": Received msgHereAreYourItems."));
	}

	public void startInteraction(Intention intent) {

	}

	public void closingTime() {

	}

}
