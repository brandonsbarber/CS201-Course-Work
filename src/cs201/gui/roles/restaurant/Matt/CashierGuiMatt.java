package cs201.gui.roles.restaurant.Matt;

import java.awt.Color;
import java.awt.Graphics2D;

import cs201.gui.Gui;
import cs201.roles.restaurantRoles.Matt.RestaurantCashierRoleMatt;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelMatt;

public class CashierGuiMatt implements Gui {

	private RestaurantCashierRoleMatt role = null;
	private boolean isPresent = false;
	
	public static final int CASHIERSIZE = (RestaurantAnimationPanelMatt.WINDOWX < RestaurantAnimationPanelMatt.WINDOWY) ? (int)(RestaurantAnimationPanelMatt.WINDOWX * .04f) : (int)(RestaurantAnimationPanelMatt.WINDOWY * .04f);
	public static final int CASHIERX = (int)(RestaurantAnimationPanelMatt.WINDOWX * .05f);
	public static final int CASHIERY = (int)(RestaurantAnimationPanelMatt.WINDOWX * .48f);

	private int xPos, yPos;

	public CashierGuiMatt(RestaurantCashierRoleMatt c) {
		role = c;
		xPos = CASHIERX;
		yPos = CASHIERY;
		isPresent = false;
	}

	public void updatePosition() {	
		return;
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.fillRect(xPos, yPos, CASHIERSIZE, CASHIERSIZE);
		
		g.setColor(Color.black);
		g.drawString("Cashier", xPos, yPos);
	}

	public boolean isPresent() {
		return isPresent;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public int X() {
		return xPos;
	}
	
	public int Y() {
		return yPos;
	}
}
