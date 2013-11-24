package cs201.gui.roles.restaurant.Matt;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cs201.gui.Gui;
import cs201.gui.structures.restaurant.RestaurantGuiMatt;
import cs201.roles.restaurantRoles.Matt.RestaurantCookRoleMatt;

public class CookGuiMatt implements Gui {

	private RestaurantCookRoleMatt role;
	private boolean isPresent;
	
	private List<String> cooking;
	private List<String> plating;
	
	private final int cookingAreaX = 342;
	private final int cookingAreaY = 425;
	private final int platingAreaX = 100;
	private final int platingAreaY = 425;

	public CookGuiMatt(RestaurantCookRoleMatt c) {
		role = c;
		isPresent = true;
		cooking = Collections.synchronizedList(new ArrayList<String>());
		plating = Collections.synchronizedList(new ArrayList<String>());
	}

	public void updatePosition() {			
		return;
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		
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
