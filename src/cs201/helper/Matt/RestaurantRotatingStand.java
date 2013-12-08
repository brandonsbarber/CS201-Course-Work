package cs201.helper.Matt;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cs201.gui.ArtManager;
import cs201.gui.Gui;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelMatt;
import cs201.helper.Constants;
import cs201.roles.restaurantRoles.Matt.RestaurantWaiterRoleMatt;

public class RestaurantRotatingStand implements Gui {
	private final int STANDSIZE = (RestaurantAnimationPanelMatt.WINDOWX < RestaurantAnimationPanelMatt.WINDOWY) ? (int)(RestaurantAnimationPanelMatt.WINDOWX * .045f) : (int)(RestaurantAnimationPanelMatt.WINDOWY * .045f);
	public static final int STANDX = (int)(RestaurantAnimationPanelMatt.WINDOWX * .70f);
	public static final int STANDY = (int)(RestaurantAnimationPanelMatt.WINDOWY * .9f);
	
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
		if (Constants.DEBUG_MODE) {
			g.setColor(Color.BLACK);
			g.drawString("Stand (" + orders.size() + ")", STANDX, STANDY);
			
			g.setColor(Color.PINK);
			g.fillOval(STANDX, STANDY, STANDSIZE, STANDSIZE);
		} else {
			if (orders.size() == 0) {
				g.drawImage(ArtManager.getImage("Stand_0"), STANDX, STANDY, STANDSIZE, STANDSIZE, null);
			} else if (orders.size() == 1) {
				g.drawImage(ArtManager.getImage("Stand_1"), STANDX, STANDY, STANDSIZE, STANDSIZE, null);
			} else if (orders.size() == 2) {
				g.drawImage(ArtManager.getImage("Stand_2"), STANDX, STANDY, STANDSIZE, STANDSIZE, null);
			} else if (orders.size() == 3) {
				g.drawImage(ArtManager.getImage("Stand_3"), STANDX, STANDY, STANDSIZE, STANDSIZE, null);
			} else if (orders.size() >= 4) {
				g.drawImage(ArtManager.getImage("Stand_4"), STANDX, STANDY, STANDSIZE, STANDSIZE, null);
			}
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
