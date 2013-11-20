package cs201.interfaces.transit;

public interface Bus extends Vehicle
{
	public void msgLeaving (Passenger p);
	
	public void msgStaying (Passenger p);
	
	public void msgDoneBoarding (Passenger p);
	
	public void msgNotBoarding(Passenger p);
}
