package cs201.roles.housingRoles;

import java.util.List;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.roles.residence.ResidentGui;
import cs201.interfaces.roles.housing.Resident;
import cs201.roles.Role;
import cs201.structures.residence.Residence;

public class ResidentRole extends Role implements Resident {
	enum ResidentState {doingNothing, hungry, eating, readyToSleep, sleeping, readyToWakeUp, payingRent};
	ResidentState state;
	private Residence residence;
	ResidentGui gui;
	
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
					//getPerson().goToMarket();
					Do("I need to get food from the market. I need to implement some way to do that.");
					return false;
				}
			default: 
				break;
		}
		return false;
	}
	
	//Actions
	
	private void pickAndEatFromFridge() {
		Do("pickAndEatFromFridge called.");
		goToFridge();//animation go to fridge
		state = ResidentState.eating;
		List<String> fridgeContents = residence.getFridgeContents();
		//picks food from home's fridge list of Food and eats it. Temporarily random choice
		int rand = (int)Math.random()*fridgeContents.size();
		residence.removeFood(fridgeContents.get(rand));
		
		//timer, gui animation
		myPerson.setHungerLevel(0); //clear hunger amount
		Do("finished pickAndEatFromFridge action");
		actionFinished();
	}
	
	private void goToSleep() {
		goToBed(); //animation go to bed
		state = ResidentState.sleeping;
		isActive = false;
		//timer/wait for wakeup
	}

	@Override
	public void startInteraction(Intention intent) {
		if (intent == Intention.ResidenceEat) {
			this.msgStartEating();
		}
		if (intent == Intention.ResidenceSleep) {
			state = ResidentState.readyToSleep;
			stateChanged();
			//action to prepare scheduler for sleep action
		}
		if (intent == Intention.ResidencePayRent){
			state = ResidentState.payingRent;
			stateChanged();
		}
	}
	
	private void actionFinished() {
		isActive = false;
		//animation to leave residence
		//gui inactive
	}
	
	private void goToFridge() { //animation
		gui.walkToFridge();
	}
	
	private void goToBed() {
		gui.goToBed();
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
}
