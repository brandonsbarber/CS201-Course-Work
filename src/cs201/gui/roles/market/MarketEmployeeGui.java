package cs201.gui.roles.market;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;

import cs201.gui.Gui;
import cs201.gui.astar.AStarNode;
import cs201.gui.astar.AStarTraversal;
import cs201.gui.astar.Position;
import cs201.gui.structures.market.MarketAnimationPanel;
import cs201.roles.marketRoles.MarketEmployeeRole;

public class MarketEmployeeGui implements Gui {

	private MarketEmployeeRole role = null;
	private boolean isPresent = false;
	
	public static final int EMPLOYEE_SIZE = 20;
	public static final int EMPLOYEE_HOME_X = 50;
	public static final int EMPLOYEE_HOME_Y = 25;

	private int xPos, yPos, xDestination, yDestination;
	
	private class Destination {
		int x, y;
		public Destination(int x_init, int y_init) {
			x = x_init;
			y = y_init;
		}
	}
	
	Queue<Destination> destinationQueue = new LinkedList<Destination>();
	Destination currentDestination = null;
	
	private enum IsleMovementState {MOVE_OUT_OF_ISLE, MOVE_TO_ISLE, MOVE_TO_SHELF};
	
	private int isleDestination = -1;
	private boolean goingToManager = false;
	private boolean goingHome = false;
	
	MarketAnimationPanel animPanel;
	
    /*
     * ********** A* IMPLEMENTATION **********
     */
	
	AStarTraversal aStar = null;
	Position currentPosition = null;
	
	public MarketEmployeeGui(MarketAnimationPanel a) {
		this(null, a);
	}

	public MarketEmployeeGui(MarketEmployeeRole c, MarketAnimationPanel a) {
		role = c;
		xPos = EMPLOYEE_HOME_X;
		yPos = EMPLOYEE_HOME_Y;
		isPresent = true;
		animPanel = a;
		
		// Create a new A* Traversal object and connect to our animation panel's grid
		Semaphore[][] grid = animPanel.getAStarGrid();
		aStar = new AStarTraversal(grid);
		
		// Set the start position
		currentPosition = new Position(1, 1);
		currentPosition.moveInto(aStar.getGrid());
	}
	
	public void guiMoveFromCurrentPostionTo(Position to){

		AStarNode aStarNode = (AStarNode)aStar.generalSearch(currentPosition, to);
		List<Position> path = aStarNode.getPath();
		Boolean firstStep   = true;
		Boolean gotPermit   = true;

		for (Position tmpPath: path) {
		    //The first node in the path is the current node. So skip it.
		    if (firstStep) {
		    	firstStep   = false;
		    	continue;
		    }

		    //Try and get lock for the next step.
		    int attempts    = 1;
		    gotPermit       = new Position(tmpPath.getX(), tmpPath.getY()).moveInto(aStar.getGrid());

		    //Did not get lock. Lets make n attempts.
		    while (!gotPermit && attempts < 3) {
		    	//System.out.println("[Gaut] " + guiWaiter.getName() + " got NO permit for " + tmpPath.toString() + " on attempt " + attempts);

		    	//Wait for 1sec and try again to get lock.
		    	try { Thread.sleep(1000); }
		    	catch (Exception e){}

		    	gotPermit   = new Position(tmpPath.getX(), tmpPath.getY()).moveInto(aStar.getGrid());
		    	attempts ++;
		    }

		    //Did not get lock after trying n attempts. So recalculating path.            
		    if (!gotPermit) {
		    	//System.out.println("[Gaut] " + guiWaiter.getName() + " No Luck even after " + attempts + " attempts! Lets recalculate");
		    	guiMoveFromCurrentPostionTo(to);
		    	break;
		    }

		    //Got the required lock. Lets move.
		    //System.out.println("[Gaut] " + guiWaiter.getName() + " got permit for " + tmpPath.toString());
		    currentPosition.release(aStar.getGrid());
		    currentPosition = new Position(tmpPath.getX(), tmpPath.getY ());
		    
		    Destination nextDestination = new Destination((currentPosition.getX() - 1) * 25, (currentPosition.getY() - 1) * 25);
		    destinationQueue.add(nextDestination);
		}
	}

	public void updatePosition() {
		
		/*
		if (goingToManager) {
			if (yPos < (MarketAnimationPanel.FIRST_SHELF_Y + MarketAnimationPanel.SHELF_HEIGHT + EMPLOYEE_SIZE)) {
				yPos++;
				return;
			}
		}
		if (isleNumber(xPos) != isleDestination && isleDestination != -1) {
			if (yPos > MarketAnimationPanel.FIRST_SHELF_Y - EMPLOYEE_SIZE - 10) {
				yPos--;
			} else {
				if (isleDestination > isleNumber(xPos)) xPos++; else xPos--;
			}
		}
		*/
		
		if (currentDestination != null) {
			if (xPos < currentDestination.x)
				xPos++;
			else if (xPos > currentDestination.x)
				xPos--;
			
			if (yPos < currentDestination.y)
				yPos++;
			else if (yPos > currentDestination.y)
				yPos--;
			
			if (xPos == currentDestination.x && yPos == currentDestination.y)
				currentDestination = null;
		} else {
			if (destinationQueue.size() != 0) {
				currentDestination = destinationQueue.poll();
			}
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.orange);
		g.fillRect(xPos, yPos, EMPLOYEE_SIZE, EMPLOYEE_SIZE);
		
		g.setColor(Color.black);
		g.drawString("Market Employee", xPos, yPos - 3);
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
	
	public void doAnimate() {
		xDestination = 200;
		yDestination = 200;
	}
	
	public void doGoToItemOnShelf(int isleNumber, int itemNumber) {
		xDestination = MarketAnimationPanel.FIRST_SHELF_X + MarketAnimationPanel.SHELF_WIDTH + 5 + MarketAnimationPanel.SHELF_SPACING * isleNumber;
		yDestination = MarketAnimationPanel.FIRST_SHELF_Y + (int)(MarketAnimationPanel.SHELF_HEIGHT * (itemNumber / 10.0));
		isleDestination = isleNumber;
	}
	
	public void doGoToManager() {
		xDestination = MarketAnimationPanel.FRONT_DESK_X - EMPLOYEE_SIZE - 15;
		yDestination = MarketAnimationPanel.FRONT_DESK_Y;
		isleDestination = -1;
		goingToManager = true;
	}
	
	public void doGoHome() {
		xDestination = EMPLOYEE_HOME_X;
		yDestination = EMPLOYEE_HOME_Y;
		
	}
	
	private int isleNumber(int x) {
		int offsetXPos = x - MarketAnimationPanel.FIRST_SHELF_X;
		int isleWidth = MarketAnimationPanel.SHELF_SPACING;
		int isleNumber = offsetXPos /  isleWidth;
		return isleNumber;
	}
	
}
