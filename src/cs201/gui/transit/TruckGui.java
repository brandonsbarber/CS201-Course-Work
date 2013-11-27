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

/**
 * 
 * @author Brandon
 *
 */
public class TruckGui extends VehicleGui {

	private ArrayList<BufferedImage> movementSprites;
	
	/**
	 * Bus GUI for showing a truck
	 * @param vehicle the vehicle who holds the truck gui
	 * @param city the city panel in which this is drawn
	 * @param x the starting x position of the gui
	 * @param y the starting y position of the gui
	 */
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

	/**
	 * Draws the truck in relation to orientation
	 * @param g the graphics object on which to draw
	 */
	@Override
	public void drawBody(Graphics2D g)
	{
		//g.setColor(Color.YELLOW);
		//g.fillRect(getX(),getY(),CityPanel.GRID_SIZE,CityPanel.GRID_SIZE);
		g.drawImage(movementSprites.get(currentDirection.ordinal()), getX(),getY(),CityPanel.GRID_SIZE,CityPanel.GRID_SIZE, null);
		
	}

}
