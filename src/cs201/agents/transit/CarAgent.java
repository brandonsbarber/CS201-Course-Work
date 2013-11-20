package cs201.agents.transit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import cs201.interfaces.transit.Car;
import cs201.interfaces.transit.Passenger;
import cs201.structures.Structure;

public class CarAgent extends VehicleAgent implements Car
{
	Passenger p;
	
	List<PickupRequest> pickups;
	
	class PickupRequest
	{
		Passenger p;
		Structure start;
		Structure destination;
		
		public PickupRequest(Passenger p, Structure s, Structure d)
		{
			this.p = p;
			start = s;
			destination = d;
		}
	};
	
	Semaphore sem;
	
	public CarAgent()
	{
		sem = new Semaphore(0,true);
		pickups = Collections.synchronizedList(new ArrayList<PickupRequest>());
	}
	
	@Override
	public void msgCallCar(Passenger p, Structure s, Structure d)
	{
		pickups.add(new PickupRequest(p,s,d));
		stateChanged();
	}

	@Override
	public void msgDoneBoarding(Passenger p)
	{
		sem.release();
	}

	@Override
	public void msgLeaving(Passenger p)
	{
		sem.release();
	}

	@Override
	protected boolean pickAndExecuteAnAction()
	{
		if(p != null)
		{
			if(destinationReached())
			{
				arrival();
				return true;
			}
			else
			{
				goToDestination();
				return true;
			}
		}
		else if(!pickups.isEmpty())
		{
			processPickup(pickups.remove(0));
			return true;
		}
		return false;
	}

	private void arrival()
	{
		p.msgReachedDestination(currentLocation);
		try
		{
			sem.acquire();
		}
		catch (InterruptedException e)
		{
			Do("Problem waiting.");
			e.printStackTrace();
		}
		p = null;
	}

	private void goToDestination()
	{
		//gui.doGoToDestination
	}
	
	private void processPickup(PickupRequest removed)
	{
		msgSetDestination(removed.start);
		//gui.doGoToDestination();
		try
		{
			sem.acquire();
		}
		catch (InterruptedException e)
		{
			Do("Problem waiting.");
			e.printStackTrace();
		}
		p = removed.p;
		msgSetDestination(removed.destination);
		//gui.doGoToDestination
	}
}
