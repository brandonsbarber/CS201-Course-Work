package cs201.gui.transit;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import cs201.agents.transit.VehicleAgent;
import cs201.gui.CityPanel;
import cs201.gui.Gui;
import cs201.structures.Structure;

public class VehicleGui implements Gui
{
	private VehicleAgent vehicle;
	
	private Structure destination;
	private int destX,destY;
	
	private int x, y;
	
	private boolean fired;
	
	private boolean present;
	
	public VehicleGui(VehicleAgent vehicle,CityPanel city)
	{
		this(vehicle,city,50,50);
	}
	
	public VehicleGui(VehicleAgent vehicle,CityPanel city, int x, int y)
	{
		this.vehicle = vehicle;
		this.x = x;
		this.y = y;
		destX = x;
		destY = y;
		fired = true;
		present = false;
	}
	
	public void setPresent(boolean present)
	{
		this.present = present;
	}
	
	public void doGoToLocation(Structure structure)
	{
		System.out.println("Going to location. "+structure);
		destination = structure;
		destX = (int)structure.getX();
		destY = (int)structure.getY();
		fired = false;
		present = true;
	}
	
	//Make me abstract for subclasses!
	public void draw(Graphics2D g)
	{
		g.setColor(Color.YELLOW);
		g.fillRect(x,y,CityPanel.GRID_SIZE,CityPanel.GRID_SIZE);
	}

	@Override
	public void updatePosition()
	{
		if(!fired)
		{
			if(x < destX)
			{
				x ++;
			}
			else if(x > destX)
			{
				x --;
			}
			if(y < destY)
			{
				y ++;
			}
			else if(y > destY)
			{
				y --;
			}
			if(x == destX && y == destY)
			{
				fired = true;
				vehicle.msgSetLocation(destination);
				vehicle.msgAnimationDestinationReached();
			}
		}
	}

	@Override
	public boolean isPresent()
	{
		return present;
	}
}
