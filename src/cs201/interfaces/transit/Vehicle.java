package cs201.interfaces.transit;

import cs201.structures.Structure;

public interface Vehicle
{
	public void msgSetDestination(Structure s);
	
	public void msgSetLocation (Structure s);
	
	public boolean destinationReached();
}
