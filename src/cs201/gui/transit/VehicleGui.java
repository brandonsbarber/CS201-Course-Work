package cs201.gui.transit;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import javax.swing.JOptionPane;

import cs201.agents.transit.VehicleAgent;
import cs201.gui.CityPanel;
import cs201.gui.Gui;
import cs201.helper.transit.MovementDirection;
import cs201.structures.Structure;

/**
 * 
 * @author Brandon
 *
 */
public abstract class VehicleGui implements Gui
{
	class MyPoint extends Point
	{
		MyPoint prev;
		MovementDirection move;
		
		public MyPoint(int i, int j, MyPoint previous,MovementDirection moveDir)
		{
			super(i,j);
			prev = previous;
			move = moveDir;
		}
		
		public boolean equals(MyPoint p)
		{
			return p.x == x && p.y == y;
		}
	}
	
	private VehicleAgent vehicle;
	
	private Structure destination;
	private int destX,destY;
	
	private int x, y;
	
	private boolean fired;
	
	private boolean present;
	
	private CityPanel city;

	protected MovementDirection currentDirection;

	private Stack<MovementDirection> moves;

	private boolean pathfinding;
	
	/**
	 * Creates a vehicle gui
	 * @param vehicle the vehicle who holds the gui
	 * @param city the city which contains the gui
	 */
	public VehicleGui(VehicleAgent vehicle,CityPanel city)
	{
		this(vehicle,city,50,50);
	}
	
	/**
	 * Creates a vehicle gui
	 * @param vehicle the vehicle who holds the gui
	 * @param city the city which contains the gui
	 * @param x the initial x
	 * @param y the initial y
	 */
	public VehicleGui(VehicleAgent vehicle,CityPanel city, int x, int y)
	{
		this.setVehicle(vehicle);
		this.setX(x);
		this.setY(y);
		this.city = city;
		destX = x;
		destY = y;
		fired = true;
		present = false;
		currentDirection = MovementDirection.None;
		moves = new Stack<MovementDirection>();
	}
	
	/**
	 * Sets whether the gui is present in the scene and should be rendered
	 * @param present whether it is present
	 */
	public void setPresent(boolean present)
	{
		this.present = present;
	}
	
	/**
	 * Signals the GUI to go to a location
	 * @param structure the structure to go to
	 */
	public void doGoToLocation(Structure structure)
	{
		x = (int)vehicle.currentLocation.getParkingLocation().getX();
		y = (int)vehicle.currentLocation.getParkingLocation().getY();
		destination = structure;
		destX = (int)destination.getParkingLocation().getX();
		destY = (int)destination.getParkingLocation().getY();
		fired = false;
		present = true;
		
		findPath();
	}
	
	/*
	 * Performs BFS to find best path
	 */
	private void findPath()
	{
		pathfinding = true;
		MovementDirection[][] map = city.getDrivingMap();
		
		Queue<MyPoint> location = new LinkedList<MyPoint>();
		
		ArrayList<MyPoint> visitedPoints = new ArrayList<MyPoint>();
		
		MyPoint startLoc = new MyPoint(x/CityPanel.GRID_SIZE,y/CityPanel.GRID_SIZE,null,map[y/CityPanel.GRID_SIZE][x/CityPanel.GRID_SIZE]);
		MyPoint destination = new MyPoint(destX/CityPanel.GRID_SIZE,destY/CityPanel.GRID_SIZE,null,map[y/CityPanel.GRID_SIZE][x/CityPanel.GRID_SIZE]);
		
		location.add(startLoc);
		visitedPoints.add(startLoc);
		
		//run a BFS
		while(!location.isEmpty())
		{
			MyPoint p = location.remove();
			if(p.equals(destination))
			{
				MyPoint head = p;
				while(head != null)
				{
					moves.add(head.move);
					head = head.prev;
				}
				break;
			}
			MovementDirection currentDirection = map[p.y][p.x];
			
			if(currentDirection == MovementDirection.Turn)
			{
				List<MovementDirection> validDirections = getJunctionDirections(map,p.x,p.y);
				for(MovementDirection dir : validDirections)
				{
					MyPoint nextPoint = getPointFromDirection(p,dir);
					if(!visitedPoints.contains(nextPoint) && isValidPoint(map,nextPoint))
					{
						visitedPoints.add(nextPoint);
						location.add(nextPoint);
					}
				}
			}
			else if(currentDirection == MovementDirection.None)
			{
				//Find an adjacent sidewalk piece
				MyPoint point = getPointFromDirection(p,MovementDirection.Down);
				if(!visitedPoints.contains(point) && isValidPoint(map,new MyPoint(point.x,point.y,null,null)) && map[point.y][point.x].isValid())
				{
					visitedPoints.add(point);
					location.add(point);
					continue;
				}
				point = getPointFromDirection(p,MovementDirection.Up);
				if(!visitedPoints.contains(point) && isValidPoint(map,new MyPoint(point.x,point.y,null,null)) && map[point.y][point.x].isValid())
				{
					visitedPoints.add(point);
					location.add(point);
					continue;
				}
				point = getPointFromDirection(p,MovementDirection.Right);
				if(!visitedPoints.contains(point) && isValidPoint(map,new MyPoint(point.x,point.y,null,null)) && map[point.y][point.x].isValid())
				{
					visitedPoints.add(point);
					location.add(point);
					continue;
				}
				point = getPointFromDirection(p,MovementDirection.Left);
				if(!visitedPoints.contains(point) && isValidPoint(map,new MyPoint(point.x,point.y,null,null)) && map[point.y][point.x].isValid())
				{
					visitedPoints.add(point);
					location.add(point);
					continue;
				}
			}
			else
			{
				MyPoint nextPoint = getPointFromDirection(p,currentDirection);
				if(!visitedPoints.contains(nextPoint) && isValidPoint(map,nextPoint))
				{
					visitedPoints.add(nextPoint);
					location.add(nextPoint);
				}
			}
		}
		
		if(moves.isEmpty())
		{
			JOptionPane.showMessageDialog(null,": I cannot find a path.");
		}
		else
		{
			//clear first element
			moves.pop();
		}
		pathfinding = false;
	}

