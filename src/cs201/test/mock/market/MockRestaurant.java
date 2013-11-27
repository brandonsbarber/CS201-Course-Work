package cs201.test.mock.market;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.StructurePanel;
import cs201.helper.CityTime;
import cs201.roles.Role;
import cs201.structures.restaurant.Restaurant;
import cs201.test.mock.EventLog;

public class MockRestaurant extends Restaurant {
	
	EventLog log = new EventLog();

	public MockRestaurant(int x, int y, int width, int height, int id,
			StructurePanel p) {
		super(x, y, width, height, id, p);
		// TODO Auto-generated constructor stub
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
