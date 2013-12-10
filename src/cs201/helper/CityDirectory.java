package cs201.helper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.Timer;

import cs201.agents.PersonAgent;
import cs201.structures.Structure;
import cs201.structures.bank.BankStructure;
import cs201.structures.market.MarketStructure;
import cs201.structures.residence.ApartmentComplex;
import cs201.structures.residence.Residence;
import cs201.structures.restaurant.Restaurant;
import cs201.trace.AlertLog;
import cs201.trace.AlertTag;

/**
 * The CityDirectory for SimCity201 based on a Singleton pattern using eager initialization. This class is accessible from anywhere
 * using `CityDirectory.getInstance()` allowing access to all public methods in the directory from anywhere without an explicit
 * reference to a CityDirectory object.
 * 
 * Note:: This class CANNOT be instantiated, only accessed.
 * 
 * This class keeps track of every Structure and PersonAgent in SimCity201. Additionally, it handles updating the time for all of
 * these objects as well.
 * 
 * @author Matthew Pohlmann
 *
 */
public class CityDirectory implements ActionListener {
	private static final CityDirectory INSTANCE = new CityDirectory();
	
	private CityDirectory() {};
	
	public static CityDirectory getInstance() {
		return INSTANCE;
	}
	
	private final int INITIALTIMEROUT = 2000; // 2 real life seconds
	private final int TIMESTEP = 15; // 15 SimCity201 minutes
	private Timer cityTimer = new Timer(INITIALTIMEROUT, this);
	private CityTime time = new CityTime();
	private List<PersonAgent> people = Collections.synchronizedList(new ArrayList<PersonAgent>());
	private List<Restaurant> restaurants = Collections.synchronizedList(new ArrayList<Restaurant>());
	private List<BankStructure> banks = Collections.synchronizedList(new ArrayList<BankStructure>());
	private List<MarketStructure> markets = Collections.synchronizedList(new ArrayList<MarketStructure>());
	private List<Residence> residences = Collections.synchronizedList(new ArrayList<Residence>());
	private List<ApartmentComplex> apartments = Collections.synchronizedList(new ArrayList<ApartmentComplex>());
	
	public List<Structure> getAllBuildings() {
		List<Structure> buildings = new LinkedList<Structure>();
		buildings.addAll(restaurants);
		buildings.addAll(banks);
		buildings.addAll(markets);
		buildings.addAll(residences);
		buildings.addAll(apartments);
		
		return buildings;
	}
	
	public void resetCity() {
		this.cityTimer.stop();
		this.time = new CityTime();
		
		// Clear People
		synchronized(people) {
			for (PersonAgent p : people) {
				p.stopGoOn();
				p.stateChanged();
				p.stateChanged();
				p.stateChanged();
			}
		}
		this.people.clear();
		
		// Clear buildings
		this.restaurants.clear();
		this.banks.clear();
		this.markets.clear();
		this.residences.clear();
		this.apartments.clear();
		
		this.startTime();
	}
	
	// SimCity201 Time Stuff
	public void startTime() {
		cityTimer.setDelay(INITIALTIMEROUT);
		cityTimer.setInitialDelay(INITIALTIMEROUT);
		cityTimer.setRepeats(true);
		cityTimer.start();
		AlertLog.getInstance().logInfo(AlertTag.GENERAL_CITY, "SimCity201", time.toString());
	}
	
	/**
	 * Adds an ActionListener to the CityTimer in such a way that the ActionListener in this object
	 * (CityDirectory) will ALWAYS be fired first, so everything else gets the correct time.
	 * @param a The ActionListener to add
	 */
	public void addTimerActionListener(ActionListener a) {
		ActionListener[] al = cityTimer.getActionListeners();		
		for(int i = 0; i < al.length; i++) {
			cityTimer.removeActionListener(al[i]);
		}
		
		cityTimer.addActionListener(a);
		
		for (int i = 0; i < al.length; i++) {
			cityTimer.addActionListener(al[i]);
		}
	}
	
	public void setStartTime(CityTime newTime) {
		time = newTime;
	}
	
	public void setTimerOut(int newTimerOut) {
		cityTimer.setDelay(newTimerOut);
		cityTimer.setInitialDelay(newTimerOut);
		cityTimer.restart();
	}
	
	public CityTime getTime() {
		return time;
	}
	
	public Timer getCityTimer() {
		return cityTimer;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		time.increment(TIMESTEP);
		AlertLog.getInstance().logInfo(AlertTag.GENERAL_CITY, "SimCity201", time.toString());
		
		
		
		synchronized(people) {
			for (PersonAgent p : people) {
				p.msgUpdateTime(time);
			}
		}
		
		synchronized(restaurants) {
			for (Restaurant r : restaurants) {
				r.updateTime(time);
			}
		}
		
		synchronized(banks) {
			for (BankStructure b : banks) {
				b.updateTime(time);
			}
		}
		
		synchronized(markets) {
			for (MarketStructure m : markets) {
				m.updateTime(time);
			}
		}
		
		synchronized(residences) {
			for (Residence r : residences) {
				r.updateTime(time);
			}
		}
		
		synchronized(apartments) {
			for (ApartmentComplex r : apartments) {
				r.updateTime(time);
			}
		}
	}
	
