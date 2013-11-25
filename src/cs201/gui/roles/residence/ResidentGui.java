package cs201.gui.roles.residence;

import java.awt.Color;
import java.awt.Graphics2D;

import cs201.gui.Gui;
import cs201.roles.housingRoles.ResidentRole;

public class ResidentGui implements Gui {

	private ResidentRole role = null;
	private boolean isPresent;
	
	private final int WIDTH = 20, HEIGHT = 20;
	private final int startX = 100, startY = 100;
	
	private int xPos;
	private int yPos;
	private int xDestination;
	private int yDestination;
	
	
	public ResidentGui() {
		this(null);
	}
	
	public ResidentGui(ResidentRole newRole) {
		xPos = startX;
		yPos = startY;
		xDestination = xPos;
		yDestination = yPos;
		role = newRole;
		isPresent = true;
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
		g.setColor(Color.ORANGE);
		g.fillRect(xPos, yPos, WIDTH, HEIGHT);
		
		g.setColor(Color.WHITE);
		g.drawString("Resident", WIDTH, HEIGHT);
		// TODO Auto-generated method stub

	}
	
	public void walkToFridge() {
		//set destination to fridge from residence layout
	}
	
	public void goToBed() {
		//animation to walk to the bed
		//animation to get in bed
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return isPresent;
	}
	
	public void setPresent() {
		if (isPresent) {
			isPresent=false;
		}
		else {
			isPresent=true;
		}
	}

}
