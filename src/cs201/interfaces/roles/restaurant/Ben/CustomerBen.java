package cs201.interfaces.roles.restaurant.Ben;

import cs201.roles.restaurantRoles.Ben.RestaurantWaiterRoleBen.Menu;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface CustomerBen {

	public void gotHungry();
	
	public void msgItWillBeAwhile();
	
	public void msgFollowMeToTable(int tablenumber, WaiterBen waiter, Menu menu);
	
	public void msgWhatWouldYouLike();
	
	public void msgHereIsYourFood();
	
	public void msgOutOf(Menu newMenu);
	
	public void msgHereIsCheck(float amount);
	
	public void msgAnimationFinishedGoToSeat();
	
	public void msgAnimationFinishedLeaveRestaurant();

	public String getName();

}