	/*
	 * Helper method for getting junction
	 */
	private List<MovementDirection> getJunctionDirections(MovementDirection[][] map,int x2, int y2)
	{
		List<MovementDirection> validDirections = new ArrayList<MovementDirection>();
		
		int leftX = x2-1;
		int rightX = x2+1;
		int upY = y2 - 1;
		int downY = y2 + 1;
		
		if(inBounds(map,leftX,y2) && getDirection(map,leftX,y2) == MovementDirection.Left)
		{
			validDirections.add(MovementDirection.Left);
		}
		if(inBounds(map,rightX,y2) && getDirection(map,rightX,y2) == MovementDirection.Right)
		{
			validDirections.add(MovementDirection.Right);
		}
		if(inBounds(map,x2,upY) && getDirection(map,x2,upY) == MovementDirection.Up)
		{
			validDirections.add(MovementDirection.Up);
		}
		if(inBounds(map,x2,downY) && getDirection(map,x2,downY) == MovementDirection.Down)
		{
			validDirections.add(MovementDirection.Down);
		}
		return validDirections;
	}

	/*
	 * Helper method with swapping for better readability
	 */
	private MovementDirection getDirection(MovementDirection[][] map, int x, int y)
	{
		return map[y][x];
	}
	
	/*
	 * Helper method for bounds
	 */
	private boolean inBounds(MovementDirection[][] map, int x2, int y2)
	{
		return y2 < map.length && y2 >= 0 && x2 >= 0 && x2 < map[y2].length;
	}

	/*
	 * Helper method for validity
	 */
	private boolean isValidPoint(MovementDirection[][] map, MyPoint nextPoint)
	{
		return nextPoint.x >= 0 && nextPoint.x < map[0].length && nextPoint.y >= 0 && nextPoint.y < map.length;
	}

	/*
	 * Helper method for extending line of point direction
	 */
	private MyPoint getPointFromDirection(MyPoint p, MovementDirection dir)
	{
		switch(dir)
		{
		case Right:
			return new MyPoint(p.x+1,p.y,p,dir);
		case Up:
			return new MyPoint(p.x,p.y-1,p,dir);
		case Down:
			return new MyPoint(p.x,p.y+1,p,dir);
		case Left:
			return new MyPoint(p.x-1,p.y,p,dir);
		default:
			return new MyPoint(p.x,p.y-1,p,MovementDirection.Up);
		
		}
	}
	
	/**
	 * Draws the GUI in the given graphcis object
	 * @param g the graphics object in which to draw
	 */
	public void draw(Graphics2D g)
	{
		drawBody(g);
		
		g.setColor(Color.BLACK);
		g.drawString(""+getVehicle().getClass().getSimpleName()+":"+getVehicle().getInstance(),getX(),getY());
	}
	
	/**
	 * To be extended in subclass
	 * @param g graphics object to render in
	 */
	public abstract void drawBody(Graphics2D g);

	/**
	 * Updates the position based on movement data
	 */
	@Override
	public void updatePosition()
	{
		if(!fired && !pathfinding)
		{
			if(x == destX && y == destY)
			{
				fired = true;
				vehicle.msgAnimationDestinationReached();
				currentDirection = MovementDirection.None;
				return;
			}
			switch(currentDirection)
			{
				case Right:
					x++;
					break;
				case Up:
					y--;
					break;
				case Down:
					y++;
					break;
				case Left:
					x--;
					break;
				default:
					break;
			}
			if(x % CityPanel.GRID_SIZE == 0 && y % CityPanel.GRID_SIZE == 0 && !moves.isEmpty())
			{
				currentDirection = moves.pop();
				return;
			}
			
		}
	}

	/**
	 * Gets whether the gui is present and should be rendered
	 */
	@Override
	public boolean isPresent()
	{
		return present;
	}

	/**
	 * Gets the current x
	 * @return the current x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the x position
	 * @param x the x position to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Gets the y position
	 * @return the current y position
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the y position
	 * @param y the y position to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Gets the vehicle holding the gui
	 * @return
	 */
	public VehicleAgent getVehicle() {
		return vehicle;
	}

	/**
	 * Sets the vehicle holding the gui
	 * @param vehicle
	 */
	public void setVehicle(VehicleAgent vehicle) {
		this.vehicle = vehicle;
	}
}
