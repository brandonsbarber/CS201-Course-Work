package cs201.roles.restaurantRoles.Skyler;

import cs201.agents.PersonAgent.Intention;
import cs201.interfaces.roles.restaurant.Skyler.CustomerSkyler;
import cs201.roles.restaurantRoles.RestaurantCustomerRole;

public class RestaurantCustomerRoleSkyler extends RestaurantCustomerRole
		implements CustomerSkyler {

	public RestaurantCustomerRoleSkyler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgHereIsYourTotal(double total) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgHereIsYourChange(double total) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgYouOweUs(double remaining_cost) {
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
