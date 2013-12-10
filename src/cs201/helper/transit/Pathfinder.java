package cs201.helper.transit;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import javax.swing.JOptionPane;

import cs201.gui.CityPanel;

public class Pathfinder
{
	@SuppressWarnings("serial")
	static class MyPoint extends Point
	{
		MyPoint prev;
		MovementDirection move;
		
		public MyPoint(int i, int j, MyPoint previous,MovementDirection moveDir)
		{
			super(i,j);
			prev = previous;
			move = moveDir;
		}
		
		public boolean equals(MyPoint p)
		{
			return p.x == x && p.y == y;
		}
	}
	
	public static Stack<MovementDirection> calcOneWayMove (MovementDirection[][] map,int x, int y, int destX, int destY)
	{
		Stack<MovementDirection> moves = new Stack<MovementDirection>();
		Queue<MyPoint> location = new LinkedList<MyPoint>();
		
		ArrayList<MyPoint> visitedPoints = new ArrayList<MyPoint>();
		
		MyPoint startLoc = new MyPoint(x/CityPanel.GRID_SIZE,y/CityPanel.GRID_SIZE,null,map[y/CityPanel.GRID_SIZE][x/CityPanel.GRID_SIZE]);
		MyPoint destination = new MyPoint(destX/CityPanel.GRID_SIZE,destY/CityPanel.GRID_SIZE,null,map[y/CityPanel.GRID_SIZE][x/CityPanel.GRID_SIZE]);
		
		location.add(startLoc);
		visitedPoints.add(startLoc);
		
		//run a BFS
		while(!location.isEmpty())
		{
			MyPoint p = location.remove();
			if(p.equals(destination))
			{
				MyPoint head = p;
				while(head != null)
				{
					moves.add(head.move);
					head = head.prev;
				}
				break;
			}
			MovementDirection currentDirection = map[p.y][p.x];
			
			if(currentDirection == MovementDirection.Turn)
			{
				List<MovementDirection> validDirections = getJunctionDirections(map,p.x,p.y);
				for(MovementDirection dir : validDirections)
				{
					MyPoint nextPoint = getPointFromDirection(p,dir);
					if(!visitedPoints.contains(nextPoint) && isValidPoint(map,nextPoint))
					{
						visitedPoints.add(nextPoint);
						location.add(nextPoint);
					}
				}
			}
			else if(currentDirection == MovementDirection.None)
			{
				MyPoint point = getPointFromDirection(p,MovementDirection.Down);
				if(!visitedPoints.contains(point) && isValidPoint(map,new MyPoint(point.x,point.y,null,null)) && map[point.y][point.x].isValid())
				{
					visitedPoints.add(point);
					location.add(point);
					continue;
				}
				point = getPointFromDirection(p,MovementDirection.Up);
				if(!visitedPoints.contains(point) && isValidPoint(map,new MyPoint(point.x,point.y,null,null)) && map[point.y][point.x].isValid())
				{
					visitedPoints.add(point);
					location.add(point);
					continue;
				}
				point = getPointFromDirection(p,MovementDirection.Right);
				if(!visitedPoints.contains(point) && isValidPoint(map,new MyPoint(point.x,point.y,null,null)) && map[point.y][point.x].isValid())
				{
					visitedPoints.add(point);
					location.add(point);
					continue;
				}
				point = getPointFromDirection(p,MovementDirection.Left);
				if(!visitedPoints.contains(point) && isValidPoint(map,new MyPoint(point.x,point.y,null,null)) && map[point.y][point.x].isValid())
				{
					visitedPoints.add(point);
					location.add(point);
					continue;
				}
			}
			else
			{
				MyPoint nextPoint = getPointFromDirection(p,currentDirection);
				if(!visitedPoints.contains(nextPoint) && isValidPoint(map,nextPoint))
				{
					visitedPoints.add(nextPoint);
					location.add(nextPoint);
				}
			}
		}
		
		if(moves.isEmpty())
		{
			JOptionPane.showMessageDialog(null,": I cannot find a path.");
		}
		else
		{
			//clear first element
			moves.pop();
		}
		
		return moves;
	}
	
