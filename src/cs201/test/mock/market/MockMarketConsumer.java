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

	public void msgHereIsYourTotal(MarketManager manager, float amount) {

	}

	public void msgHereAreYourItems(List<ItemRequest> items) {
		// Log the message
		String msg = "MarketConsumer: " + this.name + ": Received msgHereAreYourItems with ";
		for (ItemRequest item : items) {
			msg += item.amount + " " + item.item + " ";
		}
		System.out.println(msg);
		log.add(new LoggedEvent(msg));
	}

	public void startInteraction(Intention intent) {

	}

	public void closingTime() {

	}

}
