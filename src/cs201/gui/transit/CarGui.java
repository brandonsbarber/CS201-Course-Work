package cs201.gui.transit;

import java.awt.Color;
import java.awt.Graphics2D;

import cs201.agents.transit.CarAgent;
import cs201.agents.transit.VehicleAgent;
import cs201.gui.ArtManager;
import cs201.gui.CityPanel;
import cs201.helper.Constants;

/**
 * 
 * @author Brandon
 *
 */
public class CarGui extends VehicleGui
{

	/**
	 * Car GUI for showing a car
	 * @param vehicle the vehicle who holds the car gui
	 * @param city the city panel in which this is drawn
	 * @param x the starting x position of the gui
	 * @param y the starting y position of the gui
	 */
	public CarGui(VehicleAgent vehicle, CityPanel city, int x, int y)
	{
		super(vehicle, city, x, y);
		
	}

	/**
	 * Car GUI for showing a car
	 * @param vehicle the vehicle who holds the car gui
	 * @param city the city panel in which this is drawn
	 */
	public CarGui(CarAgent car, CityPanel cityPanel)
	{
		super(car,cityPanel);
	}

	/**
	 * Draws the car
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
			if(((CarAgent)getVehicle()).getPassenger() != null)
			{
				g.drawString(""+((CarAgent)getVehicle()).getPassenger().getClass().getSimpleName(),getX()+CityPanel.GRID_SIZE,getY()+CityPanel.GRID_SIZE);
			}
		}
		else
		{
			String imgName = "Car_";
			if(((CarAgent)getVehicle()).getPassenger() != null)
			{
				imgName+="Occupied_";
			}
			else
			{
				imgName+="Empty_";
			}
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
