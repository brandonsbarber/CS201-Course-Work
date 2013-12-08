package cs201.roles.restaurantRoles.Ben;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import cs201.agents.PersonAgent.Intention;
import cs201.interfaces.roles.restaurant.Ben.CashierBen;
import cs201.interfaces.roles.restaurant.Ben.CustomerBen;
import cs201.interfaces.roles.restaurant.Ben.WaiterBen;
import cs201.roles.marketRoles.MarketManagerRole.ItemRequest;
import cs201.roles.restaurantRoles.RestaurantCashierRole;
import cs201.roles.restaurantRoles.Matt.RestaurantCashierRoleMatt.Check;
import cs201.roles.restaurantRoles.Matt.RestaurantCashierRoleMatt.CheckState;
import cs201.roles.restaurantRoles.Matt.RestaurantCashierRoleMatt.CheckType;
import cs201.structures.market.MarketStructure;
import cs201.test.mock.EventLog;
import cs201.trace.AlertLog;
import cs201.trace.AlertTag;

/**
 * Restaurant Cashier Agent
 */
public class RestaurantCashierRoleBen extends RestaurantCashierRole implements CashierBen {
	
	private String name;
	public List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	public List<MarketBill> marketBills = Collections.synchronizedList(new ArrayList<MarketBill>());
	private Timer timer;
	private Map<String, Food> inventory = new HashMap<String, Food>();
	public EventLog log = new EventLog();
	private boolean closingTime = false;
	
	float netMoney = 0.0f;
	
	public RestaurantCashierRoleBen() {
		this("");
	}
	
	public RestaurantCashierRoleBen(String name) {
		super();

		this.name = name;
		
		timer = new Timer();
		
		// Set up our map of foods
		inventory.put("Chicken", new Food("Chicken", 6.99f));
		inventory.put("Steak", new Food("Steak", 11.99f));
		
		// Give the cashier starting money
		netMoney = 1000.00f;
		
	}

	/**
	 * Messages.
	 */
	
	public void msgComputeCheckForOrder(String choice, WaiterBen waiter, CustomerBen cust) {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, "Cashier " + name, "Asked to compute the check for the order.");
		
		// See if the customer owes us money already
		Order theOrder = null;
		synchronized(orders) {
			for (Order o : orders) {
				if (o.customer == cust && o.state == OrderState.outstanding)
					theOrder = o;
			}
		}
		if (theOrder != null) {
			theOrder.choice = choice;
			theOrder.waiter = waiter;
			theOrder.state = OrderState.pending;
		} else 
			theOrder = new Order(waiter, cust, choice, OrderState.pending);
		
		// Add the new order to our list of outstanding orders
		orders.add(theOrder);
		stateChanged();
	}
	
	public void msgHereIsPayment(CustomerBen cust, float amount) {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, "Cashier " + name, String.format("Got a payment of %.2f from customer", amount));
		
		// Find the customer's order
		Order theOrder = null;
		synchronized(orders) {
			for (Order o : orders) {
				if (o.customer == cust)
					theOrder = o;
			}
		}
		if (theOrder == null) return;
		
		// Mark the order as paid
		// TO-DO: check if the amount paid is enough to cover the check
		// for now, we'll just assume it is
		theOrder.state = OrderState.paid;
		
		stateChanged();
	}
	
	public void msgICantPay(CustomerBen cust) {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, "Cashier " + name, "Got msgICantPay from customer.");
		
		// Find the customer's order
		Order theOrder = null;
		synchronized(orders) {
			for (Order o : orders) {
				if (o.customer == cust)
					theOrder = o;
			}
		}
		if (theOrder == null) return;
		
		// Mark the order as outstanding
		theOrder.state = OrderState.outstanding;
		
		stateChanged();
	}
	
	@Override
	public void msgHereIsDeliveryFromMarket(MarketStructure market, double amount, ItemRequest request) {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, "Cashier " + name, "Just got a delivery from market " + market.getId());
		
		// Add the bill from the Market to be paid
		marketBills.add(new MarketBill(market, amount, request, MarketBillState.pending));
		
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {

		// Closing time.. every new beginning comes from some other beginning's end
		if (closingTime) {
			LeaveRestaurant();
			return true;
		}
		
		// If any orders are ready to be calculated, calculate them
		synchronized(orders) {
			for (Order order : orders) {
				if (order.state == OrderState.pending) {
					calculateOrder(order);
					return true;
				}
			}
		}
		
		// If any markets need to be paid, pay them
		synchronized(marketBills) {
			for (MarketBill bill : marketBills) {
				if (bill.state == MarketBillState.pending) {
					payMarketBill(bill);
					return true;
				}
			}
		}
		
		/*
		 * We have tried all our rules and found nothing to do.
		 * Return false to main loop of abstract agent and wait.
		 */
		return false;

	}

	/**
	 * Actions
	 */
	private void calculateOrder(Order o) {
		// Get the total price for the order
		o.total += inventory.get(o.choice).price;
		
		// Mark the order as having been calculated
		o.state = OrderState.calculated;
		
		// Let the waiter know the check is ready
		o.waiter.msgHereIsCheck(o.total, o.customer);
	}
	
	private void payMarketBill(MarketBill bill) {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, "Cashier " + name, "Paying the bill from market " + bill.market.getId());
		
		// We need to check to see if we got what we ordered
		// TODO this
		
		// Pay the market
		bill.market.getManager().msgHereIsMyPayment(restaurant, (float)bill.totalCost);
		bill.state = MarketBillState.paid;
		
		// Give the cook the food (the ItemRequest)
		((RestaurantCookRoleBen)restaurant.getCook()).msgHereIsYourFood(bill.request);
		
		// Deduct the funds from our account
		restaurant.removeMoney(bill.totalCost);
	}
	
	private void LeaveRestaurant() {
		this.isActive = false;
		this.myPerson.removeRole(this);
		this.myPerson.goOffWork();
		this.myPerson = null;
	}

	/**
	 * Utilities
	 */
	
	public String getName() {
		return name;
	}
	
	// A class to keep track of the orders we receive.
	public enum OrderState {pending, calculated, paid, outstanding};
	public class Order {
		public WaiterBen waiter;
		public CustomerBen customer;
		public String choice;
		public OrderState state;
		float total;
		
		public Order(WaiterBen w, CustomerBen cust, String c, OrderState s) {
			waiter = w;
			customer = cust;
			choice = c;
			state = s;
		}
	}
	
	public enum MarketBillState {pending, paid};
	public class MarketBill {
		public MarketStructure market;
		double totalCost;
		ItemRequest request;
		public MarketBillState state;
		
		public MarketBill(MarketStructure m, double t, ItemRequest r, MarketBillState s) {
			market = m;
			totalCost = t;
			request = r;
			state = s;
		}
	}
	
	private class Food {
		String type;
		float price;
		
		public Food(String t, float p) {
			type = t;
			price = p;
		}
	}

	@Override
	public void startInteraction(Intention intent) {
		closingTime = false;
	}

	@Override
	public void msgClosingTime() {
		// TODO Auto-generated method stub
		closingTime = true;
		stateChanged();
	}
	
}

