package cs201.agents.transit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import cs201.interfaces.agents.transit.Car;
import cs201.interfaces.roles.transit.Passenger;
import cs201.structures.Structure;

/**
 * 
 * @author Brandon
 *
 */
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
	
	/**
	 * Creates a new car
	 */
	public CarAgent()
	{
		sem = new Semaphore(0,true);
		pickups = Collections.synchronizedList(new ArrayList<PickupRequest>());
		print("I am at location: "+currentLocation);
	}
	
	/**
	 * Message for calling a car for pickup by a passenger
	 * @param p the passenger calling for the car
	 * @param s the location of the passenger
	 * @param d the desired location for the passenger
	 */
	@Override
	public void msgCallCar(Passenger p, Structure s, Structure d)
	{
		Do("Called by "+p+" to pickup at "+s+" and go to "+d);
		pickups.add(new PickupRequest(p,s,d));
		stateChanged();
	}

	/**
	 * Message indicating that a passenger has finished boarding
	 * @param p the passenger in question
	 */
	@Override
	public void msgDoneBoarding(Passenger p)
	{
		Do("Passenger "+p+" is done boarding");
		sem.release();
	}

	/**
	 * Message to signal that a passenger is leaving the car
	 * @param p the passenger leaving
	 */
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
	
	/*
	 * Processes arrival at destination
	 */
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
	
	/*
	 * Goes to destination
	 */
	private void goToDestination()
	{
		animate();
	}
	
	/*
	 * Picks up passenger
	 */
	private void processPickup(PickupRequest removed)
	{
		if(currentLocation == null)
		{
			msgSetLocation(removed.start);
		}
		
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

	/**
	 * Gets the current passenger of the car
	 * @return current passenger
	 */
	public Passenger getPassenger()
	{
		return p;
	}
}
