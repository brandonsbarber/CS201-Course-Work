package cs201.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.black);
		
		for (int i = 0; i < buildings.size(); i++) {
			Structure s = buildings.get(i);
			g2.fill(s);
		}
	}
	
	public void addBuilding(Structure s) {
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
