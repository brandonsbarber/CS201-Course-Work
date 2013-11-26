package cs201.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import cs201.agents.PersonAgent;
import cs201.agents.PersonAgent.Intention;
import cs201.agents.transit.CarAgent;
import cs201.gui.structures.market.MarketAnimationPanel;
import cs201.gui.structures.market.MarketConfigPanel;
import cs201.gui.structures.residence.ResidenceAnimationPanel;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelMatt;
import cs201.gui.transit.CarGui;
import cs201.helper.CityDirectory;
import cs201.helper.CityTime;
import cs201.structures.Structure;
import cs201.structures.market.MarketStructure;
import cs201.structures.residence.Residence;
import cs201.structures.restaurant.RestaurantMatt;

public class SimCity201 extends JFrame {
	private final int SIZEX = 1200;
	private final int SIZEY	= 800;
	
	CityPanel cityPanel;
	JPanel buildingPanels;
	CardLayout cardLayout;
	
	SettingsPanel settingsPanel;
	
	public SimCity201() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setSize(SIZEX, SIZEY);
		
		JPanel guiPanel = new JPanel();
		
		setLayout(new BorderLayout());
		
		guiPanel.setLayout(new BorderLayout());
		
		cityPanel = new CityPanel();
		cityPanel.setPreferredSize(new Dimension(SIZEX * 3/5, SIZEY * 3 / 5));
		cityPanel.setMaximumSize(new Dimension(SIZEX * 3/5, SIZEY * 3 / 5));
		cityPanel.setMinimumSize(new Dimension(SIZEX * 3/5, SIZEY * 3 / 5));
		
		cardLayout = new CardLayout();
		
		buildingPanels = new JPanel();
		buildingPanels.setLayout(cardLayout);
		buildingPanels.setMinimumSize(new Dimension(SIZEX * 2/5, SIZEY * 3 / 5));
		buildingPanels.setMaximumSize(new Dimension(SIZEX * 2/5, SIZEY * 3 / 5));
		buildingPanels.setPreferredSize(new Dimension(SIZEX * 2/5, SIZEY * 3 / 5));
		buildingPanels.setBackground(Color.YELLOW);

		// Create initial buildings here and add them to cityPanel and buildingPanels
		
		JScrollPane cityScrollPane = new JScrollPane(cityPanel);
		
		cityScrollPane.setMinimumSize(new Dimension(SIZEX * 3/5, SIZEY * 3 / 5));
		cityScrollPane.setMaximumSize(new Dimension(SIZEX * 3/5, SIZEY * 3 / 5));
		cityScrollPane.setPreferredSize(new Dimension(SIZEX * 3/5, SIZEY * 3 / 5));
		
		cityScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		cityScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		guiPanel.add(BorderLayout.WEST, cityScrollPane);
		guiPanel.add(BorderLayout.EAST, buildingPanels);
		
		settingsPanel = new SettingsPanel();
		
		settingsPanel.setMinimumSize(new Dimension(SIZEX, SIZEY * 2/5));
		settingsPanel.setMaximumSize(new Dimension(SIZEX, SIZEY * 2/5));
		settingsPanel.setPreferredSize(new Dimension(SIZEX, SIZEY * 2/5));
		
		add(BorderLayout.SOUTH, settingsPanel);
		add(BorderLayout.NORTH, guiPanel);
		
		pack();
		CityDirectory.getInstance().startTime();
		
		normativeRestaurant();
	}
	
	private void normativeRestaurant() {
		// One customer eats, pays, and leaves
		RestaurantAnimationPanelMatt g = new RestaurantAnimationPanelMatt(Structure.getNextInstance(),this);
		settingsPanel.addPanel("Restaurants",new ConfigPanel());
		RestaurantMatt r = new RestaurantMatt(100,100,50,50,Structure.getNextInstance(),g);
		r.setStructurePanel(g);
		r.setClosingTime(new CityTime(14, 0));
		buildingPanels.add(g,""+r.getId());
		cityPanel.addStructure(r);
		CityDirectory.getInstance().addRestaurant(r);
		
		PersonAgent p1 = new PersonAgent("Host", cityPanel);
		p1.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantHost, r, null);
		p1.setHungerEnabled(false);
		p1.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p1);
		p1.startThread();
		
		PersonAgent p2 = new PersonAgent("Cashier", cityPanel);
		p2.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCashier, r, null);
		p2.setHungerEnabled(false);
		p2.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p2);
		p2.startThread();
		
		PersonAgent p3 = new PersonAgent("Cook", cityPanel);
		p3.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCook, r, null);
		p3.setHungerEnabled(false);
		p3.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p3);
		p3.startThread();
		
		PersonAgent p4 = new PersonAgent("Waiter", cityPanel);
		p4.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantWaiter, r, null);
		p4.setHungerEnabled(false);
		p4.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p4);
		p4.startThread();
		
		PersonAgent p5 = new PersonAgent("Waiter", cityPanel);
		p5.setupPerson(CityDirectory.getInstance().getTime(), null, null, null, r, null);
		CityDirectory.getInstance().addPerson(p5);
		p5.startThread();
	}
	
	public void displayStructurePanel(StructurePanel bp) {
		cardLayout.show(buildingPanels, bp.getName());
	}
}
