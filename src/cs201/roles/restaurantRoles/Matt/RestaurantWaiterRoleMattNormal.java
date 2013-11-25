package cs201.roles.restaurantRoles.Matt;

public class RestaurantWaiterRoleMattNormal extends RestaurantWaiterRoleMatt {

	public RestaurantWaiterRoleMattNormal(RestaurantCookRoleMatt cook,
			RestaurantHostRoleMatt host, RestaurantCashierRoleMatt cashier) {
		super(cook, host, cashier);
	}

	public RestaurantWaiterRoleMattNormal() { }

	@Override
	protected void GiveOrderToCook(MyCustomer m) {
		m.state = CustomerState.none;
		DoGiveOrderToCook(m);
		((RestaurantCookRoleMatt) restaurant.getCook()).msgHereIsAnOrder(this, m.choice, m.tableNumber);
	}

}
