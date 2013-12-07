package cs201.structures.restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.StructurePanel;
import cs201.gui.roles.restaurant.Brandon.CustomerGuiBrandon;
import cs201.gui.roles.restaurant.Brandon.WaiterGuiBrandon;
import cs201.helper.CityTime;
import cs201.interfaces.roles.restaurant.Brandon.HostBrandon;
import cs201.roles.Role;
import cs201.roles.restaurantRoles.RestaurantWaiterRole;
import cs201.trace.AlertLog;
import cs201.trace.AlertTag;

import cs201.roles.restaurantRoles.Brandon.*;

import cs201.gui.structures.restaurant.*;

@SuppressWarnings("serial")
public class RestaurantBrandon extends Restaurant {

	private static final int MAXWAITERS = 4;

	public RestaurantBrandon(int x, int y, int width, int height, int id,
			StructurePanel p) {
		super(x, y, width, height, id, p);

		// Setup all roles that are persistent in this Restaurant
		this.host = new RestaurantHostRoleBrandon("Host",4);
		//HostGuiBrandon hostGui = new HostGuiBrandon((RestaurantHostRoleBrandon) host);
		//hostGui.setPresent(false);
		//(RestaurantHostRoleMatt) host).setGui(hostGui);
		//this.panel.addGui(hostGui);
		host.setRestaurant(this);
			
		this.cook = new RestaurantCookRoleBrandon(null, null, null);
		//CookGuiMatt cookGui = new CookGuiMatt((RestaurantCookRoleMatt) cook);
		//cookGui.setPresent(false);
		//((RestaurantCookRoleBrandon) cook).setGui(cookGui);
		//((RestaurantCookRoleBrandon) cook).setRotatingStand(stand);
		//this.panel.addGui(cookGui);
		cook.setRestaurant(this);
			
		this.cashier = new RestaurantCashierRoleBrandon(null, 0);
		//CashierGuiMatt cashierGui = new CashierGuiMatt((RestaurantCashierRoleMatt) cashier);
		//cashierGui.setPresent(false);
		//((RestaurantCashierRoleBrandon) cashier).setGui(cashierGui);
		//((RestaurantCashierRoleBrandon) cashier).setHost((RestaurantHostRoleMatt) host);
		//this.panel.addGui(cashierGui);
		cashier.setRestaurant(this);
				
		this.waiters = Collections.synchronizedList(new ArrayList<RestaurantWaiterRole>());
	}

	@Override
	public void closingTime() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Role getRole(Intention role) {
		switch (role) {
		case RestaurantCook: {
			if (cook.getPerson() == null) {
				//((RestaurantCookRoleBrandon) cook).getGui().setPresent(true);
				return cook;
			}
			return null;
		}
		case RestaurantHost: {
			if (host.getPerson() == null) {
				return host;
			}
			return null;
		}
		case RestaurantWaiter: {
			synchronized(waiters) {
				for (RestaurantWaiterRole r : waiters) {
					if (r.getPerson() == null) {
						((RestaurantHostRoleBrandon) host).addWaiter((RestaurantWaiterRoleBrandon) r);
						//UpdateWaiterHomePositions();
						((RestaurantWaiterRoleBrandon) r).getGui().setPresent(true);
						return r;
					}
				}
				
				if (waiters.size() < MAXWAITERS) {
					RestaurantWaiterRole newWaiter = new RestaurantWaiterRoleBrandon((HostBrandon)host,new HashMap<String,java.lang.Double>(),"");
					System.out.println("ADDING A WAITER");
					/*if (waiters.size() % 2 == 0) {
						newWaiter = new RestaurantWaiterRoleMattNormal();
					} else {
						newWaiter = new RestaurantWaiterRoleMattStand();
					}*/
					WaiterGuiBrandon waiterGui = new WaiterGuiBrandon((RestaurantWaiterRoleBrandon) newWaiter, null);
					((RestaurantWaiterRoleBrandon) newWaiter).setGui(waiterGui);
					waiters.add(newWaiter);
					waiterGui.setPresent(true);
					waiterGui.setTables(((RestaurantAnimationPanelBrandon)panel).getTables());
					((RestaurantHostRoleBrandon) host).addWaiter((RestaurantWaiterRoleBrandon) newWaiter);
					//UpdateWaiterHomePositions();
					//((RestaurantWaiterRoleBrandon) newWaiter).setRotatingStand(stand);
					this.panel.addGui(waiterGui);
					System.out.println(this.panel);
					newWaiter.setRestaurant(this);
					((RestaurantWaiterRoleBrandon) newWaiter).getGui().setPresent(true);
					return newWaiter;
				}
			}
			
			return null;
		}
		case RestaurantCashier: {
			if (cashier.getPerson() == null) {
				//((RestaurantCashierRoleBrandon) cashier).getGui().setPresent(true);
				return cashier;
			}
			return null;
		}
		case RestaurantCustomer: {
			RestaurantCustomerRoleBrandon newCustomer = new RestaurantCustomerRoleBrandon("",(RestaurantHostRoleBrandon)host);
			CustomerGuiBrandon customerGui = new CustomerGuiBrandon((RestaurantCustomerRoleBrandon) newCustomer);
			((RestaurantCustomerRoleBrandon) newCustomer).setGui(customerGui);
			//newCustomer.setCashier((RestaurantCashierRoleBrandon) cashier);
			//newCustomer.setHost((RestaurantHostRoleBrandon) host);
			this.panel.addGui(customerGui);
			newCustomer.setRestaurant(this);
			return newCustomer;
		}
		default: {
			AlertLog.getInstance().logWarning(AlertTag.RESTAURANT, this.toString(), "Wrong Intention provided in getRole(Intention)");
			return null;
		}
		}
	}

	@Override
	public void updateTime(CityTime time) {
		// TODO Auto-generated method stub
		
	}

}
