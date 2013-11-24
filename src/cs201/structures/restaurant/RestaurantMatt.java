package cs201.structures.restaurant;

import java.util.ArrayList;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.roles.restaurant.Matt.CashierGuiMatt;
import cs201.gui.roles.restaurant.Matt.CookGuiMatt;
import cs201.gui.roles.restaurant.Matt.CustomerGuiMatt;
import cs201.gui.roles.restaurant.Matt.WaiterGuiMatt;
import cs201.helper.CityTime;
import cs201.roles.Role;
import cs201.roles.restaurantRoles.RestaurantWaiterRole;
import cs201.roles.restaurantRoles.Matt.RestaurantCashierRoleMatt;
import cs201.roles.restaurantRoles.Matt.RestaurantCookRoleMatt;
import cs201.roles.restaurantRoles.Matt.RestaurantCustomerRoleMatt;
import cs201.roles.restaurantRoles.Matt.RestaurantHostRoleMatt;
import cs201.roles.restaurantRoles.Matt.RestaurantWaiterRoleMatt;

public class RestaurantMatt extends Restaurant {
	private final int INITIALWAITERS = 2;
	private final int MAXWAITERS = 4;
	
	public RestaurantMatt(int x, int y, int width, int height, int id) {
		super(x, y, width, height, id);
		
		// Setup all roles that are persistent in this Restaurant
		this.host = new RestaurantHostRoleMatt();
		
		this.cook = new RestaurantCookRoleMatt();
		CookGuiMatt cookGui = new CookGuiMatt((RestaurantCookRoleMatt) cook);
		((RestaurantCookRoleMatt) cook).setGui(cookGui);
		
		this.cashier = new RestaurantCashierRoleMatt();
		CashierGuiMatt cashierGui = new CashierGuiMatt((RestaurantCashierRoleMatt) cashier);
		((RestaurantCashierRoleMatt) cashier).setGui(cashierGui);
		((RestaurantCashierRoleMatt) cashier).setHost((RestaurantHostRoleMatt) host);
		
		this.waiters = new ArrayList<RestaurantWaiterRole>();
		for (int i = 0; i < INITIALWAITERS; i++) {
			RestaurantWaiterRoleMatt newWaiter = new RestaurantWaiterRoleMatt();
			WaiterGuiMatt waiterGui = new WaiterGuiMatt((RestaurantWaiterRoleMatt) newWaiter, null);
			((RestaurantWaiterRoleMatt) newWaiter).setGui(waiterGui);
			waiters.add(newWaiter);
		}
	}
	
	@Override
	public Role getRole(Intention role) {
		switch (role) {
		case RestaurantCook: {
			return (cook.getPerson() == null) ? cook : null;
		}
		case RestaurantHost: {
			return (host.getPerson() == null) ? host : null;
		}
		case RestaurantWaiter: {
			for (RestaurantWaiterRole r : waiters) {
				if (r.getPerson() == null) {
					((RestaurantHostRoleMatt) host).addWaiter((RestaurantWaiterRoleMatt) r);
					return r;
				}
			}
			
			if (waiters.size() < MAXWAITERS) {
				RestaurantWaiterRole newWaiter = new RestaurantWaiterRoleMatt();
				WaiterGuiMatt waiterGui = new WaiterGuiMatt((RestaurantWaiterRoleMatt) newWaiter, null);
				((RestaurantWaiterRoleMatt) newWaiter).setGui(waiterGui);
				waiters.add(newWaiter);
				((RestaurantHostRoleMatt) host).addWaiter((RestaurantWaiterRoleMatt) newWaiter);
				return newWaiter;
			}
			
			return null;
		}
		case RestaurantCashier: {
			return (cashier.getPerson() == null) ? cashier : null;
		}
		case RestaurantCustomer: {
			RestaurantCustomerRoleMatt newCustomer = new RestaurantCustomerRoleMatt();
			CustomerGuiMatt customerGui = new CustomerGuiMatt((RestaurantCustomerRoleMatt) newCustomer, null);
			((RestaurantCustomerRoleMatt) newCustomer).setGui(customerGui);
			newCustomer.setCashier((RestaurantCashierRoleMatt) cashier);
			newCustomer.setHost((RestaurantHostRoleMatt) host);
			return newCustomer;
		}
		default: {
			Do("Wrong Intention provided in getRole(Intention)");
			return null;
		}
		}
	}

	@Override
	public void updateTime(CityTime time) {
		if (this.closingTime != null && time.equalsIgnoreDay(this.closingTime)) {
			host.closingTime();
			cook.closingTime();
			cashier.closingTime();
			for (RestaurantWaiterRole r : waiters) {
				r.closingTime();
			}
		}
	}

}
