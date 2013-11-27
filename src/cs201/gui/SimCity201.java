 package cs201.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import cs201.agents.PersonAgent;
import cs201.agents.PersonAgent.Intention;
import cs201.agents.transit.BusAgent;
import cs201.agents.transit.CarAgent;
import cs201.agents.transit.TruckAgent;
import cs201.gui.structures.market.MarketAnimationPanel;
import cs201.gui.structures.residence.ApartmentComplexAnimationPanel;
import cs201.gui.structures.residence.ResidenceAnimationPanel;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelMatt;
import cs201.gui.transit.BusGui;
import cs201.gui.transit.CarGui;
import cs201.helper.CityDirectory;
import cs201.helper.CityTime;
import cs201.helper.CityTime.WeekDay;
import cs201.helper.transit.BusRoute;
import cs201.interfaces.roles.housing.Renter;
import cs201.roles.restaurantRoles.Matt.RestaurantCookRoleMatt;
import cs201.structures.Structure;
import cs201.structures.market.MarketStructure;
import cs201.structures.residence.ApartmentComplex;
import cs201.structures.residence.Residence;
import cs201.structures.restaurant.RestaurantMatt;
import cs201.structures.transit.BusStop;

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
		
		Scanner in = new Scanner(System.in);

		boolean running = true;
		
		while(running)
		{
			System.out.print("Which scenario would you like to run?\n0) Toggles debug mode for visual aid. 1) Normative Restaurant\n2) Normative Restaurant: Two Customers, Two Waiters\n3) Normative Bus\n4) Normative Walking\n5) Normative Driving\n6) Market Restaurant Delivery (to show truck)\n7) Residence Test\n8) Apartment Complex\nYour choice: ");
			String choice = in.nextLine();
			try
			{
				int choiceNum = Integer.parseInt(choice);
				switch(choiceNum)
				{
					case 1: normativeRestaurant();running = false; break;
					case 2: normativeRestaurantTwoCustomersTwoWaiters(); running = false; break;
					case 3: normativeBus(); running = false; break;
					case 4: normativeWalking(); running = false; break;
					case 5: normativeDriving(); running = false; break;
					case 6: normativeMarketRestaurantDelivery(); running = false; break;
					case 7: normativeResidence(); running = false; break;
					case 8: normativeApartmentComplex(); running = false; break;
					case 0: cityPanel.SHOW_DEBUG = ! cityPanel.SHOW_DEBUG; System.out.println("Toggled debug");break;
					default: System.out.println("Please enter a number from the range.");
				}
			}
			catch(NumberFormatException e)
			{
				System.out.println("Please enter a valid number.");
				continue;
			}
		}
		in.close();
		
		pack();
		CityDirectory.getInstance().startTime();
	}
	
	private void normativeRestaurant() {
		/* A normal Waiter, Host, Cashier, and Cook all come to work at 8:00AM. The Restaurant opens
		 * when all of them have arrived at the Restaurant. At 8:30AM a single Customer comes
		 * and a normative Restaurant scenario starts where he orders food and leaves when done.
		 * The Restaurant closes at 1:15PM (should be right after the Customer leaves), and all
		 * the employees go home.
		 */
		CityDirectory.getInstance().setStartTime(new CityTime(8, 0));
		
		RestaurantAnimationPanelMatt g = new RestaurantAnimationPanelMatt(Structure.getNextInstance(),this);
		RestaurantMatt r = new RestaurantMatt(100,100,50,50,Structure.getNextInstance(),g);
		settingsPanel.addPanel("Restaurants",new ConfigPanel());
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
	
	private void normativeRestaurantTwoCustomersTwoWaiters() {
		/* A normal Waiter and rotating stand Waiter, Host, Cashier, and Cook all come to work at 8:00AM. The Restaurant opens
		 * when the Host, Cashier, Cook, and at least one Waiter have arrived at the Restaurant. At 8:30AM two Customers come
		 * and a normative Restaurant scenario starts where they both order food and leave when done.
		 * The Restaurant closes at 1:15PM (should be right after the Customers leave), and all the
		 * employees go home.
		 */
		CityDirectory.getInstance().setStartTime(new CityTime(8, 0));
		
		RestaurantAnimationPanelMatt g = new RestaurantAnimationPanelMatt(Structure.getNextInstance(),this);
		RestaurantMatt r = new RestaurantMatt(100,100,50,50,Structure.getNextInstance(),g);
		settingsPanel.addPanel("Restaurants",new ConfigPanel());
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
	
	private void normativeApartmentComplex() {
		/* 
		 * Creates a Landlord who lives in a Residence that he owns and a Renter who lives in a Residence
		 * that he pays rent on. Every morning at 7am, the Renter will check if he has to pay any rent. The Landlord
		 * goes to work at 8am at the ApartmentComplex, where he checks if any of the Renters in his list of properties needs to pay rent.
		 * The Renter we've created has to pay his rent on Tuesdays, so the Landlord will notify him of his rent due
		 * on Monday. Otherwise, both will act as regular residents in their homes.
		 */
		ApartmentComplexAnimationPanel acap = new ApartmentComplexAnimationPanel(Structure.getNextInstance(),this);
		ApartmentComplex ac = new ApartmentComplex(14*25, 9*25, 25, 25, Structure.getNextInstance(), acap);
		ac.setStructurePanel(acap);
		ac.setClosingTime(new CityTime(12, 0));
		buildingPanels.add(acap,""+ac.getId());
		cityPanel.addStructure(ac, new Point(14*25,7*25), new Point(14*25, 8*25));
		CityDirectory.getInstance().addApartment(ac);
		
		ResidenceAnimationPanel resPanel = new ResidenceAnimationPanel(Structure.getNextInstance(), this);
		Residence res = new Residence(14*25, 10*25, 25, 25, Structure.getNextInstance(), resPanel, true);
		res.setStructurePanel(resPanel);
		buildingPanels.add(resPanel,""+res.getId());
		cityPanel.addStructure(res, new Point(12*25, 10*25), new Point(13*25, 10*25));
		CityDirectory.getInstance().addResidence(res);
		
		ResidenceAnimationPanel resPanel2 = new ResidenceAnimationPanel(Structure.getNextInstance(), this);
		Residence res2 = new Residence(15*25, 9*25, 25, 25, Structure.getNextInstance(), resPanel2, false);
		res2.setStructurePanel(resPanel2);
		buildingPanels.add(resPanel2, ""+res2.getId());
		cityPanel.addStructure(res2, new Point(17*25, 9*25), new Point(16*25, 9*25));
		CityDirectory.getInstance().addResidence(res2);
		
		PersonAgent p1 = new PersonAgent("Renter",cityPanel);
		p1.setupPerson(CityDirectory.getInstance().getTime(), res, null, null, res, null);
		CityDirectory.getInstance().addPerson(p1);
		
		PersonAgent p2 = new PersonAgent("Landlord",cityPanel);
		p2.setupPerson(CityDirectory.getInstance().getTime(), res2, ac, Intention.ResidenceLandLord, res2, null);
		CityDirectory.getInstance().addPerson(p2);
		
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
		Residence res = new Residence(14*25, 10*25, 25, 25, Structure.getNextInstance(), resPanel, false);
		res.setStructurePanel(resPanel);
		buildingPanels.add(resPanel,""+res.getId());
		cityPanel.addStructure(res, new Point(12*25, 10*25), new Point(13*25, 10*25));
		CityDirectory.getInstance().addResidence(res);
		
		PersonAgent p1 = new PersonAgent("Resident",cityPanel);
		p1.setupPerson(CityDirectory.getInstance().getTime(), res, null, null, res, null);
		CityDirectory.getInstance().addPerson(p1);
		
		p1.startThread();
	}
	
	private void normativeWalking()
	{
		/*
		 * Creates a Person who will walk from the market at 100,100 to the restaurant at 475,225 by way of crosswalks and sidewalks.
		 * The route that is taken is defined by arrows shown in Debug mode (viewed by typing zero). When the Person reaches his destination, he
		 * goes inside the structure there and performs structure actions.
		 * He begins his walk at 7:00 AM.
		 */
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
		/*
		 * Creates a Person who will drive from the market at 100,100 to the restaurant at 475,225 by way of roads.
		 * The person does this by calling a car, who comes to pick up the person. The person gets into the car, which then drives
		 * on a path determined by BFS on a movement map (visible in Debug mode), which takes him or her to the parking location
		 * of the building. The person then walks to the sidewalk location and is brought inside the building.
		 * This starts at 7:00 AM.
		 * He currently moves around the block due to the nature of the sidewalks and his movement priorities.
		 */
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
		cityPanel.addStructure(r,new Point(17*25,9*25), new Point(18*25,9*25));
		CityDirectory.getInstance().addRestaurant(r);
	
		CarAgent car = new CarAgent();
		CarGui cGui = new CarGui(car,cityPanel);
		car.setGui(cGui);
		cityPanel.addGui(cGui);
		car.startThread();
	
		PersonAgent p1 = new PersonAgent("Car Rider",cityPanel);
		p1.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantHost, m, car);
		CityDirectory.getInstance().addPerson(p1);
		p1.startThread();
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
		ArrayList<BusStop> stops = new ArrayList<BusStop>();
		stops.add(new BusStop(22*25,13*25,25,25,1, null));
		stops.add(new BusStop(12*25,13*25,25,25,2, null));
		stops.add(new BusStop(2*25,13*25,25,25,3, null));
		stops.add(new BusStop(22*25,1*25,25,25,4, null));
		stops.add(new BusStop(12*25,1*25,25,25,5, null));
		stops.add(new BusStop(2*25,1*25,25,25,6, null));
		
		for(BusStop stop : stops)
		{
			cityPanel.addStructure(stop,new Point((int)stop.x,((int)stop.y==25?2*25:12*25)),new Point((int)stop.x,(int)stop.y));
		}
		
		BusAgent bus = new BusAgent(new BusRoute(stops),0);
		BusGui busG = new BusGui(bus,cityPanel,bus.getRoute().getCurrentLocation().getParkingLocation().x,bus.getRoute().getCurrentLocation().getParkingLocation().y);
		bus.setGui(busG);
		cityPanel.addGui(busG);
		bus.startThread();
		
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

		PersonAgent p1 = new PersonAgent("Bus Rider",cityPanel);
		p1.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantHost, m, null);
		p1.getPassengerRole().setBusStops(stops);
		CityDirectory.getInstance().addPerson(p1);
		p1.startThread();
	}
	
	private void normativeMarketRestaurantDelivery()
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
		MarketStructure m = new MarketStructure(100,100,50,50,Structure.getNextInstance(),mG);
		m.setStructurePanel(mG);
		m.setClosingTime(new CityTime(18, 0));
		buildingPanels.add(mG,""+m.getId());
		cityPanel.addStructure(m);
		
		TruckAgent truck = new TruckAgent(m);
		truck.startThread();
		m.addTruck(truck);
		CityDirectory.getInstance().addMarket(m);
			
		RestaurantAnimationPanelMatt g = new RestaurantAnimationPanelMatt(Structure.getNextInstance(),this);
		RestaurantMatt r = new RestaurantMatt(475,225,50,50,Structure.getNextInstance(),g);
		r.setStructurePanel(g);
		r.setClosingTime(new CityTime(14, 0));
		buildingPanels.add(g,""+r.getId());
		cityPanel.addStructure(r,new Point(17*25,9*25), new Point(19*25,8*25));
		CityDirectory.getInstance().addRestaurant(r);
		
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
		
		((RestaurantCookRoleMatt) r.getCook()).emptySteakInventory();
	}
	
	public void displayStructurePanel(StructurePanel bp) {
		cardLayout.show(buildingPanels, bp.getName());
	}
}
