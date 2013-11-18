package cs201.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cs201.agents.PersonAgent;
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
	
	public void addPerson(PersonAgent newPerson) {
		people.add(newPerson);
	}
	
	public List<PersonAgent> getPeople() {
		return people;
	}
	
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
}
