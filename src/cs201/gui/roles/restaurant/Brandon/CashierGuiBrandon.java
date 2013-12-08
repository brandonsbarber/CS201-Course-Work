package cs201.gui.roles.restaurant.Brandon;

import java.awt.Color;
import java.awt.Graphics2D;

import cs201.gui.Gui;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelBrandon;
import cs201.roles.restaurantRoles.Brandon.RestaurantCashierRoleBrandon;
import cs201.roles.restaurantRoles.Brandon.RestaurantCustomerRoleBrandon;

public class CashierGuiBrandon implements Gui {

	boolean present;
	
	RestaurantCashierRoleBrandon agent;
	
	public CashierGuiBrandon(RestaurantCashierRoleBrandon c){ //HostAgent m) {
		agent = c;
		/*xPos = OFFSCREEN_DEST_X;
		yPos = OFFSCREEN_DEST_Y;
		xDestination = OFFSCREEN_DEST_X;
		yDestination = OFFSCREEN_DEST_Y;*/
	}
	
	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.GRAY);
		g.fillRect(RestaurantAnimationPanelBrandon.CASHIER_X, RestaurantAnimationPanelBrandon.CASHIER_Y, 25, 25);
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
