package cs201.roles.marketRoles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import cs201.agents.PersonAgent.Intention;
import cs201.agents.transit.CarAgent;
import cs201.agents.transit.TruckAgent;
import cs201.gui.CityPanel;
import cs201.gui.roles.market.MarketManagerGui;
import cs201.gui.transit.CarGui;
import cs201.gui.transit.TruckGui;
import cs201.helper.CityDirectory;
import cs201.interfaces.agents.transit.Vehicle;
import cs201.interfaces.roles.market.MarketConsumer;
import cs201.interfaces.roles.market.MarketEmployee;
import cs201.interfaces.roles.market.MarketManager;
import cs201.roles.Role;
import cs201.structures.Structure;
import cs201.structures.market.MarketStructure;
import cs201.structures.restaurant.Restaurant;
import cs201.trace.AlertLog;
import cs201.trace.AlertTag;

/**
 * The MarketManagerRole, the head of a market. Deals directly with customers and money. Dispatches MarketEmployees to retrieve items
 * @author Ben Doherty
 */
public class MarketManagerRole extends Role implements MarketManager {
	
	/*
	 * ********** DATA **********
	 */
	
	private static final int CARPRICE = 1000;

	String name = "";
	Semaphore animation = new Semaphore(0, true);
	
	// Lists
	public List<Order> orders = Collections.synchronizedList( new ArrayList<Order>() );
	public List<MyEmployee> employees = new ArrayList<MyEmployee>();
	public List<CarOrder> carOrders = Collections.synchronizedList( new ArrayList<CarOrder>() );
	
	// Maps
	public Map<MarketConsumer, ConsumerRecord> consumerBalance = new HashMap<MarketConsumer, ConsumerRecord>();
	public Map<Structure, StructureRecord> structureBalance = new HashMap<Structure, StructureRecord>();
	Map<String, InventoryEntry> inventory = new HashMap<String, InventoryEntry>();
	
	// GUI, structure
	MarketManagerGui gui;
	MarketStructure structure;
	
	// Flags
	boolean timeToLeave = false;
	
	public static class ItemRequest {
		public String item;
		public int amount;
		
		public ItemRequest(String i, int a) {
			item = i;
			amount = a;
		}
		
		public String toString() {
			StringBuffer string = new StringBuffer();
			string.append(item);
			string.append(" [");
			string.append(amount);
			string.append("]");
			
			return string.toString();
		}
	}
	
	public static class InventoryEntry {
		public String item;
		public int amount;
		public float price;
		
		public InventoryEntry(String i, int a, float p) {
			item = i;
			amount = a;
			price = p;
		}
	}
	
	/**
	 * Holds the balance for a single MarketConsumer
	 */
	public class ConsumerRecord {
		public MarketConsumer consumer;
		public float balance;
		
		public ConsumerRecord(MarketConsumer c, float b) {
			consumer = c;
			balance = b;
		}
	}
	
	/**
	 * Holds the balance for a single Structure
	 */
	public class StructureRecord {
		public Structure structure;
		public float balance;
		
		public StructureRecord(Structure s, float b) {
			structure = s;
			balance = b;
		}
	}
	
	enum OrderState {PENDING, PROCESSING, READY, SENT, FAILED};
	enum OrderType {INPERSON, DELIVERY};
	int nextOrderID = 0;
	private class Order {
		List<ItemRequest> items;
		MarketConsumer consumer = null;					// For INPERSON orders
		Restaurant structure = null;					// For DELIVERY orders
		OrderState state;
		OrderType type;
		float totalPrice;
		int id;
		
		/**
		 * Constructs an Order object for INPERSON orders
		 */
		public Order(MarketConsumer c, List<ItemRequest> i, OrderState s, int oID) {
			items = i;
			consumer = c;
			state = s;
			type = OrderType.INPERSON;
			id = oID;
		}
		