	public static Stack<MovementDirection> calcTwoWayMove (MovementDirection[][] map,int x, int y, int destX, int destY)
	{
		Stack<MovementDirection> moves = new Stack<MovementDirection>();
		
		Queue<MyPoint> location = new LinkedList<MyPoint>();
		
		ArrayList<MyPoint> visitedPoints = new ArrayList<MyPoint>();
		
		MyPoint startLoc = new MyPoint(x/CityPanel.GRID_SIZE,y/CityPanel.GRID_SIZE,null,map[y/CityPanel.GRID_SIZE][x/CityPanel.GRID_SIZE]);
		MyPoint destination = new MyPoint(destX/CityPanel.GRID_SIZE,destY/CityPanel.GRID_SIZE,null,map[y/CityPanel.GRID_SIZE][x/CityPanel.GRID_SIZE]);
		
		location.add(startLoc);
		visitedPoints.add(startLoc);
		
		//run a BFS
		while(!location.isEmpty())
		{
			MyPoint p = location.remove();
			
			if(p.equals(destination))
			{
				MyPoint head = p;
				while(head != null)
				{
					moves.add(head.move);
					head = head.prev;
				}
				break;
			}
			
			//sidewalk direction (turn, h, or v)
			MovementDirection currentDirection = map[p.y][p.x];
			
			if(p.move == MovementDirection.Horizontal || p.move == MovementDirection.Vertical || p.move == MovementDirection.None || currentDirection == MovementDirection.Turn || (p.move.isHorizontal() && currentDirection.isVertical())|| (p.move.isVertical() && currentDirection.isHorizontal()))
			{
				//Treat initial state like a junction
				List<MovementDirection> validDirections = getJunctionDirectionsOneWay(map,p.x,p.y);
				
				for(MovementDirection dir : validDirections)
				{
					MyPoint nextPoint = getPointFromDirection(p,dir);
					if(!visitedPoints.contains(nextPoint) && isValidPoint(map,nextPoint))
					{
						visitedPoints.add(nextPoint);
						location.add(nextPoint);
					}
				}
			}
			else
			{
				MyPoint nextPoint = getPointFromDirection(p,p.move);
								
				if(!visitedPoints.contains(nextPoint) && isValidPoint(map,nextPoint))
				{
					visitedPoints.add(nextPoint);
					location.add(nextPoint);
				}
			}
		}
		
		if(moves.isEmpty())
		{
			throw new IllegalArgumentException("I cannot find a path.");
		}
		else
		{
			//clear first element
			moves.pop();
		}
		return moves;
	}
	
	public static Point findRandomWalkingLocation(MovementDirection[][] walking, MovementDirection[][] driving)
	{
		Point p = new Point();
		
		ArrayList<Point> tiles = new ArrayList<Point>();
		
		for(int y = 0; y < walking.length; y++)
		{
			for(int x = 0; x < walking[y].length; x++)
			{
				if(walking[y][x].isValid() && !driving[y][x].isValid())
				{
					tiles.add(new Point(x,y));
				}
			}
		}
		
		if(!tiles.isEmpty())
		{
			p = tiles.get((int)(Math.random()*tiles.size()));
		}
		
		return p;
	}
	
	/*
	 * Helper method for getting junction
	 */
	private static List<MovementDirection> getJunctionDirections(MovementDirection[][] map,int x2, int y2)
	{
		List<MovementDirection> validDirections = new ArrayList<MovementDirection>();
		
		int leftX = x2-1;
		int rightX = x2+1;
		int upY = y2 - 1;
		int downY = y2 + 1;
		
		if(inBounds(map,leftX,y2) && (getDirection(map,leftX,y2) == MovementDirection.Left || getDirection(map,leftX,y2) == MovementDirection.Turn))
		{
			validDirections.add(MovementDirection.Left);
		}
		if(inBounds(map,rightX,y2) && (getDirection(map,rightX,y2) == MovementDirection.Right || getDirection(map,rightX,y2) == MovementDirection.Turn))
		{
			validDirections.add(MovementDirection.Right);
		}
		if(inBounds(map,x2,upY) && (getDirection(map,x2,upY) == MovementDirection.Up || getDirection(map,x2,upY) == MovementDirection.Turn))
		{
			validDirections.add(MovementDirection.Up);
		}
		if(inBounds(map,x2,downY) && (getDirection(map,x2,downY) == MovementDirection.Down || getDirection(map,x2,downY) == MovementDirection.Turn))
		{
			validDirections.add(MovementDirection.Down);
		}
		return validDirections;
	}
	
