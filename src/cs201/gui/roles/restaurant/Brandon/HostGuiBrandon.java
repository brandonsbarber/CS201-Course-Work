package cs201.gui.roles.restaurant.Brandon;

import java.awt.Color;
import java.awt.Graphics2D;

import cs201.gui.ArtManager;
import cs201.gui.Gui;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelBrandon;
import cs201.helper.Constants;
import cs201.roles.restaurantRoles.Brandon.RestaurantCookRoleBrandon;
import cs201.roles.restaurantRoles.Brandon.RestaurantHostRoleBrandon;

public class HostGuiBrandon implements Gui {
	private RestaurantHostRoleBrandon host;
	
	int x;
	int y;
	int destX;
	int destY;
	
	boolean present;

	private boolean eventFired;
	
	public HostGuiBrandon(RestaurantHostRoleBrandon host)
	{
		this.host = host;
		x = RestaurantAnimationPanelBrandon.HOST_X;
		y = RestaurantAnimationPanelBrandon.HOST_Y;
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
			host.msgReachedDestination();
			eventFired = true;
		}
	}

	@Override
	public void draw(Graphics2D g) {
		if(Constants.DEBUG_MODE)
		{
			g.setColor(Color.YELLOW);
			g.fillRect(x, y, 20, 20);
		}
		else
		{
			if(destY < y)
			{
				g.drawImage(ArtManager.getImage("Host_Up"), x,y,20,20,null);
			}
			else if(destY > y)
			{
				g.drawImage(ArtManager.getImage("Host_Down"), x,y,20,20,null);
			}
			else if(destX > x)
			{
				g.drawImage(ArtManager.getImage("Host_Right"), x,y,20,20,null);
			}
			else if(destX < x)
			{
				g.drawImage(ArtManager.getImage("Host_Left"), x,y,20,20,null);
			}
    		else
    		{
    			g.drawImage(ArtManager.getImage("Host_Down"), x,y,20,20,null);
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
