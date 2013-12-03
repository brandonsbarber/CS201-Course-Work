package cs201.gui.structures.transit;

import java.awt.Color;
import java.awt.Graphics;

import cs201.gui.SimCity201;
import cs201.gui.StructurePanel;

public class BusStopAnimationPanel extends StructurePanel
{

	public BusStopAnimationPanel(int i, SimCity201 sc)
	{
		super(i, sc);
	}
	
	public void paintComponent(Graphics g)
	{
		g.setColor(getBackground());
		g.fillRect(0, 0, 500, 500);
	}

}
