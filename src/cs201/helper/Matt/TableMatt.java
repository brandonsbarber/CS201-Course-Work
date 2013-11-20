package cs201.helper.Matt;

import cs201.interfaces.roles.restaurant.Matt.CustomerMatt;

/**
 * A simple table for use in the Host
 */
public class TableMatt {
	private CustomerMatt occupiedBy;
	private int tableNumber;
	private int xLoc;
	private int yLoc;

	public TableMatt(int tableNumber) {
		this.tableNumber = tableNumber;
		xLoc = 0;
		yLoc = 0;
	}
	
	/**
	 * Makes this Table occupied by a CustomerAgent
	 * @param cust The CustomerAgent sitting at this Table
	 */
	public void setOccupant(CustomerMatt cust) {
		occupiedBy = cust;
	}

	/**
	 * Makes this Table free from its CustomerAgent
	 */
	public void setUnoccupied() {
		occupiedBy = null;
	}

	/**
	 * Returns which CustomerAgent is sitting at this Table
	 * @return The CustomerAgent sitting at this table
	 */
	public CustomerMatt getOccupant() {
		return occupiedBy;
	}

	/**
	 * Returns whether or not this Table is occupied by a CustomerAgent
	 * @return True if a CustomerAgent is sitting at this Table, false otherwise
	 */
	public boolean isOccupied() {
		return occupiedBy != null;
	}

	/**
	 * Returns a textual representation of this Table
	 * @return String representing this Table and its table number
	 */
	public String toString() {
		return "table " + tableNumber;
	}
	
	/**
	 * Returns this Table's table number
	 * @return An int representing this Table's number
	 */
	public int tableNum() {
		return tableNumber;
	}
	
	/**
	 * Returns the X coordinate of this Table
	 * @return Int representing the X coordinate of this Table
	 */
	public int X() {
		return xLoc;
	}
	
	/**
	 * Returns the Y coordinate of this Table
	 * @return Int representing the Y coordinate of this Table
	 */
	public int Y() {
		return yLoc;
	}
	
	/**
	 * Set the position of this Table for use in animation
	 * @param xLoc The new X coordinate of the Table
	 * @param yLoc The new Y coordinate of the Table
	 */
	public void setPos(int xLoc, int yLoc) {
		this.xLoc = xLoc;
		this.yLoc = yLoc;
	}
}