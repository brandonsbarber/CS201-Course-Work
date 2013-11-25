package cs201.agents.transit;

import java.util.concurrent.Semaphore;

import cs201.agents.Agent;
import cs201.gui.transit.VehicleGui;
import cs201.interfaces.agents.transit.Vehicle;
import cs201.structures.Structure;

public abstract class VehicleAgent extends Agent implements Vehicle
{
	Structure destination, currentLocation;
	
	Semaphore animationSemaphore = new Semaphore(0);
	
	VehicleGui gui = null;
	
	public void setGui(VehicleGui gui)
	{
		this.gui = gui;
	}
	
	public void msgAnimationDestinationReached()
	{
		animationSemaphore.release();
		System.out.println("Animation Reached");
	}
	
	public void msgSetDestination (Structure destination)
	{
		this.destination = destination;
		stateChanged();
	}
	
	public void msgSetLocation(Structure s)
	{
		currentLocation = s;
	}
	
	public boolean destinationReached()
	{
		return destination == currentLocation;
	}
	
	protected void animate()
	{
		if(gui == null)
		{
			Do("GUI is null");
			return;
		}
		gui.doGoToLocation(destination);
		try
		{
			animationSemaphore.acquire();
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
