package cs201.gui.transit;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import cs201.agents.transit.BusAgent;
import cs201.agents.transit.VehicleAgent;
import cs201.gui.ArtManager;
import cs201.gui.CityPanel;
import cs201.helper.Constants;

/**
 * 
 * @author Brandon
 *
 */
public class BusGui extends VehicleGui
{
	
	/**
	 * Bus GUI for showing a bus
	 * @param vehicle the vehicle who holds the bus gui
	 * @param city the city panel in which this is drawn
	 * @param x the starting x position of the gui
	 * @param y the starting y position of the gui
	 */
	public BusGui(VehicleAgent vehicle,CityPanel city, int x, int y)
	{
		super(vehicle,city,x,y);
	}

	/**
	 * Draws the bus in relation to orientation
	 * @param g the graphics object on which to draw
	 */
	@Override
	public void drawBody(Graphics2D g)
	{
		if(Constants.DEBUG_MODE)
		{
			g.setColor(Color.YELLOW);
			g.fillRect(getX(),getY(),CityPanel.GRID_SIZE,CityPanel.GRID_SIZE);
			
			g.setColor(Color.BLACK);
			g.drawString(""+((BusAgent)getVehicle()).getNumPassengers(),getX(),getY()+CityPanel.GRID_SIZE);	
		}
		else
		{
			String imgName = "Bus_";
			switch(currentDirection)
			{
			case Right:imgName+="Right";
				break;
			case Up:imgName+="Up";
				break;
			case Down:imgName+="Down";
				break;
			case Left:imgName+="Left";
				break;
			default:imgName+="Down";
				break;
			}
			g.drawImage(ArtManager.getImage(imgName), getX(),getY(),CityPanel.GRID_SIZE,CityPanel.GRID_SIZE, null);
		}
	}
}
