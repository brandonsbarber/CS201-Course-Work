package cs201.roles.restaurantRoles;

import cs201.agents.PersonAgent.Intention;
import cs201.roles.Role;

public abstract class RestaurantHostRole extends Role {

	public RestaurantHostRole() {
		super();
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public abstract void startInteraction(Intention intent);

	@Override
	public abstract boolean pickAndExecuteAnAction();

	@Override
	public abstract void closingTime();

}
