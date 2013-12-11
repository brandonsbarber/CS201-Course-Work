package cs201.roles.restaurantRoles.Skyler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;

import cs201.agents.PersonAgent.Intention;
import cs201.interfaces.roles.restaurant.Skyler.CashierSkyler;
import cs201.interfaces.roles.restaurant.Skyler.CookSkyler;
import cs201.interfaces.roles.restaurant.Skyler.CustomerSkyler;
import cs201.interfaces.roles.restaurant.Skyler.WaiterSkyler;
import cs201.roles.marketRoles.MarketManagerRole.ItemRequest;
import cs201.roles.restaurantRoles.RestaurantCashierRole;
import cs201.structures.market.MarketStructure;

public class RestaurantCashierRoleSkyler extends RestaurantCashierRole
		implements CashierSkyler {
	public List<Check> checks
	= Collections.synchronizedList(new ArrayList<Check>());
	
	private String name;
	public double cashOnHand;
	public double debt = 0; 
	private boolean closingTime = false;
	CookSkyler cook = null;
	private List<MarketBill> marketBills = Collections.synchronizedList(new ArrayList<MarketBill>());
	
	//public EventLog log = new EventLog();

	public enum CheckState {uncalculated, unpaid, unprocessed, paid};

	public RestaurantCashierRoleSkyler() {
		super();
		this.cashOnHand = 3000;
	}
	public RestaurantCashierRoleSkyler(String name) {
		this.name = name;
		this.cashOnHand = 3000;
	}

	@Override
	public void msgRequestCheck(CustomerSkyler c, WaiterSkyler w, double amount) {
		Do("Making check.");
		//log.add(new LoggedEvent("Making check."));
		checks.add(new Check(c, w, amount));
		stateChanged();
	}

	@Override
	public void msgHereIsCash(CustomerSkyler cust, double cash) {
		Do("Received cash from "+cust);
		//log.add(new LoggedEvent("Received cash from "+cust));
		for (Check ch : checks) {
			if (ch.c.equals(cust)) {
				ch.cashPaid = cash;
				ch.state = CheckState.unprocessed;
			}
		}
		stateChanged();
	}
	
	@Override
	public void msgClosingTime() {

		closingTime = true;
		stateChanged();
	}
	
	@Override
	public void msgHereIsDeliveryFromMarket(MarketStructure market,
			double amount, ItemRequest item) {
		
		//DO SOMETHING WITH FOOD
		
		marketBills.add(new MarketBill(market, amount));
		
		cook.msgHereIsDelivery(item.item, item.amount);
		
		stateChanged();
	}
	
	@Override
	public void startInteraction(Intention intent) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean pickAndExecuteAnAction() {
		
		if (closingTime) {
			LeaveRestaurant();
			return true;
		}
		
		if(!checks.isEmpty()) {
			for (Check ch : checks) {
				if (ch.state==CheckState.unprocessed) {
					ch.processCheck();
					return true;
				}
				if (ch.state==CheckState.uncalculated) {
					ch.prepareCheck();
					return true;
				}
				if (ch.state==CheckState.paid) {
					removeCheck(ch);
					return true;
				}
			}//print("test?");
		}
		if(!marketBills.isEmpty()) {
			for (MarketBill mb : marketBills) {
				outgoingPayment(mb);
				return true;
			}
		}
		return false;
	}
	
	private void outgoingPayment(MarketBill mBill) {
		if (mBill.amount>cashOnHand) {
			Do("Not enough cash on hand to pay for our recent market orders. \n I've payed the market in full but added $"+String.format("%.2f", (mBill.amount-cashOnHand))+" to our debt.");
			//log.add(new LoggedEvent("Not enough cash on hand to pay for our recent market orders. \n I've payed $"+String.format("%.2f",cashOnHand)+" but added $"+String.format("%.2f", (amt-cashOnHand))+" to our debt."));
			
			debt+=(mBill.amount-cashOnHand);
			
			mBill.market.getManager().msgHereIsMyPayment(this.restaurant,(float) mBill.amount);
			cashOnHand-=mBill.amount;
			
			marketBills.remove(mBill);
			return;
		}
		Do("Deducted outstanding balance of $"+String.format("%.2f", mBill.amount)+" from cash on hand.");
		//log.add(new LoggedEvent("Deducted outstanding balance of $"+String.format("%.2f", amt)+" from cash on hand."));
		cashOnHand-=mBill.amount;
		restaurant.removeMoney(mBill.amount);
		mBill.market.getManager().msgHereIsMyPayment(this.restaurant,(float) mBill.amount);
		marketBills.remove(mBill);
	}
	
	private void removeCheck(Check ch) {
		checks.remove(ch);
	}
	
	private void LeaveRestaurant() {
		this.isActive = false;
		this.myPerson.removeRole(this);
		this.myPerson.goOffWork();
		this.myPerson = null;
	}
	
	public String getName() {
		return myPerson.getName();
	}
	
	public class Check {
		public CustomerSkyler c;
		WaiterSkyler w;
		public CheckState state;
		public double amount;
		public double cashPaid = 0;
		public double change = 0;
		Timer timer = new Timer();
		
		public Check(CustomerSkyler cust, WaiterSkyler wait, double amt) {
			c = cust;
			w = wait;
			amount = amt;
			state = CheckState.uncalculated;
		}
		
		void prepareCheck() {
			Do(w.getName()+", I have a check ready for you.");
			//log.add(new LoggedEvent(w.getName()+", I have a check ready for you."));
			state = CheckState.unpaid;
			w.msgCheckReady(c, amount);
		}
		
		void processCheck() {
			
			change = Math.round((cashPaid - amount)*100)/100.0;
			Do("Thanks, "+c.getName()+", you gave me $"+String.format("%.2f", cashPaid)+" for your $"+String.format("%.2f", amount)+" bill.");
			if(change>0) {
				Do("Here is your change of $"+String.format("%.2f", change));
			}
			//log.add(new LoggedEvent("Thanks, "+c.getName()+", you gave me $"+String.format("%.2f", cashPaid)+" for your $"+String.format("%.2f", amount)+" bill.\n Here is your change of $"+String.format("%.2f", change)));
			state = CheckState.paid;
			cashOnHand+=(cashPaid-change);
			restaurant.addMoney(cashPaid-change);
			c.msgHereIsYourChange(change);
			
		}
	}
	
	public class MarketBill {
		MarketStructure market;
		double amount;
		MarketBill(MarketStructure m, double amt) {
			amount = amt;
			market = m;
		}
	}
	
	public void setCashOnHand (double amt) {
		cashOnHand = amt;
	}
	
	public void setCook(CookSkyler newCook) {
		cook = newCook;
	}

}
