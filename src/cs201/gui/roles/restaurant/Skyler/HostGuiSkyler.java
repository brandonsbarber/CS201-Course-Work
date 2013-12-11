package cs201.gui.roles.restaurant.Skyler;

import java.awt.*;

import cs201.gui.Gui;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelSkyler;
import cs201.interfaces.roles.restaurant.Skyler.CustomerSkyler;
import cs201.interfaces.roles.restaurant.Skyler.HostSkyler;

public class HostGuiSkyler implements Gui {

    private HostSkyler agent = null;

    private int xPos = 90, yPos = 0;//default waiter position
    private int xDestination = 90, yDestination = 0;//default start position
    private int currentTableNum;  
    
    private boolean isPresent = false;

    public static final int xTable = 150;
    public static final int yTable = 250;

    RestaurantAnimationPanelSkyler animationPanel;
    
    public HostGuiSkyler(HostSkyler host, RestaurantAnimationPanelSkyler panel) {
        this.agent = host;
        animationPanel = panel;
    }

    public void updatePosition() {
    	if (xPos == -19 && yPos == -19 && xDestination == -19 && yDestination == -19 && !agent.getWaitingCustomers().isEmpty())
    		{
    		//agent.msgAtFront();
    		xDestination = -20;
    		yDestination = -20;
    		}
    		//agent.msgAtFront();
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;

        if (xPos == xDestination && yPos == yDestination
        		&& (xDestination == xTable + 20 + (100*(currentTableNum-1))) && (yDestination == yTable - 20)) {
           //agent.msgAtTable();
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
        
        /*for (int i=0; i < agent.getNTables(); i++) {
        g.setColor(Color.ORANGE);
        g.fillRect(xTable+(100*i), yTable, 50, 50);//200 and 250 need to be table params
        }*/
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void DoBringToTable(CustomerSkyler customer, int tableNumber) {
    	currentTableNum = tableNumber;
        xDestination = xTable + 20 + (100*(currentTableNum-1));
        yDestination = yTable - 20;
        
    }

    public void DoLeaveCustomer() {
        xDestination = -19;
        yDestination = -19;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

	@Override
	public void setPresent(boolean present) {
		isPresent = present;
	}
    
}
