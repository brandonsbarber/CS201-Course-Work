package cs201.gui.roles.restaurant.Matt;

import java.awt.Color;
import java.awt.Graphics2D;

import cs201.gui.ArtManager;
import cs201.gui.Gui;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelMatt;
import cs201.gui.structures.restaurant.RestaurantConfigPanelMatt;
import cs201.helper.Constants;
import cs201.interfaces.roles.restaurant.Matt.CustomerMatt;
import cs201.roles.restaurantRoles.Matt.RestaurantWaiterRoleMatt;

public class WaiterGuiMatt implements Gui {

    private RestaurantWaiterRoleMatt role = null;
    
    private final int WAITERSIZE = (RestaurantAnimationPanelMatt.WINDOWX < RestaurantAnimationPanelMatt.WINDOWY) ? (int)(RestaurantAnimationPanelMatt.WINDOWX * .04f) : (int)(RestaurantAnimationPanelMatt.WINDOWY * .04f);
	private final int CUSTOMERTABLEOFFSET = (RestaurantAnimationPanelMatt.WINDOWX < RestaurantAnimationPanelMatt.WINDOWY) ? (int)(RestaurantAnimationPanelMatt.WINDOWX * .04f) : (int)(RestaurantAnimationPanelMatt.WINDOWY * .04f);
	private final int COOKLOCATIONX = (int)(RestaurantAnimationPanelMatt.WINDOWX * .45f);
	private final int COOKLOCATIONY = (int)(RestaurantAnimationPanelMatt.WINDOWY * 1.0f);
	private final int INITIAL_IDLEX = (int)(RestaurantAnimationPanelMatt.WINDOWX * .48f);
	private final int INITIAL_IDLEY = 0;
	private final int BREAKX = (int)(RestaurantAnimationPanelMatt.WINDOWX * .49f);
	private final int BREAKY = 0;

	private int homeX = INITIAL_IDLEX, homeY = INITIAL_IDLEY;
    private int xPos = RestaurantAnimationPanelMatt.RESTAURANT_ENTRANCE_X, yPos = RestaurantAnimationPanelMatt.RESTAURANT_ENTRANCE_Y; //default waiter position
    private int xDestination = INITIAL_IDLEX, yDestination = INITIAL_IDLEY; //default start position
    
    private boolean animating = false, goingToIdle = false;
    private enum BreakState { none, waitingForResponse, onBreak };
    private BreakState breakState = BreakState.none;
    private String message = "";
    
    private RestaurantConfigPanelMatt panel;
    private boolean isPresent;
    
    private String moveDir = "Waiter_Down";
    
    public WaiterGuiMatt(RestaurantWaiterRoleMatt role, RestaurantConfigPanelMatt r) {
        this.role = role;
        this.panel = r;
        this.isPresent = false;
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
				if (!goingToIdle && breakState != BreakState.onBreak) 
					role.DoneAnimating();
				goingToIdle = false;
			}
		} else {
			return;
		}
    }

    public void draw(Graphics2D g) {        
		if (Constants.DEBUG_MODE) {
			g.setColor(Color.BLACK);
			g.drawString("Waiter " + (message != "" ? "(" + message + ")" : ""), xPos, yPos);
			
	        g.setColor(Color.MAGENTA);
	        g.fillRect(xPos, yPos, WAITERSIZE, WAITERSIZE);
		} else {
			g.setColor(Color.BLACK);
			g.drawString(message, xPos, yPos);
			
			if (animating) {
				double theta = Math.atan2(yPos - yDestination, xDestination - xPos);
				
				if (theta >= -Math.PI/4 && theta <= Math.PI/4) {
					moveDir = "Waiter_Right";
				} else if (theta >= Math.PI/4 && theta <= 3*Math.PI/4) {
					moveDir = "Waiter_Up";
				} else if (theta <= -Math.PI/4 && theta >= -3*Math.PI/4) {
					moveDir = "Waiter_Down";
				} else {
					moveDir = "Waiter_Left";
				}
			} else {
				moveDir = "Waiter_Down";
			}
			
			g.drawImage(ArtManager.getImage(moveDir), xPos, yPos, WAITERSIZE, WAITERSIZE, null);
		}
    }
    
    public void setMessage(String what) {
		String temp = what;
		
		switch (temp) {
		case "Steak": {
			temp = "Stk";
			break;
		}
		case "Pasta": {
			temp = "Pas";
			break;
		}
		case "Pizza": {
			temp = "Piz";
			break;
		}
		case "Ice Cream": {
			temp = "Ice";
			break;
		}
		case "Chicken": {
			temp = "Ckn";
			break;
		}
		case "Salad": {
			temp = "Sal";
			break;
		}
		default: break;
		}
		
		message = temp;
	}

    public boolean isPresent() {
        return isPresent;
    }

	public void setPresent(boolean p) {
		isPresent = p;
	}
    
    public boolean isOnBreak() {
    	return (breakState == BreakState.onBreak || breakState == BreakState.waitingForResponse);
    }
    
    public void setWaitingForBreakResponse() {
    	breakState = BreakState.waitingForResponse;
    }
    
    public void setOffBreak() {
    	breakState = BreakState.none;
    	panel.setPersonEnabled(role);
    }

    public void GoToCustomer(CustomerMatt c) {
    	xDestination = c.getGui().X() + CUSTOMERTABLEOFFSET;
        yDestination = c.getGui().Y() - CUSTOMERTABLEOFFSET;
        animating = true;
        goingToIdle = false;
    }
    
    public void GoToCustomerWaiting(CustomerMatt c) {
    	xDestination = c.getGui().WaitingX() + CUSTOMERTABLEOFFSET;
        yDestination = c.getGui().WaitingY() - CUSTOMERTABLEOFFSET;
        animating = true;
        goingToIdle = false;
    }
    
    public void GoToLocation(int x, int y) {
    	xDestination = x + CUSTOMERTABLEOFFSET;
        yDestination = y - CUSTOMERTABLEOFFSET;
        animating = true;
        goingToIdle = false;
    }
    
    public void GoToCook() {
    	xDestination = COOKLOCATIONX;
        yDestination = COOKLOCATIONY;
        animating = true;
        goingToIdle = false;
    }
    
    public void GoToWaitingPosition() {
    	if (!animating || goingToIdle) {
    		xDestination = homeX;
    		yDestination = homeY;
    		animating = true;
    		goingToIdle = true;
    	}
    }
    
    public void GoToBreakPosition() {
    	xDestination = BREAKX;
    	yDestination = BREAKY;
    	animating = true;
    	breakState = BreakState.onBreak;
    }
    
    public void SetHomePosition(int x, int y) {
    	homeX = x;
    	homeY = y;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
