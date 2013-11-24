package cs201.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import cs201.agents.PersonAgent;
import cs201.agents.PersonAgent.Intention;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelMatt;
import cs201.helper.CityDirectory;
import cs201.helper.CityTime;
import cs201.structures.residence.Residence;
import cs201.structures.restaurant.RestaurantMatt;

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
		
		// Create initial buildings here and add them to cityPanel and buildingPanels
		RestaurantAnimationPanelMatt g = new RestaurantAnimationPanelMatt(0, this);
		RestaurantMatt r = new RestaurantMatt(40, 40, 40, 40, 0, g);
		r.setClosingTime(new CityTime(17, 0));
		r.setStructurePanel(g);
		buildingPanels.add(g, "" + 0);
		CityDirectory.getInstance().addRestaurant(r);
		cityPanel.addStructure(r);
		
		RestaurantAnimationPanelMatt g2 = new RestaurantAnimationPanelMatt(1, this);
		RestaurantMatt r2 = new RestaurantMatt(40, 100, 40, 40, 1, g2);
		r2.setClosingTime(new CityTime(16, 0));
		r2.setStructurePanel(g2);
		buildingPanels.add(g2, "" + 1);
		CityDirectory.getInstance().addRestaurant(r2);
		cityPanel.addStructure(r2);
		
		Residence r3 = new Residence(40, 160, 40, 40, 2, g);
		CityDirectory.getInstance().addResidence(r3);
		cityPanel.addStructure(r3);
		
		add(BorderLayout.WEST, cityPanel);
		add(BorderLayout.EAST, buildingPanels);
		
		PersonAgent p = new PersonAgent("Matt");
		p.setupPerson(CityDirectory.getInstance().getTime(), r3, r2, Intention.RestaurantHost, r3, null);
		//p.startThread();
		CityDirectory.getInstance().addPerson(p);
		CityDirectory.getInstance().startTime();
	}
	
	public void displayStructurePanel(StructurePanel bp) {
		cardLayout.show(buildingPanels, bp.getName());
	}
}
