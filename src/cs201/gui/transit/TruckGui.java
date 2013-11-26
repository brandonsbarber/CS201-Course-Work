package cs201.gui.transit;

import java.awt.Color;
import java.awt.Graphics2D;

import cs201.agents.transit.VehicleAgent;
import cs201.gui.CityPanel;

public class TruckGui extends VehicleGui {

	public TruckGui(VehicleAgent vehicle, CityPanel city, int x, int y)
	{
		super(vehicle, city, x, y);
	}

	@Override
	public void draw(Graphics2D g)
	{
		g.setColor(Color.YELLOW);
		g.fillRect(getX(),getY(),CityPanel.GRID_SIZE,CityPanel.GRID_SIZE);
		
		g.setColor(Color.BLACK);
		g.drawString(""+getVehicle().getClass(),getX(),getY());
	}

}
