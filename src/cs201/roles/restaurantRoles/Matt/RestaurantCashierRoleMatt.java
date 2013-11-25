package cs201.roles.restaurantRoles.Matt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.roles.restaurant.Matt.CashierGuiMatt;
import cs201.helper.Matt.MenuMatt;
import cs201.interfaces.roles.restaurant.Matt.CashierMatt;
import cs201.interfaces.roles.restaurant.Matt.CustomerMatt;
import cs201.interfaces.roles.restaurant.Matt.HostMatt;
import cs201.interfaces.roles.restaurant.Matt.WaiterMatt;
import cs201.roles.restaurantRoles.RestaurantCashierRole;
import cs201.structures.market.MarketStructure;


/**
 * Restaurant Cashier Role
 * 
 * @author Matt Pohlmann
 */
public class RestaurantCashierRoleMatt extends RestaurantCashierRole implements CashierMatt {

	private final double STARTINGMONEY = 50;
	private MenuMatt menu;
	public List<Check> checks; // TEMPORARILY PUBLIC FOR TESTING
	private List<MarketInvoice> invoices;
	public enum CheckState { none, pending, customerPaying }; // TEMPORARILY PUBLIC FOR TESTING
	public enum CheckType { none, restaurant, market }; // TEMPORARILY PUBLIC FOR TESTING
	private CashierGuiMatt gui;
	public HostMatt host; // TEMPORARILY PUBLIC FOR TESTING
	private double currentMoney;
	private boolean closingTime = false;

	public RestaurantCashierRoleMatt(HostMatt host) {
		super();

		checks = Collections.synchronizedList(new ArrayList<Check>());
		invoices = Collections.synchronizedList(new ArrayList<MarketInvoice>());
		menu = new MenuMatt();
		this.host = host;
		currentMoney = STARTINGMONEY;
		System.out.printf("Cashier " + this.getName() + " has $%.2f.\n", currentMoney);
	}
	
	public RestaurantCashierRoleMatt() {
		super();

		checks = Collections.synchronizedList(new ArrayList<Check>());
		invoices = Collections.synchronizedList(new ArrayList<MarketInvoice>());
		menu = new MenuMatt();
		this.host = null;
		currentMoney = STARTINGMONEY;
		System.out.printf("Cashier " + this.getName() + " has $%.2f.\n", currentMoney);
	}
	
	public void setHost(HostMatt host) {
		this.host = host;
	}
	
	// Messages -------------------------------------------------------------
	@Override
	public void msgClosingTime() {
		closingTime = true;
		stateChanged();
	}
	
	@Override
	public void msgComputeCheck(WaiterMatt w, CustomerMatt c, String choice) {
		Check temp = new Check();
		temp.waiter = w;
		temp.customer = c;
		temp.choice = choice;
		temp.state = CheckState.pending;
		temp.type = CheckType.restaurant;
		checks.add(temp);
		stateChanged();
	}
	
	@Override
	public void msgPayCheck(CustomerMatt c, double customerMoney, double check) {
		Check temp = new Check();
		temp.customer = c;
		temp.amount = check;
		temp.customerPaid = customerMoney;
		temp.state = CheckState.customerPaying;
		temp.type = CheckType.restaurant;
		checks.add(temp);
		stateChanged();
	}
	
	@Override
	public void msgPayBillFromMarket(MarketStructure market, double amount, String order, int quantity) {
		Check temp = new Check();
		temp.choice = order;
		temp.market = market;
		temp.amount = amount;
		temp.quantity = quantity;
		temp.state = CheckState.pending;
		temp.type = CheckType.market;
		checks.add(temp);
		stateChanged();
	}
	
