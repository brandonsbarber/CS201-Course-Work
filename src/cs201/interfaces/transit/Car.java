package cs201.interfaces.transit;

import cs201.structures.Structure;

public interface Car extends Vehicle
{
	public void msgCallCar (Passenger p, Structure s, Structure d);
	
	public void msgDoneBoarding (Passenger p);
	
	public void msgLeaving (Passenger p);
}
