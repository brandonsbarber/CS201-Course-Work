package cs201.helper.Brandon;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Intersection
{
	List<Point> intersectionPoints;
	
	public Intersection(List<Point> points)
	{
		intersectionPoints = new ArrayList<Point>();
		for(Point p : points)
		{
			intersectionPoints.add(p);
		}
	}

}
