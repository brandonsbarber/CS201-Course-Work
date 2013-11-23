package cs201.structures.restaurant;

import java.util.ArrayList;

import cs201.agents.PersonAgent.Intention;
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
		
		// Setup all roles
		this.host = new RestaurantHostRoleMatt();
		this.cook = new RestaurantCookRoleMatt();
		this.cashier = new RestaurantCashierRoleMatt();
		this.waiters = new ArrayList<RestaurantWaiterRole>();
		for (int i = 0; i < INITIALWAITERS; i++) {
			RestaurantWaiterRoleMatt newWaiter = new RestaurantWaiterRoleMatt();
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
				waiters.add(newWaiter);
				return newWaiter;
			}
			
			return null;
		}
		case RestaurantCashier: {
			return (cashier.getPerson() == null) ? cashier : null;
		}
		case RestaurantCustomer: {
			RestaurantCustomerRoleMatt newCustomer = new RestaurantCustomerRoleMatt();
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
		// TODO Auto-generated method stub
		
	}

}
