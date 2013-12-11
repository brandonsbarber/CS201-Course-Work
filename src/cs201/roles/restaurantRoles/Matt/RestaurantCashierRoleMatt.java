package cs201.roles.restaurantRoles.Matt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.roles.restaurant.Matt.CashierGuiMatt;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelMatt;
import cs201.helper.Matt.MenuMatt;
import cs201.interfaces.roles.restaurant.Matt.CashierMatt;
import cs201.interfaces.roles.restaurant.Matt.CustomerMatt;
import cs201.interfaces.roles.restaurant.Matt.HostMatt;
import cs201.interfaces.roles.restaurant.Matt.WaiterMatt;
import cs201.roles.marketRoles.MarketManagerRole.ItemRequest;
import cs201.roles.restaurantRoles.RestaurantCashierRole;
import cs201.structures.market.MarketStructure;
import cs201.trace.AlertLog;
import cs201.trace.AlertTag;


/**
 * Restaurant Cashier Role
 * 
 * @author Matt Pohlmann
 */
public class RestaurantCashierRoleMatt extends RestaurantCashierRole implements CashierMatt {
	private Semaphore atTargetPosition = new Semaphore(0); // used for animation
	private MenuMatt menu;
	public List<Check> checks; // TEMPORARILY PUBLIC FOR TESTING
	private List<MarketInvoice> invoices;
	public enum CheckState { none, pending, customerPaying }; // TEMPORARILY PUBLIC FOR TESTING
	public enum CheckType { none, restaurant, market }; // TEMPORARILY PUBLIC FOR TESTING
	private CashierGuiMatt gui;
	public HostMatt host; // TEMPORARILY PUBLIC FOR TESTING
	private boolean closingTime = false;

	public RestaurantCashierRoleMatt(HostMatt host) {
		super();

		checks = Collections.synchronizedList(new ArrayList<Check>());
		invoices = Collections.synchronizedList(new ArrayList<MarketInvoice>());
		menu = new MenuMatt();
		this.host = host;
	}
	
	public RestaurantCashierRoleMatt() {
		super();

		checks = Collections.synchronizedList(new ArrayList<Check>());
		invoices = Collections.synchronizedList(new ArrayList<MarketInvoice>());
		menu = new MenuMatt();
		this.host = null;
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
	public void msgHereIsDeliveryFromMarket(MarketStructure market, double amount, ItemRequest item) {
		Check temp = new Check();
		temp.choice = item.item;
		temp.market = market;
		temp.amount = amount;
		temp.quantity = item.amount;
		temp.state = CheckState.pending;
		temp.type = CheckType.market;
		checks.add(temp);
		stateChanged();
	}
	
	@Override
	public void msgOrderInvoiceFromCook(MarketStructure market, String order, double quantity) {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), "Received invoice for a market order from " + market + " for " + quantity + " " + order + "s.");
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
		this.isActive = false;
		this.myPerson.removeRole(this);
		this.myPerson.goOffWork();
		DoLeaveRestaurant();
		this.myPerson = null;
		this.gui.setPresent(false);
	}
	
	private void GiveCheckToWaiter(Check c) {
		DoGiveCheckToWaiter(c);
		c.amount = menu.getPrice(c.choice);
		c.waiter.msgHereIsCheck(c.customer, c.amount);
		checks.remove(c);
	}
	
	private void GiveCustomerChange(Check c) {
		boolean checkPaidInFull = c.customerPaid >= c.amount;
		this.restaurant.addMoney(checkPaidInFull ? c.amount : c.customerPaid);
		this.restaurant.updateInfoPanel();
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
			if (i.market == c.market && i.order.toLowerCase().equals(c.choice.toLowerCase()) && i.quantity >= c.quantity) {
				this.restaurant.removeMoney(c.amount);
				this.restaurant.updateInfoPanel();
				DoPayMarket(c);
				((RestaurantCookRoleMatt) this.restaurant.getCook()).msgFulfillSupplyOrder(c.choice, c.quantity, c.market);
				c.market.getManager().msgHereIsMyPayment(restaurant, (float)c.amount);
				checks.remove(c);
				return;
			}
		}
		
		// If no matching invoice
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), "No matching invoice found.");
		checks.remove(c);
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
	
	private void DoGiveCheckToWaiter(Check c) {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), "Giving check back to " + c.waiter.toString() + " for " + c.customer.toString() + ".");
	}
	
	private void DoGiveCustomerChange(Check c) {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), "Giving change to " + c.customer.toString() + ".");
		if (c.customerPaid < c.amount) {
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), "Says that " + c.customer.toString() + " didn't have enough to pay his check!");
		}
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), this.restaurant.toString() + " now has " + String.format("$%.2f.", this.restaurant.getCurrentRestaurantMoney()));
	}
	
	private void DoPayMarket(Check c) {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), "Paying bill to " + c.market.toString() + String.format(" for $%.2f.\n\t%s now has $%.2f.", c.amount, this.restaurant.toString(), this.restaurant.getCurrentRestaurantMoney()));
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
	 * The CashierGui tells this CashierAgent that it has reached its destination, freeing up this CashierAgent
	 * to continue working
	 */
	public void DoneAnimating() {
		atTargetPosition.release();
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
		closingTime = false;
		this.gui.setPresent(true);
		gui.goToRegister();
		try {
			atTargetPosition.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}