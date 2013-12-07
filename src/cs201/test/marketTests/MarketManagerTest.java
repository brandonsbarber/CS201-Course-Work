package cs201.test.marketTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cs201.roles.marketRoles.MarketManagerRole;
import cs201.roles.marketRoles.MarketManagerRole.ConsumerRecord;
import cs201.roles.marketRoles.MarketManagerRole.StructureRecord;
import cs201.test.mock.market.MockMarketConsumer;
import cs201.test.mock.market.MockMarketEmployee;
import cs201.test.mock.market.MockRestaurant;

public class MarketManagerTest {

	MarketManagerRole manager;
	MockMarketEmployee employee;
	MockMarketConsumer consumer;
	MockRestaurant restaurant;
	
	/**
	 * Run before each test case. Instantiates a MarketManagerRole, a MockEmployee, and a MockConsumer. Introduces the manager to the new employee.
	 */
	@Before
	public void setUp() throws Exception {
		System.out.println("========== NEW TEST ==========");
				
		// Create the unit under test- our MarketManagerRole
		manager = new MarketManagerRole();
		assertNotNull("new MarketManagerRole() returned a null MarketManager", manager);
		
		// The MarketManager should be initialized with an empty orders list
		assertEquals("The MarketManager should start with an empty orders list.", manager.orders.size(), 0);
		
		// Create a MarketEmployee
		employee = new MockMarketEmployee("employee");
		
		// Create a consumer to place an order
		consumer = new MockMarketConsumer("consumer");
		
		// Tell the MarketManager about the employee
		manager.addEmployee(employee);
		assertEquals("The MarketManager's employee list does not have exactly one employee.", manager.employees.size(), 1);
		
		// Create a restaurant to place an order (in certain tests)
		restaurant = new MockRestaurant(0, 0, 0, 0, 0, null);
		restaurant.setOpen(true);
	}

	/**
	 * The very basic of tests. Checks to see if the MarketManager can handle an order of a single item from the MarketConsumer, dispatch an employee,
	 * and send the fulfilled order back to the consumer for the correct price.
	 */
	@Test
	public void simpleTest() {
		// Give the market a starting amount of chicken for inventory
		manager.AddInventoryEntry(new MarketManagerRole.InventoryEntry("chicken", 100, 6.99f));
		
		// Create our order by creating some item requests and adding them to a list
		MarketManagerRole.ItemRequest item1 = new MarketManagerRole.ItemRequest("chicken", 4);
		List<MarketManagerRole.ItemRequest> list = new ArrayList<MarketManagerRole.ItemRequest>();
		list.add(item1);
		
		// Give the market manager our order
		manager.msgHereIsMyOrder(consumer, list);
		
		// Check to see if our new order is in the MarketManager's list of orders
		assertEquals("The MarketManager should have added the order to his list of orders.", manager.orders.size(), 1);
		
		// Call the MarketManager's scheduler once to dispatch an employee
		assertTrue("The MarketManager's scheduler should have processed the first order and returned true.", manager.pickAndExecuteAnAction());
		
		// Ensure that the MarketManager sent a retrieval message to the MarketEmployee
		assertTrue("The MarketEmployee's log should have a msgRetrieveItems.", employee.log.getFirstEventWhichContainsString("Received msgRetrieveItems") != null);
		
		// Call the MarketManager's scheduler once to send the order back to the MarketConsumer
		assertTrue("The MarketManager's scheduler should have sent the order to the consumer and returned true.", manager.pickAndExecuteAnAction());
		
		// Ensure that the MarketConsumer got 2 messages: msgHereAreYourItems and msgHereIsYourTotal
		assertTrue("The MarketConsumer should get a hereAreYourItems message.", consumer.log.getFirstEventWhichContainsString("Received msgHereAreYourItems with 4 chicken") != null);
		assertTrue("The MarketConsumer should get a msgHereIsYourTotal", consumer.log.getFirstEventWhichContainsString("Received msgHereIsYourTotal with 27.96") != null);
	
		// Ensure that the MarketManager updated the balance for the consumer's account (should be 0.0, since the MockMarketConsumer just automatically pays the bill)
		ConsumerRecord record = manager.consumerBalance.get(consumer);
		assertTrue("The MarketConsumer should have updated the balance for the consumer account.", record.balance == 0.0f);
	}
	
