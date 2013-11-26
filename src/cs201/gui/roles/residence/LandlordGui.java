package cs201.gui.roles.residence;

import java.awt.Color;
import java.awt.Graphics2D;

import cs201.gui.Gui;
import cs201.roles.housingRoles.LandlordRole;

public class LandlordGui implements Gui {

	private LandlordRole role = null;
	private boolean isPresent;
	
	private final int WIDTH = 20, HEIGHT = 20;
	private final int startX = 100, startY = 100;
	
	private int xPos;
	private int yPos;
	private int xDestination;
	private int yDestination;
	
	private int deskX;
	private int deskY;
	
	
	public LandlordGui() {
		this(null);
	}
	
	public LandlordGui(LandlordRole newRole) {
		xPos = startX;
		yPos = startY;
		xDestination = xPos;
		yDestination = yPos;
		role = newRole;
		isPresent = false;
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		if (xPos<xDestination) {
			xPos++;
		}
		else if (xPos>xDestination) {
			xPos--;
		}
		
		if (yPos<yDestination) {
			yPos++;
		}
		else if (yPos>yDestination) {
			yPos--;
		}
		
		if (xPos==xDestination && yPos==yDestination) {
			
		}
		return;
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		g.setColor(Color.BLUE);
		g.fillRect(xPos, yPos, WIDTH, HEIGHT);
		
		g.setColor(Color.WHITE);
		g.drawString("Landlord", WIDTH, HEIGHT);
	}

	// Animation Actions
	
	public void walkToDesk() {
		// set destination equal to office locations
		xDestination = deskX;
		yDestination = deskY;
	}
	
	public void leaveOffice() {
		// set destination to door to exit
	}
	
	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return isPresent;
	}
	
	public void setPresent(boolean bool) {
		isPresent = bool;
	}
	
	public void setDesk(int x, int y) {
		deskX = x;
		deskY = y;
	}

}
