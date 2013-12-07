package cs201.gui.roles.restaurant.Brandon;

import java.awt.*;
import java.util.HashMap;

import cs201.gui.Gui;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelBrandon;
import cs201.interfaces.roles.restaurant.Brandon.CustomerBrandon;
import cs201.roles.restaurantRoles.Brandon.RestaurantWaiterRoleBrandon;

public class WaiterGuiBrandon implements Gui
{
	private KitchenGuiBrandon kitchen;
	
    private RestaurantWaiterRoleBrandon agent = null;

    private int xPos = WAITER_OFFSCREEN_X, yPos = WAITER_OFFSCREEN_Y;//default waiter position
    private int xDestination = WAITER_OFFSCREEN_X, yDestination = WAITER_OFFSCREEN_Y;//default start position

    public static int xTable = 200;
    public static int yTable = 250;

	private static final int WAITER_DIMEN_Y = 20;

	private static final int WAITER_DIMEN_X = 20;

	private static final int WAITER_OFFSCREEN_Y = -20;
	private static final int WAITER_OFFSCREEN_X = -20;
	
	private static final int CHILL_X = RestaurantAnimationPanelBrandon.WAITER_REST_X;
	private static final int CHILL_Y = RestaurantAnimationPanelBrandon.WAITER_REST_Y;
	
	private HashMap<Integer, Dimension> tables;// = new HashMap<Integer,Dimension>();
	
	private boolean eventFired = true;
	
	private String currentCarry = "";
	private boolean isCarryingOrder = false;
	
	private RestaurantAnimationPanelBrandon gui;
	
	private int idNumber;
	
    public WaiterGuiBrandon(RestaurantWaiterRoleBrandon agent,RestaurantAnimationPanelBrandon gui)
    {
        this.agent = agent;
        this.gui = gui;
    }
    
    public void setKitchen(KitchenGuiBrandon k)
    {
    	this.kitchen = k;
    }
    
    public void setTables(HashMap<Integer,Dimension> newTables)
    {
    	tables = newTables;
    }

    public void updatePosition() {
    	//System.out.println("Updating Position");
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;
        
        if (!eventFired && xPos == xDestination && yPos == yDestination)
        {
        	eventFired = true;
        	agent.msgReachedDestination();
        }
    }

    public void draw(Graphics2D g) {
    	//System.out.println("DRAWING WAITER");
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, WAITER_DIMEN_X, WAITER_DIMEN_Y);
        
        g.setColor(Color.BLACK);
        g.drawString(agent.getName(),xPos,yPos);
        g.drawRect(xPos, yPos, WAITER_DIMEN_X, WAITER_DIMEN_Y);
        
        if(!currentCarry.equals(""))
        {
        	String s = isCarryingOrder?"?":"";
        	s += currentCarry;
        	g.drawString(s,xPos,yPos+WAITER_DIMEN_Y);
        }
    }

    public boolean isPresent() {
        return present;
    }

    public void doSeat(CustomerBrandon c, int tableNum)
    {
    	Dimension dim = tables.get(tableNum);
    	c.getGui().setTableDestination((Dimension)dim.clone());
    	doGoToTable(tableNum);
    }
    
    public void doGoToTable(int tableNum)
    {
    	Dimension dim = tables.get(tableNum);
    	xDestination = dim.width + WAITER_DIMEN_X;
    	yDestination = dim.height - WAITER_DIMEN_Y;
    	eventFired = false;
    }
    
    public void doGoToKitchen(int tableOrderNum)
    {
    	xDestination = KitchenGuiBrandon.COUNTER_X + KitchenGuiBrandon.COUNTER_WIDTH;
    	yDestination = KitchenGuiBrandon.COUNTER_Y+(int)(kitchen.getTableDimension()*tableOrderNum)-WAITER_DIMEN_Y;
    	System.out.println("GOING TO KITCHEN");
    	eventFired = false;
    }
    
    public void doGoToWaitingArea()
    {
        xDestination = RestaurantAnimationPanelBrandon.CUSTOMER_WAITING_AREA_X+WAITER_DIMEN_X;
        yDestination = RestaurantAnimationPanelBrandon.CUSTOMER_WAITING_AREA_Y+WAITER_DIMEN_Y;
        eventFired = false;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    public void doCarryFood(String order)
    {
    	this.currentCarry = order;
    	isCarryingOrder = false;
    }
    
    public void doCarryOrder(String order)
    {
    	this.currentCarry = order;
    	isCarryingOrder = true;
    }
    
    public void doDropCarry()
    {
    	this.currentCarry = "";
    }
    
    public void doGoOnBreak()
    {
    	xDestination = RestaurantAnimationPanelBrandon.BREAK_X;
    	yDestination = RestaurantAnimationPanelBrandon.BREAK_Y;
    	eventFired = false;
    	onBreak = true;
    	//gui.updateAgent(agent);
    }
    
    public void signalGoOnBreak()
    {
    	agent.msgGotBreak();
    	breakTime = true;
    	System.out.println("GOT SIGNAL TO BREAK IN GUI");
    	//gui.updateAgent(agent);
    }
    
    public void signalGoOffBreak()
    {
    	breakTime = false;
    	onBreak = false;
    	agent.msgGoOffBreak();
    	//gui.updateAgent(agent);
    }
    
    private boolean breakTime = false;
    private boolean onBreak = false;

	private boolean present;
    
    public boolean getBreak()
    {
    	return breakTime;
    }
    
    public boolean getBreakToggle()
    {
    	System.out.println("BREAK TOGGLE "+(breakTime == onBreak));
    	System.out.println(breakTime);
    	System.out.println(onBreak);
    	return breakTime == onBreak;
    }
    
    public void breakDenied()
    {
    	breakTime = false;
    	//gui.updateAgent(agent);	
    }

	public void doGoToCashier()
	{
		xDestination = RestaurantAnimationPanelBrandon.CASHIER_X;
		yDestination = RestaurantAnimationPanelBrandon.CASHIER_Y;
		eventFired = false;
	}
	
	/**
	 * Stay frosty my friend.
	 */
	public void doChillOut()
	{
		xDestination = CHILL_X;
		yDestination = CHILL_Y+idNumber*2;
		eventFired = false;
	}

	public void setInstanceNumber(int idNumber)
	{
		this.idNumber = idNumber;
	}

	@Override
	public void setPresent(boolean present)
	{
		this.present = present;
	}
}
