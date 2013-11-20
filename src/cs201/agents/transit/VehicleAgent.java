package cs201.agents.transit;

import cs201.agents.Agent;
import cs201.interfaces.agents.transit.Vehicle;
import cs201.structures.Structure;

public abstract class VehicleAgent extends Agent implements Vehicle
{
	Structure destination, currentLocation;
	
	public void msgSetDestination (Structure destination)
	{
		this.destination = destination;
		stateChanged();
	}
	
	public void msgSetLocation(Structure s)
	{
		currentLocation = s;
	}
	
	public boolean destinationReached()
	{
		return destination == currentLocation;
	}
}
