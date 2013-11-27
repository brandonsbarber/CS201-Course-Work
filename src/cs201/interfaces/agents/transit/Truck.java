package cs201.interfaces.agents.transit;

import java.util.List;

import cs201.roles.marketRoles.MarketManagerRole.ItemRequest;
import cs201.structures.Structure;

/**
 * 
 * @author Brandon
 *
 */
public interface Truck extends Vehicle
{
	/**
	 * Sends truck to make delivery
	 * @param inventory what to deliver
	 * @param destination where to deliver
	 * @param price what to charge
	 */
	public void msgMakeDeliveryRun (List<ItemRequest> inventory, Structure destination, double price);
}
