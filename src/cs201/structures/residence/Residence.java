package cs201.structures.residence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import cs201.agents.PersonAgent;
import cs201.agents.PersonAgent.Intention;
import cs201.gui.StructurePanel;
import cs201.helper.CityTime;
import cs201.interfaces.roles.housing.Resident;
import cs201.roles.Role;
import cs201.structures.Structure;

public class Residence extends Structure {
	private Resident resident;
	private Role owner; //owner is either a resident or a landlord.
	private List<Food> fridge = Collections.synchronizedList(new ArrayList<Food>());
	private boolean hasFood;
	
	private class Food {
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
	
	public Residence(int x, int y, int width, int height, int id, StructurePanel p) {
	    super(x, y, width, height, id, p);
	    owner = null;
	    resident = null;
	    addFood("Food 1", 10);
	    addFood("Food 2", 10);
	    addFood("Food 3", 10);
	    addFood("Food 4", 10);
	    addFood("Food 5", 10);
	    addFood("Food 6", 10);
	}
	
	//Setters
	
	public void setOwner(Role p) {
		owner = p;
	}
	
	public void setResident(Resident r) {
		resident = r;
	}
	
	public void removeResident(Resident r) {
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
				System.out.println("One of food removed");
				if (f.noneLeft()) {
					it.remove();
					System.out.println("Food category removed");
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
	
	public List<String> getFridgeContents() {
		if (fridge.isEmpty()) {
			return null;
		}
		List<String> contents = new ArrayList<String>();
		for (Food f:fridge) {
			if(!contents.contains(f.getType())) {
				contents.add(f.getType());
			}
		}
		return contents;
	}
	
	public boolean isApartment() {
		return owner.getPerson()!=((Role)resident).getPerson();
	}	

	public boolean hasFood() {
		return hasFood;
	}
	
	
	@Override
	public Role getRole(Intention role) {
		// TODO Auto-generated method stub
		if (role==Intention.ResidenceEat) {
			return (Role) resident;
		}
		if (role==Intention.ResidenceSleep) {
			return (Role) resident;
		}
		if (role==Intention.ResidenceLandLord) {
			return owner;
		}
		return null;
	}

	@Override
	public void updateTime(CityTime time) {
		// TODO Auto-generated method stub
		
	}
}
