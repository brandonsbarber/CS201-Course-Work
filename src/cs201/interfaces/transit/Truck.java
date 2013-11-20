package cs201.interfaces.transit;

import java.util.Map;

import cs201.structures.Structure;

public interface Truck extends Vehicle
{
	public void msgMakeDeliveryRun (Map<String,Integer> inventory, Structure destination);
}
