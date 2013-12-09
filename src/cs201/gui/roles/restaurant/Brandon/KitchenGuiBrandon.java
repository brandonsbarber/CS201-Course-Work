package cs201.gui.roles.restaurant.Brandon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cs201.gui.ArtManager;
import cs201.gui.Gui;
import cs201.helper.Constants;
import cs201.roles.restaurantRoles.Brandon.RestaurantCookRoleBrandon.Order;
import cs201.roles.restaurantRoles.Brandon.RestaurantCookRoleBrandon.OrderState;

public class KitchenGuiBrandon implements Gui
{
	private static final double COUNTER_HEIGHT = 100.0;
	public static int COUNTER_X = 50;
	public static int COUNTER_Y = 300;
	
	public static int GRILL_X = 0;
	public static int GRILL_Y = 300;
	
	public static int COUNTER_WIDTH = 25;
	
	public static int GRILL_OFFSET_X = 7;
	public static int GRILL_OFFSET_Y = 5;
	
	public static int CHILL_Y = (int) (COUNTER_Y + COUNTER_HEIGHT-20);
	
	private List<Order> orders;
	
	private int numTables;
	
	public KitchenGuiBrandon(int numTables)
	{
		this.numTables = numTables;
		orders = Collections.synchronizedList(new ArrayList<Order>());
	}
	
	public void draw(Graphics2D g)
	{
		if(Constants.DEBUG_MODE)
		{
			
		}
		else
		{
			
		}
		
		//g.setColor(Color.GREEN.darker().darker());
		//g.fillRect(GRILL_X,GRILL_Y,COUNTER_WIDTH,(int)COUNTER_HEIGHT);
		g.drawImage(ArtManager.getImage("Restaurant_Brandon_Kitchen_Counter"),COUNTER_X,COUNTER_Y,COUNTER_WIDTH,(int)COUNTER_HEIGHT,null);
		
		double asrX = 1.0*COUNTER_WIDTH/18;
		double asrY = 1.0*COUNTER_HEIGHT/66;
		
		int topDimenX = (int)(40*asrX);
		int topDimenY = (int)(15*asrY);
		
		
		g.drawImage(ArtManager.getImage("Restaurant_Brandon_Kitchen_Counter_Top"), (int)(COUNTER_X-topDimenX+COUNTER_WIDTH), (int)(COUNTER_Y-topDimenY), topDimenX, topDimenY,null);
		g.drawImage(ArtManager.getImage("Restaurant_Brandon_Kitchen_Counter_Bottom"), (int)(COUNTER_X-topDimenX+COUNTER_WIDTH), (int)(COUNTER_Y+COUNTER_HEIGHT), topDimenX, topDimenY,null);
		//g.fillRect(COUNTER_X,COUNTER_Y,COUNTER_WIDTH,(int)COUNTER_HEIGHT);
	
		
		double dimen = COUNTER_HEIGHT/numTables;
		
		//g.setColor(Color.BLACK);
		for(int i = 0; i < numTables; i++)
		{
			g.drawImage(ArtManager.getImage("Restaurant_Brandon_Grill_Open"), GRILL_X, (int)(GRILL_Y+dimen*i), COUNTER_WIDTH, (int) dimen,null);
			//g.drawRect(GRILL_X,(int)(GRILL_Y+dimen*i),COUNTER_WIDTH,(int)dimen);
			//g.drawRect(COUNTER_X,(int)(GRILL_Y+dimen*i),COUNTER_WIDTH,(int)dimen);
		}
		
		//g.drawString("Oven",GRILL_X,GRILL_Y);
		//g.drawString("Counter",COUNTER_X,COUNTER_Y);
		
		for(Order o : orders)
		{
			if(o.getState() == OrderState.Cooking)
			{
				g.drawImage(ArtManager.getImage("Restaurant_Brandon_Pokeball"), GRILL_X+GRILL_OFFSET_X, (int)(GRILL_Y + dimen*(o.getTable()-1))+GRILL_OFFSET_Y,null);
				//g.drawString(o.getChoice(), GRILL_X, (int)(GRILL_Y + dimen*o.getTable()));
			}
			else if(o.getState() == OrderState.Plated)
			{
				g.drawImage(ArtManager.getImage("Restaurant_Brandon_Pokeball"), COUNTER_X+GRILL_OFFSET_X, (int)(COUNTER_Y + dimen*(o.getTable()-1))+GRILL_OFFSET_Y,null);
				//g.drawString(o.getChoice(), COUNTER_X, (int)(COUNTER_Y + dimen*o.getTable()));
			}
		}
	}
	
	public void addTable()
	{
		numTables++;
	}
	
	public void addOrder(Order o)
	{
		orders.add(o);
	}
	
	public void removeOrder(Order o)
	{
		orders.remove(o);
	}

	public double getTableDimension()
	{
		return COUNTER_HEIGHT / numTables;
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setPresent(boolean present) {
		// TODO Auto-generated method stub
		
	}

	public int getSlotY(int slot) {
		return (int)(COUNTER_Y+(1.0*COUNTER_HEIGHT/numTables)*(slot-1));
	}
}
