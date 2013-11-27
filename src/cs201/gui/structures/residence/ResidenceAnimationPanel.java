package cs201.gui.structures.residence;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

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
	
	private final int fridgeWidth = 40;
	private final int fridgeX = 250;
	private final int fridgeY = 25;
	
	private final int fridgeDoorWidth = (fridgeWidth/2)-2;
	private final int fridgeDoorHeight = 6;
	private final int fridgeDoor1X = fridgeX+1;
	private final int fridgeDoorY = fridgeY+fridgeWidth-fridgeDoorHeight;
	private final int fridgeDoor2X = fridgeDoor1X+fridgeDoorWidth+2;
	

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
		g2.fillRect(fridgeX, fridgeY, fridgeWidth, fridgeWidth);
		g2.setColor(Color.WHITE);
		g2.fillRect(fridgeDoor1X, fridgeDoorY, fridgeDoorWidth, fridgeDoorHeight);
		g2.fillRect(fridgeDoor2X, fridgeDoorY, fridgeDoorWidth, fridgeDoorHeight);
		
		// draws refrigerator
		
		g2.setColor(Color.GREEN);
		g2.fillRect(tableX, tableY, tableWidth, tableHeight); // draws dining table
		
		g2.setColor(Color.BLUE);
		g2.fillRect(bedX, bedY, bedWidth, bedHeight); // draws bed
		
		g2.setColor(Color.WHITE);
		g2.drawString("Fridge", fridgeX, fridgeY+13);
		g2.drawString("Table", tableX, tableY+tableHeight);
		g2.drawString("Bed", bedX, bedY+bedHeight);
		
		super.paintComponent(g);
	}
	
	public void informResident(ResidentGui gui) {
		gui.setBed(bedX, bedY+(bedHeight/2));
		gui.setFridge(fridgeX, fridgeY+fridgeWidth);
		gui.setTable(tableX+tableWidth, tableY+(tableHeight/2));
		gui.setExit(entranceY);
	}

}
