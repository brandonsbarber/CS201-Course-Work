package cs201.structures.restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.StructurePanel;
import cs201.gui.roles.restaurant.Matt.CashierGuiMatt;
import cs201.gui.roles.restaurant.Matt.CookGuiMatt;
import cs201.gui.roles.restaurant.Matt.CustomerGuiMatt;
import cs201.gui.roles.restaurant.Matt.HostGuiMatt;
import cs201.gui.roles.restaurant.Matt.WaiterGuiMatt;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelMatt;
import cs201.helper.CityTime;
import cs201.helper.Matt.RestaurantRotatingStand;
import cs201.roles.Role;
import cs201.roles.restaurantRoles.RestaurantWaiterRole;
import cs201.roles.restaurantRoles.Matt.RestaurantCashierRoleMatt;
import cs201.roles.restaurantRoles.Matt.RestaurantCookRoleMatt;
import cs201.roles.restaurantRoles.Matt.RestaurantCustomerRoleMatt;
import cs201.roles.restaurantRoles.Matt.RestaurantHostRoleMatt;
import cs201.roles.restaurantRoles.Matt.RestaurantWaiterRoleMatt;
import cs201.roles.restaurantRoles.Matt.RestaurantWaiterRoleMattNormal;
import cs201.roles.restaurantRoles.Matt.RestaurantWaiterRoleMattStand;
import cs201.trace.AlertLog;
import cs201.trace.AlertTag;

/**
 * Matthew Pohlmann's Restaurant Structure for SimCity201
 * @author Matthew Pohlmann
 *
 */
public class RestaurantMatt extends Restaurant {
	private final int INITIALWAITERS = 2;
	private final int MAXWAITERS = 4;
	private RestaurantRotatingStand stand = new RestaurantRotatingStand();
	
	public RestaurantMatt(int x, int y, int width, int height, int id, StructurePanel p) {
		super(x, y, width, height, id, p);
		this.panel.addGui(stand);
		stand.setPresent(true);
		
		// Setup times
		this.morningShiftStart = new CityTime(8, 00);
		this.morningShiftEnd = new CityTime(14, 00);
		this.afternoonShiftStart = new CityTime(15, 30);
		this.closingTime = new CityTime(20, 00);
		
		// Setup all roles that are persistent in this Restaurant
		this.host = new RestaurantHostRoleMatt();
		HostGuiMatt hostGui = new HostGuiMatt((RestaurantHostRoleMatt) host);
		hostGui.setPresent(false);
		((RestaurantHostRoleMatt) host).setGui(hostGui);
		this.panel.addGui(hostGui);
		host.setRestaurant(this);
		
		this.cook = new RestaurantCookRoleMatt();
		CookGuiMatt cookGui = new CookGuiMatt((RestaurantCookRoleMatt) cook);
		cookGui.setPresent(false);
		((RestaurantCookRoleMatt) cook).setGui(cookGui);
		((RestaurantCookRoleMatt) cook).setRotatingStand(stand);
		this.panel.addGui(cookGui);
		cook.setRestaurant(this);
		
		
		this.cashier = new RestaurantCashierRoleMatt();
		CashierGuiMatt cashierGui = new CashierGuiMatt((RestaurantCashierRoleMatt) cashier);
		cashierGui.setPresent(false);
		((RestaurantCashierRoleMatt) cashier).setGui(cashierGui);
		((RestaurantCashierRoleMatt) cashier).setHost((RestaurantHostRoleMatt) host);
		this.panel.addGui(cashierGui);
		cashier.setRestaurant(this);
		
		this.waiters = Collections.synchronizedList(new ArrayList<RestaurantWaiterRole>());
		for (int i = 0; i < INITIALWAITERS; i++) {
			RestaurantWaiterRoleMatt newWaiter;
			if (i % 2 == 0) {
				newWaiter = new RestaurantWaiterRoleMattNormal();
			} else {
				newWaiter = new RestaurantWaiterRoleMattStand();
			}
			WaiterGuiMatt waiterGui = new WaiterGuiMatt((RestaurantWaiterRoleMatt) newWaiter, null);
			waiterGui.setPresent(false);
			((RestaurantWaiterRoleMatt) newWaiter).setGui(waiterGui);
			((RestaurantWaiterRoleMatt) newWaiter).setRotatingStand(stand);
			waiters.add(newWaiter);
			this.panel.addGui(waiterGui);
			newWaiter.setRestaurant(this);
		}
	}
	
