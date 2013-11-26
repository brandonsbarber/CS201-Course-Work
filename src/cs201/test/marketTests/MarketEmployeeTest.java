package cs201.test.marketTests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cs201.roles.marketRoles.MarketEmployeeRole;
import cs201.roles.marketRoles.MarketManagerRole;
import cs201.test.mock.market.MockMarketConsumer;
import cs201.test.mock.market.MockMarketManager;

public class MarketEmployeeTest {

	MockMarketManager manager;
	MarketEmployeeRole employee;
	MockMarketConsumer consumer;
	
	/**
	 * Run before each test case. Instantiates a MarketManagerRole, a MockEmployee, and a MockConsumer. Introduces the manager to the new employee.
	 */
	@Before
	public void setUp() throws Exception {
		System.out.println("========== NEW TEST ==========");
		
		// Create the unit under test- our MarketEmployeeRole
		employee = new MarketEmployeeRole("employee");
		assertNotNull("new MarketEmployeeRole() returns a null pointer.", employee);
		
		// Create a MarketManager
		manager = new MockMarketManager("manager");
		
		// Create a consumer to place an order
		consumer = new MockMarketConsumer("consumer");
	}

	/**
	 * The very basic of tests. Checks to see if a MarketEmployee can retrieve an item and return it to the MarketManager.
	 */
	@Test
	public void simpleTest() {
		// Create our order by creating some item requests and adding them to a list
		MarketManagerRole.ItemRequest item1 = new MarketManagerRole.ItemRequest("chicken", 4);
		List<MarketManagerRole.ItemRequest> list = new ArrayList<MarketManagerRole.ItemRequest>();
		list.add(item1);
		
		// Tell the employee to retrieve the items
		employee.msgRetrieveItems(manager, list, 0);
		
		// Run the employee's scheduler to allow him to retrieve the items
		assertTrue("The employee's scheduler should have returned true after retrieving items.", employee.pickAndExecuteAnAction());
		
		// Ensure that the manager got the HereAreItems message
		assertTrue("The MarketManager should have gotten the HereAreItems message from the employee.", manager.log.getFirstEventWhichContainsString("Received msgHereAreItems with 4 chicken") != null);
	}

}
