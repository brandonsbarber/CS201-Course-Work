package cs201.gui.roles.restaurant.Skyler;

import java.awt.*;

import cs201.gui.Gui;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelSkyler;
import cs201.interfaces.roles.restaurant.Skyler.CustomerSkyler;
import cs201.interfaces.roles.restaurant.Skyler.WaiterSkyler;

public class WaiterGuiSkyler implements Gui {

    private WaiterSkyler agent = null;


    private int currentTableNum;  
    
    private int cookX = 430;
    private int cookY = 65;
    
    private int waitingX = 90;
    private int waitingY = 70;
    
    private int homeX = 3;
    private int homeY = 100; //Will be randomized with constructor.

    private int xPos = homeX, yPos = homeY;//default waiter position
    private int xDestination = xPos, yDestination = yPos;//default start position   
    
    
    private String displayString = "";
    private boolean goingOnBreak = false;
    private boolean atHomePos = true;
    private boolean isPresent = false;

    public static final int xTable = 150;
    public static final int yTable = 250;
    private Font font = new Font("Sans-serif", Font.PLAIN, 10);
    
    RestaurantAnimationPanelSkyler animationPanel;
    
    public WaiterGuiSkyler(WaiterSkyler agent) {
        this.agent = agent;
        homeY += (int)(Math.random()*80);
        yPos = homeY;
        yDestination = yPos;
        
    }

    public void updatePosition() {
    	/*if (xPos == -19 && yPos == -19 && xDestination == -19 && yDestination == -19) //&& !agent.getWaitingCustomers().isEmpty())
    		{
    		agent.msgAtFront();
    		xDestination = -20;//prevents msgAtFront() from being called repeatedly.
    		yDestination = -20;
    		}
    	if (xPos == cookX && yPos == cookY && xDestination == cookX && yDestination == cookY)
    		{
    			agent.msgAtCook();
    		}
    	if (xPos == 200 && yPos == -19 && xDestination == 200 && yDestination == -19)
    		agent.msgAtCashier();*/ //CONSOLIDATED INTO IF BLOCK BELOW
    	
    	if (xPos == xDestination && yPos == yDestination) {
    		if (xPos == homeX && yPos == homeY) {
    			if (!atHomePos) {
    				agent.msgAtFront();
    				atHomePos = true;
    			}
    		}
    		else atHomePos = false;
    		/*if (xPos == -19 && yPos == -19) {
    			agent.msgAtFront();
        		xDestination = -20;//prevents msgAtFront() from being called repeatedly.
        		yDestination = -20;
    		}*/
    		
    		if (xPos == waitingX && yPos == waitingY) {
    			agent.msgAtWaitingArea();
    		}
    		if (xPos == cookX && yPos == cookY) {
    			agent.msgAtCook();
    		}
    		if (xPos == 200 && yPos == -19) {
    			agent.msgAtCashier();
    		}
    		if (xPos == xTable + 20 + (100*(currentTableNum-1)) && yPos == yTable - 20) {
    			agent.msgAtTable(currentTableNum);
    		}
    	}
    	else 
    		atHomePos = false; //if position and destination don't match, must not be at home position.
    	
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;
        /*
        if (xPos == xDestination && yPos == yDestination
        		&& (xDestination == xTable + 20 + (100*(currentTableNum-1))) && (yDestination == yTable - 20)) {
           agent.msgAtTable(currentTableNum);
        }*/  //CONSOLIDATED INTO IF BLOCK
        
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(xPos, yPos, 20, 20);
        g.setColor(Color.BLUE);
        g.fillRect(xPos+1, yPos+1, 18, 18);
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
    	customer.getGui().setTable(tableNumber); //CustomerGui knows which table to go to without the customer agent knowing the tablenumber.
    	currentTableNum = tableNumber;
        xDestination = xTable + 20 + (100*(currentTableNum-1));
        yDestination = yTable - 20;
    }
    
    public void DoGoToTable(int tableNumber) {
    	displayString = "";
    	currentTableNum = tableNumber;
    	xDestination = xTable + 20 + (100*(currentTableNum-1));
        yDestination = yTable - 20;
    }

    public void DoLeaveCustomer() {
    	displayString = "";
        //xDestination = -19;
        //yDestination = -19;
        xDestination = homeX;
        yDestination = homeY;
    }
    
    public void DoGoToWaitingArea() {
    	displayString = "";
    	xDestination = waitingX;
    	yDestination = waitingY;
    }
    
    public void DoBringOrderToCook(String choice) { //NEED TO ADD IN CHOICE GRAPHIC
    	displayString = choice.substring(0, 2) + "?";
    	xDestination = cookX;
    	yDestination = cookY;
    }
    
    public void DoGoToCashier() {
    	displayString = "";
    	xDestination = 200;
    	yDestination = -19;
    }
    
    public void DoGetFood() {
    	displayString = "";
    	xDestination = cookX;
    	yDestination = cookY;
    }
    
    public void DoDeliverFood (String choice, int tableNumber) {
    	displayString = choice.substring(0, 2);
    	currentTableNum = tableNumber;
    	xDestination = xTable + 20 + (100*(currentTableNum-1));
        yDestination = yTable - 20;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
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
