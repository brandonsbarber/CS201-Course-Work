package cs201.gui.transit;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import cs201.gui.CityPanel;
import cs201.gui.CityPanel.DrivingDirection;
import cs201.gui.CityPanel.WalkingDirection;
import cs201.gui.Gui;
import cs201.roles.transit.PassengerRole;
import cs201.structures.Structure;

public class PassengerGui implements Gui
{
	public static ArrayList<BufferedImage> movementSprites;
	
	
	private PassengerRole pass;
	
	private Structure destination;
	private int destX,destY;
	
	private int x, y;
	
	private boolean fired;
	
	private boolean present;
	
	private CityPanel city;
	
	private WalkingDirection currentDirection;
	

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
		destination = structure;
		destX = (int)structure.getEntranceLocation().x;
		destY = (int)structure.getEntranceLocation().y;
		fired = false;
		present = true;
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
		if(!fired)
		{
			if(x == destX && y == destY)
			{
				fired = true;
				pass.msgAnimationFinished ();
				return;
			}
			if(getDirection(city.getWalkingMap(),x/city.GRID_SIZE,y/city.GRID_SIZE) == WalkingDirection.None)
			{
				int xDistance = destX - x;
				int yDistance = destY - y;
				
				if(Math.abs(xDistance) > Math.abs(yDistance))
				{
					if(xDistance < 0)
					{
						currentDirection = WalkingDirection.West;
					}
					else
					{
						currentDirection = WalkingDirection.East;
					}
				}
				else
				{
					if(yDistance < 0)
					{
						currentDirection = WalkingDirection.North;
					}
					else
					{
						currentDirection = WalkingDirection.South;
					}
				}
				
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
			if(x % CityPanel.GRID_SIZE == 0 && y % CityPanel.GRID_SIZE == 0)
			{
				WalkingDirection[][] map = city.getWalkingMap();
				
				WalkingDirection square = getDirection(map,x/city.GRID_SIZE,y/city.GRID_SIZE);
				
				if(currentDirection != square)
				{
					currentDirection = square;
				}
				switch(square)
				{
				case Turn:
					junction(map,x/CityPanel.GRID_SIZE,y/CityPanel.GRID_SIZE);
					break;
				}
			}
		}
	}
	
	private void junction(WalkingDirection[][] map, int x2, int y2)
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
		
		if(validDirections.size() == 1)
		{
			currentDirection = validDirections.get(0);
			return;
		}
		
		else if(validDirections.size() == 3)
		{
			if(x != destX)
			{
				for(WalkingDirection dir : validDirections)
				{
					currentDirection = dir;
					return;
				}
			}
			else
			{
				for(int i = 0; i < validDirections.size(); i++)
				{
					WalkingDirection dir = validDirections.get(i);
					if(dir == WalkingDirection.West || dir == WalkingDirection.East)
					{
						validDirections.remove(dir);
					}
				}
			}
		}
		if(validDirections.size() == 2)
		{
			if(WalkingDirection.opposites(validDirections.get(0),validDirections.get(1)))
			{
				if(validDirections.get(0).isHorizontal())
				{
					if(x > destX)
					{
						currentDirection = WalkingDirection.West;
						return;
					}
					else
					{
						currentDirection = WalkingDirection.East;
						return;
					}
				}
				else
				{
					if(y > destY)
					{
						currentDirection = WalkingDirection.North;
						return;
					}
					else
					{
						currentDirection = WalkingDirection.South;
						return;
					}
				}
			}
			else
			{
				if(x == destX)
				{
					WalkingDirection dir = WalkingDirection.None;
					for(WalkingDirection d : validDirections)
					{
						if(!d.isHorizontal())
						{
							dir = d;
						}
					}
					currentDirection = dir;
					return;
				}
				else if(y == destY)
				{
					WalkingDirection dir = WalkingDirection.None;
					for(WalkingDirection d : validDirections)
					{
						if(!d.isVertical())
						{
							dir = d;
						}
					}
					currentDirection = dir;
					return;
				}
				else
				{
					WalkingDirection dir = WalkingDirection.None;
					for(WalkingDirection d : validDirections)
					{
						if(d.isHorizontal())
						{
							dir = d;
						}
					}
					currentDirection = dir;
					return;
				}
			}
		}		
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
