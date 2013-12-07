package cs201.structures.restaurant;

import java.util.ArrayList;
import java.util.Collections;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.StructurePanel;
import cs201.gui.roles.restaurant.Ben.CustomerGuiBen;
import cs201.gui.roles.restaurant.Ben.WaiterGuiBen;
import cs201.gui.roles.restaurant.Matt.CashierGuiMatt;
import cs201.gui.roles.restaurant.Matt.CookGuiMatt;
import cs201.gui.roles.restaurant.Matt.CustomerGuiMatt;
import cs201.gui.roles.restaurant.Matt.HostGuiMatt;
import cs201.gui.roles.restaurant.Matt.WaiterGuiMatt;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelBen;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelMatt;
import cs201.helper.CityTime;
import cs201.helper.Ben.RestaurantRotatingStandBen;
import cs201.helper.Matt.RestaurantRotatingStand;
import cs201.roles.Role;
import cs201.roles.restaurantRoles.RestaurantWaiterRole;
import cs201.roles.restaurantRoles.Ben.RestaurantCashierRoleBen;
import cs201.roles.restaurantRoles.Ben.RestaurantCookRoleBen;
import cs201.roles.restaurantRoles.Ben.RestaurantCustomerRoleBen;
import cs201.roles.restaurantRoles.Ben.RestaurantHostRoleBen;
import cs201.roles.restaurantRoles.Ben.RestaurantWaiterRoleBen;
import cs201.roles.restaurantRoles.Ben.RestaurantWaiterRoleBenNormal;
import cs201.roles.restaurantRoles.Ben.RestaurantWaiterRoleBenStand;
import cs201.roles.restaurantRoles.Matt.RestaurantCashierRoleMatt;
import cs201.roles.restaurantRoles.Matt.RestaurantCookRoleMatt;
import cs201.roles.restaurantRoles.Matt.RestaurantCustomerRoleMatt;
import cs201.roles.restaurantRoles.Matt.RestaurantHostRoleMatt;
import cs201.roles.restaurantRoles.Matt.RestaurantWaiterRoleMatt;
import cs201.roles.restaurantRoles.Matt.RestaurantWaiterRoleMattNormal;
import cs201.roles.restaurantRoles.Matt.RestaurantWaiterRoleMattStand;

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
		
		// Setup all roles that are persistent in this Restaurant
		this.host = new RestaurantHostRoleBen();
		host.setRestaurant(this);
		
		this.cook = new RestaurantCookRoleBen();
		((RestaurantCookRoleBen) cook).setRotatingStand(stand);
		cook.setRestaurant(this);
		((RestaurantCookRoleBen) cook).setAnimPanel((RestaurantAnimationPanelBen) this.panel);
		
		this.cashier = new RestaurantCashierRoleBen();
		cashier.setRestaurant(this);
		
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
	
	private void checkIfRestaurantShouldOpen() {
		if (host.getPerson() != null && cashier.getPerson() != null && cook.getPerson() != null) {
			for (RestaurantWaiterRole w : waiters) {
				if (w.getPerson() != null) {
					Do("Open for business!");
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
		if (!isOpen) {
			checkIfRestaurantShouldOpen();
		}
		
		if (time.equalsIgnoreDay(this.closingTime)) {
			Do("It's closing time!");
			if (host.getPerson() != null) {
				host.msgClosingTime();
			} else {
				closingTime();
			}
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

}
