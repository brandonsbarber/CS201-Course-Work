package cs201.roles.marketRoles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs201.agents.PersonAgent.Intention;
import cs201.interfaces.roles.market.MarketConsumer;
import cs201.interfaces.roles.market.MarketEmployee;
import cs201.interfaces.roles.market.MarketManager;
import cs201.roles.Role;

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
	List<MyEmployee> employees = new ArrayList<MyEmployee>();
	Map<MarketConsumer, ConsumerRecord> consumerBalance = 
			new HashMap<MarketConsumer, ConsumerRecord>();
	Map<String, InventoryEntry> inventory = new HashMap<String, InventoryEntry>();
	
	public static class ItemRequest {
		String item;
		int amount;
		
		public ItemRequest(String i, int a) {
			item = i;
			amount = a;
		}
	}
	
	public class InventoryEntry {
		String item;
		int amount;
		float price;
	}
	
	public class ConsumerRecord {
		MarketConsumer consumer;
		float balance;
	}
	
	enum OrderState {PENDING, PROCESSING, READY, SENT};
	enum OrderType {INPERSON, DELIVERY};
	int nextOrderID = 0;
	private class Order {
		List<ItemRequest> items;
		MarketConsumer consumer;
		OrderState state;
		OrderType type;
		float totalPrice;
		int id;
		
		public Order(MarketConsumer c, List<ItemRequest> i, OrderState s, OrderType t, int oID) {
			items = i;
			consumer = c;
			state = s;
			type = t;
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
		this("");
	}
	
	public MarketManagerRole(String n) {
		name = n;
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
	
	public void msgHereIsMyOrder(MarketConsumer consumer, List<ItemRequest> items) {
		System.out.println("Got message msgHereIsOrder");
		
		// Add the new order to the list of orders
		synchronized(orders) {
			orders.add(new Order(consumer, items, OrderState.PENDING, OrderType.INPERSON, nextOrderID));
			nextOrderID++;
		}

		stateChanged();
	}
	
	public void msgHereIsMyOrderForDeliery(MarketConsumer consumer, List<ItemRequest> items) {
		// Add the new order to the list of orders
		synchronized(orders) {
			orders.add(new Order(consumer, items, OrderState.PENDING, OrderType.DELIVERY, nextOrderID));
			nextOrderID++;
		}
		
		stateChanged();
	}
	
	public void msgHereIsMyPayment(MarketConsumer consumer, float amount) {
		stateChanged();
	}
	
	public void msgHereAreItems(MarketEmployee employee, List<ItemRequest> items, int id) {
		System.out.println("Got message msgHereAreItems");
		
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

	public void closingTime() {
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
	
	private void dispatchOrder(Order o) {
		// Dispatch the order based on its type
		if (o.type == OrderType.INPERSON) {
			// The consumer is standing right there, so just give him the items
			o.consumer.msgHereAreYourItems(o.items);
		} else if (o.type == OrderType.DELIVERY) {
			// The consumer wants the items delivered to him
			// TODO
		}
		
		// The order has now been sent
		o.state = OrderState.SENT;
		
		// Calculate the price of the order
		// TODO
		
		// The consumer needs to pay for the order
		// TODO
	}

	/*
	 * ********** UTILITY **********
	 */
	
	public void AddEmployee(MarketEmployee e) {
		employees.add(new MyEmployee(e, EmployeeState.AVAILABLE));
	}
	
	public void AddInventoryEntry(InventoryEntry entry) {
		// Ensure that we store the item as lowercase
		entry.item = entry.item.toLowerCase();
		
		// Add the inventory entry
		inventory.put(entry.item, entry);
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

}
