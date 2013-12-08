package cs201.roles.restaurantRoles.Brandon;

import java.util.*;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.roles.restaurant.Brandon.CashierGuiBrandon;
import cs201.helper.Brandon.FoodBrandon;
import cs201.interfaces.roles.restaurant.Brandon.CashierBrandon;
import cs201.interfaces.roles.restaurant.Brandon.CustomerBrandon;
import cs201.interfaces.roles.restaurant.Brandon.WaiterBrandon;
import cs201.roles.marketRoles.MarketManagerRole.ItemRequest;
import cs201.roles.restaurantRoles.RestaurantCashierRole;
import cs201.structures.market.MarketStructure;

/**
 * Class representation of a cook at the restaurant
 * @author Brandon
 *
 */
public class RestaurantCashierRoleBrandon extends RestaurantCashierRole implements CashierBrandon
{
	private static final double SOUL_SALE_PRICE = 1000000;

	boolean closingTime = false;
	
	public List<Bill> bills;
	public class Bill
	{
		public Bill(WaiterBrandon w, String order, int table, CustomerBrandon cust)
		{
			this.w = w;
			c = order;
			this.table = table;
			this.cust = cust;
			s = BillState.Pending;
		}
		
		WaiterBrandon w;
		String c;
		int table;
		double price;
		BillState s;
		CustomerBrandon cust;
		double paid;
	}
	
	class Market{}
	
	public class MarketBill
	{
		public MarketBill(Market m, double amount)
		{
			this.m = m;
			this.amount = amount;
		}
		
		Market m;
		double amount;
	}
	
	public List<MarketBill> marketBills;
	
	public double budget;
	
	enum BillState {Pending,Sent,Paid,Changed};
	
	private Map<String,FoodBrandon> prices;
	
	public Map<CustomerBrandon, Double> penaltyList;
	
	public boolean soulSold = false;
	
	/**
	 * Creates a new CookAgent
	 * @param cookingTimes a map of food and cooking times
	 */
	public RestaurantCashierRoleBrandon(Map<String,FoodBrandon> foodPrices, double startBudget)
	{
		prices = new HashMap<String,FoodBrandon>();
		for(String s : foodPrices.keySet())
		{
			//Preserve pointer so there is universal modification
			prices.put(s,foodPrices.get(s));
		}
		bills = new ArrayList<Bill>();
		penaltyList = new HashMap<CustomerBrandon,Double>();
		
		//log = new EventLog();
		budget = startBudget;
		
		marketBills = Collections.synchronizedList(new ArrayList<MarketBill>());
	}
	
	/**
	 * Signals that the waiter will present an order
	 * @param w waiter giving the order
	 * @param order the order being given
	 * @param table where the customer who ordered is sitting
	 */
	public void msgAskForBill(WaiterBrandon w, String order, int table, CustomerBrandon cust)
	{
		System.out.println(this+": Has been given bill request: "+order+" from "+w.getName());
		//log.add(new LoggedEvent(this+": Has been given bill request: "+order+" from "+w.getName()));
		bills.add(new Bill(w,order,table,cust));
		stateChanged();
	}
	
	/**
	 * Receives a payment from a customer
	 * @param cust the customer paying the bill
	 * @param billAmount the amount paid by the customer
	 */
	public void msgPay(CustomerBrandon cust, double billAmount)
	{
		System.out.println(this+": "+cust.getName()+" has paid "+billAmount);
		//log.add(new LoggedEvent(this+": "+cust.getName()+" has paid "+billAmount));
		for(Bill b : bills)
		{
			if(b.cust == cust)
			{
				b.paid = billAmount;
				b.s = BillState.Paid;
				break;
			}
		}
		stateChanged();
	}
	
	public void msgGiveMarketBill(Market market, double amount)
	{
		marketBills.add(new MarketBill(market,amount));
		System.out.println(this+": Received market bill from "+market);
		//log.add(new LoggedEvent(this+": Received market bill from "+market));
		stateChanged();
	}
	
