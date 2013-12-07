package cs201.gui.roles.restaurant.Matt;

import java.awt.Color;
import java.awt.Graphics2D;

import cs201.gui.Gui;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelMatt;
import cs201.helper.Constants;
import cs201.roles.restaurantRoles.Matt.RestaurantHostRoleMatt;

public class HostGuiMatt implements Gui {
	private final int HOSTSIZE = (RestaurantAnimationPanelMatt.WINDOWX < RestaurantAnimationPanelMatt.WINDOWY) ? (int)(RestaurantAnimationPanelMatt.WINDOWX * .04f) : (int)(RestaurantAnimationPanelMatt.WINDOWY * .04f);
	private final int HOSTX = (int)(RestaurantAnimationPanelMatt.WINDOWX * .8f);
	private final int HOSTY = (int)(RestaurantAnimationPanelMatt.WINDOWY * .47f);
	
	private boolean isPresent;
	RestaurantHostRoleMatt role;
	
	public HostGuiMatt(RestaurantHostRoleMatt r) {
		role = r;
	}
	
	@Override
	public void updatePosition() {
		return;
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(HOSTX, HOSTY, HOSTSIZE, HOSTSIZE);
		
		if (Constants.DEBUG_MODE) {
			g.drawString("Host", HOSTX, HOSTY);
		}
	}

	@Override
	public boolean isPresent() {
		return isPresent;
	}

	@Override
	public void setPresent(boolean present) {
		isPresent = present;
	}

}
