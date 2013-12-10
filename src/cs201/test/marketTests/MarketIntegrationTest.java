package cs201.test.marketTests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cs201.gui.roles.market.MarketConsumerGui;
import cs201.gui.roles.market.MarketEmployeeGui;
import cs201.gui.roles.market.MarketManagerGui;
import cs201.gui.structures.market.MarketAnimationPanel;
import cs201.roles.marketRoles.MarketConsumerRole;
import cs201.roles.marketRoles.MarketEmployeeRole;
import cs201.roles.marketRoles.MarketManagerRole;

public class MarketIntegrationTest {

	MarketManagerRole manager;
	MarketEmployeeRole employee;
	MarketEmployeeRole employee2;
	MarketConsumerRole consumer;
	
	JFrame mainFrame;
	MarketAnimationPanel animationPanel;
	
	@Before
	public void setUp() throws Exception {
		// Create the unit under test- our MarketManagerRole
		manager = new MarketManagerRole();
		
		// Create some MarketEmployees
		employee = new MarketEmployeeRole("employee");
		employee2 = new MarketEmployeeRole("employee2");
		
		// Create a consumer to place an order
		consumer = new MarketConsumerRole("consumer");
		
		// Tell the MarketManager about the employee
		manager.addEmployee(employee);
		manager.addEmployee(employee2);
		
		// Set up the GUI
		setUpGui();
	}
	
	public void setUpGui() {
		// Create a new JFrame
		mainFrame = new JFrame();
		mainFrame.setLayout(new BorderLayout());
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setTitle("Market Integration Test");
		mainFrame.setVisible(true);
		mainFrame.setSize(new Dimension(500, 500));
		
		// Add our AnimationPanel to it
		animationPanel = new MarketAnimationPanel(0, null, 500, 500);
		mainFrame.add(animationPanel);
		
		// Create a new MarketManager gui and add him to our animation panel
		MarketManagerGui managerGui = new MarketManagerGui();
		animationPanel.addGui(managerGui);
		manager.setGui(managerGui);
		
		// Create a new MarketEmployee gui and add him to our animation panel
		MarketEmployeeGui employeeGui = new MarketEmployeeGui(animationPanel, 2, 2);
		animationPanel.addGui(employeeGui);
		employee.setGui(employeeGui);
		employeeGui.setRole(employee);
		
		// Create a new MarketEmployee gui and add him to our animation panel
		MarketEmployeeGui employeeGui2 = new MarketEmployeeGui(animationPanel, 4, 2);
		animationPanel.addGui(employeeGui2);
		employee2.setGui(employeeGui2);
		employeeGui2.setRole(employee2);
		
		// Create a consumer gui and add him to our animation panel
		MarketConsumerGui consumerGui = new MarketConsumerGui();
		animationPanel.addGui(consumerGui);
		consumer.setGui(consumerGui);
		consumerGui.setRole(consumer);
		consumerGui.setAnimationPanel(animationPanel);
}

	@Test
	public void guiIntegrationTest() {
		// Give the market a starting amount of chicken for inventory
		manager.addInventoryEntry(new MarketManagerRole.InventoryEntry("chicken", 100, 6.99f));
		manager.addInventoryEntry(new MarketManagerRole.InventoryEntry("lettuce", 200, 1.99f));
		
		consumer.startInteraction(null);

		// Create our order by creating some item requests and adding them to a list
		MarketManagerRole.ItemRequest item1 = new MarketManagerRole.ItemRequest("chicken", 4);
		MarketManagerRole.ItemRequest item2 = new MarketManagerRole.ItemRequest("lettuce", 5);
		List<MarketManagerRole.ItemRequest> list = new ArrayList<MarketManagerRole.ItemRequest>();
		list.add(item1);
		list.add(item2);

		// Give the market manager our order
		manager.msgHereIsMyOrder(consumer, list);
		
		// Call the MarketManager's scheduler once to dispatch an employee
		assertTrue("The MarketManager's scheduler should have processed the first order and returned true.", manager.pickAndExecuteAnAction());
		
		// Call the employee's scheduler once to send the order back to the MarketConsumer
		assertTrue("The employee's scheduler should have retrieved the item and returned true.", employee2.pickAndExecuteAnAction());
		
		// Call the employee's scheduer to return home
		assertFalse("The employee's scheduler should have nothing left to do.", employee2.pickAndExecuteAnAction());
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	@Ignore
	public void guiIntegrationTest2Employees() {
		// Give the market a starting amount of chicken for inventory
		manager.addInventoryEntry(new MarketManagerRole.InventoryEntry("chicken", 100, 6.99f));
		manager.addInventoryEntry(new MarketManagerRole.InventoryEntry("lettuce", 200, 1.99f));

		// Create some item requests
		MarketManagerRole.ItemRequest item1 = new MarketManagerRole.ItemRequest("chicken", 4);
		MarketManagerRole.ItemRequest item2 = new MarketManagerRole.ItemRequest("lettuce", 5);
		
		// Create our first order
		List<MarketManagerRole.ItemRequest> order1 = new ArrayList<MarketManagerRole.ItemRequest>();
		order1.add(item1);
		order1.add(item2);
		
		// Create our second order
		List<MarketManagerRole.ItemRequest> order2 = new ArrayList<MarketManagerRole.ItemRequest>();
		order2.add(item1);

		// Give the market manager our order
		manager.msgHereIsMyOrder(consumer, order1);
		
		// Call the MarketManager's scheduler once to dispatch an employee
		assertTrue("The MarketManager's scheduler should have processed the first order and returned true.", manager.pickAndExecuteAnAction());
		
		// Give the market manager our second order
		manager.msgHereIsMyOrder(consumer, order2);
		
		// Call the MarketManager's scheduler once to dispatch the next employee
		assertTrue("The MarketManager's scheduler should have processed the first order and returned true.", manager.pickAndExecuteAnAction());
		
		// Call the employee's scheduler once to retrieve the items
		assertTrue("The employee's scheduler should have retrieved the item and returned true.", employee.pickAndExecuteAnAction());
		
		// Call the next employee's scheduler
		assertTrue("The employee's scheduler should have retrieved the item and returned true.", employee2.pickAndExecuteAnAction());
	}

}
