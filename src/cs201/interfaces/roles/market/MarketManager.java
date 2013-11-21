package cs201.interfaces.roles.market;

import java.util.List;

import cs201.agents.PersonAgent.Intention;
import cs201.roles.marketRoles.MarketManagerRole.ItemRequest;

public interface MarketManager {
	
	public void msgHereIsMyOrder(MarketConsumer consumer, List<ItemRequest> items);
	
	public void msgHereIsMyOrderForDeliery(MarketConsumer consumer, List<ItemRequest> items);
	
	public void msgHereIsMyPayment(MarketConsumer consumer, float amount);
	
	public void msgHereAreItems(MarketEmployee employee, List<ItemRequest> items, int id);
	
	public void startInteraction(Intention intent);

	public void closingTime();
	
}
