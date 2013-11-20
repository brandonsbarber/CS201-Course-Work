package cs201.roles.transit;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

import cs201.agents.PersonAgent.Intention;
import cs201.interfaces.transit.Car;
import cs201.interfaces.transit.Passenger;
import cs201.interfaces.transit.Vehicle;
import cs201.roles.Role;
import cs201.structures.Structure;

public class PassengerRole extends Role implements Passenger
{
	Structure destination;
	Structure currentLocation;
	
	List<Vehicle> boardingRequest;
	
	Vehicle currentVehicle;
	
	Car car;
	
	Queue<Move> waypoints;
	
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
	
	enum PassengerState {None,Waiting,Boarding,InTransit,Arrived};
	PassengerState state;
	
	Semaphore waitingForVehicle;
	
	public PassengerRole(Structure currentLocation)
	{
		boardingRequest = new ArrayList<Vehicle>();
		waypoints = new LinkedList<Move>();
		
		state = PassengerState.None;
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
		waitingForVehicle.release();
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
		if(currentLocation == destination)
		{
			finishMoving();
			return true;
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
		// TODO Auto-generated method stub
		
	}

	private void finishMoving()
	{
		// TODO Auto-generated method stub
		
	}
	
	private void checkBoardingRequest(Vehicle remove)
	{
		// TODO Auto-generated method stub
		
	}
	
	private void processArrival()
	{
		
	}
	
	private void moveToLocation(Move point)
	{
		
	}

	@Override
	public void closingTime() {
		// TODO Auto-generated method stub
		
	}
}
