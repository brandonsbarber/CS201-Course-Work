package cs201.gui.roles.restaurant.Ben;


import java.awt.Color;
import java.awt.Graphics2D;

import cs201.gui.ArtManager;
import cs201.gui.Gui;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelBen;
import cs201.helper.Constants;
import cs201.helper.Ben.RestaurantRotatingStandBen;
import cs201.interfaces.roles.restaurant.Ben.WaiterBen;

/*
 * Restaurant Waiter AGent
 */
public class WaiterGuiBen implements Gui {

	private WaiterBen agent = null;
    
    RestaurantAnimationPanelBen animationPanel = null;

    private int xPos = -20, yPos = -20;
    private int xDestination = -20, yDestination = -20;
    private Boolean hasDestination = false;

    public static final int xTable = 100;
    public static final int yTable = 200;
    public static final int tableWidth = 50;
    public static final int tablePad = 20;
    private static final int BREAKAREAY = 400;
	private static final int BREAKAREAX = 5;
    
	private boolean hasIcon = false;
	private String iconText = "";
	
	private boolean isPresent = false;
	private boolean animating = false;
	private String moveDir = "Restaurant_Ben_Waiter_Down";
	
	// The default home position for the waiter
	private int homeX = 5;
	private int homeY = 5;
	
	public void setGui(RestaurantAnimationPanelBen g) {
		animationPanel = g;
	}
	
	private void setWaiterNumber(int num) {
		homeX = 5;
		homeY = 5 + (num * 25);
	}

    public WaiterGuiBen(WaiterBen agent) {
        this.agent = agent;
        isPresent = false;
    }

    public void updatePosition() {
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;

        if (xPos == xDestination && yPos == yDestination) {
        	if (hasDestination) {
        		agent.msgAtDestination();
        		hasDestination = false;
        	}
        	if (animating) {
        		animating = false;
        	}
        }
        
    }

    public void draw(Graphics2D g) {
    	if (Constants.DEBUG_MODE) {
	    	// Draw the magenta waiter rectangle
	        g.setColor(Color.MAGENTA);
	        g.fillRect(xPos, yPos, 20, 20);
	        
			// Draw his icon, if he has one
			if (hasIcon) {
				g.setColor(Color.BLACK);
				g.drawString(iconText, xPos, yPos + 15);
			}
    	} else {
    		if (animating) {
				double theta = Math.atan2(yPos - yDestination, xDestination - xPos);
				
				if (theta >= -Math.PI/4 && theta <= Math.PI/4) {
					moveDir = "Restaurant_Ben_Waiter_Right";
				} else if (theta >= Math.PI/4 && theta <= 3*Math.PI/4) {
					moveDir = "Restaurant_Ben_Waiter_Up";
				} else if (theta <= -Math.PI/4 && theta >= -3*Math.PI/4) {
					moveDir = "Restaurant_Ben_Waiter_Down";
				} else {
					moveDir = "Restaurant_Ben_Waiter_Left";
				}
			} else {
				moveDir = "Restaurant_Ben_Waiter_Down";
			}
			
    		g.drawImage(ArtManager.getImage(moveDir), xPos, yPos, null);
    		
    		// Draw his icon, if he has one
			if (hasIcon) {
				g.setColor(Color.BLACK);
				g.drawString(iconText, xPos + 15, yPos - 15);
			}
    	}
    }

    public boolean isPresent() {
        return isPresent;
    }
    
	public void setIconText(String text) {
		hasIcon = true;
		iconText = text;
	}
	
	public void removeIcon() {
		hasIcon = false;
	}

    public void DoWalkToTable(int tablenumber) {
    	animating = true;
        xDestination = xTable + (tableWidth + tablePad) * (tablenumber - 1) + (tableWidth / 2 + 10);
        yDestination = yTable - 20;
        hasDestination = true;
    }
    
    public void DoEnterRestaurant() {
    	animating = true;
    	xPos = -40;
    	yPos = -40;
    	DoWalkToHome();
    }
    
    /**
     * Forces the GUI to go to his home position. Should be followed by an pauseForAnimation()
     * in the agent (or role) code, as this will also release the semaphore when he's finished.
     */
    public void DoWalkToHome() {
    	animating = true;
    	homeX = 5;
		homeY = 5 + (agent.getWaiterNumber() * 25);
    	
        xDestination = homeX;
        yDestination = homeY;
        hasDestination = true;
    }
    
    /**
     * The GUI will move to his home position if he is bored, but this method WILL NOT release
     * the semaphore when he gets there. Don't follow this method with pauseForAnimation()
     */
    public void DoWalkToHomeIfBored() {
    	animating = true;
    	homeX = 5;
    	homeY = 5 + (agent.getWaiterNumber() * 25);
    	
    	xDestination = homeX;
    	yDestination = homeY;
    }
    
    public void DoWalkToPlatingArea() {
    	animating = true;
    	xDestination = RestaurantAnimationPanelBen.PLATINGAREAX;
    	yDestination = RestaurantAnimationPanelBen.PLATINGAREAY + 30;
    	hasDestination = true;
    }
    
    public void DoWalkToCookingArea() {
    	animating = true;
    	xDestination = RestaurantAnimationPanelBen.COOKINGAREAX;
    	yDestination = RestaurantAnimationPanelBen.COOKINGAREAY + 30;
    	hasDestination = true;
    }
    
    public void DoWalkToStand() {
    	animating = true;
    	xDestination = RestaurantRotatingStandBen.ROTATING_STAND_X;
    	yDestination = RestaurantRotatingStandBen.ROTATING_STAND_Y + 30;
    	hasDestination = true;
    }
    
    public void DoWalkToBreakArea() {
    	animating = true;
    	xDestination = BREAKAREAX;
    	yDestination = BREAKAREAY;
    	hasDestination = true;
    }
    
    public void DoLeaveRestaurant() {
    	animating = true;
    	xDestination = xPos;
    	yDestination = -40;
    	hasDestination = true;
    }
    
    public void WaiterOnBreak() {
    	//gui.setWaiterOnBreak(agent);
    }
    
    public void WaiterWorking() {
    	//gui.setWaiterWorking(agent);
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

	public void setPresent(boolean present) {
		isPresent = present;
	}
}
