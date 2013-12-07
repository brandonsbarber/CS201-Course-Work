package cs201.helper.transit;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import javax.swing.JOptionPane;

import cs201.gui.CityPanel;

public class Pathfinder
{
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
				//Find an adjacent sidewalk piece
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
		return  null;
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
		
		if(inBounds(map,leftX,y2) && getDirection(map,leftX,y2) == MovementDirection.Left)
		{
			validDirections.add(MovementDirection.Left);
		}
		if(inBounds(map,rightX,y2) && getDirection(map,rightX,y2) == MovementDirection.Right)
		{
			validDirections.add(MovementDirection.Right);
		}
		if(inBounds(map,x2,upY) && getDirection(map,x2,upY) == MovementDirection.Up)
		{
			validDirections.add(MovementDirection.Up);
		}
		if(inBounds(map,x2,downY) && getDirection(map,x2,downY) == MovementDirection.Down)
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
	private static boolean isValidPoint(MovementDirection[][] map, MyPoint nextPoint)
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
}