		/**
		 * Constructs an Order object for DELIVERY orders
		 */
		public Order(Restaurant rest, List<ItemRequest> i, OrderState s, int oID) {
			items = i;
			structure = rest;
			state = s;
			type = OrderType.DELIVERY;
			id = oID;
		}
		
		/**
		 * Calculates the price of an Order using the MarketManager's inventory.
		 */
		public void calculatePrice() {
			float total = 0.0f;
			for (ItemRequest item : items) {
				InventoryEntry entry = inventory.get(item.item.toLowerCase());
				if (entry != null) {
					total += item.amount * entry.price;
				}
			}
			totalPrice = total;
		}

	}
	
	enum CarOrderState {PENDING, PROCESSING, READY, FINISHED};
	int nextCarOrderID = 0;
	private class CarOrder {
		/**
		 * The MarketConsumer who ordered the car
		 */
		MarketConsumer consumer;
		CarOrderState  state;
		int			   id;
		
		private CarOrder(MarketConsumer c, CarOrderState s, int oID) {
			consumer = c;
			state = s;
			id = oID;
		}
	}
	
	enum EmployeeState {AVAILABLE, BUSY}
	private class MyEmployee {
		MarketEmployee employee;
		EmployeeState state;
		
		public MyEmployee(MarketEmployee e, EmployeeState s) {
			employee = e;
			state = s;
		}
	}
	
	/*
	 * ********** CONSTRUCTORS **********
	 */
	
	public MarketManagerRole() {
		this("", null);
	}
	
	public MarketManagerRole(MarketStructure s) {
		this("", s);
	}
	
	public MarketManagerRole(String n, MarketStructure s) {
		name = n;
		structure = s;
	}
	
	/*
	 * ********** SCHEDULER **********
	 */
	
	public boolean pickAndExecuteAnAction() {
		// If its time to leave, leave
		if (timeToLeave) {
			boolean inPersonOrder = false;
			for (Order order : orders) {
				if (order.type == OrderType.INPERSON && order.state != OrderState.SENT) {
					inPersonOrder = true;
					break;
				}
			}
			if (!inPersonOrder) {
				leaveMarket();
				return true;
			}
		}
		
		// Used for searching through orders and employees...
		Order order = null;
		CarOrder carOrder = null;
		MyEmployee employee = null;
		
		// If we have any FAILED orders, check to see if the restaurant is open now and resend them
		order = null;
		synchronized (orders) {
			for (Order o : orders) {
				if (o.state == OrderState.FAILED) {
					order = o;
				}
			}
		}
		if (order != null) { // if we have an order that has FAILED
			dispatchDeliveryTruckForOrder(order);
		}
		
		// Dispatch a ready order
		order = null;
		synchronized (orders) {
			for (Order o : orders) {
				if (o.state == OrderState.READY) {
					order = o;
					break;
				}
			}
		}
		if (order != null) { // if we found an order that is READY
			dispatchOrder(order);
			return true;
		}
		
		// Process the next available order
		order = null;
		synchronized (orders) {
			for (Order o : orders) {
				if (o.state == OrderState.PENDING) {
					order = o;
					break;
				}
			}
		}
		employee = null;
		for (MyEmployee e : employees) {
			if (e.state == EmployeeState.AVAILABLE) {
				employee = e;
			}
		}
		if (employee != null && order != null) {	// if we found an order and an available employee,
			// process the order
			processOrder(order, employee);
			return true;
		}
		
		// Process the next available car order
		carOrder = null;
		synchronized (carOrders) {
			for (CarOrder co : carOrders) {
				if (co.state == CarOrderState.PENDING) {
					carOrder = co;
				}
			}
		}
		employee = null;
		for (MyEmployee e : employees) {
			if (e.state == EmployeeState.AVAILABLE) {
				employee = e;
			}
		}
		if (employee != null && carOrder != null) {	// if we found an order and an available employee,
			// process the car order
			processCarOrder(carOrder, employee);
			return true;
		}
		
		// Dispatch a ready car that has been brought to the front
		carOrder = null;
		synchronized (carOrders) {
			for (CarOrder co : carOrders) {
				if (co.state == CarOrderState.READY) {
					carOrder = co;
					break;
				}
			}
		}
		if (carOrder != null) { // if we found a car order that is READY, that is, it has been brought to the front
			giveCarToConsumer(carOrder);
			return true;
		}
		
		return false;
	}
	
