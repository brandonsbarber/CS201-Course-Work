package cs201.gui.structures.restaurant;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import cs201.gui.ArtManager;
import cs201.gui.Gui;
import cs201.gui.SimCity201;
import cs201.gui.StructurePanel;
import cs201.gui.roles.restaurant.Skyler.CustomerGuiSkyler;
import cs201.gui.roles.restaurant.Skyler.HostGuiSkyler;
import cs201.gui.roles.restaurant.Skyler.WaiterGuiSkyler;
import cs201.roles.restaurantRoles.Skyler.RestaurantHostRoleSkyler;

public class RestaurantAnimationPanelSkyler extends StructurePanel {
	
	private final int nTables = RestaurantHostRoleSkyler.NTABLES;
	private final int WINDOWX = 550;
    private final int WINDOWY = 550;
    
    public static final int xTable = 150;
    public static final int yTable = 250;
    
    private Image bufferImage;
    private Dimension bufferSize;
    private boolean pauseBool = false;

    public RestaurantAnimationPanelSkyler(int i, SimCity201 sc) {
		super(i, sc);
		
		setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(15, this );
    	timer.start();
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );
        
        for (int i=0; i<WINDOWX; i=i+32) {
        	for (int j=0; j<WINDOWY; j=j+32) {
        		g.drawImage(ArtManager.getImage("Skyler_Wood_Restaurant_Floor"), i, j, 32, 32, null);
        	}
        }
        
         /*g2.setColor(Color.WHITE);
        g2.fillRect(getWidth()-100, 0, 100, 100 ); //cook's area*/
        /*g2.setColor(Color.GRAY);
        g2.fillRect(getWidth()-100, 92, 100, 8 );//bottom wall
        g2.setColor(Color.GRAY);
        g2.fillRect(getWidth()-100, 0, 20, 92 );*///serving counter 
        /*g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, 90, 70 ); //customer waiting area
        
        g2..setColor(Color.WHITE);
        g2.fillRect(0, 97, 26, 106 );*/ //waiter home area
        for (int i=getWidth()-100; i<getWidth(); i=i+32) { //Cook's area
        	for (int j=0; j<85; j=j+32) {
        		g.drawImage(ArtManager.getImage("Skyler_Kitchen_Floor"), i, j, 32, 32, null);
        	}
        }
        
        g.drawImage(ArtManager.getImage("Skyler_Counter_Vert"), getWidth()-100, 0, 22, 101, null); //Serving Counter
        g.drawImage(ArtManager.getImage("Skyler_Counter_Horiz"), getWidth()-100+18, 72, 90, 29, null); //Bottom Wall
        
        g2.setColor(Color.GRAY);
        g2.fillRect(getWidth()-75, 3, 70, 20 ); //grill
        
        for (int i=-5; i < 80; i=i+32) {		//customer waiting area
        	for (int j=-20; j<60; j=j+32) {
             g.drawImage(ArtManager.getImage("Apartment_Complex_Floor2"), i, j, 32, 32, null);
        	}
         }
        
        for (int i=0; i < 26; i=i+32) {		//waiter home position
        	for (int j=100; j<202; j=j+32) {
             g.drawImage(ArtManager.getImage("Apartment_Complex_Floor3"), i, j, 32, 32, null);
        	}
         }
        
        for (int i=0; i < nTables; i++) {
           // g.setColor(Color.ORANGE);
           // g.fillRect(xTable+(100*i), yTable, 50, 50);//200 and 250 need to be table params
            g.drawImage(ArtManager.getImage("Skyler_Restaurant_Table"), xTable+(100*i)-10, yTable+23, 44, 43, null);
        }
        
        super.paintComponent(g);
        
    }
    
    public void pauseAnimation() {
    	pauseBool = true;
    }
    
    public void restartAnimation() {
    	pauseBool = false;
    }

}
