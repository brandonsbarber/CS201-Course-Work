package cs201.test.marketTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cs201.interfaces.roles.market.MarketConsumer;
import cs201.roles.marketRoles.MarketManagerRole;
import cs201.test.mock.market.MockMarketConsumer;
import cs201.test.mock.market.MockMarketEmployee;

public class MarketManagerTest {

	MarketManagerRole manager;
	MockMarketEmployee employee;
	MarketConsumer consumer;
	
	@Before
	public void setUp() throws Exception {
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
		manager.AddEmployee(employee);
		assertEquals("The MarketManager's employee list does not have exactly one employee.", manager.employees.size(), 1);
	}

	/**
	 * The very basic of tests. Checks to see if the MarketManager can handle an order of a single item from a MarketConsumer, dispatch an employee,
	 * and send the fulfilled order back to the consumer.
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
	}
	
	@Test
	public void test2() {
		assertTrue(true);
	}

}
