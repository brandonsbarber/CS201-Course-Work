package cs201.roles.restaurantRoles.Ben;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Semaphore;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.roles.restaurant.Ben.WaiterGuiBen;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelBen;
import cs201.interfaces.roles.restaurant.Ben.CashierBen;
import cs201.interfaces.roles.restaurant.Ben.CookBen;
import cs201.interfaces.roles.restaurant.Ben.CustomerBen;
import cs201.interfaces.roles.restaurant.Ben.HostBen;
import cs201.interfaces.roles.restaurant.Ben.WaiterBen;
import cs201.roles.restaurantRoles.RestaurantWaiterRole;
import cs201.roles.restaurantRoles.Ben.RestaurantWaiterRoleBen.CustomerState;
import cs201.roles.restaurantRoles.Ben.RestaurantWaiterRoleBen.MyCustomer;

/**
 * Restaurant Host Agent
 */
public class RestaurantWaiterRoleBenNormal extends RestaurantWaiterRoleBen implements WaiterBen {
	
	protected void placeOrder(MyCustomer customer) {
		// Walk to the cook
		waiterGui.DoWalkToCookingArea();
		pauseForAnimation();
		
		// Give the cook the customer's order
		cook.msgHereIsOrder(this, customer.choice, customer.table);
		
		// The customer's order has now been placed
		customer.state = CustomerState.placedOrder;
	}
}

