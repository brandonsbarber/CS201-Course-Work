package cs201.test.mock.market;

import java.util.List;

import cs201.agents.PersonAgent.Intention;
import cs201.interfaces.roles.market.MarketConsumer;
import cs201.interfaces.roles.market.MarketEmployee;
import cs201.interfaces.roles.market.MarketManager;
import cs201.roles.marketRoles.MarketManagerRole.ItemRequest;
import cs201.test.mock.LoggedEvent;
import cs201.test.mock.Mock;

public class MockMarketManager extends Mock implements MarketManager {

	public MockMarketManager(String name) {
		super(name);
	}

	public void msgHereIsMyOrder(MarketConsumer consumer,
			List<ItemRequest> items) {
		
	}

	public void msgHereIsMyOrderForDeliery(MarketConsumer consumer,
			List<ItemRequest> items) {
		
	}

	public void msgHereIsMyPayment(MarketConsumer consumer, float amount) {
		
	}

	public void msgHereAreItems(MarketEmployee employee,
			List<ItemRequest> items, int id) {
		// Log the message
		String msg = "MarketManager: " + this.name + ": Received msgHereAreItems with ";
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
