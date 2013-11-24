package cs201.structures.residence;

import java.util.List;

import cs201.agents.PersonAgent;
import cs201.agents.PersonAgent.Intention;
import cs201.helper.CityTime;
import cs201.interfaces.roles.housing.Resident;
import cs201.roles.Role;
import cs201.structures.Structure;

public class Residence extends Structure {
	private Resident resident;
	private PersonAgent owner;
	private List<Food> fridge;
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
	
	public Residence(int x, int y, int width, int height, int id) {
	    super(x, y, width, height, id);
	    owner = null;
	    resident = null;
	}
	
	//Setters
	
	public void setOwner(PersonAgent p) {
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
		for(Food f : fridge) {
			if(f.getType() == t) {
				f.addMore(amt);
			}
			
		}
		
		Food f = new Food(t, amt);
		fridge.add(f);
		hasFood = true;
	}
	
	public void removeFood(String t) {
		for(Food f : fridge) {
			if(f.getType() == t) {
				f.minusOne();
				if (f.noneLeft()) {
					fridge.remove(f);
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
	
	public PersonAgent getOwner() {
		return owner;
	}
	
	public boolean isApartment() {
		return owner!=resident;
	}	

	public boolean hasFood() {
		return hasFood;
	}
	
	@Override
	public Role getRole(Intention role) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateTime(CityTime time) {
		// TODO Auto-generated method stub
		
	}
}
