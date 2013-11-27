package cs201.gui.roles.market;

import java.awt.Color;
import java.awt.Graphics2D;

import cs201.gui.Gui;
import cs201.roles.marketRoles.MarketConsumerRole;

public class MarketConsumerGui implements Gui {

	private MarketConsumerRole role = null;
	private boolean isPresent = false;
	
	public static final int CONSUMER_SIZE = 20;
	public static final int START_X = 250;
	public static final int START_Y = 540;

	private int xPos, yPos;
	
	private int xDestination, yDestination;
	boolean animating = false;
	
	public MarketConsumerGui() {
		this(null);
	}

	public MarketConsumerGui(MarketConsumerRole c) {
		role = c;
		xPos = START_X;
		yPos = START_Y;
		isPresent = false;
	}

	public void updatePosition() {
		if (xPos < xDestination) {
			xPos++;
		} else if (xPos > xDestination) {
			xPos--;
		} else if (yPos < yDestination) {
			yPos++;
		} else if (yPos > yDestination) {
			yPos--;
		}
		
		if (xPos == xDestination && yPos == yDestination && animating) {
			if (role != null)
				role.animationFinished();
			animating = false;
		}
		
		return;
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.cyan);
		g.fillRect(xPos, yPos, CONSUMER_SIZE, CONSUMER_SIZE);
		
		g.setColor(Color.black);
		g.drawString("Market Consumer", xPos, yPos - 3);
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
	
	public void doWalkToManager() {
		xDestination = 300;
		yDestination = 400;
		animating = true;
	}
	
	public void doLeaveMarket() {
		xDestination = 300;
		yDestination = 540;
		animating = true;
	}
	
	public void setRole(MarketConsumerRole r) {
		role = r;
	}
}
