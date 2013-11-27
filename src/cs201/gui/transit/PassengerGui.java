package cs201.gui.transit;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import cs201.gui.CityPanel;
import cs201.gui.CityPanel.WalkingDirection;
import cs201.gui.Gui;
import cs201.roles.transit.PassengerRole;
import cs201.structures.Structure;

/**
 * 
 * @author Brandon
 *
 */
public class PassengerGui implements Gui
{
	class MyPoint extends Point
	{
		MyPoint prev;
		WalkingDirection move;
		
		public MyPoint(int i, int j, MyPoint previous,WalkingDirection moveDir)
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
	
	public static ArrayList<BufferedImage> movementSprites;
	
	private PassengerRole pass;
	
	private Structure destination;
	private int destX,destY;
	
	private int x, y;
	
	private boolean fired;
	
	private boolean present;
	
	private CityPanel city;
	
	private WalkingDirection currentDirection;
	
	private Stack<WalkingDirection> moves;

	private boolean pathfinding = false;

	public PassengerGui(PassengerRole pass,CityPanel city)
	{
		this(pass,city,pass.getCurrentLocation());
	}
	
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
		currentDirection = WalkingDirection.None;
		moves = new Stack<WalkingDirection>();
		
		movementSprites = new ArrayList<BufferedImage>();
		try
		{
			movementSprites.add(null);
			movementSprites.add(WalkingDirection.North.ordinal(),ImageIO.read(new File("data/TransitSprites/Walk_North.png")));
			movementSprites.add(WalkingDirection.South.ordinal(),ImageIO.read(new File("data/TransitSprites/Walk_South.png")));
			movementSprites.add(WalkingDirection.East.ordinal(),ImageIO.read(new File("data/TransitSprites/Walk_East.png")));
			movementSprites.add(WalkingDirection.West.ordinal(),ImageIO.read(new File("data/TransitSprites/Walk_West.png")));
		}
		catch(Exception e)
		{
			System.out.println("ERROR");
			e.printStackTrace();
		}
		
	}
	
	public PassengerGui(PassengerRole pass,CityPanel city, Structure s)
	{
		this(pass,city,(int)s.x,(int)s.y);
	}
	
	public void setPresent(boolean present)
	{
		this.present = present;
	}
	
	public void doGoToLocation(Structure structure)
	{
		//JOptionPane.showMessageDialog(null,""+pass.getName()+": Being told to go to "+structure.getEntranceLocation().x/CityPanel.GRID_SIZE+" "+structure.getEntranceLocation().y/CityPanel.GRID_SIZE);
		destination = structure;
		destX = (int)structure.getEntranceLocation().x;
		destY = (int)structure.getEntranceLocation().y;
		fired = false;
		present = true;
		
		findPath();
	}
	
	private void findPath()
	{
		pathfinding = true;
		//JOptionPane.showMessageDialog(null,"Finding a path for "+pass.getName());
		WalkingDirection[][] map = city.getWalkingMap();
		
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
			//JOptionPane.showMessageDialog(null,""+p+" "+p.move);
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
			WalkingDirection currentDirection = map[p.y][p.x];
			
			if(currentDirection == WalkingDirection.Turn)
			{
				List<WalkingDirection> validDirections = getJunctionDirections(map,p.x,p.y);
				for(WalkingDirection dir : validDirections)
				{
					MyPoint nextPoint = getPointFromDirection(p,dir);
					if(!visitedPoints.contains(nextPoint) && isValidPoint(map,nextPoint))
					{
						visitedPoints.add(nextPoint);
						location.add(nextPoint);
					}
				}
			}
			else if(currentDirection == WalkingDirection.None)
			{
				//Find an adjacent sidewalk piece
				MyPoint point = getPointFromDirection(p,WalkingDirection.South);
				if(!visitedPoints.contains(point) && isValidPoint(map,new MyPoint(point.x,point.y,null,null)) && map[point.y][point.x].isValid())
				{
					visitedPoints.add(point);
					location.add(point);
					continue;
				}
				point = getPointFromDirection(p,WalkingDirection.North);
				if(!visitedPoints.contains(point) && isValidPoint(map,new MyPoint(point.x,point.y,null,null)) && map[point.y][point.x].isValid())
				{
					visitedPoints.add(point);
					location.add(point);
					continue;
				}
				point = getPointFromDirection(p,WalkingDirection.East);
				if(!visitedPoints.contains(point) && isValidPoint(map,new MyPoint(point.x,point.y,null,null)) && map[point.y][point.x].isValid())
				{
					visitedPoints.add(point);
					location.add(point);
					continue;
				}
				point = getPointFromDirection(p,WalkingDirection.West);
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
			JOptionPane.showMessageDialog(null,""+pass.getName()+": I cannot find a path.");
		}
		else
		{
			//clear first element
			moves.pop();
		}
		pathfinding = false;
	}

	private boolean isValidPoint(WalkingDirection[][] map, MyPoint nextPoint)
	{
		return nextPoint.x >= 0 && nextPoint.x < map[0].length && nextPoint.y >= 0 && nextPoint.y < map.length;
	}

	private MyPoint getPointFromDirection(MyPoint p, WalkingDirection dir)
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
			return new MyPoint(p.x,p.y-1,p,WalkingDirection.North);
		
		}
	}

	//Make me abstract for subclasses!
	public void draw(Graphics2D g)
	{
		g.setColor(Color.RED);
			g.drawImage (movementSprites.get(currentDirection.ordinal()),x,y,CityPanel.GRID_SIZE,CityPanel.GRID_SIZE,null);
		g.setColor(Color.BLACK);
		g.drawString(""+destination, x,y);
		g.drawString(""+pass.getName(), x,y+CityPanel.GRID_SIZE);
		//g.fillRect(x,y,CityPanel.GRID_SIZE,CityPanel.GRID_SIZE);
	}

	@Override
	public void updatePosition()
	{
		if(!fired && !pathfinding)
		{
			if(x == destX && y == destY)
			{
				fired = true;
				pass.msgAnimationFinished ();
				currentDirection = WalkingDirection.None;
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
	
	private List<WalkingDirection> getJunctionDirections(WalkingDirection[][] map,int x2, int y2)
	{
		List<WalkingDirection> validDirections = new ArrayList<WalkingDirection>();
		
		int leftX = x2-1;
		int rightX = x2+1;
		int upY = y2 - 1;
		int downY = y2 + 1;
		
		if(inBounds(map,leftX,y2) && getDirection(map,leftX,y2) == WalkingDirection.West)
		{
			validDirections.add(WalkingDirection.West);
		}
		if(inBounds(map,rightX,y2) && getDirection(map,rightX,y2) == WalkingDirection.East)
		{
			validDirections.add(WalkingDirection.East);
		}
		if(inBounds(map,x2,upY) && getDirection(map,x2,upY) == WalkingDirection.North)
		{
			validDirections.add(WalkingDirection.North);
		}
		if(inBounds(map,x2,downY) && getDirection(map,x2,downY) == WalkingDirection.South)
		{
			validDirections.add(WalkingDirection.South);
		}
		return validDirections;
	}

	private WalkingDirection getDirection(WalkingDirection[][] map, int x, int y)
	{
		return map[y][x];
	}
	
	private boolean inBounds(WalkingDirection[][] map, int x2, int y2)
	{
		return y2 < map.length && y2 >= 0 && x2 >= 0 && x2 < map[y2].length;
	}

	@Override
	public boolean isPresent()
	{
		return present;
	}
	
	public void setLocation(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

}
