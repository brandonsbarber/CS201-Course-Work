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
	
	public LandlordGui() {
		this(null);
	}
	
	public LandlordGui(LandlordRole newRole) {
		xPos = startX;
		yPos = startY;
		role = newRole;
		isPresent = true;
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
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

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return isPresent;
	}

}
