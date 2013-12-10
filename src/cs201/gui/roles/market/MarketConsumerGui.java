package cs201.gui.roles.market;

import java.awt.Color;
import java.awt.Graphics2D;

import cs201.gui.ArtManager;
import cs201.gui.Gui;
import cs201.gui.structures.market.MarketAnimationPanel;
import cs201.helper.Constants;
import cs201.roles.marketRoles.MarketConsumerRole;

public class MarketConsumerGui implements Gui {

	private MarketConsumerRole role = null;
	private boolean isPresent = false;
	private MarketAnimationPanel animPanel = null;
	
	public static final int CONSUMER_SIZE = 20;
	public static final int START_X = 250;
	public static final int START_Y = 540;

	private int xPos, yPos;
	
	private int xDestination, yDestination;
	boolean animating = false;
	String moveDir = "Default_Walker_Down";
	
	public MarketConsumerGui() {
		this(null);
	}

	public MarketConsumerGui(MarketConsumerRole c) {
		role = c;
		xPos = START_X;
		yPos = START_Y;
		xDestination = START_X;
		yDestination = START_Y;
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
		if (Constants.DEBUG_MODE) {
			g.setColor(Color.cyan);
			g.fillRect(xPos, yPos, CONSUMER_SIZE, CONSUMER_SIZE);
			
			g.setColor(Color.black);
			g.drawString("Market Consumer", xPos, yPos - 3);
		} else {
			if (animating) {
				double theta = Math.atan2(yPos - yDestination, xDestination - xPos);
				
				if (theta >= -Math.PI/4 && theta <= Math.PI/4) {
					moveDir = "Default_Walker_Right";
				} else if (theta >= Math.PI/4 && theta <= 3*Math.PI/4) {
					moveDir = "Default_Walker_Up";
				} else if (theta <= -Math.PI/4 && theta >= -3*Math.PI/4) {
					moveDir = "Default_Walker_Down";
				} else {
					moveDir = "Default_Walker_Left";
				}
			} else {
				moveDir = "Default_Walker_Up";
			}
			
			g.drawImage(ArtManager.getImage(moveDir), xPos, yPos, CONSUMER_SIZE, CONSUMER_SIZE, null);
		}
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
		int x;
		if (animPanel != null) {
			x = 240 + (CONSUMER_SIZE + 5) * animPanel.whatNumberAmI(this);
		}
		else {
			x = 240;
		}
		
		xDestination = x;
		yDestination = 400;
		animating = true;
	}
	
	public void doLeaveMarket() {
		animPanel.leaving(this);
		xDestination = 300;
		yDestination = 540;
		animating = true;
	}
	
	public void setRole(MarketConsumerRole r) {
		role = r;
	}
	
	public void setAnimationPanel(MarketAnimationPanel p) {
		animPanel = p;
	}
}
