package cs201.gui.structures.market;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import cs201.gui.Gui;

public class MarketAnimationPanel extends JPanel implements ActionListener {

	/*
	 * ********** DATA & CONSTANTS **********
	 */
    private final static int DEFAULT_WINDOW_WIDTH = 500;
    private final static int DEFAULT_WINDOW_HEIGHT = 500;
        
    private final int FRONT_DESK_X = 250;
    private final int FRONT_DESK_Y = 350;
    private final int FRONT_DESK_WIDTH = 150;
    private final int FRONT_DESK_HEIGHT = 40;
    private final int FIRST_SHELF_X = 50;
    private final int FIRST_SHELF_Y = 50;
    private final int SHELF_WIDTH = 50;
    private final int SHELF_HEIGHT = 200;
    private final int SHELF_SPACING = 150;
    private final int SHELF_COUNT = 3;
    
    private final int ANIMATION_LENGTH = 10;
    private Timer timer;

    private List<Gui> guis = new ArrayList<Gui>();
    
    public MarketAnimationPanel(int width, int height) {
    	setSize(width, height);
        setVisible(true);
        
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
        g2.fillRect(FRONT_DESK_X, FRONT_DESK_Y, FRONT_DESK_WIDTH, FRONT_DESK_HEIGHT);
        
        // Draw the shelves
        g2.setColor(Color.blue);
        for (int i = 0; i < SHELF_COUNT; i ++) {
        	g2.fillRect(FIRST_SHELF_X + i * SHELF_SPACING, FIRST_SHELF_Y, SHELF_WIDTH, SHELF_HEIGHT);
        }
    	
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

    public void addGui(Gui gui) {
        guis.add(gui);
    }
}
