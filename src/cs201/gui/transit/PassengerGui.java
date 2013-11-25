package cs201.gui.transit;

import java.awt.Color;
import java.awt.Graphics2D;

import cs201.gui.CityPanel;
import cs201.gui.Gui;
import cs201.roles.transit.PassengerRole;
import cs201.structures.Structure;

public class PassengerGui implements Gui
{
	private PassengerRole pass;
	
	private Structure destination;
	private int destX,destY;
	
	private int x, y;
	
	private boolean fired;
	
	private boolean present;
	

	public PassengerGui(PassengerRole pass,CityPanel city)
	{
		this(pass,city,pass.getCurrentLocation());
	}
	
	public PassengerGui(PassengerRole pass,CityPanel city, int x, int y)
	{
		this.pass = pass;
		this.x = x;
		this.y = y;
		destX = x;
		destY = y;
		fired = true;
		present = false;
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
		g.setColor(Color.RED);
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
				pass.msgAnimationFinished ();
			}
		}
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
