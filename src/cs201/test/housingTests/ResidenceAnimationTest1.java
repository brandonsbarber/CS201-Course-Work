package cs201.test.housingTests;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import org.junit.Before;
import org.junit.Test;

import cs201.gui.structures.residence.ResidenceAnimationPanel;
import cs201.structures.residence.Residence;

public class ResidenceAnimationTest1 {

	JFrame frame;
	ResidenceAnimationPanel resPanel;
	Residence residence;
	
	@Before
	public void setUp() throws Exception {
		
		JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.setTitle("Residence");
		frame.setSize(new Dimension(500, 500));
		frame.setVisible(true);
		
		ResidenceAnimationPanel resPanel = new ResidenceAnimationPanel(0, null);
		frame.add(resPanel);
		
		Residence residence = new Residence(0, 0, 0, 0, 0, resPanel, false); // new residence. not an apartment.
		
		
		
	}

	@Test
	public void test() {
		
		residence.getResident().msgStartEating();
	}

}
