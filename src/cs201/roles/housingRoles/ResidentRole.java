package cs201.roles.housingRoles;

import java.util.List;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.Gui;
import cs201.gui.roles.residence.ResidentGui;
import cs201.interfaces.roles.housing.Resident;
import cs201.roles.Role;
import cs201.roles.marketRoles.MarketManagerRole.ItemRequest;
import cs201.structures.residence.Residence;
import cs201.structures.residence.Residence.Food;

public class ResidentRole extends Role implements Resident {
	public enum ResidentState {doingNothing, hungry, eating, readyToSleep, sleeping, readyToWakeUp, payingRent, relaxing};
	ResidentState state;
	private Residence residence;
	ResidentGui gui;
	boolean isTest = false;
	
	public ResidentRole() {
		state = ResidentState.doingNothing;
		//residence = (Residence) myPerson.getHome();  //would be helpful to connect residence to person's home
	}
	
	public ResidentRole(Residence res) {
		residence = res;
		state = ResidentState.doingNothing;
	}
	
	//Messages
	
	public void msgStartEating() {
		state = ResidentState.hungry;
		stateChanged();
	}

	public void msgDoneEating() { //from animation
		state = ResidentState.doingNothing;
		stateChanged();
	}
	
	public void msgAnimationDone() {
		myPerson.animationRelease();
	}
	
	//Scheduler
	
	public boolean pickAndExecuteAnAction() {
		switch (state) {
			case readyToSleep:
					goToSleep();
					return true;
			case hungry: 
				if (residence.hasFood()) {
					pickAndEatFromFridge();
					return true;
				}
				else {
					makeShoppingList();
					return false;
				}
			case relaxing:
				actionFinished();
				return true;
			default: 
				actionFinished(); //nothing to do. Leave.
				return false;
		}
	}
	
	//Actions
	
	/**
	 * Resident walks to the fridge, picks an item from the fridge's contents, goes to the table, and eats.
	 */
	private void pickAndEatFromFridge() {
		goToFridge();//animation go to fridge
		
		List<ItemRequest> inventory = myPerson.getInventory();
		
		if (inventory!=null && inventory.size()>0) {
			for (ItemRequest i: inventory) {
				residence.addFood(i.item, i.amount);
				Do("Added "+i.amount+" "+i.item+"s to my fridge from my inventory.");
				inventory.remove(i);
			}
		}
		
		state = ResidentState.eating;
		List<Food> fridgeContents = residence.getFridgeContents();
		//Do("My choices from the fridge: "+fridgeContents);
		//picks food from home's fridge list of Food and eats it. Temporarily random choice
		int rand = (int)(Math.random()*fridgeContents.size());
		String foodToEat = fridgeContents.get(rand).getType();
		residence.removeFood(foodToEat);
		if(!isTest) {
			gui.setHolding(foodToEat);
		}
		
		eatAtTable();//timer, gui animation
		
		if(!isTest) {
			gui.clearHolding();
		}
		
		myPerson.setHungerLevel(0); //clear hunger amount
		Do("Finished pickAndEatFromFridge action. I ate one serving of "+foodToEat+"s from my fridge.");
		actionFinished();
	}
	
	/**
	 * Action to walk to the Resident's bed and go to sleep. Resident sets his role inactive so scheduler calls
	 * don't happen until he is woken up with a new action from PersonAgent's scheduler
	 */
	private void goToSleep() {
		Do("Going to sleep");
		goToBed(); //animation go to bed
		gui.setHolding("Zzz");
		state = ResidentState.sleeping;
		isActive = false;
		//timer/wait for wakeup
	}

	@Override
	public void startInteraction(Intention intent) {
		
		switch (intent) {
			case ResidenceEat:
				this.msgStartEating();
				break;
			case ResidenceSleep:
				state = ResidentState.readyToSleep;
				stateChanged();
				break;
			case ResidenceRelax:
				state = ResidentState.relaxing;
				stateChanged();
				break;
			default:
				break;
		}
		
		Do("Entering residence");
		if(!isTest) {
			this.gui.setPresent(true);
			gui.clearHolding();
			gui.enter();
			this.acquireSemaphore();
			if (intent == Intention.ResidenceRelax) {
				goToCouch();
			}
		}
		
	}
	
	/**
	 * Action called when another action has finished. Resident's actions are based in the intent when
	 * entering the residence. When the action described by that intent is finished, the resident will
	 * simply leave. He can re-enter if he decides to do something else in the residence.
	 */
	private void actionFinished() {
		gui.clearHolding();
		Do("Action finished. Leaving.");
		state = ResidentState.doingNothing;
		isActive = false;
		if(!isTest) {
			gui.exit(); //animation to leave residence
			this.acquireSemaphore();
			gui.setPresent(false); //gui inactive
		}
		
	}
	
	private void goToFridge() { //animation
		Do("Going to the fridge.");
		if(!isTest) {
			gui.walkToFridge();
			this.acquireSemaphore();
		}
	}
	
	private void eatAtTable() {
		Do("Going to the table to eat.");
		if(!isTest) {
			gui.walkToTable();
			this.acquireSemaphore();
		}
	}
	
	private void goToBed() {
		if(!isTest) {
			gui.goToBed();
			this.acquireSemaphore();
		}
		
	}
	
	private void goToCouch() {
		if(!isTest) {
			gui.goToCouch();
			this.acquireSemaphore();
		}
		
	}

	@Override
	public void msgClosingTime() {
		// TODO Auto-generated method stub
		//NOT A JOB. EMPTY
	}
	
	//Utility
	
	public void setResidence(Residence newResidence) {
		residence = newResidence;
	}
	
	public void setGui(ResidentGui newGui) {
		gui = newGui;
	}
	
	public Gui getGui() {
		return gui;
	}
	
	public ResidentState getState() {
		return state;
	}
	
	public void setTest(boolean bool) {
		isTest = bool;
	}
}
