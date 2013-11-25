package cs201.helper.Matt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cs201.roles.restaurantRoles.Matt.RestaurantWaiterRoleMatt;

public class RestaurantRotatingStand {
	private List<RotatingStandOrder> orders = Collections.synchronizedList(new ArrayList<RotatingStandOrder>());
	
	public RestaurantRotatingStand() {
		// TODO Auto-generated constructor stub
	}
	
	public void addOrder(RestaurantWaiterRoleMatt waiter, String choice, int tableNum) {
		orders.add(new RotatingStandOrder(waiter, choice, tableNum));
	}
	
	public RotatingStandOrder removeOrder() {
		if (orders.size() > 0) {
			return orders.get(0);
		}
		return null;
	}
	
	public class RotatingStandOrder {
		public RestaurantWaiterRoleMatt waiter;
		public String choice;
		public int tableNum;
		
		public RotatingStandOrder() {
			this.waiter = null;
			this.choice = "";
			this.tableNum = -1;
		}
		
		public RotatingStandOrder(RestaurantWaiterRoleMatt waiter, String choice, int tableNum) {
			this.waiter = waiter;
			this.choice = choice;
			this.tableNum = tableNum;
		}
	}
	
}
