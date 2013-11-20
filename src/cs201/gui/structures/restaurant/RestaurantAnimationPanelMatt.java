package cs201.gui.structures.restaurant;

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

public class RestaurantAnimationPanelMatt extends JPanel implements ActionListener {

    private final int WINDOWX = 500;
    private final int WINDOWY = 500;
    
    // TABLES
    private final int TABLESIZE = 50;
    private final Color TABLECOLOR = Color.ORANGE;
    public static final int TABLEX_1 = 150;
    public static final int TABLEY_1 = 150;
    public static final int TABLEX_2 = 300;
    public static final int TABLEY_2 = 150;
    public static final int TABLEX_3 = 150;
    public static final int TABLEY_3 = 300;
    public static final int TABLEX_4 = 300;
    public static final int TABLEY_4 = 300;
    
    // COOKING AREA
    public static final int COOKINGAREA_X = 278;
    public static final int COOKINGAREA_Y = 425;
    private final Color COOKAREAMAINCOLOR = Color.GRAY;
    private final Color COOKAREAGRILLCOLOR = Color.RED;
    private final int COOKAREAMAIN_SIZE_X = 160;
    private final int COOKAREAMAIN_SIZE_Y = 40;
    private final int COOKAREAMAIN_X = 250 - COOKAREAMAIN_SIZE_X / 2;
    private final int COOKAREAMAIN_Y = 430;
    private final int COOKAREAGRILL_SIZE_X = 30;
    private final int COOKAREAGRILL_SIZE_Y = 30;
    private final int COOKAREAGRILL1_X = 260;
    private final int COOKAREAGRILL1_Y = COOKAREAMAIN_Y + 5;
    private final int COOKAREAGRILL2_X = 295;
    private final int COOKAREAGRILL2_Y = COOKAREAMAIN_Y + 5;
    
    // PLATING AREA
    public static final int PLATINGAREA_X = 170;
    public static final int PLATINGAREA_Y = 425;
    private final Color PLATINGAREAMAINCOLOR = Color.YELLOW;
    private final int PLATINGAREA_SIZE = 15;
    private final int PLATINGAREA1_X = 175;
    private final int PLATINGAREA1_Y = COOKAREAMAIN_Y + 5;
    private final int PLATINGAREA2_X = 195;
    private final int PLATINGAREA3_X = 215;
    
    private final int ANIMATIONLENGTH = 10;
    private Timer timer;

    private List<Gui> guis = new ArrayList<Gui>();

    public RestaurantAnimationPanelMatt() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
 
        timer = new Timer(ANIMATIONLENGTH, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}
	
	public void toggleTimer() {
		if (timer.isRunning()) {
			timer.stop();
		} else {
			timer.start();
		}
	}

    public void paintComponent(Graphics g) {
    	Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );

        //Here is table 1
        g2.setColor(TABLECOLOR);
        g2.fillRect(TABLEX_1, TABLEY_1, TABLESIZE, TABLESIZE);
        
        //Here is table 2
        g2.setColor(TABLECOLOR);
        g2.fillRect(TABLEX_2, TABLEY_2, TABLESIZE, TABLESIZE);
        
        //Here is table 3
        g2.setColor(TABLECOLOR);
        g2.fillRect(TABLEX_3, TABLEY_3, TABLESIZE, TABLESIZE);
        
        //Here is table 4
        g2.setColor(TABLECOLOR);
        g2.fillRect(TABLEX_4, TABLEY_4, TABLESIZE, TABLESIZE);
        
        //Cooking Area
          //Main
        g2.setColor(COOKAREAMAINCOLOR);
        g2.fillRect(COOKAREAMAIN_X, COOKAREAMAIN_Y, COOKAREAMAIN_SIZE_X, COOKAREAMAIN_SIZE_Y);
          //Grill1
        g2.setColor(COOKAREAGRILLCOLOR);
        g2.fillRect(COOKAREAGRILL1_X, COOKAREAGRILL1_Y, COOKAREAGRILL_SIZE_X, COOKAREAGRILL_SIZE_Y);
          //Grill2
        g2.setColor(COOKAREAGRILLCOLOR);
        g2.fillRect(COOKAREAGRILL2_X, COOKAREAGRILL2_Y, COOKAREAGRILL_SIZE_X, COOKAREAGRILL_SIZE_Y);
          //CookingAreaLabel
        g2.setColor(Color.BLACK);
        g2.drawString("Cooking:", COOKINGAREA_X, COOKINGAREA_Y);
        
        //Plating Area
          //Plate1
        g2.setColor(PLATINGAREAMAINCOLOR);
        g2.fillRect(PLATINGAREA1_X, PLATINGAREA1_Y, PLATINGAREA_SIZE, PLATINGAREA_SIZE);
          //Plate2
        g2.setColor(PLATINGAREAMAINCOLOR);
        g2.fillRect(PLATINGAREA2_X, PLATINGAREA1_Y, PLATINGAREA_SIZE, PLATINGAREA_SIZE);
          //Plate3
        g2.setColor(PLATINGAREAMAINCOLOR);
        g2.fillRect(PLATINGAREA3_X, PLATINGAREA1_Y, PLATINGAREA_SIZE, PLATINGAREA_SIZE);
          //PlatingAreaLabel
        g2.setColor(Color.BLACK);
        g2.drawString(":Plating", PLATINGAREA_X, PLATINGAREA_Y);
        
        
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }

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
