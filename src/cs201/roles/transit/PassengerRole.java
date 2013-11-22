package cs201.roles.transit;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

import cs201.agents.PersonAgent.Intention;
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
	public Structure destination;
	public Structure currentLocation;
	
	public List<Vehicle> boardingRequest;
	
	public Vehicle currentVehicle;
	
	Car car;
	
	public Queue<Move> waypoints;
	
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
	
	public PassengerRole(Structure curLoc)
	{
		boardingRequest = new ArrayList<Vehicle>();
		waypoints = new LinkedList<Move>();
		
		state = PassengerState.None;
		this.currentLocation = curLoc;
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
	}

	@Override
	public void msgPleaseBoard(Vehicle v)
	{
		boardingRequest.add(v);
	}

	@Override
	public void msgReachedDestination(Structure s)
	{
		currentLocation = s;
		state = PassengerState.Arrived;
	}

	@Override
	public void startInteraction(Intention intent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean pickAndExecuteAnAction()
	{
		if(currentLocation == destination && state == PassengerState.None)
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
			waypoints.add (new Move(destination, MoveType.Car));
			return;
		}
		
		List<BusStop> stops = new ArrayList<BusStop>();
		//Find nearest two bus stops
		if(stops.size() == 2)
		{
			waypoints.add(new Move(stops.get(0),MoveType.Walk));
			waypoints.add(new Move(stops.get(1),MoveType.Bus));
			waypoints.add(new Move(destination,MoveType.Walk));
		}
		else
		{
			waypoints.add(new Move(destination,MoveType.Walk));
		}
	}

	private void finishMoving()
	{
		//message person done moving
		setActive(false);
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
			}
		}
		else if(remove instanceof Car)
		{
			Car car = (Car)remove;
			car.msgDoneBoarding(this);
			boardingRequest.clear();
			currentVehicle = remove;
		}
	}
	
	private void processArrival()
	{
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
				state = PassengerState.None;
			}
		}
		else if (currentVehicle instanceof Bus)
		{
			((Bus)currentVehicle).msgStaying(this);
		}
	}
	
	private void moveToLocation(Move point)
	{
		switch(point.m)
		{
			case Walk :
				/*gui.doGoToStructure(point.s);*/
				currentLocation = point.s;
				state = PassengerState.Arrived;
				break;
			case Car :
				car.msgCallCar(this, currentLocation, destination);
				break;
			case Bus : 
				((BusStop)currentLocation).addPassenger(this);
				break;
		}
	}

	@Override
	public void closingTime() {
		// TODO Auto-generated method stub
		
	}
}
