package cs201.gui.roles.restaurant.Brandon;

import java.awt.Color;
import java.awt.Graphics2D;

import cs201.gui.ArtManager;
import cs201.gui.Gui;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelBrandon;
import cs201.helper.Constants;
import cs201.roles.restaurantRoles.Brandon.RestaurantCashierRoleBrandon;
import cs201.roles.restaurantRoles.Brandon.RestaurantCustomerRoleBrandon;

public class CashierGuiBrandon implements Gui {

	boolean present;
	
	int x;
	int y;
	int destX;
	int destY;
	
	private boolean eventFired;
	
	RestaurantCashierRoleBrandon agent;
	
	public CashierGuiBrandon(RestaurantCashierRoleBrandon c){ //HostAgent m) {
		agent = c;
		x = RestaurantAnimationPanelBrandon.CASHIER_X;
		y = RestaurantAnimationPanelBrandon.CASHIER_Y;
		destX = x;
		destY = y;
	}
	
	@Override
	public void updatePosition() {
		if(destY > y)
		{
			y++;
		}
		if(destY < y)
		{
			y--;
		}
		
		if (!eventFired && x == destX && y == destY)
		{
			agent.msgReachedDestination();
			eventFired = true;
		}
	}

	@Override
	public void draw(Graphics2D g) {
		if(Constants.DEBUG_MODE)
		{
			g.setColor(Color.GRAY);
			g.fillRect(x,y, 25, 25);
		}
		else
		{
			if(destY < y)
			{
				g.drawImage(ArtManager.getImage("Cashier_Up"), x,y,20,20,null);
			}
			else if(destY > y)
			{
				g.drawImage(ArtManager.getImage("Cashier_Down"), x,y,20,20,null);
			}
			else if(destX < x)
			{
				g.drawImage(ArtManager.getImage("Cashier_Left"), x,y,20,20,null);
			}
			else if(destX > x)
			{
				g.drawImage(ArtManager.getImage("Cashier_Right"), x,y,20,20,null);
			}
			else
			{
				g.drawImage(ArtManager.getImage("Cashier_Down"), x,y,20,20,null);
			}
		}
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return present;
	}

	@Override
	public void setPresent(boolean present) {
		this.present = present;
	}

}
