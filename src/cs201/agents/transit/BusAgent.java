package cs201.agents.transit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import cs201.helper.transit.BusRoute;
import cs201.interfaces.agents.transit.Bus;
import cs201.interfaces.roles.transit.Passenger;
import cs201.structures.transit.BusStop;

public class BusAgent extends VehicleAgent implements Bus
{
	public List<Passenger> passengers;
	List<Passenger> justBoarded;
	List<Passenger> removalList;
	
	BusRoute route;
	
	Semaphore sem;
	
	public BusAgent(BusRoute route,int stopNum)
	{
		passengers = Collections.synchronizedList(new ArrayList<Passenger>());
		justBoarded = new ArrayList<Passenger>();
		removalList = new ArrayList<Passenger>();
		this.route = route;
		sem = new Semaphore(0);
		
		for(int i = 0; i < stopNum; i++)
		{
			System.out.println(route.getNextStop());
		}
		
		msgSetLocation(route.getNextStop());
		System.out.println("Starting at : "+currentLocation);
		stateChanged();
	}
	
	public BusRoute getRoute()
	{
		return route;
	}
	
	@Override
	public void msgLeaving(Passenger p)
	{
		removalList.add(p);
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
		Do("Passenger "+p+" has boarded");
		passengers.add(p);
		justBoarded.add(p);
		sem.release();
	}

	@Override
	public void msgNotBoarding(Passenger p)
	{
		sem.release();
	}
	
	@Override
	public boolean pickAndExecuteAnAction()
	{
		if(route != null)
		{
			goToNextStop();
			return true;
		}
		return false;
	}

	private void goToNextStop()
	{
		BusStop s = route.getNextStop();
		msgSetDestination(s);
		
		animate();
		
		for(Passenger pass : passengers)
		{
			pass.msgReachedDestination(s);
			try
			{
				sem.acquire();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		passengers.removeAll(removalList);
		removalList.clear();
		
		List<Passenger> newPassengers = s.getPassengerList(this);
		
		for(Passenger pass : newPassengers)
		{
			pass.msgPleaseBoard(this);
			try
			{
				sem.acquire();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
		s.removePassengers(this,justBoarded);
		justBoarded.clear();
	}

	public int getNumPassengers()
	{
		return passengers.size();
	}
}
