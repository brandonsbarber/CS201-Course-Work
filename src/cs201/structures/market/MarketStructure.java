package cs201.structures.market;

import java.util.List;

import cs201.agents.PersonAgent.Intention;
import cs201.agents.transit.TruckAgent;
import cs201.gui.StructurePanel;
import cs201.helper.CityTime;
import cs201.interfaces.roles.market.MarketEmployee;
import cs201.interfaces.roles.market.MarketManager;
import cs201.roles.Role;
import cs201.roles.marketRoles.MarketEmployeeRole;
import cs201.roles.marketRoles.MarketManagerRole;
import cs201.structures.Structure;

public class MarketStructure extends Structure {
	MarketManagerRole manager = null;
	MarketEmployeeRole employee = null;
	TruckAgent deliveryTruck = null;
	
	/**
	 * Constructs a Market with the given dimensions at a given location. Automatically creates a MarketManagerRole and a MarketEmployeeRole
	 */
	public MarketStructure(int x, int y, int width, int height, int id, StructurePanel p) {
		super(x, y, width, height, id, p);
		
		// Create a manager to manage this market
		MarketManagerRole newManager = new MarketManagerRole("Manager", this);
		setManager(newManager);
		
		// Create an initial employee
		MarketEmployeeRole newEmployee = new MarketEmployeeRole();
		hireEmployee(newEmployee);
		employee = newEmployee;
		
		// Initialize delivery truck
		this.deliveryTruck = new TruckAgent(this);
		deliveryTruck.startThread();
	}

	
	/**
	 * Hires a new MarketEmployee at this market. Automatically adds the new employee to the MarketManager's list.
	 * @param newEmployee The new MarketEmployee to be added.
	 */
	public void hireEmployee(MarketEmployee newEmployee) {
		if (manager != null)
			manager.addEmployee(newEmployee);
	}
	
	/**
	 * Fires a MarketEmployee at this market.
	 * @param employee The MarketEmployee to be fired.
	 */
	public void fireEmployee(MarketEmployee employee) {
		if (manager != null)
			manager.removeEmployee(employee);
			
	}
	
	/**
	 * @return A list of MarketEmployees currently employed at this market.
	 */
	public List<MarketEmployee> getEmployees() {
		return manager.getEmployees();
	}
	
	
	/**
	 * Based on the given Intention, return the appropriate role
	 * @param role the requester's Intention when visiting this Market
	 * @return A Role (usually a MarketManagerRole) representing the contact person for this market
	 */
	public Role getRole(Intention role) {
		switch (role) {
		case MarketManager:
			return manager;
			
		case MarketEmployee:
			return employee;
		}
		return null;
	}
	
	/**
	 * Sets the MarketManagerRole for this market.
	 * @param m the MarketManagerRole
	 */
	public void setManager(MarketManagerRole m) {
		manager = m;
	}
	
	/**
	 * @return The current working MarketManagerRole
	 */
	public MarketManagerRole getManager() {
		return manager;
	}
	
	/**
	 * @return The delivery truck for the market.
	 */
	public TruckAgent getDeliveryTruck() {
		return deliveryTruck;
	}
	
	/**
	 * Add an inventory entry to this market. You must instantiate a MarketManager first and set him with setManager(...);
	 * @param item A String name for the item, i.e., "chicken"
	 * @param quantity The number of inventory items the market has in stock
	 * @param price A float price
	 */
	public void addInventory(String item, int quantity, int price) {
		if (manager != null) {
			manager.AddInventoryEntry(new MarketManagerRole.InventoryEntry(item, quantity, price));
		}
	}

	@Override
	public void updateTime(CityTime time) {
		// TODO Auto-generated method stub
		
	}

}
