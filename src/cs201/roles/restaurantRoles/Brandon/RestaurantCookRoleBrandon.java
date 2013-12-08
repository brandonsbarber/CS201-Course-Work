package cs201.roles.restaurantRoles.Brandon;

import java.util.*;
import java.util.concurrent.Semaphore;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.roles.restaurant.Brandon.CashierGuiBrandon;
import cs201.gui.roles.restaurant.Brandon.CookGuiBrandon;
import cs201.gui.roles.restaurant.Brandon.KitchenGuiBrandon;
import cs201.helper.Brandon.FoodBrandon;
import cs201.helper.Brandon.RestaurantRotatingStandBrandon;
import cs201.interfaces.roles.restaurant.Brandon.CookBrandon;
import cs201.interfaces.roles.restaurant.Brandon.WaiterBrandon;
import cs201.roles.restaurantRoles.RestaurantCookRole;

/**
 * Class representation of a cook at the restaurant
 * @author Brandon
 *
 */
public class RestaurantCookRoleBrandon extends RestaurantCookRole implements CookBrandon
{
	private KitchenGuiBrandon kitchen;
	
	private List<Order> orders;
	public static class Order
	{
		public Order(WaiterBrandon w, String order, int table)
		{
			this.w = w;
			c = order;
			this.table = table;
			s = OrderState.Pending;
			
			idNumber = ++instanceCount;
		}
		
		public boolean equals(Object o)
		{
			if(o instanceof Order)
			{
				if(((Order)o).idNumber == idNumber)
				{
					return true;
				}
			}
			return false;
		}
		
		public OrderState getState()
		{
			return s;
		}
		
		public int getTable()
		{
			return table;
		}
		
		public String getChoice()
		{
			return c;
		}
		
		WaiterBrandon w;
		String c;
		int table;
		OrderState s;
		int idNumber;
		
		private static int instanceCount = 0;
	}
	
	class MyMarket
	{
		/*public MyMarket(Market agent)
		{
			this.m = agent;
			outOf = new TreeSet<String>();
		}
		
		Market m;
		Set<String> outOf;*/
	}
	
	class Market {}
	
	public static enum OrderState{Pending,Cooking,Done,Plated, PickedUp};
	
	private Map<String,FoodBrandon> cookTime;
	
	private Map<String,Integer> derelictDeficit = Collections.synchronizedMap(new HashMap<String,Integer>());
	
	private Timer timer;
	
	private List<MyMarket> markets;
	
	private int currentMarket = 0;
	
	private Map<String,Integer> remainingOrder =Collections.synchronizedMap( new HashMap<String,Integer>());
	
	private Map<String,Double> menuData,savedMenu;
	
	/**
	 * Creates a new CookAgent
	 * @param cookingTimes a map of food and cooking times
	 * @param savedPrices 
	 * @param prices 
	 */
	public RestaurantCookRoleBrandon(Map<String,FoodBrandon> cookingTimes, HashMap<String, Double> prices, HashMap<String, Double> savedPrices)
	{
		cookTime = Collections.synchronizedMap(new HashMap<String,FoodBrandon>());
		for(String s : cookingTimes.keySet())
		{
			//Preserve pointer so there is universal modification
			cookTime.put(s,cookingTimes.get(s));
		}
		orders = Collections.synchronizedList(new ArrayList<Order>());
		timer = new Timer();
		markets = Collections.synchronizedList(new ArrayList<MyMarket>());
		menuData = prices;
		savedMenu = savedPrices;
		calcLowFoods();
	}
	
	public void setKitchen(KitchenGuiBrandon kitchen)
	{
		this.kitchen = kitchen;
	}
	
	/**
	 * Adds a market to the list of markets which the cook pulls from
	 * @param m the new market to be referenced.
	 */
	public void addMarket(Market m)
	{
		markets.add(new MyMarket());
		remainingOrder.putAll(derelictDeficit);
		derelictDeficit.clear();
		stateChanged();
	}
	
	/**
	 * Message from a market indicating that some parts of the order cannot be fulfilled
	 * @param agent the market sending this message
	 * @param orderFulfill the food not provided by the market
	 */
	public void msgCannotFulfill(Market agent, HashMap<String,Integer> orderFulfill)
	{
		//Changes state of this market to being out of the food item
		/*for(MyMarket m : markets)
		{
			if(m.m == agent)
			{
				for(String s : orderFulfill.keySet())
				{
					m.outOf.add(s);
				}
			}
		}*/
		//Determines if all markets are out of a given food item
		for(String s : orderFulfill.keySet())
		{
			boolean allOut = true;
			/*for(MyMarket m : markets)
			{
				if(!m.outOf.contains(s))
				{
					allOut = false;
				}
			}*/
			//If all are out, puts in a separate list that is not referenced until new markets are added
			if(allOut)
			{
				System.out.println(this+": MARKETS ALL OUT OF "+s.toUpperCase());
				derelictDeficit.put(s,orderFulfill.remove(s));
			}
		}
		
		//Any remaining items are added to remaining order list
		for(String key:orderFulfill.keySet())
		{
			if(remainingOrder.containsKey(key))
			{
				remainingOrder.put(key,remainingOrder.get(key)+orderFulfill.get(key));
			}
			else
			{
				remainingOrder.put(key,orderFulfill.get(key));
			}
		}
		
		System.out.println(this+": Received the following deficits."+orderFulfill);
		System.out.println(this+": Remaining order "+remainingOrder);
		stateChanged();
	}
	
