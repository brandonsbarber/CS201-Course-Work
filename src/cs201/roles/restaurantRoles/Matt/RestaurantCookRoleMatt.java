package cs201.roles.restaurantRoles.Matt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import javax.swing.Timer;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.roles.restaurant.Matt.CookGuiMatt;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelMatt;
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
	private Semaphore atTargetPosition = new Semaphore(0); // used for animation
	private CookGuiMatt gui = null;
	private List<Order> orders;
	private Map<String, Food> foods;
	private enum OrderState { pending, cooking, done, pickup };
	private final int FOODTHRESHOLD = 2;
	private final int MAXSTOCK = 25;
	private final int INITIALSTOCK = 20;
	private boolean closingTime = false;
	private final int STANDCHECKTIMER = 2000; // 3 seconds
	private Timer standTimer = new Timer(STANDCHECKTIMER, this);
	private RestaurantRotatingStand stand = null;
	private boolean checkForStandOrder = false;

	public RestaurantCookRoleMatt() {
		super();

		orders = Collections.synchronizedList(new ArrayList<Order>());
		foods = Collections.synchronizedMap(new Hashtable<String, Food>());
		
		foods.put("Steak", new Food("Steak", 1200, INITIALSTOCK)); 
		foods.put("Pasta", new Food("Pasta", 1000, INITIALSTOCK));
		foods.put("Ice Cream", new Food("Ice Cream", 640, INITIALSTOCK));
		foods.put("Pizza", new Food("Pizza", 900, INITIALSTOCK));
		foods.put("Chicken", new Food("Chicken", 1100, INITIALSTOCK));
		foods.put("Salad", new Food("Salad", 700, INITIALSTOCK));
		
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
		
		if (checkForStandOrder) {
			checkForStandOrder = false;
			GetOrderFromStand();
			return true;
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
		
		gui.goToKitchen();
		
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
	
	private void GetOrderFromStand() {
		DoGetOrderFromStand();
		RotatingStandOrder r = stand.removeOrder();
		if (r == null) {
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), "No orders to pick up.");
			return;
		} else {
			orders.add(new Order(r.choice, r.waiter, r.tableNum));
		}
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
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), "Leaving work.");
		gui.goToLocation(RestaurantAnimationPanelMatt.RESTAURANT_ENTRANCE_X, RestaurantAnimationPanelMatt.RESTAURANT_ENTRANCE_Y);
		try {
			atTargetPosition.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void DoGetOrderFromStand() {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), "Retrieving an order from the Rotating Stand.");
		gui.goToLocation(RestaurantRotatingStand.STANDX, RestaurantRotatingStand.STANDY + RestaurantRotatingStand.STANDSIZE);
		try {
			atTargetPosition.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void DoCookOrder(Order o) {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), "Cooking " + o.toString() + " for " + foods.get(o.choice).cookTime);
		gui.goToLocation(RestaurantAnimationPanelMatt.FRIDGE_X, RestaurantAnimationPanelMatt.FRIDGE_Y);
		try {
			atTargetPosition.acquire();
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		gui.goToLocation(RestaurantAnimationPanelMatt.STOVES_X, RestaurantAnimationPanelMatt.STOVES_Y);
		try {
			atTargetPosition.acquire();
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		gui.addCookingItem(o.choice);
	}
	
	private void DoOutOfFood(Food f) {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), "Out of " + f.type + "!");
	}
	
	private void DoInformWaiter(Order o) {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), "Telling " + o.waiter.toString() + " that order " + o.toString() + " is ready.");
		gui.goToLocation(RestaurantAnimationPanelMatt.STOVES_X, RestaurantAnimationPanelMatt.STOVES_Y);
		try {
			atTargetPosition.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		gui.removeCookingItem(o.choice);
		gui.goToLocation(RestaurantAnimationPanelMatt.PLATING_X, RestaurantAnimationPanelMatt.PLATING_Y);
		try {
			atTargetPosition.acquire();
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
			return (choice + " for Table " + tableNum);
		}
	}
	
	public CookGuiMatt getGui() {
		return this.gui;
	}
	
	/**
	 * The CookGui tells this CookAgent that it has reached its destination, freeing up this CookAgent
	 * to continue working
	 */
	public void DoneAnimating() {
		atTargetPosition.release();
	}
	
	public void setRotatingStand(RestaurantRotatingStand stand) {
		this.stand = stand;
	}

	@Override
	public void startInteraction(Intention intent) {
		closingTime = false;
		this.gui.setPresent(true);
		gui.goToKitchen();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (stand.getNumOrders() > 0 && !checkForStandOrder) {
			checkForStandOrder = true;
			stateChanged();
		}
	}

}

