package cs201.gui.roles.restaurant.Matt;

import java.awt.Color;
import java.awt.Graphics2D;

import cs201.gui.Gui;
import cs201.roles.restaurantRoles.Matt.RestaurantCashierRoleMatt;

public class CashierGuiMatt implements Gui {

	private RestaurantCashierRoleMatt role = null;
	private boolean isPresent = false;
	
	public static final int CASHIERSIZE = 20;
	public static final int CASHIERX = 25;
	public static final int CASHIERY = 240;

	private int xPos, yPos;

	public CashierGuiMatt(RestaurantCashierRoleMatt c) {
		role = c;
		xPos = CASHIERX;
		yPos = CASHIERY;
		isPresent = true;
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
