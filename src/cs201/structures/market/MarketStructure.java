package cs201.structures.market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cs201.agents.PersonAgent.Intention;
import cs201.agents.transit.TruckAgent;
import cs201.gui.StructurePanel;
import cs201.gui.roles.market.MarketConsumerGui;
import cs201.gui.roles.market.MarketEmployeeGui;
import cs201.gui.roles.market.MarketManagerGui;
import cs201.gui.structures.market.MarketAnimationPanel;
import cs201.helper.CityTime;
import cs201.interfaces.roles.market.MarketEmployee;
import cs201.roles.Role;
import cs201.roles.marketRoles.MarketConsumerRole;
import cs201.roles.marketRoles.MarketEmployeeRole;
import cs201.roles.marketRoles.MarketManagerRole;
import cs201.structures.Structure;
import cs201.trace.AlertLog;
import cs201.trace.AlertTag;

public class MarketStructure extends Structure {
	private final int INITIALEMPLOYEES 	= 1;
	private final int MAXEMPLOYEES 	   	= 3;
	
	MarketManagerRole manager = null;
	List<MarketEmployeeRole> employees = Collections.synchronizedList(new ArrayList<MarketEmployeeRole>());
	TruckAgent deliveryTruck = null;
	StructurePanel panel = null;
	double totalFunds = 0.0;
	
	/**
	 * Constructs a Market with the given dimensions at a given location. Automatically creates a MarketManagerRole and a MarketEmployeeRole
	 */
	public MarketStructure(int x, int y, int width, int height, int id, StructurePanel p) {
		super(x, y, width, height, id, p);

		panel = p;
		
		// Setup times
		this.morningShiftStart = new CityTime(8, 00);
		this.morningShiftEnd = new CityTime(12, 30);
		this.afternoonShiftStart = new CityTime(13, 00);
		this.closingTime = new CityTime(18, 00);
				
		// Create a manager to manage this market
		MarketManagerRole newManager = new MarketManagerRole("Manager", this);
		setManager(newManager);
		
		// Create a manager gui
		MarketManagerGui managerGui = new MarketManagerGui();
		newManager.setGui(managerGui);
		managerGui.setRole(newManager);
		panel.addGui(managerGui);
		
		// Create an initial employees
		for (int i = 0; i < INITIALEMPLOYEES; i++) {
			createNewEmployeeRole();
		}
		
		// Add some initial inventory
		addInventory("Steak", 1000, 10.99f);
		addInventory("Pasta", 1000, 7.99f);
		addInventory("Pizza", 1000, 5.99f);
		addInventory("Ice Cream", 1000, 1.99f);
		addInventory("Chicken", 1000, 8.99f);
		addInventory("Salad", 1000, 3.99f);
		
	}
	
	private MarketEmployeeRole createNewEmployeeRole() {
		// Set up the new employee
		MarketEmployeeRole newEmployee = new MarketEmployeeRole();
		MarketEmployeeGui employeeGui = new MarketEmployeeGui(newEmployee, (MarketAnimationPanel) panel, 2 + (employees.size() * 2), 2);
		newEmployee.setGui(employeeGui);
		panel.addGui(employeeGui);

		// Add him to our list
		employees.add(newEmployee);
		
		return newEmployee;
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
			// Let's see if there's an available employee role already created
			synchronized(employees) {
				for (MarketEmployeeRole r : employees) {
					// If the role is available
					if (r.getPerson() == null) { 
						// Tell the manager about the new employee
						manager.addEmployee(r);
						
						// Make the employee present
						r.getGui().setPresent(true);
						
						return r;
					}
				}
			}
			
			// If there isn't, we'll create a new one
			if (employees.size() < MAXEMPLOYEES) {
				
				MarketEmployeeRole newEmployee = createNewEmployeeRole();
				
				// Tell the manager about the new employee
				manager.addEmployee(newEmployee);
				
				// Make the employee present
				newEmployee.getGui().setPresent(true);
				
				return newEmployee;
			}
			
			// Otherwise, sorry! This Market is fresh out of roles (no pun intended)
			return null;
			
		case MarketConsumerGoods:
		case MarketConsumerCar:			
			
			return createConsumer();
			
		}
		return null;
	}
	
	/**
	 * Adds money to this market's "account"
	 */
	public void addMoney(double amount) {
		totalFunds += amount;
	}
	
	/**
	 * Removes money from this market's "account"
	 */
	public void subtractMoney(double amount) {
		totalFunds -= amount;
	}
	
	private void checkIfOpen() {
		if (manager.getPerson() != null && atLeastOneEmployeeWorking()) {
			isOpen = true;
			Do("Open for business.");
		}
	}
	
	private boolean atLeastOneEmployeeWorking() {
		synchronized(employees) {
			for (MarketEmployeeRole r : employees) {
				if (r.getPerson() != null) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Creates a new MarketConsumerRole with a corresponding gui element, and adds it to the panel
	 */
	public MarketConsumerRole createConsumer() {
		MarketConsumerRole newConsumer = new MarketConsumerRole();
		
		// Create a consumer gui and add it to the panel
		MarketConsumerGui newGui = new MarketConsumerGui();
		newConsumer.setGui(newGui);
		newGui.setRole(newConsumer);
		newGui.setAnimationPanel((MarketAnimationPanel)panel);
		panel.addGui(newGui);
		newConsumer.setStructure(this);
		
		return newConsumer;
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
	
	public void addTruck(TruckAgent truck)
	{
		deliveryTruck = truck;
	}
	
	/**
	 * Add an inventory entry to this market. You must instantiate a MarketManager first and set him with setManager(...);
	 * @param item A String name for the item, i.e., "chicken"
	 * @param quantity The number of inventory items the market has in stock
	 * @param price A float price
	 */
	public void addInventory(String item, int quantity, float price) {
		if (manager != null) {
			manager.AddInventoryEntry(new MarketManagerRole.InventoryEntry(item, quantity, price));
		}
	}

	@Override
	public void updateTime(CityTime time) {
		if (!isOpen) {
			checkIfOpen();
		}
		
		if (time.equalsIgnoreDay(morningShiftEnd)) {
			AlertLog.getInstance().logMessage(AlertTag.MARKET, this.toString(), "Morning shift over!");
			if (manager.getPerson() != null) {
				manager.msgClosingTime();
				isOpen = false;
			}
		}
		
		if (time.equalsIgnoreDay(this.closingTime)) {
			AlertLog.getInstance().logMessage(AlertTag.MARKET, this.toString(), "It's closing time!");
			if (manager.getPerson() != null) {
				manager.msgClosingTime();
				isOpen = false;
			}
		}
		
	}
	
	public boolean isOpen() {
		return isOpen;
	}

}
