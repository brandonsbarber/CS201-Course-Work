package cs201.gui.transit;

import java.awt.Color;
import java.awt.Graphics2D;

import cs201.agents.transit.CarAgent;
import cs201.agents.transit.VehicleAgent;
import cs201.gui.CityPanel;

public class CarGui extends VehicleGui
{

	public CarGui(VehicleAgent vehicle, CityPanel city, int x, int y)
	{
		super(vehicle, city, x, y);
	}

	public CarGui(CarAgent car, CityPanel cityPanel)
	{
		super(car,cityPanel);
	}

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
