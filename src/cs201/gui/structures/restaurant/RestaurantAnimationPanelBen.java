package cs201.gui.structures.restaurant;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Timer;

import cs201.gui.ArtManager;
import cs201.gui.SimCity201;
import cs201.gui.StructurePanel;
import cs201.gui.roles.restaurant.Ben.CustomerGuiBen;
import cs201.helper.Constants;
import cs201.roles.restaurantRoles.Ben.RestaurantHostRoleBen;

public class RestaurantAnimationPanelBen extends StructurePanel {

    private final int WINDOWX = 1000;
    private final int WINDOWY = 600;
    private Image bufferImage;
    private Dimension bufferSize;
    
    public static final int TABLEWIDTH 			= 50;
    public static final int TABLEHEIGHT 		= 50;
    public static final int TABLEXPOS 			= 100;	// the start x position of the tables
    public static final int TABLEYPOS 			= 200;	// the start y position of the tables
    public static final int TABLEPAD 			= 20;
    public static final int TIMERDELAY 			= 10;
    public static final int PLATINGAREAX 		= 100;
    public static final int PLATINGAREAY 		= 25;
    public static final int COOKINGAREAX 		= 300;
    public static final int COOKINGAREAY 		= 25;

    // Get the total number of tables from the Host Agent
    int numTables = RestaurantHostRoleBen.NTABLES;
    
    // The plating area
    public PlateQueue platingArea = new PlateQueue(PLATINGAREAX, PLATINGAREAY);
    public PlateQueue cookingArea = new PlateQueue(COOKINGAREAX, COOKINGAREAY);

    // The list of customer waiting spots
    public List<CustomerGuiBen> customerWaitingSpots = new ArrayList<CustomerGuiBen>();
    
    public RestaurantAnimationPanelBen(int i, SimCity201 sc, int x, int y) {
    	super(i, sc);
    	
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        //setBackground(Color.black);
        
        bufferSize = this.getSize();
        
    	Timer timer = new Timer(TIMERDELAY, this );
    	timer.start();
    }

    /*
	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}
	*/

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        
        if (Constants.DEBUG_MODE) {
            //Clear the screen by painting a rectangle the size of the frame
            g2.setColor(getBackground());
            g2.fillRect(0, 0, WINDOWX, WINDOWY );

	        //Here is the table
	        g2.setColor(Color.ORANGE);
	        for (int i = 0; i < numTables; i++) {
	            g2.fillRect(TABLEXPOS + (TABLEWIDTH + TABLEPAD) * i, TABLEYPOS, TABLEWIDTH, TABLEHEIGHT);
	        }
	        
	        // Here is the plating area
	        platingArea.draw(g2);
	        
	        // Here is the cooking area
	        cookingArea.draw(g2);
        
        } else {
        	// Draw the floor
        	g.drawImage(ArtManager.getImage("Restaurant_Ben_Floor"), 0, 0, null);
        	
        	// Draw the table
        	for (int i = 0; i < numTables; i++) {
            	g.drawImage(ArtManager.getImage("Restaurant_Ben_Table"), TABLEXPOS + (TABLEWIDTH + TABLEPAD) * i, TABLEYPOS, null);
            }
        	
        }

        super.paintComponent(g);
    }
    
    public class PlateQueue {
    	int xPos, yPos, width, height;
    	List<String> queue = Collections.synchronizedList( new ArrayList<String>() );
    	
    	PlateQueue(int x, int y) {
    		xPos = x;
    		yPos = y;
    		width = 150;
    		height = 25;
    	}
    	
    	void draw(Graphics2D g) {
    		// Draw the border
            g.setColor(Color.WHITE);
            g.fillRect(xPos, yPos, width, height);
            
            // Draw the items
            int x = xPos, y = yPos + height / 2 + 5;
            synchronized(queue) {
	            for (int i = 0; i < queue.size(); i++) {
	            	x = xPos + 10 + i * 25;
	            	
	            	g.setColor(Color.BLACK);
	            	g.drawString(queue.get(i), x, y);
	            }
            }
    	}
    	
    	public void addItem(String s) {
    		synchronized(queue) {
    			queue.add(s);
    		}
    	}
    	
    	public void removeItem(String s) {
    		synchronized(queue) {
    			queue.remove(s);
    		}
    	}
    }
}
