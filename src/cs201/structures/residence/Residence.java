package cs201.structures.residence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.StructurePanel;
import cs201.gui.configPanels.MarketConfigPanel;
import cs201.gui.configPanels.ResidenceConfigPanel;
import cs201.gui.roles.residence.ResidentGui;
import cs201.gui.structures.residence.ResidenceAnimationPanel;
import cs201.helper.CityTime;
import cs201.interfaces.roles.housing.Resident;
import cs201.roles.Role;
import cs201.roles.housingRoles.LandlordRole;
import cs201.roles.housingRoles.RenterRole;
import cs201.roles.housingRoles.ResidentRole;
import cs201.roles.marketRoles.MarketManagerRole.ItemRequest;
import cs201.structures.Structure;

public class Residence extends Structure {
	private ResidentRole resident;
	private Role owner; //owner is either a resident or a landlord.
	private ApartmentComplex complex;
	private List<Food> fridge = Collections.synchronizedList(new ArrayList<Food>());
	private boolean hasFood;
	private boolean isApartment;
	private boolean isOccupied = false;
	private ResidenceConfigPanel configPanel;
	
	public class Food {
		private String type;
		private int amount;

		public Food(String t, int amt) {
			type = t;
			amount = amt;
		}
		public void addMore(int amt) {
			amount += amt;
		}
		public void minusOne() {
			amount--;
		}
		public boolean noneLeft() {
			return amount==0;
		}
		public String getType(){
			return type;
		}
		public int getAmount(){
			return amount;
		}
	}
	
	public Residence(int x, int y, int width, int height, int id, StructurePanel p, boolean isApt) {
	    super(x, y, width, height, id, p);
	    isApartment = isApt;
	    
	    if (isApartment) {
	    	resident = new RenterRole(this);
	    	owner = null;
	    }
	    else {
	    	resident = new ResidentRole(this);
	    	owner = resident;
	    }
	    
	    complex = null;
	    
	    ResidentGui rGui = new ResidentGui(resident);
	    resident.setGui(rGui);
	    panel.addGui(rGui);
	    ((ResidenceAnimationPanel)panel).informResident(rGui);
	    
	    addFood("Steak", 10);
	    addFood("Pizza", 10);
	    addFood("Pasta", 10);
	    addFood("Ice Cream", 10);
	    addFood("Chicken", 10);
	    addFood("Salad", 10);
	}
	
	//Setters
	
	public void setOwner(Role o) {
		owner = o;
	}
	
	public void setApartmentComplex(ApartmentComplex newComplex) {
		if (isApartment) {
			LandlordRole l = newComplex.getLandlord();
			
			owner = (Role)l;
			complex = newComplex;
			((RenterRole)resident).setLandlord(l);
		}
	}
	
	public void setResident(ResidentRole r) {
		resident = r;
		if (!isApartment) {
			owner = r;
		}
	}
	
	public void removeResident(ResidentRole r) {
		if(resident == r) {
	           resident = null;
	        }
	}
	
	public void addFood(String t, int amt) {
		if (!fridge.isEmpty()) {
			for(Food f : fridge) {
				if(f.getType() == t) {
					f.addMore(amt);
				}
				
			}
		}
		
		
		Food f = new Food(t, amt);
		fridge.add(f);
		hasFood = true;
	}
	
	public void removeFood(String t) {
		for(Iterator<Food> it = fridge.iterator(); it.hasNext();) {
			Food f = it.next();
			if (f.getType() == t) {
				f.minusOne();
				if (f.noneLeft()) {
					if(resident.getPerson()!=null) {
						resident.getPerson().getMarketChecklist().add(new ItemRequest(f.getType(), 10));
						Do("I'm all out of "+f.getType()+". I will go get more when I get the chance.");
					}
					it.remove();
					if (fridge.isEmpty()) {
						hasFood = false;
					}	
				}
			}
			
		}
	}
	
	public void performMaintenance() {

		//perform maintenance actions

	}
	
	//Getters
	
	public Resident getResident() {
		return resident;
	}
	
	public Role getOwner() {
		return owner;
	}
	
	public List<Food> getFridgeContents() {
		return fridge;
	}
	
	public boolean isApartment() {
		//return owner.getPerson()!=((Role)resident).getPerson();
		return isApartment;
	}	

	public boolean hasFood() {
		for(ItemRequest i:resident.getPerson().getInventory()) { //
			addFood(i.item, i.amount);
		}
		resident.getPerson().getInventory().clear();
		return hasFood;
	}
	
	public boolean isOccupied() {
		return isOccupied;
	}
	
	public void setOccupied(boolean occupied) {
		this.isOccupied = occupied;
	}
	
	
	@Override
	public Role getRole(Intention role) {
		// TODO Auto-generated method stub
		if (role==Intention.ResidenceEat || role==Intention.ResidenceSleep || role==Intention.ResidencePayRent || role==Intention.ResidenceRelax) {
			return resident;
		}
		
		return null;
	}

	@Override
	public void updateTime(CityTime time) {
		// TODO Auto-generated method stub
		
	}
	
	public void updateConfigPanel() {
		if (configPanel != null) {
			configPanel.updateFridgeContents(this);
		}
	}
	
	public void setConfigPanel(ResidenceConfigPanel config) {
		configPanel = config;
	}
}
