package cs201.gui.structures.residence;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import cs201.gui.Gui;

public class ApartmentComplexAnimationPanel extends JPanel implements
		ActionListener {

	private final int WINDOWX = 500;
	private final int WINDOWY = 500;
	
	private final int deskWidth = 70;
	private final int deskHeight = 40;
	private final int deskX = 70;
	private final int deskY = 30;
	
	private List<Gui> guis = new ArrayList<Gui>();
	
	public ApartmentComplexAnimationPanel() {
		// TODO Auto-generated constructor stub
		setSize(WINDOWX, WINDOWY);
		setVisible(true);
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
	    for(Gui gui : guis) {
	        if (gui.isPresent()) {
	            gui.updatePosition();
	            gui.draw(g2);
	        }
	    }

	}

	public void addGui(Gui gui) {
	    guis.add(gui);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
