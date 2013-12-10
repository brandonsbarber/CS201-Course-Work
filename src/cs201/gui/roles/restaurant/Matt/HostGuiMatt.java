package cs201.gui.roles.restaurant.Matt;

import java.awt.Color;
import java.awt.Graphics2D;

import cs201.gui.ArtManager;
import cs201.gui.Gui;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelMatt;
import cs201.helper.Constants;
import cs201.roles.restaurantRoles.Matt.RestaurantHostRoleMatt;

public class HostGuiMatt implements Gui {
	private final int HOSTSIZE = (RestaurantAnimationPanelMatt.WINDOWX < RestaurantAnimationPanelMatt.WINDOWY) ? (int)(RestaurantAnimationPanelMatt.WINDOWX * .04f) : (int)(RestaurantAnimationPanelMatt.WINDOWY * .04f);
	private final int HOSTX = (int)(RestaurantAnimationPanelMatt.WINDOWX * .1f);
	private final int HOSTY = (int)(RestaurantAnimationPanelMatt.WINDOWY * .03f);
	
	private boolean isPresent;
	private RestaurantHostRoleMatt role;
	
	private int xPos, yPos;
	private int xDestination, yDestination;
	private boolean animating = false;
	
	private String moveDir = "Host_Down";
	
	public HostGuiMatt(RestaurantHostRoleMatt r) {
		role = r;
		xPos = RestaurantAnimationPanelMatt.RESTAURANT_ENTRANCE_X;
		xDestination = xPos;
		yPos = RestaurantAnimationPanelMatt.RESTAURANT_ENTRANCE_Y;
		yDestination = yPos;
		isPresent = false;
	}
	
	@Override
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

	@Override
	public void draw(Graphics2D g) {
		if (Constants.DEBUG_MODE) {
			g.setColor(Color.black);
			g.drawString("Host", xPos, yPos);
			
			g.setColor(Color.BLACK);
			g.fillRect(xPos, yPos, HOSTSIZE, HOSTSIZE);
		} else {			
			if (animating) {
				double theta = Math.atan2(yPos - yDestination, xDestination - xPos);
				
				if (theta >= -Math.PI/4 && theta <= Math.PI/4) {
					moveDir = "Host_Right";
				} else if (theta >= Math.PI/4 && theta <= 3*Math.PI/4) {
					moveDir = "Host_Up";
				} else if (theta <= -Math.PI/4 && theta >= -3*Math.PI/4) {
					moveDir = "Host_Down";
				} else {
					moveDir = "Host_Left";
				}
			} else {
				moveDir = "Host_Down";
			}
			
			g.drawImage(ArtManager.getImage(moveDir), xPos, yPos, HOSTSIZE, HOSTSIZE, null);
		}
	}
	
	public void goToLocation(int x, int y) {
		xDestination = x;
		yDestination = y;
		animating = true;
	}
	
	public void goToDesk() {
		xDestination = HOSTX;
		yDestination = HOSTY;
		animating = true;
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
