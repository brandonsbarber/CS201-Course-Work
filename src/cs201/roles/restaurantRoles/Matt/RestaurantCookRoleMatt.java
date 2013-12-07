package cs201.roles.restaurantRoles.Matt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.Timer;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.roles.restaurant.Matt.CookGuiMatt;
import cs201.helper.CityDirectory;
import cs201.helper.Matt.RestaurantRotatingStand;
import cs201.helper.Matt.RestaurantRotatingStand.RotatingStandOrder;
import cs201.interfaces.roles.restaurant.Matt.CookMatt;
import cs201.interfaces.roles.restaurant.Matt.WaiterMatt;
import cs201.roles.marketRoles.MarketManagerRole.ItemRequest;
import cs201.roles.restaurantRoles.RestaurantCookRole;
import cs201.structures.market.MarketStructure;
import cs201.trace.AlertLog;
import cs201.trace.AlertTag;

/**
 * Restaurant Cook Agent
 */
public class RestaurantCookRoleMatt extends RestaurantCookRole implements CookMatt, ActionListener {
	private CookGuiMatt gui = null;
	private List<Order> orders;
	private Map<String, Food> foods;
	private enum OrderState { pending, cooking, done, pickup };
	private final int FOODTHRESHOLD = 2;
	private final int MAXSTOCK = 25;
	private final int INITIALSTOCK = 20;
	private boolean closingTime = false;
	private final int STANDCHECKTIMER = 3000; // 3 seconds
	private Timer standTimer = new Timer(STANDCHECKTIMER, this);
	private RestaurantRotatingStand stand = null;

	public RestaurantCookRoleMatt() {
		super();

		orders = Collections.synchronizedList(new ArrayList<Order>());
		foods = Collections.synchronizedMap(new Hashtable<String, Food>());
		
		foods.put("Steak", new Food("Steak", 3500, INITIALSTOCK)); 
		foods.put("Pasta", new Food("Pasta", 3100, INITIALSTOCK));
		foods.put("Ice Cream", new Food("Ice Cream", 1900, INITIALSTOCK));
		foods.put("Pizza", new Food("Pizza", 2800, INITIALSTOCK));
		foods.put("Chicken", new Food("Chicken", 3300, INITIALSTOCK));
		foods.put("Salad", new Food("Salad", 2100, INITIALSTOCK));
		
		standTimer.setRepeats(true);
		standTimer.start();
	}
	
	public void setGui(CookGuiMatt gui) {
		this.gui = gui;
	}
	
	// Messages -------------------------------------------------------------
	@Override
	public void msgClosingTime() {
		closingTime = true;
		stateChanged();
	}
	
	@Override
	public void msgHereIsAnOrder(WaiterMatt w, String choice, int tableNum) {
		orders.add(new Order(choice, (RestaurantWaiterRoleMatt) w, tableNum));
		stateChanged();
	}
	
	@Override
	public void msgFulfillSupplyOrder(String type, int amount, MarketStructure from) {
		Food temp = foods.get(type);
		temp.quantity += amount;
		temp.orderPending = false;
		if (temp.amountOrdered > amount || amount == 0) {
			temp.marketsTried.add(from);
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
		// If there is something to do
		
		if (closingTime) {
			LeaveRestaurant();
			return true;
		}
		
		// If food needs to be ordered
		synchronized(foods) {
			for (String f : foods.keySet()) {
				Food temp = foods.get(f);
				if (!temp.orderPending && temp.quantity < FOODTHRESHOLD && temp.marketsTried.size() < CityDirectory.getInstance().getMarkets().size()) {
					OrderFood(temp);
					return true;
				}
			}
		}
		
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
	private void LeaveRestaurant() {
		this.isActive = false;
		this.myPerson.goOffWork();
		this.myPerson.removeRole(this);
		DoLeaveRestaurant();
		this.myPerson = null;
		this.gui.setPresent(false);
	}
	
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
		f.amountOrdered = MAXSTOCK - f.quantity;
		for (MarketStructure m : CityDirectory.getInstance().getMarkets()) {
			if (!f.marketsTried.contains(m)) {
				DoOrderFood(f, m);
				MarketStructure market = CityDirectory.getInstance().getRandomMarket();
				((RestaurantCashierRoleMatt) this.restaurant.getCashier()).msgOrderInvoiceFromCook(market, f.type, f.amountOrdered);
				market.getManager().msgHereIsMyOrderForDelivery(restaurant, new ItemRequest(f.type, f.amountOrdered));
				f.orderPending = true;
				break;
			}
		}
	}

	// Utilities -------------------------------------------------------------
	private void DoLeaveRestaurant() {
		// TODO leave restaurant animation
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), "Leaving work.");
	}
	
	private void DoCookOrder(Order o) {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), "Cooking " + o.toString() + " for " + foods.get(o.choice).cookTime + " for Table " + o.tableNum);
		gui.addCookingItem(o.choice);
	}
	
	private void DoOutOfFood(Food f) {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), "Out of " + f.type + "!");
	}
	
	private void DoInformWaiter(Order o) {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), "Telling " + o.waiter.toString() + " that order " + o.toString() + " is ready.");
		gui.removeCookingItem(o.choice);
		gui.addPlatingItem(o.choice);
	}
	
	private void DoOrderFood(Food f, MarketStructure m) {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), "Ordering " + f.type + " from " + m + ".");
	}
	
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
		private List<MarketStructure> marketsTried;
		private boolean orderPending;
		private int amountOrdered;
		
		public Food(String type, int cookTime, int initialQuantity) {
			this.type = type;
			this.cookTime = cookTime;
			this.quantity = initialQuantity;
			this.marketsTried = new ArrayList<MarketStructure>();
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
		 * Returns a textual representation of this Order (i.e. Steak for table 1)
		 * @return String representing this order
		 */
		public String toString() {
			return (choice + " for table " + tableNum);
		}
	}
	
	public CookGuiMatt getGui() {
		return this.gui;
	}
	
	public void setRotatingStand(RestaurantRotatingStand stand) {
		this.stand = stand;
	}

	@Override
	public void startInteraction(Intention intent) {
		// TODO maybe animate into restaurant?
		closingTime = false;
		this.gui.setPresent(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		RotatingStandOrder r = stand.removeOrder();
		if (r == null) {
			return;
		} else {
			orders.add(new Order(r.choice, r.waiter, r.tableNum));
			stateChanged();
		}
	}

}

