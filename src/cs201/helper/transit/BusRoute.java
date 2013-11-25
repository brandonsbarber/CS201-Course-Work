package cs201.helper.transit;

import java.util.ArrayList;
import java.util.List;

import cs201.structures.transit.BusStop;

public class BusRoute
{
	private List<BusStop> stops;
	private int destination;
	
	public BusRoute(List<BusStop> list)
	{
		stops = new ArrayList<BusStop>();
		destination = 0;
		
		for(BusStop stop : list)
		{
			stops.add(stop);
		}
	}
	
	public BusStop getNextStop()
	{
		if(destination >= stops.size())
		{
			destination = 0;
		}
		return stops.get(destination++);
	}
	
	public boolean hasStop(BusStop stop)
	{
		for(BusStop b : stops)
		{
			if(b == stop)
			{
				return true;
			}
		}
		return false;
	}
	
}
