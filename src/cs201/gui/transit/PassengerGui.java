package cs201.gui.transit;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;
import java.util.Stack;

import javax.swing.JOptionPane;

import cs201.gui.ArtManager;
import cs201.gui.CityPanel;
import cs201.helper.Constants;
import cs201.helper.transit.MovementDirection;
import cs201.helper.transit.Pathfinder;
import cs201.gui.Gui;
import cs201.roles.transit.PassengerRole;
import cs201.structures.Structure;
import cs201.trace.AlertLog;
import cs201.trace.AlertTag;

/**
 * 
 * @author Brandon
 *
 */
public class PassengerGui implements Gui
{
	private PassengerRole pass;
	
	private Structure destination;
	private int destX,destY;
	
	private int x, y;
	
	private boolean fired;
	
	private boolean present;
	
	private CityPanel city;
	
	private MovementDirection currentDirection;
	
	private Stack<MovementDirection> moves;

	private boolean pathfinding = false;

	private boolean allowedToMove;

	private Point current, next;

	/**
	 * Creates a passenger gui
	 * @param pass the passenger who holds the gui
	 * @param city the city which contains the gui
	 */
	public PassengerGui(PassengerRole pass,CityPanel city)
	{
		this(pass,city,pass.getCurrentLocation());
		allowedToMove = true;
	}
	
	/**
	 * Creates a passenger gui
	 * @param pass the passenger who holds the gui
	 * @param city the city which contains the gui
	 * @param x the initial x
	 * @param y the initial y
	 */
	public PassengerGui(PassengerRole pass,CityPanel city, int x, int y)
	{
		this.pass = pass;
		this.city = city;
		this.x = x;
		this.y = y;
		destX = x;
		destY = y;
		fired = true;
		present = false;
		currentDirection = MovementDirection.None;
		moves = new Stack<MovementDirection>();
		roaming = false;
	}
	
	/**
	 * Creates a passenger gui
	 * @param pass the passenger who holds the gui
	 * @param city the city which contains the gui
	 * @param s the structure to start at
	 */
	public PassengerGui(PassengerRole pass,CityPanel city, Structure s)
	{
		this(pass, city, (int) s.getRect().x, (int) s.getRect().y);
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
			List<Gui> list = city.crosswalkPermissions.get(current.y).get(current.x);
			synchronized(list)
			{
				list.remove(this);
			}
		}
	}
	
	/**
	 * Signals the GUI to go to a location
	 * @param structure the structure to go to
	 */
	public void doGoToLocation(Structure structure)
	{
		//JOptionPane.showMessageDialog(null,""+pass.getName()+": Being told to go to "+structure.getEntranceLocation().x/CityPanel.GRID_SIZE+" "+structure.getEntranceLocation().y/CityPanel.GRID_SIZE);
		destination = structure;
		destX = (int)structure.getEntranceLocation().x;
		destY = (int)structure.getEntranceLocation().y;
		fired = false;
		present = true;
		
		current = new Point(x/CityPanel.GRID_SIZE,y/CityPanel.GRID_SIZE);
		next = current;
		
		//city.permissions[current.y][current.x].tryAcquire();
		
		findPath();
	}
	
	public void doGoToLocation(int x, int y)
	{
		destX = x;
		destY = y;
		fired = false;
		present = true;
		
		current = new Point(this.x/CityPanel.GRID_SIZE,this.y/CityPanel.GRID_SIZE);
		next = current;
		
		findPath();
	}
	
	public void doRoam()
	{
		roaming = true;
		Point p = Pathfinder.findRandomWalkingLocation(city.getWalkingMap(),city.getDrivingMap());
		doGoToLocation(p.x*CityPanel.GRID_SIZE,p.y*CityPanel.GRID_SIZE);
	}
	
	/*
	 * Performs BFS to find best path
	 */
	private void findPath()
	{
		moves.clear();
		pathfinding = true;
		try
		{
			moves = Pathfinder.calcTwoWayMove(city.getWalkingMap(), x, y, destX, destY);
		}
		catch(IllegalArgumentException e)
		{
			AlertLog.getInstance().logError(AlertTag.TRANSIT, pass.getName(), ""+e.getMessage());
		}
		pathfinding = false;
	}

	/**
	 * Draws the GUI in the given graphcis object
	 * @param g the graphics object in which to draw
	 */
	public void draw(Graphics2D g)
	{	
		if (Constants.DEBUG_MODE)
		{
			g.setColor(Color.RED);
			g.fillRect(x,y,CityPanel.GRID_SIZE,CityPanel.GRID_SIZE);
			
			g.setColor(Color.BLACK);
			g.drawString(""+destination, x,y);
			g.drawString(""+pass.getName(), x,y+CityPanel.GRID_SIZE);
		}
		else
		{
			String moveDir = "Person_";
			switch(currentDirection)
			{
			case Right:moveDir+="Right";
				break;
			case None:moveDir+="Down";
				break;
			case Up:moveDir+="Up";
				break;
			case Down:moveDir+="Down";
				break;
			case Turn:moveDir+="Down";
				break;
			case Left:moveDir+="Left";
				break;
			default:moveDir+="Down";
				break;
			}
			
			g.drawImage (ArtManager.getImage(moveDir),x,y,CityPanel.GRID_SIZE,CityPanel.GRID_SIZE,null);
		}
	}

	/**
	 * To be extended in subclass
	 * @param g graphics object to render in
	 */
	@Override
	public void updatePosition()
	{
		if(!fired && !pathfinding)
		{
			if(x == destX && y == destY)
			{
				List<Gui> list = city.crosswalkPermissions.get(current.y).get(current.x);
				synchronized(list)
				{
					list.remove(this);
				}
				list = city.crosswalkPermissions.get(next.y).get(next.x);
				synchronized(list)
				{
					list.remove(this);
				}
				currentDirection = MovementDirection.None;
				fired = true;
				if(!roaming)
				{
					pass.msgAnimationFinished ();
				}
				else
				{
					doRoam();
				}
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
						List<Gui> list = city.crosswalkPermissions.get(current.y).get(current.x);
						synchronized(list)
						{
							list.remove(this);
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
				List<Gui> list = city.crosswalkPermissions.get(next.y).get(next.x);
				synchronized(list)
				{
					for(Gui g : list)
					{
						if(g instanceof VehicleGui)
						{
							allowedToMove = false;
							return;
						}
					}
					list.add(this);
					allowedToMove = true;
				}
				
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
	 * Sets the location of the gui
	 * @param x the new x
	 * @param y the new y
	 */
	public void setLocation(int x, int y)
	{
		System.out.println("Setting location: "+this.pass.getName()+" "+x+" "+y);
		this.x = x;
		this.y = y;
	}

	public boolean locationEquals(Structure currentLocation) 
	{
		return currentLocation.getEntranceLocation().x == x && currentLocation.getEntranceLocation().y == y;
	}

	public void setLocation()
	{
		Point p = Pathfinder.findRandomWalkingLocation(city.getWalkingMap(),city.getDrivingMap());
		setLocation(p.x*CityPanel.GRID_SIZE,p.y*CityPanel.GRID_SIZE);
		
		current = new Point(x/CityPanel.GRID_SIZE,y/CityPanel.GRID_SIZE);
		next = current;
		
		setPresent(true);
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public void stopRoam()
	{
		roaming = false;
		destX = x;
		destY = y;
	}
	
	boolean roaming;

	public Point findRoad(int i, int j)
	{
		return Pathfinder.findRoad(city, i, j);
	}

}
