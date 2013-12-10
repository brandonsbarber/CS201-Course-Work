package cs201.roles.transit;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.transit.PassengerGui;
import cs201.helper.transit.BusRoute;
import cs201.interfaces.agents.transit.Bus;
import cs201.interfaces.agents.transit.Car;
import cs201.interfaces.agents.transit.Vehicle;
import cs201.interfaces.roles.transit.Passenger;
import cs201.roles.Role;
import cs201.structures.Structure;
import cs201.structures.transit.BusStop;
import cs201.trace.AlertLog;
import cs201.trace.AlertTag;

/**
 * 
 * @author Brandon
 *
 */
public class PassengerRole extends Role implements Passenger
{
	public boolean testing = false;
	
	public Structure destination;
	public Structure currentLocation;
	
	public List<Vehicle> boardingRequest;
	
	public Vehicle currentVehicle;
	
	private Car car;
	
	public Queue<Move> waypoints;
	
	private List<BusStop> busStops;
	
	class Move
	{
		Structure s;
		MoveType m;
		Point p;
		
		public Move(Structure struct, MoveType move)
		{
			s = struct;
			m = move;
			p = s.getEntranceLocation();
		}
	};
	
	enum MoveType {Walk,Bus,Car};
	
	public enum PassengerState {None,Waiting,Boarding,InTransit,Arrived,Roaming};
	public PassengerState state;
	
	Semaphore waitingForVehicle;
	Semaphore animationPause;
	
	PassengerGui gui;
	
	public static final int START_WALK_DISTANCE = 200;
	
	private int walkDistance;
	
	/**
	 * Creates a Passenger at the given location
	 * @param curLoc
	 */
	public PassengerRole(Structure curLoc)
	{
		boardingRequest = new ArrayList<Vehicle>();
		waypoints = new LinkedList<Move>();
		
		animationPause = new Semaphore(0,true);
		
		state = PassengerState.None;
		this.currentLocation = curLoc;
		
		waitingForVehicle = new Semaphore(0);
		
		walkDistance = START_WALK_DISTANCE;
		
		busStops = new ArrayList<BusStop>();
	}
	
	/**
	 * Sets the bus stops to be referenced by the passenger
	 * Note: Bus will not be taken unless this is called.
	 * @param stops the stops to be used to take the bus
	 */
	public void setBusStops(List<BusStop> stops)
	{
		this.busStops = stops;
	}
	
	/**
	 * Sets testing mode on PassengerRole
	 */
	public void isTesting()
	{
		testing = true;
	}
	
	/**
	 * Sets gui to use
	 * @param gui the gui to use
	 */
	public void setGui(PassengerGui gui)
	{
		this.gui = gui;
	}
	
	/**
	 * Gives a car to the passenger
	 * Note: Car will not be taken unless this is called
	 * @param c the car to use
	 */
	public void addCar(Car c)
	{
		AlertLog.getInstance().logMessage(AlertTag.TRANSIT,getName(),"I have a car");
		this.car = c;
	}

	/**
	 * Does nothing
	 */
	@Override
	public void msgClosingTime() 
	{
		
	}
	
	public void msgStartRoaming()
	{
		if(state != PassengerState.Roaming)
		{
			setCurrentLocation(currentLocation);
			state = PassengerState.Roaming;
		}
		stateChanged();
	}

	/**
	 * Signals that the animation is finished performing
	 */
	public void msgAnimationFinished()
	{
		AlertLog.getInstance().logDebug(AlertTag.TRANSIT,getName(),"Animation has finished");
		animationPause.release();
	}

	/**
	 * Sets the current location
	 * @param s2 current location
	 */
	public void setCurrentLocation(Structure s2)
	{
		AlertLog.getInstance().logDebug(AlertTag.TRANSIT,""+getName(),"Current location set to  "+s2);
		currentLocation = s2;
		if(gui != null){gui.setLocation((int)currentLocation.getEntranceLocation().x, (int)currentLocation.getEntranceLocation().y);}
	}

	/**
	 * Get the current location
	 */
	public Structure getCurrentLocation()
	{
		return currentLocation;
	}

	/**
	 * Sets walking distance for passenger
	 * @param i walking distance
	 */
	public void setWalkingDistance(int i)
	{
		walkDistance = i;
	}
	