	@Override
	public boolean pickAndExecuteAnAction()
	{
		if(closingTime)
		{
			leaveRestaurant();
			return true;
		}
		
		for(Bill b : bills)
		{
			if(b.s == BillState.Paid)
			{
				calcChange(b);
				return true;
			}
		}
		for(Bill b : bills)
		{
			if(b.s == BillState.Pending)
			{
				processBill(b);
				return true;
			}
		}
		for(int i = 0; i < bills.size(); i++)
		{
			if(bills.get(i).s == BillState.Changed)
			{
				bills.remove(i);
				return true;
			}
		}
		for(MarketBill b : marketBills)
		{
			payMarket(b);
			return true;
		}
		return false;
	}

	private void payMarket(MarketBill b)
	{
		if(budget < b.amount)
		{
			sellSoul();
		}
		System.out.println(this+": Paying market "+b.m.toString()+" "+b.amount);
		//log.add(new LoggedEvent(this+": Paying market "+b.m.toString()+" "+b.amount));
		//b.m.msgPayBill(b.amount);
		budget -= b.amount;
		
		marketBills.remove(b);
	}

	private void sellSoul()
	{
		if(!soulSold )
		{
			System.out.println(this+": Selling soul...");
			//log.add(new LoggedEvent(this+": Selling soul..."));
			budget += SOUL_SALE_PRICE;
			soulSold = true;
		}
	}

	private void processBill(Bill bill)
	{
		System.out.println(prices);
		System.out.println(prices.get(bill.c));
		System.out.println(bill.c);
		bill.price = prices.get(bill.c).getPrice();
		
		if(penaltyList.containsKey(bill.cust))
		{
			bill.price += penaltyList.remove(bill.cust);
			System.out.println(this+": Added extra to the customer!");
			//log.add(new LoggedEvent(this+": Added extra to the customer!"));
		}
		
		bill.w.msgGiveWaiterBill(bill.cust,bill.price);
		bill.s = BillState.Sent;
	}
	
	private void calcChange(Bill bill)
	{
		double change = bill.paid - bill.price;
		System.out.println("Change is "+change);
		//log.add(new LoggedEvent("Change is "+change));
		int changeMult = (int)(change*100);
		change = ((double)changeMult)/100;
		//INSERT IF PAID LESS THAN BILL
		
		System.out.println(this+": Giving "+change+" back to "+bill.cust);
		//log.add(new LoggedEvent(this+": Giving "+change+" back to "+bill.cust));
		bill.s = BillState.Changed;
		
		if(change < 0)
		{
			System.out.println(this+": CUSTOMER CANNOT PAY! "+change*-1);
			//log.add(new LoggedEvent(this+": CUSTOMER CANNOT PAY! "+change*-1));
			penaltyList.put(bill.cust,change*-1);
			bill.cust.msgGiveChange(0);
			System.out.println(this+"Adding "+bill.price+" - "+(-1*change)+" to the budget");
			budget += bill.price - -1*change;
		}
		else
		{
			bill.cust.msgGiveChange(change);
			budget += bill.price;
			System.out.println(this+": Adding $"+bill.price+" to the budget");

		}
	}
	
	public String toString()
	{
		return "Cashier";
	}

	@Override
	public void startInteraction(Intention intent) {
		closingTime = false;
		this.gui.setPresent(true);
	}

	@Override
	public void msgClosingTime() {
		closingTime = true;
		stateChanged();
	}
	
	private void leaveRestaurant()
	{
		this.isActive = false;
		this.myPerson.removeRole(this);
		this.myPerson.goOffWork();
		this.myPerson = null;
		this.gui.setPresent(false);
	}

	@Override
	public void msgHereIsDeliveryFromMarket(MarketStructure market,
			double amount, ItemRequest item) {
		// TODO Auto-generated method stub
		
	}

	CashierGuiBrandon gui;
	
	public void setGui(CashierGuiBrandon cashierGui) {
		gui = cashierGui;
	}

	public CashierGuiBrandon getGui() {
		return gui;
	}
}