	/**
	 * Another basic test. Checks to see if the MarketManager can handle an order of 2 items from the MarketConsumer, dispatch an employee, and send
	 * the order back to the consumer for the correct price.
	 */
	@Test
	public void multipleItems() {
		// Give the market a starting amount of chicken for inventory
		manager.AddInventoryEntry(new MarketManagerRole.InventoryEntry("chicken", 100, 6.99f));
		manager.AddInventoryEntry(new MarketManagerRole.InventoryEntry("steak", 100, 11.99f));
		manager.AddInventoryEntry(new MarketManagerRole.InventoryEntry("butter", 20, 5.99f));
		
		// Create our order by creating some item requests and adding them to a list
		MarketManagerRole.ItemRequest item1 = new MarketManagerRole.ItemRequest("chicken", 4);
		MarketManagerRole.ItemRequest item2 = new MarketManagerRole.ItemRequest("steak", 6);
		List<MarketManagerRole.ItemRequest> list = new ArrayList<MarketManagerRole.ItemRequest>();
		list.add(item1);
		list.add(item2);
		
		// Give the market manager our order
		manager.msgHereIsMyOrder(consumer, list);
		
		// Check to see if our new order is in the MarketManager's list of orders
		assertEquals("The MarketManager should have added the order to his list of orders.", manager.orders.size(), 1);
		
		// Call the MarketManager's scheduler once to dispatch an employee
		assertTrue("The MarketManager's scheduler should have processed the first order and returned true.", manager.pickAndExecuteAnAction());
		
		// Ensure that the MarketManager sent a retrieval message to the MarketEmployee
		assertTrue("The MarketEmployee's log should have a msgRetrieveItems.", employee.log.getFirstEventWhichContainsString("Received msgRetrieveItems with 4 chicken 6 steak") != null);
		
		// Call the MarketManager's scheduler once to send the order back to the MarketConsumer
		assertTrue("The MarketManager's scheduler should have sent the order to the consumer and returned true.", manager.pickAndExecuteAnAction());
		
		// Ensure that the MarketConsumer got 2 messages: msgHereAreYourItems and msgHereIsYourTotal
		assertTrue("The MarketConsumer should get a hereAreYourItems message.", consumer.log.getFirstEventWhichContainsString("Received msgHereAreYourItems with 4 chicken 6 steak") != null);
		assertTrue("The MarketConsumer should get a msgHereIsYourTotal", consumer.log.getFirstEventWhichContainsString("Received msgHereIsYourTotal with 99.90") != null);
		
		// Ensure that the MarketManager updated the balance for the consumer's account (should be 0.0, since the MockMarketConsumer just automatically pays the bill)
		ConsumerRecord record = manager.consumerBalance.get(consumer);
		assertTrue("The MarketManager should have updated the balance for the consumer account.", record.balance == 0.0f);
	}
	
	/**
	 * Another basic test. Checks to see if the MarketManager can handle orders from a restaurant, tell an employee to retrieve the items, dispatch a delivery truck, and bill the
	 * restaurant for the correct price.
	 */
	@Test
	public void structureTest() {
		// Give the market a starting amount of steak for inventory
		manager.AddInventoryEntry(new MarketManagerRole.InventoryEntry("steak", 100, 10.0f));
		
		// Create our order by creating an item request
		MarketManagerRole.ItemRequest item1 = new MarketManagerRole.ItemRequest("steak", 10);
		
		// Give the market manager our order
		manager.msgHereIsMyOrderForDelivery(restaurant, item1);
		
		// Check to see if our new order is in the MarketManager's list of orders
		assertEquals("The MarketManager should have added the order to his list of orders.", manager.orders.size(), 1);
		
		// Call the MarketManager's scheduler once to dispatch an employee
		assertTrue("The MarketManager's scheduler should have processed the first order and returned true.", manager.pickAndExecuteAnAction());
		
		// Ensure that the MarketManager sent a retrieval message to the MarketEmployee
		assertTrue("The MarketEmployee's log should have a msgRetrieveItems.", employee.log.getFirstEventWhichContainsString("Received msgRetrieveItems") != null);
		
		// Call the MarketManager's scheduler once to dispatch a delivery truck
		assertTrue("The MarketManager's scheduler should have dispatched a delivery truck and returned true.", manager.pickAndExecuteAnAction());
		
		// The rest of the process is out of our hands- the delivery truck delivers the food and bills the Restaurant
		// But lets make sure the MarketManager has updated the outstanding balance for the Restaurant
		StructureRecord record = manager.structureBalance.get(restaurant);
		assertTrue("The MarketManager should have updated the balance for the restaurant account.", record.balance == 100.0f);
	}
	
