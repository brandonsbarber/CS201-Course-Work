package cs201.roles.restaurantRoles.Brandon;

import java.util.Map;

import cs201.interfaces.roles.restaurant.Brandon.HostBrandon;

public class RestaurantWaiterRoleBrandonNormal extends RestaurantWaiterRoleBrandon {

	public RestaurantWaiterRoleBrandonNormal(HostBrandon host,
			Map<String, Double> menu, String name) {
		super(host, menu, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void deliverOrder(MyCustomer c) {
		gui.doGoToKitchen(c.t);
		try
		{
			animationPause.acquire();
		}
		catch(InterruptedException e)
		{
			System.out.println(this+": Error with waiter going to kitchen");
		}
		
		System.out.println(this+": Giving order to cook");
		chef.msgPresentOrder(this, c.choice, c.t);
	}

}
