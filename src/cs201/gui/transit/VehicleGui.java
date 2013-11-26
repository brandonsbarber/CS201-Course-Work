package cs201.gui.transit;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import cs201.agents.transit.VehicleAgent;
import cs201.gui.CityPanel;
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
	
	public VehicleGui(VehicleAgent vehicle,CityPanel city)
	{
		this(vehicle,city,50,50);
	}
	
	public VehicleGui(VehicleAgent vehicle,CityPanel city, int x, int y)
	{
		this.setVehicle(vehicle);
		this.setX(x);
		this.setY(y);
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
		destination = structure;
		destX = (int)structure.getX();
		destY = (int)structure.getY();
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
			if(getX() < destX)
			{
				setX(getX() + 1);
			}
			else if(getX() > destX)
			{
				setX(getX() - 1);
			}
			if(getY() < destY)
			{
				setY(getY() + 1);
			}
			else if(getY() > destY)
			{
				setY(getY() - 1);
			}
			if(getX() == destX && getY() == destY)
			{
				fired = true;
				getVehicle().msgSetLocation(destination);
				getVehicle().msgAnimationDestinationReached();
			}
		}
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
