package cs201.test.housingTests;

import static org.junit.Assert.*;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import org.junit.Before;
import org.junit.Test;

import cs201.gui.structures.residence.ResidenceGui;

public class ResidenceAnimationTest {

	JFrame wrapper;
	ResidenceGui residenceGui;
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		
		wrapper = new JFrame();
		wrapper.setLayout(new BorderLayout());
		wrapper.setTitle("Animation Test");
		wrapper.setVisible(true);
		wrapper.setSize(new Dimension(300, 300));
		
		//residenceGui = new ResidenceGui();
        wrapper.add(residenceGui);
		
	}

}
