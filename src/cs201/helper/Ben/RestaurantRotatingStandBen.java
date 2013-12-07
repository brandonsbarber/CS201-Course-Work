package cs201.helper.Ben;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cs201.gui.Gui;
import cs201.helper.Constants;
import cs201.interfaces.roles.restaurant.Ben.CookBen;
import cs201.interfaces.roles.restaurant.Ben.WaiterBen;

public class RestaurantRotatingStandBen implements Gui {
	
	public static final int ROTATING_STAND_SIZE 	= 15;
	public static final int ROTATING_STAND_Y 		= 50;
	public static final int ROTATING_STAND_X 		= 270;
	
	List<RotatingStandOrderBen> orders = Collections.synchronizedList(new ArrayList<RotatingStandOrderBen>());
	boolean isPresent = false;

	/**
	 * An order to go on this rotating stand.
	 */
	static public class RotatingStandOrderBen {
		public WaiterBen 	waiter;
		public String		choice;
		public int			table;
		
		public RotatingStandOrderBen(WaiterBen theWaiter, String theChoice, int theTable) {
			waiter = theWaiter;
			choice = theChoice;
			table = theTable;
		}
	}
	
	/**
	 * Add an order to the stand.
	 * @param theOrder The order to add
	 */
	public void addOrder(RotatingStandOrderBen theOrder) {
		orders.add(theOrder);
	}
	
	/**
	 * Removes the last order from the stand
	 * @return A RotatingStandOrderBen that is the oldest on the stand
	 * or null if the stand is empty.
	 */
	public RotatingStandOrderBen removeOrder() {
		if (orders.size() > 0) {
			return orders.remove(0);
		} else {
			return null;
		}
	}
	
	/**
	 * @return The number of orders on the stand.
	 */
	public int orderCount() {
		return orders.size();
	}

	@Override
	public void updatePosition() {
		// Nothing to do here, as the stand doesn't move
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.GRAY);
		g.fillOval(ROTATING_STAND_X, ROTATING_STAND_Y, ROTATING_STAND_SIZE, ROTATING_STAND_SIZE);
		
		if (Constants.DEBUG_MODE) {
			g.setColor(Color.BLACK);
			g.drawString("Stand", ROTATING_STAND_X, ROTATING_STAND_Y);
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
