package cs201.gui.roles.restaurant.Brandon;

import java.awt.Color;
import java.awt.Graphics2D;

import cs201.gui.ArtManager;
import cs201.gui.Gui;
import cs201.helper.Constants;
import cs201.roles.restaurantRoles.Brandon.RestaurantCookRoleBrandon;

public class CookGuiBrandon implements Gui {

	private RestaurantCookRoleBrandon cook;
	
	int x;
	int y;
	int destX;
	int destY;
	
	boolean present;
	
	KitchenGuiBrandon kitchen;

	private boolean eventFired;
	
	public CookGuiBrandon(RestaurantCookRoleBrandon cook,KitchenGuiBrandon kitchen)
	{
		this.cook = cook;
		x = KitchenGuiBrandon.COUNTER_X-(KitchenGuiBrandon.GRILL_X+KitchenGuiBrandon.COUNTER_WIDTH);
		y = KitchenGuiBrandon.COUNTER_Y;
		destX = x;
		destY = y;
		
		this.kitchen = kitchen;
	}
	
	public void doGoToSlot(int slot)
	{
		destY = kitchen.getSlotY(slot);
		eventFired = false;
	}
	
	public void doGoToChill()
	{
		destY = KitchenGuiBrandon.CHILL_Y;
		eventFired = false;
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
			cook.msgReachedDestination();
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
				g.drawImage(ArtManager.getImage("Cook_Up"), x,y,20,20,null);
			}
			else
			{
				g.drawImage(ArtManager.getImage("Cook_Down"), x,y,20,20,null);
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

	public void doGoToStand()
	{
		destX = cook.getStand().STAND_X;
		destY = cook.getStand().STAND_Y;
		eventFired = false;
	}

}