	/**
	 * A test to see if the MarketManager can handle a restaurant order if the restaurant is closed when the market manager goes to
	 * dispatch the truck.
	 */
	@Test
	public void structureTestRestaurantClosed() {
		// Give the market a starting amount of steak for inventory
		manager.AddInventoryEntry(new MarketManagerRole.InventoryEntry("waffles", 100, 4.0f));
				
		// Create our order by creating an item request
		MarketManagerRole.ItemRequest item1 = new MarketManagerRole.ItemRequest("waffles", 10);
		
		// Give the market manager our order
		manager.msgHereIsMyOrderForDelivery(restaurant, item1);
		
		// Check to see if our new order is in the MarketManager's list of orders
		assertEquals("The MarketManager should have added the order to his list of orders.", manager.orders.size(), 1);
		
		// Call the MarketManager's scheduler once to dispatch an employee
		assertTrue("The MarketManager's scheduler should have processed the first order and returned true.", manager.pickAndExecuteAnAction());
		
		// Ensure that the MarketManager sent a retrieval message to the MarketEmployee
		assertTrue("The MarketEmployee's log should have a msgRetrieveItems.", employee.log.getFirstEventWhichContainsString("Received msgRetrieveItems") != null);
		
		// Call the MarketManager's scheduler once to dispatch a delivery truck
		assertTrue("The MarketManager's scheduler should have dispatched a delivery truck and returned true.", manager.pickAndExecuteAnAction());
		
		
	}

