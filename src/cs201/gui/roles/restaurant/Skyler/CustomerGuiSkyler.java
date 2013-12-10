package cs201.gui.roles.restaurant.Skyler;

import java.awt.*;

import cs201.gui.ArtManager;
import cs201.gui.Gui;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelSkyler;
import cs201.interfaces.roles.restaurant.Skyler.CustomerSkyler;
import cs201.roles.restaurantRoles.Skyler.RestaurantCustomerRoleSkyler;

public class CustomerGuiSkyler implements Gui{

	private CustomerSkyler agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;
	private int tableNumber;

	//private HostAgent host;
	RestaurantAnimationPanelSkyler animationPanel;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, GoPay, LeaveRestaurant};
	private Command command=Command.noCommand;
	private Font font = new Font("Sans-serif", Font.PLAIN, 10);

	public static final int xTable = 150;
	public static final int yTable = 250;
	
	String dir = new String();

	public CustomerGuiSkyler(CustomerSkyler c, RestaurantAnimationPanelSkyler panel){ //HostAgent m) {
		agent = c;
		dir = "Default_Walker_Down";
		//maitreD = m;
		this.animationPanel = panel;
	}

	public void updatePosition() {
		if (xPos < xDestination) {
			xPos++;
			dir = "Default_Walker_Right";
		}
		else if (xPos > xDestination) {
			xPos--;
			dir = "Default_Walker_Left";
		}

		if (yPos < yDestination) {
			yPos++;
			dir = "Default_Walker_Down";
		}
		else if (yPos > yDestination) {
			yPos--;
			dir = "Default_Walker_Up";
		}

		if (xPos == xDestination && yPos == yDestination) {
			dir = "Default_Walker_Down";
			if (command==Command.GoToSeat) agent.msgAnimationFinishedGoToSeat();
			else if (command==Command.GoPay) {
				agent.msgAnimationFinishedPay();
			}
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
				//animationPanel.setCustomerEnabled(agent);
			}
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		/*g.setColor(Color.BLACK);
		g.fillRect(xPos, yPos, 20, 20);
		g.setColor(Color.GREEN);
		g.fillRect(xPos+1, yPos+1, 18, 18);*/
		
		g.drawImage(ArtManager.getImage(dir), xPos, yPos, 17, 22, null);
		
		if(agent.getState()==RestaurantCustomerRoleSkyler.AgentState.Eating) {
			g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString(agent.getChoice().substring(0, 2), xPos+2, yPos+40);
		}
		
		}

	public boolean isPresent() {
		return isPresent;
	}
	
	public void setHungry() {
		isHungry = true;
		agent.gotHungry();
		setPresent(true);
		xPos = (int)(Math.random()*70);
		yPos = (int)(Math.random()*50);
		xDestination = xPos;
		yDestination = yPos;
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void setTable(int tableNum) {
		tableNumber = tableNum;
	}
	
	public void DoGoToSeat() {//later you will map seatnumber to table coordinates.
		xDestination = xTable + (100 * (tableNumber-1));
		yDestination = yTable;
		command = Command.GoToSeat;
	}
	
	public void DoGoPay() {
		xDestination = 200;
		yDestination = -19;
		command = Command.GoPay;
	}

	public void DoExitRestaurant() {
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveRestaurant;
	}
}
