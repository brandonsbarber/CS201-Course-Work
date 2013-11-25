package cs201.gui.structures.residence;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import cs201.gui.Gui;
import cs201.gui.SimCity201;
import cs201.gui.StructurePanel;

public class ResidenceAnimationPanel extends StructurePanel {

	private final int WINDOWX = 500;
	private final int WINDOWY = 500;
	
	private final int bedWidth = 70;
	private final int bedHeight = 30;
	private final int bedX = 100;
	private final int bedY = 100;
	
	
	private final int fridgeWidth = 30;
	private final int fridgeX = 300;
	private final int fridgeY = 300;
	
	private final int tableWidth = 70;
	private final int tableHeight = 40;
	private final int tableX = 70;
	private final int tableY = 30;
	
	
	private List<Gui> guis = new ArrayList<Gui>();
	
	public ResidenceAnimationPanel(int i, SimCity201 sc) {
		super(i, sc);
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
