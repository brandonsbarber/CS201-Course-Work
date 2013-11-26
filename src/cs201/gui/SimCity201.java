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
		
		MarketConfigPanel marketConfigPanel1 = new MarketConfigPanel();
		settingsPanel.addPanel("Restaurants",new ConfigPanel());
		settingsPanel.addPanel("Transit",new TransitConfigPanel());
		settingsPanel.addPanel("Transit",new TransitConfigPanel());
		settingsPanel.addPanel("Banks",new ConfigPanel());
		settingsPanel.addPanel("Markets",marketConfigPanel1);
		settingsPanel.addPanel("Housing",new ConfigPanel());
		settingsPanel.addPanel("Housing",new ConfigPanel());
		settingsPanel.addPanel("Housing",new ConfigPanel());
		settingsPanel.addPanel("Restaurants",new ConfigPanel());
		settingsPanel.addPanel("Restaurants",new ConfigPanel());
		
		RestaurantAnimationPanelMatt g = new RestaurantAnimationPanelMatt(Structure.getNextInstance(),this);
		RestaurantMatt r = new RestaurantMatt(100,100,50,50,Structure.getNextInstance(),g);
		r.setStructurePanel(g);
		r.setClosingTime(new CityTime(10, 30));
		buildingPanels.add(g,""+r.getId());
		cityPanel.addStructure(r);
		CityDirectory.getInstance().addRestaurant(r);		
		
		MarketAnimationPanel mG = new MarketAnimationPanel(Structure.getNextInstance(),this, 50, 50);
		MarketStructure m = new MarketStructure(225,100,50,50,Structure.getNextInstance(),mG);
		m.setStructurePanel(mG);
		m.setClosingTime(new CityTime(18, 0));
		buildingPanels.add(mG,""+m.getId());
		cityPanel.addStructure(m);
		CityDirectory.getInstance().addMarket(m);
		marketConfigPanel1.setStructure(m);
		
		/*
		MarketAnimationPanel mG2 = new MarketAnimationPanel(Structure.getNextInstance(),this, 50, 50);
		MarketStructure m2 = new MarketStructure(19*25,9*25,50,50,Structure.getNextInstance(),mG2);
		m2.setStructurePanel(mG2);
		buildingPanels.add(mG2,""+m2.getId());
		cityPanel.addStructure(m2);
		CityDirectory.getInstance().addMarket(m2);
		*/
		
		ResidenceAnimationPanel rap = new ResidenceAnimationPanel(Structure.getNextInstance(), this);
		Residence re = new Residence(19*25 ,9*25 ,50 ,50, Structure.getNextInstance(), rap, false);
		re.setStructurePanel(rap);
		buildingPanels.add(rap, "" + re.getId());
		cityPanel.addStructure(re);
		CityDirectory.getInstance().addResidence(re);
		
		pack();
		CityDirectory.getInstance().startTime();
		
		
		PersonAgent p6 = new PersonAgent("Manager", cityPanel);
		p6.setupPerson(CityDirectory.getInstance().getTime(), null, m, Intention.MarketManager, m, null);
		CityDirectory.getInstance().addPerson(p6);
		p6.startThread();
		
		CarAgent car = new CarAgent();
		CarGui cGui = new CarGui(car, cityPanel);
		car.setGui(cGui);
		car.startThread();
		cityPanel.addGui(cGui);
		
		PersonAgent p7 = new PersonAgent("Employee", cityPanel);
		p7.setupPerson(CityDirectory.getInstance().getTime(), re, m, Intention.MarketEmployee, re, car);
		CityDirectory.getInstance().addPerson(p7);
		p7.startThread();
		
		PersonAgent p8 = new PersonAgent("Market Customer", cityPanel);
		p8.setupPerson(CityDirectory.getInstance().getTime(), null, null, null, m, null);
		CityDirectory.getInstance().addPerson(p8);
		p8.startThread();
	}
	
	private void normativeRestaurant() {
		// One customer eats, pays, and leaves
		RestaurantAnimationPanelMatt g = new RestaurantAnimationPanelMatt(Structure.getNextInstance(),this);
		RestaurantMatt r = new RestaurantMatt(100,100,50,50,Structure.getNextInstance(),g);
		r.setStructurePanel(g);
		r.setClosingTime(new CityTime(14, 0));
		buildingPanels.add(g,""+r.getId());
		cityPanel.addStructure(r);
		CityDirectory.getInstance().addRestaurant(r);
		
		PersonAgent p1 = new PersonAgent("Host", cityPanel);
		p1.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantHost, r, null);
		CityDirectory.getInstance().addPerson(p1);
		p1.startThread();
	}
	
	public void displayStructurePanel(StructurePanel bp) {
		cardLayout.show(buildingPanels, bp.getName());
	}
}
