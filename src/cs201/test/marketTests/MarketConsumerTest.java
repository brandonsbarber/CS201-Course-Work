package cs201.test.marketTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import org.junit.Before;
import org.junit.Test;

import cs201.gui.roles.market.MarketEmployeeGui;
import cs201.gui.roles.market.MarketManagerGui;
import cs201.gui.structures.market.MarketAnimationPanel;
import cs201.interfaces.roles.market.MarketConsumer;
import cs201.interfaces.roles.market.MarketEmployee;
import cs201.roles.marketRoles.MarketConsumerRole;
import cs201.roles.marketRoles.MarketEmployeeRole;
import cs201.roles.marketRoles.MarketManagerRole;
import cs201.test.mock.market.MockMarketConsumer;
import cs201.test.mock.market.MockMarketEmployee;
import cs201.test.mock.market.MockMarketManager;

public class MarketConsumerTest {

	MockMarketManager manager;
	MockMarketEmployee employee;
	MarketConsumerRole consumer;
	
	/**
	 * Run before each test case. Instantiates a MarketManagerRole, a MockEmployee, and a MockConsumer. Introduces the manager to the new employee.
	 */
	@Before
	public void setUp() throws Exception {
		System.out.println("========== NEW TEST ==========");
		
		// Create the unit under test- our MarketConsumerRole
		consumer = new MarketConsumerRole("consumer");
		assertNotNull("new MarketConsumerRole() returns a null pointer.", consumer);
		
		// Create a MarketManager
		manager = new MockMarketManager("manager");
		
		// Create a consumer to place an order
		employee = new MockMarketEmployee("employee");
	}

	/**
	 * The very basic of tests. Checks to see if a MarketEmployee can retrieve an item and return it to the MarketManager.
	 */
	@Test
	public void simpleTest() {
		// Give the consumer a bill to pay
		consumer.msgHereIsYourTotal(manager, 123.45f);
		
		// Run the consumer's scheduler to verify he pays back the MarketManager
		assertTrue("The MarketConsumer should have paid the market's bill and returned true.", consumer.pickAndExecuteAnAction());
		
		// Ensure that the manager received the HereIsPayment message
		assertTrue("The MarketManager should have gotten a HereIsPayment message from the consumer.", manager.log.getFirstEventWhichContainsString("Received msgHereIsPayment with 123.45") != null);
	}

}
