package cs201.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import cs201.helper.CityDirectory;

public class SimCity201 extends JFrame {
	private final int SIZEX = 1200;
	private final int SIZEY	= 700;
	
	CityPanel cityPanel;
	JPanel buildingPanels;
	CardLayout cardLayout;
	
	public SimCity201() {
		setVisible(true);
		setSize(SIZEX, SIZEY);
		
		setLayout(new BorderLayout());
		
		cityPanel = new CityPanel();
		cityPanel.setPreferredSize(new Dimension(SIZEX / 2, SIZEY));
		cityPanel.setMaximumSize(new Dimension(SIZEX / 2, SIZEY));
		cityPanel.setMinimumSize(new Dimension(SIZEX / 2, SIZEY));
		
		cardLayout = new CardLayout();
		
		buildingPanels = new JPanel();
		buildingPanels.setLayout(cardLayout);
		buildingPanels.setMinimumSize(new Dimension(SIZEX / 2, SIZEY));
		buildingPanels.setMaximumSize(new Dimension(SIZEX / 2, SIZEY));
		buildingPanels.setPreferredSize(new Dimension(SIZEX / 2, SIZEY));
		buildingPanels.setBackground(Color.YELLOW);
		
		add(BorderLayout.WEST, cityPanel);
		add(BorderLayout.EAST, buildingPanels);

		CityDirectory.getInstance().startTime();
	}
	
	public void displayStructurePanel(StructurePanel bp) {
		cardLayout.show(buildingPanels, bp.getName());
	}
}
