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
    private final int WINDOWY = 350;
    
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
        
        g2.setColor(Color.WHITE);
        g2.fillRect(getWidth()-100, 0, 100, 100 ); //cook's area
        g2.setColor(Color.GRAY);
        g2.fillRect(getWidth()-100, 92, 100, 8 );//bottom wall
        g2.setColor(Color.GRAY);
        g2.fillRect(getWidth()-100, 0, 20, 92 );//serving counter
        g2.setColor(Color.BLACK);
        g2.fillRect(getWidth()-75, 3, 70, 20 ); //grill
        
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, 90, 70 ); //customer waiting area
        
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 97, 26, 106 ); //waiter home area
        
        for (int i=0; i < nTables; i++) {
            g.setColor(Color.ORANGE);
            g.fillRect(xTable+(100*i), yTable, 50, 50);//200 and 250 need to be table params
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