	@Override
	public Role getRole(Intention role) {
		switch (role) {
		case RestaurantCook: {
			if (cook.getPerson() == null) {
				((RestaurantCookRoleMatt) cook).getGui().setPresent(true);
				this.configPanel.updateInfo(this);
				return cook;
			}
			return null;
		}
		case RestaurantHost: {
			if (host.getPerson() == null) {
				this.configPanel.updateInfo(this);
				return host;
			}
			return null;
		}
		case RestaurantWaiter: {
			synchronized(waiters) {
				for (RestaurantWaiterRole r : waiters) {
					if (r.getPerson() == null) {
						((RestaurantHostRoleMatt) host).addWaiter((RestaurantWaiterRoleMatt) r);
						UpdateWaiterHomePositions();
						((RestaurantWaiterRoleMatt) r).getGui().setPresent(true);
						this.configPanel.updateInfo(this);
						return r;
					}
				}
				
				if (waiters.size() < MAXWAITERS) {
					RestaurantWaiterRole newWaiter;
					if (waiters.size() % 2 == 0) {
						newWaiter = new RestaurantWaiterRoleMattNormal();
					} else {
						newWaiter = new RestaurantWaiterRoleMattStand();
					}
					WaiterGuiMatt waiterGui = new WaiterGuiMatt((RestaurantWaiterRoleMatt) newWaiter, null);
					((RestaurantWaiterRoleMatt) newWaiter).setGui(waiterGui);
					waiters.add(newWaiter);
					((RestaurantHostRoleMatt) host).addWaiter((RestaurantWaiterRoleMatt) newWaiter);
					UpdateWaiterHomePositions();
					((RestaurantWaiterRoleMatt) newWaiter).setRotatingStand(stand);
					this.panel.addGui(waiterGui);
					newWaiter.setRestaurant(this);
					((RestaurantWaiterRoleMatt) newWaiter).getGui().setPresent(true);
					this.configPanel.updateInfo(this);
					return newWaiter;
				}
			}
			
			return null;
		}
		case RestaurantCashier: {
			if (cashier.getPerson() == null) {
				((RestaurantCashierRoleMatt) cashier).getGui().setPresent(true);
				this.configPanel.updateInfo(this);
				return cashier;
			}
			return null;
		}
		case RestaurantCustomer: {
			RestaurantCustomerRoleMatt newCustomer = new RestaurantCustomerRoleMatt();
			CustomerGuiMatt customerGui = new CustomerGuiMatt((RestaurantCustomerRoleMatt) newCustomer, null);
			((RestaurantCustomerRoleMatt) newCustomer).setGui(customerGui);
			newCustomer.setCashier((RestaurantCashierRoleMatt) cashier);
			newCustomer.setHost((RestaurantHostRoleMatt) host);
			this.panel.addGui(customerGui);
			newCustomer.setRestaurant(this);
			return newCustomer;
		}
		default: {
			AlertLog.getInstance().logWarning(AlertTag.RESTAURANT, this.toString(), "Wrong Intention provided in getRole(Intention)");
			return null;
		}
		}
	}
	
	private void checkIfRestaurantShouldOpen(CityTime time) {
		// If it's not during shift hours, there's no way the restaurant would be open
		if (!(CityTime.timeDifference(time, morningShiftStart) >= 0 && CityTime.timeDifference(time, morningShiftEnd) < 0) &&
				!(CityTime.timeDifference(time, afternoonShiftStart) >= 0 && CityTime.timeDifference(time, closingTime) < 0)) {
			return;
		}
		
		if (host.getPerson() != null && cashier.getPerson() != null && cook.getPerson() != null) {
			for (RestaurantWaiterRole w : waiters) {
				if (w.getPerson() != null) {
					AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, this.toString(), "Open for business!");
					this.isOpen = true;
					return;
				}
			}
		}
	}
	
	private void UpdateWaiterHomePositions() {		
		int initialX = (int)(RestaurantAnimationPanelMatt.WINDOWX * .48f);
    	int initialY = (int)(RestaurantAnimationPanelMatt.WINDOWY * .18f);
    	int mult = (int)(RestaurantAnimationPanelMatt.WINDOWX * .024f);
    	int offset = (int)(RestaurantAnimationPanelMatt.WINDOWX * .048f);
    	
    	synchronized(waiters) {
    		for (RestaurantWaiterRoleMatt waiter : ((RestaurantHostRoleMatt) host).getActiveWaiters()) {
    			int x = initialX - (((RestaurantHostRoleMatt) host).getNumActiveWaiters() - 1) * mult;
    			int y = initialY;
				waiter.getGui().SetHomePosition(x, y);
				waiter.getGui().GoToWaitingPosition();
				initialX += offset;
    		}
    	}
    }

	@Override
	public void updateTime(CityTime time) {		
		if (time.equalsIgnoreDay(morningShiftStart) || time.equalsIgnoreDay(afternoonShiftStart)) {
			this.forceClosed = false;
		}
		
		if (time.equalsIgnoreDay(morningShiftEnd)) {
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, this.toString(), "Morning shift over!");
			this.isOpen = false;
			if (host.getPerson() != null) {
				host.msgClosingTime();
			} else {
				closingTime();
			}
			this.configPanel.updateInfo(this);
		} else if (time.equalsIgnoreDay(this.closingTime)) {
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, this.toString(), "It's closing time!");
			this.isOpen = false;
			if (host.getPerson() != null) {
				host.msgClosingTime();
			} else {
				closingTime();
			}
			this.configPanel.updateInfo(this);
		} else if (!isOpen && !forceClosed) {
			checkIfRestaurantShouldOpen(time);
			this.configPanel.updateInfo(this);
		}
	}
	
	@Override
	public void closingTime() {
		cashier.msgClosingTime();
		cook.msgClosingTime();
		for (RestaurantWaiterRole r : waiters) {
			r.msgClosingTime();
		}
	}

	@Override
	public void emptyEntireCookInventory() {
		((RestaurantCookRoleMatt) cook).emptyInventory();
		this.configPanel.updateInfo(this);
	}

	@Override
	public List<String> getCookInventory() {
		return ((RestaurantCookRoleMatt) cook).getInventory();
	}

}