	/**
	 * Tells the Passenger to go to this location
	 * @param s the structure to go to
	 */
	@Override
	public void msgGoTo(Structure s)
	{
		destination = s;
		state = PassengerState.None;
		waypoints.clear();
		AlertLog.getInstance().logMessage(AlertTag.TRANSIT,""+getName(),"Received message to go to: "+s);
		stateChanged();
	}

	/**
	 * Message asking the passenger to board a vehicle
	 * @param v the vehicle asking to board
	 */
	@Override
	public void msgPleaseBoard(Vehicle v)
	{
		boardingRequest.add(v);
		AlertLog.getInstance().logMessage(AlertTag.TRANSIT,getName(),"Received message to board: "+v);
		waitingForVehicle.release();
	}

	/**
	 * Message called when arriving at a destination
	 * @param s the destination arrived at
	 */
	@Override
	public void msgReachedDestination(Structure s)
	{
		currentLocation = s;
		state = PassengerState.Arrived;
		AlertLog.getInstance().logMessage(AlertTag.TRANSIT,getName(),"Received message arrived at destination: "+s);
		stateChanged();
	}

	/**
	 * Does nothing
	 */
	@Override
	public void startInteraction(Intention intent)
	{
		
	}

	@Override
	public boolean pickAndExecuteAnAction()
	{
		if(state == PassengerState.Roaming)
		{
			roam();
			return false;
		}
		if(currentLocation == destination && !gui.locationEquals(currentLocation))
		{
			gui.doGoToLocation(currentLocation.getEntranceLocation().x, currentLocation.getEntranceLocation().y);
			try
			{
				animationPause.acquire();
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
			return true;
		}
		if(currentLocation == destination && state == PassengerState.None && waypoints.isEmpty())
		{
			System.out.println("REACHED DESTINATION");
			finishMoving();
			return false;
		}
		if(state == PassengerState.None && waypoints.isEmpty())
		{
			populateWaypoints();
			return true;
		}
		if(!boardingRequest.isEmpty())
		{
			checkBoardingRequest(boardingRequest.remove(0));
			return true;
		}
		if(state == PassengerState.Arrived)
		{
			processArrival();
			return true;
		}
		if(state != PassengerState.InTransit)
		{
			moveToLocation(waypoints.peek());
			return true;
		}
		return false;
	}

	private void roam()
	{
		System.out.println("ROAMING");
		gui.doRoam();
		//gui.startRoaming(); DO NOT ACQUIRE anymore; setActive(false);
		try
		{
			animationPause.acquire();
		}
		catch (InterruptedException e1)
		{
			e1.printStackTrace();
		}
		/*gui.doGoToLocation(currentLocation);
		try
		{
			animationPause.acquire();
		}
		catch (InterruptedException e1)
		{
			e1.printStackTrace();
		}*/
		//gui.setPresent(false);
		setActive(false);
	}

	/*
	 * Figures out how to break down move
	 */
	private void populateWaypoints()
	{
		if (car != null)
		{
			AlertLog.getInstance().logMessage(AlertTag.TRANSIT,getName(),"Populating waypoints for car");
			waypoints.add (new Move(destination, MoveType.Car));
			waypoints.add (new Move(destination,MoveType.Walk));
			return;
		}
		
		
		List<BusStop> stops = new ArrayList<BusStop>();
		
		//find closest to current location
		BusStop closest = null;
		double minDistance = Double.MAX_VALUE;
		
		for(BusStop stop : busStops)
		{
			double stopDistance = Math.sqrt(Math.pow(stop.getRect().x - currentLocation.getRect().x,2) + Math.pow(stop.getRect().y - currentLocation.getRect().y,2));
			if(stopDistance < minDistance)
			{
				closest = stop;
				minDistance = stopDistance;
			}
		}
		if(closest != null)
		{
			stops.add(closest);
		}
		
		//find closest to destination
		closest = null;
		minDistance = Double.MAX_VALUE;
		
		for(BusStop stop : busStops)
		{
			double stopDistance = Math.sqrt(Math.pow(destination.getRect().x - stop.getRect().x,2) + Math.pow(destination.getRect().y - stop.getRect().y,2));
			if(stopDistance < minDistance)
			{
				closest = stop;
				minDistance = stopDistance;
			}
		}
		if(closest != null && !stops.contains(closest))
		{
			stops.add(closest);
		}
		
		if(!shouldWalk() && stops.size() == 2)
		{
			AlertLog.getInstance().logMessage(AlertTag.TRANSIT,getName(),"Populating waypoints for bus");
			waypoints.add(new Move(stops.get(0),MoveType.Walk));
			waypoints.add(new Move(stops.get(1),MoveType.Bus));
			waypoints.add(new Move(stops.get(1),MoveType.Walk));
			waypoints.add(new Move(destination,MoveType.Walk));
		}
		else
		{
			AlertLog.getInstance().logMessage(AlertTag.TRANSIT,getName(),"Populating waypoints for walking");
			waypoints.add(new Move(destination,MoveType.Walk));
		}
	}

	/*
	 * Determines if walking is worth it
	 */
	public boolean shouldWalk()
	{
		double distance = Math.sqrt(Math.pow(destination.getRect().x - currentLocation.getRect().x,2) + Math.pow(destination.getRect().y - currentLocation.getRect().y,2));
		return distance < walkDistance;
	}
	
	private void finishMoving()
	{
		//message person done moving
		gui.setPresent(false);
		setActive(false);
		if(myPerson != null)
		{
			myPerson.doneMoving(currentLocation);
			System.out.println("TELLING DONE MOVING");
		}
		AlertLog.getInstance().logMessage(AlertTag.TRANSIT,""+getName(),"Done moving");
	}
	
	/*
	 * Processes a vehicle asking to board
	 */
	private void checkBoardingRequest(Vehicle remove)
	{
		if(remove instanceof Bus)
		{
			Bus bus = (Bus)remove;
			BusRoute route = bus.getRoute();
			if(route.hasStop((BusStop)waypoints.peek().s))
			{
				bus.msgDoneBoarding(this);
				state = PassengerState.InTransit;
				boardingRequest.clear();
				gui.setPresent(false);
				currentVehicle = bus;
			}
		}
		else if(remove instanceof Car)
		{
			Car car = (Car)remove;
			if(car == this.car)
			{
				car.msgDoneBoarding(this);
				boardingRequest.clear();
				currentVehicle = remove;
				state = PassengerState.InTransit;
				gui.setPresent(false);
			}
		}
	}
	
	/*
	 * Processes arriving at a location
	 */
	private void processArrival()
	{
		AlertLog.getInstance().logMessage(AlertTag.TRANSIT,getName(),"Processing arrival at "+currentLocation);
		if(currentLocation == waypoints.peek().s)
		{
			waypoints.remove();	//this is why I shouldn't listen to Eclipse!
			AlertLog.getInstance().logMessage(AlertTag.TRANSIT,getName(),"At proper location.");
			if(currentVehicle != null)
			{
				if(currentVehicle instanceof Car)
				{
					((Car)currentVehicle).msgLeaving(this);
				}
				else if(currentVehicle instanceof Bus)
				{
					((Bus)currentVehicle).msgLeaving(this);
				}
				currentVehicle = null;
				if(currentLocation.getParkingLocation() != null)
				{
					gui.setLocation(currentLocation.getParkingLocation().x, currentLocation.getParkingLocation().y);
				}
				else
				{
					gui.setLocation((int)currentLocation.getRect().x, (int)currentLocation.getRect().y);
				}
			}
			else
			{
				gui.setLocation((int)currentLocation.getRect().x,(int)currentLocation.getRect().y);
			}
			gui.setPresent(true);
			state = PassengerState.None;
		}
		else if (currentVehicle instanceof Bus)
		{
			((Bus)currentVehicle).msgStaying(this);
			state = PassengerState.InTransit;
		}
	}
	
	/*
	 * Moves to a location
	 */
	private void moveToLocation(Move point)
	{
		switch(point.m)
		{
			case Walk :
				if(!testing)
				{
					gui.doGoToLocation(point.s);
					try
					{
						animationPause.acquire();
					}
					catch (InterruptedException e1)
					{
						e1.printStackTrace();
					}
				}
				currentLocation = point.s;
				state = PassengerState.Arrived;
				break;
			case Car :
				car.msgCallCar(this, currentLocation, destination);
				
				if(!testing)
				{
					try
					{
						waitingForVehicle.acquire();
					}
					catch(InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				
				break;
			case Bus : 
				((BusStop)currentLocation).addPassenger(this);
				if(!testing)
				{
					try
					{
						waitingForVehicle.acquire();
					}
					catch(InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				break;
		}
	}

	public boolean isAtLocation(Structure location)
	{
		return gui.locationEquals(location);
	}
}
