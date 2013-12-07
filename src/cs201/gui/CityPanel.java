package cs201.gui;

import java.awt.Color;
import java.awt.Dimension;
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

public class CityPanel extends JPanel implements MouseListener, ActionListener
{
	
	public MovementDirection[][] drivingMap;
	public MovementDirection[][] walkingMap;
	
	public static final int GRID_SIZE = 25;

	public static CityPanel INSTANCE = null;
	
	List<Structure> buildings;
	
	List<Gui> guis;
	
	private String[][] cityGrid = 
	{
			{"G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G"},
			{"G","ST","8","ST","8","8","ST","8","ST","8","8","ST","8","ST","8","8","ST","8","ST","8","8","ST","8","ST","G"},
			{"G","7","T","47","4","4","45","T","47","4","4","45","T","47","4","4","45","T","47","4","4","45","T","5","G"},
			{"G","ST","38","ST","8","8","ST","38","ST","8","8","ST","18","ST","8","8","ST","38","ST","8","8","ST","18","ST","G"},
			{"G","7","3","7","G","G","5","3","7","G","G","5","1","7","G","G","5","3","7","G","G","5","1","5","G"},
			{"G","7","3","7","G","G","5","3","7","G","G","5","1","7","G","G","5","3","7","G","G","5","1","5","G"},
			{"G","ST","36","ST","6","6","ST","36","ST","6","6","ST","16","ST","6","6","ST","36","6","6","6","ST","16","ST","G"},
			{"G","7","T","27","2","2","25","T","27","2","2","25","T","27","2","2","25","T","27","2","2","25","T","5","G"},
			{"G","ST","18","ST","8","8","ST","18","ST","8","8","ST","38","ST","8","8","ST","18","ST","8","8","ST","38","ST","G"},
			{"G","7","1","7","G","G","5","1","7","G","G","5","3","7","G","G","5","1","7","G","G","5","3","5","G"},
			{"G","7","1","7","G","G","5","1","7","G","G","5","3","7","G","G","5","1","7","G","G","5","3","5","G"},
			{"G","ST","16","ST","6","6","ST","16","ST","6","6","ST","36","ST","6","6","ST","16","ST","6","6","ST","36","ST","G"},
			{"G","7","T","47","4","4","45","T","47","4","4","45","T","47","4","4","45","T","47","4","4","45","T","5","G"},
			{"G","ST","6","ST","6","6","ST","6","ST","6","6","ST","6","ST","6","6","ST","6","ST","6","6","ST","6","ST","G"},
			{"G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G"}
	};

	/**
	 * Creates a city panel and makes it the sole instance in the program.
	 * Sets up initial data too
	 */
	public CityPanel()
	{
		Timer timer = new Timer(1000/240,this);
		
		try
		{
			MapParser.parseMap();
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
						if(Character.isDigit(cityGrid[y][x].charAt(1)))
						{
							val = Integer.parseInt(cityGrid[y][x].substring(1));
						}
					}
					switch(val)
					{
					case 5:wDir = MovementDirection.Up;break;
					case 6:wDir = MovementDirection.Right;break;
					case 7:wDir = MovementDirection.Down;break;
					case 8:wDir = MovementDirection.Left;break;
					default:wDir = MovementDirection.None;break;
					}
				}
				else
				{
					if(cityGrid[y][x].equals("T"))
					{
						dir = MovementDirection.Turn;
						wDir = MovementDirection.None;
					}
					else if(cityGrid[y][x].equals("ST"))
					{
						dir = MovementDirection.None;
						wDir = MovementDirection.Turn;
					}
					else
					{
						dir = MovementDirection.None;
						wDir = MovementDirection.None;
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
		
		g2.setColor(Color.LIGHT_GRAY.brighter().brighter());
		g2.fillRect(0,0,(int)bounds.getWidth(),(int)bounds.getHeight());
		
		for(int y = 0; y < cityGrid.length; y++)
		{
			for(int x = 0; x < cityGrid[y].length; x++)
			{
				if(cityGrid[y][x].equals("G"))
				{
					g2.setColor(Color.GREEN);
				}
				else if(cityGrid[y][x].equals("R"))
				{
					g2.setColor(Color.GRAY.darker());
				}
				else if(cityGrid[y][x].equals("ST") || Character.isDigit(cityGrid[y][x].charAt(0)) && Integer.parseInt(cityGrid[y][x].substring(0,1)) > 4)
				{
					g2.setColor(Color.GRAY.brighter().brighter().brighter().brighter());
				}
				else if(cityGrid[y][x].equals("T") || Character.isDigit(cityGrid[y][x].charAt(0)))
				{
					g2.setColor(Color.GRAY.darker());
				}
				
				g2.fillRect(x*GRID_SIZE, y*GRID_SIZE, GRID_SIZE, GRID_SIZE);
				
				if(cityGrid[y][x].length() == 2 && Character.isDigit(cityGrid[y][x].charAt(1)))
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
			g2.setColor(Color.BLACK);
			Structure s = buildings.get(i);
			g2.fill(s);
			
			g2.setColor(Color.WHITE);
			g2.drawString(""+s.getId(),(int)s.x,(int)(s.y + s.height));
			
			if(Constants.DEBUG_MODE)
			{
				g2.setColor(Color.BLUE);
				g2.fill(new Rectangle(s.getParkingLocation().x,s.getParkingLocation().y,GRID_SIZE,GRID_SIZE));
				g2.fill(new Rectangle(s.getEntranceLocation().x,s.getEntranceLocation().y,GRID_SIZE,GRID_SIZE));
				
				g2.setColor(Color.WHITE);
				g2.drawString("P",s.getParkingLocation().x,s.getParkingLocation().y+25);
				g2.drawString("E",s.getEntranceLocation().x,s.getEntranceLocation().y+25);
				
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
		g2.drawString(CityDirectory.getInstance().getTime().toString(), bounds.width / 2, bounds.height - bounds.height / 10);
	}
	
	/**
	 * Adds a structure and attempts to place parking and entrances around it
	 * @param s the structure to add
	 */
	public void addStructure(Structure s) {
		//testing hacks
		buildings.add(s);
		Point location = new Point();
		location.x = (int)s.getX();
		location.y = (int)s.getY();
		
		//Calculating street entrance
		int leftX = location.x/GRID_SIZE - 2;
		int rightX = (int)((1.0*location.x+s.width)/GRID_SIZE) + 1;
		int upY = location.y/GRID_SIZE - 2;
		int downY = (int)((1.0*location.y+s.height)/GRID_SIZE) + 1;
		
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
		location.x = (int)s.getX();
		location.y = (int)s.getY();
		
		//Calculating sidewalk entrance
		leftX = location.x/GRID_SIZE - 1;
		rightX = (int)((1.0*location.x+s.width)/GRID_SIZE);
		upY = location.y/GRID_SIZE - 1;
		downY = (int)((1.0*location.y+s.height)/GRID_SIZE);
		
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
			if (s.contains(arg0.getX(), arg0.getY())) {
				s.displayStructure();
			}
		}
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
