package cs201.test.transitTests;

import java.util.ArrayList;

import cs201.agents.transit.TruckAgent;
import cs201.roles.marketRoles.MarketManagerRole.ItemRequest;
import cs201.structures.Structure;
import cs201.structures.transit.BusStop;
import junit.framework.TestCase;

public class TruckAgentTest extends TestCase
{
	TruckAgent truck;
	
	Structure market;
	Structure destination,destination2;
	
	public void setUp()
	{
		market = new BusStop(0, 0, 0, 0, 0, null);
		destination = new BusStop(0,0,0,0,1,null);
		destination2 = new BusStop(0,0,0,0,2,null);
		
		truck = new TruckAgent(market);
	}
	
	public void testOneDelivery()
	{
		assertEquals("Truck should be at market.",market,truck.currentLocation);
		assertEquals("Truck should have no destination.",null,truck.destination);
		assertEquals("Truck should have no orders",0,truck.deliveries.size());
		
		truck.msgMakeDeliveryRun(new ArrayList<ItemRequest>(), destination, 0,0);
		
		assertEquals("Truck should be at market.",market,truck.currentLocation);
		assertEquals("Truck should have no destination.",null,truck.destination);
		assertEquals("Truck should have one order",1,truck.deliveries.size());
		
		assertTrue("Scheduler should be true.",truck.pickAndExecuteAnAction());
		
		assertEquals("Truck should be at destination.",destination,truck.currentLocation);
		assertEquals("Truck should have destination.",destination,truck.destination);
		assertEquals("Truck should have one order",1,truck.deliveries.size());
		
		assertTrue("Scheduler should be true.",truck.pickAndExecuteAnAction());
		
		assertEquals("Truck should be at destination.",destination,truck.currentLocation);
		assertEquals("Truck should have destination.",destination,truck.destination);
		assertEquals("Truck should have no orders",0,truck.deliveries.size());
		
		assertFalse("Scheduler should be false.",truck.pickAndExecuteAnAction());
		
		assertEquals("Truck should be at market.",market,truck.currentLocation);
		assertEquals("Truck should have destination as market.",market,truck.destination);
		assertEquals("Truck should have no orders",0,truck.deliveries.size());
	}
	
	public void testTwoDeliveriesSequentially()
	{
		assertEquals("Truck should be at market.",market,truck.currentLocation);
		assertEquals("Truck should have no destination.",null,truck.destination);
		assertEquals("Truck should have no orders",0,truck.deliveries.size());
		
		truck.msgMakeDeliveryRun(new ArrayList<ItemRequest>(), destination, 0,0);
		
		assertEquals("Truck should be at market.",market,truck.currentLocation);
		assertEquals("Truck should have no destination.",null,truck.destination);
		assertEquals("Truck should have one order",1,truck.deliveries.size());
		
		assertTrue("Scheduler should be true.",truck.pickAndExecuteAnAction());
		
		assertEquals("Truck should be at destination.",destination,truck.currentLocation);
		assertEquals("Truck should have destination.",destination,truck.destination);
		assertEquals("Truck should have one order",1,truck.deliveries.size());
		
		assertTrue("Scheduler should be true.",truck.pickAndExecuteAnAction());
		
		assertEquals("Truck should be at destination.",destination,truck.currentLocation);
		assertEquals("Truck should have destination.",destination,truck.destination);
		assertEquals("Truck should have no orders",0,truck.deliveries.size());
		
		assertFalse("Scheduler should be false.",truck.pickAndExecuteAnAction());
		
		assertEquals("Truck should be at market.",market,truck.currentLocation);
		assertEquals("Truck should have destination as market.",market,truck.destination);
		assertEquals("Truck should have no orders",0,truck.deliveries.size());
		
		truck.msgMakeDeliveryRun(new ArrayList<ItemRequest>(), destination, 0,0);
		
		assertEquals("Truck should be at market.",market,truck.currentLocation);
		assertEquals("Truck should have destination.",market,truck.destination);
		assertEquals("Truck should have one order",1,truck.deliveries.size());
		
		assertTrue("Scheduler should be true.",truck.pickAndExecuteAnAction());
		
		assertEquals("Truck should be at destination.",destination,truck.currentLocation);
		assertEquals("Truck should have destination.",destination,truck.destination);
		assertEquals("Truck should have one order",1,truck.deliveries.size());
		
		assertTrue("Scheduler should be true.",truck.pickAndExecuteAnAction());
		
		assertEquals("Truck should be at destination.",destination,truck.currentLocation);
		assertEquals("Truck should have destination.",destination,truck.destination);
		assertEquals("Truck should have no orders",0,truck.deliveries.size());
		
		assertFalse("Scheduler should be false.",truck.pickAndExecuteAnAction());
		
		assertEquals("Truck should be at market.",market,truck.currentLocation);
		assertEquals("Truck should have destination as market.",market,truck.destination);
		assertEquals("Truck should have no orders",0,truck.deliveries.size());
	}
	
	public void testTwoDeliveriesSimultaneous()
	{
		assertEquals("Truck should be at market.",market,truck.currentLocation);
		assertEquals("Truck should have no destination.",null,truck.destination);
		assertEquals("Truck should have no orders",0,truck.deliveries.size());
		
		truck.msgMakeDeliveryRun(new ArrayList<ItemRequest>(), destination, 0,0);
		truck.msgMakeDeliveryRun(new ArrayList<ItemRequest>(), destination2, 0,0);
		
		assertEquals("Truck should be at market.",market,truck.currentLocation);
		assertEquals("Truck should have no destination.",null,truck.destination);
		assertEquals("Truck should have two orders",2,truck.deliveries.size());
		
		assertTrue("Scheduler should be true.",truck.pickAndExecuteAnAction());
		
		assertEquals("Truck should be at destination.",destination,truck.currentLocation);
		assertEquals("Truck should have destination.",destination,truck.destination);
		assertEquals("Truck should have two orders",2,truck.deliveries.size());
		
		assertTrue("Scheduler should be true.",truck.pickAndExecuteAnAction());
		
		assertEquals("Truck should be at destination.",destination,truck.currentLocation);
		assertEquals("Truck should have destination.",destination,truck.destination);
		assertEquals("Truck should have one order",1,truck.deliveries.size());
		
		assertTrue("Scheduler should be true.",truck.pickAndExecuteAnAction());
		
		assertEquals("Truck should be at destination.",destination2,truck.currentLocation);
		assertEquals("Truck should have destination.",destination2,truck.destination);
		assertEquals("Truck should have one order",1,truck.deliveries.size());
		
		assertTrue("Scheduler should be true.",truck.pickAndExecuteAnAction());
		
		assertEquals("Truck should be at destination.",destination2,truck.currentLocation);
		assertEquals("Truck should have destination.",destination2,truck.destination);
		assertEquals("Truck should have no orders",0,truck.deliveries.size());
		
		assertFalse("Scheduler should be false.",truck.pickAndExecuteAnAction());
		
		assertEquals("Truck should be at market.",market,truck.currentLocation);
		assertEquals("Truck should have destination as market.",market,truck.destination);
		assertEquals("Truck should have no orders",0,truck.deliveries.size());
	}
}
