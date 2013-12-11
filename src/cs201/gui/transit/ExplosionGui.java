package cs201.gui.transit;

import java.awt.Graphics2D;

import cs201.gui.ArtManager;
import cs201.gui.CityPanel;
import cs201.gui.Gui;

public class ExplosionGui implements Gui
{
	int x;
	int y;
	int frame;
	int subframe;
	
	public static final int MAX_FRAME = 8;
	public static final int MAX_SUBFRAME = 10;
	
	public ExplosionGui(int x, int y)
	{
		this.x = x;
		this.y = y;
		
		frame = 0;
		subframe = 0;
	}

	@Override
	public void updatePosition()
	{
		if(subframe++ > MAX_SUBFRAME)
		{
			subframe = 0;
			frame++;
		}
	}

	@Override
	public void draw(Graphics2D g)
	{
		g.drawImage(ArtManager.getImage("Explosion_"+frame), x-CityPanel.GRID_SIZE, y-CityPanel.GRID_SIZE, CityPanel.GRID_SIZE*3, CityPanel.GRID_SIZE*3,null);
	}

	@Override
	public boolean isPresent()
	{
		return frame < MAX_FRAME;
	}

	@Override
	public void setPresent(boolean present)
	{
		
	}
}
