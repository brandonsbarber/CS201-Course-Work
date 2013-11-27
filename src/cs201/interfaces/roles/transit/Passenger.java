package cs201.interfaces.roles.transit;

import cs201.interfaces.agents.transit.Vehicle;
import cs201.structures.Structure;

/**
 * 
 * @author Brandon
 *
 */
public interface Passenger
{
	/**
	 * Goes to the given structure
	 * @param s
	 */
	public void msgGoTo (Structure s);
	
	/**
	 * Presents a boarding request
	 * @param v
	 */
	public void msgPleaseBoard (Vehicle v);
	
	/**
	 * Signals that a destination has been reached
	 * @param s
	 */
	public void msgReachedDestination (Structure s);
}
