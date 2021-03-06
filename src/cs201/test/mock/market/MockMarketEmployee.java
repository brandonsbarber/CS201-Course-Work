package cs201.test.mock.market;

import java.util.List;

import cs201.interfaces.roles.market.MarketEmployee;
import cs201.interfaces.roles.market.MarketManager;
import cs201.roles.marketRoles.MarketManagerRole.ItemRequest;
import cs201.test.mock.LoggedEvent;
import cs201.test.mock.Mock;

public class MockMarketEmployee extends Mock implements MarketEmployee {
	
	public MockMarketEmployee(String name) {
		super(name);
	}

	public void msgRetrieveItems(MarketManager manager, List<ItemRequest> items, int id) {
		// Log the message
		String msg = "MarketEmployee: " + this.name + ": Received msgRetrieveItems with ";
		for (ItemRequest item : items) {
			msg += item.amount + " " + item.item + " ";
		}
		System.out.println(msg);
		log.add(new LoggedEvent(msg));
		
		// Immediately return the items to the manager
		manager.msgHereAreItems(this, items, id);
	}
	
	public void msgRetrieveCar(MarketManager manager, int id) {
		// Log the message
		String msg = "MarketEmployee: " + this.name + ": Received msgRetrieveCar";
		System.out.println(msg);
		log.add(new LoggedEvent(msg));
		
		// Immediately give the manager the car
		manager.msgHereIsCar(this, id);
	}

	/**
	 * Always returns false, since a MockMarketEmployee will never have a backing PersonAgent
	 */
	public boolean hasAPerson() {
		return false;
	}

	public void msgClosingTime() {
		
	}
}
