package cs201.interfaces.transit;

import cs201.structures.Structure;

public interface Passenger
{
	public void msgGoTo (Structure s);
	
	public void msgPleaseBoard (Vehicle v);
	
	public void msgReachedDestination (Structure s);
}
