package cs201.gui.roles.restaurant.Matt;

import java.awt.Color;
import java.awt.Graphics2D;

import cs201.gui.Gui;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelMatt;
import cs201.gui.structures.restaurant.RestaurantConfigPanelMatt;
import cs201.roles.restaurantRoles.Matt.RestaurantCustomerRoleMatt;

public class CustomerGuiMatt implements Gui {

	private RestaurantCustomerRoleMatt agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;
	
	private final int CUSTOMERSIZE = (RestaurantAnimationPanelMatt.WINDOWX < RestaurantAnimationPanelMatt.WINDOWY) ? (int)(RestaurantAnimationPanelMatt.WINDOWX * .04f) : (int)(RestaurantAnimationPanelMatt.WINDOWY * .04f);
	private final int RESTAURANTENTRANCE = -(int)(RestaurantAnimationPanelMatt.WINDOWX * .08f);
	private final int RESTAURANTEXIT = -(int)(RestaurantAnimationPanelMatt.WINDOWX * .08f);
	private final int DEFAULTWAITINGAREA = 0;
	
	private int waitingAreaX = DEFAULTWAITINGAREA;
	private int waitingAreaY = DEFAULTWAITINGAREA;

	RestaurantConfigPanelMatt panel;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private boolean animating = false, hasPosition = false;
	
	private enum guiState { none, goingToRestaurant, goingToWaitingArea, goingToTable, goingToCashier, goingToExit };
	private guiState state;
	
	private String eating;

	public CustomerGuiMatt(RestaurantCustomerRoleMatt c, RestaurantConfigPanelMatt r) {
		agent = c;
		xPos = RESTAURANTENTRANCE;
		yPos = RESTAURANTENTRANCE;
		xDestination = RESTAURANTEXIT;
		yDestination = RESTAURANTEXIT;
		this.panel = r;
		state = guiState.none;
		eating = "";
	}

	public void updatePosition() {			
		if (animating && hasPosition) {
			if (xPos < xDestination)
				xPos++;
			else if (xPos > xDestination)
				xPos--;
	
			if (yPos < yDestination)
				yPos++;
			else if (yPos > yDestination)
				yPos--;
			
			if (xPos == xDestination && yPos == yDestination) {
				animating = false;
				hasPosition = false;
				if (state == guiState.goingToExit) {
					resetHungry();
				}
				agent.DoneAnimating();
			}
		} else {
			return;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, CUSTOMERSIZE, CUSTOMERSIZE);
		
		g.setColor(Color.black);
		if (eating.length() > 0) {
			g.drawString(eating, xPos, yPos);
		}
	}

	public boolean isPresent() {
		return isPresent;
	}
	
	public void resetHungry() {
		isHungry = false;
		//panel.setPersonEnabled(agent);
		state = guiState.none;
	}
	
	public void setHungry() {
		isHungry = true;
		agent.msgIsHungry();
		setPresent(true);
	}
	
	public void setMessage(String what) {
		String temp = what;
		boolean endsWithQuestion = false;
		if (temp.endsWith("?")) {
			temp = temp.substring(0, temp.length() - 1);
			endsWithQuestion = true;
		}
		
		switch (temp) {
		case "Steak": {
			temp = "Stk";
			break;
		}
		case "Pasta": {
			temp = "Pas";
			break;
		}
		case "Pizza": {
			temp = "Piz";
			break;
		}
		case "Ice Cream": {
			temp = "Ice";
			break;
		}
		case "Chicken": {
			temp = "Ckn";
			break;
		}
		case "Salad": {
			temp = "Sal";
			break;
		}
		default: break;
		}
		
		if (endsWithQuestion) {
			temp += "?";
		}
		eating = temp;
	}
	
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public void Animate() {
		animating = true;
	}

	public void DoGoToSeat(int x, int y) {
		xDestination = x;
		yDestination = y;
		state = guiState.goingToTable;
		hasPosition = true;
	}

	public void DoExitRestaurant() {
		xDestination = RESTAURANTEXIT;
		yDestination = RESTAURANTEXIT;
		state = guiState.goingToExit;
		eating = "";
		hasPosition = true;
		animating = true;
	}
	
	public void DoGoToRestaurant() {
		xDestination = DEFAULTWAITINGAREA;
		yDestination = DEFAULTWAITINGAREA;
		state = guiState.goingToRestaurant;
		eating = "";
		animating = true;
		hasPosition = true;
	}
	
	public void DoGoToWaitingArea() {
		xDestination = waitingAreaX;
		yDestination = waitingAreaY;
		state = guiState.goingToWaitingArea;
		eating = "";
		hasPosition = true;
	}
	
	public void DoGoToCashier() {
		xDestination = cs201.gui.roles.restaurant.Matt.CashierGuiMatt.CASHIERX;
		yDestination = cs201.gui.roles.restaurant.Matt.CashierGuiMatt.CASHIERY + cs201.gui.roles.restaurant.Matt.CashierGuiMatt.CASHIERSIZE;
		state = guiState.goingToCashier;
		eating = "$$";
		hasPosition = true;
		animating = true;
	}
	
	public void SetWaitingArea(int x, int y) {
		waitingAreaX = x;
		waitingAreaY = y;
		hasPosition = true;
	}
	
	public int X() {
		return xPos;
	}
	
	public int Y() {
		return yPos;
	}
	
	public int WaitingX() {
		return waitingAreaX;
	}
	
	public int WaitingY() {
		return waitingAreaY;
	}
}
