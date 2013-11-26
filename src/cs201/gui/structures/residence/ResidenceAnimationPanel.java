package cs201.gui.structures.residence;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import cs201.gui.Gui;
import cs201.gui.SimCity201;
import cs201.gui.StructurePanel;
import cs201.gui.roles.residence.ResidentGui;

public class ResidenceAnimationPanel extends StructurePanel {

	private final int WINDOWX = 500;
	private final int WINDOWY = 500;
	
	private final int bedWidth = 70;
	private final int bedHeight = 30;
	private final int bedX = 250;
	private final int bedY = 430;
	
	private final int entranceY = 250;
	
	private final int fridgeWidth = 30;
	private final int fridgeX = 250;
	private final int fridgeY = 10;

	private final int tableWidth = 40;
	private final int tableHeight = 70;
	private final int tableX = 70;
	private final int tableY = 30;
	
	public ResidenceAnimationPanel(int i, SimCity201 sc) {
		super(i, sc);
		// TODO Auto-generated constructor stub
		setSize(WINDOWX, WINDOWY);
		setMaximumSize(new Dimension(WINDOWX, WINDOWY));
		setMinimumSize(new Dimension(WINDOWX, WINDOWY));
		setVisible(true);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		
		g2.setColor(getBackground());
		g2.fillRect(0, 0, WINDOWX, WINDOWY); //clear the screen
		
		g2.setColor(Color.GRAY);
		g2.fillRect(fridgeX, fridgeY, fridgeWidth, fridgeWidth); // draws refrigerator
		
		g2.setColor(Color.GREEN);
		g2.fillRect(tableX, tableY, tableWidth, tableHeight); // draws dining table
		
		g2.setColor(Color.BLUE);
		g2.fillRect(bedX, bedY, bedWidth, bedHeight); // draws bed
		
		super.paintComponent(g);
	}
	
	public void informResident(ResidentGui gui) {
		gui.setBed(bedX, bedY);
		gui.setFridge(fridgeX, fridgeY);
		gui.setTable(tableX, tableY);
		gui.setExit(entranceY);
	}

}
