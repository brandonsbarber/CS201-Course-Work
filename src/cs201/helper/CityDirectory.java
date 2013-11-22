package cs201.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cs201.agents.PersonAgent;
import cs201.structures.bank.Bank;
import cs201.structures.market.MarketStructure;
import cs201.structures.residence.Residence;
import cs201.structures.restaurant.Restaurant;

/**
 * The CityDirectory for SimCity201 based on a Singleton pattern using eager initialization. This class is accessible from anywhere
 * using `CityDirectory.getInstance()` allowing access to all public methods in the directory from anywhere without an explicit
 * reference to a CityDirectory object.
 * 
 * Note:: This class CANNOT be instantiated, only accessed.
 * 
 * @author Matt Pohlmann
 *
 */
public class CityDirectory {
	private static final CityDirectory INSTANCE = new CityDirectory();
	
	private CityDirectory() {};
	
	public static CityDirectory getInstance() {
		return INSTANCE;
	}
	
	private List<PersonAgent> people = Collections.synchronizedList(new ArrayList<PersonAgent>());
	private List<Restaurant> restaurants = Collections.synchronizedList(new ArrayList<Restaurant>());
	private List<Bank> banks = Collections.synchronizedList(new ArrayList<Bank>());
	private List<MarketStructure> markets = Collections.synchronizedList(new ArrayList<MarketStructure>());
	private List<Residence> residences = Collections.synchronizedList(new ArrayList<Residence>());
	
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
	
	public Restaurant getRestaurantWithID(int id) {
		for (Restaurant r : restaurants) {
			if (r.getId() == id) {
				return r;
			}
		}
		
		return null;
	}
	
	// Bank Stuff
	public void addBank(Bank newBank) {
		banks.add(newBank);
	}
	
	public List<Bank> getBanks() {
		return banks;
	}
	
	public Bank getBankWithID(int id) {
		for (Bank r : banks) {
			if (r.getId() == id) {
				return r;
			}
		}
		
		return null;
	}
	
	// Market Stuff
	public void addMarket(MarketStructure newMarket) {
		markets.add(newMarket);
	}
	
	public List<MarketStructure> getMarkets() {
		return markets;
	}
	
	public MarketStructure getMarketWithID(int id) {
		for (MarketStructure r : markets) {
			if (r.getId() == id) {
				return r;
			}
		}
		
		return null;
	}
	
	// Residence Stuff
	public void addResidence(Residence newResidence) {
		residences.add(newResidence);
	}
	
	public List<Residence> getResidences() {
		return residences;
	}
	
	public Residence getResidenceWithID(int id) {
		for (Residence r : residences) {
			if (r.getId() == id) {
				return r;
			}
		}
		
		return null;
	}
	
}