	/*
	 * ********** MESSAGES **********
	 */
	
	/**
	 * Sent by MarketConsumers in-person at a Market
	 */
	public void msgHereIsMyOrder(MarketConsumer consumer, List<ItemRequest> items) {
		AlertLog.getInstance().logMessage(AlertTag.MARKET, "Market manager " + name, "Just got a new order from a consumer.");
		
		// Add the new order to the list of orders
		synchronized(orders) {
			orders.add(new Order(consumer, items, OrderState.PENDING, nextOrderID));
			nextOrderID++;
		}

		stateChanged();
	}
		
	/**
	 * Sent by a restaurant's cook to order food.
	 * @param structure The requesting Restaurant's structure
	 * @param item An ItemRequest
	 */
	public void msgHereIsMyOrderForDelivery(Restaurant restaurant, ItemRequest item) {
		AlertLog.getInstance().logMessage(AlertTag.MARKET, "Market manager " + name, "Just got a new order from a restaurant.");
		
		List<ItemRequest> items = new ArrayList<ItemRequest>();
		items.add(item);
		
		// Add the new order to the list of orders
		synchronized(orders) {
			orders.add(new Order(restaurant, items, OrderState.PENDING, nextOrderID));
			nextOrderID++;
		}
		
		stateChanged();
	}
	
	/**
	 * Sent by a MarketConsumer to buy a new car.
	 * @param consumer The MarketConsumer who wants a new set of wheels.
	 */
	public void msgIWouldLikeACar(MarketConsumer consumer) {
		AlertLog.getInstance().logMessage(AlertTag.MARKET, "Market manager " + name, "A consumer just told me he wants a car.");
		
		synchronized(carOrders) {
			carOrders.add(new CarOrder(consumer, CarOrderState.PENDING, nextCarOrderID));
			nextCarOrderID++;
		}
		
		stateChanged();
	}
	
	/**
	 * Sent by a structure to pay a bill.
	 * @param structure The structure in debt.
	 * @param amount The amount to put towards the outstanding balance.
	 */
	public void msgHereIsMyPayment(Structure structure, float amount) {
		AlertLog.getInstance().logMessage(AlertTag.MARKET, "Market manager " + name, String.format("Just received a payment of $.2f from structure " + structure.getId(), amount));
		
		// Pay the structure's balance
		StructureRecord record = structureBalance.get(structure);
		if (record != null) {
			record.balance -= amount;
		}
		
		// Add the money to our account
		this.structure.addMoney((double)amount);
		
		stateChanged();
	}
	
	/**
	 * Sent by a MarketConsumer to pay a bill.
	 * @param consumer The MarketConsumer in debt.
	 * @param amount The amount to put towards the outstanding balance.
	 */
	public void msgHereIsMyPayment(MarketConsumer consumer, float amount) {
		AlertLog.getInstance().logMessage(AlertTag.MARKET, "Market manager " + name, String.format("Just received a payment of $.2f from a market consumer. ", amount));
		
		// Pay the consumer's balance
		ConsumerRecord record = consumerBalance.get(consumer);
		if (record != null) {
			record.balance -= amount;
		}
		
		stateChanged();
	}
	
