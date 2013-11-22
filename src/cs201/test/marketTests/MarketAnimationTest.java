package cs201.test.marketTests;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;

import cs201.gui.structures.market.MarketAnimationPanel;

public class MarketAnimationTest {
	JFrame mainFrame;
	MarketAnimationPanel animationPanel;

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void test() throws InterruptedException {
		mainFrame = new JFrame();
		mainFrame.setLayout(new BorderLayout());
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setTitle("Market Animation Test");
		mainFrame.setVisible(true);
		mainFrame.setSize(new Dimension(500, 500));
		animationPanel = new MarketAnimationPanel();
		mainFrame.add(animationPanel);
		
		Thread.sleep(2000);
	}

}
