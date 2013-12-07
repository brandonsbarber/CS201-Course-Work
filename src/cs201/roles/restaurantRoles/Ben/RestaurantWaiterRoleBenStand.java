package cs201.roles.restaurantRoles.Ben;

import cs201.helper.Ben.RestaurantRotatingStandBen.RotatingStandOrderBen;
import cs201.helper.Matt.RestaurantRotatingStand.RotatingStandOrder;
import cs201.interfaces.roles.restaurant.Ben.WaiterBen;

/**
 * Restaurant Host Agent
 */
public class RestaurantWaiterRoleBenStand extends RestaurantWaiterRoleBen implements WaiterBen {
	
	protected void placeOrder(MyCustomer customer) {
		// Walk to the stand
		waiterGui.DoWalkToStand();
		pauseForAnimation();
		
		// Place the order on the stand
		RotatingStandOrderBen newOrder = new RotatingStandOrderBen(this, customer.choice, customer.table);
		stand.addOrder(newOrder);
		
		// The customer's order has now been placed
		customer.state = CustomerState.placedOrder;
	}
}

