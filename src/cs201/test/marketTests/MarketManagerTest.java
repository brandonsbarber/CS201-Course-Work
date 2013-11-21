package cs201.test.marketTests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cs201.interfaces.roles.market.MarketConsumer;
import cs201.interfaces.roles.market.MarketEmployee;
import cs201.interfaces.roles.market.MarketManager;
import cs201.roles.marketRoles.MarketConsumerRole;
import cs201.roles.marketRoles.MarketEmployeeRole;
import cs201.roles.marketRoles.MarketManagerRole;
import cs201.roles.marketRoles.MarketManagerRole.ItemRequest;
import cs201.test.mock.market.MockMarketEmployee;

public class MarketManagerTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		
		// Create the unit under test- our MarketManagerRole
		MarketManagerRole manager = new MarketManagerRole();
		
		// The MarketManager should be initialized with an empty orders list
		assertEquals("The MarketManager should start with an empty orders list.", manager.orders.size(), 0);
		
		// Create a MarketEmployee
		MockMarketEmployee employee = new MockMarketEmployee("employee");
		
		// Create a consumer to place an order
		MarketConsumer consumer = new MarketConsumerRole();
		
		// Tell the MarketManager about the employee
		manager.AddEmployee(employee);
		
		// Create our order by creating some item requests and adding them to a list
		MarketManagerRole.ItemRequest item1 = new MarketManagerRole.ItemRequest("chicken", 4);
		List<MarketManagerRole.ItemRequest> list = new ArrayList<MarketManagerRole.ItemRequest>();
		list.add(item1);
		
		// Give the market manager our order
		manager.msgHereIsMyOrder(consumer, list);
		
		// Check to see if our new order is in the MarketManager's list of orders
		assertEquals("The MarketManager should have added the order to his list of orders.", manager.orders.size(), 1);
		
		// Call the MarketManager's scheduler once
		assertTrue("The MarketManager's scheduler should have processed the first order and returned true.", manager.pickAndExecuteAnAction());
		
		// Ensure that the MarketManager sent a retrieval message to the MarketEmployee
		assertTrue("The MarketEmployee's log should have a msgRetrieveItems.", employee.log.getFirstEventWhichContainsString("Received msgRetrieveItems") != null);
		
	}

}
