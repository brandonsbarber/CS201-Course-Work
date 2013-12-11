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
		scenarioList.add("Normative Restaurant");
		scenarioList.add("Normative Restaurant: Two Customers, Two Waiters");
		scenarioList.add("Normative Bus");
		scenarioList.add("Normative Walking");
		scenarioList.add("Normative Driving");
		scenarioList.add("Market Restaurant Delivery (to show truck)");
		scenarioList.add("Normative Market");
		scenarioList.add("Normative Residence Test");
		scenarioList.add("Normative Apartment Complex");
		scenarioList.add("Ben's Normative Restaurant");
		scenarioList.add("Ben's Normative Restaurant: Two Customers, Two Waiters");
		scenarioList.add("Market Consumer Purchase Car");
		scenarioList.add("Ben's Normative Restaurant Delivery");
		scenarioList.add("Restaurant Shift Change");
		scenarioList.add("100 People");
		scenarioList.add("Brandon's Restaurant");
		scenarioList.add("Brandon's Restaurant: Two Customers, Two Waiters");	
		scenarioList.add("Brandon's Restaurant: Shift Change");
		scenarioList.add("Market Shift Change");
		scenarioList.add("Ben's Restaurant Shift Change");
		scenarioList.add("Brandon Restaurant Market Order");
		scenarioList.add("Beaucoup Buses");
		scenarioList.add("Killer Buses");
		scenarioList.add("Weekend Behavior Change");
		scenarioList.add("Joust");
		scenarioList.add("Skyler Restaurant");
		scenarioList.add("Failed Market Delivery Truck");
		scenarioList.add("Residence runs out of a food item");
		scenarioList.add("Residence is completely out of food");
		
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
			case 3: normativeBus(); break;
			case 4: normativeWalking(); break;
			case 5: normativeDriving(); break;
			case 6: normativeMarketRestaurantDelivery(); break;
			case 7: normativeMarket(); break;
			case 8: normativeResidence(); break;
			case 9: normativeApartmentComplex(); break;
			case 10: normativeRestaurantBen(); break;
			case 11: normativeRestaurantBenTwoOfEach(); break;
			case 12: marketConsumerCar(); break;
			case 13: normativeMarketRestaurantBenDelivery(); break;
			case 14: restaurantShiftChange(); break;
			case 15: hundredPeople(); break;
			case 16: brandonRestaurant(); break;
			case 17: brandonRestaurantTwoCustomersTwoWaiters(); break;
			case 18: brandonRestaurantShiftChange(); break;
			case 19: marketShiftChange(); break;
			case 20: benRestaurantShiftChange(); break;
			case 21: brandonRestaurantMarketOrder(); break;
			case 22: beaucoupBuses();break;
			case 23: hundredPeopleBus();break;
			case 24: weekendDifference(); break;
			case 25: joust();break;
			case 26: skylerRestaurant();break;
			case 27: failedMarketDeliveryTruck(); break;
			case 28: residenceOutOfFood(false); break;
			case 29: residenceOutOfFood(true); break;
			
			default: return;
		}
	}

	private void joust()
	{
		CityDirectory.getInstance().setStartTime(new CityTime(8, 0));
		
		ArrayList<BusStop> stops = new ArrayList<BusStop>();

		BusStopAnimationPanel panel = new BusStopAnimationPanel(Structure.getNextInstance(),this);
		stops.add(new BusStop(4*25,11*25,25,25,1, panel));
		timePanel.addAnimationPanel(panel);
		
		cityPanel.addStructure(stops.get(0),new Point(3*25,11*25),new Point((int)stops.get(0).getRect().x,(int)stops.get(0).getRect().y));
		
		BusStopAnimationPanel panel2 = new BusStopAnimationPanel(Structure.getNextInstance(),this);
		stops.add(new BusStop(25*25,6*25,25,25,2, panel2));
		timePanel.addAnimationPanel(panel2);
		
		cityPanel.addStructure(stops.get(1),new Point(26*25,6*25),new Point((int)stops.get(1).getRect().x,(int)stops.get(1).getRect().y));
		
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
		
		PersonAgent p6 = new PersonAgent("Customer-2", cityPanel);
		p6.setWakeupTime(new CityTime(8, 00));
		p6.setupPerson(CityDirectory.getInstance().getTime(), null, null, null, r, null);
		CityDirectory.getInstance().addPerson(p6);
		personPanel.addPerson(p6);
		p6.startThread();
		
		PersonAgent p7 = new PersonAgent("Customer-3", cityPanel);
		p7.setWakeupTime(new CityTime(8, 00));
		p7.setupPerson(CityDirectory.getInstance().getTime(), null, null, null, r, null);
		CityDirectory.getInstance().addPerson(p7);
		personPanel.addPerson(p7);
		p7.startThread();
		
		PersonAgent p8 = new PersonAgent("Waiter", cityPanel);
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
		stops.add(new BusStop(23*25,13*25,25,25,1, panel));
		timePanel.addAnimationPanel(panel);
		
		cityPanel.addStructure(stops.get(0),new Point(23*25,14*25),new Point((int)stops.get(0).getRect().x,(int)stops.get(0).getRect().y));
		
		BusStopAnimationPanel panel2 = new BusStopAnimationPanel(Structure.getNextInstance(),this);
		stops.add(new BusStop(1*25,6*25,25,25,2, panel2));
		timePanel.addAnimationPanel(panel2);
		
		cityPanel.addStructure(stops.get(1),new Point(2*25,6*25),new Point((int)stops.get(1).getRect().x,(int)stops.get(1).getRect().y));
		
		BusStopAnimationPanel panel3 = new BusStopAnimationPanel(Structure.getNextInstance(),this);
		stops.add(new BusStop(24*25,4*25,25,25,2, panel3));
		timePanel.addAnimationPanel(panel3);
		
		cityPanel.addStructure(stops.get(2),new Point(24*25,3*25),new Point((int)stops.get(2).getRect().x,(int)stops.get(2).getRect().y));
		
		BusStopAnimationPanel panel4 = new BusStopAnimationPanel(Structure.getNextInstance(),this);
		stops.add(new BusStop(1*25,12*25,25,25,2, panel4));
		timePanel.addAnimationPanel(panel4);
		
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
		stops.add(new BusStop(23*25,13*25,25,25,1, panel));
		timePanel.addAnimationPanel(panel);
		
		cityPanel.addStructure(stops.get(0),new Point(23*25,14*25),new Point((int)stops.get(0).getRect().x,(int)stops.get(0).getRect().y));
		
		BusStopAnimationPanel panel2 = new BusStopAnimationPanel(Structure.getNextInstance(),this);
		stops.add(new BusStop(1*25,6*25,25,25,2, panel2));
		timePanel.addAnimationPanel(panel2);
		
		cityPanel.addStructure(stops.get(1),new Point(2*25,6*25),new Point((int)stops.get(1).getRect().x,(int)stops.get(1).getRect().y));
		
		BusStopAnimationPanel panel3 = new BusStopAnimationPanel(Structure.getNextInstance(),this);
		stops.add(new BusStop(24*25,4*25,25,25,2, panel3));
		timePanel.addAnimationPanel(panel3);
		
		cityPanel.addStructure(stops.get(2),new Point(24*25,3*25),new Point((int)stops.get(2).getRect().x,(int)stops.get(2).getRect().y));
		
		BusStopAnimationPanel panel4 = new BusStopAnimationPanel(Structure.getNextInstance(),this);
		stops.add(new BusStop(1*25,12*25,25,25,2, panel4));
		timePanel.addAnimationPanel(panel4);
		
		cityPanel.addStructure(stops.get(3),new Point(2*25,12*25),new Point((int)stops.get(3).getRect().x,(int)stops.get(3).getRect().y));
		
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
