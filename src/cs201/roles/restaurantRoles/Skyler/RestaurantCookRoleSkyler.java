package cs201.roles.restaurantRoles.Skyler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cs201.agents.PersonAgent.Intention;
import cs201.helper.CityDirectory;
import cs201.interfaces.roles.restaurant.Skyler.CookSkyler;
import cs201.interfaces.roles.restaurant.Skyler.WaiterSkyler;
import cs201.roles.marketRoles.MarketManagerRole.ItemRequest;
import cs201.roles.restaurantRoles.RestaurantCookRole;
import cs201.structures.market.MarketStructure;

public class RestaurantCookRoleSkyler extends RestaurantCookRole implements
		CookSkyler {
	
	private List<Order> orders
	= Collections.synchronizedList(new ArrayList<Order>());
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	//private Timer timer = new Timer();
	private Map <String, Food> inventory = new HashMap <String, Food>();
	private String name;
	private static int orderQuantity = 10; // number of food items to be ordered at one time when an order is placed.
	private int defaultAmt = 100; //100 of each food to begin with
	
	private int NMARKETS;
	
	private boolean closingTime = false;

	public enum OrderState {arrived, rejected, cooking, doneCooking, plated, waiterNotified, waiterHere, gone};

	public RestaurantCookRoleSkyler() {
		this("");
	}
	public RestaurantCookRoleSkyler(String name) {
		super();

		this.name = name;
		
		inventory.put("Steak", new Food("Steak", 5000, defaultAmt));
		inventory.put("Chicken", new Food("Chicken", 4000, defaultAmt));
		inventory.put("Salad", new Food("Salad", 2000, defaultAmt));
		inventory.put("Pizza", new Food("Pizza", 6000, defaultAmt));
	}
	

	@Override
	public void msgHereIsOrder(WaiterSkyler w, String choice, int tableNum) {
		Do(""+choice+" added to order list.");
		orders.add(new Order(w, choice, tableNum));
		stateChanged();
	}

	@Override
	public void msgImBackFor(int tableNum) {
		for (Order o : orders) {
			if (o.tableNumber == tableNum && o.state != OrderState.gone) {
				o.state = OrderState.waiterHere;
				stateChanged();
			}
		}
	}
	
	public void msgHereIsDelivery(String item, int amount) {
		Do("Just received "+amount+" of "+item+" from a delivery.");
		inventory.get(item).addFromOrder(amount);
	}

	@Override
	public void startInteraction(Intention intent) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean pickAndExecuteAnAction() {
		if (closingTime) {
			leaveRestaurant();
			return true;
		}
		for (Order order : orders) {
			if (order.state==OrderState.arrived) {
				CookOrder(order);
				return true;
			}
			if (order.state==OrderState.rejected){
				sendBackOrder(order);
				orders.remove(order);
				return true;
			}
			if (order.state==OrderState.doneCooking) {
				PlateIt(order);
				return true;
			}
			if (order.state==OrderState.plated){
				CallWaiter(order);
				return true;
			}
			if (order.state==OrderState.waiterHere){
				order.waiter.msgHereIsFood(order.choice, order.tableNumber);
				order.state = OrderState.gone;
				return true;
			}
		}
		checkInventory();
		return false;
	}

	@Override
	public void msgClosingTime() {
		closingTime = true;
		stateChanged();

	}
	
	private void checkInventory() {
		if(CityDirectory.getInstance().getMarkets().isEmpty()) {
			Do("No markets = no food!!!!");
			return;
		}
		
		Food tempFood = inventory.get("Steak");
		if(tempFood.amount < 3){
			if(tempFood.amtOrdered==0) {
				newRestockOrder(tempFood, orderQuantity);
			}
		}
		tempFood = inventory.get("Salad");
		if(tempFood.amount < 3) {
			if (tempFood.amtOrdered==0) {
				newRestockOrder(tempFood, orderQuantity);
			}
		}
		tempFood = inventory.get("Pizza");
		if(tempFood.amount < 3) {
			if (tempFood.amtOrdered==0) {
				newRestockOrder(tempFood, orderQuantity);
			}
		}
		tempFood = inventory.get("Chicken");
		if(tempFood.amount < 3) {
			if (tempFood.amtOrdered==0) {
				newRestockOrder(tempFood, orderQuantity);
			}
		}
	}
	
	public List<String> getInventory() {
		List<String> formattedInventory = new ArrayList<String>();
		String tempString;
		
		if (inventory.isEmpty()) {
			return null;
		}
		
		for (Map.Entry<String, Food> entry : inventory.entrySet())
		{
		    tempString = entry.getKey() + " [" + entry.getValue().amount + "]";
		    formattedInventory.add(tempString);
		}
		
		return formattedInventory;
	}
	
	public void clearInventory() {
		for (Map.Entry<String, Food> entry : inventory.entrySet()) {
			entry.getValue().clear();
		}
	}
	
	public void clearAllButOne() {
		clearInventory();
		inventory.get("Steak").add(1);
	}
	
	private void CookOrder(Order o) {
		if(inventory.get(o.choice).amount==0) {
			Do("Sorry, but we're out of "+o.choice+" right now.");
			if (inventory.get(o.choice).amtOrdered==0) {
				checkInventory();
			}
			o.state = OrderState.rejected;
			return;
		}
		Do(o.choice+" cooking.");
		o.state = OrderState.cooking;
		o.cookOrder(inventory.get(o.choice).cookingTime);
		inventory.get(o.choice).amount--;
		this.restaurant.updateInfoPanel();
	}
	
	private void newRestockOrder(Food food, int amount) {
		// create a new restocking (outgoing) order. should this be its own class?
		// need to somewhat randomly choose which market to try to order from?
		
		MarketStructure market = CityDirectory.getInstance().getRandomMarket();
		
		//print(markets.get(tempFood.whichMarket).getName()+": I'd like to place an order for "+amount+" "+type+" please.");
		market.getManager().msgHereIsMyOrderForDelivery(restaurant, new ItemRequest(food.type, amount));
		Do("Just placed an order for "+amount+" of "+food.type);
		
		food.amtOrdered+=amount;
		this.restaurant.updateInfoPanel();

	}
	
	private void sendBackOrder(Order o) {
		o.waiter.msgOutOf(o.choice, o.tableNumber);
	}

	
	private void PlateIt(Order o) {
		Do(o.choice+" plated.");
		o.state = OrderState.plated;
	}
	
	private void CallWaiter(Order o) {
		Do(o.waiter.getName()+", the "+o.choice+" for table "+o.tableNumber+" is ready.");
		o.state = OrderState.waiterNotified;
		o.waiter.msgOrderReady(o.choice, o.tableNumber);
	}
	
	private void leaveRestaurant() {
		this.isActive = false;
		this.myPerson.goOffWork();
		this.myPerson.removeRole(this);
		this.myPerson = null;
		this.restaurant.updateInfoPanel();
	}
	
	public String getName() {
		return myPerson.getName();
	}

	
	private class Order {
		String choice;
		int tableNumber;
		WaiterSkyler waiter;
		OrderState state;
		Timer timer = new Timer();
		
		Order(WaiterSkyler w, String c, int table) {
			waiter = w;
			choice = c;
			tableNumber = table;
			state = OrderState.arrived;
		}
		
		void cookOrder(int time) {
			timer.schedule(new TimerTask() {
				public void run() {	
					Do(choice+" done cooking.");
					state = OrderState.doneCooking;
					stateChanged();
				}
			},
			time);
		}
	}
	
	private class Food {
		String type;
		int cookingTime;
		int amount;
		int amtOrdered;
		int failCounter = 0;
		int whichMarket;
		
		Food(String name, int time, int amt) {
			type = name;
			cookingTime = time;
			amount = amt;
			amtOrdered = 0;
		}
		
		public void clear() {
			amount = 0;
		}
		
		public void addFromOrder(int amt) {
			amount += amt;
			amtOrdered -= amt;
		}
		
		public void add(int amt) {
			amount += amt;
		}
	
	}

}
