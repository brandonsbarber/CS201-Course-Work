package cs201.test.mock.Brandon.transit;

import cs201.helper.transit.BusRoute;
import cs201.interfaces.agents.transit.Bus;
import cs201.interfaces.roles.transit.Passenger;
import cs201.structures.Structure;
import cs201.test.mock.LoggedEvent;
import cs201.test.mock.Mock;

public class MockBus extends Mock implements Bus
{
	BusRoute route;
	
	public MockBus(String name,BusRoute route)
	{
		super(name);
		this.route = route;
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void msgLeaving(Passenger p)
	{
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
