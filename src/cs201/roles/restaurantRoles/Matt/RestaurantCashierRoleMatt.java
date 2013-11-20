package cs201.roles.restaurantRoles.Matt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cs201.gui.roles.restaurant.Matt.CashierGuiMatt;
import cs201.helper.Matt.MenuMatt;
import cs201.interfaces.roles.restaurant.RestaurantCashierRole;
import cs201.interfaces.roles.restaurant.Matt.CashierMatt;
import cs201.interfaces.roles.restaurant.Matt.CustomerMatt;
import cs201.interfaces.roles.restaurant.Matt.HostMatt;
import cs201.interfaces.roles.restaurant.Matt.WaiterMatt;


/**
 * Restaurant Cashier Role
 * 
 * @author Matt Pohlmann
 */
public class RestaurantCashierRoleMatt extends RestaurantCashierRole implements CashierMatt {

	private final double STARTINGMONEY = 50;
	private MenuMatt menu;
	public List<Check> checks; // TEMPORARILY PUBLIC FOR TESTING
	public enum CheckState { none, pending, customerPaying }; // TEMPORARILY PUBLIC FOR TESTING
	public enum CheckType { none, restaurant, market }; // TEMPORARILY PUBLIC FOR TESTING
	private CashierGuiMatt gui;
	public HostMatt host; // TEMPORARILY PUBLIC FOR TESTING
	private double currentMoney;

	public RestaurantCashierRoleMatt(HostMatt host) {
		super();

		checks = Collections.synchronizedList(new ArrayList<Check>());
		menu = new MenuMatt();
		this.host = host;
		currentMoney = STARTINGMONEY;
		System.out.printf("Cashier " + this.getName() + " has $%.2f.\n", currentMoney);
	}
	
	// Messages -------------------------------------------------------------
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
	public void msgPayBillFromMarket(/*Market market,*/ double amount, String order, double quantity) {
		Check temp = new Check();
		//temp.market = market;
		temp.amount = amount;
		temp.state = CheckState.pending;
		temp.type = CheckType.market;
		checks.add(temp);
		stateChanged();
	}
	
	@Override
	public void msgOrderInvoiceFromCook(double amount, String order, double quantity) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {		
		// If there is something to do
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
		currentMoney -= c.amount;
		DoPayMarket(c);
		//c.market.msgPayBill(c.amount);
		checks.remove(c);
	}

	// Utilities -------------------------------------------------------------
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
		//public Market market; // TEMPORARILY PUBLIC FOR TESTING
		String choice;
		public double amount; // TEMPORARILY PUBLIC FOR TESTING
		double customerPaid;
		public CheckState state; // TEMPORARILY PUBLIC FOR TESTING
		public CheckType type; // TEMPORARILY PUBLIC FOR TESTING
		
		public  Check() {
			this.waiter = null;
			this.customer = null;
			//this.market = null;
			this.choice = "";
			this.amount = 0;
			this.customerPaid = 0;
			this.state = CheckState.none;
			this.type = CheckType.none;
		}
	}
	
	/**
	 * Sets this CashierAgent's gui to gui
	 * @param gui The CashierGui being set
	 */
	public void setGui(CashierGuiMatt gui) {
		this.gui = gui;
	}
}