package cs201.interfaces.agents.transit;

import cs201.helper.transit.BusRoute;
import cs201.interfaces.roles.transit.Passenger;

/**
 * 
 * @author Brandon
 *
 */
public interface Bus extends Vehicle
{
	/**
	 * Signals that passenger is leaving
	 * @param p passenger leaving
	 */
	public void msgLeaving (Passenger p);
	
	/**
	 * Signals that passenger is staying
	 * @param p passenger staying
	 */
	public void msgStaying (Passenger p);
	
	/**
	 * Signals that passenger is boarding
	 * @param p passenger boarding
	 */
	public void msgDoneBoarding (Passenger p);
	
	/**
	 * Signals that passenger is not boarding
	 * @param p passenger not boarding
	 */
	public void msgNotBoarding(Passenger p);
	
	/**
	 * Gets the route of the bus
	 * @return bus route
	 */
	public BusRoute getRoute();
}
