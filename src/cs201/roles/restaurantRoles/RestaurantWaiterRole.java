package cs201.roles.restaurantRoles;

import cs201.agents.PersonAgent.Intention;
import cs201.roles.Role;
import cs201.structures.restaurant.Restaurant;

/**
 * Base class for all RestaurantWaiters in SimCity201.
 * @author Matthew Pohlmann
 */
public abstract class RestaurantWaiterRole extends Role {
	protected Restaurant restaurant;
	
	public RestaurantWaiterRole() {
		super();
		
		this.restaurant = null;
	}

	@Override
	public abstract void startInteraction(Intention intent);

	@Override
	public abstract boolean pickAndExecuteAnAction();

	@Override
	public abstract void msgClosingTime();
	
	public void setRestaurant(Restaurant m) {
		this.restaurant = m;
	}
	
}
