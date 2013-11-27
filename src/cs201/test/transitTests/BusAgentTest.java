package cs201.test.transitTests;

import java.util.ArrayList;

import cs201.agents.transit.BusAgent;
import cs201.helper.transit.BusRoute;
import cs201.structures.Structure;
import cs201.structures.transit.BusStop;
import cs201.test.mock.Brandon.transit.MockPassenger;
import junit.framework.TestCase;

public class BusAgentTest extends TestCase
{
	BusAgent bus;
	
	MockPassenger pass1,pass2;
	
	Structure s1,s2,s3,s4;
	ArrayList<BusStop> stops;
	BusRoute route;
	
	public void setUp()
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
		
		bus = new BusAgent(route, 0);
		bus.testing = true;
	}
	
	/**
	 * Note: It moves and picks location on the same scheduler run
	 */
	public void testEmptyNormalBusMovement()
	{
		assertEquals("Initial location should be s1",s1,bus.currentLocation);
		assertEquals("Destination should not be set.",null,bus.destination);
		assertEquals("Bus should have no passengers.",0,bus.passengers.size());
		
		assertTrue(bus.pickAndExecuteAnAction());
		
		assertEquals("Initial location should be s2",s2,bus.currentLocation);
		assertEquals("Destination should be s2.",s2,bus.destination);
		assertEquals("Bus should have no passengers.",0,bus.passengers.size());
		
		assertTrue(bus.pickAndExecuteAnAction());
		
		assertEquals("Initial location should be s3",s3,bus.currentLocation);
		assertEquals("Destination should be s3.",s3,bus.destination);
		assertEquals("Bus should have no passengers.",0,bus.passengers.size());
		
		assertTrue(bus.pickAndExecuteAnAction());
		
		assertEquals("Initial location should be s4",s4,bus.currentLocation);
		assertEquals("Destination should be s4.",s4,bus.destination);
		assertEquals("Bus should have no passengers.",0,bus.passengers.size());
		
		assertTrue(bus.pickAndExecuteAnAction());
		
		assertEquals("Initial location should be s1",s1,bus.currentLocation);
		assertEquals("Destination should be s1.",s1,bus.destination);
		assertEquals("Bus should have no passengers.",0,bus.passengers.size());
	}
	
	public void testOneStop()
	{
		ArrayList<BusStop> myStops = new ArrayList<BusStop>();
		myStops.add((BusStop)s1);
		
		BusAgent bus = new BusAgent(new BusRoute(myStops), 0);
		
		assertTrue(bus.pickAndExecuteAnAction());
		
		assertEquals("Initial location should be s1",s1,bus.currentLocation);
		assertEquals("Destination should be s1.",s1,bus.destination);
		assertEquals("Bus should have no passengers.",0,bus.passengers.size());
		
		assertTrue(bus.pickAndExecuteAnAction());
		
		assertEquals("Initial location should be s1",s1,bus.currentLocation);
		assertEquals("Destination should be s1.",s1,bus.destination);
		assertEquals("Bus should have no passengers.",0,bus.passengers.size());
		
		assertTrue(bus.pickAndExecuteAnAction());
		
		assertEquals("Initial location should be s1",s1,bus.currentLocation);
		assertEquals("Destination should be s1.",s1,bus.destination);
		assertEquals("Bus should have no passengers.",0,bus.passengers.size());
		
		assertTrue(bus.pickAndExecuteAnAction());
		
		assertEquals("Initial location should be s1",s1,bus.currentLocation);
		assertEquals("Destination should be s1.",s1,bus.destination);
		assertEquals("Bus should have no passengers.",0,bus.passengers.size());
		
		
	}

	public void testOnePassengerOneStop()
	{
		MockPassenger p = new MockPassenger("Passenger 1");
		
		((BusStop)s1).addPassenger(p);
		
		assertTrue(bus.pickAndExecuteAnAction());
		
		assertEquals("Initial location should be s2",s2,bus.currentLocation);
		assertEquals("Destination should be s2.",s2,bus.destination);
		assertEquals("Bus should have no passengers.",0,bus.passengers.size());
		
		assertTrue(bus.pickAndExecuteAnAction());
		
		assertEquals("Initial location should be s3",s3,bus.currentLocation);
		assertEquals("Destination should be s3.",s3,bus.destination);
		assertEquals("Bus should have no passengers.",0,bus.passengers.size());
		
		assertTrue(bus.pickAndExecuteAnAction());
		
		assertEquals("Initial location should be s4",s4,bus.currentLocation);
		assertEquals("Destination should be s4.",s4,bus.destination);
		assertEquals("Bus should have no passengers.",0,bus.passengers.size());
		
		assertTrue(bus.pickAndExecuteAnAction());
		
		assertEquals("Initial location should be s1",s1,bus.currentLocation);
		assertEquals("Destination should be s1.",s1,bus.destination);
		assertEquals("Bus should have no passengers.",0,bus.passengers.size());
	}
}
