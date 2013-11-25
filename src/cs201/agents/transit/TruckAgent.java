package cs201.agents.transit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cs201.gui.transit.VehicleGui;
import cs201.interfaces.agents.transit.Truck;
import cs201.roles.marketRoles.MarketManagerRole.ItemRequest;
import cs201.structures.Structure;

public class TruckAgent extends VehicleAgent implements Truck
{
	Structure homeStructure;
	
	List<Delivery> deliveries;
	
	class Delivery 
	{
		List<ItemRequest> inventory;
		Structure destination;
		DeliveryState s;
		
		public Delivery(List<ItemRequest> items, Structure dest)
		{
			inventory = items;
			destination = dest;
			s = DeliveryState.NotDone;
		}
	};
	
	enum DeliveryState {NotDone,InProgress,Done};
	
	VehicleGui gui;
	
	public TruckAgent(Structure home)
	{
		homeStructure = home;
		deliveries = new ArrayList<Delivery>();
	}
	
	public void setGui(VehicleGui gui)
	{
		this.gui = gui;
	}
	
	@Override
	public void msgMakeDeliveryRun(List<ItemRequest> inventory, Structure destination)
	{
		deliveries.add(new Delivery(inventory,destination));
		stateChanged();
	}

	@Override
	protected boolean pickAndExecuteAnAction()
	{
		if(deliveries.isEmpty() && currentLocation != homeStructure)
		{
			returnHome();
			return true;
		}
		else
		{
			for(int i = 0; i < deliveries.size(); i++)
			{
				if(deliveries.get(i).s == DeliveryState.Done)
				{
					deliveries.remove(i);
					return true;
				}
			}
			for(Delivery d : deliveries)
			{
				if(d.s == DeliveryState.NotDone)
				{
					makeDeliveryRun(d);
					return true;
				}
			}
		}
		return false;
	}

	private void returnHome()
	{
		msgSetDestination (homeStructure);
		gui.doGoToLocation(destination);
		try
		{
			animationSemaphore.acquire();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	private void makeDeliveryRun(Delivery d)
	{
		msgSetDestination (homeStructure);
		
		gui.doGoToLocation(destination);
		try
		{
			animationSemaphore.acquire();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		msgSetDestination (d.destination);
		d.s = DeliveryState.InProgress;
		
		gui.doGoToLocation(destination);
		try
		{
			animationSemaphore.acquire();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		//d.destination.msgMakeDelivery(inventory);
		d.s = DeliveryState.Done;

	}
}
