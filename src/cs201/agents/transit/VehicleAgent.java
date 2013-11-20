package cs201.agents.transit;

import cs201.agents.Agent;
import cs201.interfaces.transit.Vehicle;
import cs201.structures.Structure;

public abstract class VehicleAgent extends Agent implements Vehicle
{
	Structure destination, currentLocation;
	
	public void msgSetDestination (Structure destination)
	{
		this.destination = destination;
	}
	
	public boolean destinationReached()
	{
		return destination == currentLocation;
	}
}
