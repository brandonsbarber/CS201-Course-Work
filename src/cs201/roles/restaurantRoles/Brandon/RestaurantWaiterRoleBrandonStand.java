package cs201.roles.restaurantRoles.Brandon;

import java.util.Map;

import cs201.interfaces.roles.restaurant.Brandon.HostBrandon;

public class RestaurantWaiterRoleBrandonStand extends RestaurantWaiterRoleBrandon{

	public RestaurantWaiterRoleBrandonStand(HostBrandon host,
			Map<String, Double> menu, String name) {
		super(host, menu, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void deliverOrder(MyCustomer c)
	{
		gui.doGoToStand();
		try
		{
			animationPause.acquire();
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		
		stand.addOrder(this, c.choice, c.t);
	}

}
