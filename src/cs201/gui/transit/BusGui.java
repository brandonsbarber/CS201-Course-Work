package cs201.gui.transit;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import cs201.agents.transit.VehicleAgent;
import cs201.agents.transit.BusAgent;
import cs201.gui.CityPanel;
import cs201.gui.CityPanel.DrivingDirection;
import cs201.gui.CityPanel.WalkingDirection;

public class BusGui extends VehicleGui
{
	private ArrayList<BufferedImage> movementSprites;

	public BusGui(VehicleAgent vehicle,CityPanel city, int x, int y)
	{
		super(vehicle,city,x,y);
		
		movementSprites = new ArrayList<BufferedImage>();
		try
		{
			movementSprites.add(null);
			movementSprites.add(DrivingDirection.North.ordinal(),ImageIO.read(new File("data/TransitSprites/Bus_North.png")));
			movementSprites.add(DrivingDirection.South.ordinal(),ImageIO.read(new File("data/TransitSprites/Bus_South.png")));
			movementSprites.add(DrivingDirection.East.ordinal(),ImageIO.read(new File("data/TransitSprites/Bus_East.png")));
			movementSprites.add(DrivingDirection.West.ordinal(),ImageIO.read(new File("data/TransitSprites/Bus_West.png")));
		}
		catch(Exception e)
		{
			System.out.println("ERROR");
			e.printStackTrace();
		}
	}

	@Override
	public void drawBody(Graphics2D g)
	{
		g.setColor(Color.YELLOW);
		//g.fillRect(getX(),getY(),CityPanel.GRID_SIZE,CityPanel.GRID_SIZE);
		g.drawImage(movementSprites.get(currentDirection.ordinal()), getX(),getY(),CityPanel.GRID_SIZE,CityPanel.GRID_SIZE, null);
		
		g.setColor(Color.BLACK);
		g.drawString(""+((BusAgent)getVehicle()).getNumPassengers(),getX(),getY()+CityPanel.GRID_SIZE);
	}
}
