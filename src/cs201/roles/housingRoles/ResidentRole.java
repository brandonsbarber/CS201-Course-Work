package cs201.roles.housingRoles;

import java.util.List;

import cs201.agents.PersonAgent.Intention;
import cs201.interfaces.roles.housing.Resident;
import cs201.roles.Role;
import cs201.structures.residence.Residence;

public class ResidentRole extends Role implements Resident {
	enum ResidentState {doingNothing, hungry, eating, readyToSleep, sleeping, readyToWakeUp};
	ResidentState state;
	private Residence residence;
	
	public ResidentRole() {
		state = ResidentState.doingNothing;
		//residence = (Residence) myPerson.getHome();  //would be helpful to connect residence to person's home
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
		//animation go to fridge
		state = ResidentState.eating;
		List<String> fridgeContents = residence.getFridgeContents();
		//picks food from home's fridge list of Food and eats it. Temporarily random choice
		int rand = (int)Math.random()*fridgeContents.size();
		residence.removeFood(fridgeContents.get(rand));
		
		//timer, gui animation
		myPerson.setHungerLevel(0); //clear hunger amount
		Do("finished pickAndEatFromFridge action");
	}
	
	private void goToSleep() {
		//animation go to bed
		state = ResidentState.sleeping;
		//timer/wait for wakeup
	}

	@Override
	public void startInteraction(Intention intent) {
		// TODO Auto-generated method stub
		if (intent == Intention.ResidenceEat) {
			this.msgStartEating();
		}
		if (intent == Intention.ResidenceSleep) {
			state = ResidentState.readyToSleep;
			stateChanged();
			//action to prepare scheduler for sleep action
		}
	}

	@Override
	public void closingTime() {
		// TODO Auto-generated method stub
		
	}
	
	//Utility
	
	public void setResidence(Residence newResidence) {
		residence = newResidence;
	}
}
