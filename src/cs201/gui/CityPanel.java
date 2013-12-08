package cs201.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import cs201.helper.CityDirectory;
import cs201.helper.Constants;
import cs201.helper.transit.MapParser;
import cs201.helper.transit.MovementDirection;
import cs201.structures.Structure;

@SuppressWarnings("serial")
public class CityPanel extends JPanel implements MouseListener, ActionListener
{
	
	public MovementDirection[][] drivingMap;
	public MovementDirection[][] walkingMap;
	
	public static final int GRID_SIZE = 25;

	public static CityPanel INSTANCE = null;
	
	List<Structure> buildings;
	
	List<Gui> guis;
	
	private String[][] cityGrid;
	private SimCity201 city;

	/**
	 * Creates a city panel and makes it the sole instance in the program.
	 * Sets up initial data too
	 */
	public CityPanel(SimCity201 city)
	{
		Timer timer = new Timer(1000/240,this);
		this.city = city;
		
		try
		{
			cityGrid = MapParser.parseMap();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		buildings = Collections.synchronizedList(new ArrayList<Structure>());
		guis = Collections.synchronizedList(new ArrayList<Gui>());
		
		if(INSTANCE == null)
		{
			INSTANCE = this;
		}
		
		addMouseListener(this);
		
		populateDrivingMap();

		timer.start();
	}
	
	/**
	 * Adds a gui for rendering
	 * @param gui the gui to add
	 */
	public void addGui(Gui gui)
	{
		guis.add(gui);
	}
	
	/**
	 * Makes driving and walking maps from map data
	 */
	private void populateDrivingMap()
	{
		drivingMap = new MovementDirection[cityGrid.length][cityGrid[0].length];
		walkingMap = new MovementDirection[cityGrid.length][cityGrid[0].length];
		
		for(int y = 0; y < cityGrid.length; y++)
		{
			for(int x = 0; x < cityGrid[y].length; x++)
			{
				MovementDirection dir = MovementDirection.None;
				MovementDirection wDir = MovementDirection.None;
				
				if(Character.isDigit(cityGrid[y][x].charAt(0)))
				{
					int val = Integer.parseInt(cityGrid[y][x].substring(0,1));
					switch(val)
					{
						case 1:dir = MovementDirection.Up;break;
						case 2:dir = MovementDirection.Right;break;
						case 3:dir = MovementDirection.Down;break;
						case 4:dir = MovementDirection.Left;break;
						default:dir = MovementDirection.None;break;
					}
					
					if(cityGrid[y][x].length() == 2)
					{
						char second = cityGrid[y][x].charAt(1);
						switch(second)
						{
							case 'H': wDir = MovementDirection.Horizontal;break;
							case 'V': wDir = MovementDirection.Vertical;break;
							default : wDir = MovementDirection.None;break;
						}
					}
				}
				else
				{
					if(cityGrid[y][x].equals("H"))
					{
						wDir = MovementDirection.Horizontal;
					}
					if(cityGrid[y][x].equals("V"))
					{
						wDir = MovementDirection.Vertical;
					}
					if(cityGrid[y][x].equals("T"))
					{
						dir = MovementDirection.Turn;
					}
					if(cityGrid[y][x].equals("ST"))
					{
						wDir = MovementDirection.Turn;
					}
				}
				
				drivingMap[y][x] = dir;
				walkingMap[y][x] = wDir;
			}
		}
	}

	/**
	 * Paints the Panel with the city, elements, and grid is debug is turned on
	 */
	public void paintComponent(Graphics g)
	{	
		Graphics2D g2 = (Graphics2D) g;
		Dimension bounds = getPreferredSize();
		
		g2.setColor(getBackground());
		g2.fillRect(0,0,(int)bounds.getWidth(),(int)bounds.getHeight());
		
		for(int y = 0; y < cityGrid.length; y++)
		{
			for(int x = 0; x < cityGrid[y].length; x++)
			{
				if(cityGrid[y][x].equals("G"))
				{
					g2.setColor(Color.GREEN);
				}
				else if(cityGrid[y][x].equals("ST") || cityGrid[y][x].equals("V") ||  cityGrid[y][x].equals("H"))
				{
					g2.setColor(Color.GRAY.brighter().brighter());
				}
				else if(cityGrid[y][x].equals("T") || Character.isDigit(cityGrid[y][x].charAt(0)))
				{
					g2.setColor(Color.GRAY.darker());
				}
				
				g2.fillRect(x*GRID_SIZE, y*GRID_SIZE, GRID_SIZE, GRID_SIZE);
				
				if(cityGrid[y][x].length() == 2 && (cityGrid[y][x].charAt(1) == 'H' || cityGrid[y][x].charAt(1) == 'V'))
				{
					g2.setColor(Color.GRAY.brighter());
					for(int i = 0; i < 5 ; i++)
					{
						if(i % 2 == 0)
						{
							if(drivingMap[y][x].isVertical())
							{
								g2.fillRect(x*GRID_SIZE+i*5,y*GRID_SIZE,5,GRID_SIZE);
							}
							else
							{
								g2.fillRect(x*GRID_SIZE,y*GRID_SIZE+i*5,GRID_SIZE,5);
							}
						}
					}
				}
				
				if(Constants.DEBUG_MODE)
				{
					g2.setColor(Color.BLACK);
					g2.drawString(cityGrid[y][x], x*GRID_SIZE, y*GRID_SIZE+GRID_SIZE);
					
					if(drivingMap[y][x].isValid())
					{
						if(drivingMap[y][x] == MovementDirection.Up)
						{
							g2.setColor(Color.BLACK);
							g2.drawLine((int)((1.0*x+.5)*GRID_SIZE),(int)((1.0*y+.25)*GRID_SIZE), (int)((1.0*x+.5)*GRID_SIZE), (int)((1.0*y+.75)*GRID_SIZE));
							g2.drawLine((int)((1.0*x+.5)*GRID_SIZE),(int)((1.0*y+.25)*GRID_SIZE), (int)((1.0*x)*GRID_SIZE), (int)((1.0*y+.5)*GRID_SIZE));
							g2.drawLine((int)((1.0*x+.5)*GRID_SIZE),(int)((1.0*y+.25)*GRID_SIZE), (int)((1.0*x+1)*GRID_SIZE), (int)((1.0*y+.5)*GRID_SIZE));
	
						}
						else if(drivingMap[y][x] == MovementDirection.Down)
						{
							g2.setColor(Color.BLACK);
							g2.drawLine((int)((1.0*x+.5)*GRID_SIZE),(int)((1.0*y+.25)*GRID_SIZE), (int)((1.0*x+.5)*GRID_SIZE), (int)((1.0*y+.75)*GRID_SIZE));
							
							g2.drawLine((int)((1.0*x+.5)*GRID_SIZE),(int)((1.0*y+.75)*GRID_SIZE), (int)((1.0*x)*GRID_SIZE), (int)((1.0*y+.5)*GRID_SIZE));
							g2.drawLine((int)((1.0*x+.5)*GRID_SIZE),(int)((1.0*y+.75)*GRID_SIZE), (int)((1.0*x+1)*GRID_SIZE), (int)((1.0*y+.5)*GRID_SIZE));
						}
						else if(drivingMap[y][x] == MovementDirection.Left)
						{
							g2.setColor(Color.BLACK);
							g2.drawLine((int)((1.0*x+.25)*GRID_SIZE),(int)((1.0*y+.5)*GRID_SIZE), (int)((1.0*x+.75)*GRID_SIZE), (int)((1.0*y+.5)*GRID_SIZE));
							g2.drawLine((int)((1.0*x+.25)*GRID_SIZE),(int)((1.0*y+.5)*GRID_SIZE), (int)((1.0*x+.5)*GRID_SIZE), (int)((1.0*y+1)*GRID_SIZE));
							g2.drawLine((int)((1.0*x+.25)*GRID_SIZE),(int)((1.0*y+.5)*GRID_SIZE), (int)((1.0*x+.5)*GRID_SIZE), (int)((1.0*y)*GRID_SIZE));
						}
						else if(drivingMap[y][x] == MovementDirection.Right)
						{
							g2.setColor(Color.BLACK);
							g2.drawLine((int)((1.0*x+.25)*GRID_SIZE),(int)((1.0*y+.5)*GRID_SIZE), (int)((1.0*x+.75)*GRID_SIZE), (int)((1.0*y+.5)*GRID_SIZE));
							g2.drawLine((int)((1.0*x+.75)*GRID_SIZE),(int)((1.0*y+.5)*GRID_SIZE), (int)((1.0*x+.5)*GRID_SIZE), (int)((1.0*y+1)*GRID_SIZE));
							g2.drawLine((int)((1.0*x+.75)*GRID_SIZE),(int)((1.0*y+.5)*GRID_SIZE), (int)((1.0*x+.5)*GRID_SIZE), (int)((1.0*y)*GRID_SIZE));
						}
					}
					if(walkingMap[y][x].isValid())
					{
						if(walkingMap[y][x] == MovementDirection.Up)
						{
							g2.setColor(Color.BLACK);
							g2.drawLine((int)((1.0*x+.5)*GRID_SIZE),(int)((1.0*y+.25)*GRID_SIZE), (int)((1.0*x+.5)*GRID_SIZE), (int)((1.0*y+.75)*GRID_SIZE));
							g2.drawLine((int)((1.0*x+.5)*GRID_SIZE),(int)((1.0*y+.25)*GRID_SIZE), (int)((1.0*x)*GRID_SIZE), (int)((1.0*y+.5)*GRID_SIZE));
							g2.drawLine((int)((1.0*x+.5)*GRID_SIZE),(int)((1.0*y+.25)*GRID_SIZE), (int)((1.0*x+1)*GRID_SIZE), (int)((1.0*y+.5)*GRID_SIZE));
	
						}
						else if(walkingMap[y][x] == MovementDirection.Down)
						{
							g2.setColor(Color.BLACK);
							g2.drawLine((int)((1.0*x+.5)*GRID_SIZE),(int)((1.0*y+.25)*GRID_SIZE), (int)((1.0*x+.5)*GRID_SIZE), (int)((1.0*y+.75)*GRID_SIZE));
							
							g2.drawLine((int)((1.0*x+.5)*GRID_SIZE),(int)((1.0*y+.75)*GRID_SIZE), (int)((1.0*x)*GRID_SIZE), (int)((1.0*y+.5)*GRID_SIZE));
							g2.drawLine((int)((1.0*x+.5)*GRID_SIZE),(int)((1.0*y+.75)*GRID_SIZE), (int)((1.0*x+1)*GRID_SIZE), (int)((1.0*y+.5)*GRID_SIZE));
						}
						else if(walkingMap[y][x] == MovementDirection.Left)
						{
							g2.setColor(Color.BLACK);
							g2.drawLine((int)((1.0*x+.25)*GRID_SIZE),(int)((1.0*y+.5)*GRID_SIZE), (int)((1.0*x+.75)*GRID_SIZE), (int)((1.0*y+.5)*GRID_SIZE));
							g2.drawLine((int)((1.0*x+.25)*GRID_SIZE),(int)((1.0*y+.5)*GRID_SIZE), (int)((1.0*x+.5)*GRID_SIZE), (int)((1.0*y+1)*GRID_SIZE));
							g2.drawLine((int)((1.0*x+.25)*GRID_SIZE),(int)((1.0*y+.5)*GRID_SIZE), (int)((1.0*x+.5)*GRID_SIZE), (int)((1.0*y)*GRID_SIZE));
						}
						else if(walkingMap[y][x] == MovementDirection.Right)
						{
							g2.setColor(Color.BLACK);
							g2.drawLine((int)((1.0*x+.25)*GRID_SIZE),(int)((1.0*y+.5)*GRID_SIZE), (int)((1.0*x+.75)*GRID_SIZE), (int)((1.0*y+.5)*GRID_SIZE));
							g2.drawLine((int)((1.0*x+.75)*GRID_SIZE),(int)((1.0*y+.5)*GRID_SIZE), (int)((1.0*x+.5)*GRID_SIZE), (int)((1.0*y+1)*GRID_SIZE));
							g2.drawLine((int)((1.0*x+.75)*GRID_SIZE),(int)((1.0*y+.5)*GRID_SIZE), (int)((1.0*x+.5)*GRID_SIZE), (int)((1.0*y)*GRID_SIZE));
						}
					}
				}
			}
		}
		
		if(Constants.DEBUG_MODE)
		{
			g2.setColor(Color.BLACK);
			
			for(int x = 0; x < cityGrid[0].length; x++)
			{
				for(int y = 0; y < cityGrid.length; y++)
				{
					g2.drawRect(x*GRID_SIZE, y*GRID_SIZE, GRID_SIZE, GRID_SIZE);
					if(y == 1)
					{
						g2.drawString(""+x,x*GRID_SIZE,y*GRID_SIZE);
					}
					if(x == 0)
					{
						g2.drawString(""+y, x, (y+1)*GRID_SIZE);
					}
				}
				
			}
		}
		
		for (int i = 0; i < buildings.size(); i++)
		{			
			if(Constants.DEBUG_MODE)
			{
				g2.setColor(Color.BLACK);
				Structure r = buildings.get(i);
				Rectangle s = r.getRect();
				g2.fill(s);
				
				g2.setColor(Color.WHITE);
				g2.drawString("" + r.getId(), (int) s.x, (int) (s.y + s.height));
				
				g2.setColor(Color.BLUE);
				g2.fill(new Rectangle(r.getParkingLocation().x,r.getParkingLocation().y,GRID_SIZE,GRID_SIZE));
				g2.fill(new Rectangle(r.getEntranceLocation().x,r.getEntranceLocation().y,GRID_SIZE,GRID_SIZE));
				
				g2.setColor(Color.WHITE);
				g2.drawString("P",r.getParkingLocation().x,r.getParkingLocation().y+25);
				g2.drawString("E",r.getEntranceLocation().x,r.getEntranceLocation().y+25);
				
				g2.setColor(Color.WHITE);
				g2.drawString(r.getOpen() ? "Open" : "Closed", s.x, s.y + s.height / 2);
			} else {
				Structure s = buildings.get(i);
				Rectangle r = s.getRect();
				g2.drawImage(s.getSprite(), r.x, r.y, r.width, r.height, null);
			}
		}
		
		try
		{
			for(Gui gui: guis)
			{
				if(gui.isPresent())
				{
					gui.updatePosition();
					gui.draw(g2);
				}
			}
		}
		catch(ConcurrentModificationException e)
		{
			
		}
		
		
		g2.setColor(Color.BLACK);
		Font font = new Font(Font.SANS_SERIF, Font.BOLD, 17);
		g2.setFont(font);
		g2.drawString("Current Time: " + CityDirectory.getInstance().getTime().toString(), bounds.width / 2, bounds.height - bounds.height / 10);
	}
	
	/**
	 * Adds a structure and attempts to place parking and entrances around it
	 * @param s the structure to add
	 */
	public void addStructure(Structure s) {
		//testing hacks
		buildings.add(s);
		Point location = new Point();
		location.x = (int)s.getRect().x;
		location.y = (int)s.getRect().y;
		
		//Calculating street entrance
		int leftX = location.x/GRID_SIZE - 2;
		int rightX = (int)((1.0*location.x+s.getRect().width)/GRID_SIZE) + 1;
		int upY = location.y/GRID_SIZE - 2;
		int downY = (int)((1.0*location.y+s.getRect().height)/GRID_SIZE) + 1;
		
		if(downY < drivingMap.length && drivingMap[downY][location.x/GRID_SIZE].isValid())
		{
			location.y = downY*GRID_SIZE;
			s.setParkingLocation(location);
		}
		else if(leftX >= 0 && drivingMap[location.y/GRID_SIZE][leftX].isValid())
		{
			location.x = leftX*GRID_SIZE;
			s.setParkingLocation(location);
		}
		else if(rightX < drivingMap[0].length && drivingMap[location.y/GRID_SIZE][rightX].isValid())
		{
			location.x = rightX*GRID_SIZE;
			s.setParkingLocation(location);
		}
		else if(upY >= 0 && drivingMap[upY][location.x/GRID_SIZE].isValid())
		{
			location.y = upY*GRID_SIZE;
			s.setParkingLocation(location);
		}
		
		location = new Point();
		location.x = (int)s.getRect().x;
		location.y = (int)s.getRect().y;
		
		//Calculating sidewalk entrance
		leftX = location.x/GRID_SIZE - 1;
		rightX = (int)((1.0*location.x+s.getRect().width)/GRID_SIZE);
		upY = location.y/GRID_SIZE - 1;
		downY = (int)((1.0*location.y+s.getRect().height)/GRID_SIZE);
		
		if(downY < walkingMap.length && walkingMap[downY][location.x/GRID_SIZE].isValid())
		{
			location.y = downY*GRID_SIZE;
			s.setEntranceLocation(location);
		}
		else if(leftX >= 0 && walkingMap[location.y/GRID_SIZE][leftX].isValid())
		{
			location.x = leftX*GRID_SIZE;
			s.setParkingLocation(location);
		}
		else if(rightX < walkingMap[0].length && walkingMap[location.y/GRID_SIZE][rightX].isValid())
		{
			location.x = rightX*GRID_SIZE;
			s.setParkingLocation(location);
		}
		else if(upY >= 0 && walkingMap[upY][location.x/GRID_SIZE].isValid())
		{
			location.y = upY*GRID_SIZE;
			s.setParkingLocation(location);
		}
	}
	
	/**
	 * Adds a structure with entrance and parking at given points
	 * @param s the structure to add
	 * @param parking where structure's parking is
	 * @param entrance where structure's entrance is
	 */
	public void addStructure(Structure s, Point parking,Point entrance)
	{
		buildings.add(s);
		s.setParkingLocation(parking);
		s.setEntranceLocation(entrance);	
	}
	
	/**
	 * Gets all of the structures contained in this CityPanel
	 * @return the structures contained within
	 */
	public List<Structure> getStructures() {
		return buildings;
	}
	
	/**
	 * Processes mouse click
	 * @param arg0 the mouse click to process
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) {
		for (int i = 0; i < buildings.size(); i++) {
			Structure s = buildings.get(i);
			if (s.getRect().contains(arg0.getX(), arg0.getY())) {
				s.displayStructure();
				return;
			}
		}
		city.displayBlankPanel();
	}
	
	/**
	 * Does nothing
	 * @param arg0
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	/**
	 * Does nothing
	 * @param arg0
	 */
	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	/**
	 * Does nothing
	 * @param arg0
	 */
	@Override
	public void mousePressed(MouseEvent arg0) {
		
	}

	/**
	 * Does nothing
	 * @param arg0
	 */
	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}
	
	/**
	 * Gets the map of driving permissions
	 * @return 2D array representing driving permissions of the city with direction
	 */
	public MovementDirection[][] getDrivingMap()
	{
		return drivingMap;
	}
	
	/**
	 * Gets the map of walking permissions
	 * @return 2D array representing walking permissions of the city with direction
	 */
	public MovementDirection[][] getWalkingMap()
	{
		return walkingMap;
	}
	
	/**
	 * Repaints the panel based on timer
	 * @param e the event trigger
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}
	
}
