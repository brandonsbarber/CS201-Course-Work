package cs201.test.transitTests;

import junit.framework.TestCase;
import cs201.agents.transit.CarAgent;
import cs201.interfaces.roles.transit.Passenger;
import cs201.test.mock.Brandon.transit.MockPassenger;

public class CarAgentTest extends TestCase
{
	CarAgent car;
	
	MockPassenger pass,pass2;
	
	public void setUp() throws Exception
	{
		car = new CarAgent();
		
		pass = new MockPassenger("Passenger 1");
	}
	
	public void testOnePassengerOneCar()
	{
		assertEquals("Passenger should have no logs",0,pass.log.size());
	}
}
