package cs201.gui.roles.restaurant.Matt;

import java.awt.Color;
import java.awt.Graphics2D;

import cs201.gui.ArtManager;
import cs201.gui.Gui;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelMatt;
import cs201.helper.Constants;
import cs201.roles.restaurantRoles.Matt.RestaurantCashierRoleMatt;

public class CashierGuiMatt implements Gui {

	private RestaurantCashierRoleMatt role = null;
	private boolean isPresent = false;
	
	public static final int CASHIERSIZE = (RestaurantAnimationPanelMatt.WINDOWX < RestaurantAnimationPanelMatt.WINDOWY) ? (int)(RestaurantAnimationPanelMatt.WINDOWX * .04f) : (int)(RestaurantAnimationPanelMatt.WINDOWY * .04f);
	public static final int CASHIERX = (int)(RestaurantAnimationPanelMatt.WINDOWX * .05f);
	public static final int CASHIERY = (int)(RestaurantAnimationPanelMatt.WINDOWX * .48f);

	private int xPos, yPos;
	private int xDestination, yDestination;
	private boolean animating = false;
	
	private String moveDir = "Cashier_Down";

	public CashierGuiMatt(RestaurantCashierRoleMatt c) {
		role = c;
		xPos = RestaurantAnimationPanelMatt.RESTAURANT_ENTRANCE_X;
		xDestination = xPos;
		yPos = RestaurantAnimationPanelMatt.RESTAURANT_ENTRANCE_Y;
		yDestination = yPos;
		isPresent = false;
	}

	public void updatePosition() {	
    	if (animating) {
			if (xPos < xDestination)
				xPos++;
			else if (xPos > xDestination)
				xPos--;
	
			if (yPos < yDestination)
				yPos++;
			else if (yPos > yDestination)
				yPos--;
			
			if (xPos == xDestination && yPos == yDestination) {
				animating = false;
				role.DoneAnimating();
			}
		} else {
			return;
		}
	}

	public void draw(Graphics2D g) {
		if (Constants.DEBUG_MODE) {
			g.setColor(Color.black);
			g.drawString("Cashier", xPos, yPos);
			
			g.setColor(Color.BLUE);
			g.fillRect(xPos, yPos, CASHIERSIZE, CASHIERSIZE);
		} else {			
			if (animating) {
				double theta = Math.atan2(yPos - yDestination, xDestination - xPos);
				
				if (theta >= -Math.PI/4 && theta <= Math.PI/4) {
					moveDir = "Cashier_Right";
				} else if (theta >= Math.PI/4 && theta <= 3*Math.PI/4) {
					moveDir = "Cashier_Up";
				} else if (theta <= -Math.PI/4 && theta >= -3*Math.PI/4) {
					moveDir = "Cashier_Down";
				} else {
					moveDir = "Cashier_Left";
				}
			} else {
				moveDir = "Cashier_Down";
			}
			
			g.drawImage(ArtManager.getImage(moveDir), xPos, yPos, CASHIERSIZE, CASHIERSIZE, null);
		}
	}
	
	public void goToLocation(int x, int y) {
		xDestination = x;
		yDestination = y;
		animating = true;
	}
	
	public void goToRegister() {
		xDestination = CASHIERX;
		yDestination = CASHIERY;
		animating = true;
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
