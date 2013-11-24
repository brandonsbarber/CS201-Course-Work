package cs201.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import cs201.structures.Structure;

public class CityPanel extends JPanel implements MouseListener
{
	private static final int GRID_SIZE = 25;

	ArrayList<Structure> buildings;
	
	private String[][] cityGrid = 
	{
			{"G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G"},
			{"G","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","G"},
			{"G","S","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","S","G"},
			{"G","S","C","S","S","S","S","C","S","S","S","S","C","S","S","S","S","R","S","G"},
			{"G","S","R","S","G","G","S","R","S","G","G","S","R","S","G","G","S","R","S","G"},
			{"G","S","R","S","G","G","S","R","S","G","G","S","R","S","G","G","S","R","S","G"},
			{"G","S","C","S","S","S","S","C","S","S","S","S","C","S","S","S","S","R","S","G"},
			{"G","S","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","S","G"},
			{"G","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","G"},
			{"G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G"}
	};
	
	
	public CityPanel() {
		buildings = new ArrayList<Structure>();
		
		addMouseListener(this);
	}
	
	public void paintComponent(Graphics g)
	{	
		Graphics2D g2 = (Graphics2D) g;
		Dimension bounds = getPreferredSize();
		
		g2.setColor(Color.LIGHT_GRAY.brighter().brighter());
		g2.fillRect(0,0,(int)bounds.getWidth(),(int)bounds.getHeight());
		
		for(int y = 0; y < cityGrid.length; y++)
		{
			for(int x = 0; x < cityGrid[y].length; x++)
			{
				if(cityGrid[y][x].equals("G"))
				{
					g2.setColor(Color.GREEN);
				}
				else if(cityGrid[y][x].equals("R"))
				{
					g2.setColor(Color.GRAY.darker());
				}
				else if(cityGrid[y][x].equals("C"))
				{
					g2.setColor(Color.GRAY.brighter());
				}
				else if(cityGrid[y][x].equals("S"))
				{
					g2.setColor(Color.GRAY.brighter().brighter().brighter().brighter());
				}
				
				g2.fillRect(x*GRID_SIZE, y*GRID_SIZE, GRID_SIZE, GRID_SIZE);
			}
		}
		
		g2.setColor(Color.BLACK);
		
		for (int i = 0; i < buildings.size(); i++) {
			Structure s = buildings.get(i);
			g2.fill(s);
		}
	}
	
	public void addStructure(Structure s) {
		buildings.add(s);
	}
	
	public ArrayList<Structure> getStructures() {
		return buildings;
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		for (int i = 0; i < buildings.size(); i++) {
			Structure s = buildings.get(i);
			if (s.contains(arg0.getX(), arg0.getY())) {
				s.displayStructure();
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
