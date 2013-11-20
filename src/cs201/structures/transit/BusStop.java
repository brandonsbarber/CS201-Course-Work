package cs201.structures.transit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cs201.agents.PersonAgent.Intention;
import cs201.interfaces.transit.Bus;
import cs201.interfaces.transit.Passenger;
import cs201.roles.Role;
import cs201.structures.Structure;

public class BusStop extends Structure
{
	List<Passenger> waitingPassengers;
	
	List<Passenger> addRequests;
	
	Bus parkedBus;
	
	public BusStop(int x, int y, int width, int height, int id)
	{
		super(x, y, width, height, id);
		waitingPassengers = Collections.synchronizedList(new ArrayList<Passenger>());
		addRequests = new ArrayList<Passenger>();
	}

	@Override
	public Role getRole(Intention role)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Passenger> getPassengerList(Bus bus)
	{
		if(parkedBus == null)
		{
			parkedBus = bus;
			return waitingPassengers;
		}
		return null;
	}
	
	public void addPassenger(Passenger p)
	{
		addRequests.add(p);
	}
	
	public void removePassengers(Bus bus, List<Passenger> passengers)
	{
		if(bus == parkedBus)
		{
			waitingPassengers.removeAll(passengers);
			waitingPassengers.addAll(addRequests);
			addRequests.clear();
			parkedBus = null;
		}
	}
}
