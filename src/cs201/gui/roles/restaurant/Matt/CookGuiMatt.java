package cs201.gui.roles.restaurant.Matt;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cs201.gui.Gui;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelMatt;
import cs201.helper.Constants;
import cs201.roles.restaurantRoles.Matt.RestaurantCookRoleMatt;

public class CookGuiMatt implements Gui {

	private RestaurantCookRoleMatt role;
	private boolean isPresent;
	
	private List<String> cooking;
	private List<String> plating;
	
	private final int COOKSIZE = (RestaurantAnimationPanelMatt.WINDOWX < RestaurantAnimationPanelMatt.WINDOWY) ? (int)(RestaurantAnimationPanelMatt.WINDOWX * .04f) : (int)(RestaurantAnimationPanelMatt.WINDOWY * .04f);
	private final int COOKX = (int)(RestaurantAnimationPanelMatt.WINDOWX * .47f);
	private final int COOKY = (int)(RestaurantAnimationPanelMatt.WINDOWY * .95f);
	private final int cookingAreaX = (int)(RestaurantAnimationPanelMatt.WINDOWX * .684f);
	private final int cookingAreaY = (int)(RestaurantAnimationPanelMatt.WINDOWY * .85f);
	private final int platingAreaX = (int)(RestaurantAnimationPanelMatt.WINDOWX * .2f);
	private final int platingAreaY = (int)(RestaurantAnimationPanelMatt.WINDOWY * .85f);

	public CookGuiMatt(RestaurantCookRoleMatt c) {
		role = c;
		isPresent = false;
		cooking = Collections.synchronizedList(new ArrayList<String>());
		plating = Collections.synchronizedList(new ArrayList<String>());
	}

	public void updatePosition() {			
		return;
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		
		if (Constants.DEBUG_MODE) {
			g.drawString("Cook", COOKX, COOKY);
		}
		
		int x = cookingAreaX;
		int y = cookingAreaY;
		synchronized(cooking) {
			for (String food : cooking) {
				g.drawString(food, x, y);
				y += 13;
			}
		}
		
		x = platingAreaX;
		y = platingAreaY;
		synchronized(plating) {
			for (String food : plating) {
				g.drawString(food, x, y);
				y += 13;
			}
		}
		
		g.setColor(Color.WHITE);
		g.fillRect(COOKX, COOKY, COOKSIZE, COOKSIZE);
	}
	
	public void addCookingItem(String food) {
		cooking.add(food);
	}
	
	public void removeCookingItem(String food) {
		cooking.remove(food);
	}
	
	public void addPlatingItem(String food) {
		plating.add(food);
	}
	
	public void removePlatingItem(String food) {
		plating.remove(food);
	}

	public boolean isPresent() {
		return isPresent;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}
}
