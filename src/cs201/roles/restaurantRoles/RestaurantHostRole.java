package cs201.roles.restaurantRoles;

import cs201.agents.PersonAgent.Intention;
import cs201.roles.Role;
import cs201.structures.restaurant.Restaurant;

/**
 * Base class for all RestaurantHosts in SimCity201.
 * @author Matthew Pohlmann
 */
public abstract class RestaurantHostRole extends Role {
	protected Restaurant restaurant;
	
	public RestaurantHostRole() {
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