	/**
	 * Sent by a DeliveryTruck when a restaurant is closed and cannot accept deliveries.
	 * @param id 
	 * 
	 */
	public void msgDeliveryFailed(int deliveryID) {
		AlertLog.getInstance().logMessage(AlertTag.MARKET, "Market manager " + name, String.format("Was just notified that delivery " + deliveryID + " failed."));
		
		// Find the order in our list of orders
		Order order = null;
		for (Order o : orders) {
			if (o.id == deliveryID) {
				order = o;
			}
		}
		if (order == null)
			return;
		
		/* The delivery truck wasn't able to deliver the goods, probably because the restaurant was closed. Mark the
		 * order as FAILED so we can try again when the restaurant is open.
		 */
		order.state = OrderState.FAILED;
		stateChanged();
	}
	
	public void msgHereAreItems(MarketEmployee employee, List<ItemRequest> items, int id) {		
		AlertLog.getInstance().logMessage(AlertTag.MARKET, "Market manager " + name, "Just got the requested items from my employee.");
		
		// Find the consumer's order in our list
		Order theOrder = null;
		synchronized (orders) {
			for (Order o : orders) {
				if (o.id == id) {
					theOrder = o;
					break;
				}
			}
		}
		if (theOrder == null) return;
		
		// Set his items to the ones the employee returned
		theOrder.items = items;
		
		// The order is now ready to be shipped
		theOrder.state = OrderState.READY;
		
		// The employee can process other orders
		MyEmployee myEmployee = null;
		for (MyEmployee e : employees) {
			if (e.employee == employee) {
				myEmployee = e;
			}
		}
		if (myEmployee != null) myEmployee.state = EmployeeState.AVAILABLE;
		
		stateChanged();
	}
	
	public void msgHereIsCar(MarketEmployee employee, int id) {
		AlertLog.getInstance().logMessage(AlertTag.MARKET, "Market manager " + name, "Just got the requested car from my employee.");
		
		// Find the CarOrder in our list
		CarOrder theOrder = null;
		synchronized(carOrders) {
			for (CarOrder co : carOrders) {
				if (co.id == id) {
					theOrder = co;
					break;
				}
			}
		}
		if (theOrder == null) return;
		
		// The car is now READY to be given to the consumer
		theOrder.state = CarOrderState.READY;
		
		// The employee can process other orders
		MyEmployee myEmployee = null;
		for (MyEmployee e : employees) {
			if (e.employee == employee) {
				myEmployee = e;
			}
		}
		if (myEmployee != null) myEmployee.state = EmployeeState.AVAILABLE;
		
		stateChanged();
	}
	
	public void startInteraction(Intention intent) {
		// animate inside market
		this.gui.setPresent(true);
		timeToLeave = false;
	}

	public void msgClosingTime() {
		timeToLeave = true;
		stateChanged();
	}
	
	public void animationFinished() {
		animation.release();
	}
	
	/*
	 * ********** ACTIONS **********
	 */
	
	private void processOrder(Order o, MyEmployee e) {
		// We're going to assemble a list of valid ItemRequests to our MarketEmployee
		List<ItemRequest> itemList = new ArrayList<ItemRequest>();
		
		// Go through each PersonItem in the order, check to see if we sell it, and check to see if we have any in stock
		for (ItemRequest item : o.items) {
			int amountWeHave = AmountInStock(item);
			if (amountWeHave > 0) {
				if (item.amount > amountWeHave) {
					item.amount = amountWeHave;
				}
				subtractInventory(item.item, item.amount);
				itemList.add(item);
			}
		}
		
		// Mark the order as being processed
		o.state = OrderState.PROCESSING;
		
		// Send the employee a message to retrieve the items
		e.state = EmployeeState.BUSY;
		e.employee.msgRetrieveItems(this, itemList, o.id);
		
	}
	
	/**
	 * Messages the employee to get a car and bring it to the front.
	 * @param co The CarOrder to be processed
	 * @param e The MyEmployee to do the processing
	 */
	private void processCarOrder(CarOrder co, MyEmployee e) {
		// Mark the order as being processed
		co.state = CarOrderState.PROCESSING;
		
		// Send the employee a message to retrieve the car
		e.state = EmployeeState.BUSY;
		e.employee.msgRetrieveCar(this, co.id);
	}
	
