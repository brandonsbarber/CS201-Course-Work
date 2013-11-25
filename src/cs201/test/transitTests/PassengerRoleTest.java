package cs201.test.transitTests;

import junit.framework.TestCase;
import cs201.gui.transit.PassengerGui;
import cs201.roles.transit.PassengerRole;
import cs201.roles.transit.PassengerRole.PassengerState;
import cs201.structures.Structure;
import cs201.structures.transit.BusStop;
import cs201.test.mock.Brandon.transit.MockCar;

public class PassengerRoleTest extends TestCase
{
	PassengerRole pass;
	MockCar car,car2;
	Structure s1,s2;
	PassengerGui passGui;
	
	public void setUp() throws Exception
	{
		s1 = new BusStop(0, 0, 0, 0, 0, null);
		s2 = new BusStop(0, 0, 0, 0, 1, null);
		
		pass = new PassengerRole(s1);
		passGui = new PassengerGui(pass,null);
		pass.setGui(passGui);
		pass.isTesting();
		car = new MockCar("Mock Car 1");
		car2 = new MockCar("Mock Car 2");
		//pass.addCar(car);
	}
	
	public void testPassengerOneCar()
	{
		pass.addCar(car);
		
		pass.msgGoTo(s2);
		assertEquals("Ensure that passenger's destination is s2.",s2,pass.destination);
		assertEquals("Ensure that passenger's current location is s1.",s1,pass.currentLocation);
		assertEquals("Ensure that passenger's current waypoints list is empty.",0,pass.waypoints.size());
		assertEquals("Ensure that passenger's current state is none.",pass.state,PassengerState.None);
		
		assertTrue("Should execute an action and return true.",pass.pickAndExecuteAnAction());
		
		assertEquals("Should have one waypoint for a car.",1,pass.waypoints.size());
		
		pass.pickAndExecuteAnAction();
		
		assertEquals("Car should have received a message from the passenger requesting a pickup.",1,car.log.size());
		
		pass.msgPleaseBoard(car);
		
		assertEquals("Passenger should now have a boarding request from the car.",1,pass.boardingRequest.size());
		
		assertTrue("Scheduler should execute.",pass.pickAndExecuteAnAction());
		
		assertEquals("Passenger's list of boarding requests should be empty now.",0,pass.boardingRequest.size());
		
		assertEquals("Car should have two log calls now",2,car.log.size());
		
		assertEquals("Passenger's current vehicle should be the car.",car,pass.currentVehicle);
		
		pass.msgReachedDestination(s2);
		
		assertEquals("Passenger's current location should now read as s2.",s2,pass.currentLocation);
		
		assertEquals("Passenger's state should be arrived.",PassengerState.Arrived,pass.state);
		
		assertTrue("Passenger's pick and execute an action should return true",pass.pickAndExecuteAnAction());
		
		assertEquals("Passenger's state should be none.",PassengerState.None,pass.state);
		
		assertEquals("Passenger's vehicle should be null.",null,pass.currentVehicle);
		
		assertEquals("Car should have three log calls. ",3,car.log.size());
		
		assertEquals("waypoints should be empty.",0,pass.waypoints.size());
		
		assertFalse("Passenger should return false to stop execution.",pass.pickAndExecuteAnAction());
		
		assertFalse("Should not be active.",pass.getActive());
	}
	
	public void testPassengerTwoCars()
	{
		pass.addCar(car);
		
		pass.msgGoTo(s2);
		assertEquals("Ensure that passenger's destination is s2.",s2,pass.destination);
		assertEquals("Ensure that passenger's current location is s1.",s1,pass.currentLocation);
		assertEquals("Ensure that passenger's current waypoints list is empty.",0,pass.waypoints.size());
		assertEquals("Ensure that passenger's current state is none.",pass.state,PassengerState.None);
		
		assertTrue("Should execute an action and return true.",pass.pickAndExecuteAnAction());
		
		assertEquals("Should have one waypoint for a car.",1,pass.waypoints.size());
		
		pass.pickAndExecuteAnAction();
		
		assertEquals("Car should have received a message from the passenger requesting a pickup.",1,car.log.size());
		
		//adding a car request from a non-car
		pass.msgPleaseBoard(car2);
		
		assertEquals("Passenger should now have a boarding request from the car.",1,pass.boardingRequest.size());
		
		pass.msgPleaseBoard(car);
		
		assertEquals("Passenger should now have a boarding request from the car.",2,pass.boardingRequest.size());
		
		assertTrue("Scheduler should execute.",pass.pickAndExecuteAnAction());
		
		assertEquals("Passenger's list of boarding requests should have one boarding request.",1,pass.boardingRequest.size());
		
		assertTrue("Scheduler should execute.",pass.pickAndExecuteAnAction());
		
		assertEquals("Car should have two log calls now",2,car.log.size());
		
		assertEquals("Passenger's current vehicle should be the car.",car,pass.currentVehicle);
		
		pass.msgReachedDestination(s2);
		
		assertEquals("Passenger's current location should now read as s2.",s2,pass.currentLocation);
		
		assertEquals("Passenger's state should be arrived.",PassengerState.Arrived,pass.state);
		
		assertTrue("Passenger's pick and execute an action should return true",pass.pickAndExecuteAnAction());
		
		assertEquals("Passenger's state should be none.",PassengerState.None,pass.state);
		
		assertEquals("Passenger's vehicle should be null.",null,pass.currentVehicle);
		
		assertEquals("Car should have three log calls. ",3,car.log.size());
		
		assertEquals("waypoints should be empty.",0,pass.waypoints.size());
		
		assertFalse("Passenger should return false to stop execution.",pass.pickAndExecuteAnAction());
		
		assertFalse("Should not be active.",pass.getActive());
	}
	
	public void testPassengerWalk()
	{
		pass.msgGoTo(s2);
		assertEquals("Ensure that passenger's destination is s2.",s2,pass.destination);
		assertEquals("Ensure that passenger's current location is s1.",s1,pass.currentLocation);
		assertEquals("Ensure that passenger's current waypoints list is empty.",0,pass.waypoints.size());
		assertEquals("Ensure that passenger's current state is none.",pass.state,PassengerState.None);
		
		assertTrue("Should execute an action and return true.",pass.pickAndExecuteAnAction());
		
		assertEquals("Should have one waypoint for walking.",1,pass.waypoints.size());
		
		assertTrue("Should return true.",pass.pickAndExecuteAnAction());
		
		assertEquals("Passenger's state should be arrived.",PassengerState.Arrived,pass.state);
		assertEquals("Current location should be s2.",s2,pass.currentLocation);
		
		assertTrue("Should return true.",pass.pickAndExecuteAnAction());
		
		assertEquals("Passenger's state should be none.",PassengerState.None,pass.state);
		
		assertFalse("Should return false.",pass.pickAndExecuteAnAction());
		
		assertFalse("Passenger shouldn't be active now.",pass.getActive());
	}
}