	// People Stuff
	public void addPerson(PersonAgent newPerson) {
		people.add(newPerson);
	}
	
	public List<PersonAgent> getPeople() {
		return people;
	}
	
	// Restaurant Stuff
	public void addRestaurant(Restaurant newRestaurant) {
		restaurants.add(newRestaurant);
	}
	
	public List<Restaurant> getRestaurants() {
		return restaurants;
	}
	
	public List<Restaurant> getOpenRestaurants() {
		List<Restaurant> openRestaurants = new LinkedList<Restaurant>();
		for (Restaurant m : restaurants) {
			if (m.getOpen()) {
				openRestaurants.add(m);
			}
		}
		
		return openRestaurants;
	}
	
	public Restaurant getRestaurantWithID(int id) {
		synchronized(restaurants) {
			for (Restaurant r : restaurants) {
				if (r.getId() == id) {
					return r;
				}
			}
		}
		
		return null;
	}
	
	public Restaurant getRandomRestaurant() {
		Random randGenerator = new Random();
		int num = randGenerator.nextInt(restaurants.size());
		return restaurants.get(num);
	}
	
	public Restaurant getRandomOpenRestaurant() {
		Random randGenerator = new Random();
		List<Restaurant> openRestaurants = getOpenRestaurants();
		int num = randGenerator.nextInt(openRestaurants.size());
		return openRestaurants.get(num);
	}
	
	// Bank Stuff
	public void addBank(BankStructure newBank) {
		banks.add(newBank);
	}
	
	public List<BankStructure> getBanks() {
		return banks;
	}
	
	public List<BankStructure> getOpenBanks() {
		List<BankStructure> openBanks = new LinkedList<BankStructure>();
		for (BankStructure b : banks) {
			if (b.getOpen()) {
				openBanks.add(b);
			}
		}
		
		return openBanks;
	}
	
	public BankStructure getBankWithID(int id) {
		synchronized(banks) {
			for (BankStructure r : banks) {
				if (r.getId() == id) {
					return r;
				}
			}
		}
		
		return null;
	}
	
	public BankStructure getRandomBank() {
		Random randGenerator = new Random();
		int num = randGenerator.nextInt(banks.size());
		return banks.get(num);
	}
	
	public BankStructure getRandomOpenBank() {
		Random randGenerator = new Random();
		List<BankStructure> openBanks = getOpenBanks();
		int num = randGenerator.nextInt(openBanks.size());
		return openBanks.get(num);
	}
	
	// Market Stuff
	public void addMarket(MarketStructure newMarket) {
		markets.add(newMarket);
	}
	
	public List<MarketStructure> getMarkets() {
		return markets;
	}
	
	public List<MarketStructure> getOpenMarkets() {
		List<MarketStructure> openMarkets = new LinkedList<MarketStructure>();
		for (MarketStructure m : markets) {
			if (m.getOpen()) {
				openMarkets.add(m);
			}
		}
		
		return openMarkets;
	}
	
	public MarketStructure getMarketWithID(int id) {
		for (MarketStructure r : markets) {
			if (r.getId() == id) {
				return r;
			}
		}
		
		return null;
	}
	
	public MarketStructure getRandomMarket() {
		Random randGenerator = new Random();
		int num = randGenerator.nextInt(markets.size());
		return markets.get(num);
	}
	
	public MarketStructure getRandomOpenMarket() {
		Random randGenerator = new Random();
		List<MarketStructure> openMarkets = getOpenMarkets();
		int num = randGenerator.nextInt(openMarkets.size());
		return openMarkets.get(num);
	}
	
	// Residence Stuff
	public void addResidence(Residence newResidence) {
		residences.add(newResidence);
	}
	
	public List<Residence> getResidences() {
		return residences;
	}
	
	public List<Residence> getUnoccupiedResidences() {
		List<Residence> _residences = new LinkedList<Residence>();
		synchronized(residences) {
			for (Residence r : residences) {
				if (!r.isOccupied()) {
					_residences.add(r);
				}
			}
		}
		
		return _residences;
	}
	
	public Residence getResidenceWithID(int id) {
		for (Residence r : residences) {
			if (r.getId() == id) {
				return r;
			}
		}
		
		return null;
	}	
	
	// Apartment Stuff
	public void addApartment(ApartmentComplex newApartment) {
		apartments.add(newApartment);
	}
	
	public List<ApartmentComplex> getApartments() {
		return apartments;
	}
	
	public List<ApartmentComplex> getOpenApartmentComplexes() {
		List<ApartmentComplex> openApartments = new LinkedList<ApartmentComplex>();
		for (ApartmentComplex m : apartments) {
			if (m.getOpen()) {
				openApartments.add(m);
			}
		}
		
		return openApartments;
	}
	
	public ApartmentComplex getApartmentWithID(int id) {
		for (ApartmentComplex r : apartments) {
			if (r.getId() == id) {
				return r;
			}
		}
		
		return null;
	}
}
