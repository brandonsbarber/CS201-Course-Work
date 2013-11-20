package cs201.interfaces.agents.transit;

import cs201.interfaces.roles.transit.Passenger;
import cs201.structures.Structure;

public interface Car extends Vehicle
{
	public void msgCallCar (Passenger p, Structure s, Structure d);
	
	public void msgDoneBoarding (Passenger p);
	
	public void msgLeaving (Passenger p);
}
