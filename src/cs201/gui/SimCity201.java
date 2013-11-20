package cs201.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SimCity201 extends JFrame {
	CityPanel cityPanel;
	JPanel buildingPanels;
	CardLayout cardLayout;
	
	public SimCity201() {
		setVisible(true);
		setSize(1000, 500);
		
		setLayout(new BorderLayout());
		
		cityPanel = new CityPanel();
		cityPanel.setPreferredSize(new Dimension(1000, 250));
		cityPanel.setMaximumSize(new Dimension(1000, 250));
		cityPanel.setMinimumSize(new Dimension(1000, 250));
		
		cardLayout = new CardLayout();
		
		buildingPanels = new JPanel();
		buildingPanels.setLayout(cardLayout);
		buildingPanels.setMinimumSize(new Dimension(1000, 250));
		buildingPanels.setMaximumSize(new Dimension(1000, 250));
		buildingPanels.setPreferredSize(new Dimension(1000, 250));
		buildingPanels.setBackground(Color.YELLOW);
		
		// Create initial buildings here and add them to cityPanel and buildingPanels
		for (int i = 0; i < 3; i++) {
			
		}
		
		
		add(BorderLayout.NORTH, cityPanel);
		add(BorderLayout.SOUTH, buildingPanels);
	}
	
	public void displayBuildingPanel(BuildingPanel bp) {
		cardLayout.show(buildingPanels, bp.getName());
	}
}
