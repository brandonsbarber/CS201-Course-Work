package cs201.gui.roles.market;

import java.awt.Color;
import java.awt.Graphics2D;

import cs201.gui.ArtManager;
import cs201.gui.Gui;
import cs201.helper.Constants;
import cs201.roles.marketRoles.MarketManagerRole;

public class MarketManagerGui implements Gui {

	private MarketManagerRole role = null;
	private boolean isPresent = false;
	
	public static final int MANAGER_SIZE = 20;
	public static final int MANAGER_X = 250;
	public static final int MANAGER_Y = 320;

	private int xPos, yPos;
	
	public MarketManagerGui() {
		this(null);
	}

	public MarketManagerGui(MarketManagerRole c) {
		role = c;
		xPos = MANAGER_X;
		yPos = MANAGER_Y;
		isPresent = false;
	}

	public void updatePosition() {	
		return;
	}

	public void draw(Graphics2D g) {
		if (Constants.DEBUG_MODE) {
			g.setColor(Color.GREEN);
			g.fillRect(xPos, yPos, MANAGER_SIZE, MANAGER_SIZE);
			
			g.setColor(Color.black);
			g.drawString("Market Manager", xPos, yPos - 3);
		} else {
			// Draw the market manager
			g.drawImage(ArtManager.getImage("Market_Manager_Down"), xPos, yPos, MANAGER_SIZE, MANAGER_SIZE, null);
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
	
	public void setRole(MarketManagerRole r) {
		role = r;
	}
}
