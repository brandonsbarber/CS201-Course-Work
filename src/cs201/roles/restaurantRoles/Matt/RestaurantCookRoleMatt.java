package cs201.roles.restaurantRoles.Matt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.Timer;

import cs201.gui.roles.restaurant.Matt.CookGuiMatt;
import cs201.interfaces.roles.restaurant.RestaurantCookRole;
import cs201.interfaces.roles.restaurant.Matt.CookMatt;
import cs201.interfaces.roles.restaurant.Matt.WaiterMatt;

/**
 * Restaurant Cook Agent
 */
public class RestaurantCookRoleMatt extends RestaurantCookRole implements CookMatt {
	private CookGuiMatt gui = null;
	private List<Order> orders;
	private Map<String, Food> foods;
	private enum OrderState { pending, cooking, done, pickup };
	//private List<MarketAgent> markets;
	private final int FOODTHRESHOLD = 2;
	private final int MAXSTOCK = 5;
	private final int INITIALSTOCK = 3;

	public RestaurantCookRoleMatt() {
		super();

		orders = Collections.synchronizedList(new ArrayList<Order>());
		//markets = Collections.synchronizedList(new ArrayList<MarketAgent>());
		foods = Collections.synchronizedMap(new Hashtable<String, Food>());
		
		foods.put("Steak", new Food("Steak", 3500, INITIALSTOCK)); 
		foods.put("Pasta", new Food("Pasta", 3100, INITIALSTOCK));
		foods.put("Ice Cream", new Food("Ice Cream", 1900, INITIALSTOCK));
		foods.put("Pizza", new Food("Pizza", 2800, INITIALSTOCK));
		foods.put("Chicken", new Food("Chicken", 3300, INITIALSTOCK));
		foods.put("Salad", new Food("Salad", 2100, INITIALSTOCK));
	}
	
	public void setGui(CookGuiMatt gui) {
		this.gui = gui;
	}
	
	// Messages -------------------------------------------------------------
	@Override
	public void msgHereIsAnOrder(WaiterMatt w, String choice, int tableNum) {
		orders.add(new Order(choice, (RestaurantWaiterRoleMatt) w, tableNum));
		stateChanged();
	}
	
	@Override
	public void msgFulfillSupplyOrder(String type, int amount) {
		Food temp = foods.get(type);
		temp.quantity += amount;
		temp.orderPending = false;
		if (temp.amountOrdered > amount || amount == 0) {
			temp.nextMarket++;
		}
		stateChanged();
	}
	
	@Override
	public void msgOrderPickedUp(String type) {
		gui.removePlatingItem(type);
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {		
		// If there is something to do'
		
		// If food needs to be ordered
		/*synchronized(foods) {
			for (String f : foods.keySet()) {
				Food temp = foods.get(f);
				if (!temp.orderPending && temp.quantity < FOODTHRESHOLD && temp.nextMarket < markets.size()) {
					OrderFood(temp);
					return true;
				}
			}
		}*/
		
		synchronized(orders) {
			for (Order o : orders) {
				if (o.state == OrderState.done) {
					InformWaiter(o);
					return true;
				}
			}
		}
		synchronized(orders) {
			for (Order o : orders) {
				if (o.state == OrderState.pending) {
					CookOrder(o);
					return true;
				}
			}
		}
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions -------------------------------------------------------------
	private void CookOrder(Order o) {
		Food f = foods.get(o.choice);
		if (f.quantity == 0) {
			DoOutOfFood(f);
			o.waiter.msgOutOfFood(f.type, o.tableNum);
			orders.remove(o);
			return;
		}
		f.quantity--;
		
		DoCookOrder(o);
		o.state = OrderState.cooking;
		o.CookOrder(foods.get(o.choice).cookTime);
	}
	
	private void InformWaiter(Order o) {
		DoInformWaiter(o);
		o.state = OrderState.pickup;
		o.waiter.msgOrderIsReady(o.choice, o.tableNum);
		orders.remove(o);
	}
	
	private void OrderFood(Food f) {
		DoOrderFood(f);
		f.amountOrdered = MAXSTOCK - f.quantity;
		//markets.get(f.nextMarket).msgOrderSupplies(f.type, f.amountOrdered);
		f.orderPending = true;
	}

	// Utilities -------------------------------------------------------------
	private void DoCookOrder(Order o) {
		System.out.println("Cook " + this.toString() + " cooking " + o.toString() + " for " + foods.get(o.choice).cookTime);
		gui.addCookingItem(o.choice);
	}
	
	private void DoOutOfFood(Food f) {
		System.out.println("Cook " + this.toString() + " out of " + f.type + "!");
	}
	
	private void DoInformWaiter(Order o) {
		System.out.println("Cook " + this.toString() + " telling waiter " + o.waiter.toString() + " that order " + o.toString() + " is ready.");
		gui.removeCookingItem(o.choice);
		gui.addPlatingItem(o.choice);
	}
	
	private void DoOrderFood(Food f) {
		System.out.println("Cook " + this.toString() + " ordering " + f.type + " from Market " + f.nextMarket + ".");
	}
	
	/**
	 * Adds a MarketAgent to this CookAgent
	 * @param m The MarketAgent being added to this CookAgent's list of MarketAgents
	 */
	/*public void addMarket(MarketAgent m) {
		markets.add(m);
	}*/
	
	public void emptyInventory() {
		synchronized(foods) {
			for (String f : foods.keySet()) {
				Food temp = foods.get(f);
				temp.quantity = 0;
			}
		}
		stateChanged();
	}
	
	public void emptySteakInventory() {
		foods.get("Steak").quantity = 0;
		stateChanged();
	}
	
	private class Food {
		private String type;
		private int cookTime;
		private int quantity;
		private int nextMarket;
		private boolean orderPending;
		private int amountOrdered;
		
		public Food(String type, int cookTime, int initialQuantity) {
			this.type = type;
			this.cookTime = cookTime;
			this.quantity = initialQuantity;
			this.nextMarket = 0;
			this.orderPending = false;
			this.amountOrdered = 0;
		}
	}
	
	private class Order {
		private RestaurantWaiterRoleMatt waiter;
		private String choice;
		private int tableNum;
		private OrderState state;
		private Timer cookTimer;
		
		public Order(String choice, RestaurantWaiterRoleMatt waiter, int tableNumber) {
			this.waiter = waiter;
			this.choice = choice;
			this.tableNum = tableNumber;
			this.state = OrderState.pending;
		}
		
		public void CookOrder(int howLong) {
			cookTimer = new Timer(howLong, 
					new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					state = OrderState.done;
					stateChanged();
				}
			});
			cookTimer.setRepeats(false);
			cookTimer.start();
		}
		
		/**
		 * Returns a textual representation of this Order
		 * @return String representing this order
		 */
		public String toString() {
			return (choice + " for table " + tableNum);
		}
	}
}

