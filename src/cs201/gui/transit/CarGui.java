package cs201.gui.transit;

import java.awt.Color;
import java.awt.Graphics2D;

import cs201.agents.transit.CarAgent;
import cs201.agents.transit.VehicleAgent;
import cs201.gui.CityPanel;

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
		g.setColor(Color.YELLOW);
		g.fillRect(getX(),getY(),CityPanel.GRID_SIZE,CityPanel.GRID_SIZE);
		
		g.setColor(Color.BLACK);
		if(((CarAgent)getVehicle()).getPassenger() != null)
		{
			g.drawString(""+((CarAgent)getVehicle()).getPassenger().getClass().getSimpleName(),getX()+CityPanel.GRID_SIZE,getY()+CityPanel.GRID_SIZE);
		}
	}

}
