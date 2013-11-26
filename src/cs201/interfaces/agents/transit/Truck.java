package cs201.interfaces.agents.transit;

import java.util.List;

import cs201.roles.marketRoles.MarketManagerRole.ItemRequest;
import cs201.structures.Structure;

public interface Truck extends Vehicle
{
	public void msgMakeDeliveryRun (List<ItemRequest> inventory, Structure destination, double price);
}
