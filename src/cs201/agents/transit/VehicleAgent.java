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
	
	private static int INSTANCES = 0;
	private int instance;
	
	public VehicleAgent()
	{
		instance = ++INSTANCES;
	}
	
	public void setGui(VehicleGui gui)
	{
		this.gui = gui;
	}
	
	public void msgAnimationDestinationReached()
	{
		Do("Done animating");
		animationSemaphore.release();
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
			return;
		}
		gui.setPresent(true);
		gui.doGoToLocation(destination);
		try
		{
			Do("Animating to "+destination +" from "+currentLocation);
			animationSemaphore.acquire();
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		currentLocation = destination;
	}
	
	protected void Do(String msg) {
		StringBuffer output = new StringBuffer();
		output.append("[");
		output.append(this.getClass().getSimpleName());
		output.append("] ");
		output.append(this.instance);
		output.append(": ");
		output.append(msg);
		
		System.out.println(output.toString());
	}

	public int getInstance()
	{
		return instance;
	}
}
