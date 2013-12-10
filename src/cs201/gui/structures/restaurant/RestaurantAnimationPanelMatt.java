package cs201.gui.structures.restaurant;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import cs201.gui.ArtManager;
import cs201.gui.SimCity201;
import cs201.gui.StructurePanel;
import cs201.helper.Constants;

/**
 * Animation panel for Matthew Pohlmann's Restaurant in SimCity201. Changing the DEFAULTWINDOWX AND
 * DEFAULTWINDOWY member variables resizes everything inside the restaurant accordingly.
 * 
 * @author Matthew Pohlmann
 */
@SuppressWarnings("serial")
public class RestaurantAnimationPanelMatt extends StructurePanel {
	
	private static final int DEFAULTWINDOWX = 480;
	private static final int DEFAULTWINDOWY = 480;
    public static int WINDOWX = DEFAULTWINDOWX;
    public static int WINDOWY = DEFAULTWINDOWY;
    
    
    // Entrance/Exit
    public static final int RESTAURANT_ENTRANCE_X = -(int)(WINDOWX * .08f);
    public static final int RESTAURANT_ENTRANCE_Y = -(int)(WINDOWY * .08f);
    
    // TABLES
    public static final int TABLESIZE = (WINDOWX < WINDOWY) ? (int)(WINDOWX * .09f) : (int)(WINDOWY * .09f);
    private final Color TABLECOLOR = Color.ORANGE;
    public static final int TABLEX_1 = (int)(WINDOWX * 3 / 10);
    public static final int TABLEY_1 = (int)(WINDOWY * 3 / 10);
    public static final int TABLEX_2 = (int)(WINDOWX * 6 / 10);
    public static final int TABLEY_2 = (int)(WINDOWY * 3 / 10);
    public static final int TABLEX_3 = (int)(WINDOWX * 3 / 10);
    public static final int TABLEY_3 = (int)(WINDOWY * 6 / 10);
    public static final int TABLEX_4 = (int)(WINDOWX * 6 / 10);
    public static final int TABLEY_4 = (int)(WINDOWY * 6 / 10);
    
    // CASH REGISTER
    private final int CASHAREAMAIN_SIZE_X = (int)(WINDOWX * .035f);
    private final int CASHAREAMAIN_SIZE_Y = (int)(WINDOWY * .15f);
    private final int CASHAREAMAIN_X = (int)(WINDOWX * .10f);
    private final int CASHAREAMAIN_Y = (int)(WINDOWY * .465f);
    
    // COOKING AREA
    public static final int COOKINGAREA_X = (int)(WINDOWX * .50f);
    public static final int COOKINGAREA_Y = (int)(WINDOWY * .89f);
    public static final int FRIDGE_X = (int)(WINDOWX * .61f);
    public static final int FRIDGE_Y = (int)(WINDOWY * .95f);
    public static final int STOVES_X = (int)(WINDOWX * .55f);
    public static final int STOVES_Y = (int)(WINDOWY * .95f);
    private final Color COOKAREAMAINCOLOR = Color.GRAY;
    private final Color COOKAREAGRILLCOLOR = Color.RED;
    private final int COOKAREAMAIN_SIZE_X = (int)(WINDOWX * .33f);
    private final int COOKAREAMAIN_SIZE_Y = (int)(WINDOWY * .09f);
    private final int COOKAREAMAIN_X = (int)(WINDOWX * .5f - COOKAREAMAIN_SIZE_X * .5f);
    private final int COOKAREAMAIN_Y = (int)(WINDOWY * .86f);
    private final int COOKAREAGRILL_SIZE_X = (int)(WINDOWX * .06f);
    private final int COOKAREAGRILL_SIZE_Y = (int)(WINDOWY * .06f);
    private final int COOKAREAGRILL1_X = (int)(WINDOWX * .52f);
    private final int COOKAREAGRILL1_Y = COOKAREAMAIN_Y + (int)(WINDOWY * .01f);
    private final int COOKAREAGRILL2_X = (int)(WINDOWX * .59f);
    private final int COOKAREAGRILL2_Y = COOKAREAMAIN_Y + (int)(WINDOWY * .01f);
    
