package cs201.gui.roles.restaurant.Ben;

import java.awt.Color;
import java.awt.Graphics2D;

import cs201.gui.ArtManager;
import cs201.gui.Gui;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelBen;
import cs201.helper.Constants;
import cs201.interfaces.roles.restaurant.Ben.CustomerBen;

public class CustomerGuiBen implements Gui{

	private CustomerBen agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;

	//private HostAgent host;
	RestaurantAnimationPanelBen animationPanel;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;

	public static final int xTable = 100;
	public static final int yTable = 200;
	public static final int tablePad = 20;
	public static final int tableWidth = 50;
	public static final int tableHeight = 50;
	
	public static final int customerWidth  = 20;
	public static final int customerHeight = 20;
	
	private boolean hasIcon = false;
	private String iconText = "";
	private boolean animating = false;
	private String moveDir = "Default_Walker_Down";
	
	public CustomerGuiBen(CustomerBen c, RestaurantAnimationPanelBen ap){ //HostAgent m) {
		agent = c;
		xPos = -40;
		yPos = -40;
		
		// Find a spot for us to wait in
		int index = -1;
		for (int i = 0; i < ap.customerWaitingSpots.size(); i++) {
			if (ap.customerWaitingSpots.get(i) == null) {
				ap.customerWaitingSpots.set(i, this);
				index = i + 1;
				break;
			}
		}
		if (index == -1) {
			ap.customerWaitingSpots.add(this);
			index = ap.customerWaitingSpots.size();
		}
		
		xDestination = 20 + ((index - 1) * 25);
		yDestination = 5;
		//maitreD = m;
		this.animationPanel = ap;
	}

	public void updatePosition() {
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;

		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToSeat) agent.msgAnimationFinishedGoToSeat();
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
				//gui.setCustomerEnabled(agent);
			}
			command=Command.noCommand;
			if (animating) {
				animating = false;
			}
		}
	}

	public void draw(Graphics2D g) {
		if (Constants.DEBUG_MODE) {
			// Draw the green customer rectangle
			g.setColor(Color.GREEN);
			g.fillRect(xPos, yPos, customerWidth, customerHeight);
			
			// Draw his icon, if he has one
			if (hasIcon) {
				g.setColor(Color.BLACK);
				g.drawString(iconText, xPos, yPos + 15);
			}
			
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
				moveDir = "Default_Walker_Down";
			}
			
			g.drawImage(ArtManager.getImage(moveDir), xPos, yPos, null);
			
			// Draw his icon, if he has one
			if (hasIcon) {
				g.setColor(Color.BLACK);
				g.drawString(iconText, xPos + 15, yPos - 15);
			}
		}
		
	}

	public boolean isPresent() {
		return isPresent;
	}
	
	public void setHungry() {
		isHungry = true;
		agent.gotHungry();
		setPresent(true);
	}
	
	public boolean isHungry() {
		return isHungry;
	}
	
	public void setIconText(String text) {
		hasIcon = true;
		iconText = text;
	}
	
	public void removeIcon() {
		hasIcon = false;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void DoGoToSeat(int seatnumber) {
		animating = true;
		
		xDestination = xTable + (tableWidth + tablePad) * (seatnumber - 1) + (tableWidth / 2 - 10);
		yDestination = yTable - 20;
		command = Command.GoToSeat;
		
		// Set our waiting position to null
		for (int i = 0; i < animationPanel.customerWaitingSpots.size(); i++) {
			if (animationPanel.customerWaitingSpots.get(i) == this)
				animationPanel.customerWaitingSpots.set(i, null);
		}
	}

	public void DoExitRestaurant() {
		animating = true;
		
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveRestaurant;
	}
}
