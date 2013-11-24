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

public class CityPanel extends JPanel implements MouseListener {
	ArrayList<Structure> buildings;
	
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
		
		g2.setColor(Color.BLACK);
		
		for(int i = 0; i <= 250; i+=50)
		{
			g2.drawLine(0, i, 1000, i);
		}
		
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
