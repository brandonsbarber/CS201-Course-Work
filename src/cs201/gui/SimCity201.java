package cs201.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.LinkedList;

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
		
		normativeDriving();
		
		CityDirectory.getInstance().startTime();
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
	
	private void normativeWalking()
	{
		//One person walks from Market to Restaurant
		MarketAnimationPanel mG = new MarketAnimationPanel(Structure.getNextInstance(),this,50,50);
		MarketStructure m = new MarketStructure(100,100,50,50,Structure.getNextInstance(),mG);
		m.setStructurePanel(mG);
		m.setClosingTime(new CityTime(18, 0));
		buildingPanels.add(mG,""+m.getId());
		cityPanel.addStructure(m);
		CityDirectory.getInstance().addMarket(m);
		
		RestaurantAnimationPanelMatt g = new RestaurantAnimationPanelMatt(Structure.getNextInstance(),this);
		RestaurantMatt r = new RestaurantMatt(475,225,50,50,Structure.getNextInstance(),g);
		r.setStructurePanel(g);
		r.setClosingTime(new CityTime(14, 0));
		buildingPanels.add(g,""+r.getId());
		cityPanel.addStructure(r,new Point(19*25,7*25), new Point(19*25,8*25));
		CityDirectory.getInstance().addRestaurant(r);
		
		PersonAgent p1 = new PersonAgent("Walker",cityPanel);
		p1.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantHost, m, null);
		CityDirectory.getInstance().addPerson(p1);
		p1.startThread();
	}
	
	private void normativeDriving()
	{
		//One person drives from Market to Restaurant
				MarketAnimationPanel mG = new MarketAnimationPanel(Structure.getNextInstance(),this,50,50);
				MarketStructure m = new MarketStructure(100,100,50,50,Structure.getNextInstance(),mG);
				m.setStructurePanel(mG);
				m.setClosingTime(new CityTime(18, 0));
				buildingPanels.add(mG,""+m.getId());
				cityPanel.addStructure(m);
				CityDirectory.getInstance().addMarket(m);
				
				RestaurantAnimationPanelMatt g = new RestaurantAnimationPanelMatt(Structure.getNextInstance(),this);
				RestaurantMatt r = new RestaurantMatt(475,225,50,50,Structure.getNextInstance(),g);
				r.setStructurePanel(g);
				r.setClosingTime(new CityTime(14, 0));
				buildingPanels.add(g,""+r.getId());
				cityPanel.addStructure(r,new Point(19*25,7*25), new Point(19*25,8*25));
				CityDirectory.getInstance().addRestaurant(r);
				
				CarAgent car = new CarAgent();
				CarGui cGui = new CarGui(car,cityPanel);
				car.setGui(cGui);
				cityPanel.addGui(cGui);
				car.startThread();
				
				PersonAgent p1 = new PersonAgent("Walker",cityPanel);
				p1.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantHost, m, car);
				CityDirectory.getInstance().addPerson(p1);
				p1.startThread();
	}
	
	private void normativeBus()
	{
		
	}
	
	public void displayStructurePanel(StructurePanel bp) {
		cardLayout.show(buildingPanels, bp.getName());
	}
}
