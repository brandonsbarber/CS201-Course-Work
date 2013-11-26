package cs201.gui.transit;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import cs201.agents.transit.VehicleAgent;
import cs201.gui.CityPanel;
import cs201.gui.CityPanel.DrivingDirection;
import cs201.gui.Gui;
import cs201.structures.Structure;

public abstract class VehicleGui implements Gui
{
	private VehicleAgent vehicle;
	
	private Structure destination;
	private int destX,destY;
	
	private int x, y;
	
	private boolean fired;
	
	private boolean present;
	
	private CityPanel city;

	private DrivingDirection currentDirection;
	
	public VehicleGui(VehicleAgent vehicle,CityPanel city)
	{
		this(vehicle,city,50,50);
	}
	
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
	}
	
	public void setPresent(boolean present)
	{
		this.present = present;
	}
	
	public void doGoToLocation(Structure structure)
	{
		destination = structure;
		destX = (int)destination.getParkingLocation().getX();
		destY = (int)destination.getParkingLocation().getY();
		fired = false;
		present = true;
	}
	
	public void draw(Graphics2D g)
	{
		drawBody(g);
		
		g.setColor(Color.BLACK);
		g.drawString(""+getVehicle().getClass().getSimpleName()+":"+getVehicle().getInstance(),getX(),getY());
	}
	
	public abstract void drawBody(Graphics2D g);

	@Override
	public void updatePosition()
	{
		if(!fired)
		{
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
				DrivingDirection[][] map = city.getDrivingMap();
				
				DrivingDirection square = getDirection(map,x/city.GRID_SIZE,y/city.GRID_SIZE);
				
				switch(square)
				{
				case Turn:
					junction(map,x/CityPanel.GRID_SIZE,y/CityPanel.GRID_SIZE);
					break;
				}
			}
			if(getX() == destX && getY() == destY)
			{
				fired = true;
				getVehicle().msgSetLocation(destination);
				getVehicle().msgAnimationDestinationReached();
			}
		}
	}

	private void junction(DrivingDirection[][] map, int x2, int y2)
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
		
		if(validDirections.size() == 1)
		{
			currentDirection = validDirections.get(0);
			return;
		}
		
		else if(validDirections.size() == 3)
		{
			if(x != destX)
			{
				for(DrivingDirection dir : validDirections)
				{
					currentDirection = dir;
					return;
				}
			}
			else
			{
				for(int i = 0; i < validDirections.size(); i++)
				{
					DrivingDirection dir = validDirections.get(i);
					if(dir == DrivingDirection.West || dir == DrivingDirection.East)
					{
						validDirections.remove(dir);
					}
				}
			}
		}
		if(validDirections.size() == 2)
		{
			if(DrivingDirection.opposites(validDirections.get(0),validDirections.get(1)))
			{
				if(validDirections.get(0).isHorizontal())
				{
					if(x > destX)
					{
						currentDirection = DrivingDirection.West;
						return;
					}
					else
					{
						currentDirection = DrivingDirection.East;
						return;
					}
				}
				else
				{
					if(y > destY)
					{
						currentDirection = DrivingDirection.North;
						return;
					}
					else
					{
						currentDirection = DrivingDirection.South;
						return;
					}
				}
			}
			else
			{
				if(x == destX)
				{
					DrivingDirection dir = DrivingDirection.None;
					for(DrivingDirection d : validDirections)
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
					DrivingDirection dir = DrivingDirection.None;
					for(DrivingDirection d : validDirections)
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
					DrivingDirection dir = DrivingDirection.None;
					for(DrivingDirection d : validDirections)
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

	private DrivingDirection getDirection(DrivingDirection[][] map, int x, int y)
	{
		return map[y][x];
	}
	
	private boolean inBounds(DrivingDirection[][] map, int x2, int y2)
	{
		return y2 < map.length && y2 >= 0 && x2 >= 0 && x2 < map[y2].length;
	}
	
	@Override
	public boolean isPresent()
	{
		return present;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public VehicleAgent getVehicle() {
		return vehicle;
	}

	public void setVehicle(VehicleAgent vehicle) {
		this.vehicle = vehicle;
	}
}
