package cs201.structures;

import java.awt.Point;
import java.awt.geom.Rectangle2D;

import cs201.agents.PersonAgent;
import cs201.gui.StructurePanel;
import cs201.helper.CityTime;
import cs201.roles.Role;

/**
 * Base class for all Structures in SimCity201. They are actually Rectangles that can be clicked on in the GUI
 * @author Matthew Pohlmann
 *
 */
@SuppressWarnings("serial")
public abstract class Structure extends Rectangle2D.Double {
	protected int id;
	protected StructurePanel panel;
	protected Point guiLocation;
	protected Point entranceLocation;
	protected Point deliveryLocation;
	protected Point parkingLocation;
	protected CityTime closingTime;
	protected boolean isOpen;
	
	private static int INSTANCES = 0;
	
	public Structure(int x, int y, int width, int height, int id, StructurePanel p) {
		super(x, y, width, height);
		
		this.id = ++INSTANCES;
		this.panel = p;
		this.guiLocation = null;
		this.entranceLocation = null;
		this.deliveryLocation = null;
		this.parkingLocation = null;
		this.closingTime = null;
		this.isOpen = false;
	}
	
	/**
	 * Returns this Structure's unique ID
	 * @return
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Used to get a unique ID for a Structure
	 * @return int representing the number of Structures in SimCity
	 */
	public static int getNextInstance() {
		return INSTANCES + 1;
	}
	
	/**
	 * Given an Intention enum (from PersonAgent) will return the correct Role for purposes of assign a Role
	 * @param role The Intention from PersonAgent
	 * @return The correct Role based upon the PersonAgent's intent at this Structure
	 */
	public abstract Role getRole(PersonAgent.Intention role);
	
	/**
	 * Updates the time at this Structure. Used to tell people working there that it's time to close up and go home
	 * @param time The new CityTime
	 */
	public abstract void updateTime(CityTime time);
	
	/**
	 * When this Structure is clicked in the GUI, this Structure's panel will be shown in the card layout of the main program
	 */
	public void displayStructure() {
		panel.displayStructurePanel();
	}
	
	/**
	 * Sets this Structure's panel so it can be viewed when clicked
	 * @param sp The new StructurePanel
	 */
	public void setStructurePanel(StructurePanel sp) {
		panel = sp;
	}
	
	/**
	 * Returns the location of this Structure in SimCity201
	 * @return A Point representing this Structure's location
	 */
	public Point getGuiLocation() {
		return guiLocation;
	}
	
	/**
	 * Returns the location of this Structure's entrance
	 * @return A Point representing this Structure's entrance
	 */
	public Point getEntranceLocation() {
		return entranceLocation;
	}
	
	/**
	 * Returns the location of this Structure's delivery ramp
	 * @return A Point representing this Structure's delivery ramp location
	 */
	public Point getDeliveryLocation() {
		return guiLocation;
	}
	
	/**
	 * Returns the location of this Structure's "Parking Garage"
	 * @return A Point representing this Structure's parking garage entrance
	 */
	public Point getParkingLocation() {
		return parkingLocation;
	}
	
	/**
	 * Returns the time at which this Structure closes (if at all)
	 * @return
	 */
	public CityTime getClosingTime() {
		return closingTime;
	}
	
	/**
	 * Set when this Structure closes. (Day is irrelevant, as we assume every Restaurant/Bank/Market is closed on weekends?)
	 * @param time The Structure's closing time
	 */
	public void setClosingTime(CityTime time) {
		this.closingTime = new CityTime();
		this.closingTime.day = null;
		this.closingTime.hour = time.hour;
		this.closingTime.minute = time.minute;
	}
	
	/**
	 * Sets whether this Structure is open or closed
	 * @param open True to set this Structure to open, False to close it down
	 */
	public void setOpen(boolean open) {
		isOpen = open;
	}
	
	/** 
	 * Returns whether or not this Structure is open
	 * @return True for an open Structure, false otherwise
	 */
	public boolean getOpen() {
		return isOpen;
	}
	
	/**
	 * This function is now deprecated after the addition of the Trace Panel. Please use that instead.
	 * 
	 * General-purpose function for printing to the terminal. Format: "Restaurant 2: Gained $100"
	 * @param msg The message that should be printed (i.e. Gained $100)
	 */
	@Deprecated
	protected void Do(String msg) {
		StringBuffer output = new StringBuffer();
		output.append("[");
		output.append(this.getClass().getSimpleName());
		output.append("] ");
		output.append(this.id);
		output.append(": ");
		output.append(msg);
		
		System.out.println(output.toString());
	}
	
	/**
	 * A String representation of this Structure (i.e. Bank 1)
	 * @return A String representation of this Structure
	 */
	public String toString() {
		StringBuffer output = new StringBuffer();
		output.append(this.getClass().getSimpleName());
		output.append(" ");
		output.append(this.id);
		
		return output.toString();
	}

	public void setParkingLocation(Point point)
	{
		parkingLocation = point;
	}
	
	public void setEntranceLocation(Point point)
	{
		entranceLocation = point;
	}
}