	/**
	 * Depending on the type of order, send the items on their way and charge whoever is responsible.
	 * @param o The order to dispatch
	 */
	private void dispatchOrder(Order o) {
		
		// Calculate the price of the order first
		o.calculatePrice();
		
		// Dispatch the order based on its type
		if (o.type == OrderType.INPERSON) {
			
			// The consumer is standing right there, so just give him the items
			o.consumer.msgHereAreYourItems(o.items);
			
			// The order has been sent
			o.state = OrderState.SENT;
			
		} else if (o.type == OrderType.DELIVERY) {
				
			/* Before we dispatch a truck, let's check to make sure the restaurant is even open! This is done in the
			 * dispatchDeliveryTruckForOrder() function. If the restaurant isn't open, we'll mark
			 * the order as FAILED so we'll try again later.
			 * Note: the delivery could still fail, if the restaurant closes in the time it takes the truck to arrive
			 */
			
			// Dispatch a truck
			dispatchDeliveryTruckForOrder(o);
		}	
		
		// The purchaser needs to pay for the order
		if (o.type == OrderType.INPERSON) {
						
			// Charge the order to the consumer's balance
			chargeConsumer(o.consumer, o.totalPrice);
			
			// Let him know how much he was charged
			o.consumer.msgHereIsYourTotal(this, o.totalPrice);
			
		} else if (o.type == OrderType.DELIVERY) {

			// The delivery truck will bill the market when it delivers...
						
			// Charge the order to the structure's balance
			StructureRecord record = structureBalance.get(o.structure);
			if (record != null) {
				structureBalance.get(o.structure).balance += o.totalPrice;
			} else {
				structureBalance.put(o.structure, new StructureRecord(o.structure, o.totalPrice));
			}
			
		}
	}
	
	/**
	 * Checks to see if the order's structure is open. If it is, this function dispatches
	 * a delivery truck to send the order to the restaurant. This function will mark the order as
	 * SENT - if we were able to dispatch the delivery truck, or
	 * FAILED - if we were unable to dispatch the truck
	 * @param o The order to dispatch via truck
	 * @return True if we were able to dispatch the order, False if something went wrong
	 */
	private boolean dispatchDeliveryTruckForOrder(Order o) {
		if (structure != null && o.structure != null && o.structure.getOpen()) {
		//if (structure != null && o.structure != null) {
			// Get our delivery truck
			TruckAgent deliveryTruck = structure.getNextDeliveryTruck();
		
			// Tell him to make a run
			deliveryTruck.msgMakeDeliveryRun(o.items, o.structure, o.totalPrice, o.id);
			
			// The order has now been "SENT"
			o.state = OrderState.SENT;
			return true;
		}
		
		// If we don't have a pointer to our structure we don't have a delivery truck
		// Or, the structure isn't open
		o.state = OrderState.FAILED;
		return false;
	}
	
	/**
	 * After the MarketEmployee has brought out a car, this function will hand the car over to the consumer who
	 * purchased it.
	 */
	private void giveCarToConsumer(CarOrder co) {
		// First lets charge the consumer
		// TODO this is a fixed price, we need to change this
		chargeConsumer(co.consumer, CARPRICE);
		
		// Let the consumer know how much he was charged
		co.consumer.msgHereIsYourTotal(this, CARPRICE);
		
		// Create a new vehicle
		CarAgent newCar = new CarAgent();
		if(CityPanel.INSTANCE != null)
		{
			CarGui gui = new CarGui(newCar, CityPanel.INSTANCE);
			newCar.setGui(gui);
			CityPanel.INSTANCE.addGui(gui);
		}
		newCar.startThread();
		
		// Give him his BRAND NEW CAR!
		co.consumer.msgHereIsYourCar(newCar);
		
		// The order is finished
		co.state = CarOrderState.FINISHED;
	}
	
