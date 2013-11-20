package cs201.structures.restaurant;

import java.util.ArrayList;
import java.util.List;

import cs201.agents.PersonAgent.Intention;
import cs201.interfaces.roles.restaurant.RestaurantCashier;
import cs201.interfaces.roles.restaurant.RestaurantCook;
import cs201.interfaces.roles.restaurant.RestaurantCustomer;
import cs201.interfaces.roles.restaurant.RestaurantHost;
import cs201.interfaces.roles.restaurant.RestaurantWaiter;
import cs201.roles.Role;
import cs201.roles.restaurantRoles.Matt.RestaurantCashierRoleMatt;
import cs201.roles.restaurantRoles.Matt.RestaurantCookRoleMatt;
import cs201.roles.restaurantRoles.Matt.RestaurantHostRoleMatt;
import cs201.roles.restaurantRoles.Matt.RestaurantWaiterRoleMatt;
import cs201.structures.Structure;

/**
 * Base Restaurant class that every team member must extend in their personal Restaurants
 * @author Matt Pohlmann
 *
 */
public abstract class Restaurant extends Structure {
	protected final int INITIALWAITERS = 2;
	protected final int MAXWAITERS = 3;
	
	private RestaurantCashier cashier;
	private RestaurantCook cook;
	private RestaurantHost host;
	private List<RestaurantWaiter> waiters;
	private double moneyOnHand;
	private int bankAccountNumber;
	private boolean isOpen;
	
	public Restaurant(int x, int y, int width, int height, int id) {
		super(x, y, width, height, id);
		// TODO Auto-generated constructor stub
		
		if (this instanceof RestaurantMatt) {
			host = new RestaurantHostRoleMatt();
			cashier = new RestaurantCashierRoleMatt(host);
			cook = new RestaurantCookRoleMatt();
			waiters = new ArrayList<RestaurantWaiter>();
			for (int i = 0; i < INITIALWAITERS; i++) {
				waiters.add(new RestaurantWaiterRoleMatt());
			}
		}
	}

	@Override
	public Role getRole(Intention role) {
		switch (role) {
		case RestaurantCook: {
			return (cook.getPerson() == null) ? cook : null;
		}
		case RestaurantHost: {
			return (host.getPerson() == null) ? host : null;
		}
		case RestaurantWaiter: {
			RestaurantWaiter waiter = null;
			for (RestaurantWaiter r : waiters) {
				if (r.getPerson() == null) {
					waiter = r;
					break;
				}
			}
			if (waiters.size() < MAXWAITERS) {
				RestaurantWaiter newWaiter = new RestaurantWaiter();
				waiters.add(newWaiter);
				waiter = newWaiter;
			}
			
			return waiter;
		}
		case RestaurantCashier: {
			return (cashier.getPerson() == null) ? cashier : null;
		}
		case RestaurantCustomer: {
			return new RestaurantCustomer();
		}
		default: {
			Do("Wrong Intention provided in getRole(Intention)");
			return null;
		}
		}
	}
	
	/**
	 * Returns this Restaurant's Cashier if someone is currently acting as a Cashier, null otherwise
	 * @return This Restaurant's active Cashier
	 */
	public RestaurantCashier getCashier() {
		return (cashier.getPerson() != null) ? cashier : null;
	}
	
	/**
	 * Returns this Restaurant's Cook if someone is currently acting as a Cook, null otherwise
	 * @return This Restaurant's active Cook
	 */
	public RestaurantCook getCook() {
		return (cook.getPerson() != null) ? cook : null;
	}
	
	/**
	 * Returns this Restaurant's Host if someone is currently acting as a Host, null otherwise
	 * @return This Restaurant's active Host
	 */
	public RestaurantHost getHost() {
		return (host.getPerson() != null) ? host : null;
	}
	
	/**
	 * Returns a list of this Restaurant's Waiters
	 * @return A list of Waiters
	 */
	public List<RestaurantWaiter> getWaiters() {
		return waiters;
	}
	
	/**
	 * Adds money to the Restaurant's money on hand
	 * @param howMuch How much money to add
	 */
	public void addMoney(double howMuch) {
		moneyOnHand += howMuch;
	}
	
	/**
	 * Removes money from the Restaurant's money on hand
	 * @param howMuch How much money to remove
	 */
	public void removeMoney(double howMuch) {
		moneyOnHand -= howMuch;
	}
	
	/**
	 * Returns how much money this Restaurant currently has on hand (not including what's in the bank)
	 * @return The Restaurant's money on hand
	 */
	public double getCurrentRestaurantMoney() {
		return moneyOnHand;
	}
	
	/**
	 * Sets this Restaurant's bank account number
	 * @param newNumber The new account number
	 */
	public void setBankAccountNumber(int newNumber) {
		bankAccountNumber = newNumber;
	}
	
	/**
	 * Gets this Restaurant's bank account number
	 * @return The Restaurant's account number
	 */
	public int getBankAccountNumber() {
		return bankAccountNumber;
	}
	
	/**
	 * Sets whether this Restaurant is open or closed
	 * @param open True to set this Restaurant to open, closed to close it down
	 */
	public void setOpen(boolean open) {
		isOpen = open;
	}
	
	/** 
	 * Returns whether or not this Restaurant is open
	 * @return True for an open Restaurant, false otherwise
	 */
	public boolean getOpen() {
		return isOpen;
	}

}
