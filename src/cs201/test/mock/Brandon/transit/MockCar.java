package cs201.test.mock.Brandon.transit;

import java.awt.Point;

import cs201.interfaces.agents.transit.Car;
import cs201.interfaces.roles.transit.Passenger;
import cs201.structures.Structure;
import cs201.test.mock.LoggedEvent;
import cs201.test.mock.Mock;

public class MockCar extends Mock implements Car
{

	public MockCar(String name)
	{
		super(name);
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
	public void msgLeaving(Passenger p)
	{
		log.add(new LoggedEvent("Passenger "+p+" is leaving."));
	}


	@Override
	public boolean destinationReached() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void msgCallCar(Passenger p, Structure s, Structure d) {
		log.add(new LoggedEvent("Passenger "+p+" has asked me to pick up from "+s+" and go to "+d));
	}

	@Override
	public void msgDoneBoarding(Passenger p) {
		log.add(new LoggedEvent("Passenger "+p+" is done boarding."));
	}

	@Override
	public void msgCallCar(Passenger p, Point point, Structure destination) {
		// TODO Auto-generated method stub
		
	}

}
