package cs201.roles.restaurantRoles.Brandon;

import java.util.Map;

import cs201.interfaces.roles.restaurant.Brandon.HostBrandon;
import cs201.trace.AlertLog;
import cs201.trace.AlertTag;

public class RestaurantWaiterRoleBrandonNormal extends RestaurantWaiterRoleBrandon {

	public RestaurantWaiterRoleBrandonNormal(HostBrandon host,
			Map<String, Double> menu, String name) {
		super(host, menu, name);
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
			AlertLog.getInstance().logError(AlertTag.RESTAURANT,""+this,"Error with waiter going to kitchen");
		}
		
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT,""+this,"Giving order to cook");
		chef.msgPresentOrder(this, c.choice, c.t);
	}

}
