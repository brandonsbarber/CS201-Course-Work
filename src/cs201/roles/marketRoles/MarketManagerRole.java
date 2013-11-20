package cs201.roles.marketRoles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs201.agents.PersonAgent.Intention;
import cs201.interfaces.marketInterfaces.MarketConsumer;
import cs201.interfaces.marketInterfaces.MarketEmployee;
import cs201.interfaces.marketInterfaces.MarketManager;
import cs201.roles.Role;

public class MarketManagerRole extends Role implements MarketManager {
	
	/*
	 * ********** DATA **********
	 */
	
	String name = "";
	List<Order> orders = Collections.synchronizedList( new ArrayList<Order>() );
	List<MyEmployee> employees = new ArrayList<MyEmployee>();
	Map<MarketConsumer, ConsumerRecord> consumerBalance = 
			new HashMap<MarketConsumer, ConsumerRecord>();
	Map<Item, InventoryEntry> inventory = new HashMap<Item, InventoryEntry>();
	//List<DeliveryTruck> deliveryTrucks;
	
	public class InventoryEntry {
		Item item;
		int amount;
	}
	
	public class ConsumerRecord {
		MarketConsumer consumer;
		float balance;
	}
	
	public class Item {
		String type;
		float price;
	}
	
	enum OrderState {PENDING, PROCESSING, READY, SENT};
	enum OrderType {INPERSON, DELIVERY};
	int nextOrderID = 0;
	private class Order {
		List<Item> items;
		MarketConsumer consumer;
		OrderState state;
		OrderType type;
		float totalPrice;
		int id;
		
		public Order(MarketConsumer c, List<Item> i, OrderState s, OrderType t, int oID) {
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
			ProcessOrder(order, employee);
			return true;
		}
		
		return false;
	}
	
	/*
	 * ********** MESSAGES **********
	 */
	
	public void msgHereIsMyOrder(MarketConsumer consumer, List<Item> items) {
		// Add the new order to the list of orders
		synchronized(orders) {
			orders.add(new Order(consumer, items, OrderState.PENDING, OrderType.INPERSON, nextOrderID));
			nextOrderID++;
		}

		stateChanged();
	}
	
	public void msgHereIsMyOrderForDeliery(MarketConsumer consumer, List<Item> items) {
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
	
	public void msgHereAreItems(MarketEmployee employee, List<Item> items, int id) {
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
		
		// Set his items to the ones the employee returned
		theOrder.items = items;
		
		// The order is now ready to be shipped
		theOrder.state = OrderState.READY;
		
		// The employee can process other orders
		MyEmployee myEmployee = null;
		for (MyEmployee e : employees) {
			if (e == employee) {
				myEmployee = e;
			}
		}
		myEmployee.state = EmployeeState.AVAILABLE;
		
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
	
	private void ProcessOrder(Order o, MyEmployee e) {
		// Send the employee a message to retrieve the items
		e.employee.msgRetrieveItems(this, o.items, o.id);
		e.state = EmployeeState.BUSY;
		
		// Mark the order as being processed
		o.state = OrderState.PROCESSING;
	}

	/*
	 * ********** UTILITY **********
	 */
	
	public void AddEmployee(MarketEmployee e) {
		employees.add(new MyEmployee(e, EmployeeState.AVAILABLE));
	}

}
