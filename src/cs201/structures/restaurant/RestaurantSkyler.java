package cs201.structures.restaurant;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.StructurePanel;
import cs201.helper.CityTime;
import cs201.roles.Role;
import cs201.roles.restaurantRoles.Skyler.RestaurantHostRoleSkyler;

public class RestaurantSkyler extends Restaurant {
	
	private final int numWaiters = 2;

	public RestaurantSkyler(int x, int y, int width, int height, int id,
			StructurePanel p) {
		super(x, y, width, height, id, p);
		
		this.morningShiftStart = new CityTime(8, 00);
		this.morningShiftEnd = new CityTime(12, 30);
		this.afternoonShiftStart = new CityTime(13, 00);
		this.closingTime = new CityTime(18, 00);
		
		this.host = new RestaurantHostRoleSkyler();
	}

	@Override
	public void closingTime() {
		// TODO Auto-generated method stub

	}

	@Override
	public Role getRole(Intention role) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateTime(CityTime time) {
		// TODO Auto-generated method stub

	}

}
