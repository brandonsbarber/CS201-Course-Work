package cs201.structures.restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.StructurePanel;
import cs201.gui.roles.restaurant.Brandon.CashierGuiBrandon;
import cs201.gui.roles.restaurant.Brandon.CookGuiBrandon;
import cs201.gui.roles.restaurant.Brandon.CustomerGuiBrandon;
import cs201.gui.roles.restaurant.Brandon.HostGuiBrandon;
import cs201.gui.roles.restaurant.Brandon.KitchenGuiBrandon;
import cs201.gui.roles.restaurant.Brandon.WaiterGuiBrandon;
import cs201.helper.CityTime;
import cs201.helper.Brandon.FoodBrandon;
import cs201.helper.Brandon.MenuBrandon;
import cs201.helper.Brandon.RestaurantRotatingStandBrandon;
import cs201.interfaces.roles.restaurant.Brandon.CashierBrandon;
import cs201.interfaces.roles.restaurant.Brandon.CookBrandon;
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
	private RestaurantRotatingStandBrandon stand;

	public RestaurantBrandon(int x, int y, int width, int height, int id, StructurePanel p) {
		super(x, y, width, height, id, p);

		/*--------------------------------
		 * 
		 * -------------------------------
		 */
		prices = new HashMap<String,java.lang.Double>();
    	prices.put("Steak",15.99);
    	prices.put("Chicken",10.99);
    	prices.put("Salad",5.99);
    	prices.put("Pizza",8.99);
		
    	savedPrices = (HashMap<String,java.lang.Double>)prices.clone();
    	
		cookingTimes = new HashMap<String,FoodBrandon>();
		cookingTimes.put("Steak",new FoodBrandon ("Steak",1,2,2,1000,prices.get("Steak")));
		cookingTimes.put("Chicken",new FoodBrandon ("Chicken",1,2,2,1000,prices.get("Chicken")));
		cookingTimes.put("Salad",new FoodBrandon ("Salad",1,2,2,1000,prices.get("Salad")));
		cookingTimes.put("Pizza",new FoodBrandon ("Pizza",1,2,3,1000,prices.get("Pizza")));
		
		MenuBrandon menu = new MenuBrandon(prices);
		
		/*--------------------------------
		 * 
		 * -------------------------------
		 */
		
		kitchen = new KitchenGuiBrandon(4);
		
		this.panel.addGui(kitchen);
		
		stand = new RestaurantRotatingStandBrandon();
		
		this.panel.addGui(stand);
		
		// Setup all roles that are persistent in this Restaurant
		this.host = new RestaurantHostRoleBrandon("Host",4);
		HostGuiBrandon hostGui = new HostGuiBrandon((RestaurantHostRoleBrandon) host);
		hostGui.setPresent(false);
		((RestaurantHostRoleBrandon) host).setGui(hostGui);
		this.panel.addGui(hostGui);
		host.setRestaurant(this);
			
		this.cook = new RestaurantCookRoleBrandon(cookingTimes, prices, savedPrices);
		CookGuiBrandon cookGui = new CookGuiBrandon((RestaurantCookRoleBrandon) cook,kitchen);
		cookGui.setPresent(false);
		((RestaurantCookRoleBrandon) cook).setGui(cookGui);
		((RestaurantCookRoleBrandon) cook).setRotatingStand(stand);
		this.panel.addGui(cookGui);
		cook.setRestaurant(this);
			
		this.cashier = new RestaurantCashierRoleBrandon(cookingTimes, 0);
		CashierGuiBrandon cashierGui = new CashierGuiBrandon((RestaurantCashierRoleBrandon) cashier);
		cashierGui.setPresent(false);
		((RestaurantCashierRoleBrandon) cashier).setGui(cashierGui);
		//((RestaurantCashierRoleBrandon) cashier).setHost((RestaurantHostRoleMatt) host);
		this.panel.addGui(cashierGui);
		cashier.setRestaurant(this);
		
		((RestaurantCookRoleBrandon)cook).setKitchen(kitchen);
		
		this.waiters = Collections.synchronizedList(new ArrayList<RestaurantWaiterRole>());
	}
	
	KitchenGuiBrandon kitchen;
	HashMap<String,FoodBrandon> cookingTimes;
	HashMap<String,java.lang.Double> prices;
	HashMap<String,java.lang.Double> savedPrices;

	@Override
	public void closingTime() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Role getRole(Intention role) {
		switch (role) {
		case RestaurantCook: {
			if (cook.getPerson() == null) {
				((RestaurantCookRoleBrandon) cook).getGui().setPresent(true);
				return cook;
			}
			return null;
		}
		case RestaurantHost: {
			if (host.getPerson() == null) {
				((RestaurantHostRoleBrandon) host).getGui().setPresent(true);
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
					RestaurantWaiterRoleBrandon newWaiter = new RestaurantWaiterRoleBrandonStand((HostBrandon)host,prices,"");
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
					waiterGui.setKitchen(kitchen);
					waiterGui.setTables(((RestaurantAnimationPanelBrandon)panel).getTables());
					((RestaurantHostRoleBrandon) host).addWaiter((RestaurantWaiterRoleBrandon) newWaiter);
					//UpdateWaiterHomePositions();
					((RestaurantWaiterRoleBrandon) newWaiter).setRotatingStand(stand);
					this.panel.addGui(waiterGui);
					System.out.println(this.panel);
					newWaiter.setRestaurant(this);
					newWaiter.setChef((CookBrandon)cook);
					newWaiter.setCashier((CashierBrandon)cashier);
					((RestaurantWaiterRoleBrandon) newWaiter).getGui().setPresent(true);
					return newWaiter;
				}
			}
			
			return null;
		}
		case RestaurantCashier: {
			if (cashier.getPerson() == null) {
				((RestaurantCashierRoleBrandon) cashier).getGui().setPresent(true);
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
