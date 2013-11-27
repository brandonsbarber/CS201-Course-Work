package cs201.interfaces.agents.transit;

import cs201.structures.Structure;

/**
 * 
 * @author Brandon
 *
 */
public interface Vehicle
{
	/**
	 * Sets the destination of the vehicle
	 * @param s
	 */
	public void msgSetDestination(Structure s);
	
	/**
	 * Sets the location of the vehicle
	 * @param s
	 */
	public void msgSetLocation (Structure s);
	
	/**
	 * Asks whether the destination has been reached
	 * @return
	 */
	public boolean destinationReached();
}
