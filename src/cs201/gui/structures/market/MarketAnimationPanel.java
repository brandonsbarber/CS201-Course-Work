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
    private final int DEFAULT_WINDOW_WIDTH = 500;
    private final int DEFAULT_WINDOW_HEIGHT = 500;
    
    private final int ANIMATION_LENGTH = 10;

    private List<Gui> guis = new ArrayList<Gui>();
    
    public MarketAnimationPanel(int width, int height) {
    	setSize(width, height);
        setVisible(true);
    }

    public MarketAnimationPanel() {
    	setSize(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
        setVisible(true);
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
        g2.fillRect(250, 350, 150, 40);
    	
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
