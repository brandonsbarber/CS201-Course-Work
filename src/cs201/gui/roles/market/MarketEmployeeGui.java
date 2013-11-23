package cs201.gui.roles.market;

import java.awt.Color;
import java.awt.Graphics2D;

import cs201.gui.Gui;
import cs201.gui.structures.market.MarketAnimationPanel;
import cs201.roles.marketRoles.MarketManagerRole;

public class MarketEmployeeGui implements Gui {

	private MarketManagerRole role = null;
	private boolean isPresent = false;
	
	public static final int EMPLOYEE_SIZE = 20;
	public static final int EMPLOYEE_HOME_X = 50;
	public static final int EMPLOYEE_HOME_Y = 25;

	private int xPos, yPos, xDestination, yDestination;
	
	private enum IsleMovementState {MOVE_OUT_OF_ISLE, MOVE_TO_ISLE, MOVE_TO_SHELF};
	
	private int isleDestination = -1;
	private boolean goingToManager = false;
	private boolean goingHome = false;
	
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
		
		if (goingToManager) {
			if (yPos < (MarketAnimationPanel.FIRST_SHELF_Y + MarketAnimationPanel.SHELF_HEIGHT + EMPLOYEE_SIZE)) {
				yPos++;
				return;
			}
		}
		if (isleNumber(xPos) != isleDestination && isleDestination != -1) {
			if (yPos > MarketAnimationPanel.FIRST_SHELF_Y - EMPLOYEE_SIZE - 10) {
				yPos--;
			} else {
				if (isleDestination > isleNumber(xPos)) xPos++; else xPos--;
			}
		}
		else if (xPos < xDestination) {
			xPos++;
		} else if (xPos > xDestination) {
			xPos--;
		}
		else if (yPos < yDestination) {
			yPos++;
		} else if (yPos > yDestination) {
			yPos--;
		}
		else {
			goingToManager = false;
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
	
	public void doGoToItemOnShelf(int isleNumber, int itemNumber) {
		xDestination = MarketAnimationPanel.FIRST_SHELF_X + MarketAnimationPanel.SHELF_WIDTH + 5 + MarketAnimationPanel.SHELF_SPACING * isleNumber;
		yDestination = MarketAnimationPanel.FIRST_SHELF_Y + (int)(MarketAnimationPanel.SHELF_HEIGHT * (itemNumber / 10.0));
		isleDestination = isleNumber;
	}
	
	public void doGoToManager() {
		xDestination = MarketAnimationPanel.FRONT_DESK_X - EMPLOYEE_SIZE - 15;
		yDestination = MarketAnimationPanel.FRONT_DESK_Y;
		isleDestination = -1;
		goingToManager = true;
	}
	
	public void doGoHome() {
		xDestination = EMPLOYEE_HOME_X;
		yDestination = EMPLOYEE_HOME_Y;
		
	}
	
	private int isleNumber(int x) {
		int offsetXPos = x - MarketAnimationPanel.FIRST_SHELF_X;
		int isleWidth = MarketAnimationPanel.SHELF_SPACING;
		int isleNumber = offsetXPos /  isleWidth;
		return isleNumber;
	}
	
}
