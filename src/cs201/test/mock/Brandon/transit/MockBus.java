package cs201.test.mock.Brandon.transit;

import java.util.ArrayList;
import java.util.List;

import cs201.helper.transit.BusRoute;
import cs201.interfaces.agents.transit.Bus;
import cs201.interfaces.roles.transit.Passenger;
import cs201.structures.Structure;
import cs201.test.mock.LoggedEvent;
import cs201.test.mock.Mock;

public class MockBus extends Mock implements Bus
{
	BusRoute route;
	public List<Passenger> passengers;
	
	public MockBus(String name,BusRoute route)
	{
		super(name);
		this.route = route;
		passengers = new ArrayList<Passenger>();
	}

	@Override
	public void msgSetDestination(Structure s)
	{
		log.add(new LoggedEvent("Setting destination to structure "+s.toString()));
	}

	@Override
	public void msgSetLocation(Structure s)
	{
		log.add(new LoggedEvent("Setting location to structure "+s.toString()));	
	}

	@Override
	public boolean destinationReached()
	{
		return false;
	}

	@Override
	public void msgLeaving(Passenger p)
	{
		passengers.remove(p);
		System.out.println("removing passenger");
		log.add(new LoggedEvent("Passenger "+p+" is leaving."));
	}

	@Override
	public void msgStaying(Passenger p)
	{
		log.add(new LoggedEvent("Passenger "+p+" is staying."));
	}

	@Override
	public void msgDoneBoarding(Passenger p)
	{
		passengers.add(p);
		log.add(new LoggedEvent("Passenger "+p+" is done boarding."));
	}

	@Override
	public void msgNotBoarding(Passenger p)
	{
		log.add(new LoggedEvent("Passenger "+p+" is not boarding."));
	}

	@Override
	public BusRoute getRoute()
	{
		return route;
	}

}
