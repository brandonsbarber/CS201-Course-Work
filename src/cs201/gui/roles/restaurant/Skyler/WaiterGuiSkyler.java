package cs201.gui.roles.restaurant.Skyler;

import java.awt.*;

import cs201.gui.ArtManager;
import cs201.gui.Gui;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelSkyler;
import cs201.interfaces.roles.restaurant.Skyler.CustomerSkyler;
import cs201.interfaces.roles.restaurant.Skyler.WaiterSkyler;

public class WaiterGuiSkyler implements Gui {

    private WaiterSkyler agent = null;


    private int currentTableNum;  
    
    private int cookX = 420; //was 430
    private int cookY = 82; //was 65
    
    private int cookX2 = 360; //was 430
    private int cookY2 = 62; //was 65
    
    private int waitingX = 90;
    private int waitingY = 70;
    
    private int homeX = 3;
    private int homeY = 100; //Will be randomized with constructor.

    private int xPos = homeX, yPos = homeY;//default waiter position
    private int xDestination = xPos, yDestination = yPos;//default start position   
    
    private String dir = new String();
    private String destinationDir = new String();
    private String displayString = "";
    private boolean goingOnBreak = false;
    private boolean atHomePos = true;
    private boolean isPresent = false;
    private boolean arrived = false;

    public static final int xTable = 150;
    public static final int yTable = 250;
    private Font font = new Font("Sans-serif", Font.PLAIN, 10);
    
    RestaurantAnimationPanelSkyler animationPanel;
    
    public WaiterGuiSkyler(WaiterSkyler agent) {
        this.agent = agent;
        homeY += (int)(Math.random()*80);
        yPos = homeY;
        yDestination = yPos;
        dir = "Skyler_Waiter_Down";
        destinationDir = dir;
    }

    public void updatePosition() {
    	
    	if (xPos == xDestination && yPos == yDestination) {
    		dir = destinationDir;
    		if (!arrived) {
    			arrived = true;
    			if (xPos == homeX && yPos == homeY) {
    				if (!atHomePos) {
    					agent.msgAtFront();
    					atHomePos = true;
    				}
    			}
    			else atHomePos = false;
    				
    			if (xPos == waitingX && yPos == waitingY) {
    				agent.msgAtWaitingArea();
    			}
    			if (xPos == cookX && yPos == cookY || xPos == cookX2 && yPos == cookY2) {
    				agent.msgAtCook();
    			}
    			if (xPos == 200 && yPos == -19) {
    				agent.msgAtCashier();
    			}
    			if (xPos == xTable + 20 + (100*(currentTableNum-1)) && yPos == yTable - 20) {
    				agent.msgAtTable(currentTableNum);
    			}
    		}
    	}
    	else {
    		atHomePos = false; //if position and destination don't match, must not be at home position.
    		arrived = false;
    	}
    	
        if (xPos < xDestination){
            xPos++;
            dir = "Skyler_Waiter_Right";
        }
        else if (xPos > xDestination) {
            xPos--;
            dir = "Skyler_Waiter_Left";
        }

        if (yPos < yDestination) {
            yPos++;
            dir = "Skyler_Waiter_Down";
        }
        else if (yPos > yDestination) {
            yPos--;
            dir = "Skyler_Waiter_Up";
        }
        
    }

    public void draw(Graphics2D g) {
        /*g.setColor(Color.BLACK);
        g.fillRect(xPos, yPos, 20, 20);
        g.setColor(Color.BLUE);
        g.fillRect(xPos+1, yPos+1, 18, 18);*/
        
        g.drawImage(ArtManager.getImage(dir), xPos, yPos, 16, 24, null);
        
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(displayString, xPos+3, yPos+14);
    }

    public boolean isPresent() {
        return isPresent;
    }
    
    public boolean isGoingOnBreak() {
    	return goingOnBreak;
    }
    
    public void breakButtonPressed(RestaurantAnimationPanelSkyler gui) {
    	animationPanel = gui;
    	goingOnBreak = true;
    	agent.msgWantBreak();
    }
    
    public void offBreakButtonPressed(RestaurantAnimationPanelSkyler gui) {
    	animationPanel = gui;
    	goingOnBreak = false;
    	agent.msgEndBreak();
    }
    
    public void breakDenied() {
    	goingOnBreak = false;
    	//animationPanel.updateInfoPanel(agent);
    }

    public void DoBringToTable(CustomerSkyler customer, int tableNumber) {
    	arrived = false;
    	customer.getGui().setTable(tableNumber); //CustomerGui knows which table to go to without the customer agent knowing the tablenumber.
    	currentTableNum = tableNumber;
        xDestination = xTable + 20 + (100*(currentTableNum-1));
        yDestination = yTable - 20;
        destinationDir = "Skyler_Waiter_Down";
    }
    
    public void DoGoToTable(int tableNumber) {
    	arrived = false;
    	displayString = "";
    	currentTableNum = tableNumber;
    	xDestination = xTable + 20 + (100*(currentTableNum-1));
        yDestination = yTable - 20;
        destinationDir = "Skyler_Waiter_Down";
    }

    public void DoLeaveCustomer() {
    	arrived = false;
    	displayString = "";
        //xDestination = -19;
        //yDestination = -19;
        xDestination = homeX;
        yDestination = homeY;
        destinationDir = "Skyler_Waiter_Left";
    }
    
    public void DoGoToWaitingArea() {
    	arrived = false;
    	displayString = "";
    	xDestination = waitingX;
    	yDestination = waitingY;
    	destinationDir = "Skyler_Waiter_Left";
    }
    
    public void DoBringOrderToCook(String choice) { //NEED TO ADD IN CHOICE GRAPHIC
    	arrived = false;
    	displayString = choice.substring(0, 2) + "?";
    	xDestination = cookX;
    	yDestination = cookY;
    	destinationDir = "Skyler_Waiter_Up";
    }
    
    public void DoGoToCashier() {
    	arrived = false;
    	displayString = "";
    	xDestination = 200;
    	yDestination = -19;
    	destinationDir = "Skyler_Waiter_Up";
    }
    
    public void DoGetFood() {
    	arrived = false;
    	displayString = "";
    	xDestination = cookX2;
    	yDestination = cookY2;
    	destinationDir = "Skyler_Waiter_Right";
    }
    
    public void DoDeliverFood (String choice, int tableNumber) {
    	arrived = false;
    	displayString = choice.substring(0, 2);
    	currentTableNum = tableNumber;
    	xDestination = xTable + 20 + (100*(currentTableNum-1));
        yDestination = yTable - 20;
        destinationDir = "Skyler_Waiter_Down";
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    public boolean inMotion() {
    	return !arrived;
    }
    
    public boolean atFront() {
    	if (xPos==-20)
    		{if (yPos==-20)
    			return true;
    		else return false;}
    	
    		else return false;
    }

	@Override
	public void setPresent(boolean present) {
		isPresent = present;
	}
    
}
