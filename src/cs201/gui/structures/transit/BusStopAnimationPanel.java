package cs201.gui.structures.transit;

import java.awt.Color;
import java.awt.Graphics;

import cs201.gui.SimCity201;
import cs201.gui.StructurePanel;
import cs201.structures.transit.BusStop;

public class BusStopAnimationPanel extends StructurePanel
{
	
	BusStop stop;

	public BusStopAnimationPanel(int i, SimCity201 sc)
	{
		super(i, sc);
	}
	
	public void setStop(BusStop stop)
	{
		this.stop = stop;
	}
	
	public void paintComponent(Graphics g)
	{
		g.setColor(getBackground());
		g.fillRect(0, 0, 500, 500);
		
		g.setColor(Color.BLACK);
		g.drawString(""+stop.getPassengerList().size(), 0, 100);
	}

}