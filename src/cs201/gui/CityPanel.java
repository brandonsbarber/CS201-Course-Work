package cs201.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import cs201.agents.PassengerTestAgent;
import cs201.agents.transit.BusAgent;
import cs201.agents.transit.CarAgent;
import cs201.gui.transit.VehicleGui;
import cs201.helper.transit.BusRoute;
import cs201.roles.transit.PassengerRole;
import cs201.structures.Structure;
import cs201.structures.restaurant.RestaurantMatt;
import cs201.structures.transit.BusStop;

public class CityPanel extends JPanel implements MouseListener, ActionListener
{
	public static final int GRID_SIZE = 25;

	ArrayList<Structure> buildings;
	
	ArrayList<VehicleGui> guis;
	
	public enum DrivingDirection
	{
		None,North,South,East,West,Turn;
		
		public DrivingDirection turnRight()
		{
			if(this == North)
			{
				return East;
			}
			else if(this == East)
			{
				return South;
			}
			else if(this == South)
			{
				return West;
			}
			else if(this == West)
			{
				return North;
			}
			
			return this;
		}
		
		public boolean isValid()
		{
			return ordinal() > 0;
		}
	};
	
	private String[][] cityGrid = 
	{
			{"G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G"},{"G","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","G"},{"G","S","T","2","2","2","2","T","2","2","2","2","T","2","2","2","2","T","S","G"},{"G","S","1","S","S","S","S","3","S","S","S","S","1","S","S","S","S","3","S","G"},{"G","S","1","S","G","G","S","3","S","G","G","S","1","S","G","G","S","3","S","G"},{"G","S","1","S","G","G","S","3","S","G","G","S","1","S","G","G","S","3","S","G"},{"G","S","1","S","S","S","S","3","S","S","S","S","1","S","S","S","S","3","S","G"},{"G","S","T","4","4","4","4","T","4","4","4","4","T","4","4","4","4","T","S","G"},{"G","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","G"},{"G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G"}
	};
	
	private List<BusStop> stops;
	
	
	public CityPanel()
	{
		Timer timer = new Timer(1000/120,this);
		
		buildings = new ArrayList<Structure>();
		guis = new ArrayList<VehicleGui>();
		
		stops = new ArrayList<BusStop>();
		
		stops.add(new BusStop(2*25,2*25,25,25,1));
		stops.add(new BusStop(7*25,2*25,25,25,2));
		stops.add(new BusStop(7*25,7*25,25,25,3));
		stops.add(new BusStop(2*25,7*25,25,25,4));
		
		for(BusStop stop : stops)
		{
			buildings.add(stop);
		}
		
		BusAgent bus = new BusAgent(new BusRoute(stops));
		VehicleGui busG;
		guis.add(busG = new VehicleGui(bus,this,(int)stops.get(0).x,(int)stops.get(0).y));
		
		bus.setGui(busG);
		
		bus.startThread();
		
		addMouseListener(this);
		
		populateDrivingMap();
		
		CarAgent car = new CarAgent();
		VehicleGui gui;
		guis.add(gui = new VehicleGui(car,this));
		
		Structure startLoc = new RestaurantMatt(18*25,2*25,25,25,1);
		Structure endLoc = new RestaurantMatt(50,50,25,25,2);
		
		PassengerRole pass = new PassengerRole(startLoc);
		
		PassengerTestAgent passAgent = new PassengerTestAgent(pass);
		
		pass.msgGoTo(endLoc);
		
		pass.addCar(car);
		
		car.setGui(gui);
		
		//car.startThread();
		
		passAgent.startThread();
		
		timer.start();
	}
	
	private void populateDrivingMap()
	{
		drivingMap = new DrivingDirection[cityGrid.length][cityGrid[0].length];
		
		for(int y = 0; y < cityGrid.length; y++)
		{
			for(int x = 0; x < cityGrid[y].length; x++)
			{
				DrivingDirection dir = DrivingDirection.None;
				
				if(Character.isDigit(cityGrid[y][x].charAt(0)))
				{
					int val = Integer.parseInt(cityGrid[y][x]);
					switch(val)
					{
						case 1:dir = DrivingDirection.North;break;
						case 2:dir = DrivingDirection.East;break;
						case 3:dir = DrivingDirection.South;break;
						case 4:dir = DrivingDirection.West;break;
					}
				}
				else
				{
					if(cityGrid[y][x].equals("T"))
					{
						dir = DrivingDirection.Turn;
					}
					else
					{
						dir = DrivingDirection.None;
					}
				}
				drivingMap[y][x] = dir;
			}
		}
	}

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
				else if(cityGrid[y][x].equals("C"))
				{
					g2.setColor(Color.GRAY.brighter());
				}
				else if(cityGrid[y][x].equals("S"))
				{
					g2.setColor(Color.GRAY.brighter().brighter().brighter().brighter());
				}
				
				if(drivingMap[y][x].isValid())
				{
					g2.setColor(Color.BLUE);
				}
				
				g2.fillRect(x*GRID_SIZE, y*GRID_SIZE, GRID_SIZE, GRID_SIZE);
			}
		}
		
		
		
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
		
		for (int i = 0; i < buildings.size(); i++)
		{
			Structure s = buildings.get(i);
			g2.fill(s);
		}
		
		for(VehicleGui gui: guis)
		{
			if(gui.isPresent())
			{
				gui.updatePosition();
				gui.draw(g2);
			}
		}
	}
	
	public void addStructure(Structure s) {
		buildings.add(s);
	}
	
	public ArrayList<Structure> getStructures() {
		return buildings;
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		for (int i = 0; i < buildings.size(); i++) {
			Structure s = buildings.get(i);
			if (s.contains(arg0.getX(), arg0.getY())) {
				s.displayStructure();
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public DrivingDirection[][] drivingMap;


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		repaint();
		
	}
	
}
