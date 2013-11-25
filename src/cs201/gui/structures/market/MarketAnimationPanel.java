package cs201.gui.structures.market;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.swing.JPanel;
import javax.swing.Timer;

import cs201.gui.Gui;

public class MarketAnimationPanel extends JPanel implements ActionListener {

	/*
	 * ********** DATA & CONSTANTS **********
	 */
    private final static int DEFAULT_WINDOW_WIDTH = 500;
    private final static int DEFAULT_WINDOW_HEIGHT = 500;
        
    public final static int FIRST_SHELF_X = 50;
    public final static int FIRST_SHELF_Y = 50;
    public final static int SHELF_WIDTH = 50;
    public final static int SHELF_HEIGHT = 200;
    public final static int SHELF_SPACING = 150;
    public final static int SHELF_COUNT = 3;
    
    private final int ANIMATION_LENGTH = 10;
    private Timer timer;

    private List<Gui> guis = new ArrayList<Gui>();
    
    /*
     * ********** A* IMPLEMENTATION **********
     */
    
    // Set up our grid
    static int gridWidth = 20;
    static int gridHeight = 20;
    final int gridPixelWidth = DEFAULT_WINDOW_WIDTH / gridWidth;
    final int gridPixelHeight = DEFAULT_WINDOW_HEIGHT / gridHeight;
    
    // Set up our grid of semaphores
    Semaphore[][] grid = new Semaphore[gridWidth + 1][gridHeight + 1]; 
    
    public Semaphore[][] getAStarGrid() {
    	return grid;
    }
  	
    /*
     * ********** CONSTRUCTORS **********
     */
    
    public MarketAnimationPanel(int width, int height) {
    	// Set the properties of the JPanel
    	setSize(width, height);
        setVisible(true);
        
        // Initialize the semaphore grid
      	for (int i = 0; i < gridWidth + 1; i++) {
      	    for (int j = 0; j < gridHeight + 1; j++) {
      	    	grid[i][j]=new Semaphore(1,true);
      	    }
      	}
      	
      	// Make the 0-th row and column unavailable
      	try {
      		for (int i = 0; i < gridHeight + 1; i++) grid[0][0+i].acquire();
      		for (int i = 1; i < gridWidth + 1; i++) grid[0+i][0].acquire();
      	} catch (Exception e) {
      		System.out.println("Unexcepted error occured in MarketAnimationPanel constructor: " + e);
      	}
      	
      	// Make shelves and walls unavailable
      	for (int i = 3; i <= 18; i += 3) {
        	acquireRectangleGrid(i, 5, i, 9);
        }
      	acquireRectangleGrid(3, 3, 18, 3);
      	acquireRectangleGrid(3, 11, 12, 11);
      	acquireRectangleGrid(15, 11, 18, 11);
        acquireRectangleGrid(10, 15, 15, 15);
        
      	// Create a timer to control the animation
        timer = new Timer(ANIMATION_LENGTH, this );
    	timer.start();
    }

    public MarketAnimationPanel() {
    	this(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called                       
	}                                                                       
                                                          
                                                                            
    public void paintComponent(Graphics g) {
    	Graphics2D g2 = (Graphics2D)g;
    	
        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT );
    	
    	// Draw the front desk bottom right of the screen
        g2.setColor(Color.black);
        drawRectangleGrid(10, 15, 15, 15, g2);
        
        // Draw the shelves
        g2.setColor(Color.blue);
        for (int i = 3; i <= 18; i += 3) {
        	drawRectangleGrid(i, 5, i, 9, g2);
        }
        
        // Draw the walls
        g2.setColor(Color.gray);
        drawRectangleGrid(3, 3, 18, 3, g2);
        drawRectangleGrid(3, 11, 12, 11, g2);
        drawRectangleGrid(15, 11, 18, 11, g2);
    	
        // Tell each gui to update
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }

        // Draw each gui
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
    }
    
    private void drawRectangleGrid(int leftX, int topY, int rightX, int bottomY, Graphics2D context) {
    	for (int x = leftX - 1; x <= rightX - 1; x++) {
    		for (int y = topY - 1; y <= bottomY - 1; y++) {
    			context.fillRect(gridPixelWidth * x, gridPixelHeight * y, gridPixelWidth, gridPixelHeight);
    		}
    	}
    }
    
    private void acquireRectangleGrid(int left, int top, int right, int bottom) {
    	for (int x = left; x <= right; x++) {
    		for (int y = top; y <= bottom; y++) {
    			//System.out.println("Acquiring (" + x + "," + y +")");
    			try {
					grid[x][y].acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
    }

    public void addGui(Gui gui) {
        guis.add(gui);
    }
}