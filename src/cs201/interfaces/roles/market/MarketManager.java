package cs201.interfaces.roles.market;

import java.util.List;

import cs201.agents.PersonAgent.Intention;
import cs201.roles.marketRoles.MarketManagerRole.ItemRequest;
import cs201.structures.restaurant.Restaurant;

public interface MarketManager {
	
	public void msgHereIsMyOrder(MarketConsumer consumer, List<ItemRequest> items);
	
	public void msgHereIsMyOrderForDelivery(Restaurant restaurant, ItemRequest item);
	
	public void msgHereIsMyPayment(MarketConsumer consumer, float amount);
	
	
	/*
	 * Messages sent from the employee to the manager after he retrieves items or a car
	 */
	
	public void msgHereAreItems(MarketEmployee employee, List<ItemRequest> items, int id);
	public void msgHereIsCar(MarketEmployee employee, int id);
	
	public void startInteraction(Intention intent);
	
}
