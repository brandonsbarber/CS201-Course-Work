package cs201.helper.Matt;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cs201.gui.Gui;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelMatt;
import cs201.helper.Constants;
import cs201.roles.restaurantRoles.Matt.RestaurantWaiterRoleMatt;

public class RestaurantRotatingStand implements Gui {
	private final int STANDSIZE = (RestaurantAnimationPanelMatt.WINDOWX < RestaurantAnimationPanelMatt.WINDOWY) ? (int)(RestaurantAnimationPanelMatt.WINDOWX * .04f) : (int)(RestaurantAnimationPanelMatt.WINDOWY * .04f);
	public static final int STANDX = (int)(RestaurantAnimationPanelMatt.WINDOWX * .47f);
	public static final int STANDY = (int)(RestaurantAnimationPanelMatt.WINDOWY * .8f);
	
	private List<RotatingStandOrder> orders = Collections.synchronizedList(new ArrayList<RotatingStandOrder>());
	private boolean isPresent = false;
	
	public RestaurantRotatingStand() {
		
	}
	
	public void addOrder(RestaurantWaiterRoleMatt waiter, String choice, int tableNum) {
		orders.add(new RotatingStandOrder(waiter, choice, tableNum));
	}
	
	public RotatingStandOrder removeOrder() {
		if (orders.size() > 0) {
			return orders.remove(0);
		}
		return null;
	}
	
	public class RotatingStandOrder {
		public RestaurantWaiterRoleMatt waiter;
		public String choice;
		public int tableNum;
		
		public RotatingStandOrder() {
			this.waiter = null;
			this.choice = "";
			this.tableNum = -1;
		}
		
		public RotatingStandOrder(RestaurantWaiterRoleMatt waiter, String choice, int tableNum) {
			this.waiter = waiter;
			this.choice = choice;
			this.tableNum = tableNum;
		}
	}

	@Override
	public void updatePosition() {
		return;
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.PINK);
		g.fillOval(STANDX, STANDY, STANDSIZE, STANDSIZE);
		
		if (Constants.DEBUG_MODE) {
			g.setColor(Color.BLACK);
			g.drawString("Stand", STANDX, STANDY);
		}
	}

	@Override
	public boolean isPresent() {
		return isPresent;
	}

	@Override
	public void setPresent(boolean present) {
		isPresent = present;
	}
	
}
