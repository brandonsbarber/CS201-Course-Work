package cs201.roles.marketRoles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs201.agents.PersonAgent.Intention;
import cs201.agents.transit.TruckAgent;
import cs201.gui.roles.market.MarketManagerGui;
import cs201.interfaces.roles.market.MarketConsumer;
import cs201.interfaces.roles.market.MarketEmployee;
import cs201.interfaces.roles.market.MarketManager;
import cs201.roles.Role;
import cs201.structures.Structure;
import cs201.structures.market.MarketStructure;
import cs201.structures.restaurant.Restaurant;

/**
 * The MarketManagerRole, the head of a market. Deals directly with customers and money. Dispatches MarketEmployees to retrieve items
 * @author Ben Doherty
 */
public class MarketManagerRole extends Role implements MarketManager {
	
	/*
	 * ********** DATA **********
	 */
	
	String name = "";
	public List<Order> orders = Collections.synchronizedList( new ArrayList<Order>() );
	public List<MyEmployee> employees = new ArrayList<MyEmployee>();
	Map<MarketConsumer, ConsumerRecord> consumerBalance = new HashMap<MarketConsumer, ConsumerRecord>();
	Map<Structure, StructureRecord> structureBalance = new HashMap<Structure, StructureRecord>();
	Map<String, InventoryEntry> inventory = new HashMap<String, InventoryEntry>();
	MarketManagerGui gui;
	MarketStructure structure;
	
	public static class ItemRequest {
		public String item;
		public int amount;
		
		public ItemRequest(String i, int a) {
			item = i;
			amount = a;
		}
	}
	
	public static class InventoryEntry {
		String item;
		int amount;
		float price;
		
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
		MarketConsumer consumer;
		float balance;
	}
	
	/**
	 * Holds the balance for a single Structure
	 */
	public class StructureRecord {
		Structure structure;
		float balance;
	}
	
	enum OrderState {PENDING, PROCESSING, READY, SENT};
	enum OrderType {INPERSON, DELIVERY};
	int nextOrderID = 0;
	private class Order {
		List<ItemRequest> items;
		MarketConsumer consumer = null;		// For INPERSON orders
		Structure structure = null;			// For DELIVERY orders
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
		public Order(Structure struct, List<ItemRequest> i, OrderState s, int oID) {
			items = i;
			structure = struct;
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
				InventoryEntry entry = inventory.get(item.item);
				if (entry != null) {
					total += item.amount * entry.price;
				}
			}
			totalPrice = total;
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
	
	public MarketManagerRole(String n, MarketStructure s) {
		name = n;
		structure = s;
	}
	
	/*
	 * ********** SCHEDULER **********
	 */
	
	public boolean pickAndExecuteAnAction() {
		// Process the next available order
		Order order = null;
		synchronized (orders) {
			for (Order o : orders) {
				if (o.state == OrderState.PENDING) {
					order = o;
				}
			}
		}
		MyEmployee employee = null;
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
		
		// Dispatch a ready order
		order = null;
		synchronized (orders) {
			for (Order o : orders) {
				if (o.state == OrderState.READY) {
					order = o;
				}
			}
		}
		if (order != null) { // if we found an order that is READY
			dispatchOrder(order);
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
	 * @param items A list of items
	 */
	public void msgHereIsMyOrderForDelivery(Structure structure, List<ItemRequest> items) {
		// Add the new order to the list of orders
		synchronized(orders) {
			orders.add(new Order(structure, items, OrderState.PENDING, nextOrderID));
			nextOrderID++;
		}
		
		stateChanged();
	}
	
	public void msgHereIsMyPayment(MarketConsumer consumer, float amount) {
		// Pay the consumer's balance
		ConsumerRecord record = consumerBalance.get(consumer);
		if (record != null) {
			record.balance -= amount;
		}
		
		stateChanged();
	}
	
	public void msgHereAreItems(MarketEmployee employee, List<ItemRequest> items, int id) {		
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
	
	public void startInteraction(Intention intent) {
		// TODO Auto-generated method stub
		
	}

	public void msgClosingTime() {
		// TODO Auto-generated method stub
		
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
				item.amount = amountWeHave;
				itemList.add(item);
			}
		}
		
		// Mark the order as being processed
		o.state = OrderState.PROCESSING;
		
		// Send the employee a message to retrieve the items
		e.employee.msgRetrieveItems(this, itemList, o.id);
		e.state = EmployeeState.BUSY;
		
	}
	
	/**
	 * Depending on the type of order, send the items on their way and charge whoever is responsible
	 * @param o
	 */
	private void dispatchOrder(Order o) {
		// Dispatch the order based on its type
		if (o.type == OrderType.INPERSON) {
			
			// The consumer is standing right there, so just give him the items
			o.consumer.msgHereAreYourItems(o.items);
			
		} else if (o.type == OrderType.DELIVERY) {
			
			// The consumer wants the items delivered to him
			TruckAgent deliveryTruck = structure.getDeliveryTruck();
			deliveryTruck.msgMakeDeliveryRun(o.items, o.structure);
			
		}
		
		// The order has now been sent
		o.state = OrderState.SENT;
		
		// Calculate the price of the order
		o.calculatePrice();
		
		// The purchaser needs to pay for the order
		if (o.type == OrderType.INPERSON) {
			
			o.consumer.msgHereIsYourTotal(this, o.totalPrice);
			consumerBalance.get(o.consumer).balance += o.totalPrice;
			
		} else if (o.type == OrderType.DELIVERY) {
			
			// TODO
			// structure.getCashier().msgHereIsTotal
			structureBalance.get(o.structure).balance += o.totalPrice;
			
		}
	}

	/*
	 * ********** UTILITY **********
	 */
	
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
	
	public void AddInventoryEntry(InventoryEntry entry) {
		// Ensure that we store the item as lowercase
		entry.item = entry.item.toLowerCase();
		
		// Add the inventory entry
		inventory.put(entry.item, entry);
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

}
