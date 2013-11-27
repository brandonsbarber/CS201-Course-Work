package cs201.test.transitTests;

import junit.framework.TestCase;
import cs201.agents.transit.CarAgent;
import cs201.structures.Structure;
import cs201.structures.transit.BusStop;
import cs201.test.mock.Brandon.transit.MockPassenger;

/**
 * 
 * @author Brandon
 *
 */
public class CarAgentTest extends TestCase
{
	CarAgent car;
	
	MockPassenger pass,pass2;
	
	Structure s1,s2;
	
	/**
	 * Sets up testing
	 */
	public void setUp() throws Exception
	{
		car = new CarAgent();
		car.testing = true;
		
		pass = new MockPassenger("Passenger 1");
		pass2 = new MockPassenger("Passenger 2");
		
		s1 = new BusStop(0, 0, 0, 0, 0, null);
		s2 = new BusStop(20, 20, 0, 0, 1, null);
	}
	
	/**
	 * Tests one passenger trying to get in a car
	 */
	public void testOnePassengerOneCar()
	{
		assertEquals("Passenger should have no logs",0,pass.log.size());
		assertTrue("Car should not have a passenger",car.p == null);
		
		car.msgCallCar(pass, s1, s2);
		
		assertEquals("Car should have a pickup request",1,car.pickups.size());
		assertTrue("Car's destination should still be null",null == car.destination);
		
		assertTrue("Scheduler should be true",car.pickAndExecuteAnAction());

		assertEquals("Car's location should be s1",s1,car.currentLocation);
		assertEquals("Car's destination should be s2",s2,car.destination);
		assertEquals("Car's passenger should be pass",pass,car.p);
		
		assertTrue("Scheduler should be true",car.pickAndExecuteAnAction());
		
		assertEquals("Car's location should be s2",s2,car.currentLocation);
		assertEquals("Car's passenger should be pass",pass,car.p);
		
		assertTrue("Scheduler should be true",car.pickAndExecuteAnAction());
		
		assertEquals("Passenger's log should have two entries",2,pass.log.size());
		assertEquals("Car's location should be s2",s2,car.currentLocation);
		assertEquals("Car's passenger should be null",null,car.p);
		
		assertFalse("Scheduler should return false",car.pickAndExecuteAnAction());
	}
	
	/**
	 * Tests two passengers asking for the car, one after another
	 */
	public void testTwoPassengerOneCarSequential()
	{
		assertEquals("Passenger should have no logs",0,pass.log.size());
		assertTrue("Car should not have a passenger",car.p == null);
		
		car.msgCallCar(pass, s1, s2);
		
		assertEquals("Car should have a pickup request",1,car.pickups.size());
		assertEquals("Car's destination should be null",null, car.destination);
		
		assertTrue("Scheduler should be true",car.pickAndExecuteAnAction());

		assertEquals("Car's location should be s1",s1,car.currentLocation);
		assertEquals("Car's destination should be s2",s2,car.destination);
		assertEquals("Car's passenger should be pass",pass,car.p);
		
		assertTrue("Scheduler should be true",car.pickAndExecuteAnAction());
		
		assertEquals("Car's location should be s2",s2,car.currentLocation);
		assertEquals("Car's passenger should be pass",pass,car.p);
		
		assertTrue("Scheduler should be true",car.pickAndExecuteAnAction());
		
		assertEquals("Passenger's log should have two entries",2,pass.log.size());
		assertEquals("Car's location should be s2",s2,car.currentLocation);
		assertEquals("Car's passenger should be null",null,car.p);
		
		//Second passenger
		assertEquals("Passenger2 should have no logs",0,pass2.log.size());
		assertTrue("Car should not have a passenger",car.p == null);
		
		car.msgCallCar(pass2, s1, s2);
		
		assertEquals("Car should have a pickup request",1,car.pickups.size());
		assertEquals("Car's destination should be s2",s2,car.destination);
		
		assertTrue("Scheduler should be true",car.pickAndExecuteAnAction());

		assertEquals("Car's location should be s1",s1,car.currentLocation);
		assertEquals("Car's destination should be s2",s2,car.destination);
		assertEquals("Car's passenger should be pass2",pass2,car.p);
		
		assertTrue("Scheduler should be true",car.pickAndExecuteAnAction());
		
		assertEquals("Car's location should be s2",s2,car.currentLocation);
		assertEquals("Car's passenger should be pass2",pass2,car.p);
		
		assertTrue("Scheduler should be true",car.pickAndExecuteAnAction());
		
		assertEquals("Passenger2's log should have two entries",2,pass2.log.size());
		assertEquals("Car's location should be s2",s2,car.currentLocation);
		assertEquals("Car's passenger should be null",null,car.p);
		
		assertFalse("Scheduler should return false",car.pickAndExecuteAnAction());
	}
	
	/**
	 * Tests two passengers asking for the same car at the same time
	 */
	public void testTwoPassengerOneCarSimultaneous()
	{
		assertEquals("Passenger should have no logs",0,pass.log.size());
		assertTrue("Car should not have a passenger",car.p == null);
		
		car.msgCallCar(pass, s1, s2);
		
		assertEquals("Car should have a pickup request",1,car.pickups.size());
		assertEquals("Car's destination should be null",null, car.destination);
		
		//Second passenger
		assertEquals("Passenger2 should have no logs",0,pass2.log.size());
		assertTrue("Car should not have a passenger",car.p == null);
		
		car.msgCallCar(pass2, s1, s2);
		
		assertEquals("Car should have two pickup request",2,car.pickups.size());
		assertEquals("Car's destination should be null",null, car.destination);
		
		assertTrue("Scheduler should be true",car.pickAndExecuteAnAction());

		assertEquals("Car's location should be s1",s1,car.currentLocation);
		assertEquals("Car's destination should be s2",s2,car.destination);
		assertEquals("Car's passenger should be pass",pass,car.p);
		
		assertTrue("Scheduler should be true",car.pickAndExecuteAnAction());
		
		assertEquals("Car's location should be s2",s2,car.currentLocation);
		assertEquals("Car's passenger should be pass",pass,car.p);
		
		assertTrue("Scheduler should be true",car.pickAndExecuteAnAction());
		
		assertEquals("Passenger's log should have two entries",2,pass.log.size());
		assertEquals("Car's location should be s2",s2,car.currentLocation);
		assertEquals("Car's passenger should be null",null,car.p);
		
		assertEquals("Car should have a pickup request",1,car.pickups.size());
		assertEquals("Car's destination should be s2",s2,car.destination);
		
		assertTrue("Scheduler should be true",car.pickAndExecuteAnAction());

		assertEquals("Car's location should be s1",s1,car.currentLocation);
		assertEquals("Car's destination should be s2",s2,car.destination);
		assertEquals("Car's passenger should be pass2",pass2,car.p);
		
		assertTrue("Scheduler should be true",car.pickAndExecuteAnAction());
		
		assertEquals("Car's location should be s2",s2,car.currentLocation);
		assertEquals("Car's passenger should be pass2",pass2,car.p);
		
		assertTrue("Scheduler should be true",car.pickAndExecuteAnAction());
		
		assertEquals("Passenger2's log should have two entries",2,pass2.log.size());
		assertEquals("Car's location should be s2",s2,car.currentLocation);
		assertEquals("Car's passenger should be null",null,car.p);
		
		assertFalse("Scheduler should return false",car.pickAndExecuteAnAction());
	}
}
