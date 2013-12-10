package cs201.gui.roles.restaurant.Matt;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cs201.gui.ArtManager;
import cs201.gui.Gui;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelMatt;
import cs201.helper.Constants;
import cs201.roles.restaurantRoles.Matt.RestaurantCookRoleMatt;

public class CookGuiMatt implements Gui {

	private RestaurantCookRoleMatt role;
	private boolean isPresent;
	
	private List<String> cooking;
	private List<String> plating;
	
	private final int COOKSIZE = (RestaurantAnimationPanelMatt.WINDOWX < RestaurantAnimationPanelMatt.WINDOWY) ? (int)(RestaurantAnimationPanelMatt.WINDOWX * .04f) : (int)(RestaurantAnimationPanelMatt.WINDOWY * .04f);
	private final int COOKX = (int)(RestaurantAnimationPanelMatt.WINDOWX * .47f);
	private final int COOKY = (int)(RestaurantAnimationPanelMatt.WINDOWY * .95f);
	private final int cookingAreaX = (int)(RestaurantAnimationPanelMatt.WINDOWX * .684f);
	private final int cookingAreaY = (int)(RestaurantAnimationPanelMatt.WINDOWY * .85f);
	private final int platingAreaX = (int)(RestaurantAnimationPanelMatt.WINDOWX * .2f);
	private final int platingAreaY = (int)(RestaurantAnimationPanelMatt.WINDOWY * .85f);
	
	private int xPos, yPos;
	private int xDestination, yDestination;
	private boolean animating = false;
	private boolean goingToIdle = false;
	
	private String moveDir = "Cook_Down";

	public CookGuiMatt(RestaurantCookRoleMatt c) {
		cooking = Collections.synchronizedList(new ArrayList<String>());
		plating = Collections.synchronizedList(new ArrayList<String>());
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
				if (!goingToIdle) {
					role.DoneAnimating();
				}
			}
		} else {
			return;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		
		int x = cookingAreaX;
		int y = cookingAreaY;
		synchronized(cooking) {
			for (String food : cooking) {
				g.drawString(food, x, y);
				y += 13;
			}
		}
		
		x = platingAreaX;
		y = platingAreaY;
		synchronized(plating) {
			for (String food : plating) {
				g.drawString(food, x, y);
				y += 13;
			}
		}
		
		if (Constants.DEBUG_MODE) {
			g.setColor(Color.black);
			g.drawString("Cook", COOKX, COOKY);
			
			g.setColor(Color.WHITE);
			g.fillRect(xPos, yPos, COOKSIZE, COOKSIZE);
		} else {			
			if (animating) {
				double theta = Math.atan2(yPos - yDestination, xDestination - xPos);
				
				if (theta >= -Math.PI/4 && theta <= Math.PI/4) {
					moveDir = "Cook_Right";
				} else if (theta >= Math.PI/4 && theta <= 3*Math.PI/4) {
					moveDir = "Cook_Up";
				} else if (theta <= -Math.PI/4 && theta >= -3*Math.PI/4) {
					moveDir = "Cook_Down";
				} else {
					moveDir = "Cook_Left";
				}
			} else {
				moveDir = "Cook_Down";
			}
			
			g.drawImage(ArtManager.getImage(moveDir), xPos, yPos, COOKSIZE, COOKSIZE, null);
		}
	}
	
	public void addCookingItem(String food) {
		cooking.add(food);
	}
	
	public void removeCookingItem(String food) {
		cooking.remove(food);
	}
	
	public void addPlatingItem(String food) {
		plating.add(food);
	}
	
	public void removePlatingItem(String food) {
		plating.remove(food);
	}
	
	public void goToLocation(int x, int y) {
		xDestination = x;
		yDestination = y;
		animating = true;
		goingToIdle = false;
	}
	
	public void goToKitchen() {
		if (xPos == COOKX && yPos == COOKY) {
			return;
		}
		xDestination = COOKX;
		yDestination = COOKY;
		animating = true;
		goingToIdle = true;
	}

	public boolean isPresent() {
		return isPresent;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}
}
