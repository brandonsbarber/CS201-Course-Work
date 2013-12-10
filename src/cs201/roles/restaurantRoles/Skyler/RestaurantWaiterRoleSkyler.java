package cs201.roles.restaurantRoles.Skyler;

import cs201.agents.PersonAgent.Intention;
import cs201.interfaces.roles.restaurant.Skyler.CustomerSkyler;
import cs201.interfaces.roles.restaurant.Skyler.WaiterSkyler;
import cs201.roles.restaurantRoles.RestaurantWaiterRole;

public class RestaurantWaiterRoleSkyler extends RestaurantWaiterRole implements
		WaiterSkyler {

	public RestaurantWaiterRoleSkyler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgCheckReady(CustomerSkyler c, double amt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startInteraction(Intention intent) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void msgClosingTime() {
		// TODO Auto-generated method stub

	}

}