    // PLATING AREA
    public static final int PLATINGAREA_X = (int)(WINDOWX * .34f);
    public static final int PLATINGAREA_Y = (int)(WINDOWY * .89f);
    public static final int PLATING_X = (int)(WINDOWX * .38f);
    public static final int PLATING_Y = (int)(WINDOWY * .95f);
    private final Color PLATINGAREAMAINCOLOR = Color.YELLOW;
    private final int PLATINGAREA_SIZE = (int)(WINDOWX * .03f);
    private final int PLATINGAREA1_X = (int)(WINDOWX * .35f);
    private final int PLATINGAREA1_Y = COOKAREAMAIN_Y + (int)(WINDOWY * .01f);
    private final int PLATINGAREA2_X = (int)(WINDOWX * .39f);
    private final int PLATINGAREA3_X = (int)(WINDOWX * .43f);

    public RestaurantAnimationPanelMatt(int i, SimCity201 sc) {
    	super(i, sc);
    	
    	setSize(WINDOWX, WINDOWY);
		setMinimumSize(new Dimension(WINDOWX, WINDOWY));
		setMaximumSize(new Dimension(WINDOWX, WINDOWY));
		setPreferredSize(new Dimension(WINDOWX, WINDOWY));
        setVisible(true);
    }
    
    public RestaurantAnimationPanelMatt(int i, SimCity201 sc, int x, int y) {
    	super(i, sc);
    	
    	WINDOWX = x;
    	WINDOWY = y;
    	setSize(WINDOWX, WINDOWY);
		setMinimumSize(new Dimension(WINDOWX, WINDOWY));
		setMaximumSize(new Dimension(WINDOWX, WINDOWY));
		setPreferredSize(new Dimension(WINDOWX, WINDOWY));
        setVisible(true);
    }

	@Override
    public void paintComponent(Graphics g) {
    	Graphics2D g2 = (Graphics2D)g;
        
        if (Constants.DEBUG_MODE) {
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
              //Grill 1
            g2.setColor(COOKAREAGRILLCOLOR);
            g2.fillRect(COOKAREAGRILL1_X, COOKAREAGRILL1_Y, COOKAREAGRILL_SIZE_X, COOKAREAGRILL_SIZE_Y);
              //Grill 2
            g2.setColor(COOKAREAGRILLCOLOR);
            g2.fillRect(COOKAREAGRILL2_X, COOKAREAGRILL2_Y, COOKAREAGRILL_SIZE_X, COOKAREAGRILL_SIZE_Y);
              //CookingAreaLabel
            g2.setColor(Color.BLACK);
            g2.drawString("Cooking:", COOKINGAREA_X, COOKINGAREA_Y);
            
            //Plating Area
              //Plate 1
            g2.setColor(PLATINGAREAMAINCOLOR);
            g2.fillRect(PLATINGAREA1_X, PLATINGAREA1_Y, PLATINGAREA_SIZE, PLATINGAREA_SIZE);
              //Plate 2
            g2.setColor(PLATINGAREAMAINCOLOR);
            g2.fillRect(PLATINGAREA2_X, PLATINGAREA1_Y, PLATINGAREA_SIZE, PLATINGAREA_SIZE);
              //Plate 3
            g2.setColor(PLATINGAREAMAINCOLOR);
            g2.fillRect(PLATINGAREA3_X, PLATINGAREA1_Y, PLATINGAREA_SIZE, PLATINGAREA_SIZE);
              //PlatingAreaLabel
            g2.setColor(Color.BLACK);
            g2.drawString(":Plating", PLATINGAREA_X, PLATINGAREA_Y);
        } else {
        	g.drawImage(ArtManager.getImage("Floor"), 0, 0, WINDOWX, WINDOWY, null);
        	
        	g.drawImage(ArtManager.getImage("Table"), TABLEX_1, TABLEY_1, TABLESIZE, TABLESIZE, null);
        	g.drawImage(ArtManager.getImage("Table"), TABLEX_2, TABLEY_2, TABLESIZE, TABLESIZE, null);
        	g.drawImage(ArtManager.getImage("Table"), TABLEX_3, TABLEY_3, TABLESIZE, TABLESIZE, null);
        	g.drawImage(ArtManager.getImage("Table"), TABLEX_4, TABLEY_4, TABLESIZE, TABLESIZE, null);
        	
        	g.drawImage(ArtManager.getImage("Kitchen"), COOKAREAMAIN_X, COOKAREAMAIN_Y, COOKAREAMAIN_SIZE_X, COOKAREAMAIN_SIZE_Y, null);
        	g.drawImage(ArtManager.getImage("Cash_Register"), CASHAREAMAIN_X, CASHAREAMAIN_Y, CASHAREAMAIN_SIZE_X, CASHAREAMAIN_SIZE_Y, null);
        }
        
        super.paintComponent(g);
    }
}
