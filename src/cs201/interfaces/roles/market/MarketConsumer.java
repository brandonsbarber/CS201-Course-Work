package cs201.interfaces.roles.market;

import java.util.List;

import cs201.agents.PersonAgent.Intention;
import cs201.roles.marketRoles.MarketManagerRole.ItemRequest;

public interface MarketConsumer {
	
	public void msgHereIsYourTotal(MarketManager manager, float amount);
	
	public void msgHereAreYourItems(List<ItemRequest> items);
	
	public void startInteraction(Intention intent);
	
}
