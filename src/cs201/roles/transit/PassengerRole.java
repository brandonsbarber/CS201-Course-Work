package cs201.roles.transit;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
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
		
		public Move(Structure struct, MoveType move)
		{
			s = struct;
			m = move;
		}
	};
	
	enum MoveType {Walk,Bus,Car};
	
	public enum PassengerState {None,Waiting,Boarding,InTransit,Arrived};
	public PassengerState state;
	
	Semaphore waitingForVehicle;
	Semaphore animationPause;
	
	PassengerGui gui;
	
	public static final int START_WALK_DISTANCE = 200;
	
	private int walkDistance;
	
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
	
	public void setBusStops(List<BusStop> stops)
	{
		this.busStops = stops;
	}
	
	public void isTesting()
	{
		testing = true;
	}
	
	public void setGui(PassengerGui gui)
	{
		this.gui = gui;
		gui.setPresent(true);
	}
	
	public void addCar(Car c)
	{
		this.car = c;
	}
	
	@Override
	public void msgGoTo(Structure s)
	{
		destination = s;
		state = PassengerState.None;
		waypoints.clear();
		Do("Received message to go to: "+s);
		stateChanged();
	}

	@Override
	public void msgPleaseBoard(Vehicle v)
	{
		boardingRequest.add(v);
		Do("Received message to board: "+v);
		waitingForVehicle.release();
	}

	@Override
	public void msgReachedDestination(Structure s)
	{
		currentLocation = s;
		state = PassengerState.Arrived;
		Do("Received message arrived at destination: "+s);
		stateChanged();
	}

	@Override
	public void startInteraction(Intention intent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean pickAndExecuteAnAction()
	{
		if(currentLocation == destination && state == PassengerState.None && waypoints.isEmpty())
		{
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

	

	private void populateWaypoints()
	{
		if (car != null)
		{
			Do("Populating waypoints for car");
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
			double stopDistance = Math.sqrt(Math.pow(stop.x - currentLocation.x,2) + Math.pow(stop.y - currentLocation.y,2));
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
			double stopDistance = Math.sqrt(Math.pow(destination.x - stop.x,2) + Math.pow(destination.y - stop.y,2));
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
			Do("Populating waypoints for bus");
			waypoints.add(new Move(stops.get(0),MoveType.Walk));
			waypoints.add(new Move(stops.get(1),MoveType.Bus));
			waypoints.add(new Move(stops.get(1),MoveType.Walk));
			waypoints.add(new Move(destination,MoveType.Walk));
		}
		else
		{
			Do("Populating waypoints for walking");
			waypoints.add(new Move(destination,MoveType.Walk));
		}
	}

	public boolean shouldWalk()
	{
		double distance = Math.sqrt(Math.pow(destination.x - currentLocation.x,2) + Math.pow(destination.y - currentLocation.y,2));
		return distance < walkDistance;
	}
	
	private void finishMoving()
	{
		//message person done moving
		setActive(false);
		gui.setPresent(false);
		if(myPerson != null)
		{
			myPerson.doneMoving(currentLocation);
		}
	}
	
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
	
	private void processArrival()
	{
		Do("Processing arrival at "+currentLocation);
		if(currentLocation == waypoints.peek().s)
		{
			Structure s = waypoints.remove().s;
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
					gui.setLocation((int)currentLocation.x, (int)currentLocation.y);
				}
			}
			else
			{
				gui.setLocation((int)currentLocation.x,(int)currentLocation.y);
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

	@Override
	public void msgClosingTime() {
		// TODO Auto-generated method stub
		
	}

	public void msgAnimationFinished()
	{
		Do("Animation has finished");
		animationPause.release();
	}

	public void setCurrentLocation(Structure s2)
	{
		currentLocation = s2;
		gui.setLocation((int)currentLocation.x, (int)currentLocation.y);
	}

	public Structure getCurrentLocation()
	{
		return currentLocation;
	}

	public void setWalkingDistance(int i)
	{
		walkDistance = i;
	}
}
