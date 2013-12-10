package cs201.gui.roles.market;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;

import cs201.gui.ArtManager;
import cs201.gui.Gui;
import cs201.gui.astar.AStarNode;
import cs201.gui.astar.AStarTraversal;
import cs201.gui.astar.Position;
import cs201.gui.structures.market.MarketAnimationPanel;
import cs201.helper.Constants;
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
	
	private boolean hasCar = false;
	private boolean movingCarIn = false;
	private boolean movingCarOut = false;
	private boolean gettingShelfItem = false;
	private int carX = 0;
	private int carY = 0;
	String moveDir = "Market_Employee_Down";
	
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
		xPos = -40;
		yPos = -40;
		isPresent = false;
		animPanel = a;
		
		// Create a new A* Traversal object and connect to our animation panel's grid
		Semaphore[][] grid = animPanel.getAStarGrid();
		aStar = new AStarTraversal(grid);
		
		// Set the start position
//		currentPosition = new Position(startX, startY);
		currentPosition = new Position(1, 1);
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
	
	public void setIconDirection(String direction) {
		moveDir = direction;
	}

	public void updatePosition() {
		
		if (movingCarIn) {
			carX = xPos + 50;
			carY = yPos;
			moveDir = "Market_Employee_Left";
		}
		
		if (movingCarOut) {
			carX++;
			if (carX > 500) {
				movingCarOut = false;
				hasCar = false;
			}
		}
		
		if (animating && ultimateDestination != null && xPos == ultimateDestination.getXInPixels() && yPos == ultimateDestination.getYInPixels()) {
			if (role != null) {
				role.animationFinished();
			}
			animating = false;
		}
		
		if (nextPosition != null) {
			if (xPos < nextPosition.getXInPixels()) {
				xPos++;
				moveDir = "Market_Employee_Right";
			}
			else if (xPos > nextPosition.getXInPixels()) {
				xPos--;
				moveDir = "Market_Employee_Left";
			}
			if (yPos < nextPosition.getYInPixels()) {
				yPos++;
				moveDir = "Market_Employee_Down";
			}
			else if (yPos > nextPosition.getYInPixels()) {
				yPos--;
				moveDir = "Market_Employee_Up";
			}
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
			} else {
				if (gettingShelfItem) {
					moveDir = "Market_Employee_Left";
				} else {
					moveDir = "Market_Employee_Down";
				}
			}
		}
	}

	public void draw(Graphics2D g) {
		if (Constants.DEBUG_MODE) {
			// Draw the employee icon
			g.setColor(Color.orange);
			g.fillRect(xPos, yPos, EMPLOYEE_SIZE, EMPLOYEE_SIZE);
			
			// Draw the employee text
			g.setColor(Color.black);
			g.drawString("Market Employee", xPos, yPos - 3);
		} else {
			// Draw the employee
			g.drawImage(ArtManager.getImage(moveDir), xPos, yPos, EMPLOYEE_SIZE, EMPLOYEE_SIZE, null);
		}
		
		// Draw the car, if the employee is currently bringing one out
		if (hasCar) {
			g.setColor(Color.red);
			g.fillRect(carX, carY, 150, 35);
		}
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
		gettingShelfItem = true;
	}
	
	public void doWalkToCarLot() {
		guiMoveFromCurrentPositionTo(new Position(20, 13));
		gettingShelfItem = false;
	}
	
	public void setHasCar(boolean car) {
		hasCar = car;
	}
	
	public void setMovingCarIn(boolean moving) {
		movingCarIn = moving;
	}
	
	public void setMovingCarOut(boolean moving) {
		movingCarOut = moving;
	}
	
	public void doBringCarOut() {
		guiMoveFromCurrentPositionTo(new Position(11, 13));
		gettingShelfItem = false;
	}
	
	public void doGoToManager() {
		gettingShelfItem = false;
		guiMoveFromCurrentPositionTo(new Position(13, 14));
	}
	
	public void doEnterMarket() {
		gettingShelfItem = false;
		doGoHome();
	}
	
	public void doGoHome() {
		gettingShelfItem = false;
		guiMoveFromCurrentPositionTo(homePosition);
	}
	
	public void doLeaveMarket() {
		animating = true;
		gettingShelfItem = false;
		guiMoveFromCurrentPositionTo(new Position(1, 1));
		ultimateDestination = new Position(1, 1);
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
