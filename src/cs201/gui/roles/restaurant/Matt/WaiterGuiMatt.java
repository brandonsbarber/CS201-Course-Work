package cs201.gui.roles.restaurant.Matt;

import java.awt.Color;
import java.awt.Graphics2D;

import cs201.gui.Gui;
import cs201.gui.structures.restaurant.RestaurantConfigPanelMatt;
import cs201.interfaces.roles.restaurant.Matt.CustomerMatt;
import cs201.roles.restaurantRoles.Matt.RestaurantWaiterRoleMatt;

public class WaiterGuiMatt implements Gui {

    private RestaurantWaiterRoleMatt role = null;
    
    private final int WAITERSIZE = 20;
	private final int CUSTOMERTABLEOFFSET = 20;
	private final int COOKLOCATIONX = 225, COOKLOCATIONY = 500;
	private final int INITIAL_IDLEX = 240, INITIAL_IDLEY = 0;
	private final int BREAKX = 245, BREAKY = 0;

	private int homeX = INITIAL_IDLEX, homeY = INITIAL_IDLEY;
    private int xPos = INITIAL_IDLEX, yPos = INITIAL_IDLEY;//default waiter position
    private int xDestination = INITIAL_IDLEX, yDestination = INITIAL_IDLEY;//default start position
    
    private boolean animating = false, goingToIdle = false;
    private enum BreakState { none, waitingForResponse, onBreak };
    private BreakState breakState = BreakState.none;
    private String message = "";
    
    private RestaurantConfigPanelMatt panel;
    private boolean isPresent;
    
    public WaiterGuiMatt(RestaurantWaiterRoleMatt role, RestaurantConfigPanelMatt r) {
        this.role = role;
        this.panel = r;
        this.isPresent = true;
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
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, WAITERSIZE, WAITERSIZE);
        
        g.setColor(Color.black);
		if (message.length() > 0) {
			g.drawString(message, xPos, yPos);
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
