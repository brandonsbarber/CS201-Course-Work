package cs201.test.marketTests;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JFrame;

import org.junit.Before;
import org.junit.Test;

import cs201.gui.roles.market.MarketConsumerGui;
import cs201.gui.roles.market.MarketEmployeeGui;
import cs201.gui.roles.market.MarketManagerGui;
import cs201.gui.structures.market.MarketAnimationPanel;

public class MarketAnimationTest {
	JFrame mainFrame;
	MarketAnimationPanel animationPanel;

	@Before
	public void setUp() throws Exception {

	}
	
	/**
	 * A test to see if our MarketAnimationPanel works with a MarketManagerGui, MarketEmployeeGui, and a MarketConsumerGui.
	 * This tests always "passes" in terms of JUnit, but the point is to watch the animation to make sure everything looks correct.
	 * @throws InterruptedException
	 * @throws IOException
	 */
	@Test
	public void test() throws InterruptedException, IOException {
		// Create a new JFrame
		mainFrame = new JFrame();
		mainFrame.setLayout(new BorderLayout());
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setTitle("Market Animation Test");
		mainFrame.setVisible(true);
		mainFrame.setSize(new Dimension(500, 500));
		
		// Add our AnimationPanel to it
		animationPanel = new MarketAnimationPanel(0, null, 500, 500);
		mainFrame.add(animationPanel);
		
		// Create a new MarketManager gui and add him to our animation panel
		MarketManagerGui managerGui = new MarketManagerGui();
		animationPanel.addGui(managerGui);
		
		// Create a new MarketEmployee gui and add him to our animation panel
		MarketEmployeeGui employeeGui = new MarketEmployeeGui(animationPanel, 1, 1);
		animationPanel.addGui(employeeGui);
		employeeGui.setPresent(true);
		
		// Create a new MarketEmployee gui and add him to our animation panel
		MarketEmployeeGui employeeGui2 = new MarketEmployeeGui(animationPanel, 1, 2);
		animationPanel.addGui(employeeGui2);
		employeeGui2.setPresent(true);
		
		// Create a new MarketEmployee gui and add him to our animation panel
		MarketEmployeeGui employeeGui3 = new MarketEmployeeGui(animationPanel, 1, 3);
		animationPanel.addGui(employeeGui3);
		employeeGui3.setPresent(true);
		
		// Create a new MarketConsumer gui and add him to our animation panel
		MarketConsumerGui consumer = new MarketConsumerGui();
		animationPanel.addGui(consumer);
		consumer.setPresent(true);
		
		// Just make the employees walk to random shelves
		consumer.doWalkToManager();
		/*
		Random generator = new Random();
		while(true) {
			employeeGui.doGoToItemOnShelf(generator.nextInt(5), generator.nextInt(5));
			employeeGui2.doGoToItemOnShelf(generator.nextInt(5), generator.nextInt(5));
			employeeGui3.doGoToItemOnShelf(generator.nextInt(5), generator.nextInt(5));
			Thread.sleep(6000);
		}*/
		employeeGui.doLeaveMarket();
		Thread.sleep(6000);

	}

}
