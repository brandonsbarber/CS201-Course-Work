package cs201.gui.roles.market;

import java.awt.Color;
import java.awt.Graphics2D;

import cs201.gui.Gui;
import cs201.roles.marketRoles.MarketManagerRole;

public class MarketEmployeeGui implements Gui {

	private MarketManagerRole role = null;
	private boolean isPresent = false;
	
	public static final int EMPLOYEE_SIZE = 20;
	public static final int EMPLOYEE_HOME_X = 50;
	public static final int EMPLOYEE_HOME_Y = 25;

	private int xPos, yPos, xDestination, yDestination;
	
	public MarketEmployeeGui() {
		this(null);
	}

	public MarketEmployeeGui(MarketManagerRole c) {
		role = c;
		xPos = EMPLOYEE_HOME_X;
		yPos = EMPLOYEE_HOME_Y;
		isPresent = true;
	}

	public void updatePosition() {
		if (xPos < xDestination) {
			xPos++;
		} else if (xPos > xDestination) {
			xPos--;
		}
		
		if (yPos < yDestination) {
			yPos++;
		} else if (yPos > yDestination) {
			yPos--;
		}
		
		return;
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.orange);
		g.fillRect(xPos, yPos, EMPLOYEE_SIZE, EMPLOYEE_SIZE);
		
		g.setColor(Color.black);
		g.drawString("Market Employee", xPos, yPos - 3);
	}

	public boolean isPresent() {
		return isPresent;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public int X() {
		return xPos;
	}
	
	public int Y() {
		return yPos;
	}
	
	public void doAnimate() {
		xDestination = 200;
		yDestination = 200;
	}
}
