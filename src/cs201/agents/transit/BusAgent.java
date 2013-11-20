package cs201.agents.transit;

import java.util.List;

import cs201.interfaces.transit.Bus;
import cs201.interfaces.transit.Passenger;

public class BusAgent extends VehicleAgent implements Bus
{
	List<Passenger> passengers;
	
	
	
	@Override
	public void msgLeaving(Passenger p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgStaying(Passenger p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDoneBoarding(Passenger p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgNotBoarding(Passenger p) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}
}
