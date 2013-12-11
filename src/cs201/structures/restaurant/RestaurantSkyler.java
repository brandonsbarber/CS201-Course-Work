package cs201.structures.restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.StructurePanel;
import cs201.gui.roles.restaurant.Skyler.CustomerGuiSkyler;
import cs201.gui.roles.restaurant.Skyler.WaiterGuiSkyler;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelSkyler;
import cs201.helper.CityTime;
import cs201.roles.Role;
import cs201.roles.restaurantRoles.RestaurantWaiterRole;
import cs201.roles.restaurantRoles.Skyler.RestaurantCashierRoleSkyler;
import cs201.roles.restaurantRoles.Skyler.RestaurantCookRoleSkyler;
import cs201.roles.restaurantRoles.Skyler.RestaurantCustomerRoleSkyler;
import cs201.roles.restaurantRoles.Skyler.RestaurantHostRoleSkyler;
import cs201.roles.restaurantRoles.Skyler.RestaurantWaiterRoleSkyler;
import cs201.trace.AlertLog;
import cs201.trace.AlertTag;

public class RestaurantSkyler extends Restaurant {
	
	private final int numWaiters = 2;

	public RestaurantSkyler(int x, int y, int width, int height, int id,
			StructurePanel p) {
		super(x, y, width, height, id, p);
		
		this.morningShiftStart = new CityTime(8, 00);
		this.morningShiftEnd = new CityTime(12, 30);
		this.afternoonShiftStart = new CityTime(13, 00);
		this.closingTime = new CityTime(18, 00);
		
		this.host = new RestaurantHostRoleSkyler();
		host.setRestaurant(this);
		
		this.cook = new RestaurantCookRoleSkyler();
		cook.setRestaurant(this);
		
		this.cashier = new RestaurantCashierRoleSkyler();
		cashier.setRestaurant(this);
		
		this.waiters = Collections.synchronizedList(new ArrayList<RestaurantWaiterRole>());
		for (int i = 0 ; i<numWaiters; i++) {
			RestaurantWaiterRoleSkyler newRole = new RestaurantWaiterRoleSkyler();
			WaiterGuiSkyler newGui = new WaiterGuiSkyler(newRole);
			newRole.setGui(newGui);
			
			this.panel.addGui(newGui);
			newRole.setRestaurant(this);
			newRole.setCook((RestaurantCookRoleSkyler) this.cook);
			newRole.setCashier((RestaurantCashierRoleSkyler) this.cashier);
			newRole.setHost((RestaurantHostRoleSkyler) this.host);
			newRole.setAnimPanel((RestaurantAnimationPanelSkyler) this.panel);
			
			
			waiters.add(newRole);
			
		}
	}
	
	@Override
	public Role getRole(Intention role) {
		
		switch(role) {
		case RestaurantCook: {
			if (cook.getPerson() == null) {
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
		case RestaurantCashier: {
			if (cashier.getPerson() == null) {
				return cashier;
			}
			return null;	
		}
		
		case RestaurantWaiter: {
			synchronized(waiters) {
				for (RestaurantWaiterRole r : waiters) {
				if (r.getPerson() == null) {
					((RestaurantHostRoleSkyler) host).addWaiter((RestaurantWaiterRoleSkyler) r);
					((RestaurantWaiterRoleSkyler) r).getGui().setPresent(true);
					return r;
				}
			}
			}//end synch
			return null;
		}
		
		case RestaurantCustomer: {
			RestaurantCustomerRoleSkyler newCust = new RestaurantCustomerRoleSkyler();
			CustomerGuiSkyler newGui = new CustomerGuiSkyler(newCust, (RestaurantAnimationPanelSkyler)this.panel);
			newCust.setGui(newGui);
			newCust.setCashier((RestaurantCashierRoleSkyler)cashier);
			newCust.setHost((RestaurantHostRoleSkyler)host);
			this.panel.addGui(newGui);
			newCust.setRestaurant(this);
			return newCust;
		}
		default: {
			Do("Error in intention passed to Restaurant.");
			return null;
		 }
		}
	}
	
	
	@Override
	public void closingTime() {

		cashier.msgClosingTime();
		cook.msgClosingTime();
		for (RestaurantWaiterRole r : waiters) {
			r.msgClosingTime();
		}
	}

	private void checkIfItsTimeToOpen(CityTime time) {
		// If it's not during shift hours, there's no way the restaurant would be open
		if (!(CityTime.timeDifference(time, morningShiftStart) >= 0 && CityTime.timeDifference(time, morningShiftEnd) < 0) &&
				!(CityTime.timeDifference(time, afternoonShiftStart) >= 0 && CityTime.timeDifference(time, closingTime) < 0)) {
			return;
		}
		
		if (host.getPerson() != null && cashier.getPerson() != null && cook.getPerson() != null) {
			for (RestaurantWaiterRole w : waiters) {
				if (w.getPerson() != null) {
					AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, this.toString(), "Open for business!");
					this.isOpen = true;
					return;
				}
			}
		}
	}
	

	@Override
	public void updateTime(CityTime time) {
		if (time.equalsIgnoreDay(morningShiftStart) || time.equalsIgnoreDay(afternoonShiftStart)) {
			this.forceClosed = false;
		}
		
		if (time.equalsIgnoreDay(morningShiftEnd)) {
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, this.toString(), "Morning shift over!");
			this.isOpen = false;
			if (host.getPerson() != null) {
				host.msgClosingTime();
			} else {
				closingTime();
			}
			this.configPanel.updateInfo(this);
		} else if (time.equalsIgnoreDay(this.closingTime)) {
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, this.toString(), "It's closing time!");
			this.isOpen = false;
			if (host.getPerson() != null) {
				host.msgClosingTime();
			} else {
				closingTime();
			}
			this.configPanel.updateInfo(this);
		} else if (!isOpen && !forceClosed) {
			checkIfItsTimeToOpen(time);
			this.configPanel.updateInfo(this);
		}
	}

	@Override
	public void emptyEntireCookInventory() {
		((RestaurantCookRoleSkyler)cook).clearInventory();
		this.configPanel.updateInfo(this);
	}

	@Override
	public List<String> getCookInventory() {
		return ((RestaurantCookRoleSkyler)cook).getInventory();
	}

}
