package cs201.test.marketTests;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.util.Random;

import javax.swing.JFrame;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cs201.gui.ArtManager;
import cs201.gui.roles.market.MarketConsumerGui;
import cs201.gui.roles.market.MarketEmployeeGui;
import cs201.gui.roles.market.MarketManagerGui;
import cs201.gui.structures.market.MarketAnimationPanel;

public class MarketAnimationTest {
	JFrame mainFrame;
	MarketAnimationPanel animationPanel;
	MarketManagerGui managerGui;
	MarketEmployeeGui employeeGui, employeeGui2, employeeGui3;
	MarketConsumerGui consumerGui;

	@Before
	public void setUp() throws Exception {
		ArtManager.load();
		
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
		managerGui = new MarketManagerGui();
		animationPanel.addGui(managerGui);
		
		// Create a new MarketEmployee gui and add him to our animation panel
		employeeGui = new MarketEmployeeGui(animationPanel, 2, 2);
		animationPanel.addGui(employeeGui);
		employeeGui.setPresent(true);
		
		// Create a new MarketEmployee gui and add him to our animation panel
		employeeGui2 = new MarketEmployeeGui(animationPanel, 4, 2);
		animationPanel.addGui(employeeGui2);
		employeeGui2.setPresent(true);
		
		// Create a new MarketEmployee gui and add him to our animation panel
		employeeGui3 = new MarketEmployeeGui(animationPanel, 6, 2);
		animationPanel.addGui(employeeGui3);
		employeeGui3.setPresent(true);
		
		// Create a new MarketConsumer gui and add him to our animation panel
		consumerGui = new MarketConsumerGui();
		animationPanel.addGui(consumerGui);
		consumerGui.setAnimationPanel(animationPanel);
		consumerGui.setPresent(true);
	}
	
	/**
	 * A test to see if our MarketAnimationPanel works with a MarketManagerGui, MarketEmployeeGui, and a MarketConsumerGui.
	 * This tests always "passes" in terms of JUnit, but the point is to watch the animation to make sure everything looks correct.
	 * @throws InterruptedException
	 * @throws IOException
	 */
	@Test
	@Ignore
	public void shelfTest() throws InterruptedException, IOException {
		enterEmployees(3);
		
		// Just make the employees walk to random shelves
		Random generator = new Random();
		for(int i = 0; i < 3; i++) {
			employeeGui.doGoToItemOnShelf(generator.nextInt(5), generator.nextInt(5));
			employeeGui2.doGoToItemOnShelf(generator.nextInt(5), generator.nextInt(5));
			employeeGui3.doGoToItemOnShelf(generator.nextInt(5), generator.nextInt(5));
			Thread.sleep(6000);
		}
		
		exitEmployees(3);
	}
	
	@Test
	public void carTest() throws InterruptedException {
		enterEmployees(1);
		
		// Make the employee get a car
		employeeGui.doWalkToCarLot();
		Thread.sleep(10000);
		employeeGui.setHasCar(true);
		employeeGui.setMovingCarIn(true);
		employeeGui.doBringCarOut();
		Thread.sleep(10000);
		employeeGui.setMovingCarIn(false);
		employeeGui.setMovingCarOut(true);
		
		exitEmployees(1);
	}
	
	private void enterEmployees(int howMany) throws InterruptedException {
		// Make the employees enter the market
		if (howMany >= 1) employeeGui.doEnterMarket();
		if (howMany >= 2) employeeGui2.doEnterMarket();
		if (howMany >= 3) employeeGui3.doEnterMarket();
		Thread.sleep(5000);
	}
	
	private void exitEmployees(int howMany) throws InterruptedException {
		// Make the employees leave the market
		if (howMany >= 1) employeeGui.doLeaveMarket();
		if (howMany >= 2) employeeGui2.doLeaveMarket();
		if (howMany >= 3) employeeGui3.doLeaveMarket();
		Thread.sleep(7000);
	}

}
