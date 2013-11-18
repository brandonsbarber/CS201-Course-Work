package cs201.structures.restaurant;

import java.util.List;

import cs201.agents.PersonAgent.Intention;
import cs201.roles.Role;
import cs201.roles.restaurantRoles.RestaurantCashierRole;
import cs201.roles.restaurantRoles.RestaurantCookRole;
import cs201.roles.restaurantRoles.RestaurantHostRole;
import cs201.roles.restaurantRoles.RestaurantWaiterRole;
import cs201.structures.Structure;

public abstract class Restaurant extends Structure {
	RestaurantCashierRole cashier;
	RestaurantCookRole cook;
	RestaurantHostRole host;
	List<RestaurantWaiterRole> waiters;
	double moneyOnHand;
	int bankAccountNumber;
	boolean isOpen;
	
	public Restaurant(int x, int y, int width, int height, int id) {
		super(x, y, width, height, id);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Role getRole(Intention role) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Returns this Restaurant's Cashier if someone is currently acting as a Cashier, null otherwise
	 * @return This Restaurant's active Cashier
	 */
	public RestaurantCashierRole getCashier() {
		return (cashier.getPerson() != null) ? cashier : null;
	}
	
	/**
	 * Returns this Restaurant's Cook if someone is currently acting as a Cook, null otherwise
	 * @return This Restaurant's active Cook
	 */
	public RestaurantCookRole getCook() {
		return (cook.getPerson() != null) ? cook : null;
	}
	
	/**
	 * Returns this Restaurant's Host if someone is currently acting as a Host, null otherwise
	 * @return This Restaurant's active Host
	 */
	public RestaurantHostRole getHost() {
		return (host.getPerson() != null) ? host : null;
	}
	
	/**
	 * Returns a list of this Restaurant's Waiters
	 * @return A list of Waiters
	 */
	public List<RestaurantWaiterRole> getWaiters() {
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
