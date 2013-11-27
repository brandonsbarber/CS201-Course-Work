package cs201.agents.transit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import cs201.interfaces.agents.transit.Car;
import cs201.interfaces.roles.transit.Passenger;
import cs201.structures.Structure;

public class CarAgent extends VehicleAgent implements Car
{
	public boolean testing = false;
	public Passenger p;
	
	public List<PickupRequest> pickups;
	
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
		Do("Called by "+p+" to pickup at "+s+" and go to "+d);
		pickups.add(new PickupRequest(p,s,d));
		stateChanged();
	}

	@Override
	public void msgDoneBoarding(Passenger p)
	{
		Do("Passenger "+p+" is done boarding");
		sem.release();
	}

	@Override
	public void msgLeaving(Passenger p)
	{
		Do("Passenger "+p+" has left");
		sem.release();
	}

	@Override
	public boolean pickAndExecuteAnAction()
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

		if(!testing)
		{
			try
			{
				sem.acquire();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		p = null;
		if(gui != null)
		{
			gui.setPresent(false);
		}
	}

	private void goToDestination()
	{
		animate();
	}
	
	private void processPickup(PickupRequest removed)
	{
		msgSetDestination(removed.start);
				
		animate();
		
		removed.p.msgPleaseBoard(this);
		
		if(!testing)
		{
			try
			{
				sem.acquire();
			}
			catch (InterruptedException e)
			{
				Do("Problem waiting.");
				e.printStackTrace();
			}
		}
		p = removed.p;
		
		msgSetDestination(removed.destination);
	}

	public Passenger getPassenger()
	{
		return p;
	}
}
