package cs201.structures.transit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.StructurePanel;
import cs201.helper.CityTime;
import cs201.interfaces.agents.transit.Bus;
import cs201.interfaces.roles.transit.Passenger;
import cs201.roles.Role;
import cs201.structures.Structure;

/**
 * 
 * @author Brandon
 *
 */
public class BusStop extends Structure
{
	List<Passenger> waitingPassengers;
	
	List<Passenger> addRequests;
	
	Bus parkedBus;
	
	Semaphore passengerAccess;
	
	/**
	 * Creates a bus stop
	 * @param x the x of the bus stop
	 * @param y the y of the bus stop
	 * @param width the width of the bus stop
	 * @param height the height of the bus stop
	 * @param id the id of the bus stop
	 * @param p the panel representing the bus stop
	 */
	public BusStop(int x, int y, int width, int height, int id, StructurePanel p)
	{
		super(x, y, width, height, id, p);
		waitingPassengers = Collections.synchronizedList(new ArrayList<Passenger>());
		addRequests = new ArrayList<Passenger>();
		passengerAccess = new Semaphore(1,true);
	}

	/**
	 * Returns null
	 */
	@Override
	public Role getRole(Intention role)
	{
		return null;
	}
	
	/**
	 * Note: Only to be used in testing!
	 * @return
	 */
	public List<Passenger> getPassengerList()
	{
		passengerAccess.release();
		return getPassengerList(null);
	}
	
	/**
	 * Gets the passengers at the bus stop
	 * @param bus the bus who is asking
	 * @return the list of passengers
	 */
	public List<Passenger> getPassengerList(Bus bus)
	{
		try
		{
			passengerAccess.acquire();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		waitingPassengers.addAll(addRequests);
		addRequests.clear();
		parkedBus = bus;
		return waitingPassengers;
	}
	
	/**
	 * Schedules a passenger to add
	 * @param p the passenger to add
	 */
	public void addPassenger(Passenger p)
	{
		addRequests.add(p);
	}
	
	/**
	 * Removes passengers from a bus
	 * @param bus the bus calling
	 * @param passengers the passengers being removed
	 */
	public void removePassengers(Bus bus, List<Passenger> passengers)
	{
		waitingPassengers.removeAll(passengers);
		parkedBus = null;
		
		passengerAccess.release();
	}

	/**
	 * Does nothing
	 */
	@Override
	public void updateTime(CityTime time) {
		
	}
}
