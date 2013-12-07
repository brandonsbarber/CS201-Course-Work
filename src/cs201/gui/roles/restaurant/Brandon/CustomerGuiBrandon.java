package cs201.gui.roles.restaurant.Brandon;

import java.awt.*;
import java.util.concurrent.Semaphore;

import cs201.gui.Gui;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelBrandon;
import cs201.roles.restaurantRoles.Brandon.RestaurantCustomerRoleBrandon;

public class CustomerGuiBrandon implements Gui{

	private RestaurantCustomerRoleBrandon agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;

	private int xPos, yPos;
	private int xDestination, yDestination;

	public static final int xTable = 200;
	public static final int yTable = 250;

	private static final int OFFSCREEN_DEST_Y = -40;
	private static final int OFFSCREEN_DEST_X = -40;
	public static final int CUSTOMER_SIZE_Y = 20;
	public static final int CUSTOMER_SIZE_X = 20;
	
	private boolean eventFired = true;
	
	private Semaphore waitForTable = new Semaphore(0,true);
	
	private Dimension tableDestination;
	
	private String orderChoice = "";
	private boolean isOrder = false;
	
	public CustomerGuiBrandon(RestaurantCustomerRoleBrandon c){ //HostAgent m) {
		agent = c;
		xPos = OFFSCREEN_DEST_X;
		yPos = OFFSCREEN_DEST_Y;
		xDestination = OFFSCREEN_DEST_X;
		yDestination = OFFSCREEN_DEST_Y;
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

		if (!eventFired && xPos == xDestination && yPos == yDestination)
		{
			agent.msgReachedDestination();
			eventFired = true;
		}
	}
	
	public void didLeaveRestaurant()
	{
		isHungry = false;
		//gui.updateAgent(agent);
		setPresent(false);
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, CUSTOMER_SIZE_X, CUSTOMER_SIZE_Y);
		
		g.setColor(Color.BLACK);
		g.drawString(agent.getName(),xPos,yPos);
		
		if(!orderChoice.equals(""))
        {
        	String s = isOrder?"?":"";
        	s += orderChoice;
        	g.drawString(s,xPos,yPos+CUSTOMER_SIZE_Y);
        }
	}

	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		isHungry = true;
		agent.gotHungry();
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void setTableDestination(Dimension clone)
	{
		tableDestination = clone;
		waitForTable.release();
	}

	public void doLeaveRestaurant()
	{
		xDestination = OFFSCREEN_DEST_X;
		yDestination = OFFSCREEN_DEST_Y;
		eventFired = false;
	}
	
	public void doGoToTable()
	{
		try
		{
			waitForTable.acquire();
		}
		catch(Exception e)
		{
			System.out.println("Error in waiting for table information.");
		}
		xDestination = tableDestination.width;
		yDestination = tableDestination.height;
		eventFired = false;
	}
	
	public void doSelectOrder(String order)
	{
		orderChoice = order;
		isOrder = true;
		System.out.println("CustomerGUI did get call to show order: "+order);
	}
	
	public void doReceiveFood(String order)
	{
		orderChoice = order;
		isOrder = false;
		System.out.println("CustomerGUI did get call to show food: "+order);

	}
	
	public void doRemoveLabel()
	{
		orderChoice = "";
	}

	public void doGoToCashier()
	{
		xDestination = RestaurantAnimationPanelBrandon.CASHIER_X;
		yDestination = RestaurantAnimationPanelBrandon.CASHIER_Y;
		eventFired = false;
	}

	public void doSetBill(double billAmount)
	{
		orderChoice = "$"+billAmount;
		isOrder = false;
	}
	
	public void doGoToRestaurant()
	{
		xDestination = RestaurantAnimationPanelBrandon.CUSTOMER_WAITING_AREA_X;
		yDestination = RestaurantAnimationPanelBrandon.CUSTOMER_WAITING_AREA_Y;
		eventFired = false;
	}
}
