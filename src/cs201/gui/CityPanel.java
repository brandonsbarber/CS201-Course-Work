package cs201.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
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

import cs201.agents.PassengerTestAgent;
import cs201.agents.transit.BusAgent;
import cs201.agents.transit.CarAgent;
import cs201.agents.transit.TruckAgent;
import cs201.gui.CityPanel.DrivingDirection;
import cs201.gui.transit.BusGui;
import cs201.gui.transit.CarGui;
import cs201.gui.transit.PassengerGui;
import cs201.gui.transit.VehicleGui;
import cs201.helper.CityDirectory;
import cs201.helper.transit.BusRoute;
import cs201.roles.marketRoles.MarketManagerRole.ItemRequest;
import cs201.roles.transit.PassengerRole;
import cs201.structures.Structure;
import cs201.structures.transit.BusStop;

public class CityPanel extends JPanel implements MouseListener, ActionListener
{
	public static final int GRID_SIZE = 25;

	public static CityPanel INSTANCE = null;
	
	List<Structure> buildings;
	
	List<Gui> guis;
	
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
		
		public boolean isVertical()
		{
			return this == North || this == South;
		}
		
		public boolean isHorizontal()
		{
			return this == West || this == East;
		}

		public static boolean opposites(DrivingDirection drivingDirection, DrivingDirection drivingDirection2)
		{
			return (drivingDirection == West && drivingDirection2 == East) || (drivingDirection == East && drivingDirection2 == West) || (drivingDirection == South && drivingDirection2 == North) || (drivingDirection == North && drivingDirection2 == South);
		}
	};
	
	private String[][] cityGrid = 
	{
		{"G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G"},
		{"G","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","G"},
		{"G","S","T","4","4","4","4","T","4","4","4","4","T","4","4","4","4","T","4","4","4","4","T","S","G"},
		{"G","S","3","S","S","S","S","3","S","S","S","S","1","S","S","S","S","3","S","S","S","S","1","S","G"},
		{"G","S","3","S","G","G","S","3","S","G","G","S","1","S","G","G","S","3","S","G","G","S","1","S","G"},
		{"G","S","3","S","G","G","S","3","S","G","G","S","1","S","G","G","S","3","S","G","G","S","1","S","G"},
		{"G","S","3","S","S","S","S","3","S","S","S","S","1","S","S","S","S","3","S","S","S","S","1","S","G"},
		{"G","S","T","2","2","2","2","T","2","2","2","2","T","2","2","2","2","T","2","2","2","2","T","S","G"},
		{"G","S","1","S","S","S","S","1","S","S","S","S","3","S","S","S","S","1","S","S","S","S","3","S","G"},
		{"G","S","1","S","G","G","S","1","S","G","G","S","3","S","G","G","S","1","S","G","G","S","3","S","G"},
		{"G","S","1","S","G","G","S","1","S","G","G","S","3","S","G","G","S","1","S","G","G","S","3","S","G"},
		{"G","S","1","S","S","S","S","1","S","S","S","S","3","S","S","S","S","1","S","S","S","S","3","S","G"},
		{"G","S","T","4","4","4","4","T","4","4","4","4","T","4","4","4","4","T","4","4","4","4","T","S","G"},
		{"G","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","G"},
		{"G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G","G"}
	};
	
	private List<BusStop> stops;
	
	
	public CityPanel()
	{
		Timer timer = new Timer(1000/120,this);
		
		buildings = Collections.synchronizedList(new ArrayList<Structure>());
		guis = Collections.synchronizedList(new ArrayList<Gui>());
		
		stops = new ArrayList<BusStop>();
		
		if(INSTANCE == null)
		{
			INSTANCE = this;
		}
		
		addMouseListener(this);
		
		populateDrivingMap();
		//Testing Hacks
		
		stops.add(new BusStop(22*25,12*25,25,25,1, null));
		stops.add(new BusStop(12*25,12*25,25,25,2, null));
		stops.add(new BusStop(2*25,12*25,25,25,3, null));
		stops.add(new BusStop(22*25,2*25,25,25,4, null));
		stops.add(new BusStop(12*25,2*25,25,25,5, null));
		stops.add(new BusStop(2*25,2*25,25,25,6, null));
		
		for(BusStop stop : stops)
		{
			buildings.add(stop);
		}
		
		BusAgent bus = new BusAgent(new BusRoute(stops),0);
		VehicleGui busG;
		guis.add(busG = new BusGui(bus,this,(int)stops.get(0).x,(int)stops.get(0).y));
		
		bus.setGui(busG);
		
		bus.startThread();
		/*
		BusAgent bus2 = new BusAgent(new BusRoute(stops),2);
		VehicleGui busG2;
		guis.add(busG2 = new VehicleGui(bus2,this,(int)stops.get(2).x,(int)stops.get(2).y));
		
		bus2.setGui(busG2);
		
		bus2.startThread();
		
		CarAgent car = new CarAgent();
		VehicleGui gui;
		guis.add(gui = new CarGui(car,this));
		
		Structure startLoc = new BusStop(18*25,2*25,25,25,1, null);
		Structure endLoc = new BusStop(50,50,25,25,2, null);
		
		PassengerRole pass = new PassengerRole(stops.get(0));
		
		pass.setBusStops(stops);
		
		PassengerGui pgui = new PassengerGui(pass,this);
		
		pass.setGui(pgui);
		
		guis.add(pgui);
		
		PassengerTestAgent passAgent = new PassengerTestAgent(pass);
		
		pass.msgGoTo(stops.get(5));
		
		pass.addCar(car);
		
		car.setGui(gui);
		
		car.startThread();
		
		passAgent.startThread();
		
		TruckAgent truck = new TruckAgent(stops.get(0));
		
		truck.msgMakeDeliveryRun(new ArrayList<ItemRequest>(), stops.get(1),1);
		
		truck.msgMakeDeliveryRun(new ArrayList<ItemRequest>(), stops.get(2),1);
		
		truck.startThread();*/
		
		timer.start();
	}
	
	public void addGui(Gui gui)
	{
		guis.add(gui);
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
				else if(cityGrid[y][x].equals("T") || Character.isDigit(cityGrid[y][x].charAt(0)))
				{
					g2.setColor(Color.GRAY.darker());
				}
				
				g2.fillRect(x*GRID_SIZE, y*GRID_SIZE, GRID_SIZE, GRID_SIZE);
				
				if(drivingMap[y][x].isValid())
				{
					if(drivingMap[y][x] == DrivingDirection.North)
					{
						g2.setColor(Color.BLACK);
						g2.drawLine((int)((1.0*x+.5)*GRID_SIZE),(int)((1.0*y+.25)*GRID_SIZE), (int)((1.0*x+.5)*GRID_SIZE), (int)((1.0*y+.75)*GRID_SIZE));
						g2.drawLine((int)((1.0*x+.5)*GRID_SIZE),(int)((1.0*y+.25)*GRID_SIZE), (int)((1.0*x)*GRID_SIZE), (int)((1.0*y+.5)*GRID_SIZE));
						g2.drawLine((int)((1.0*x+.5)*GRID_SIZE),(int)((1.0*y+.25)*GRID_SIZE), (int)((1.0*x+1)*GRID_SIZE), (int)((1.0*y+.5)*GRID_SIZE));

					}
					else if(drivingMap[y][x] == DrivingDirection.South)
					{
						g2.setColor(Color.BLACK);
						g2.drawLine((int)((1.0*x+.5)*GRID_SIZE),(int)((1.0*y+.25)*GRID_SIZE), (int)((1.0*x+.5)*GRID_SIZE), (int)((1.0*y+.75)*GRID_SIZE));
						
						g2.drawLine((int)((1.0*x+.5)*GRID_SIZE),(int)((1.0*y+.75)*GRID_SIZE), (int)((1.0*x)*GRID_SIZE), (int)((1.0*y+.5)*GRID_SIZE));
						g2.drawLine((int)((1.0*x+.5)*GRID_SIZE),(int)((1.0*y+.75)*GRID_SIZE), (int)((1.0*x+1)*GRID_SIZE), (int)((1.0*y+.5)*GRID_SIZE));
					}
					else if(drivingMap[y][x] == DrivingDirection.West)
					{
						g2.setColor(Color.BLACK);
						g2.drawLine((int)((1.0*x+.25)*GRID_SIZE),(int)((1.0*y+.5)*GRID_SIZE), (int)((1.0*x+.75)*GRID_SIZE), (int)((1.0*y+.5)*GRID_SIZE));
						g2.drawLine((int)((1.0*x+.25)*GRID_SIZE),(int)((1.0*y+.5)*GRID_SIZE), (int)((1.0*x+.5)*GRID_SIZE), (int)((1.0*y+1)*GRID_SIZE));
						g2.drawLine((int)((1.0*x+.25)*GRID_SIZE),(int)((1.0*y+.5)*GRID_SIZE), (int)((1.0*x+.5)*GRID_SIZE), (int)((1.0*y)*GRID_SIZE));
					}
					else if(drivingMap[y][x] == DrivingDirection.East)
					{
						g2.setColor(Color.BLACK);
						g2.drawLine((int)((1.0*x+.25)*GRID_SIZE),(int)((1.0*y+.5)*GRID_SIZE), (int)((1.0*x+.75)*GRID_SIZE), (int)((1.0*y+.5)*GRID_SIZE));
						g2.drawLine((int)((1.0*x+.75)*GRID_SIZE),(int)((1.0*y+.5)*GRID_SIZE), (int)((1.0*x+.5)*GRID_SIZE), (int)((1.0*y+1)*GRID_SIZE));
						g2.drawLine((int)((1.0*x+.75)*GRID_SIZE),(int)((1.0*y+.5)*GRID_SIZE), (int)((1.0*x+.5)*GRID_SIZE), (int)((1.0*y)*GRID_SIZE));
					}
					
				}
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
	
	public void addStructure(Structure s) {
		//testing hacks
		buildings.add(s);
		
		
		/*if(s.x == 19*25)
		{
			PassengerRole role = new PassengerRole(buildings.get(0));
			PassengerTestAgent agent = new PassengerTestAgent(role);
			role.setBusStops(stops);
			PassengerGui pgui = new PassengerGui(role,this);
			role.setGui(pgui);
			guis.add(pgui);
			role.msgGoTo(s);
			agent.startThread();
		}
		else
		{
			PassengerRole role = new PassengerRole(s);
			PassengerTestAgent agent = new PassengerTestAgent(role);
			role.setBusStops(stops);
			PassengerGui pgui = new PassengerGui(role,this);
			role.setGui(pgui);
			guis.add(pgui);
			role.msgGoTo(stops.get(3));
			agent.startThread();
		}*/
		
		
	}
	
	public List<Structure> getStructures() {
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
	
	public DrivingDirection[][] getDrivingMap()
	{
		return drivingMap;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		repaint();
		
	}
	
}
