package cs201.structures.restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.StructurePanel;
import cs201.gui.roles.restaurant.Ben.CustomerGuiBen;
import cs201.gui.roles.restaurant.Ben.WaiterGuiBen;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelBen;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelMatt;
import cs201.helper.CityTime;
import cs201.helper.Ben.RestaurantRotatingStandBen;
import cs201.roles.Role;
import cs201.roles.restaurantRoles.RestaurantWaiterRole;
import cs201.roles.restaurantRoles.Ben.RestaurantCashierRoleBen;
import cs201.roles.restaurantRoles.Ben.RestaurantCookRoleBen;
import cs201.roles.restaurantRoles.Ben.RestaurantCustomerRoleBen;
import cs201.roles.restaurantRoles.Ben.RestaurantHostRoleBen;
import cs201.roles.restaurantRoles.Ben.RestaurantWaiterRoleBen;
import cs201.roles.restaurantRoles.Ben.RestaurantWaiterRoleBenNormal;
import cs201.roles.restaurantRoles.Ben.RestaurantWaiterRoleBenStand;
import cs201.roles.restaurantRoles.Matt.RestaurantHostRoleMatt;
import cs201.roles.restaurantRoles.Matt.RestaurantWaiterRoleMatt;
import cs201.trace.AlertLog;
import cs201.trace.AlertTag;

/**
 * Ben Doherty's Restaurant Structure for SimCity201
 * @author Ben Doherty
 *
 */
public class RestaurantBen extends Restaurant {
	private final int INITIALWAITERS = 2;
	private final int MAXWAITERS = 4;
	private RestaurantRotatingStandBen stand = new RestaurantRotatingStandBen();
	
	public RestaurantBen(int x, int y, int width, int height, int id, StructurePanel p) {
		super(x, y, width, height, id, p);
		
		// Set up the stand
		this.panel.addGui(stand);
		stand.setPresent(true);
		
		// Setup times
		this.morningShiftStart = new CityTime(8, 00);
		this.morningShiftEnd = new CityTime(12, 30);
		this.afternoonShiftStart = new CityTime(13, 00);
		this.closingTime = new CityTime(18, 00);
		
		// Setup all roles that are persistent in this Restaurant
		this.host = new RestaurantHostRoleBen();
		host.setRestaurant(this);
		
		this.cashier = new RestaurantCashierRoleBen();
		cashier.setRestaurant(this);
		
		this.cook = new RestaurantCookRoleBen();
		((RestaurantCookRoleBen) cook).setRotatingStand(stand);
		cook.setRestaurant(this);
		((RestaurantCookRoleBen) cook).setAnimPanel((RestaurantAnimationPanelBen) this.panel);
		((RestaurantCookRoleBen) cook).setCashier((RestaurantCashierRoleBen) cashier);
		
		
		this.waiters = Collections.synchronizedList(new ArrayList<RestaurantWaiterRole>());
		for (int i = 0; i < INITIALWAITERS; i++) {
			RestaurantWaiterRoleBen newWaiter;
			
			if (i % 2 == 0) {
				newWaiter = new RestaurantWaiterRoleBenNormal();
			} else {
				newWaiter = new RestaurantWaiterRoleBenStand();
			}
			
			WaiterGuiBen waiterGui = new WaiterGuiBen(newWaiter);
			waiterGui.setPresent(false);
			newWaiter.setGui(waiterGui);
			newWaiter.setRotatingStand(stand);
			waiters.add(newWaiter);
			this.panel.addGui(waiterGui);
			newWaiter.setRestaurant(this);
			newWaiter.setCook((RestaurantCookRoleBen) this.cook);
			newWaiter.setCashier((RestaurantCashierRoleBen) this.cashier);
			newWaiter.setHost((RestaurantHostRoleBen) this.host);
			newWaiter.setAnimPanel((RestaurantAnimationPanelBen) this.panel);
		}
	}
	
	@Override
	public Role getRole(Intention role) {
		switch (role) {
		case RestaurantCook: {
			if (cook.getPerson() == null) {
				return cook;
			}
			return null;
		}
		case RestaurantHost: {
			if (host.getPerson() == null) {
				return host;
			}
			return null;
		}
		case RestaurantWaiter: {
			synchronized(waiters) {
				for (RestaurantWaiterRole r : waiters) {
					if (r.getPerson() == null) {
						((RestaurantHostRoleBen) host).addWaiter((RestaurantWaiterRoleBen) r);
						((RestaurantWaiterRoleBen) r).getGui().setPresent(true);
						return r;
					}
				}
				
				if (waiters.size() < MAXWAITERS) {
					RestaurantWaiterRoleBen newWaiter;
					if (waiters.size() % 2 == 0) {
						newWaiter = new RestaurantWaiterRoleBenNormal();
					} else {
						newWaiter = new RestaurantWaiterRoleBenStand();
					}
					
					WaiterGuiBen waiterGui = new WaiterGuiBen(newWaiter);
					newWaiter.setGui(waiterGui);
					newWaiter.setRotatingStand(stand);
					waiters.add(newWaiter);
					((RestaurantHostRoleBen) host).addWaiter(newWaiter);
					this.panel.addGui(waiterGui);
					newWaiter.setRestaurant(this);
					newWaiter.getGui().setPresent(true);
					newWaiter.setCook((RestaurantCookRoleBen) this.cook);
					newWaiter.setCashier((RestaurantCashierRoleBen) this.cashier);
					newWaiter.setHost((RestaurantHostRoleBen) this.host);
					newWaiter.setAnimPanel((RestaurantAnimationPanelBen) this.panel);
					return newWaiter;
				}
			}
			
			return null;
		}
		case RestaurantCashier: {
			if (cashier.getPerson() == null) {
				return cashier;
			}
			return null;
		}
		case RestaurantCustomer: {			
			RestaurantCustomerRoleBen newCustomer = new RestaurantCustomerRoleBen();
			CustomerGuiBen customerGui = new CustomerGuiBen(newCustomer, (RestaurantAnimationPanelBen) this.panel);
			newCustomer.setGui(customerGui);
			newCustomer.setCashier((RestaurantCashierRoleBen) cashier);
			newCustomer.setHost((RestaurantHostRoleBen) host);
			this.panel.addGui(customerGui);
			newCustomer.setRestaurant(this);
			return newCustomer;
		}
		default: {
			Do("Wrong Intention provided in getRole(Intention)");
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
		if (time.equalsIgnoreDay(morningShiftEnd)) {
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, this.toString(), "Morning shift over!");
			this.isOpen = false;
			if (host.getPerson() != null) {
				host.msgClosingTime();
			} else {
				closingTime();
			}
		} else if (time.equalsIgnoreDay(this.closingTime)) {
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, this.toString(), "It's closing time!");
			this.isOpen = false;
			if (host.getPerson() != null) {
				host.msgClosingTime();
			} else {
				closingTime();
			}
		} else if (!isOpen) {
			checkIfRestaurantShouldOpen(time);
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
	public void closeRestaurant() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void emptyEntireCookInventory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getCookInventory() {
		// TODO Auto-generated method stub
		return null;
	}

}
