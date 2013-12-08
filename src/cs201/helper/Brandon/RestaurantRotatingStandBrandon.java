package cs201.helper.Brandon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cs201.gui.Gui;
import cs201.gui.roles.restaurant.Brandon.KitchenGuiBrandon;
import cs201.roles.restaurantRoles.Brandon.RestaurantWaiterRoleBrandon;

public class RestaurantRotatingStandBrandon implements Gui{
	
	public static int STAND_X = KitchenGuiBrandon.GRILL_X + KitchenGuiBrandon.COUNTER_WIDTH;
	public static int STAND_Y = KitchenGuiBrandon.GRILL_Y - 20;
	
	List<StandOrder> orders = Collections.synchronizedList(new ArrayList<StandOrder>());
	
	class StandOrder
	{
		public RestaurantWaiterRoleBrandon waiter;
		public String choice;
		public int table;
		
		public StandOrder(RestaurantWaiterRoleBrandon waiter,String choice, int table)
		{
			this.waiter = waiter;
			this.choice = choice;
			this.table = table;
		}
	}
	
	public void addOrder(RestaurantWaiterRoleBrandon waiter,String choice, int table)
	{
		orders.add(new StandOrder(waiter,choice,table));
	}
	
	public StandOrder removeOrder()
	{
		if(orders.size() > 0)
		{
			return orders.remove(0);
		}
		return null;
	}
	
	public int getNumOrders()
	{
		return orders.size();
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.gray.darker());
		g.fillRect(STAND_X, STAND_Y, 20, 20);
		g.setColor(Color.black);
		g.drawString(""+getNumOrders(), STAND_X, STAND_Y+20);
	}

	boolean present;
	
	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setPresent(boolean present) {
		this.present = present;		
	}
	
}
