 package cs201.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import cs201.agents.PersonAgent;
import cs201.agents.PersonAgent.Intention;
import cs201.agents.transit.BusAgent;
import cs201.agents.transit.CarAgent;
import cs201.agents.transit.TruckAgent;
import cs201.gui.configPanels.MarketConfigPanel;
import cs201.gui.configPanels.PersonConfigPanel;
import cs201.gui.configPanels.ResidenceConfigPanel;
import cs201.gui.configPanels.RestaurantConfigPanel;
import cs201.gui.configPanels.TransitConfigPanel;
import cs201.gui.structures.market.MarketAnimationPanel;
import cs201.gui.structures.residence.ApartmentComplexAnimationPanel;
import cs201.gui.structures.residence.ResidenceAnimationPanel;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelBen;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelBrandon;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelMatt;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelSkyler;
import cs201.gui.structures.transit.BusStopAnimationPanel;
import cs201.gui.transit.BusGui;
import cs201.gui.transit.CarGui;
import cs201.helper.CityDirectory;
import cs201.helper.CityTime;
import cs201.helper.CityTime.WeekDay;
import cs201.helper.Constants;
import cs201.helper.transit.BusRoute;
import cs201.interfaces.roles.housing.Renter;
import cs201.roles.marketRoles.MarketManagerRole.InventoryEntry;
import cs201.roles.marketRoles.MarketManagerRole.ItemRequest;
import cs201.roles.restaurantRoles.Matt.RestaurantCookRoleMatt;
import cs201.roles.restaurantRoles.Brandon.RestaurantCookRoleBrandon;
import cs201.roles.restaurantRoles.Skyler.RestaurantCookRoleSkyler;
import cs201.structures.Structure;
import cs201.structures.market.MarketStructure;
import cs201.structures.residence.ApartmentComplex;
import cs201.structures.residence.Residence;
import cs201.structures.restaurant.RestaurantBen;
import cs201.structures.restaurant.RestaurantBrandon;
import cs201.structures.restaurant.RestaurantMatt;
import cs201.structures.restaurant.RestaurantSkyler;
import cs201.structures.transit.BusStop;

@SuppressWarnings("serial")
public class SimCity201 extends JFrame {
	public static final int SIZEX = 1200;
	public static final int SIZEY = 800;
	
	CityPanel cityPanel;
	JPanel buildingPanels;
	CardLayout cardLayout;
	ScenarioPanel scenarioPanel;
	TimePanel timePanel;
	
	SettingsPanel settingsPanel;
	MarketConfigPanel marketPanel;
	RestaurantConfigPanel restaurantPanel;
	PersonConfigPanel personPanel;
	TransitConfigPanel transitPanel;
	ResidenceConfigPanel residencePanel;
	
	BaseSettingsPanel bottomSettingsPanel;
	
	/**
	 * Creates the entire city panel, then prompts for a scenario to execute
	 */
	public SimCity201() {
		try {
			ArtManager.load();
		} catch(IOException e) {
			JOptionPane.showMessageDialog(null, "There was a problem loading your images.");
			System.exit(1);
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(false);
		setSize(SIZEX, SIZEY);
		setTitle("SimCity201 - Team 21");
		
		JPanel mainPanel = new JPanel();
		
		JPanel guiPanel = new JPanel();
		
		bottomSettingsPanel = new BaseSettingsPanel();
		CityDirectory.getInstance().addTimerActionListener(bottomSettingsPanel);
		
		timePanel = new TimePanel();
		bottomSettingsPanel.setTimePanel(timePanel);
		
		mainPanel.setLayout(new BorderLayout());
		setLayout(new BorderLayout());
		
		guiPanel.setLayout(new BorderLayout());
		
		cityPanel = new CityPanel(this);
		
		cardLayout = new CardLayout();
		
		buildingPanels = new JPanel();
		buildingPanels.setLayout(cardLayout);
		buildingPanels.setBorder(BorderFactory.createEtchedBorder());
		buildingPanels.setMinimumSize(new Dimension(SIZEX * 2/5, SIZEY * 3 / 5));
		buildingPanels.setMaximumSize(new Dimension(SIZEX * 2/5, SIZEY * 3 / 5));
		buildingPanels.setPreferredSize(new Dimension(SIZEX * 2/5, SIZEY * 3 / 5));
		
		JPanel blankPanel = new JPanel();
		buildingPanels.add(blankPanel, "blank");
		
		JScrollPane cityScrollPane = new JScrollPane(cityPanel);
		
		cityScrollPane.setMinimumSize(new Dimension(SIZEX * 3/5, SIZEY * 3 / 5));
		cityScrollPane.setMaximumSize(new Dimension(SIZEX * 3/5, SIZEY * 3 / 5));
		cityScrollPane.setPreferredSize(new Dimension(SIZEX * 3/5, SIZEY * 3 / 5));
		
		cityScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		cityScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		guiPanel.add(BorderLayout.WEST, cityScrollPane);
		guiPanel.add(BorderLayout.EAST, buildingPanels);
		
		settingsPanel = new SettingsPanel();
		
		settingsPanel.setMinimumSize(new Dimension(SIZEX, SIZEY * 2/5));
		settingsPanel.setMaximumSize(new Dimension(SIZEX, SIZEY * 2/5));
		settingsPanel.setPreferredSize(new Dimension(SIZEX, SIZEY * 2/5));
		
		personPanel = new PersonConfigPanel();
		settingsPanel.addPanel("PersonAgentPanel", personPanel);
		transitPanel = new TransitConfigPanel();
		settingsPanel.addPanel("Transit",transitPanel);
		marketPanel = new MarketConfigPanel();
		settingsPanel.addPanel("Markets", marketPanel);
		restaurantPanel = new RestaurantConfigPanel();
		settingsPanel.addPanel("Restaurants", restaurantPanel);
		residencePanel = new ResidenceConfigPanel();
		settingsPanel.addPanel("Residences", residencePanel);
		
		mainPanel.add(BorderLayout.SOUTH, settingsPanel);
		mainPanel.add(BorderLayout.NORTH, guiPanel);
		
		add(bottomSettingsPanel,BorderLayout.SOUTH);
		add(mainPanel);
		
		List<String> scenarioList = new ArrayList<String>();
		
		scenarioList.add("RestaurantMatt: Normative");
		scenarioList.add("RestaurantMatt: Two Customers, Two Waiters");
		scenarioList.add("RestaurantMat: Shift Change");
		scenarioList.add("RestaurantBrandon: Normative");
		scenarioList.add("RestaurantBrandon: Two Customers, Two Waiters");	
		scenarioList.add("RestaurantBrandon: Shift Change");
		scenarioList.add("RestaurantBen: Normative");
		scenarioList.add("RestaurantBen: Two Customers, Two Waiters");
		scenarioList.add("RestaurantBen: Shift Change");
		scenarioList.add("RestaurantSkyler: Normative");
		scenarioList.add("RestaurantSkyler: Two Customers, Two Waiters");
		scenarioList.add("RestaurantSkyler: Shift Change");
		
		scenarioList.add("Bus: Normative");
		scenarioList.add("Bus: Joust");
		scenarioList.add("Beaucoup Buses");
		scenarioList.add("Killer Buses");
		scenarioList.add("Walking: Normative");
		scenarioList.add("100 People");
		scenarioList.add("Driving: Normative");
		
		scenarioList.add("Market: Normative");	
		scenarioList.add("Market: Purchase Car");
		scenarioList.add("Market: Shift Change");
		scenarioList.add("Market: RestaurantMatt Delivery");
		scenarioList.add("Market: RestaurantBrandon Delivery");
		scenarioList.add("Market: RestaurantBen Delivery");
		scenarioList.add("Market: RestaurantSkyler Deliver");
		scenarioList.add("Market: Failed Delivery");
		
		scenarioList.add("Residence: Normative");
		scenarioList.add("Residence: Out of single food item");
		scenarioList.add("Residence: Completely out of food");
		scenarioList.add("Apartment Complex: Normative");

		scenarioList.add("Weekend Behavior Change");
		scenarioList.add("All Workplaces");
		
		scenarioList.add("Reset City"); // keep as last item
		
		scenarioPanel = new ScenarioPanel(scenarioList);
		bottomSettingsPanel.setScenarioPanel(scenarioPanel);
		scenarioPanel.setSimCity(this);
		
		pack();
		CityDirectory.getInstance().startTime();
	}
	
	/**
	 * Runs a given scenario. This method is called internally, and also by the scenarioPanel to invoke new scenarios.
	 * @param scenarioNumber The number of the scenario to run, corresponding to the order they are added to the scenarioList.
	 * (The first scenario is 1)
	 */
	public void runScenario(int scenarioNumber) {
		clearScenario();
		
		switch(scenarioNumber)
		{
			case 1: normativeRestaurant(); break;
			case 2: normativeRestaurantTwoCustomersTwoWaiters(); break;
			case 3: restaurantShiftChange(); break;
			case 4: brandonRestaurant(); break;
			case 5: brandonRestaurantTwoCustomersTwoWaiters(); break;
			case 6: brandonRestaurantShiftChange(); break;
			case 7: normativeRestaurantBen(); break;
			case 8: normativeRestaurantBenTwoOfEach(); break;
			case 9: benRestaurantShiftChange(); break;
			case 10: skylerRestaurant(); break;
			case 11: skylerRestaurantTwo(); break; //
			case 12: break; //
			case 13: normativeBus(); break;
			case 14: joust(); break;
			case 15: beaucoupBuses(); break;
			case 16: hundredPeopleBus(); break;
			case 17: normativeWalking(); break;
			case 18: hundredPeople(); break;
			case 19: normativeDriving(); break;
			case 20: normativeMarket(); break;
			case 21: marketConsumerCar(); break;
			case 22: marketShiftChange(); break;
			case 23: normativeMarketRestaurantDelivery(); break;
			case 24: brandonRestaurantMarketOrder(); break;
			case 25: normativeMarketRestaurantBenDelivery(); break;
			case 26: normativeMarketRestaurantSkylerDelivery(); break; //
			case 27: failedMarketDeliveryTruck(); break;
			case 28: normativeResidence(); break;
			case 29: residenceOutOfFood(false); break;
			case 30: residenceOutOfFood(true); break;
			case 31: normativeApartmentComplex(); break;
			case 32: weekendDifference(); break;
			case 33: allWorkplaces(); break;
			default: return;
		}
	}

	private void drunk()
	{
CityDirectory.getInstance().setStartTime(new CityTime(8, 0));
		
		ArrayList<BusStop> stops = new ArrayList<BusStop>();

		BusStopAnimationPanel panel = new BusStopAnimationPanel(Structure.getNextInstance(),this);
		BusStop stop1;
		stops.add(stop1 = new BusStop(4*25,11*25,25,25,1, panel));
		timePanel.addAnimationPanel(panel);
		stop1.setStructurePanel(panel);
		panel.setStop(stop1);
		buildingPanels.add(""+stop1.getId(),panel);
		
		cityPanel.addStructure(stop1,new Point(3*25,11*25),new Point((int)stop1.getRect().x,(int)stop1.getRect().y));
		
		BusStopAnimationPanel panel2 = new BusStopAnimationPanel(Structure.getNextInstance(),this);
		BusStop stop2;
		stops.add(stop2 = new BusStop(25*25,6*25,25,25,2, panel2));
		timePanel.addAnimationPanel(panel2);
		stop2.setStructurePanel(panel2);
		panel2.setStop(stop2);
		buildingPanels.add(""+stop2.getId(),panel2);
		
		cityPanel.addStructure(stop2,new Point(26*25,6*25),new Point((int)stop2.getRect().x,(int)stop2.getRect().y));

		BusAgent bus = new BusAgent(new BusRoute(stops),0);
		BusGui busG = new BusGui(bus,cityPanel,bus.getRoute().getCurrentLocation().getParkingLocation().x,bus.getRoute().getCurrentLocation().getParkingLocation().y);
		bus.setGui(busG);
		cityPanel.addGui(busG);
		bus.startThread();
		transitPanel.addVehicle(bus);
		
		BusAgent bus1 = new BusAgent(new BusRoute(stops),1);
		BusGui busG1 = new BusGui(bus1,cityPanel,bus1.getRoute().getCurrentLocation().getParkingLocation().x,bus1.getRoute().getCurrentLocation().getParkingLocation().y);
		bus1.setGui(busG1);
		cityPanel.addGui(busG1);
		bus1.setDrunk(true);
		bus1.startThread();
		transitPanel.addVehicle(bus1);
	}

	private void joust()
	{
		CityDirectory.getInstance().setStartTime(new CityTime(8, 0));
		
		ArrayList<BusStop> stops = new ArrayList<BusStop>();

		BusStopAnimationPanel panel = new BusStopAnimationPanel(Structure.getNextInstance(),this);
		BusStop stop1;
		stops.add(stop1 = new BusStop(4*25,11*25,25,25,1, panel));
		timePanel.addAnimationPanel(panel);
		stop1.setStructurePanel(panel);
		panel.setStop(stop1);
		buildingPanels.add(""+stop1.getId(),panel);
		
		cityPanel.addStructure(stop1,new Point(3*25,11*25),new Point((int)stop1.getRect().x,(int)stop1.getRect().y));
		
		BusStopAnimationPanel panel2 = new BusStopAnimationPanel(Structure.getNextInstance(),this);
		BusStop stop2;
		stops.add(stop2 = new BusStop(25*25,6*25,25,25,2, panel2));
		timePanel.addAnimationPanel(panel2);
		stop2.setStructurePanel(panel2);
		panel2.setStop(stop2);
		buildingPanels.add(""+stop2.getId(),panel2);
		
		cityPanel.addStructure(stop2,new Point(26*25,6*25),new Point((int)stop2.getRect().x,(int)stop2.getRect().y));
		
		for(int i = 0; i < stops.size(); i++)
		{
			BusAgent bus = new BusAgent(new BusRoute(stops),i);
			BusGui busG = new BusGui(bus,cityPanel,bus.getRoute().getCurrentLocation().getParkingLocation().x,bus.getRoute().getCurrentLocation().getParkingLocation().y);
			bus.setGui(busG);
			cityPanel.addGui(busG);
			bus.startThread();
			transitPanel.addVehicle(bus);
		}
	}

	/**
	 * Clears SimCity201 to run a new scenario. This method is called by the scenario panel when the user wants to run a new scenario.
	 */
	public void clearScenario() {
		this.personPanel.resetCity();
		this.transitPanel.resetCity();
		this.marketPanel.resetCity();
		this.restaurantPanel.resetCity();
		this.residencePanel.resetCity();
		
		bottomSettingsPanel.resetCity();
		CityDirectory.getInstance().resetCity();
		Constants.ANIMATION_SPEED_FACTOR = 1.0f;
		cityPanel.resetCity();
		for(int i = 0; i < buildingPanels.getComponents().length; i++) {
			cardLayout.removeLayoutComponent(buildingPanels.getComponents()[i]);
		}
		buildingPanels.removeAll();
		JPanel blankPanel = new JPanel();
		buildingPanels.add(blankPanel, "blank");
		
		System.gc();
	}
	
	private void skylerRestaurant() {
		CityDirectory.getInstance().setStartTime(new CityTime(8, 0));
		
		RestaurantAnimationPanelSkyler g = new RestaurantAnimationPanelSkyler(Structure.getNextInstance(),this);
		timePanel.addAnimationPanel(g);
		RestaurantSkyler r = new RestaurantSkyler(125,125,50,50,Structure.getNextInstance(),g);
		restaurantPanel.addRestaurant(r);
		r.setConfigPanel(restaurantPanel);
		r.setStructurePanel(g);
		r.setClosingTime(new CityTime(13, 15));
		buildingPanels.add(g,""+r.getId());
		cityPanel.addStructure(r);
		CityDirectory.getInstance().addRestaurant(r);
		
		PersonAgent p1 = new PersonAgent("Host", cityPanel);
		p1.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantHost, r, null);
		p1.setHungerEnabled(false);
		p1.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p1);
		personPanel.addPerson(p1);
		p1.startThread();
		
