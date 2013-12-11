package cs201.roles.restaurantRoles.Brandon;

import java.util.*;
import java.util.concurrent.Semaphore;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.roles.restaurant.Brandon.CashierGuiBrandon;
import cs201.helper.Brandon.FoodBrandon;
import cs201.interfaces.roles.restaurant.Brandon.CashierBrandon;
import cs201.interfaces.roles.restaurant.Brandon.CustomerBrandon;
import cs201.interfaces.roles.restaurant.Brandon.WaiterBrandon;
import cs201.roles.marketRoles.MarketManagerRole.ItemRequest;
import cs201.roles.restaurantRoles.RestaurantCashierRole;
import cs201.structures.market.MarketStructure;
import cs201.trace.AlertLog;
import cs201.trace.AlertTag;

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

	public class MarketBill
	{
		public MarketBill(MarketStructure market, double amount)
		{
			this.m = market;
			this.amount = amount;
		}
		
		MarketStructure m;
		double amount;
	}
	
	public List<MarketBill> marketBills;
	
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
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this, "Has been given bill request: "+order+" from "+w.getName());
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
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,cust.getName()+" has paid "+billAmount);
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
	
	public void msgGiveMarketBill(MarketStructure market, double amount)
	{
		marketBills.add(new MarketBill(market,amount));
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,"Received market bill from "+market);
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
		if(restaurant.getCurrentRestaurantMoney() < b.amount)
		{
			sellSoul();
		}
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,"Paying market "+b.m.toString()+" "+b.amount);
		
		b.m.getManager().msgHereIsMyPayment(this.restaurant,(float) b.amount);
		
		restaurant.addMoney(-b.amount);
		restaurant.updateInfoPanel();
		
		marketBills.remove(b);
	}

	private void sellSoul()
	{
		if(!soulSold )
		{
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,"Selling soul...");
			restaurant.addMoney(SOUL_SALE_PRICE);
			restaurant.updateInfoPanel();
			soulSold = true;
		}
	}

	private void processBill(Bill bill)
	{
		bill.price = prices.get(bill.c).getPrice();
		
		if(penaltyList.containsKey(bill.cust))
		{
			bill.price += penaltyList.remove(bill.cust);
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,"Added extra to the customer!");
		}
		
		bill.w.msgGiveWaiterBill(bill.cust,bill.price);
		bill.s = BillState.Sent;
	}
	
	private void calcChange(Bill bill)
	{
		double change = bill.paid - bill.price;
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,"Change is "+change);
		
		int changeMult = (int)(change*100);
		change = ((double)changeMult)/100;
		//INSERT IF PAID LESS THAN BILL
		
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,"Giving "+change+" back to "+bill.cust);

		bill.s = BillState.Changed;
		
		if(change < 0)
		{
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,"CUSTOMER CANNOT PAY! "+change*-1);

			penaltyList.put(bill.cust,change*-1);
			bill.cust.msgGiveChange(0);
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,"Adding "+bill.price+" - "+(-1*change)+" to the budget");
			restaurant.addMoney(bill.price - -1*change);
			restaurant.updateInfoPanel();
		}
		else
		{
			bill.cust.msgGiveChange(change);
			restaurant.addMoney(bill.price);
			restaurant.updateInfoPanel();
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,"Adding $"+bill.price+" to the budget");

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
	public void msgHereIsDeliveryFromMarket(MarketStructure market, double amount, ItemRequest item)
	{	
		HashMap<String,Integer> foods = new HashMap<String,Integer>();
		foods.put(item.item,item.amount);
		
		((RestaurantCookRoleBrandon)this.restaurant.getCook()).msgReceivedOrder(market, foods);
		
		marketBills.add(new MarketBill(market,amount));
		
		stateChanged();
	}

	CashierGuiBrandon gui;

	private Semaphore animationPause = new Semaphore(0,true);
	
	public void setGui(CashierGuiBrandon cashierGui) {
		gui = cashierGui;
	}

	public CashierGuiBrandon getGui() {
		return gui;
	}

	public void msgReachedDestination()
	{
		animationPause.release();
	}
}