	/**
	 * A more complicated test. Checks to see if the MarketManager can process two different, interleaved orders
	 */
	@Test
	public void multipleOrders() {
		// Give the market a starting amount of chicken and steak for inventory
		manager.AddInventoryEntry(new MarketManagerRole.InventoryEntry("chicken", 100, 7.00f));
		manager.AddInventoryEntry(new MarketManagerRole.InventoryEntry("steak", 100, 11.00f));
		manager.AddInventoryEntry(new MarketManagerRole.InventoryEntry("pizza", 100, 5.00f));
		
		// Create our order by creating some item requests and adding them to a list
		MarketManagerRole.ItemRequest item1 = new MarketManagerRole.ItemRequest("chicken", 4);
		MarketManagerRole.ItemRequest item2 = new MarketManagerRole.ItemRequest("steak", 10);
		List<MarketManagerRole.ItemRequest> list = new ArrayList<MarketManagerRole.ItemRequest>();
		list.add(item1);
		list.add(item2);
		
		// Create our second order
		MarketManagerRole.ItemRequest item3 = new MarketManagerRole.ItemRequest("pizza", 10);
		List<MarketManagerRole.ItemRequest> list2 = new ArrayList<MarketManagerRole.ItemRequest>();
		list2.add(item3);
		
		// Give the market manager our order
		manager.msgHereIsMyOrder(consumer, list);
		
		// Check to see if our new order is in the MarketManager's list of orders
		assertEquals("The MarketManager should have added the order to his list of orders.", manager.orders.size(), 1);
		
		// Call the MarketManager's scheduler once to dispatch an employee
		assertTrue("The MarketManager's scheduler should have processed the first order and returned true.", manager.pickAndExecuteAnAction());
		
		// Ensure that the MarketManager sent a retrieval message to the MarketEmployee
		assertTrue("The MarketEmployee's log should have a msgRetrieveItems.", employee.log.getFirstEventWhichContainsString("Received msgRetrieveItems") != null);
		
		// Here comes another order!
		manager.msgHereIsMyOrder(consumer, list2);
		
		// Call the MarketManager's scheduler once to send the first order back to the MarketConsumer
		assertTrue("The MarketManager's scheduler should have sent the first order to the consumer and returned true.", manager.pickAndExecuteAnAction());
		
		// Ensure that the MarketConsumer got 2 messages: msgHereAreYourItems and msgHereIsYourTotal
		assertTrue("The MarketConsumer should get a hereAreYourItems message.", consumer.log.getFirstEventWhichContainsString("Received msgHereAreYourItems with 4 chicken 10 steak") != null);
		assertTrue("The MarketConsumer should get a msgHereIsYourTotal", consumer.log.getFirstEventWhichContainsString("Received msgHereIsYourTotal with 138.00") != null);
	
		// Ensure that the MarketManager updated the balance for the consumer's account (should be 0.0, since the MockMarketConsumer just automatically pays the bill)
		ConsumerRecord record = manager.consumerBalance.get(consumer);
		assertTrue("The MarketConsumer should have updated the balance for the consumer account.", record.balance == 0.0f);
		
		// Now call the MarketManager's scheduler again to process the next order
		assertTrue("The MarketManager's scheduler should have processed the second order and returned true.", manager.pickAndExecuteAnAction());
		
		// Ensure that the MarketManager sent a retrieval message to the MarketEmployee
		assertTrue("The MarketEmployee's log should have a msgRetrieveItems.", employee.log.getFirstEventWhichContainsString("Received msgRetrieveItems") != null);
		
		// Call the MarketManager's scheduler once to send the second order back to the MarketConsumer
		assertTrue("The MarketManager's scheduler should have sent the second order to the consumer and returned true.", manager.pickAndExecuteAnAction());
		
		// Ensure that the MarketConsumer got 2 more messages: msgHereAreYourItems and msgHereIsYourTotal
		assertTrue("The MarketConsumer should get a hereAreYourItems message.", consumer.log.getFirstEventWhichContainsString("Received msgHereAreYourItems with 10 pizza") != null);
		assertTrue("The MarketConsumer should get a msgHereIsYourTotal", consumer.log.getFirstEventWhichContainsString("Received msgHereIsYourTotal with 50.00") != null);

	}
	
	/**
	 * Checks to see if a MarketManager can handle an order for a car.
	 */
	@Test
	public void carTest() {
		// Give the market manage our order
		manager.msgIWouldLikeACar(consumer);
		
		// Check to see if our new order is in the MarketManager's list of orders
		assertEquals("The MarketManager should have added the CarOrder to his list of carOrders.", manager.carOrders.size(), 1);
		
		// Call the MarketManager's scheduler once to dispatch an employee
		assertTrue("The MarketManager's scheduler should have processed the CarOrder and returned true.", manager.pickAndExecuteAnAction());
		
		// Ensure that the MarketManager sent a retrieval message to the MarketEmployee
		assertTrue("The MarketEmployee's log should have a msgRetrieveCar", employee.log.getFirstEventWhichContainsString("Received msgRetrieveCar") != null);
		
		// Call the MarketManager's scheduler once to give the car to the MarketConsumer
		assertTrue("The MarketManager's scheduler should have given the car to the consumer and returned true.", manager.pickAndExecuteAnAction());
		
		// Ensure that the MarketConsumer got 2 messages: msgHereIsYourCar and msgHereIsYourTotal
		assertTrue("The MarketConsumer should get a msgHereIsYourCar", consumer.log.getFirstEventWhichContainsString("Received msgHereIsYourCar") != null);
		assertTrue("The MarketConsumer should get a msgHereIsYourTotal", consumer.log.getFirstEventWhichContainsString("Received msgHereIsYourTotal with 1000.00") != null);
			
		// Ensure that the MarketManager updated the balance for the consumer's account (should be 0.0, since the MockMarketConsumer just automatically pays the bill)
		ConsumerRecord record = manager.consumerBalance.get(consumer);
		assertTrue("The MarketConsumer should have updated the balance for the consumer account.", record.balance == 0.0f);
	}
}
