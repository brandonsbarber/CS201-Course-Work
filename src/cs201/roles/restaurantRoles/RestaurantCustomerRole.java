package cs201.roles.restaurantRoles;

import cs201.agents.PersonAgent.Intention;
import cs201.roles.Role;
import cs201.structures.restaurant.Restaurant;

public abstract class RestaurantCustomerRole extends Role {
	protected Restaurant restaurant;
	
	public RestaurantCustomerRole() {
		super();
		
		this.restaurant = null;
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public abstract void startInteraction(Intention intent);

	@Override
	public abstract boolean pickAndExecuteAnAction();

	@Override
	public abstract void closingTime();
	
	public void setRestaurant(Restaurant m) {
		this.restaurant = m;
	}

}
