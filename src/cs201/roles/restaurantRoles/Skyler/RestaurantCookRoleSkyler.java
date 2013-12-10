package cs201.roles.restaurantRoles.Skyler;

import cs201.agents.PersonAgent.Intention;
import cs201.interfaces.roles.restaurant.Skyler.CookSkyler;
import cs201.interfaces.roles.restaurant.Skyler.WaiterSkyler;
import cs201.roles.restaurantRoles.RestaurantCookRole;

public class RestaurantCookRoleSkyler extends RestaurantCookRole implements
		CookSkyler {

	public RestaurantCookRoleSkyler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgHereIsOrder(WaiterSkyler w, String choice, int tableNum) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgImBackFor(int tableNum) {
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
