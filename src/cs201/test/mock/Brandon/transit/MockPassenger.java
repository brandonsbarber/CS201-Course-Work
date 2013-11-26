package cs201.test.mock.Brandon.transit;

import cs201.interfaces.agents.transit.Vehicle;
import cs201.interfaces.roles.transit.Passenger;
import cs201.roles.Role;
import cs201.structures.Structure;
import cs201.test.mock.LoggedEvent;
import cs201.test.mock.Mock;

public class MockPassenger extends Mock implements Passenger
{

	public MockPassenger(String name)
	{
		super(name);
	}

	@Override
	public void msgGoTo(Structure s)
	{
		log.add(new LoggedEvent("Received message GoTo "+s));
	}

	@Override
	public void msgPleaseBoard(Vehicle v)
	{
		log.add(new LoggedEvent("Received message to board "+v));
	}

	@Override
	public void msgReachedDestination(Structure s)
	{
		log.add(new LoggedEvent("Received message arrived at "+s));
	}

}
