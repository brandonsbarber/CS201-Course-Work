package cs201.agents.transit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import cs201.helper.transit.BusRoute;
import cs201.interfaces.agents.transit.Bus;
import cs201.interfaces.roles.transit.Passenger;
import cs201.structures.transit.BusStop;

/**
 * 
 * @author Brandon
 *
 */
public class BusAgent extends VehicleAgent implements Bus
{
	class MyPassenger
	{
		Passenger p;
		PassengerState s;
		
		public MyPassenger(Passenger p)
		{
			this.p = p;
			this.s = PassengerState.None;
		}
	}
	
	enum PassengerState{None};
	
	public List<MyPassenger> passengers;
	List<MyPassenger> justBoarded;
	List<MyPassenger> removalList;
	
	BusRoute route;
	
	Semaphore sem;
	public boolean testing = false;
	
	/**
	 * Creates a bus with the given route and position along the route
	 * @param route the route to follow
 	 * @param stopNum how many stops in on the route to start on
	 */
	public BusAgent(BusRoute route,int stopNum)
	{
		passengers = Collections.synchronizedList(new ArrayList<MyPassenger>());
		justBoarded = new ArrayList<MyPassenger>();
		removalList = new ArrayList<MyPassenger>();
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
	
	/**
	 * Gets the bus route
	 * @retun the bus route
	 */
	public BusRoute getRoute()
	{
		return route;
	}
	
	/**
	 * Message to signal that a passenger is leaving the bus
	 * @param p the passenger leaving
	 */
	@Override
	public void msgLeaving(Passenger p)
	{
		removalList.add(new MyPassenger(p));
		sem.release();
	}

	/**
	 * Message to signal that a passenger is staying on the bus
	 * @param p the passenger staying
	 */
	@Override
	public void msgStaying(Passenger p)
	{
		sem.release();
	}

	/**
	 * Message indicating that a passenger has finished boarding
	 * @param p the passenger in question
	 */
	@Override
	public void msgDoneBoarding(Passenger p)
	{
		Do("Passenger "+p+" has boarded");
		passengers.add(new MyPassenger(p));
		justBoarded.add(new MyPassenger(p));
		sem.release();
	}

	/**
	 * Message indicating that a passenger is not boarding
	 * @param p the passenger in question
	 */
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

	/*
	 * Goes to next stop on the list
	 */
	private void goToNextStop()
	{
		BusStop s = route.getNextStop();
		msgSetDestination(s);
		
		animate();
		
		for(MyPassenger pass : passengers)
		{
			pass.p.msgReachedDestination(s);
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
			if(!testing )
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
		}
		
		//s.removePassengers(this,justBoarded);
		justBoarded.clear();
	}

	/**
	 * Gets how many passengers the bus has
	 * @return how many passengers the bus has
	 */
	public int getNumPassengers()
	{
		return passengers.size();
	}
}
