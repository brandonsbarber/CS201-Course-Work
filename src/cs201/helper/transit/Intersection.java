package cs201.helper.transit;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Semaphore;

import cs201.gui.CityPanel;

public class Intersection implements Comparable
{
	Set<Point> intersectionPoints;
	Semaphore intersectionSemaphore;
	CityPanel city;
	
	int instance;
	
	private static int INSTANCE_COUNT = 0;
	
	public Intersection(CityPanel city, Set<Point> points)
	{
		instance = ++INSTANCE_COUNT;
		
		intersectionPoints = new HashSet<Point>();
		for(Point p : points)
		{
			intersectionPoints.add(p);
		}
		
		intersectionSemaphore = new Semaphore(1,true);
		this.city = city;
	}
	
	public int getInstance()
	{
		return instance;
	}
	
	public boolean containsPoint(Point p)
	{
		return intersectionPoints.contains(p);
	}
	
	public void acquireAll()
	{
		for(Point p : intersectionPoints)
		{
			city.permissions[p.y][p.x].tryAcquire();
		}
	}
	
	public void releaseAll()
	{
		for(Point p : intersectionPoints)
		{
			city.permissions[p.y][p.x].release();
		}
	}
	
	public boolean acquireIntersection()
	{
		return intersectionSemaphore.tryAcquire();
	}
	
	public void releaseIntersection()
	{
		intersectionSemaphore.release();
	}
	
	@Override
	public int compareTo(Object o)
	{
		if(!(o instanceof Intersection))
		{
			return -1;
		}
		Intersection i = (Intersection)o;
		for(Point p : i.intersectionPoints)
		{
			if(!intersectionPoints.contains(p))
			{
				return -1;
			}
		}
		return 0;
	}
}