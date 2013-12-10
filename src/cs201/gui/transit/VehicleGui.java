package cs201.gui.transit;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.JOptionPane;

import cs201.agents.transit.VehicleAgent;
import cs201.gui.CityPanel;
import cs201.gui.Gui;
import cs201.helper.Constants;
import cs201.helper.transit.MovementDirection;
import cs201.helper.transit.Pathfinder;
import cs201.structures.Structure;
import cs201.trace.AlertLog;
import cs201.trace.AlertTag;

/**
 * 
 * @author Brandon
 *
 */
public abstract class VehicleGui implements Gui
{
	
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
	
	private Point next;
	private Point current;

	private boolean allowedToMove = true;
	
	private Set<Point> acquiredPoints;
	
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
		acquiredPoints = new HashSet<Point>();
	}
	
	/**
	 * Sets whether the gui is present in the scene and should be rendered
	 * @param present whether it is present
	 */
	public void setPresent(boolean present)
	{
		this.present = present;
		if(!present)
		{
			city.permissions[next.y][next.x].release();
		}
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

		current = new Point(x/CityPanel.GRID_SIZE,y/CityPanel.GRID_SIZE);
		next = current;
		
		city.permissions[current.y][current.x].tryAcquire();
		
		findPath();
	}
	
	/*
	 * Performs BFS to find best path
	 */
	private void findPath()
	{
		pathfinding = true;
		moves = Pathfinder.calcOneWayMove(city.getDrivingMap(), x, y, destX, destY);
		pathfinding = false;
	}
	
	/**
	 * Draws the GUI in the given graphics object
	 * @param g the graphics object in which to draw
	 */
	public void draw(Graphics2D g)
	{
		drawBody(g);
		
		if(Constants.DEBUG_MODE)
		{
			g.setColor(Color.BLACK);
			g.drawString(""+getVehicle().getClass().getSimpleName()+":"+getVehicle().getInstance(),getX(),getY());
		}
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

				revertCrosswalk();
				city.permissions[current.y][current.x].release();
				
				return;
			}
			if(allowedToMove)
			{
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
			}
			if(x % CityPanel.GRID_SIZE == 0 && y % CityPanel.GRID_SIZE == 0 && !moves.isEmpty())
			{	
				if(allowedToMove)
				{
					currentDirection = moves.pop();
				
					if(current != next)
					{
						if(!Pathfinder.isCrossWalk(current, city.getWalkingMap(), city.getDrivingMap()) || !Pathfinder.isInIntersection(next, city.getWalkingMap()).contains(current))
						{
							city.permissions[current.y][current.x].release();
							revertCrosswalk();
							acquiredPoints.remove(current);
						}
						
						if(Pathfinder.isInIntersection(city, current) && !Pathfinder.isInIntersection(city,next))
						{
							Set<Point> intersectionPoints = Pathfinder.tryIntersectionAcquire(city, current.x,current.y);
							for(Point p : intersectionPoints)
							{
								if(acquiredPoints.remove(p))
								{
									city.permissions[p.y][p.x].release();
								}
							}
						}
					}
					
					current = next;
					
					switch(currentDirection)
					{
					case Down:next = new Point(current.x,current.y + 1);
						break;
					case Left:next = new Point(current.x - 1,current.y);
						break;
					case Right:next = new Point(current.x + 1,current.y);
						break;
					case Up:next = new Point(current.x,current.y - 1);
						break;
					default:next = current;
						break;
					
					}
				}
				
				if((canMoveCrosswalk() && city.permissions[next.y][next.x].tryAcquire()) || acquiredPoints.contains(next))
				{
					allowedToMove = true;
					if(Pathfinder.isInIntersection(city,next))
					{
						Set<Point> intersectionPoints = Pathfinder.tryIntersectionAcquire(city, next.x,next.y);
						for(Point p : intersectionPoints)
						{
							city.permissions[p.y][p.x].tryAcquire();
						}	
						acquiredPoints.addAll(intersectionPoints);
					}
				}
				else
				{
					allowedToMove = false;
				}
				
				return;
			}
			
		}
	}

	private boolean canMoveCrosswalk()
	{
		if(city.getWalkingMap()[next.y][next.x].isValid())
		{
			List<Gui> list = city.crosswalkPermissions.get(next.y).get(next.x);
			synchronized(list)
			{
				if(list.size() != 0)
				{
					return false;
				}
				list.add(this);
			}
		}
		return true;
	}
	
	private void revertCrosswalk()
	{
		List<Gui> list = city.crosswalkPermissions.get(current.y).get(current.x);
		synchronized(list)
		{
			list.remove(this);
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
