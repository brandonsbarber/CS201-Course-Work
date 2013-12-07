package cs201.structures.restaurant;

import java.util.List;

import cs201.gui.StructurePanel;
import cs201.roles.restaurantRoles.RestaurantCashierRole;
import cs201.roles.restaurantRoles.RestaurantCookRole;
import cs201.roles.restaurantRoles.RestaurantHostRole;
import cs201.roles.restaurantRoles.RestaurantWaiterRole;
import cs201.structures.Structure;
 
/**
 * Base Restaurant class that every team member must extend in their personal Restaurants
 * @author Matthew Pohlmann
 *
 */
public abstract class Restaurant extends Structure {
	private final double INITIALMONEY = 100;
	
	protected RestaurantCashierRole cashier;
	protected RestaurantCookRole cook;
	protected RestaurantHostRole host;
	protected List<RestaurantWaiterRole> waiters;
	protected double moneyOnHand;
	protected int bankAccountNumber;
	
	public Restaurant(int x, int y, int width, int height, int id, StructurePanel p) {
		super(x, y, width, height, id, p);
		
		this.cashier = null;
		this.cook = null;
		this.host = null;
		this.waiters = null;
		this.moneyOnHand = INITIALMONEY;
		this.bankAccountNumber = -1;
	}
	
	/**
	 * Returns this Restaurant's CashierRole
	 * @return This Restaurant's Cashier
	 */
	public RestaurantCashierRole getCashier() {
		return cashier;
	}
	
	/**
	 * Returns this Restaurant's CookRole
	 * @return This Restaurant's Cook
	 */
	public RestaurantCookRole getCook() {
		return cook;
	}
	
	/**
	 * Returns this Restaurant's HostRole
	 * @return This Restaurant's Host
	 */
	public RestaurantHostRole getHost() {
		return host;
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
	 * Gets this Restaurant's bank account number
	 * @return The Restaurant's account number
	 */
	public int getBankAccountNumber() {
		return bankAccountNumber;
	}
	
	/**
	 * Should be called by the Host when he believes it's okay for all the other employees to go Home
	 */
	public abstract void closingTime();

}
