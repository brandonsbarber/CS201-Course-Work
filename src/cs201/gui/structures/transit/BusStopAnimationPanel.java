package cs201.gui.structures.transit;

import java.awt.Color;
import java.awt.Graphics;

import cs201.gui.ArtManager;
import cs201.gui.CityPanel;
import cs201.gui.SimCity201;
import cs201.gui.StructurePanel;
import cs201.helper.Constants;
import cs201.structures.transit.BusStop;

public class BusStopAnimationPanel extends StructurePanel
{
	
	public static int BENCH_X = 15;
	public static int BENCH_Y = 150;
	
	public static int BENCH_HEIGHT = 30;
	
	public static int STREET_X = 0;
	public static int STREET_Y = BENCH_Y+BENCH_HEIGHT+100;
	
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
		g.fillRect(0, 0, getWidth(), getHeight());
		
		if(Constants.DEBUG_MODE)
		{
			g.setColor(Color.GRAY.darker());
			g.fillRect(0, 0, getWidth(), getHeight());
			
			g.setColor(getBackground());
			g.fillRect(0,0,getWidth(),STREET_Y);
			
			g.setColor(Color.BLACK);
			g.drawString(""+stop.getPassengerList().size(), 0, 100);
			
			g.setColor(Color.GREEN);
			g.fillRect(BENCH_X, BENCH_Y, getWidth()-BENCH_X, BENCH_HEIGHT);
			
			if(stop != null)
			{
				int numPassengers = stop.getPassengerList().size();
				int length = getWidth()-BENCH_X;
				int remainder = length - numPassengers*CityPanel.GRID_SIZE;
				int spacing = (int)(1.0*remainder/numPassengers);
				
				for(int i = 0; i < numPassengers; i++)
				{
					g.setColor(Color.RED);
					g.fillRect(+BENCH_X+i*CityPanel.GRID_SIZE+spacing*i, BENCH_Y, CityPanel.GRID_SIZE, CityPanel.GRID_SIZE);
				}
			}
		}
		else
		{
			for(int x = 0; x < getWidth(); x+=32)
			{
				for(int y = 0; y < getHeight(); y+=32)
				{
					g.drawImage(ArtManager.getImage("Road_Tile"),x, y, 32, 32,this);
				}
			}
			
			g.drawImage(ArtManager.getImage("Restaurant_Brandon_Floor"),0,0,getWidth(),STREET_Y,null);
			
			g.drawImage(ArtManager.getImage("Bus_Stop_Bench"),BENCH_X, BENCH_Y, getWidth()-BENCH_X, BENCH_HEIGHT,null);
			
			if(stop != null)
			{
				int numPassengers = stop.getPassengerList().size();
				int length = getWidth()-BENCH_X;
				int remainder = length - numPassengers*CityPanel.GRID_SIZE;
				int spacing = (int)(1.0*remainder/numPassengers);
				
				for(int i = 0; i < numPassengers; i++)
				{
					g.drawImage(ArtManager.getImage("Person_Down"),BENCH_X+i*CityPanel.GRID_SIZE+spacing*i, BENCH_Y, CityPanel.GRID_SIZE, CityPanel.GRID_SIZE,null);
				}
			}
		}
	}

}