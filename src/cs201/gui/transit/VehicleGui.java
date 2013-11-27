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
import cs201.gui.CityPanel.DrivingDirection;
import cs201.gui.CityPanel.DrivingDirection;
import cs201.gui.CityPanel.DrivingDirection;
import cs201.gui.CityPanel.WalkingDirection;
import cs201.gui.Gui;
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
		DrivingDirection move;
		
		public MyPoint(int i, int j, MyPoint previous,DrivingDirection moveDir)
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

	protected DrivingDirection currentDirection;

	private Stack<DrivingDirection> moves;

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
		currentDirection = DrivingDirection.None;
		moves = new Stack<DrivingDirection>();
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
		DrivingDirection[][] map = city.getDrivingMap();
		
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
			DrivingDirection currentDirection = map[p.y][p.x];
			
			if(currentDirection == DrivingDirection.Turn)
			{
				List<DrivingDirection> validDirections = getJunctionDirections(map,p.x,p.y);
				for(DrivingDirection dir : validDirections)
				{
					MyPoint nextPoint = getPointFromDirection(p,dir);
					if(!visitedPoints.contains(nextPoint) && isValidPoint(map,nextPoint))
					{
						visitedPoints.add(nextPoint);
						location.add(nextPoint);
					}
				}
			}
			else if(currentDirection == DrivingDirection.None)
			{
				//Find an adjacent sidewalk piece
				MyPoint point = getPointFromDirection(p,DrivingDirection.South);
				if(!visitedPoints.contains(point) && isValidPoint(map,new MyPoint(point.x,point.y,null,null)) && map[point.y][point.x].isValid())
				{
					visitedPoints.add(point);
					location.add(point);
					continue;
				}
				point = getPointFromDirection(p,DrivingDirection.North);
				if(!visitedPoints.contains(point) && isValidPoint(map,new MyPoint(point.x,point.y,null,null)) && map[point.y][point.x].isValid())
				{
					visitedPoints.add(point);
					location.add(point);
					continue;
				}
				point = getPointFromDirection(p,DrivingDirection.East);
				if(!visitedPoints.contains(point) && isValidPoint(map,new MyPoint(point.x,point.y,null,null)) && map[point.y][point.x].isValid())
				{
					visitedPoints.add(point);
					location.add(point);
					continue;
				}
				point = getPointFromDirection(p,DrivingDirection.West);
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
	private List<DrivingDirection> getJunctionDirections(DrivingDirection[][] map,int x2, int y2)
	{
		List<DrivingDirection> validDirections = new ArrayList<DrivingDirection>();
		
		int leftX = x2-1;
		int rightX = x2+1;
		int upY = y2 - 1;
		int downY = y2 + 1;
		
		if(inBounds(map,leftX,y2) && getDirection(map,leftX,y2) == DrivingDirection.West)
		{
			validDirections.add(DrivingDirection.West);
		}
		if(inBounds(map,rightX,y2) && getDirection(map,rightX,y2) == DrivingDirection.East)
		{
			validDirections.add(DrivingDirection.East);
		}
		if(inBounds(map,x2,upY) && getDirection(map,x2,upY) == DrivingDirection.North)
		{
			validDirections.add(DrivingDirection.North);
		}
		if(inBounds(map,x2,downY) && getDirection(map,x2,downY) == DrivingDirection.South)
		{
			validDirections.add(DrivingDirection.South);
		}
		return validDirections;
	}

	/*
	 * Helper method with swapping for better readability
	 */
	private DrivingDirection getDirection(DrivingDirection[][] map, int x, int y)
	{
		return map[y][x];
	}
	
	/*
	 * Helper method for bounds
	 */
	private boolean inBounds(DrivingDirection[][] map, int x2, int y2)
	{
		return y2 < map.length && y2 >= 0 && x2 >= 0 && x2 < map[y2].length;
	}

	/*
	 * Helper method for validity
	 */
	private boolean isValidPoint(DrivingDirection[][] map, MyPoint nextPoint)
	{
		return nextPoint.x >= 0 && nextPoint.x < map[0].length && nextPoint.y >= 0 && nextPoint.y < map.length;
	}

	/*
	 * Helper method for extending line of point direction
	 */
	private MyPoint getPointFromDirection(MyPoint p, DrivingDirection dir)
	{
		switch(dir)
		{
		case East:
			return new MyPoint(p.x+1,p.y,p,dir);
		case North:
			return new MyPoint(p.x,p.y-1,p,dir);
		case South:
			return new MyPoint(p.x,p.y+1,p,dir);
		case West:
			return new MyPoint(p.x-1,p.y,p,dir);
		default:
			return new MyPoint(p.x,p.y-1,p,DrivingDirection.North);
		
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
				currentDirection = DrivingDirection.None;
				return;
			}
			switch(currentDirection)
			{
				case East:
					x++;
					break;
				case North:
					y--;
					break;
				case South:
					y++;
					break;
				case West:
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
