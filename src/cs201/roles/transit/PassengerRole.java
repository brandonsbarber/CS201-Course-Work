package cs201.roles.transit;

import cs201.agents.PersonAgent.Intention;
import cs201.interfaces.transit.Passenger;
import cs201.interfaces.transit.Vehicle;
import cs201.roles.Role;
import cs201.structures.Structure;

public class PassengerRole extends Role implements Passenger
{
	@Override
	public void msgGoTo(Structure s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPleaseBoard(Vehicle v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReachedDestination(Structure s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startInteraction(Intention intent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void closingTime() {
		// TODO Auto-generated method stub
		
	}
}
