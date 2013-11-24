package cs201.roles.housingRoles;

import cs201.agents.PersonAgent.Intention;
import cs201.interfaces.roles.housing.Resident;
import cs201.roles.Role;
import cs201.structures.residence.Residence;

public class ResidentRole extends Role implements Resident {
	enum ResidentState {doingNothing, hungry, eating, readyToSleep, sleeping, readyToWakeUp};
	ResidentState state;
	
	public ResidentRole() {
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
			if (((Residence)(myPerson.getHome())).hasFood()) {
				pickAndEatFromFridge();
				return true;
			}
			else {
				//getPerson().goToMarket();
				return false;
			}	
		default: 
			break;
		}
		return false;
	}
	
	//Actions
	
	private void pickAndEatFromFridge() {
		state = ResidentState.eating;

		//picks food from home's fridge list of Food and eats it.
		
	}
	
	private void goToSleep() {
		//go to bed
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
	
}