		PersonAgent p2 = new PersonAgent("Cashier", cityPanel);
		p2.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCashier, r, null);
		p2.setHungerEnabled(false);
		p2.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p2);
		personPanel.addPerson(p2);
		p2.startThread();
		
		PersonAgent p3 = new PersonAgent("Cook", cityPanel);
		p3.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCook, r, null);
		p3.setHungerEnabled(false);
		p3.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p3);
		personPanel.addPerson(p3);
		p3.startThread();
		
		PersonAgent p4 = new PersonAgent("Waiter", cityPanel);
		p4.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantWaiter, r, null);
		p4.setHungerEnabled(false);
		p4.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p4);
		personPanel.addPerson(p4);
		p4.startThread();
		
		PersonAgent p5 = new PersonAgent("Customer", cityPanel);
		p5.setWakeupTime(new CityTime(8, 00));
		p5.setupPerson(CityDirectory.getInstance().getTime(), null, null, null, r, null);
		CityDirectory.getInstance().addPerson(p5);
		personPanel.addPerson(p5);
		p5.startThread();
	}
	
	private void skylerRestaurantTwo() {
		CityDirectory.getInstance().setStartTime(new CityTime(8, 0));
		
		RestaurantAnimationPanelSkyler g = new RestaurantAnimationPanelSkyler(Structure.getNextInstance(),this);
		timePanel.addAnimationPanel(g);
		RestaurantSkyler r = new RestaurantSkyler(125,125,50,50,Structure.getNextInstance(),g);
		restaurantPanel.addRestaurant(r);
		r.setConfigPanel(restaurantPanel);
		r.setStructurePanel(g);
		r.setClosingTime(new CityTime(13, 15));
		buildingPanels.add(g,""+r.getId());
		cityPanel.addStructure(r);
		CityDirectory.getInstance().addRestaurant(r);
		
		PersonAgent p1 = new PersonAgent("Host", cityPanel);
		p1.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantHost, r, null);
		p1.setHungerEnabled(false);
		p1.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p1);
		personPanel.addPerson(p1);
		p1.startThread();
		
		PersonAgent p2 = new PersonAgent("Cashier", cityPanel);
		p2.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCashier, r, null);
		p2.setHungerEnabled(false);
		p2.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p2);
		personPanel.addPerson(p2);
		p2.startThread();
		
		PersonAgent p3 = new PersonAgent("Cook", cityPanel);
		p3.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCook, r, null);
		p3.setHungerEnabled(false);
		p3.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p3);
		personPanel.addPerson(p3);
		p3.startThread();
		
		PersonAgent p4 = new PersonAgent("Waiter-1", cityPanel);
		p4.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantWaiter, r, null);
		p4.setHungerEnabled(false);
		p4.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p4);
		personPanel.addPerson(p4);
		p4.startThread();
		
		PersonAgent p5 = new PersonAgent("Customer-1", cityPanel);
		p5.setWakeupTime(new CityTime(8, 00));
		p5.setupPerson(CityDirectory.getInstance().getTime(), null, null, null, r, null);
		CityDirectory.getInstance().addPerson(p5);
		personPanel.addPerson(p5);
		p5.startThread();
		
		PersonAgent p6 = new PersonAgent("Customer-2", cityPanel);
		p6.setWakeupTime(new CityTime(8, 00));
		p6.setupPerson(CityDirectory.getInstance().getTime(), null, null, null, r, null);
		CityDirectory.getInstance().addPerson(p6);
		personPanel.addPerson(p6);
		p6.startThread();
		
		PersonAgent p8 = new PersonAgent("Waiter-2", cityPanel);
		p8.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantWaiter, r, null);
		p8.setHungerEnabled(false);
		p8.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p8);
		personPanel.addPerson(p8);
		p8.startThread();
	}
		
	private void weekendDifference() {
		/* Employees work at a Restaurant during the week, but workplaces are closed on the weekends 
		 * so people must eat at home or find other things to do. In this scenario, the time begins on 
		 * Friday and a Host wakes up at 7:00AM goes to work at the Restaurant normally at 8:00AM, but 
		 * on Saturday people sleep in instead so the Host wakes up at 8:30AM and doesn't go to work 
		 * at the Restaurant.
		 */
		CityTime time = new CityTime();
		time.day = CityTime.WeekDay.Friday;
		CityDirectory.getInstance().setStartTime(time);

		RestaurantAnimationPanelMatt g = new RestaurantAnimationPanelMatt(Structure.getNextInstance(),this);
		timePanel.addAnimationPanel(g);
		RestaurantMatt r = new RestaurantMatt(125, 125, 50, 50, Structure.getNextInstance(), g);
		restaurantPanel.addRestaurant(r);
		r.setConfigPanel(restaurantPanel);
		r.setStructurePanel(g);
		buildingPanels.add(g, "" + r.getId());
		cityPanel.addStructure(r);
		CityDirectory.getInstance().addRestaurant(r);
		
		ResidenceAnimationPanel resPanel = new ResidenceAnimationPanel(Structure.getNextInstance(), this);
		Residence res = new Residence(17*25, 11*25, 25, 25, Structure.getNextInstance(), resPanel, false);
		res.setStructurePanel(resPanel);
		buildingPanels.add(resPanel,""+res.getId());
		cityPanel.addStructure(res, new Point(15*25, 11*25), new Point(16*25, 11*25));
		CityDirectory.getInstance().addResidence(res);
		timePanel.addAnimationPanel(resPanel);
		
		PersonAgent p1 = new PersonAgent("Host", cityPanel);
		p1.setupPerson(CityDirectory.getInstance().getTime(), res, r, Intention.RestaurantHost, res, null);
		CityDirectory.getInstance().addPerson(p1);
		personPanel.addPerson(p1);
		p1.startThread();
	}
	
	private void normativeRestaurant() {
		/* A normal Waiter, Host, Cashier, and Cook all come to work at 8:00AM. The Restaurant opens
		 * when all of them have arrived at the Restaurant. At 8:00AM a single Customer wakes up 
		 * and a normative Restaurant scenario starts where he orders food and leaves when done when
		 * he sees that the Restaurant is open.
		 * The morning shift ends at 12:30PM and all the employees go home.
		 */
		CityDirectory.getInstance().setStartTime(new CityTime(8, 0));
		
		RestaurantAnimationPanelMatt g = new RestaurantAnimationPanelMatt(Structure.getNextInstance(),this);
		timePanel.addAnimationPanel(g);
		RestaurantMatt r = new RestaurantMatt(125,125,50,50,Structure.getNextInstance(),g);
		restaurantPanel.addRestaurant(r);
		r.setConfigPanel(restaurantPanel);
		r.setStructurePanel(g);
		r.setClosingTime(new CityTime(13, 15));
		buildingPanels.add(g,""+r.getId());
		cityPanel.addStructure(r);
		CityDirectory.getInstance().addRestaurant(r);
		
		PersonAgent p1 = new PersonAgent("Host", cityPanel);
		p1.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantHost, r, null);
		p1.setHungerEnabled(false);
		p1.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p1);
		personPanel.addPerson(p1);
		p1.startThread();
		
		PersonAgent p2 = new PersonAgent("Cashier", cityPanel);
		p2.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCashier, r, null);
		p2.setHungerEnabled(false);
		p2.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p2);
		personPanel.addPerson(p2);
		p2.startThread();
		
		PersonAgent p3 = new PersonAgent("Cook", cityPanel);
		p3.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCook, r, null);
		p3.setHungerEnabled(false);
		p3.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p3);
		personPanel.addPerson(p3);
		p3.startThread();
		
		PersonAgent p4 = new PersonAgent("Waiter", cityPanel);
		p4.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantWaiter, r, null);
		p4.setHungerEnabled(false);
		p4.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p4);
		personPanel.addPerson(p4);
		p4.startThread();
		
		PersonAgent p5 = new PersonAgent("Customer", cityPanel);
		p5.setWakeupTime(new CityTime(8, 00));
		p5.setupPerson(CityDirectory.getInstance().getTime(), null, null, null, r, null);
		CityDirectory.getInstance().addPerson(p5);
		personPanel.addPerson(p5);
		p5.startThread();
	}
	
	private void normativeRestaurantTwoCustomersTwoWaiters() {
		/* A normal Waiter and rotating stand Waiter, Host, Cashier, and Cook all come to work at 8:00AM. The Restaurant opens
		 * when the Host, Cashier, Cook, and at least one Waiter have arrived at the Restaurant. Two Customers wake up at 8:00AM
		 * and a normative Restaurant scenario starts where they both order food and leave when done when they see that the 
		 * Restaurant is open.
		 * The morning shift ends at 12:30PM (should be right after the Customers leave), and all the employees go home.
		 */
		CityDirectory.getInstance().setStartTime(new CityTime(8, 0));
		
		RestaurantAnimationPanelMatt g = new RestaurantAnimationPanelMatt(Structure.getNextInstance(),this);
		timePanel.addAnimationPanel(g);
		RestaurantMatt r = new RestaurantMatt(125,125,50,50,Structure.getNextInstance(),g);
		restaurantPanel.addRestaurant(r);
		r.setConfigPanel(restaurantPanel);
		r.setStructurePanel(g);
		r.setClosingTime(new CityTime(13, 15));
		buildingPanels.add(g,""+r.getId());
		cityPanel.addStructure(r);
		CityDirectory.getInstance().addRestaurant(r);
		
		PersonAgent p1 = new PersonAgent("Host", cityPanel);
		p1.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantHost, r, null);
		p1.setHungerEnabled(false);
		p1.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p1);
		personPanel.addPerson(p1);
		p1.startThread();
		
		PersonAgent p2 = new PersonAgent("Cashier", cityPanel);
		p2.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCashier, r, null);
		p2.setHungerEnabled(false);
		p2.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p2);
		personPanel.addPerson(p2);
		p2.startThread();
		
		PersonAgent p3 = new PersonAgent("Cook", cityPanel);
		p3.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCook, r, null);
		p3.setHungerEnabled(false);
		p3.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p3);
		personPanel.addPerson(p3);
		p3.startThread();
		
		PersonAgent p4 = new PersonAgent("Waiter 1", cityPanel);
		p4.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantWaiter, r, null);
		p4.setHungerEnabled(false);
		p4.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p4);
		personPanel.addPerson(p4);
		p4.startThread();
		
		PersonAgent p4b = new PersonAgent("Waiter 2", cityPanel);
		p4b.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantWaiter, r, null);
		p4b.setHungerEnabled(false);
		p4b.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p4b);
		personPanel.addPerson(p4b);
		p4b.startThread();
		
		PersonAgent p5 = new PersonAgent("Customer 1", cityPanel);
		p5.setWakeupTime(new CityTime(8, 00));
		p5.setupPerson(CityDirectory.getInstance().getTime(), null, null, null, r, null);
		CityDirectory.getInstance().addPerson(p5);
		personPanel.addPerson(p5);
		p5.startThread();
		
		PersonAgent p6 = new PersonAgent("Customer 2", cityPanel);
		p6.setWakeupTime(new CityTime(8, 00));
		p6.setupPerson(CityDirectory.getInstance().getTime(), null, null, null, r, null);
		CityDirectory.getInstance().addPerson(p6);
		personPanel.addPerson(p6);
		p6.startThread();
	}
	
	private void normativeApartmentComplex() {
		/* 
		 * Creates a Landlord who lives in a Residence that he owns and a Renter who lives in a Residence
		 * that he pays rent on. Every morning at 7am, the Renter will check if he has to pay any rent. The Landlord
		 * goes to work at 8am at the ApartmentComplex, where he checks if any of the Renters in his list of properties needs to pay rent.
		 * The Renter we've created has to pay his rent on Tuesdays, so the Landlord will notify him of his rent due
		 * on Monday. Otherwise, both will act as regular residents in their homes.
		 */
		ApartmentComplexAnimationPanel acap = new ApartmentComplexAnimationPanel(Structure.getNextInstance(),this);
		timePanel.addAnimationPanel(acap);
		ApartmentComplex ac = new ApartmentComplex(17*25, 11*25, 25, 25, Structure.getNextInstance(), acap);
		ac.setStructurePanel(acap);
		ac.setClosingTime(new CityTime(12, 0));
		buildingPanels.add(acap,""+ac.getId());
		cityPanel.addStructure(ac, new Point(17*25,9*25), new Point(17*25, 10*25));
		CityDirectory.getInstance().addApartment(ac);
		timePanel.addAnimationPanel(acap);
		
		ResidenceAnimationPanel resPanel = new ResidenceAnimationPanel(Structure.getNextInstance(), this);
		timePanel.addAnimationPanel(resPanel);
		Residence res = new Residence(17*25, 12*25, 25, 25, Structure.getNextInstance(), resPanel, true);
		res.setStructurePanel(resPanel);
		buildingPanels.add(resPanel,""+res.getId());
		cityPanel.addStructure(res, new Point(15*25, 12*25), new Point(16*25, 12*25));
		CityDirectory.getInstance().addResidence(res);
		
		ResidenceAnimationPanel resPanel2 = new ResidenceAnimationPanel(Structure.getNextInstance(), this);
		timePanel.addAnimationPanel(resPanel2);
		Residence res2 = new Residence(18*25, 11*25, 25, 25, Structure.getNextInstance(), resPanel2, false);
		res2.setStructurePanel(resPanel2);
		buildingPanels.add(resPanel2, ""+res2.getId());
		cityPanel.addStructure(res2, new Point(18*25, 9*25), new Point(19*25, 11*25));
		CityDirectory.getInstance().addResidence(res2);
		
		PersonAgent p1 = new PersonAgent("Renter",cityPanel);
		p1.setupPerson(CityDirectory.getInstance().getTime(), res, null, null, res, null);
		CityDirectory.getInstance().addPerson(p1);
		personPanel.addPerson(p1);
		
		PersonAgent p2 = new PersonAgent("Landlord",cityPanel);
		p2.setupPerson(CityDirectory.getInstance().getTime(), res2, ac, Intention.ResidenceLandLord, res2, null);
		CityDirectory.getInstance().addPerson(p2);
		personPanel.addPerson(p2);
		
		ac.addApartment(res);
		ac.getLandlord().addProperty(res, (Renter)res.getResident(), 30, WeekDay.Tuesday);
		res.setApartmentComplex(ac);
		
		p1.startThread();
		p2.startThread();
	}
	
	private void normativeResidence() {
		/*
		 * Creates a Residence and a Resident who inhabits that residence. Each morning at 7am, that Resident will
		 * eat from his refrigerator to start the day. If he has nothing else to do, he will relax at home until he
		 * gets hungry or has something else to do. At 10pm he will go to sleep in his bed.
		 */
		ResidenceAnimationPanel resPanel = new ResidenceAnimationPanel(Structure.getNextInstance(), this);
		timePanel.addAnimationPanel(resPanel);
		Residence res = new Residence(17*25, 11*25, 25, 25, Structure.getNextInstance(), resPanel, false);
		res.setStructurePanel(resPanel);
		buildingPanels.add(resPanel,""+res.getId());
		cityPanel.addStructure(res, new Point(15*25, 11*25), new Point(16*25, 11*25));
		CityDirectory.getInstance().addResidence(res);
		timePanel.addAnimationPanel(resPanel);
		
		PersonAgent p1 = new PersonAgent("Resident",cityPanel);
		p1.setupPerson(CityDirectory.getInstance().getTime(), res, null, null, res, null);
		p1.setSleepTime(new CityTime(11, 0));
		personPanel.addPerson(p1);
		CityDirectory.getInstance().addPerson(p1);
		
		p1.startThread();
		
		//p1.setWakeupTime(new CityTime(13,0)); //Need to change wakeup Time after a delay.
	}
	
	private void residenceOutOfFood(boolean b) {
		ResidenceAnimationPanel resPanel = new ResidenceAnimationPanel(Structure.getNextInstance(), this);
		timePanel.addAnimationPanel(resPanel);
		Residence res = new Residence(17*25, 11*25, 25, 25, Structure.getNextInstance(), resPanel, false);
		res.setStructurePanel(resPanel);
		buildingPanels.add(resPanel,""+res.getId());
		cityPanel.addStructure(res, new Point(15*25, 11*25), new Point(16*25, 11*25));
		CityDirectory.getInstance().addResidence(res);
		timePanel.addAnimationPanel(resPanel);
		int limit = 9;
		if (b) {
			limit++;
		}
		for (int i=0; i<limit; i++) { //bring all food amounts down to 1 in the fridge.
			res.removeFood("Steak");
			res.removeFood("Pasta");
			res.removeFood("Ice Cream");
			res.removeFood("Pizza");
			res.removeFood("Chicken");
			res.removeFood("Salad");
		}
		res.setConfigPanel(residencePanel);
		residencePanel.addResidenceStructure(res);
		
		MarketAnimationPanel mG = new MarketAnimationPanel(Structure.getNextInstance(),this,50,50);
		MarketStructure m = new MarketStructure(125,125,50,50,Structure.getNextInstance(),mG);
		m.setStructurePanel(mG);
		m.setClosingTime(new CityTime(18, 0));
		buildingPanels.add(mG,""+m.getId());
		cityPanel.addStructure(m);
		timePanel.addAnimationPanel(mG);
		m.setConfigPanel(marketPanel);
		marketPanel.addMarketStructure(m);
		
		TruckAgent truck = new TruckAgent(m);
		truck.startThread();
		m.addTruck(truck);
		CityDirectory.getInstance().addMarket(m);
		transitPanel.addVehicle(truck);
			
		PersonAgent p = new PersonAgent("Market Employee",cityPanel);
		p.setupPerson(CityDirectory.getInstance().getTime(), null, m, Intention.MarketEmployee, m, null);
		p.setHungerEnabled(false);
		p.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p);
		personPanel.addPerson(p);
		p.startThread();
		
		PersonAgent p2 = new PersonAgent("Market Manager",cityPanel);
		p2.setupPerson(CityDirectory.getInstance().getTime(), null, m, Intention.MarketManager, m, null);
		p2.setHungerEnabled(false);
		p2.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p2);
		personPanel.addPerson(p2);
		p2.startThread();
		
		PersonAgent p3 = new PersonAgent("Resident",cityPanel);
		p3.setupPerson(CityDirectory.getInstance().getTime(), res, null, null, res, null);
		p3.setSleepTime(new CityTime(20, 0));
		personPanel.addPerson(p3);
		CityDirectory.getInstance().addPerson(p3);
		
		p3.startThread();
	}
	
	private void normativeWalking()
	{
		/*
		 * Creates a Person who will walk from the market at 125,125 to the restaurant at 23*25,11*25 by way of crosswalks and sidewalks.
		 * The route that is taken is defined by arrows shown in Debug mode (viewed by typing zero). When the Person reaches his destination, he
		 * goes inside the structure there and performs structure actions.
		 * He begins his walk at 7:00 AM.
		 */
		CityDirectory.getInstance().setStartTime(new CityTime(8, 0));
		
		MarketAnimationPanel mG = new MarketAnimationPanel(Structure.getNextInstance(),this,50,50);
		MarketStructure m = new MarketStructure(125,125,50,50,Structure.getNextInstance(),mG);
		m.setConfigPanel(marketPanel);
		marketPanel.addMarketStructure(m);
		m.setStructurePanel(mG);
		m.setClosingTime(new CityTime(18, 0));
		buildingPanels.add(mG,""+m.getId());
		cityPanel.addStructure(m);
		CityDirectory.getInstance().addMarket(m);
		timePanel.addAnimationPanel(mG);
		
		RestaurantAnimationPanelMatt g = new RestaurantAnimationPanelMatt(Structure.getNextInstance(),this);
		RestaurantMatt r = new RestaurantMatt(23*25,11*25,50,50,Structure.getNextInstance(),g);
		r.setStructurePanel(g);
		r.setClosingTime(new CityTime(14, 0));
		buildingPanels.add(g,""+r.getId());
		cityPanel.addStructure(r,new Point(23*25,14*25), new Point(22*25,11*25));
		CityDirectory.getInstance().addRestaurant(r);
		timePanel.addAnimationPanel(g);
		r.setConfigPanel(restaurantPanel);
		restaurantPanel.addRestaurant(r);
		
		PersonAgent p1 = new PersonAgent("Walker",cityPanel);
		//p1.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.None, m, null);
		p1.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantHost, m, null);
		CityDirectory.getInstance().addPerson(p1);
		p1.startThread();
		personPanel.addPerson(p1);
	}
	
	private void normativeDriving()
	{
		/*
		 * Creates a Person who will drive from the market at 125,125 to the restaurant at 23*25,11*25 by way of roads.
		 * The person does this by calling a car, who comes to pick up the person. The person gets into the car, which then drives
		 * on a path determined by BFS on a movement map (visible in Debug mode), which takes him or her to the parking location
		 * of the building. The person then walks to the sidewalk location and is brought inside the building.
		 * This starts at 7:00 AM.
		 * He currently moves around the block due to the nature of the sidewalks and his movement priorities.
		 */
		CityDirectory.getInstance().setStartTime(new CityTime(7, 0));
		
		MarketAnimationPanel mG = new MarketAnimationPanel(Structure.getNextInstance(),this,50,50);
		MarketStructure m = new MarketStructure(125,125,50,50,Structure.getNextInstance(),mG);
		m.setConfigPanel(marketPanel);
		marketPanel.addMarketStructure(m);
		m.setStructurePanel(mG);
		m.setClosingTime(new CityTime(18, 0));
		buildingPanels.add(mG,""+m.getId());
		cityPanel.addStructure(m);
		CityDirectory.getInstance().addMarket(m);
		timePanel.addAnimationPanel(mG);
			
		RestaurantAnimationPanelMatt g = new RestaurantAnimationPanelMatt(Structure.getNextInstance(),this);
		RestaurantMatt r = new RestaurantMatt(23*25,11*25,50,50,Structure.getNextInstance(),g);
		restaurantPanel.addRestaurant(r);
		r.setConfigPanel(restaurantPanel);
		r.setStructurePanel(g);
		r.setClosingTime(new CityTime(14, 0));
		buildingPanels.add(g,""+r.getId());
		cityPanel.addStructure(r,new Point(26*25,11*25), new Point(25*25,11*25));
		CityDirectory.getInstance().addRestaurant(r);
		timePanel.addAnimationPanel(g);
	
		CarAgent car = new CarAgent();
		CarGui cGui = new CarGui(car,cityPanel);
		car.setGui(cGui);
		cityPanel.addGui(cGui);
		car.startThread();
		transitPanel.addVehicle(car);
	
		PersonAgent p1 = new PersonAgent("Car Rider",cityPanel);
		p1.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantHost, m, car);
		CityDirectory.getInstance().addPerson(p1);
		p1.startThread();
		personPanel.addPerson(p1);
	}
	
	private void normativeBus()
	{
		/*
		 * Creates a person, bus stops, and a bus. The bus will go through the stops on its route in sequential order and loops through these stops.
		 * When the person reaches the bus stop (determined by proximity to location and destination), the person adds himself to the bus stop's list of waiting customers.
		 * This is accessed by the bus when it arrives at the bus stop, and ends up taking the passenger around the circuit.
		 * At each bus stop, the person is told that a stop has been reached. When the bus gets to the proper stop, the person gets off and walks the rest of the way.
		 * The bus moves from the very beginning.
		 * The person moves at 7:00 AM
		 */
		CityDirectory.getInstance().setStartTime(new CityTime(8, 0));
		
		ArrayList<BusStop> stops = new ArrayList<BusStop>();

		BusStopAnimationPanel panel = new BusStopAnimationPanel(Structure.getNextInstance(),this);
		BusStop stop1;
		stops.add(stop1 = new BusStop(23*25,13*25,25,25,1, panel));
		timePanel.addAnimationPanel(panel);
		stop1.setStructurePanel(panel);
		panel.setStop(stop1);
		buildingPanels.add(""+stop1.getId(),panel);
		
		cityPanel.addStructure(stops.get(0),new Point(23*25,14*25),new Point((int)stops.get(0).getRect().x,(int)stops.get(0).getRect().y));
		
		BusStopAnimationPanel panel2 = new BusStopAnimationPanel(Structure.getNextInstance(),this);
		BusStop stop2;
		stops.add(stop2 = new BusStop(1*25,6*25,25,25,2, panel2));
		timePanel.addAnimationPanel(panel2);
		stop2.setStructurePanel(panel2);
		panel2.setStop(stop2);
		buildingPanels.add(""+stop2.getId(),panel2);
		
		cityPanel.addStructure(stops.get(1),new Point(2*25,6*25),new Point((int)stops.get(1).getRect().x,(int)stops.get(1).getRect().y));
		
		BusStopAnimationPanel panel3 = new BusStopAnimationPanel(Structure.getNextInstance(),this);
		BusStop stop3;
		stops.add(stop3 = new BusStop(24*25,4*25,25,25,2, panel3));
		timePanel.addAnimationPanel(panel3);
		stop3.setStructurePanel(panel3);
		panel3.setStop(stop3);
		buildingPanels.add(""+stop3.getId(),panel3);
		
		cityPanel.addStructure(stops.get(2),new Point(24*25,3*25),new Point((int)stops.get(2).getRect().x,(int)stops.get(2).getRect().y));
		
		BusStopAnimationPanel panel4 = new BusStopAnimationPanel(Structure.getNextInstance(),this);
		BusStop stop4;
		stops.add(stop4 = new BusStop(1*25,12*25,25,25,2, panel4));
		timePanel.addAnimationPanel(panel4);
		stop4.setStructurePanel(panel4);
		panel4.setStop(stop4);
		buildingPanels.add(""+stop4.getId(),panel4);
		
		cityPanel.addStructure(stops.get(3),new Point(2*25,12*25),new Point((int)stops.get(3).getRect().x,(int)stops.get(3).getRect().y));
		
		for(int i = 0; i < 1/*stops.size()*/; i++)
		{
			BusAgent bus = new BusAgent(new BusRoute(stops),i);
			BusGui busG = new BusGui(bus,cityPanel,bus.getRoute().getCurrentLocation().getParkingLocation().x,bus.getRoute().getCurrentLocation().getParkingLocation().y);
			bus.setGui(busG);
			cityPanel.addGui(busG);
			bus.startThread();
			transitPanel.addVehicle(bus);
		}
		
		MarketAnimationPanel mG = new MarketAnimationPanel(Structure.getNextInstance(),this,50,50);
		MarketStructure m = new MarketStructure(125,125,50,50,Structure.getNextInstance(),mG);
		m.setConfigPanel(marketPanel);
		marketPanel.addMarketStructure(m);
		m.setStructurePanel(mG);
		m.setClosingTime(new CityTime(18, 0));
		buildingPanels.add(mG,""+m.getId());
		cityPanel.addStructure(m);
		CityDirectory.getInstance().addMarket(m);
		timePanel.addAnimationPanel(mG);
		
		RestaurantAnimationPanelBrandon g = new RestaurantAnimationPanelBrandon(Structure.getNextInstance(),this);
		RestaurantBrandon r = new RestaurantBrandon(23*25,11*25,50,50,Structure.getNextInstance(),g);
		restaurantPanel.addRestaurant(r);
		r.setConfigPanel(restaurantPanel);
		r.setStructurePanel(g);
		r.setClosingTime(new CityTime(14, 0));
		buildingPanels.add(g,""+r.getId());
		cityPanel.addStructure(r,new Point(23*25,9*25), new Point(23*25,10*25));
		CityDirectory.getInstance().addRestaurant(r);
		timePanel.addAnimationPanel(g);

		PersonAgent p1 = new PersonAgent("Bus Rider",cityPanel);
		p1.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantHost, m, null);
		p1.getPassengerRole().setBusStops(stops);
		CityDirectory.getInstance().addPerson(p1);
		p1.startThread();
		personPanel.addPerson(p1);
	}
	
	private void normativeMarketRestaurantDelivery()
	{
		/* A Restaurant Cook and Cashier go to work at 8:00AM. The Restaurant is forced open for this scenario (normally
		 * it would be closed if only a Cook and Cashier were present). A Market Manager and Employee also go to work at
		 * 8:00AM. The cook's inventory is forced to 0 for Steak, so he orders 25 steaks from the market. The market
		 * employee gets the steaks from the shelves, gives them to the manager, who dispatches a delivery truck to bring
		 * the food back to the restaurant. The cashier checks to make sure the delivery matches an invoice he has from
		 * the cook. It matches, so he gives the cook the delivery and pays the market. The restaurant ends up with
		 * negative money which is okay. Eventually what will happen is the restaurant will have to cover for this by
		 * withdrawing from its bank account. If it doesn't have enough, it will take out a loan.
		 */
		CityDirectory.getInstance().setStartTime(new CityTime(8, 00));
		
		MarketAnimationPanel mG = new MarketAnimationPanel(Structure.getNextInstance(),this,50,50);
		timePanel.addAnimationPanel(mG);
		MarketStructure m = new MarketStructure(125,125,50,50,Structure.getNextInstance(),mG);
		m.setStructurePanel(mG);
		m.setClosingTime(new CityTime(18, 0));
		buildingPanels.add(mG,""+m.getId());
		cityPanel.addStructure(m);
		m.setConfigPanel(marketPanel);
		marketPanel.addMarketStructure(m);
		
		TruckAgent truck = new TruckAgent(m);
		truck.startThread();
		m.addTruck(truck);
		CityDirectory.getInstance().addMarket(m);
		transitPanel.addVehicle(truck);
		
		TruckAgent truck2 = new TruckAgent(m);
		truck2.startThread();
		m.addTruck(truck2);
		transitPanel.addVehicle(truck2);
		
		RestaurantAnimationPanelMatt g = new RestaurantAnimationPanelMatt(Structure.getNextInstance(),this);
		timePanel.addAnimationPanel(g);
		RestaurantMatt r = new RestaurantMatt(23*25,11*25,50,50,Structure.getNextInstance(),g);
		restaurantPanel.addRestaurant(r);
		r.setConfigPanel(restaurantPanel);
		r.setStructurePanel(g);
		r.setClosingTime(new CityTime(14, 0));
		buildingPanels.add(g,""+r.getId());
		cityPanel.addStructure(r,new Point(23*25,9*25), new Point(23*25,10*25));
		CityDirectory.getInstance().addRestaurant(r);
		
		PersonAgent p1 = new PersonAgent("Cook",cityPanel);
		p1.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCook, r, null);
		p1.setHungerEnabled(false);
		p1.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p1);
		personPanel.addPerson(p1);
		p1.startThread();
		
		PersonAgent p1b = new PersonAgent("Cashier",cityPanel);
		p1b.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCashier, r, null);
		p1b.setHungerEnabled(false);
		p1b.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p1b);
		personPanel.addPerson(p1b);
		p1b.startThread();
		
		PersonAgent p1c = new PersonAgent("Host",cityPanel);
		p1c.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantHost, r, null);
		p1c.setHungerEnabled(false);
		p1c.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p1c);
		personPanel.addPerson(p1c);
		p1c.startThread();
		
		PersonAgent p1d = new PersonAgent("Waiter",cityPanel);
		p1d.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantWaiter, r, null);
		p1d.setHungerEnabled(false);
		p1d.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p1d);
		personPanel.addPerson(p1d);
		p1d.startThread();
		
		PersonAgent p2 = new PersonAgent("Market Employee",cityPanel);
		p2.setupPerson(CityDirectory.getInstance().getTime(), null, m, Intention.MarketEmployee, m, null);
		p2.setHungerEnabled(false);
		p2.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p2);
		personPanel.addPerson(p2);
		p2.startThread();
		
		PersonAgent p3 = new PersonAgent("Market Manager",cityPanel);
		p3.setupPerson(CityDirectory.getInstance().getTime(), null, m, Intention.MarketManager, m, null);
		p3.setHungerEnabled(false);
		p3.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p3);
		personPanel.addPerson(p3);
		p3.startThread();
		
		((RestaurantCookRoleMatt) r.getCook()).emptySteakInventory();
	}
	
	private void normativeMarket()
	{
		/*
		 * A Market Manager, Market Employee, and Market Customer all go to a market at 8 AM when it opens.
		 * The market has a forced inventory of 10 Pizzas, 5 Burgers, and 15 Fritos, and the Customer orders two Burgers and a Pizza.
		 * The Market Manager conveys these orders to the Employee, who goes and pulls them off the shelf. The Employee then brings them to the front,
		 * where they are given through the Manager to the Customer. The Customer pays and leaves, and the Employee returns to the back of the Market.
		 */
		CityDirectory.getInstance().setStartTime(new CityTime(8, 00));
		
		MarketAnimationPanel mG = new MarketAnimationPanel(Structure.getNextInstance(),this,50,50);
		MarketStructure m = new MarketStructure(125,125,50,50,Structure.getNextInstance(),mG);
		m.setStructurePanel(mG);
		m.setClosingTime(new CityTime(18, 0));
		buildingPanels.add(mG,""+m.getId());
		cityPanel.addStructure(m);
		timePanel.addAnimationPanel(mG);
		m.setConfigPanel(marketPanel);
		marketPanel.addMarketStructure(m);
		
		MarketAnimationPanel mG2 = new MarketAnimationPanel(Structure.getNextInstance(),this,50,50);
		MarketStructure m2 = new MarketStructure(23*25,11*25,50,50,Structure.getNextInstance(),mG2);
		m2.setStructurePanel(mG2);
		m2.setClosingTime(new CityTime(18, 0));
		buildingPanels.add(mG2,""+m2.getId());
		cityPanel.addStructure(m2);
		timePanel.addAnimationPanel(mG2);
		m2.setConfigPanel(marketPanel);
		marketPanel.addMarketStructure(m2);
		
		m.getManager().addInventoryEntry(new InventoryEntry("Pizza",10,20));
		m.getManager().addInventoryEntry(new InventoryEntry("Burgers",5,10));
		m.getManager().addInventoryEntry(new InventoryEntry("Fritos",15,200));
		
		TruckAgent truck = new TruckAgent(m);
		truck.startThread();
		m.addTruck(truck);
		CityDirectory.getInstance().addMarket(m);
		transitPanel.addVehicle(truck);
			
		PersonAgent p = new PersonAgent("Market Employee",cityPanel);
		p.setupPerson(CityDirectory.getInstance().getTime(), null, m, Intention.MarketEmployee, m, null);
		p.setHungerEnabled(false);
		p.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p);
		personPanel.addPerson(p);
		p.startThread();
		
		PersonAgent p2 = new PersonAgent("Market Manager",cityPanel);
		p2.setupPerson(CityDirectory.getInstance().getTime(), null, m, Intention.MarketManager, m, null);
		p2.setHungerEnabled(false);
		p2.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p2);
		personPanel.addPerson(p2);
		p2.startThread();
		
		PersonAgent p3 = new PersonAgent("Market Customer",cityPanel);
		p3.setupPerson(CityDirectory.getInstance().getTime(), null, null, null, m, null);
		p3.setHungerEnabled(false);
		p3.getMarketChecklist().add(new ItemRequest("Burgers",2));
		p3.getMarketChecklist().add(new ItemRequest("Pizza",1));
		p3.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p3);
		personPanel.addPerson(p3);
		p3.startThread();
	}
	
	private void normativeRestaurantBen()
	{
		/* A normal Waiter, Host, Cashier, and Cook all come to work at 8:00AM. Ben's Restaurant opens
		 * when all of them have arrived at the Restaurant. At 8:30AM a single Customer comes
		 * and a normative Restaurant scenario starts where he orders food and leaves when done.
		 * The Restaurant closes at 1:15PM (should be right after the Customer leaves), and all
		 * the employees go home.
		 */
		CityDirectory.getInstance().setStartTime(new CityTime(8, 0));
		
		RestaurantAnimationPanelBen g = new RestaurantAnimationPanelBen(Structure.getNextInstance(), this, 0, 0);
		RestaurantBen r = new RestaurantBen(125, 125, 50, 50, Structure.getNextInstance(), g);
		restaurantPanel.addRestaurant(r);
		r.setConfigPanel(restaurantPanel);
		r.setStructurePanel(g);
		r.setClosingTime(new CityTime(13, 15));
		buildingPanels.add(g, ""+r.getId());
		cityPanel.addStructure(r);
		CityDirectory.getInstance().addRestaurant(r);
		timePanel.addAnimationPanel(g);
		
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
		
		PersonAgent p5 = new PersonAgent("Customer", cityPanel);
		p5.setWakeupTime(new CityTime(8, 30));
		p5.setupPerson(CityDirectory.getInstance().getTime(), null, null, null, r, null);
		CityDirectory.getInstance().addPerson(p5);
		p5.startThread();
	}
	
	private void normativeRestaurantBenTwoOfEach() {
		/* A normal Waiter and rotating stand Waiter, Host, Cashier, and Cook all come to work at 8:00AM. Ben's Restaurant opens
		 * when the Host, Cashier, Cook, and at least one Waiter have arrived at the Restaurant. At 8:30AM two Customers come
		 * and a normative Restaurant scenario starts where they both order food and leave when done.
		 * The Restaurant closes at 1:15PM (should be right after the Customers leave), and all the
		 * employees go home.
		 */
		CityDirectory.getInstance().setStartTime(new CityTime(8, 0));
		
		RestaurantAnimationPanelBen g = new RestaurantAnimationPanelBen(Structure.getNextInstance(), this, 0, 0);
		RestaurantBen r = new RestaurantBen(125, 125, 50, 50, Structure.getNextInstance(), g);
		restaurantPanel.addRestaurant(r);
		r.setConfigPanel(restaurantPanel);
		r.setStructurePanel(g);
		r.setClosingTime(new CityTime(13, 15));
		buildingPanels.add(g, ""+r.getId());
		cityPanel.addStructure(r);
		CityDirectory.getInstance().addRestaurant(r);
		timePanel.addAnimationPanel(g);
		
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
		
		PersonAgent p4 = new PersonAgent("Waiter 1", cityPanel);
		p4.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantWaiter, r, null);
		p4.setHungerEnabled(false);
		p4.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p4);
		p4.startThread();
		
		PersonAgent p4b = new PersonAgent("Waiter 2", cityPanel);
		p4b.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantWaiter, r, null);
		p4b.setHungerEnabled(false);
		p4b.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p4b);
		p4b.startThread();
		
		PersonAgent p5 = new PersonAgent("Customer 1", cityPanel);
		p5.setWakeupTime(new CityTime(8, 30));
		p5.setupPerson(CityDirectory.getInstance().getTime(), null, null, null, r, null);
		CityDirectory.getInstance().addPerson(p5);
		p5.startThread();
		
		PersonAgent p6 = new PersonAgent("Customer 2", cityPanel);
		p6.setWakeupTime(new CityTime(8, 30));
		p6.setupPerson(CityDirectory.getInstance().getTime(), null, null, null, r, null);
		CityDirectory.getInstance().addPerson(p6);
		p6.startThread();
	}
	
	private void marketConsumerCar()
	{
		/*
		 * A Market Manager, Market Employee, and Market Customer all go to a market at 8 AM when it opens.
		 * The Customer enters the market and wants to purchase a car.
		 * The Market Manager conveys this to the employee, who brings a new car out from the lot.
		 * The Customer pays and leaves, and the Employee returns to the back of the Market.
		 */
		CityDirectory.getInstance().setStartTime(new CityTime(8, 00));
		
		MarketAnimationPanel mG = new MarketAnimationPanel(Structure.getNextInstance(),this,50,50);
		MarketStructure m = new MarketStructure(125,125,50,50,Structure.getNextInstance(),mG);
		m.setStructurePanel(mG);
		m.setClosingTime(new CityTime(18, 0));
		buildingPanels.add(mG,""+m.getId());
		cityPanel.addStructure(m);
		timePanel.addAnimationPanel(mG);
		m.setConfigPanel(marketPanel);
		marketPanel.addMarketStructure(m);
		
		m.getManager().addInventoryEntry(new InventoryEntry("Pizza",10,20));
		m.getManager().addInventoryEntry(new InventoryEntry("Burgers",5,10));
		m.getManager().addInventoryEntry(new InventoryEntry("Fritos",15,200));
		
		TruckAgent truck = new TruckAgent(m);
		truck.startThread();
		m.addTruck(truck);
		CityDirectory.getInstance().addMarket(m);
		transitPanel.addVehicle(truck);
			
		PersonAgent p = new PersonAgent("Market Employee",cityPanel);
		p.setupPerson(CityDirectory.getInstance().getTime(), null, m, Intention.MarketEmployee, m, null);
		p.setHungerEnabled(false);
		p.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p);
		p.startThread();
		personPanel.addPerson(p);
		
		PersonAgent employee2 = new PersonAgent("Market Employee",cityPanel);
		employee2.setupPerson(CityDirectory.getInstance().getTime(), null, m, Intention.MarketEmployee, m, null);
		employee2.setHungerEnabled(false);
		employee2.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(employee2);
		employee2.startThread();
		personPanel.addPerson(employee2);
		
		PersonAgent p2 = new PersonAgent("Market Manager",cityPanel);
		p2.setupPerson(CityDirectory.getInstance().getTime(), null, m, Intention.MarketManager, m, null);
		p2.setHungerEnabled(false);
		p2.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p2);
		p2.startThread();
		personPanel.addPerson(p2);
		
		PersonAgent p3 = new PersonAgent("Market Customer Car",cityPanel);
		p3.setupPerson(CityDirectory.getInstance().getTime(), null, m, null, m, null);
		p3.setHungerEnabled(false);
		p3.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p3);
		p3.startThread();
		personPanel.addPerson(p3);
		
		PersonAgent p4 = new PersonAgent("Market Customer Goods",cityPanel);
		p4.setupPerson(CityDirectory.getInstance().getTime(), null, m, null, m, null);
		p4.setHungerEnabled(false);
		p4.getMarketChecklist().add(new ItemRequest("Burgers",2));
		p4.getMarketChecklist().add(new ItemRequest("Pizza",1));
		p4.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p4);
		p4.startThread();
		personPanel.addPerson(p4);
	}
	
	private void normativeMarketRestaurantBenDelivery()
	{
		/* A Restaurant Cook and Cashier go to work at 8:00AM. A Market Manager and Employee also go to work at 8:00AM.
		 * The cook's inventory is forced to 0 for Steak, so he orders 25 steaks from the market. The market employee 
		 * gets the steaks from the shelves, gives them to the manager, who dispatches a delivery truck to bring the 
		 * food back to the restaurant. The cashier checks to make sure the delivery matches an invoice he has from the 
		 * cook. It matches, so he gives the cook the delivery and pays the market. The restaurant ends up with negative
		 * money which is okay. Eventually what will happen is the restaurant will have to cover for this by withdrawing
		 * from its bank account. If it doesn't have enough, it will take out a loan.
		 */
		CityDirectory.getInstance().setStartTime(new CityTime(8, 00));
		
		MarketAnimationPanel mG = new MarketAnimationPanel(Structure.getNextInstance(),this,50,50);
		MarketStructure m = new MarketStructure(125,125,50,50,Structure.getNextInstance(),mG);
		m.setStructurePanel(mG);
		m.setClosingTime(new CityTime(18, 0));
		buildingPanels.add(mG,""+m.getId());
		cityPanel.addStructure(m);
		timePanel.addAnimationPanel(mG);
		m.setConfigPanel(marketPanel);
		marketPanel.addMarketStructure(m);
		
		TruckAgent truck = new TruckAgent(m);
		truck.startThread();
		m.addTruck(truck);
		CityDirectory.getInstance().addMarket(m);
		transitPanel.addVehicle(truck);
		
		RestaurantAnimationPanelBen g = new RestaurantAnimationPanelBen(Structure.getNextInstance(), this, 0, 0);
		RestaurantBen r = new RestaurantBen(23*25, 11*25, 50, 50, Structure.getNextInstance(), g);
		restaurantPanel.addRestaurant(r);
		r.setConfigPanel(restaurantPanel);
		r.setStructurePanel(g);
		r.setClosingTime(new CityTime(14, 00));
		buildingPanels.add(g, ""+r.getId());
		cityPanel.addStructure(r,new Point(23*25,9*25), new Point(23*25,10*25));
		CityDirectory.getInstance().addRestaurant(r);
		timePanel.addAnimationPanel(g);
		
		PersonAgent p1 = new PersonAgent("Cook",cityPanel);
		p1.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCook, r, null);
		p1.setHungerEnabled(false);
		p1.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p1);
		p1.startThread();
		
		PersonAgent p1b = new PersonAgent("Cashier",cityPanel);
		p1b.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCashier, r, null);
		p1b.setHungerEnabled(false);
		p1b.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p1b);
		p1b.startThread();
		
		PersonAgent p1c = new PersonAgent("Host",cityPanel);
		p1c.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantHost, r, null);
		p1c.setHungerEnabled(false);
		p1c.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p1c);
		p1c.startThread();
		
		PersonAgent p1d = new PersonAgent("Waiter",cityPanel);
		p1d.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantWaiter, r, null);
		p1d.setHungerEnabled(false);
		p1d.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p1d);
		p1d.startThread();
		
		PersonAgent p2 = new PersonAgent("Market Employee",cityPanel);
		p2.setupPerson(CityDirectory.getInstance().getTime(), null, m, Intention.MarketEmployee, m, null);
		p2.setHungerEnabled(false);
		p2.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p2);
		p2.startThread();
		
		PersonAgent p3 = new PersonAgent("Market Manager",cityPanel);
		p3.setupPerson(CityDirectory.getInstance().getTime(), null, m, Intention.MarketManager, m, null);
		p3.setHungerEnabled(false);
		p3.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p3);
		p3.startThread();
		
	}
	
	private void normativeMarketRestaurantSkylerDelivery() {
CityDirectory.getInstance().setStartTime(new CityTime(8, 00));
		
		MarketAnimationPanel mG = new MarketAnimationPanel(Structure.getNextInstance(),this,50,50);
		MarketStructure m = new MarketStructure(125,125,50,50,Structure.getNextInstance(),mG);
		m.setStructurePanel(mG);
		m.setClosingTime(new CityTime(18, 0));
		buildingPanels.add(mG,""+m.getId());
		cityPanel.addStructure(m);
		timePanel.addAnimationPanel(mG);
		m.setConfigPanel(marketPanel);
		marketPanel.addMarketStructure(m);
		
		TruckAgent truck = new TruckAgent(m);
		truck.startThread();
		m.addTruck(truck);
		CityDirectory.getInstance().addMarket(m);
		transitPanel.addVehicle(truck);
		
		RestaurantAnimationPanelSkyler g = new RestaurantAnimationPanelSkyler(Structure.getNextInstance(), this);
		RestaurantSkyler r = new RestaurantSkyler(23*25, 11*25, 50, 50, Structure.getNextInstance(), g);
		restaurantPanel.addRestaurant(r);
		r.setConfigPanel(restaurantPanel);
		r.setStructurePanel(g);
		r.setClosingTime(new CityTime(14, 00));
		((RestaurantCookRoleSkyler) r.getCook()).clearInventory();
		buildingPanels.add(g, ""+r.getId());
		cityPanel.addStructure(r,new Point(23*25,9*25), new Point(23*25,10*25));
		CityDirectory.getInstance().addRestaurant(r);
		timePanel.addAnimationPanel(g);
		
		PersonAgent p1 = new PersonAgent("Cook",cityPanel);
		p1.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCook, r, null);
		p1.setHungerEnabled(false);
		p1.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p1);
		p1.startThread();
		
		PersonAgent p1b = new PersonAgent("Cashier",cityPanel);
		p1b.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCashier, r, null);
		p1b.setHungerEnabled(false);
		p1b.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p1b);
		p1b.startThread();
		
		PersonAgent p1c = new PersonAgent("Host",cityPanel);
		p1c.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantHost, r, null);
		p1c.setHungerEnabled(false);
		p1c.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p1c);
		p1c.startThread();
		
		PersonAgent p1d = new PersonAgent("Waiter",cityPanel);
		p1d.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantWaiter, r, null);
		p1d.setHungerEnabled(false);
		p1d.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p1d);
		p1d.startThread();
		
		PersonAgent p2 = new PersonAgent("Market Employee",cityPanel);
		p2.setupPerson(CityDirectory.getInstance().getTime(), null, m, Intention.MarketEmployee, m, null);
		p2.setHungerEnabled(false);
		p2.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p2);
		p2.startThread();
		
		PersonAgent p3 = new PersonAgent("Market Manager",cityPanel);
		p3.setupPerson(CityDirectory.getInstance().getTime(), null, m, Intention.MarketManager, m, null);
		p3.setHungerEnabled(false);
		p3.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p3);
		p3.startThread();
	}
	
	private void restaurantShiftChange() {
		/* This scenario simply tests that shifts work as intended at RestaurantMatt. The morning and afternoon 
		 * shifts have a different set of employees working them. The first shift is from 8:00AM - 12:30PM. At 
		 * the end of the shift, everybody gets off work and the restaurant closes until the next shift starts. 
		 * The afternoon shift is from 1:00PM - 6:00PM. The Restaurant closes again after the afternoon shift is 
		 * done, until the next day, etc..
		 */
		CityDirectory.getInstance().setStartTime(new CityTime(7, 0));
		
		RestaurantAnimationPanelMatt g = new RestaurantAnimationPanelMatt(Structure.getNextInstance(),this);
		timePanel.addAnimationPanel(g);
		RestaurantMatt r = new RestaurantMatt(125,125,50,50,Structure.getNextInstance(),g);
		restaurantPanel.addRestaurant(r);
		r.setConfigPanel(restaurantPanel);
		r.setStructurePanel(g);
		buildingPanels.add(g,""+r.getId());
		cityPanel.addStructure(r);
		CityDirectory.getInstance().addRestaurant(r);
		timePanel.addAnimationPanel(g);
		
		PersonAgent p1 = new PersonAgent("Host AM", cityPanel);
		p1.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantHost, r, null);
		p1.setWorkTime(r.getMorningShiftStart());
		p1.setHungerEnabled(false);
		p1.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p1);
		personPanel.addPerson(p1);
		p1.startThread();
		
		PersonAgent p2 = new PersonAgent("Cashier AM", cityPanel);
		p2.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCashier, r, null);
		p2.setWorkTime(r.getMorningShiftStart());
		p2.setHungerEnabled(false);
		p2.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p2);
		personPanel.addPerson(p2);
		p2.startThread();
		
		PersonAgent p3 = new PersonAgent("Cook AM", cityPanel);
		p3.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCook, r, null);
		p3.setWorkTime(r.getMorningShiftStart());
		p3.setHungerEnabled(false);
		p3.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p3);
		personPanel.addPerson(p3);
		p3.startThread();
		
		PersonAgent p4 = new PersonAgent("Waiter AM", cityPanel);
		p4.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantWaiter, r, null);
		p4.setWorkTime(r.getMorningShiftStart());
		p4.setHungerEnabled(false);
		p4.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p4);
		personPanel.addPerson(p4);
		p4.startThread();
		
		PersonAgent p5 = new PersonAgent("Customer", cityPanel);
		p5.setWakeupTime(new CityTime(8, 00));
		p5.setupPerson(CityDirectory.getInstance().getTime(), null, null, null, r, null);
		CityDirectory.getInstance().addPerson(p5);
		personPanel.addPerson(p5);
		p5.startThread();
		
		PersonAgent pp1 = new PersonAgent("Host PM", cityPanel);
		pp1.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantHost, r, null);
		pp1.setWorkTime(r.getAfternoonShiftStart());
		pp1.setHungerEnabled(false);
		pp1.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(pp1);
		personPanel.addPerson(pp1);
		pp1.startThread();
		
		PersonAgent pp2 = new PersonAgent("Cashier PM", cityPanel);
		pp2.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCashier, r, null);
		pp2.setWorkTime(r.getAfternoonShiftStart());
		pp2.setHungerEnabled(false);
		pp2.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(pp2);
		personPanel.addPerson(pp2);
		pp2.startThread();
		
		PersonAgent pp3 = new PersonAgent("Cook PM", cityPanel);
		pp3.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCook, r, null);
		pp3.setWorkTime(r.getAfternoonShiftStart());
		pp3.setHungerEnabled(false);
		pp3.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(pp3);
		personPanel.addPerson(pp3);
		pp3.startThread();
		
		PersonAgent pp4 = new PersonAgent("Waiter PM", cityPanel);
		pp4.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantWaiter, r, null);
		pp4.setWorkTime(r.getAfternoonShiftStart());
		pp4.setHungerEnabled(false);
		pp4.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(pp4);
		personPanel.addPerson(pp4);
		pp4.startThread();
		
	}
	
	private void benRestaurantShiftChange() {
		CityDirectory.getInstance().setStartTime(new CityTime(7, 0));
		
		RestaurantAnimationPanelBen g = new RestaurantAnimationPanelBen(Structure.getNextInstance(),this, 0, 0);
		RestaurantBen r = new RestaurantBen(125,125,50,50,Structure.getNextInstance(),g);
		restaurantPanel.addRestaurant(r);
		r.setConfigPanel(restaurantPanel);
		r.setStructurePanel(g);
		buildingPanels.add(g,""+r.getId());
		cityPanel.addStructure(r);
		CityDirectory.getInstance().addRestaurant(r);
		timePanel.addAnimationPanel(g);
		
		PersonAgent p1 = new PersonAgent("Host AM", cityPanel);
		p1.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantHost, r, null);
		p1.setWorkTime(r.getMorningShiftStart());
		p1.setHungerEnabled(false);
		p1.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p1);
		p1.startThread();
		
		PersonAgent p2 = new PersonAgent("Cashier AM", cityPanel);
		p2.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCashier, r, null);
		p2.setWorkTime(r.getMorningShiftStart());
		p2.setHungerEnabled(false);
		p2.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p2);
		p2.startThread();
		
		PersonAgent p3 = new PersonAgent("Cook AM", cityPanel);
		p3.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCook, r, null);
		p3.setWorkTime(r.getMorningShiftStart());
		p3.setHungerEnabled(false);
		p3.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p3);
		p3.startThread();
		
		PersonAgent p4 = new PersonAgent("Waiter AM", cityPanel);
		p4.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantWaiter, r, null);
		p4.setWorkTime(r.getMorningShiftStart());
		p4.setHungerEnabled(false);
		p4.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p4);
		p4.startThread();
		
		PersonAgent p5 = new PersonAgent("Customer", cityPanel);
		p5.setWakeupTime(new CityTime(8, 00));
		p5.setupPerson(CityDirectory.getInstance().getTime(), null, null, null, r, null);
		CityDirectory.getInstance().addPerson(p5);
		p5.startThread();
		
		PersonAgent pp1 = new PersonAgent("Host PM", cityPanel);
		pp1.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantHost, r, null);
		pp1.setWorkTime(r.getAfternoonShiftStart());
		pp1.setHungerEnabled(false);
		pp1.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(pp1);
		pp1.startThread();
		
		PersonAgent pp2 = new PersonAgent("Cashier PM", cityPanel);
		pp2.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCashier, r, null);
		pp2.setWorkTime(r.getAfternoonShiftStart());
		pp2.setHungerEnabled(false);
		pp2.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(pp2);
		pp2.startThread();
		
		PersonAgent pp3 = new PersonAgent("Cook PM", cityPanel);
		pp3.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCook, r, null);
		pp3.setWorkTime(r.getAfternoonShiftStart());
		pp3.setHungerEnabled(false);
		pp3.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(pp3);
		pp3.startThread();
		
		PersonAgent pp4 = new PersonAgent("Waiter PM", cityPanel);
		pp4.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantWaiter, r, null);
		pp4.setWorkTime(r.getAfternoonShiftStart());
		pp4.setHungerEnabled(false);
		pp4.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(pp4);
		pp4.startThread();
		
	}
	
	private void brandonRestaurantShiftChange() {
		/*
		 * 
		 * 
		 */
		CityDirectory.getInstance().setStartTime(new CityTime(7, 0));
		
		RestaurantAnimationPanelBrandon g = new RestaurantAnimationPanelBrandon(Structure.getNextInstance(),this);
		RestaurantBrandon r = new RestaurantBrandon(125,125,50,50,Structure.getNextInstance(),g);
		restaurantPanel.addRestaurant(r);
		r.setConfigPanel(restaurantPanel);
		r.setStructurePanel(g);
		buildingPanels.add(g,""+r.getId());
		cityPanel.addStructure(r);
		CityDirectory.getInstance().addRestaurant(r);
		timePanel.addAnimationPanel(g);
		
		PersonAgent p1 = new PersonAgent("Host AM", cityPanel);
		p1.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantHost, r, null);
		p1.setWorkTime(r.getMorningShiftStart());
		p1.setHungerEnabled(false);
		p1.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p1);
		p1.startThread();
		personPanel.addPerson(p1);
		
		PersonAgent p2 = new PersonAgent("Cashier AM", cityPanel);
		p2.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCashier, r, null);
		p2.setWorkTime(r.getMorningShiftStart());
		p2.setHungerEnabled(false);
		p2.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p2);
		p2.startThread();
		personPanel.addPerson(p2);
		
		PersonAgent p3 = new PersonAgent("Cook AM", cityPanel);
		p3.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCook, r, null);
		p3.setWorkTime(r.getMorningShiftStart());
		p3.setHungerEnabled(false);
		p3.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p3);
		p3.startThread();
		personPanel.addPerson(p3);
		
		PersonAgent p4 = new PersonAgent("Waiter AM", cityPanel);
		p4.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantWaiter, r, null);
		p4.setWorkTime(r.getMorningShiftStart());
		p4.setHungerEnabled(false);
		p4.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p4);
		p4.startThread();
		personPanel.addPerson(p4);
		
		PersonAgent p5 = new PersonAgent("Customer", cityPanel);
		p5.setWakeupTime(new CityTime(8, 00));
		p5.setupPerson(CityDirectory.getInstance().getTime(), null, null, null, r, null);
		CityDirectory.getInstance().addPerson(p5);
		p5.startThread();
		personPanel.addPerson(p5);
		
		PersonAgent pp1 = new PersonAgent("Host PM", cityPanel);
		pp1.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantHost, r, null);
		pp1.setWorkTime(r.getAfternoonShiftStart());
		pp1.setHungerEnabled(false);
		pp1.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(pp1);
		pp1.startThread();
		personPanel.addPerson(pp1);
		
		PersonAgent pp2 = new PersonAgent("Cashier PM", cityPanel);
		pp2.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCashier, r, null);
		pp2.setWorkTime(r.getAfternoonShiftStart());
		pp2.setHungerEnabled(false);
		pp2.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(pp2);
		pp2.startThread();
		personPanel.addPerson(pp2);
		
		PersonAgent pp3 = new PersonAgent("Cook PM", cityPanel);
		pp3.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCook, r, null);
		pp3.setWorkTime(r.getAfternoonShiftStart());
		pp3.setHungerEnabled(false);
		pp3.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(pp3);
		pp3.startThread();
		personPanel.addPerson(pp3);
		
		PersonAgent pp4 = new PersonAgent("Waiter PM", cityPanel);
		pp4.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantWaiter, r, null);
		pp4.setWorkTime(r.getAfternoonShiftStart());
		pp4.setHungerEnabled(false);
		pp4.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(pp4);
		pp4.startThread();
		personPanel.addPerson(pp4);
	}
	
	private void marketShiftChange() {
		CityDirectory.getInstance().setStartTime(new CityTime(8, 00));
		
		MarketAnimationPanel mG = new MarketAnimationPanel(Structure.getNextInstance(),this,50,50);
		MarketStructure m = new MarketStructure(125,125,50,50,Structure.getNextInstance(),mG);
		m.setStructurePanel(mG);
		m.setClosingTime(new CityTime(18, 0));
		buildingPanels.add(mG,""+m.getId());
		cityPanel.addStructure(m);
		timePanel.addAnimationPanel(mG);
		m.setConfigPanel(marketPanel);
		marketPanel.addMarketStructure(m);
		
		m.getManager().addInventoryEntry(new InventoryEntry("Pizza",10,20));
		m.getManager().addInventoryEntry(new InventoryEntry("Burgers",5,10));
		m.getManager().addInventoryEntry(new InventoryEntry("Fritos",15,200));
		
		TruckAgent truck = new TruckAgent(m);
		truck.startThread();
		m.addTruck(truck);
		CityDirectory.getInstance().addMarket(m);
		transitPanel.addVehicle(truck);
			
		PersonAgent p = new PersonAgent("Market Employee AM",cityPanel);
		p.setupPerson(CityDirectory.getInstance().getTime(), null, m, Intention.MarketEmployee, m, null);
		p.setHungerEnabled(false);
		p.setHungerLevel(0);
		p.setWorkTime(m.getMorningShiftStart());
		CityDirectory.getInstance().addPerson(p);
		personPanel.addPerson(p);
		p.startThread();
		
		PersonAgent pPM = new PersonAgent("Market Employee PM",cityPanel);
		pPM.setupPerson(CityDirectory.getInstance().getTime(), null, m, Intention.MarketEmployee, m, null);
		pPM.setHungerEnabled(false);
		pPM.setHungerLevel(0);
		pPM.setWorkTime(m.getAfternoonShiftStart());
		CityDirectory.getInstance().addPerson(pPM);
		personPanel.addPerson(pPM);
		pPM.startThread();
		
		PersonAgent p2 = new PersonAgent("Market Manager AM",cityPanel);
		p2.setupPerson(CityDirectory.getInstance().getTime(), null, m, Intention.MarketManager, m, null);
		p2.setHungerEnabled(false);
		p2.setHungerLevel(0);
		p2.setWorkTime(m.getMorningShiftStart());
		CityDirectory.getInstance().addPerson(p2);
		personPanel.addPerson(p2);
		p2.startThread();
		
		PersonAgent p2PM = new PersonAgent("Market Manager PM",cityPanel);
		p2PM.setupPerson(CityDirectory.getInstance().getTime(), null, m, Intention.MarketManager, m, null);
		p2PM.setHungerEnabled(false);
		p2PM.setHungerLevel(0);
		p2PM.setWorkTime(m.getAfternoonShiftStart());
		CityDirectory.getInstance().addPerson(p2PM);
		personPanel.addPerson(p2PM);
		p2PM.startThread();
		
		PersonAgent p3 = new PersonAgent("Market Customer",cityPanel);
		p3.setupPerson(CityDirectory.getInstance().getTime(), null, null, null, m, null);
		p3.setHungerEnabled(false);
		p3.getMarketChecklist().add(new ItemRequest("Burgers",2));
		p3.getMarketChecklist().add(new ItemRequest("Pizza",1));
		p3.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p3);
		personPanel.addPerson(p3);
		p3.startThread();
	}
	
	private void hundredPeople() {
		RestaurantAnimationPanelMatt g = new RestaurantAnimationPanelMatt(Structure.getNextInstance(),this);
		RestaurantMatt r = new RestaurantMatt(125,125,50,50,Structure.getNextInstance(),g);
		settingsPanel.addPanel("Restaurants",new ConfigPanel());
		r.setStructurePanel(g);
		buildingPanels.add(g,""+r.getId());
		cityPanel.addStructure(r);
		CityDirectory.getInstance().addRestaurant(r);
		timePanel.addAnimationPanel(g);
		r.setConfigPanel(restaurantPanel);
		restaurantPanel.addRestaurant(r);
		
		for (int i = 1; i <= 100; i++)
		{
			createPerson(i + "", null, null, null, null, null);
		}
	}
	
	private void hundredPeopleBus()
	{
		for (int i = 1; i <= 100; i++) {
			createPerson(i + "", null, null, null, null, null);
		}
		
		ArrayList<BusStop> stops = new ArrayList<BusStop>();
		
		BusStopAnimationPanel panel = new BusStopAnimationPanel(Structure.getNextInstance(),this);
		stops.add(new BusStop(5*25,1*25,25,25,1, panel));
		timePanel.addAnimationPanel(panel);
			
		BusStopAnimationPanel panel2 = new BusStopAnimationPanel(Structure.getNextInstance(),this);
		stops.add(new BusStop(12*25,1*25,25,25,2, panel2));
		timePanel.addAnimationPanel(panel2);
			
		BusStopAnimationPanel panel3 = new BusStopAnimationPanel(Structure.getNextInstance(),this);
		stops.add(new BusStop(24*25,1*25,25,25,3, panel3));
		timePanel.addAnimationPanel(panel3);
			
		BusStopAnimationPanel panel4 = new BusStopAnimationPanel(Structure.getNextInstance(),this);
		stops.add(new BusStop(5*25,13*25,25,25,4, panel4));
		timePanel.addAnimationPanel(panel4);
			
		BusStopAnimationPanel panel5 = new BusStopAnimationPanel(Structure.getNextInstance(),this);
		stops.add(new BusStop(12*25,13*25,25,25,5, panel5));
		timePanel.addAnimationPanel(panel5);
			
		BusStopAnimationPanel panel6 = new BusStopAnimationPanel(Structure.getNextInstance(),this);
		stops.add(new BusStop(24*25,13*25,25,25,6, panel6));
		timePanel.addAnimationPanel(panel6);
		
		panel.setStop(stops.get(0));
		panel2.setStop(stops.get(1));
		panel3.setStop(stops.get(2));
		panel4.setStop(stops.get(3));
		panel5.setStop(stops.get(4));
		panel6.setStop(stops.get(5));
		
		stops.get(0).setStructurePanel(panel);
		stops.get(1).setStructurePanel(panel2);
		stops.get(2).setStructurePanel(panel3);
		stops.get(3).setStructurePanel(panel4);
		stops.get(4).setStructurePanel(panel5);
		stops.get(5).setStructurePanel(panel6);
		
		buildingPanels.add(panel,""+stops.get(0).getId());
		buildingPanels.add(panel2,""+stops.get(1).getId());
		buildingPanels.add(panel3,""+stops.get(2).getId());
		buildingPanels.add(panel4,""+stops.get(3).getId());
		buildingPanels.add(panel5,""+stops.get(4).getId());
		buildingPanels.add(panel6,""+stops.get(5).getId());
		
		for(BusStop stop : stops)
		{
			cityPanel.addStructure(stop,new Point((int)stop.getRect().x,((int)stop.getRect().y==25?2*25:14*25)),new Point((int)stop.getRect().x,(int)stop.getRect().y));
		}
		
		for(int i = 0; i < stops.size(); i++)
		{
			BusAgent bus = new BusAgent(new BusRoute(stops),i);
			BusGui busG = new BusGui(bus,cityPanel,bus.getRoute().getCurrentLocation().getParkingLocation().x,bus.getRoute().getCurrentLocation().getParkingLocation().y);
			bus.setGui(busG);
			cityPanel.addGui(busG);
			bus.startThread();
			transitPanel.addVehicle(bus);
		}
	}
	
	private void hundredPeopleBusDrunk()
	{
		for (int i = 1; i <= 100; i++) {
			createPerson(i + "", null, null, null, null, null);
		}
		
		ArrayList<BusStop> stops = new ArrayList<BusStop>();
		
		BusStopAnimationPanel panel = new BusStopAnimationPanel(Structure.getNextInstance(),this);
		stops.add(new BusStop(5*25,1*25,25,25,1, panel));
		timePanel.addAnimationPanel(panel);
			
		BusStopAnimationPanel panel2 = new BusStopAnimationPanel(Structure.getNextInstance(),this);
		stops.add(new BusStop(12*25,1*25,25,25,2, panel2));
		timePanel.addAnimationPanel(panel2);
			
		BusStopAnimationPanel panel3 = new BusStopAnimationPanel(Structure.getNextInstance(),this);
		stops.add(new BusStop(24*25,1*25,25,25,3, panel3));
		timePanel.addAnimationPanel(panel3);
			
		BusStopAnimationPanel panel4 = new BusStopAnimationPanel(Structure.getNextInstance(),this);
		stops.add(new BusStop(5*25,13*25,25,25,4, panel4));
		timePanel.addAnimationPanel(panel4);
			
		BusStopAnimationPanel panel5 = new BusStopAnimationPanel(Structure.getNextInstance(),this);
		stops.add(new BusStop(12*25,13*25,25,25,5, panel5));
		timePanel.addAnimationPanel(panel5);
			
		BusStopAnimationPanel panel6 = new BusStopAnimationPanel(Structure.getNextInstance(),this);
		stops.add(new BusStop(24*25,13*25,25,25,6, panel6));
		timePanel.addAnimationPanel(panel6);
		
		panel.setStop(stops.get(0));
		panel2.setStop(stops.get(1));
		panel3.setStop(stops.get(2));
		panel4.setStop(stops.get(3));
		panel5.setStop(stops.get(4));
		panel6.setStop(stops.get(5));
		
		stops.get(0).setStructurePanel(panel);
		stops.get(1).setStructurePanel(panel2);
		stops.get(2).setStructurePanel(panel3);
		stops.get(3).setStructurePanel(panel4);
		stops.get(4).setStructurePanel(panel5);
		stops.get(5).setStructurePanel(panel6);
		
		buildingPanels.add(panel,""+stops.get(0).getId());
		buildingPanels.add(panel2,""+stops.get(1).getId());
		buildingPanels.add(panel3,""+stops.get(2).getId());
		buildingPanels.add(panel4,""+stops.get(3).getId());
		buildingPanels.add(panel5,""+stops.get(4).getId());
		buildingPanels.add(panel6,""+stops.get(5).getId());
		
		for(BusStop stop : stops)
		{
			cityPanel.addStructure(stop,new Point((int)stop.getRect().x,((int)stop.getRect().y==25?2*25:14*25)),new Point((int)stop.getRect().x,(int)stop.getRect().y));
		}
		
		/*for(int i = 0; i < stops.size(); i++)
		{
			BusAgent bus = new BusAgent(new BusRoute(stops),i);
			BusGui busG = new BusGui(bus,cityPanel,bus.getRoute().getCurrentLocation().getParkingLocation().x,bus.getRoute().getCurrentLocation().getParkingLocation().y);
			bus.setGui(busG);
			cityPanel.addGui(busG);
			if(i == 0)
			{
				busG.setDrunk(true);
			}
			bus.startThread();
			transitPanel.addVehicle(bus);
		}*/
		
		BusAgent bus = new BusAgent(new BusRoute(stops),0);
		BusGui busG = new BusGui(bus,cityPanel,bus.getRoute().getCurrentLocation().getParkingLocation().x,bus.getRoute().getCurrentLocation().getParkingLocation().y);
		bus.setGui(busG);
		cityPanel.addGui(busG);
		busG.setDrunk(true);
		bus.startThread();
		transitPanel.addVehicle(bus);
		
		CityDirectory.getInstance().setStartTime(new CityTime(6,45));
	}

	private void brandonRestaurant() {
		CityDirectory.getInstance().setStartTime(new CityTime(8, 0));
		
		RestaurantAnimationPanelBrandon g = new RestaurantAnimationPanelBrandon(Structure.getNextInstance(),this);
		RestaurantBrandon r = new RestaurantBrandon(125,125,50,50,Structure.getNextInstance(),g);
		restaurantPanel.addRestaurant(r);
		r.setConfigPanel(restaurantPanel);
		r.setStructurePanel(g);
		r.setClosingTime(new CityTime(13, 15));
		buildingPanels.add(g,""+r.getId());
		cityPanel.addStructure(r);
		CityDirectory.getInstance().addRestaurant(r);
		timePanel.addAnimationPanel(g);
		
		PersonAgent p1 = new PersonAgent("Host", cityPanel);
		p1.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantHost, r, null);
		p1.setHungerEnabled(false);
		p1.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p1);
		personPanel.addPerson(p1);
		p1.startThread();
		
		PersonAgent p2 = new PersonAgent("Cashier", cityPanel);
		p2.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCashier, r, null);
		p2.setHungerEnabled(false);
		p2.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p2);
		personPanel.addPerson(p2);
		p2.startThread();
		
		PersonAgent p3 = new PersonAgent("Cook", cityPanel);
		p3.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCook, r, null);
		p3.setHungerEnabled(false);
		p3.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p3);
		personPanel.addPerson(p3);
		p3.startThread();
		
		PersonAgent p4 = new PersonAgent("Waiter", cityPanel);
		p4.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantWaiter, r, null);
		p4.setHungerEnabled(false);
		p4.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p4);
		personPanel.addPerson(p4);
		p4.startThread();
		
		PersonAgent p5 = new PersonAgent("Customer", cityPanel);
		p5.setWakeupTime(new CityTime(8, 00));
		p5.setupPerson(CityDirectory.getInstance().getTime(), null, null, null, null, null);
		CityDirectory.getInstance().addPerson(p5);
		personPanel.addPerson(p5);
		p5.startThread();
	}

	private void brandonRestaurantTwoCustomersTwoWaiters()
	{
		CityDirectory.getInstance().setStartTime(new CityTime(8, 0));
		
		RestaurantAnimationPanelBrandon g = new RestaurantAnimationPanelBrandon(Structure.getNextInstance(),this);
		RestaurantBrandon r = new RestaurantBrandon(125,125,50,50,Structure.getNextInstance(),g);
		restaurantPanel.addRestaurant(r);
		r.setConfigPanel(restaurantPanel);
		r.setStructurePanel(g);
		r.setClosingTime(new CityTime(13, 15));
		buildingPanels.add(g,""+r.getId());
		cityPanel.addStructure(r);
		CityDirectory.getInstance().addRestaurant(r);
		timePanel.addAnimationPanel(g);
		
		PersonAgent p1 = new PersonAgent("Host", cityPanel);
		p1.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantHost, r, null);
		p1.setHungerEnabled(false);
		p1.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p1);
		personPanel.addPerson(p1);
		p1.startThread();
		
		PersonAgent p2 = new PersonAgent("Cashier", cityPanel);
		p2.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCashier, r, null);
		p2.setHungerEnabled(false);
		p2.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p2);
		personPanel.addPerson(p2);
		p2.startThread();
		
		PersonAgent p3 = new PersonAgent("Cook", cityPanel);
		p3.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCook, r, null);
		p3.setHungerEnabled(false);
		p3.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p3);
		personPanel.addPerson(p3);
		p3.startThread();
		
		PersonAgent p4 = new PersonAgent("Waiter 1", cityPanel);
		p4.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantWaiter, r, null);
		p4.setHungerEnabled(false);
		p4.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p4);
		personPanel.addPerson(p4);
		p4.startThread();
		
		PersonAgent p4b = new PersonAgent("Waiter 2", cityPanel);
		p4b.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantWaiter, r, null);
		p4b.setHungerEnabled(false);
		p4b.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p4b);
		personPanel.addPerson(p4b);
		p4b.startThread();
		
		PersonAgent p5 = new PersonAgent("Customer 1", cityPanel);
		p5.setWakeupTime(new CityTime(8, 30));
		p5.setupPerson(CityDirectory.getInstance().getTime(), null, null, null, r, null);
		CityDirectory.getInstance().addPerson(p5);
		personPanel.addPerson(p5);
		p5.startThread();
		
		PersonAgent p6 = new PersonAgent("Customer 2", cityPanel);
		p6.setWakeupTime(new CityTime(8, 30));
		p6.setupPerson(CityDirectory.getInstance().getTime(), null, null, null, r, null);
		CityDirectory.getInstance().addPerson(p6);
		personPanel.addPerson(p6);
		p6.startThread();
	}
	
	private void brandonRestaurantMarketOrder()
	{
		/* A Restaurant Cook and Cashier go to work at 8:00AM. The Restaurant is forced open for this scenario (normally
		 * it would be closed if only a Cook and Cashier were present). A Market Manager and Employee also go to work at
		 * 8:00AM. The cook's inventory is forced to 0 for Steak, so he orders 25 steaks from the market. The market
		 * employee gets the steaks from the shelves, gives them to the manager, who dispatches a delivery truck to bring
		 * the food back to the restaurant. The cashier checks to make sure the delivery matches an invoice he has from
		 * the cook. It matches, so he gives the cook the delivery and pays the market. The restaurant ends up with
		 * negative money which is okay. Eventually what will happen is the restaurant will have to cover for this by
		 * withdrawing from its bank account. If it doesn't have enough, it will take out a loan.
		 */
		CityDirectory.getInstance().setStartTime(new CityTime(8, 00));
		
		MarketAnimationPanel mG = new MarketAnimationPanel(Structure.getNextInstance(),this,50,50);
		timePanel.addAnimationPanel(mG);
		MarketStructure m = new MarketStructure(125,125,50,50,Structure.getNextInstance(),mG);
		m.setStructurePanel(mG);
		m.setClosingTime(new CityTime(18, 0));
		buildingPanels.add(mG,""+m.getId());
		cityPanel.addStructure(m);
		m.setConfigPanel(marketPanel);
		marketPanel.addMarketStructure(m);
		
		TruckAgent truck = new TruckAgent(m);
		truck.startThread();
		m.addTruck(truck);
		CityDirectory.getInstance().addMarket(m);
		transitPanel.addVehicle(truck);
		
		TruckAgent truck2 = new TruckAgent(m);
		truck2.startThread();
		m.addTruck(truck2);
		transitPanel.addVehicle(truck2);
		
		RestaurantAnimationPanelBrandon g = new RestaurantAnimationPanelBrandon(Structure.getNextInstance(),this);
		timePanel.addAnimationPanel(g);
		RestaurantBrandon r = new RestaurantBrandon(23*25,11*25,50,50,Structure.getNextInstance(),g);
		restaurantPanel.addRestaurant(r);
		r.setConfigPanel(restaurantPanel);
		r.setStructurePanel(g);
		r.setClosingTime(new CityTime(14, 0));
		buildingPanels.add(g,""+r.getId());
		cityPanel.addStructure(r,new Point(23*25,9*25), new Point(23*25,10*25));
		CityDirectory.getInstance().addRestaurant(r);
		
		((RestaurantCookRoleBrandon) r.getCook()).emptySomeFood();
		
		PersonAgent p1 = new PersonAgent("Cook",cityPanel);
		p1.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCook, r, null);
		p1.setHungerEnabled(false);
		p1.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p1);
		personPanel.addPerson(p1);
		p1.startThread();
		
		PersonAgent p1b = new PersonAgent("Cashier",cityPanel);
		p1b.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCashier, r, null);
		p1b.setHungerEnabled(false);
		p1b.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p1b);
		personPanel.addPerson(p1b);
		p1b.startThread();
		
		PersonAgent p1c = new PersonAgent("Host",cityPanel);
		p1c.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantHost, r, null);
		p1c.setHungerEnabled(false);
		p1c.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p1c);
		personPanel.addPerson(p1c);
		p1c.startThread();
		
		PersonAgent p1d = new PersonAgent("Waiter",cityPanel);
		p1d.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantWaiter, r, null);
		p1d.setHungerEnabled(false);
		p1d.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p1d);
		personPanel.addPerson(p1d);
		p1d.startThread();
		
		PersonAgent p2 = new PersonAgent("Market Employee",cityPanel);
		p2.setupPerson(CityDirectory.getInstance().getTime(), null, m, Intention.MarketEmployee, m, null);
		p2.setHungerEnabled(false);
		p2.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p2);
		personPanel.addPerson(p2);
		p2.startThread();
		
		PersonAgent p3 = new PersonAgent("Market Manager",cityPanel);
		p3.setupPerson(CityDirectory.getInstance().getTime(), null, m, Intention.MarketManager, m, null);
		p3.setHungerEnabled(false);
		p3.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p3);
		personPanel.addPerson(p3);
		p3.startThread();
	}
	
	private void beaucoupBuses()
	{
		CityDirectory.getInstance().setStartTime(new CityTime(7, 0));
		
		ArrayList<BusStop> stops = new ArrayList<BusStop>();
	
		BusStopAnimationPanel panel = new BusStopAnimationPanel(Structure.getNextInstance(),this);
		stops.add(new BusStop(5*25,1*25,25,25,1, panel));
		timePanel.addAnimationPanel(panel);
			
		BusStopAnimationPanel panel2 = new BusStopAnimationPanel(Structure.getNextInstance(),this);
		stops.add(new BusStop(12*25,1*25,25,25,2, panel2));
		timePanel.addAnimationPanel(panel2);
			
		BusStopAnimationPanel panel3 = new BusStopAnimationPanel(Structure.getNextInstance(),this);
		stops.add(new BusStop(24*25,1*25,25,25,3, panel3));
		timePanel.addAnimationPanel(panel3);
			
		BusStopAnimationPanel panel4 = new BusStopAnimationPanel(Structure.getNextInstance(),this);
		stops.add(new BusStop(5*25,13*25,25,25,4, panel4));
		timePanel.addAnimationPanel(panel4);
			
		BusStopAnimationPanel panel5 = new BusStopAnimationPanel(Structure.getNextInstance(),this);
		stops.add(new BusStop(12*25,13*25,25,25,5, panel5));
		timePanel.addAnimationPanel(panel5);
			
		BusStopAnimationPanel panel6 = new BusStopAnimationPanel(Structure.getNextInstance(),this);
		stops.add(new BusStop(24*25,13*25,25,25,6, panel6));
		timePanel.addAnimationPanel(panel6);
		
		panel.setStop(stops.get(0));
		panel2.setStop(stops.get(1));
		panel3.setStop(stops.get(2));
		panel4.setStop(stops.get(3));
		panel5.setStop(stops.get(4));
		panel6.setStop(stops.get(5));
		
		stops.get(0).setStructurePanel(panel);
		stops.get(1).setStructurePanel(panel2);
		stops.get(2).setStructurePanel(panel3);
		stops.get(3).setStructurePanel(panel4);
		stops.get(4).setStructurePanel(panel5);
		stops.get(5).setStructurePanel(panel6);
		
		buildingPanels.add(panel,""+stops.get(0).getId());
		buildingPanels.add(panel2,""+stops.get(1).getId());
		buildingPanels.add(panel3,""+stops.get(2).getId());
		buildingPanels.add(panel4,""+stops.get(3).getId());
		buildingPanels.add(panel5,""+stops.get(4).getId());
		buildingPanels.add(panel6,""+stops.get(5).getId());
	
		for(BusStop stop : stops)
		{
			cityPanel.addStructure(stop,new Point((int)stop.getRect().x,((int)stop.getRect().y==25?2*25:14*25)),new Point((int)stop.getRect().x,(int)stop.getRect().y));
		}
		
		for(int i = 0; i < stops.size(); i++)
		{
			BusAgent bus = new BusAgent(new BusRoute(stops),i);
			BusGui busG = new BusGui(bus,cityPanel,bus.getRoute().getCurrentLocation().getParkingLocation().x,bus.getRoute().getCurrentLocation().getParkingLocation().y);
			bus.setGui(busG);
			cityPanel.addGui(busG);
			bus.startThread();
			transitPanel.addVehicle(bus);
		}
	}
	
	private void failedMarketDeliveryTruck()
	{
		/* A Restaurant Cook and Cashier go to work at 8:00AM. The Restaurant is forced open for this scenario (normally
		 * it would be closed if only a Cook and Cashier were present). A Market Manager and Employee also go to work at
		 * 8:00AM. The cook's inventory is forced to 0 for Steak, so he orders 25 steaks from the market. The market
		 * employee gets the steaks from the shelves, gives them to the manager, who dispatches a delivery truck to bring
		 * the food back to the restaurant. The cashier checks to make sure the delivery matches an invoice he has from
		 * the cook. It matches, so he gives the cook the delivery and pays the market. The restaurant ends up with
		 * negative money which is okay. Eventually what will happen is the restaurant will have to cover for this by
		 * withdrawing from its bank account. If it doesn't have enough, it will take out a loan.
		 */
		CityDirectory.getInstance().setStartTime(new CityTime(8, 00));
		
		MarketAnimationPanel mG = new MarketAnimationPanel(Structure.getNextInstance(),this,50,50);
		timePanel.addAnimationPanel(mG);
		MarketStructure m = new MarketStructure(125,125,50,50,Structure.getNextInstance(),mG);
		m.setStructurePanel(mG);
		m.setClosingTime(new CityTime(18, 0));
		buildingPanels.add(mG,""+m.getId());
		cityPanel.addStructure(m);
		m.setConfigPanel(marketPanel);
		marketPanel.addMarketStructure(m);
		
		TruckAgent truck = new TruckAgent(m);
		truck.startThread();
		m.addTruck(truck);
		CityDirectory.getInstance().addMarket(m);
		transitPanel.addVehicle(truck);
		
		TruckAgent truck2 = new TruckAgent(m);
		truck.startThread();
		m.addTruck(truck2);
		transitPanel.addVehicle(truck2);
		
		RestaurantAnimationPanelMatt g = new RestaurantAnimationPanelMatt(Structure.getNextInstance(),this);
		timePanel.addAnimationPanel(g);
		RestaurantMatt r = new RestaurantMatt(23*25,11*25,50,50,Structure.getNextInstance(),g);
		restaurantPanel.addRestaurant(r);
		r.setConfigPanel(restaurantPanel);
		r.setStructurePanel(g);
		r.setClosingTime(new CityTime(14, 0));
		r.setOpen(true);
		buildingPanels.add(g,""+r.getId());
		cityPanel.addStructure(r,new Point(23*25,9*25), new Point(23*25,10*25));
		CityDirectory.getInstance().addRestaurant(r);
		
		PersonAgent p1 = new PersonAgent("Cook",cityPanel);
		p1.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCook, r, null);
		p1.setHungerEnabled(false);
		p1.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p1);
		personPanel.addPerson(p1);
		p1.startThread();
		
		PersonAgent p1b = new PersonAgent("Cashier",cityPanel);
		p1b.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCashier, r, null);
		p1b.setHungerEnabled(false);
		p1b.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p1b);
		personPanel.addPerson(p1b);
		p1b.startThread();
		
		PersonAgent p2 = new PersonAgent("Market Employee",cityPanel);
		p2.setupPerson(CityDirectory.getInstance().getTime(), null, m, Intention.MarketEmployee, m, null);
		p2.setHungerEnabled(false);
		p2.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p2);
		personPanel.addPerson(p2);
		p2.startThread();
		
		PersonAgent p3 = new PersonAgent("Market Manager",cityPanel);
		p3.setupPerson(CityDirectory.getInstance().getTime(), null, m, Intention.MarketManager, m, null);
		p3.setHungerEnabled(false);
		p3.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p3);
		personPanel.addPerson(p3);
		p3.startThread();
		
		((RestaurantCookRoleMatt) r.getCook()).emptySteakInventory();
	}
	
	private void allWorkplaces() {
		CityDirectory.getInstance().setStartTime(new CityTime(8, 0));
		
		// APARTMENT COMPLEX 1
		ApartmentComplexAnimationPanel acap = new ApartmentComplexAnimationPanel(Structure.getNextInstance(),this);
		timePanel.addAnimationPanel(acap);
		ApartmentComplex ac = new ApartmentComplex(5*25, 11*25, 25, 25, Structure.getNextInstance(), acap);
		ac.setStructurePanel(acap);
		ac.setClosingTime(new CityTime(12, 0));
		buildingPanels.add(acap,""+ac.getId());
		cityPanel.addStructure(ac, new Point(3*25,11*25), new Point(4*25, 11*25));
		CityDirectory.getInstance().addApartment(ac);
		timePanel.addAnimationPanel(acap);
		//BUILDING1
		ResidenceAnimationPanel resPanel1 = new ResidenceAnimationPanel(Structure.getNextInstance(), this);
		timePanel.addAnimationPanel(resPanel1);
		Residence res1 = new Residence(6*25, 11*25, 25, 25, Structure.getNextInstance(), resPanel1, true);
		res1.setStructurePanel(resPanel1);
		buildingPanels.add(resPanel1,""+res1.getId());
		cityPanel.addStructure(res1, new Point(6*25, 9*25), new Point(6*25, 10*25));
		CityDirectory.getInstance().addResidence(res1);
		//BUILDING2
		ResidenceAnimationPanel resPanel2 = new ResidenceAnimationPanel(Structure.getNextInstance(), this);
		timePanel.addAnimationPanel(resPanel2);
		Residence res2 = new Residence(5*25, 12*25, 25, 25, Structure.getNextInstance(), resPanel2, true);
		res2.setStructurePanel(resPanel2);
		buildingPanels.add(resPanel2, ""+res2.getId());
		cityPanel.addStructure(res2, new Point(3*25, 12*25), new Point(4*25, 12*25));
		CityDirectory.getInstance().addResidence(res2);
		//BUILDING3
		ResidenceAnimationPanel resPanel3 = new ResidenceAnimationPanel(Structure.getNextInstance(), this);
		timePanel.addAnimationPanel(resPanel3);
		Residence res3 = new Residence(6*25, 12*25, 25, 25, Structure.getNextInstance(), resPanel3, true);
		res3.setStructurePanel(resPanel3);
		buildingPanels.add(resPanel3,""+res3.getId());
		cityPanel.addStructure(res3, new Point(6*25, 14*25), new Point(6*25, 13*25));
		CityDirectory.getInstance().addResidence(res3);
		//ADDBUILDINGS
		ac.addApartment(res1);
		ac.getLandlord().addProperty(res1, (Renter)res1.getResident(), 30, WeekDay.Wednesday);
		res1.setApartmentComplex(ac);
		ac.addApartment(res2);
		ac.getLandlord().addProperty(res2, (Renter)res2.getResident(), 20, WeekDay.Monday);
		res2.setApartmentComplex(ac);
		ac.addApartment(res3);
		ac.getLandlord().addProperty(res3, (Renter)res3.getResident(), 45, WeekDay.Friday);
		res3.setApartmentComplex(ac);
		
		// APARTMENT COMPLEX 2
		ApartmentComplexAnimationPanel acap2 = new ApartmentComplexAnimationPanel(Structure.getNextInstance(),this);
		timePanel.addAnimationPanel(acap2);
		ApartmentComplex ac2 = new ApartmentComplex(17*25, 11*25, 25, 25, Structure.getNextInstance(), acap2);
		ac2.setStructurePanel(acap2);
		ac2.setClosingTime(new CityTime(12, 0));
		buildingPanels.add(acap2,""+ac2.getId());
		cityPanel.addStructure(ac2, new Point(15*25,11*25), new Point(16*25, 11*25));
		CityDirectory.getInstance().addApartment(ac2);
		timePanel.addAnimationPanel(acap2);
		//BUILDING1
		ResidenceAnimationPanel resPanel1b = new ResidenceAnimationPanel(Structure.getNextInstance(), this);
		timePanel.addAnimationPanel(resPanel1b);
		Residence res1b = new Residence(18*25, 11*25, 25, 25, Structure.getNextInstance(), resPanel1b, true);
		res1b.setStructurePanel(resPanel1b);
		buildingPanels.add(resPanel1b,""+res1b.getId());
		cityPanel.addStructure(res1b, new Point(18*25, 9*25), new Point(18*25, 10*25));
		CityDirectory.getInstance().addResidence(res1b);
		//BUILDING2
		ResidenceAnimationPanel resPanel2c = new ResidenceAnimationPanel(Structure.getNextInstance(), this);
		timePanel.addAnimationPanel(resPanel2c);
		Residence res2c = new Residence(17*25, 12*25, 25, 25, Structure.getNextInstance(), resPanel2c, true);
		res2c.setStructurePanel(resPanel2c);
		buildingPanels.add(resPanel2c, ""+res2c.getId());
		cityPanel.addStructure(res2c, new Point(15*25, 12*25), new Point(16*25, 12*25));
		CityDirectory.getInstance().addResidence(res2c);
		//BUILDING3
		ResidenceAnimationPanel resPanel3d = new ResidenceAnimationPanel(Structure.getNextInstance(), this);
		timePanel.addAnimationPanel(resPanel3d);
		Residence res3d = new Residence(18*25, 12*25, 25, 25, Structure.getNextInstance(), resPanel3d, true);
		res3d.setStructurePanel(resPanel3d);
		buildingPanels.add(resPanel3d,""+res3d.getId());
		cityPanel.addStructure(res3d, new Point(18*25, 14*25), new Point(18*25, 13*25));
		CityDirectory.getInstance().addResidence(res3d);
		//ADDBUILDINGS
		ac2.addApartment(res1b);
		ac2.getLandlord().addProperty(res1b, (Renter)res1b.getResident(), 30, WeekDay.Wednesday);
		res1b.setApartmentComplex(ac2);
		ac2.addApartment(res2c);
		ac2.getLandlord().addProperty(res2c, (Renter)res2c.getResident(), 20, WeekDay.Monday);
		res2c.setApartmentComplex(ac2);
		ac2.addApartment(res3d);
		ac2.getLandlord().addProperty(res3d, (Renter)res3d.getResident(), 45, WeekDay.Friday);
		res3d.setApartmentComplex(ac2);
		
		// Two Markets
		MarketAnimationPanel mG = new MarketAnimationPanel(Structure.getNextInstance(),this,50,50);
		MarketStructure m = new MarketStructure(125,125,50,50,Structure.getNextInstance(),mG);
		m.setStructurePanel(mG);
		m.setClosingTime(new CityTime(18, 0));
		buildingPanels.add(mG,""+m.getId());
		cityPanel.addStructure(m);
		timePanel.addAnimationPanel(mG);
		m.setConfigPanel(marketPanel);
		marketPanel.addMarketStructure(m);
		
		MarketAnimationPanel mG2 = new MarketAnimationPanel(Structure.getNextInstance(),this,50,50);
		MarketStructure m2 = new MarketStructure(200,125,50,50,Structure.getNextInstance(),mG2);
		m2.setStructurePanel(mG2);
		m2.setClosingTime(new CityTime(18, 0));
		buildingPanels.add(mG2,""+m2.getId());
		cityPanel.addStructure(m2);
		timePanel.addAnimationPanel(mG2);
		m2.setConfigPanel(marketPanel);
		marketPanel.addMarketStructure(m2);
		
		// Ben's Restaurant		
		RestaurantAnimationPanelBen g = new RestaurantAnimationPanelBen(Structure.getNextInstance(), this, 0, 0);
		RestaurantBen r = new RestaurantBen(275, 125, 50, 50, Structure.getNextInstance(), g);
		restaurantPanel.addRestaurant(r);
		r.setConfigPanel(restaurantPanel);
		r.setStructurePanel(g);
		r.setClosingTime(new CityTime(13, 15));
		buildingPanels.add(g, ""+r.getId());
		cityPanel.addStructure(r);
		CityDirectory.getInstance().addRestaurant(r);
		timePanel.addAnimationPanel(g);
		
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

		// Matt's Restaurant
		RestaurantAnimationPanelMatt g_matt = new RestaurantAnimationPanelMatt(Structure.getNextInstance(),this);
		timePanel.addAnimationPanel(g_matt);
		RestaurantMatt r_matt = new RestaurantMatt(275 + 150,125,50,50,Structure.getNextInstance(),g_matt);
		restaurantPanel.addRestaurant(r_matt);
		r_matt.setConfigPanel(restaurantPanel);
		r_matt.setStructurePanel(g_matt);
		r_matt.setClosingTime(new CityTime(13, 15));
		buildingPanels.add(g_matt,""+r_matt.getId());
		cityPanel.addStructure(r_matt);
		CityDirectory.getInstance().addRestaurant(r_matt);
		
		PersonAgent p1_matt = new PersonAgent("Host", cityPanel);
		p1_matt.setupPerson(CityDirectory.getInstance().getTime(), null, r_matt, Intention.RestaurantHost, r_matt, null);
		p1_matt.setHungerEnabled(false);
		p1_matt.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p1_matt);
		personPanel.addPerson(p1_matt);
		p1.startThread();
		
		PersonAgent p2_matt = new PersonAgent("Cashier", cityPanel);
		p2_matt.setupPerson(CityDirectory.getInstance().getTime(), null, r_matt, Intention.RestaurantCashier, r_matt, null);
		p2_matt.setHungerEnabled(false);
		p2_matt.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p2_matt);
		personPanel.addPerson(p2_matt);
		p2_matt.startThread();
		
		PersonAgent p3_matt = new PersonAgent("Cook", cityPanel);
		p3_matt.setupPerson(CityDirectory.getInstance().getTime(), null, r_matt, Intention.RestaurantCook, r_matt, null);
		p3_matt.setHungerEnabled(false);
		p3_matt.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p3_matt);
		personPanel.addPerson(p3_matt);
		p3_matt.startThread();
		
		PersonAgent p4_matt = new PersonAgent("Waiter", cityPanel);
		p4_matt.setupPerson(CityDirectory.getInstance().getTime(), null, r_matt, Intention.RestaurantWaiter, r_matt, null);
		p4_matt.setHungerEnabled(false);
		p4_matt.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p4_matt);
		personPanel.addPerson(p4_matt);
		p4_matt.startThread();

		// Brandon's Restaurant
		RestaurantAnimationPanelBrandon g_brandon = new RestaurantAnimationPanelBrandon(Structure.getNextInstance(),this);
		RestaurantBrandon r_brandon = new RestaurantBrandon(275+150+75,125,50,50,Structure.getNextInstance(),g_brandon);
		restaurantPanel.addRestaurant(r_brandon);
		r_brandon.setConfigPanel(restaurantPanel);
		r_brandon.setStructurePanel(g_brandon);
		r_brandon.setClosingTime(new CityTime(13, 15));
		buildingPanels.add(g_brandon,""+r_brandon.getId());
		cityPanel.addStructure(r_brandon);
		CityDirectory.getInstance().addRestaurant(r_brandon);
		timePanel.addAnimationPanel(g_brandon);
		
		PersonAgent p1_brandon = new PersonAgent("Host", cityPanel);
		p1_brandon.setupPerson(CityDirectory.getInstance().getTime(), null, r_brandon, Intention.RestaurantHost, r_brandon, null);
		p1_brandon.setHungerEnabled(false);
		p1_brandon.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p1_brandon);
		personPanel.addPerson(p1);
		p1_brandon.startThread();
		
		PersonAgent p2_brandon = new PersonAgent("Cashier", cityPanel);
		p2_brandon.setupPerson(CityDirectory.getInstance().getTime(), null, r_brandon, Intention.RestaurantCashier, r_brandon, null);
		p2_brandon.setHungerEnabled(false);
		p2_brandon.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p2_brandon);
		personPanel.addPerson(p2_brandon);
		p2_brandon.startThread();
		
		PersonAgent p3_brandon = new PersonAgent("Cook", cityPanel);
		p3_brandon.setupPerson(CityDirectory.getInstance().getTime(), null, r_brandon, Intention.RestaurantCook, r_brandon, null);
		p3_brandon.setHungerEnabled(false);
		p3_brandon.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p3_brandon);
		personPanel.addPerson(p3_brandon);
		p3_brandon.startThread();
		
		PersonAgent p4_brandon = new PersonAgent("Waiter", cityPanel);
		p4_brandon.setupPerson(CityDirectory.getInstance().getTime(), null, r_brandon, Intention.RestaurantWaiter, r_brandon, null);
		p4_brandon.setHungerEnabled(false);
		p4_brandon.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p4_brandon);
		personPanel.addPerson(p4_brandon);
		p4_brandon.startThread();

		// Skyler's Restaurant
		RestaurantAnimationPanelSkyler g_skyler = new RestaurantAnimationPanelSkyler(Structure.getNextInstance(),this);
		timePanel.addAnimationPanel(g_skyler);
		RestaurantSkyler r_skyler = new RestaurantSkyler(275+150+75+75,125,50,50,Structure.getNextInstance(),g_skyler);
		restaurantPanel.addRestaurant(r_skyler);
		r_skyler.setConfigPanel(restaurantPanel);
		r_skyler.setStructurePanel(g_skyler);
		r_skyler.setClosingTime(new CityTime(13, 15));
		buildingPanels.add(g_skyler,""+r_skyler.getId());
		cityPanel.addStructure(r_skyler);
		CityDirectory.getInstance().addRestaurant(r_skyler);
		
		PersonAgent p1_skyler = new PersonAgent("Host", cityPanel);
		p1_skyler.setupPerson(CityDirectory.getInstance().getTime(), null, r_skyler, Intention.RestaurantHost, r_skyler, null);
		p1_skyler.setHungerEnabled(false);
		p1_skyler.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p1_skyler);
		personPanel.addPerson(p1_skyler);
		p1_skyler.startThread();
		
		PersonAgent p2_skyler = new PersonAgent("Cashier", cityPanel);
		p2_skyler.setupPerson(CityDirectory.getInstance().getTime(), null, r_skyler, Intention.RestaurantCashier, r_skyler, null);
		p2_skyler.setHungerEnabled(false);
		p2_skyler.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p2_skyler);
		personPanel.addPerson(p2_skyler);
		p2_skyler.startThread();
		
		PersonAgent p3_skyler = new PersonAgent("Cook", cityPanel);
		p3_skyler.setupPerson(CityDirectory.getInstance().getTime(), null, r_skyler, Intention.RestaurantCook, r_skyler, null);
		p3_skyler.setHungerEnabled(false);
		p3_skyler.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p3_skyler);
		personPanel.addPerson(p3_skyler);
		p3_skyler.startThread();
		
		PersonAgent p4_skyler = new PersonAgent("Waiter", cityPanel);
		p4_skyler.setupPerson(CityDirectory.getInstance().getTime(), null, r_skyler, Intention.RestaurantWaiter, r_skyler, null);
		p4_skyler.setHungerEnabled(false);
		p4_skyler.setHungerLevel(0);
		CityDirectory.getInstance().addPerson(p4_skyler);
		personPanel.addPerson(p4_skyler);
		p4_skyler.startThread();
		
		// Houses
		List<Residence> houses = new ArrayList<Residence>();
		fullRowOfHouses(houses, 5, 17);	// 24 houses
		threeHousesInRow(houses, 5, 23); // 12 houses
		fourHousesAtLocation(houses, 17, 23); // 4 houses
	}
	
	private void fullRowOfHouses(List<Residence> list, int x, int y) {
		threeHousesInRow(list, x, y);
		threeHousesInRow(list, x + 12, y);
	}
	
	private void threeHousesInRow(List<Residence> list, int x, int y) {
		fourHousesAtLocation(list, x, y);
		fourHousesAtLocation(list, x + 3, y);
		fourHousesAtLocation(list, x + 6, y);
	}
	
	private void fourHousesAtLocation(List<Residence> list, int x, int y) {
		houseAtLocation(list, x * 25, y * 25);
		houseAtLocation(list, (x + 1) * 25, y * 25);
		houseAtLocation(list, x * 25, (y + 1) * 25);
		houseAtLocation(list, (x + 1) * 25, (y + 1) * 25);
	}
	
	private void houseAtLocation(List<Residence> list, int x, int y) {
		ResidenceAnimationPanel resPanel = new ResidenceAnimationPanel(Structure.getNextInstance(), this);
		timePanel.addAnimationPanel(resPanel);
		Residence res = new Residence(x, y, 25, 25, Structure.getNextInstance(), resPanel, false);
		res.setStructurePanel(resPanel);
		buildingPanels.add(resPanel,""+res.getId());
		cityPanel.addStructure(res, new Point(15*25, 11*25), new Point(16*25, 11*25));
		CityDirectory.getInstance().addResidence(res);
		timePanel.addAnimationPanel(resPanel);
		
		list.add(res);
	}
	
	public PersonAgent createPerson(String name, Structure location, Residence home, Intention job, Structure workplace, CarAgent car) {
		PersonAgent p = new PersonAgent(name, cityPanel);
		
		p.setupPerson(CityDirectory.getInstance().getTime(), home, workplace, job, location, car);
		CityDirectory.getInstance().addPerson(p);
		personPanel.addPerson(p);
		p.startThread();
		return p;
	}
	
	public void displayStructurePanel(StructurePanel bp) {
		cardLayout.show(buildingPanels, bp.getName());
	}
	
	public void displayBlankPanel() {
		cardLayout.show(buildingPanels, "blank");
	}
}
