package cs201.structures.market;

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

public class MarketStructure extends Structure {
	MarketManagerRole manager = null;
	MarketEmployeeRole employee = null;
	TruckAgent deliveryTruck = null;
	StructurePanel panel = null;
	boolean isOpen = false;
	
	/**
	 * Constructs a Market with the given dimensions at a given location. Automatically creates a MarketManagerRole and a MarketEmployeeRole
	 */
	public MarketStructure(int x, int y, int width, int height, int id, StructurePanel p) {
		super(x, y, width, height, id, p);

		panel = p;
				
		// Create a manager to manage this market
		MarketManagerRole newManager = new MarketManagerRole("Manager", this);
		setManager(newManager);
		
		// Create a manager gui
		MarketManagerGui managerGui = new MarketManagerGui();
		newManager.setGui(managerGui);
		managerGui.setRole(newManager);
		panel.addGui(managerGui);
		
		// Create an initial employee
		MarketEmployeeRole newEmployee = new MarketEmployeeRole();
		hireEmployee(newEmployee);
		employee = newEmployee;
		
		// Create an employee gui
		MarketAnimationPanel panel = (MarketAnimationPanel)p;
		MarketEmployeeGui employeeGui = new MarketEmployeeGui(newEmployee, panel, 1, 3);
		employee.setGui(employeeGui);
		panel.addGui(employeeGui);
		
		// Initialize delivery truck
		this.deliveryTruck = new TruckAgent(this);
		deliveryTruck.startThread();
		
		// Add some initial inventory
		addInventory("Steak", 1000, 10.99f);
		addInventory("Pasta", 1000, 7.99f);
		addInventory("Pizza", 1000, 5.99f);
		addInventory("Ice Cream", 1000, 1.99f);
		addInventory("Chicken", 1000, 8.99f);
		addInventory("Salad", 1000, 3.99f);
		
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
			
		case MarketConsumerGoods:
		case MarketConsumerCar:			
			
			return createConsumer();
			
		}
		return null;
	}
	
	private void checkIfOpen() {
		if (manager.getPerson() != null && employee.getPerson() != null) {
			isOpen = true;
			Do("Open for business.");
		}
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
		
		if (time.equalsIgnoreDay(this.closingTime)) {
			// Message everyone to go home
			manager.msgClosingTime();
			isOpen = false;
		}
		
	}
	
	public boolean isOpen() {
		return isOpen;
	}

}
