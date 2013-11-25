package cs201.roles.restaurantRoles.Matt;

public class RestaurantWaiterRoleMattStand extends RestaurantWaiterRoleMatt {
	
	public RestaurantWaiterRoleMattStand(RestaurantCookRoleMatt cook,
			RestaurantHostRoleMatt host, RestaurantCashierRoleMatt cashier) {
		super(cook, host, cashier);
	}

	public RestaurantWaiterRoleMattStand() { }

	@Override
	protected void GiveOrderToCook(MyCustomer m) {
		m.state = CustomerState.none;
		DoPutOrderOnStand(m);
		this.stand.addOrder(this, m.choice, m.tableNumber);
	}

}
