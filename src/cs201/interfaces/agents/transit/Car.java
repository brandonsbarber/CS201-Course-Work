package cs201.interfaces.agents.transit;

import cs201.interfaces.roles.transit.Passenger;
import cs201.structures.Structure;

/**
 * 
 * @author Brandon
 *
 */
public interface Car extends Vehicle
{
	/**
	 * Calls for car
	 * @param p who is calling
	 * @param s where is call
	 * @param d where to
	 */
	public void msgCallCar (Passenger p, Structure s, Structure d);
	
	/**
	 * Signals that boarding is done
	 * @param p the passenger boarding
	 */
	public void msgDoneBoarding (Passenger p);
	
	/**
	 * Signals that leaving is done
	 * @param p the passenger leaving
	 */
	public void msgLeaving (Passenger p);
}
