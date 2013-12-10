package cs201.roles.restaurantRoles.Skyler;

import cs201.agents.PersonAgent.Intention;
import cs201.interfaces.roles.restaurant.Skyler.CustomerSkyler;
import cs201.interfaces.roles.restaurant.Skyler.HostSkyler;
import cs201.interfaces.roles.restaurant.Skyler.WaiterSkyler;
import cs201.roles.restaurantRoles.RestaurantHostRole;

public class RestaurantHostRoleSkyler extends RestaurantHostRole implements
		HostSkyler {

	public RestaurantHostRoleSkyler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgWaiterReady(WaiterSkyler w) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgIWantFood(CustomerSkyler cust) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgIWantABreak(WaiterSkyler w) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgBackFromBreak(WaiterSkyler w) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgTableFree(int tableNum) {
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
