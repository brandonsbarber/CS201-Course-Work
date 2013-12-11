package cs201.gui.roles.residence;

import java.awt.Color;
import java.awt.Graphics2D;

import cs201.gui.ArtManager;
import cs201.gui.Gui;
import cs201.roles.housingRoles.LandlordRole;

public class LandlordGui implements Gui {

	private LandlordRole role = null;
	private boolean isPresent;
	private boolean animating;
	
	private final int WIDTH = 20, HEIGHT = 20;
	private final int startX = 0, startY = 250;
	
	private int xPos;
	private int yPos;
	private int xDestination;
	private int yDestination;
	private int exitX = -20;
	private int exitY = 250;
	
	private int chairX;
	private int chairY;
	
	private String dir = new String();
	private String destinationDir = new String();
	
	
	public LandlordGui() {
		this(null);
	}
	
	public LandlordGui(LandlordRole newRole) {
		xPos = startX;
		yPos = startY;
		xDestination = xPos;
		yDestination = yPos;
		dir = "Skyler_Waiter_Down";
		destinationDir = "Skyler_Waiter_Down";
		
		role = newRole;
		isPresent = false;
		animating = false;
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		if (xPos<xDestination) {
			xPos++;
			dir = "Skyler_Waiter_Right";
		}
		else if (xPos>xDestination) {
			xPos--;
			dir = "Skyler_Waiter_Left";
		}
		
		if (yPos<yDestination) {
			yPos++;
			dir = "Skyler_Waiter_Up";
		}
		else if (yPos>yDestination) {
			yPos--;
			dir = "Skyler_Waiter_Down";
		}
		
		if (xPos==xDestination && yPos==yDestination && animating==true) {
			dir = destinationDir;
			role.msgAnimationDone();
			animating = false;
		}
		return;
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		/*g.setColor(Color.BLUE);
		g.fillRect(xPos, yPos, WIDTH, HEIGHT);*/
		
		g.drawImage(ArtManager.getImage(dir), xPos, yPos, 16, 24, null);
		
		g.setColor(Color.WHITE);
		g.drawString("Landlord", xPos, yPos);
	}

	// Animation Actions
	
	public void sitAtDesk() {
		// set destination equal to office locations
		xDestination = chairX;
		yDestination = chairY;
		animating = true;
		destinationDir = "Skyler_Waiter_Up";
	}
	
	public void leaveOffice() {
		// set destination to door to exit
		xDestination = exitX;
		yDestination = exitY;
		animating = true;
		destinationDir = "Skyler_Waiter_Left";
	}
	
	public void enter() {
		xDestination = 0;
		yDestination = startY;
		animating = true;
		destinationDir = "Skyler_Waiter_Right";
	}
	
	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return isPresent;
	}
	
	public void setPresent(boolean bool) {
		isPresent = bool;
	}
	
	public void setChair(int x, int y) {
		chairX = x;
		chairY = y;
	}

}
