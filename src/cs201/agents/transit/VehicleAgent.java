package cs201.agents.transit;

import java.awt.Point;
import java.util.concurrent.Semaphore;

import cs201.agents.Agent;
import cs201.gui.transit.VehicleGui;
import cs201.interfaces.agents.transit.Vehicle;
import cs201.structures.Structure;
import cs201.trace.AlertLog;
import cs201.trace.AlertTag;

/**
 * 
 * @author Brandon
 *
 */
public abstract class VehicleAgent extends Agent implements Vehicle
{
	public Structure destination;

	public Structure currentLocation;
	
	Semaphore animationSemaphore = new Semaphore(0);
	
	VehicleGui gui = null;
	
	private static int INSTANCES = 0;
	private int instance;
	
	/**
	 * Creates a new Vehicle (used for instance counting)
	 */
	public VehicleAgent()
	{
		instance = ++INSTANCES;
	}
	
	/**
	 * Sets the GUI of the vehicle
	 * @param gui the gui of the vehicle
	 */
	public void setGui(VehicleGui gui)
	{
		this.gui = gui;
	}
	
	public void setDrunk(boolean drunk)
	{
		if(gui != null)
		{
			gui.setDrunk(drunk);
		}
	}
	
	/**
	 * Message received from GUI indicating that animation is finished
	 */
	public void msgAnimationDestinationReached()
	{
		animationSemaphore.release();
	}
	
	/**
	 * Sets the destination of the vehicle
	 * @param destination where to go
	 */
	public void msgSetDestination (Structure destination)
	{
		this.destination = destination;
		stateChanged();
	}
	
	/**
	 * Sets the current location of the vehicle
	 * @param s the current location
	 */
	public void msgSetLocation(Structure s)
	{
		currentLocation = s;
	}
	
	/**
	 * Checks whether the destination has been reached
	 */
	public boolean destinationReached()
	{
		return destination == currentLocation;
	}
	
	/**
	 * Handles animation calls concisely
	 */
	protected void animate()
	{
		if(gui == null)
		{
			currentLocation = destination;
			return;
		}
		gui.setPresent(true);
		if(destination != null)
		{
			gui.doGoToLocation(destination);
		}
		else
		{
			gui.doGoToLocation(destinationPoint);
		}
		try
		{
			AlertLog.getInstance().logMessage(AlertTag.TRANSIT,"Vehicle "+getInstance(),"Animating to "+destination +" from "+currentLocation);
			animationSemaphore.acquire();
		}
		catch(InterruptedException e)
		{
			if(!gui.getDrunk())
			{
				e.printStackTrace();
			}
			else
			{
				destroyed = true;
			}
		}
		currentLocation = destination;
	}
	
	public void msgSetDestination(Point p)
	{
		destinationPoint = p;
	}
	
	Point destinationPoint;
	
	/**
	 * Gets the instance count of the vehicle
	 * @return instance count of the vehicle
	 */
	public int getInstance()
	{
		return instance;
	}
	
	public String toString()
	{
		return this.getClass().getSimpleName()+""+instance;
	}

	boolean destroyed = false;
	
	public boolean getDestroyed() {
		return destroyed;
	}
}