	@Override
	public void msgOrderInvoiceFromCook(MarketStructure market, String order, double quantity) {
		MarketInvoice temp = new MarketInvoice(market, order, quantity);
		invoices.add(temp);
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
		
		synchronized(checks) {
			for (Check c : checks) {
				if (c.state == CheckState.customerPaying && c.type == CheckType.restaurant) {
					GiveCustomerChange(c);
					return true;
				}
			}
		}
		
		synchronized(checks) {
			for (Check c : checks) {
				if (c.state == CheckState.pending && c.type == CheckType.restaurant) {
					GiveCheckToWaiter(c);
					return true;
				}
			}
		}
			
		synchronized(checks) {
			for (Check c : checks) {
				if (c.state == CheckState.pending && c.type == CheckType.market) {
					PayMarket(c);
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
		// TODO
		this.isActive = false;
		this.myPerson.removeRole(this);
		this.myPerson.goOffWork();
		this.myPerson = null;
		DoLeaveRestaurant();
	}
	
	private void GiveCheckToWaiter(Check c) {
		DoGiveCheckToWaiter(c);
		c.amount = menu.getPrice(c.choice);
		c.waiter.msgHereIsCheck(c.customer, c.amount);
		checks.remove(c);
	}
	
	private void GiveCustomerChange(Check c) {
		boolean checkPaidInFull = c.customerPaid >= c.amount;
		currentMoney += checkPaidInFull ? c.amount : c.customerPaid;
		double change = checkPaidInFull ? c.customerPaid - c.amount : 0;
		DoGiveCustomerChange(c);
		c.customer.msgHereIsYourChange(change);
		if (!checkPaidInFull) {
			host.msgBanThisCustomer(c.customer);
		}
		checks.remove(c);
	}
	
	private void PayMarket(Check c) {
		for (MarketInvoice i : invoices) {
			if (i.market == c.market && i.order == c.choice && i.quantity >= c.quantity) {
				currentMoney -= c.amount;
				DoPayMarket(c);
				//c.market.getManager().msgHereIsMyPayment(restaurant, c.amount);
				checks.remove(c);
				return;
			}
		}
		
		// If no matching invoice
		checks.remove(c);
	}

	// Utilities -------------------------------------------------------------
	private void DoLeaveRestaurant() {
		// TODO leave restaurant animation
	}
	
	private void DoGiveCheckToWaiter(Check c) {
		System.out.println("Cashier " + this.toString() + " giving check back to Waiter " + c.waiter.toString() + " for Customer " + c.customer.toString() + ".");
	}
	
	private void DoGiveCustomerChange(Check c) {
		System.out.println("Cashier " + this.toString() + " giving change to Customer " + c.customer.toString() + ".");
		if (c.customerPaid < c.amount) {
			System.out.println("Cashier " + this.toString() + " says that Customer " + c.customer.toString() + " didn't have enough to pay his check!");
		}
		System.out.printf("\tCashier now has $%.2f.\n", currentMoney);
	}
	
	private void DoPayMarket(Check c) {
		System.out.printf("Cashier " + this.toString() + " paying bill to Market " + /*c.market.toString() +*/ " for $%.2f.\n\tCashier now has $%.2f.\n", c.amount, currentMoney);
	}
	
	public class Check { // TEMPORARILY PUBLIC FOR TESTING
		WaiterMatt waiter;
		public CustomerMatt customer; // TEMPORARILY PUBLIC FOR TESTING
		public MarketStructure market; // TEMPORARILY PUBLIC FOR TESTING
		String choice;
		int quantity;
		public double amount; // TEMPORARILY PUBLIC FOR TESTING
		double customerPaid;
		public CheckState state; // TEMPORARILY PUBLIC FOR TESTING
		public CheckType type; // TEMPORARILY PUBLIC FOR TESTING
		
		public  Check() {
			this.waiter = null;
			this.customer = null;
			this.market = null;
			this.quantity = 0;
			this.choice = "";
			this.amount = 0;
			this.customerPaid = 0;
			this.state = CheckState.none;
			this.type = CheckType.none;
		}
	}
	
	private class MarketInvoice {
		MarketStructure market;
		String order;
		double quantity;
		
		public MarketInvoice(MarketStructure market, String order, double quantity) {
			this.market = market;
			this.order = order;
			this.quantity = quantity;
		}
	}
	
	/**
	 * Sets this CashierAgent's gui to gui
	 * @param gui The CashierGui being set
	 */
	public void setGui(CashierGuiMatt gui) {
		this.gui = gui;
	}
	
	public CashierGuiMatt getGui() {
		return this.gui;
	}

	@Override
	public void startInteraction(Intention intent) {
		// TODO maybe animate into restaurant?
		closingTime = false;
	}

}