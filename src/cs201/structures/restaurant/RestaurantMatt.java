package cs201.structures.restaurant;

import cs201.agents.PersonAgent.Intention;
import cs201.roles.Role;
import cs201.roles.restaurantRoles.RestaurantWaiterRole;
import cs201.roles.restaurantRoles.Matt.RestaurantCashierRoleMatt;
import cs201.roles.restaurantRoles.Matt.RestaurantCustomerRoleMatt;
import cs201.roles.restaurantRoles.Matt.RestaurantHostRoleMatt;
import cs201.roles.restaurantRoles.Matt.RestaurantWaiterRoleMatt;

public class RestaurantMatt extends Restaurant {

	public RestaurantMatt(int x, int y, int width, int height, int id) {
		super(x, y, width, height, id);
		
		// TODO Auto-generated constructor stub
		
		// Setup all roles
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

}