	/**
	 * Message from a market indicating that a food restock order has been received 
	 * @param agent the market sending the message
	 * @param orderFulfill the food order that is fulfilled
	 */
	public void msgReceivedOrder(Market agent, HashMap<String,Integer> orderFulfill)
	{
		for(String key:orderFulfill.keySet())
		{
			cookTime.get(key).setAmount(cookTime.get(key).getAmount()+orderFulfill.get(key));
			if(!menuData.containsKey(key))
			{
				menuData.put(key,savedMenu.get(key));
			}
		}
		System.out.println(this+": Received this order "+orderFulfill);
		
		for(String key: cookTime.keySet())
		{
			System.out.println(this+": "+key+" "+cookTime.get(key).getAmount());
		}
		
		stateChanged();
	}
	
	/**
	 * Signals that the waiter will present an order
	 * @param w waiter giving the order
	 * @param order the order being given
	 * @param table where the customer who ordered is sitting
	 */
	public void msgPresentOrder(WaiterBrandon w, String order, int table)
	{
		System.out.println("Cook: Has been given "+order+" from "+w.getName());
		orders.add(new Order(w,order,table));
		stateChanged();
	}
	
	public void msgPickingUpOrder(WaiterBrandon w, int table)
	{
		for(Order o : orders)
		{
			if(o.w == w && o.table == table)
			{
				o.s = OrderState.PickedUp;
				break;
			}
		}
		stateChanged();
	}
	
	@Override
	public boolean pickAndExecuteAnAction()
	{
		//Processes if there is any food remaining to be ordered
		if(remainingOrder.size() != 0)
		{
			System.out.println(this+": PLACING A DEFICIT ORDER");
			calcLowFoods();
			return true;
		}
		for(Order o : orders)
		{
			if(o.s == OrderState.PickedUp)
			{
				removeOrder(o);
				return true;
			}
		}
		for(Order o : orders)
		{
			if(o.s == OrderState.Done)
			{
				plateFood(o);
				return true;
			}
		}
		for(Order o : orders)
		{
			if(o.s == OrderState.Pending)
			{
				cook(o);
				return true;
			}
		}
		chill();
		return false;
	}
	
	private void chill()
	{
		gui.doGoToChill();
		try
		{
			animationPause.acquire();
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	private void removeOrder(Order o)
	{
		orders.remove(o);
		//kitchen.removeOrder(o);
	}
	
	private void plateFood(Order o)
	{
		gui.doGoToSlot(o.table);
		try
		{
			animationPause.acquire();
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		
		System.out.println("Cook: Telling "+o.w.getName()+" that "+o.c+" is done");
		o.w.msgOrderDone(o.table);
		o.s = OrderState.Plated;
	}
	
	private void cook(Order o)
	{
		System.out.println("Cook: I am preparing to cook "+o.c);
		FoodBrandon f = cookTime.get(o.c);
		if(f.isGone ())
		{
			System.out.println("Cook: We are out of "+f.getType());
			o.w.msgOutOfFood(o.c,o.table);
			orders.remove(o);
			return;
		}
		
		f.useFood();
		o.s = OrderState.Cooking;
		timer.schedule (new CookingTask(this,o), cookTime.get(o.c).getCookingTime());
		
		gui.doGoToSlot(o.table);
		try
		{
			animationPause.acquire();
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		
		kitchen.addOrder(o);
		
		if(f.isLow () && !f.ordered())
		{
			System.out.println ("Cook: We are low on "+f.getType()+": "+f.getAmount());
			calcLowFoods();
		}
	}
	
	private void calcLowFoods()
	{
		HashMap<String,Integer> foodOrder = new HashMap<String,Integer>();
		//Adds food to the order
		for(String s : cookTime.keySet())
		{
			if(!cookTime.get(s).ordered() && cookTime.get(s).isLow())
			{
				foodOrder.put(s,cookTime.get(s).getOrderAmount());
				cookTime.get(s).order();
			}
		}
		//Keeps it from processing with no markets
		if(markets.size() == 0)
		{
			derelictDeficit.putAll(foodOrder);
			return;
		}
		
		//Gets the market to use in this case
		MyMarket market = markets.get(currentMarket++);
		if(currentMarket == markets.size())
		{
			currentMarket = 0;
		}
		
		System.out.println(this+": Contents of remaining order: "+remainingOrder);
		
		//Transfers all remaining orders to the order
		for(String s : remainingOrder.keySet())
		{
			//System.out.println(this+": This market is out of "+market.outOf);
			System.out.println(this+": Adding from remaining order");
			//Clobbers the previous order so that there are no duplicate orders
			foodOrder.put(s,remainingOrder.get(s));
		}
		remainingOrder.clear();
		
		System.out.println(this+": Low on "+foodOrder.keySet());
		
		System.out.println(this+": Placing order "+foodOrder);
		//market.m.msgSendOrder(this,foodOrder);
	}
	
	private class CookingTask extends TimerTask
	{
		private Order o;
		private RestaurantCookRoleBrandon owner;
		
		public CookingTask(RestaurantCookRoleBrandon owner, Order o)
		{
			this.owner = owner;
			this.o = o;
		}
		
		@Override
		public void run()
		{
			o.s = OrderState.Done;
			owner.stateChanged();
		}
	}
	
	/**
	 * Returns a string representation of this cook
	 * @return string representation of the agent
	 */
	public String toString()
	{
		return "Cook";
	}

	@Override
	public void startInteraction(Intention intent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgClosingTime() {
		// TODO Auto-generated method stub
		
	}

	CookGuiBrandon gui;
	
	public void setGui(CookGuiBrandon cookGui) {
		this.gui = cookGui;		
	}

	public CookGuiBrandon getGui() {
		return gui;
	}

	public void msgReachedDestination() {
		animationPause.release();
	}
	
	Semaphore animationPause = new Semaphore(0,true);

	RestaurantRotatingStandBrandon stand;
	
	public void setRotatingStand(RestaurantRotatingStandBrandon stand)
	{
		this.stand = stand;		
	}
}

