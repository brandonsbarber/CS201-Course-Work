package cs201.test.marketTests;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;

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
		animationPanel = new MarketAnimationPanel();
		mainFrame.add(animationPanel);
		
		// Create a new MarketManager gui and add him to our animation panel
		MarketManagerGui managerGui = new MarketManagerGui();
		animationPanel.addGui(managerGui);
		
		// Create a new MarketEmployee gui and add him to our animation panel
		MarketEmployeeGui employeeGui = new MarketEmployeeGui();
		animationPanel.addGui(employeeGui);
		MarketEmployeeGui employeeGui2 = new MarketEmployeeGui();
		animationPanel.addGui(employeeGui2);
		
		employeeGui.doGoToItemOnShelf(2, 7);
		Thread.sleep(5000);
		employeeGui2.doGoToItemOnShelf(0, 4);
		Thread.sleep(5000);
		employeeGui.doGoToItemOnShelf(1, 4);
		Thread.sleep(10000);
		employeeGui.doGoToItemOnShelf(0, 3);
		Thread.sleep(10000);
		employeeGui.doGoToManager();
		Thread.sleep(10000);
		employeeGui.doGoHome();
		
		System.in.read();
	}

}
