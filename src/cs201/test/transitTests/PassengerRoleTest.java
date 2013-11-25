package cs201.test.transitTests;

import java.util.ArrayList;

import junit.framework.TestCase;
import cs201.gui.transit.PassengerGui;
import cs201.helper.transit.BusRoute;
import cs201.roles.transit.PassengerRole;
import cs201.roles.transit.PassengerRole.PassengerState;
import cs201.structures.Structure;
import cs201.structures.transit.BusStop;
import cs201.test.mock.Brandon.transit.MockBus;
import cs201.test.mock.Brandon.transit.MockCar;

public class PassengerRoleTest extends TestCase
{
	PassengerRole pass;
	MockCar car,car2;
	Structure s1,s2,s3,s4;
	ArrayList<BusStop> stops;
	BusRoute route;
	PassengerGui passGui;
	
	MockBus bus;
	
	public void setUp() throws Exception
	{
		s1 = new BusStop(0, 0, 0, 0, 0, null);
		s2 = new BusStop(20, 20, 0, 0, 1, null);
		s3 = new BusStop(100, 100, 0, 0, 2, null);
		s4 = new BusStop(400, 400, 0, 0, 3, null);
		
		stops = new ArrayList<BusStop>();
		stops.add((BusStop) s1);
		stops.add((BusStop) s2);
		stops.add((BusStop) s3);
		stops.add((BusStop) s4);
		
		route = new BusRoute(stops);
		
		pass = new PassengerRole(s1);
		passGui = new PassengerGui(pass,null);
		pass.setGui(passGui);
		pass.isTesting();
		car = new MockCar("Mock Car 1");
		car2 = new MockCar("Mock Car 2");
		
		bus = new MockBus("Bus 1", route);
		
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
	
	public void testPassengerWalk ()
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
	
	public void testPassengerShortWalk ()
	{
		pass.msgGoTo(s2);
		pass.setBusStops(stops);
		
		assertEquals("Ensure that passenger's destination is s2.",s2,pass.destination);
		assertEquals("Ensure that passenger's current location is s1.",s1,pass.currentLocation);
		assertEquals("Ensure that passenger's current waypoints list is empty.",0,pass.waypoints.size());
		assertEquals("Ensure that passenger's current state is none.",pass.state,PassengerState.None);
		
		assertTrue("Should execute an action and return true.",pass.pickAndExecuteAnAction());
		
		assertTrue("Should walk",pass.shouldWalk());
		
		assertEquals("Should have one waypoint for walking.",1,pass.waypoints.size());
		
		assertTrue("Should return true.",pass.pickAndExecuteAnAction());
		
		assertEquals("Passenger's state should be arrived.",PassengerState.Arrived,pass.state);
		assertEquals("Current location should be s2.",s2,pass.currentLocation);
		
		assertTrue("Should return true.",pass.pickAndExecuteAnAction());
		
		assertEquals("Passenger's state should be none.",PassengerState.None,pass.state);
		
		assertFalse("Should return false.",pass.pickAndExecuteAnAction());
		
		assertFalse("Passenger shouldn't be active now.",pass.getActive());
	}
	
	public void testPassengerLongWalkOneStop ()
	{
		pass.setCurrentLocation(s2);
		pass.msgGoTo(s3);
		pass.setBusStops(stops);
		
		assertEquals("Ensure that passenger's destination is s3.",s3,pass.destination);
		assertEquals("Ensure that passenger's current location is s2.",s2,pass.currentLocation);
		assertEquals("Ensure that passenger's current waypoints list is empty.",0,pass.waypoints.size());
		assertEquals("Ensure that passenger's current state is none.",pass.state,PassengerState.None);
		
		assertTrue("Should execute an action and return true.",pass.pickAndExecuteAnAction());
		
		assertFalse("Should not walk",pass.shouldWalk());
		
		assertEquals("Should have three waypoints for walking,bus,walking.",3,pass.waypoints.size());
		
		assertTrue("Should return true.",pass.pickAndExecuteAnAction());
		
		//"Walked" to s1 for bus stop
		assertEquals("Passenger's state should be arrived.",PassengerState.Arrived,pass.state);
		assertEquals("Current location should be s2.",s2,pass.currentLocation);
		
		assertTrue("Should return true.",pass.pickAndExecuteAnAction());
		
		assertEquals("Passenger's state should be none.",PassengerState.None,pass.state);
		
		assertEquals("Should have two waypoints for bus,walking.",2,pass.waypoints.size());
		
		assertTrue("Should return true.",pass.pickAndExecuteAnAction());

		assertEquals("S1 should have one passenger in waitingPassengers",1,((BusStop)s2).getPassengerList().size());
		
		pass.msgPleaseBoard(bus);
		
		assertEquals("Passenger's list of boarding requests should have one boarding request.",1,pass.boardingRequest.size());
		
		assertTrue("Should return true.",pass.pickAndExecuteAnAction());
		
		assertEquals("Bus should now have one passenger.",1,bus.passengers.size());
		assertEquals("Bus should now have one log call.",1,bus.log.size());
		assertEquals("Passenger's list of boarding requests should have no boarding requests.",0,pass.boardingRequest.size());
		
		pass.msgReachedDestination(s3);
		
		assertEquals("Ensure that passenger's current location is s3.",s3,pass.currentLocation);
		assertEquals("Ensure that passenger's current waypoints list is two.",2,pass.waypoints.size());
		assertEquals("Ensure that passenger's current state is Arrived.",pass.state,PassengerState.Arrived);
		
		assertTrue("Should return true.",pass.pickAndExecuteAnAction());
		
		assertEquals("Ensure that passenger's current location is s3.",s3,pass.currentLocation);
		assertEquals("Ensure that passenger's current waypoints list is one.",1,pass.waypoints.size());
		assertEquals("Ensure that passenger's current state is none.",pass.state,PassengerState.None);
		
		assertEquals("Bus should now have no passengers.",0,bus.passengers.size());
		assertEquals("Bus should now have two log calls.",2,bus.log.size());
		
		assertTrue("Should return true.",pass.pickAndExecuteAnAction());
		
		assertEquals("Ensure that passenger's current location is s3.",s3,pass.currentLocation);
		assertEquals("Ensure that passenger's current waypoints list is one.",1,pass.waypoints.size());
		assertEquals("Ensure that passenger's current state is Arrived.",pass.state,PassengerState.Arrived);
		
		assertTrue("Should return true.",pass.pickAndExecuteAnAction());
		
		assertEquals("Ensure that passenger's current state is none.",pass.state,PassengerState.None);
		assertEquals("Ensure that passenger's current waypoints list is zero.",0,pass.waypoints.size());
		
		assertEquals("Passenger's destination and currentLocation should be the same.",pass.destination,pass.currentLocation);
		
		assertFalse("Should return false.",pass.pickAndExecuteAnAction());

		assertFalse("Should be inactive.",pass.getActive());
		
	}
}
