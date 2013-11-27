package cs201.gui.transit;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import cs201.agents.transit.VehicleAgent;
import cs201.gui.CityPanel;
import cs201.gui.CityPanel.DrivingDirection;

public class TruckGui extends VehicleGui {

	private ArrayList<BufferedImage> movementSprites;
	
	public TruckGui(VehicleAgent vehicle, CityPanel city, int x, int y)
	{
		super(vehicle, city, x, y);
		
		movementSprites = new ArrayList<BufferedImage>();
		try
		{
			movementSprites.add(null);
			movementSprites.add(DrivingDirection.North.ordinal(),ImageIO.read(new File("data/TransitSprites/Truck.png")));
			movementSprites.add(DrivingDirection.South.ordinal(),ImageIO.read(new File("data/TransitSprites/Truck.png")));
			movementSprites.add(DrivingDirection.East.ordinal(),ImageIO.read(new File("data/TransitSprites/Truck.png")));
			movementSprites.add(DrivingDirection.West.ordinal(),ImageIO.read(new File("data/TransitSprites/Truck.png")));
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
		//g.setColor(Color.YELLOW);
		//g.fillRect(getX(),getY(),CityPanel.GRID_SIZE,CityPanel.GRID_SIZE);
		g.drawImage(movementSprites.get(currentDirection.ordinal()), getX(),getY(),CityPanel.GRID_SIZE,CityPanel.GRID_SIZE, null);
		
	}

}