	/*
	 * Helper method for getting junction
	 */
	private static List<MovementDirection> getJunctionDirectionsOneWay(MovementDirection[][] map,int x2, int y2)
	{
		List<MovementDirection> validDirections = new ArrayList<MovementDirection>();
		
		int leftX = x2-1;
		int rightX = x2+1;
		int upY = y2 - 1;
		int downY = y2 + 1;
		
		if(inBounds(map,leftX,y2) && getDirection(map,leftX, y2) != MovementDirection.None)
		{
			validDirections.add(MovementDirection.Left);
		}
		if(inBounds(map,rightX,y2) && getDirection(map,rightX, y2) != MovementDirection.None)
		{
			validDirections.add(MovementDirection.Right);
		}
		if(inBounds(map,x2,upY) && getDirection(map,x2,upY) != MovementDirection.None)
		{
			validDirections.add(MovementDirection.Up);
		}
		if(inBounds(map,x2,downY) && getDirection(map,x2,downY) != MovementDirection.None)
		{
			validDirections.add(MovementDirection.Down);
		}
		return validDirections;
	}

	/*
	 * Helper method with swapping for better readability
	 */
	private static MovementDirection getDirection(MovementDirection[][] map, int x, int y)
	{
		return map[y][x];
	}
	
	/*
	 * Helper method for bounds
	 */
	private static boolean inBounds(MovementDirection[][] map, int x2, int y2)
	{
		return y2 < map.length && y2 >= 0 && x2 >= 0 && x2 < map[y2].length;
	}

	/*
	 * Helper method for validity
	 */
	private static boolean isValidPoint(MovementDirection[][] map, Point nextPoint)
	{
		return nextPoint.x >= 0 && nextPoint.x < map[0].length && nextPoint.y >= 0 && nextPoint.y < map.length;
	}

	/*
	 * Helper method for extending line of point direction
	 */
	private static MyPoint getPointFromDirection(MyPoint p, MovementDirection dir)
	{
		switch(dir)
		{
		case Right:
			return new MyPoint(p.x+1,p.y,p,dir);
		case Up:
			return new MyPoint(p.x,p.y-1,p,dir);
		case Down:
			return new MyPoint(p.x,p.y+1,p,dir);
		case Left:
			return new MyPoint(p.x-1,p.y,p,dir);
		default:
			return new MyPoint(p.x,p.y-1,p,MovementDirection.Up);
		}
	}
	
	public static boolean isCrossWalk(Point p, MovementDirection[][] walking, MovementDirection[][] driving)
	{
		return walking[p.y][p.x].isValid() && driving[p.y][p.x].isValid();
	}
	
	public static Set<Point> tryIntersectionAcquire(CityPanel city, int x, int y)
	{
		Point p = new Point(x,y);
		
		Set<Point> intersection = new HashSet<Point>();
		
		if(isValidPoint(city.getDrivingMap(),p) && city.getDrivingMap()[p.y][p.x] != MovementDirection.Turn && !isCrossWalk(p,city.getWalkingMap(),city.getDrivingMap()))
		{
			return intersection;
		}
		
		intersection.add(p);
		
		ArrayList<Point> toCheck = new ArrayList<Point>();
		ArrayList<Point> checked = new ArrayList<Point>();
		toCheck.add(p);
		
		while(!toCheck.isEmpty())
		{
			Point check = toCheck.remove(0);
			if(checked.contains(check))
			{
				continue;
			}
			checked.add(check);
			if(isValidPoint(city.getDrivingMap(),check) && (city.getDrivingMap()[check.y][check.x] == MovementDirection.Turn || isCrossWalk(check,city.getWalkingMap(),city.getDrivingMap())))
			{
				intersection.add(check);
				toCheck.add(new Point(check.x,check.y+1));
				toCheck.add(new Point(check.x,check.y-1));
				toCheck.add(new Point(check.x+1,check.y));
				toCheck.add(new Point(check.x-1,check.y));
			}
		}
		
		return intersection;
	}
	
	public static boolean isInIntersection(CityPanel city, Point p)
	{
		return city.getDrivingMap()[p.y][p.x] == MovementDirection.Turn;
	}
}