	private void leaveMarket() {
		System.out.println("leaveMarket() called");
		// Message all the employees and let them know its time to go home
		for (MyEmployee employee : employees) {
			employee.employee.msgClosingTime();
		}
		
		// Remove all the employees from my list (they'll be back tomorrow)
		employees.clear();
				
		gui.doLeaveMarket();
		pauseForAnimation();
		
		this.isActive = false;
		this.myPerson.goOffWork();
		this.myPerson.removeRole(this);
		this.myPerson = null;
		gui.setPresent(false);
	}

	/*
	 * ********** UTILITY **********
	 */
	
	public void pauseForAnimation() {
		try {
			animation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void addEmployee(MarketEmployee e) {
		employees.add(new MyEmployee(e, EmployeeState.AVAILABLE));
	}
	
	/**
	 * Removes a MarketEmployee from this MarketManager's list of employees, effectively firing him.
	 * @param employee The MarketEmployee to be removed.
	 */
	public void removeEmployee(MarketEmployee employee) {
		// Search my employee list until I find this employee, then remove him
		for (MyEmployee thisEmployee : employees) {
			if (thisEmployee.employee == employee) {
				employees.remove(thisEmployee);
				break;
			}
		}
	}
	
	public void addInventoryEntry(InventoryEntry entry) {
		// Ensure that we store the item as lowercase
		entry.item = entry.item.toLowerCase();
		
		// Add the inventory entry
		inventory.put(entry.item, entry);
		
		// Update the structure config panel
		structure.updateConfigPanel();
	}
	
	private void subtractInventory(String item, int amountToDecrease) {
		// Update our inventory database
		InventoryEntry entry = inventory.get(item.toLowerCase());
		if (entry != null) {
			entry.amount -= amountToDecrease;
		}
		
		// Update the structure config panel
		structure.updateConfigPanel();
	}
	
	public void setGui(MarketManagerGui g) {
		gui = g;
	}
	
	/*
	 * Returns how many of a certain item we have in stock, up to the consumer's initial request
	 */
	private int AmountInStock(ItemRequest item) {
		String lowercaseItemName = item.item.toLowerCase();
		InventoryEntry entry = inventory.get(lowercaseItemName);
		if (entry == null) return 0;
		if (entry.amount >= item.amount)
			return item.amount;
		else
			return entry.amount;
	}
	
	/**
	 * Gets the MarketManager's list of employees.
	 * @return A List<MarketEmployee> of the currently employed MarketEmployees by this MarketManager.
	 */
	public List<MarketEmployee> getEmployees() {
		List<MarketEmployee> employeeList = new ArrayList<MarketEmployee>();
		for (MyEmployee employee : employees) {
			employeeList.add(employee.employee);
		}
		return employeeList;
	}
	
	/**
	 * Returns the MarketManager's current inventory.
	 */
	public List<InventoryEntry> getInventory() {
		List<InventoryEntry> inventoryList = new ArrayList<InventoryEntry>();
		for (Map.Entry<String, InventoryEntry> entry : inventory.entrySet()) {
			inventoryList.add(entry.getValue());
		}
		return inventoryList;
	}
	
	/**
	 * This function either adds an amount to a consumer's balance, or it creates a new ConsumerRecord
	 * with amount and adds it to the map.
	 * @param consumer The MarketConsumer to charge
	 * @param amount The amount they should be charged
	 */
	private void chargeConsumer(MarketConsumer consumer, float amount) {
		ConsumerRecord record = consumerBalance.get(consumer);
		if (record != null) {
			record.balance += amount;
		} else {
			consumerBalance.put(consumer, new ConsumerRecord(consumer, amount));
		}
	}
	
	/**
	 * Gets an unfulfilled MarketEmployeeRole from this manager's list of employees.
	 * An unfulfilled role is one without a PersonAgent.
	 * @return A MarketEmployeeRole if there is a job available, null if not
	 */
	public MarketEmployeeRole getUnfulfilledEmployeeRole() {
		// TODO
		return null;
	}
	
}
