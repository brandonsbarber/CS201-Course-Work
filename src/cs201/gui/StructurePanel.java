package cs201.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class StructurePanel extends JPanel {
	Rectangle2D rectangle;
	String name;
	SimCity201 city;

	public StructurePanel(Rectangle2D r, int i, SimCity201 sc) {
		rectangle = r;
		name = "" + i;
		city = sc;
		
		setBackground(Color.LIGHT_GRAY);
		setMinimumSize(new Dimension(1000, 250));
		setMaximumSize(new Dimension(1000, 250));
		setPreferredSize(new Dimension(1000, 250));
		
		JLabel j = new JLabel(name);
		add(j);
	}

	public String getName() {
		return name;
	}
	
	public void displayStructurePanel() {
		city.displayStructurePanel(this);
	}

}
