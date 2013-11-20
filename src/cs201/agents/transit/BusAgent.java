package cs201.agents.transit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import cs201.helper.transit.BusRoute;
import cs201.interfaces.transit.Bus;
import cs201.interfaces.transit.Passenger;
import cs201.structures.transit.BusStop;

public class BusAgent extends VehicleAgent implements Bus
{
	List<Passenger> passengers;
	List<Passenger> justBoarded;
	
	BusRoute route;
	
	Semaphore sem;
	
	public BusAgent(BusRoute route)
	{
		passengers = Collections.synchronizedList(new ArrayList<Passenger>());
		this.route = route;
		sem = new Semaphore(0);
	}
	
	public BusRoute getRoute()
	{
		return route;
	}
	
	@Override
	public void msgLeaving(Passenger p)
	{
		passengers.remove(p);
		sem.release();
	}

	@Override
	public void msgStaying(Passenger p)
	{
		sem.release();
	}

	@Override
	public void msgDoneBoarding(Passenger p)
	{
		passengers.add(p);
		justBoarded.add(p);
	}

	@Override
	public void msgNotBoarding(Passenger p) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}
}
