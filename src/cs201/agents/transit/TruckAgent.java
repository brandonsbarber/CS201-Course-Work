package cs201.agents.transit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cs201.gui.CityPanel;
import cs201.gui.transit.VehicleGui;
import cs201.interfaces.agents.transit.Truck;
import cs201.roles.marketRoles.MarketManagerRole.ItemRequest;
import cs201.structures.Structure;
import cs201.structures.market.MarketStructure;
import cs201.structures.restaurant.Restaurant;

public class TruckAgent extends VehicleAgent implements Truck
{
	Structure homeStructure;
	
	List<Delivery> deliveries;
	
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
	
	enum DeliveryState {NotDone,InProgress,Done};
	
	public TruckAgent(Structure home)
	{
		homeStructure = home;
		msgSetLocation(homeStructure);
		deliveries = new ArrayList<Delivery>();
		gui = new VehicleGui(this,CityPanel.INSTANCE,(int)homeStructure.x,(int)homeStructure.y);
		CityPanel.INSTANCE.addGui(gui);
		
	}
	
	public void setGui(VehicleGui gui)
	{
		this.gui = gui;
	}
	
	@Override
	public void msgMakeDeliveryRun(List<ItemRequest> inventory, Structure destination,double price)
	{
		deliveries.add(new Delivery(inventory,destination,price));
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
		animate();
		gui.setPresent(false);
	}

	private void makeDeliveryRun(Delivery d)
	{
		gui.setPresent(true);
		msgSetDestination (homeStructure);
		animate();
		
		msgSetDestination (d.destination);
		d.s = DeliveryState.InProgress;
		
		animate();
		
		for(ItemRequest item : d.inventory)
		{
			((Restaurant)d.destination).getCashier().msgHereIsDeliveryFromMarket ((MarketStructure)homeStructure,d.price,item);
		}
		
		d.s = DeliveryState.Done;

	}
}
