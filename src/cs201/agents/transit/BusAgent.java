package cs201.agents.transit;

import java.awt.geom.Rectangle2D.Double;
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
	
	enum PassengerState{None, Sitting, Leaving, InformedArrived};
	
	public List<MyPassenger> passengers;

	private List<Passenger> queriedList;
	
	List<Passenger> justAdded;
	
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
		
		queriedList = Collections.synchronizedList(new ArrayList<Passenger>());
		justAdded = Collections.synchronizedList(new ArrayList<Passenger>());
		
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
		for(MyPassenger pass : passengers)
		{
			if(pass.p == p)
			{
				pass.s = PassengerState.Leaving;
			}
		}
		stateChanged();
	}

	/**
	 * Message to signal that a passenger is staying on the bus
	 * @param p the passenger staying
	 */
	@Override
	public void msgStaying(Passenger p)
	{
		for(MyPassenger pass : passengers)
		{
			if(pass.p == p)
			{
				pass.s = PassengerState.Sitting;
			}
		}
		stateChanged();
	}

	/**
	 * Message indicating that a passenger has finished boarding
	 * @param p the passenger in question
	 */
	@Override
	public void msgDoneBoarding(Passenger p)
	{
		Do("Passenger "+p+" has boarded");
		queriedList.remove(p);
		justAdded.add(p);
		passengers.add(new MyPassenger(p));
		stateChanged();
	}

	/**
	 * Message indicating that a passenger is not boarding
	 * @param p the passenger in question
	 */
	@Override
	public void msgNotBoarding(Passenger p)
	{
		queriedList.remove(p);
		stateChanged();
	}
	
	@Override
	public boolean pickAndExecuteAnAction()
	{
		if(route == null)
		{
			return false;
		}
		
		for(int i = 0; i < passengers.size(); i++)
		{
			if(passengers.get(i).s == PassengerState.Leaving)
			{
				passengers.remove(i);
				return true;
			}
		}
		for(MyPassenger pass : passengers)
		{
			if(pass.s == PassengerState.InformedArrived)
			{
				return false;
			}
		}
		if(!queriedList.isEmpty())
		{
			return false;
		}
		
		goToNextStop();
		
		//always continues route
		return true;
	}

	/*
	 * Goes to next stop on the list
	 */
	private void goToNextStop()
	{
		BusStop current = (BusStop)currentLocation;
		current.removePassengers(this, justAdded);
		
		BusStop s = route.getNextStop();
		msgSetDestination(s);
		
		animate();
		
		for(MyPassenger pass : passengers)
		{
			pass.s = PassengerState.InformedArrived;
			pass.p.msgReachedDestination(s);
		}
		
		List<Passenger> newPassengers = s.getPassengerList(this);
		
		for(Passenger pass : newPassengers)
		{
			queriedList.add(pass);
			pass.msgPleaseBoard(this);
		}
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
