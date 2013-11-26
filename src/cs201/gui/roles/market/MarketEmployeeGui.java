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
	
	Queue<Position> destinationQueue = new LinkedList<Position>();
	Queue<Position> positionQueue = new LinkedList<Position>();
	Position ultimateDestination = null;
	Position nextPosition = null;
	Position homePosition = null;
		
	private boolean animating = false;
	
	MarketAnimationPanel animPanel;
	
    /*
     * ********** A* IMPLEMENTATION **********
     */
	
	AStarTraversal aStar = null;
	Position currentPosition = null;
	
	public MarketEmployeeGui(MarketAnimationPanel a, int startX, int startY) {
		this(null, a, startX, startY);
	}

	public MarketEmployeeGui(MarketEmployeeRole c, MarketAnimationPanel a, int startX, int startY) {
		role = c;
		homePosition = new Position(startX, startY);
		xPos = homePosition.getXInPixels();
		yPos = homePosition.getYInPixels();
		isPresent = false;
		animPanel = a;
		
		// Create a new A* Traversal object and connect to our animation panel's grid
		Semaphore[][] grid = animPanel.getAStarGrid();
		aStar = new AStarTraversal(grid);
		
		// Set the start position
		currentPosition = new Position(startX, startY);
		//currentPosition.moveInto(grid);
	}
	
	private void calculatePathToNextDestination(Position nextDestination) {
		AStarNode aStarNode = (AStarNode)aStar.generalSearch(currentPosition, nextDestination);
		List<Position> path = aStarNode.getPath();
		Boolean firstStep   = true;
		Boolean gotPermit   = true;
		
		// The first node in the path is the current node. So skip it.
		path.remove(0);
		
		// Add the remaining positions to our position queue
		positionQueue.clear();
		for (Position position : path) {
			positionQueue.add(position);
		}
	}
	
	public boolean guiMoveFromCurrentPositionTo(Position to){
		if (to.open(aStar.getGrid())) {
			animating = true;
			ultimateDestination = to;
			calculatePathToNextDestination(ultimateDestination);
			return true;
		}
		return false;
	}

	public void updatePosition() {
		
		if (animating && xPos == ultimateDestination.getXInPixels() && yPos == ultimateDestination.getYInPixels()) {
			if (role != null) {
				role.animationFinished();
			}
			animating = false;
		}
		
		if (nextPosition != null) {
			if (xPos < nextPosition.getXInPixels())
				xPos++;
			else if (xPos > nextPosition.getXInPixels())
				xPos--;
			
			if (yPos < nextPosition.getYInPixels())
				yPos++;
			else if (yPos > nextPosition.getYInPixels())
				yPos--;
			
			if (xPos == nextPosition.getXInPixels() && yPos == nextPosition.getYInPixels()) {
				nextPosition = null;
			}
		} else {
			if (positionQueue.size() > 0) {
				nextPosition = positionQueue.poll();
				
				//Try and get lock for the next step.
			    //int attempts    = 1;
			    //boolean gotPermit = nextPosition.moveInto(aStar.getGrid());

			    //Did not get lock. Lets make n attempts.
			    /*
			    while (!gotPermit && attempts < 3) {
			    	//System.out.println("[Gaut] " + guiWaiter.getName() + " got NO permit for " + tmpPath.toString() + " on attempt " + attempts);

			    	//Wait for 1sec and try again to get lock.
			    	try { Thread.sleep(1000); }
			    	catch (Exception e){}

			    	gotPermit   = new Position(nextPosition.getX(), nextPosition.getY()).moveInto(aStar.getGrid());
			    	attempts ++;
			    }
			    */

			    //Did not get lock after trying n attempts. So recalculating path.            
			    //if (!gotPermit) {
			    	//System.out.println("[Gaut] " + guiWaiter.getName() + " No Luck even after " + attempts + " attempts! Lets recalculate");
			    	//positionQueue.clear();
			    	//guiMoveFromCurrentPositionTo(ultimateDestination);
			   // } else {
			    	//Got the required lock. Lets move.
			    	//System.out.println("[Gaut] " + guiWaiter.getName() + " got permit for " + tmpPath.toString());
			    	//currentPosition.release(aStar.getGrid());
			    	currentPosition = nextPosition;
			    //}
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
	
	public void doGoToItemOnShelf(int isleNumber, int itemNumber) {
		guiMoveFromCurrentPositionTo(new Position(4 + 3 * isleNumber, 5 + itemNumber));
	}
	
	public void doGoToManager() {
		for (int x = 10; x > 0; x--) {
			if (guiMoveFromCurrentPositionTo(new Position(x, 14)))
				return;
		}
		//guiMoveFromCurrentPositionTo(new Position(10, 14));
		// There isn't a spot, so just release the semaphore
		if (role != null)
			role.animationFinished();
	}
	
	public void doGoHome() {
		guiMoveFromCurrentPositionTo(homePosition);
	}
	
	public void doLeaveMarket() {
		animating = true;
		guiMoveFromCurrentPositionTo(new Position(1, 1));
		ultimateDestination = new Position(-1, -1);
		positionQueue.add(ultimateDestination);
	}
	
	private int isleNumber(int x) {
		int offsetXPos = x - MarketAnimationPanel.FIRST_SHELF_X;
		int isleWidth = MarketAnimationPanel.SHELF_SPACING;
		int isleNumber = offsetXPos /  isleWidth;
		return isleNumber;
	}
	
	public void setRole(MarketEmployeeRole r) {
		role = r;
	}
	
}
