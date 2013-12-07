package cs201.gui.roles.restaurant.Brandon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cs201.gui.Gui;
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
	
	
	
	private List<Order> orders;
	
	private int numTables;
	
	public KitchenGuiBrandon(int numTables)
	{
		this.numTables = numTables;
		orders = Collections.synchronizedList(new ArrayList<Order>());
	}
	
	public void draw(Graphics2D g)
	{
		g.setColor(Color.GREEN.darker().darker());
		g.fillRect(GRILL_X,GRILL_Y,COUNTER_WIDTH,(int)COUNTER_HEIGHT);
		g.fillRect(COUNTER_X,COUNTER_Y,COUNTER_WIDTH,(int)COUNTER_HEIGHT);
		
		double dimen = COUNTER_HEIGHT/numTables;
		
		g.setColor(Color.BLACK);
		for(int i = 0; i < numTables; i++)
		{
			g.drawRect(GRILL_X,(int)(GRILL_Y+dimen*i),COUNTER_WIDTH,(int)dimen);
			g.drawRect(COUNTER_X,(int)(GRILL_Y+dimen*i),COUNTER_WIDTH,(int)dimen);
		}
		
		g.drawString("Oven",GRILL_X,GRILL_Y);
		g.drawString("Counter",COUNTER_X,COUNTER_Y);
		
		for(Order o : orders)
		{
			if(o.getState() == OrderState.Cooking)
			{
				g.drawString(o.getChoice(), GRILL_X, (int)(GRILL_Y + dimen*o.getTable()));
			}
			else if(o.getState() == OrderState.Plated)
			{
				g.drawString(o.getChoice(), COUNTER_X, (int)(COUNTER_Y + dimen*o.getTable()));
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
}
