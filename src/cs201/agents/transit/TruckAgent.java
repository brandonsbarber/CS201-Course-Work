package cs201.agents.transit;

import java.util.ArrayList;
import java.util.List;

import cs201.gui.CityPanel;
import cs201.gui.transit.TruckGui;
import cs201.gui.transit.VehicleGui;
import cs201.interfaces.agents.transit.Truck;
import cs201.roles.marketRoles.MarketManagerRole.ItemRequest;
import cs201.structures.Structure;
import cs201.structures.market.MarketStructure;
import cs201.structures.restaurant.Restaurant;
import cs201.trace.AlertLog;
import cs201.trace.AlertTag;

/**
 * 
 * @author Brandon
 *
 */
public class TruckAgent extends VehicleAgent implements Truck
{
	Structure homeStructure;
	
	public List<Delivery> deliveries;
	
	class Delivery 
	{
		List<ItemRequest> inventory;
		Structure destination;
		DeliveryState s;
		double price;
		
		public Delivery(List<ItemRequest> items, Structure dest,double price)
		{
			inventory = items;
			destination = dest;
			s = DeliveryState.NotDone;
			this.price = price;
		}
	};
	
	enum DeliveryState {NotDone,InProgress,Done, Failed};
	
	/**
	 * Creates a truck agent with the given home structure
	 * @param home home structure for the truck
	 */
	public TruckAgent(Structure home)
	{
		homeStructure = home;
		msgSetLocation(homeStructure);
		deliveries = new ArrayList<Delivery>();
		if(CityPanel.INSTANCE != null)
		{
			gui = new TruckGui(this,CityPanel.INSTANCE,(int)homeStructure.getParkingLocation().x,(int)homeStructure.getParkingLocation().y);
			CityPanel.INSTANCE.addGui(gui);
		}
	}
	
	/**
	 * Sets the GUI of the truck
	 * @param gui the gui to use
	 */
	public void setGui(VehicleGui gui)
	{
		this.gui = gui;
	}
	
	/**
	 * Adds a delivery notification for the truck to do
	 * @param inventory the inventory that will be carried
	 * @param destination where to drive
	 * @param price the price of the goods
	 */
	@Override
	public void msgMakeDeliveryRun(List<ItemRequest> inventory, Structure destination,double price)
	{
		AlertLog.getInstance().logMessage(AlertTag.TRANSIT,"Vehicle "+getInstance(),"Told to make delivery run to: "+destination+" with "+inventory+" and price of $"+price);
		deliveries.add(new Delivery(inventory,destination,price));
		stateChanged();
	}
	
	@Override
	public boolean pickAndExecuteAnAction()
	{
		if(deliveries.isEmpty() && currentLocation != homeStructure)
		{
			returnHome();
			return false;
		}
		else
		{
			for(Delivery d : deliveries)
			{
				if(d.s == DeliveryState.Failed)
				{
					processFailed(d);
					return true;
				}
			}
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

	/*
	 * Returns the truck to home position
	 */
	private void returnHome()
	{
		msgSetDestination (homeStructure);
		animate();
		if(gui != null)
		{
			gui.setPresent(false);
		}
	}

	/*
	 * Moves Truck to home, then delivery location, and delivers
	 */
	private void makeDeliveryRun(Delivery d)
	{
		if(gui != null)
		{
			gui.setPresent(true);
		}
		msgSetDestination (homeStructure);
		animate();
		
		msgSetDestination (d.destination);
		d.s = DeliveryState.InProgress;
		
		animate();
		
		msgSetLocation(d.destination);
		
		if(!((Restaurant)d.destination).getOpen())
		{
			AlertLog.getInstance().logMessage(AlertTag.TRANSIT, "Truck: "+getInstance(), "Delivery to restaurant failed");
			d.s = DeliveryState.Failed;
		}
		else
		{
			AlertLog.getInstance().logMessage(AlertTag.TRANSIT, "Truck: "+getInstance(), "Delivery to restaurant succeeded");
			for(ItemRequest item : d.inventory)
			{
				((Restaurant)d.destination).getCashier().msgHereIsDeliveryFromMarket ((MarketStructure)homeStructure,d.price,item);
			}
			
			d.s = DeliveryState.Done;
		}
	}
	

	private void processFailed(Delivery d)
	{
		if(gui != null)
		{
			gui.setPresent(true);
		}
		
		returnHome();
		
		if(homeStructure instanceof MarketStructure)
		{
			MarketStructure m = (MarketStructure)homeStructure;
			//FIX THIS AFTER TALKING WITH BEN
			m.getManager().msgDeliveryFailed();
		}
		
		d.s = DeliveryState.Done;
	}
}
