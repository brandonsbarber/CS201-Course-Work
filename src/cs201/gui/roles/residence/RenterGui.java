package cs201.gui.roles.residence;

import java.awt.Color;
import java.awt.Graphics2D;

import cs201.gui.Gui;
import cs201.roles.housingRoles.LandlordRole;
import cs201.roles.housingRoles.RenterRole;

public class RenterGui implements Gui {

	private RenterRole role = null;
	private boolean isPresent;
	
	private final int WIDTH = 20, HEIGHT = 20;
	private final int startX = 100, startY = 100;
	
	private int xPos;
	private int yPos;
	private int xDestination;
	private int yDestination;
	
	public RenterGui() {
		this(null);
	}
	
	public RenterGui(RenterRole newRole) {
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
		g.setColor(Color.ORANGE);
		g.fillRect(xPos, yPos, WIDTH, HEIGHT);
		
		g.setColor(Color.WHITE);
		g.drawString("Renter", WIDTH, HEIGHT);
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
