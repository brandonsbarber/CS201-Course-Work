package cs201.roles.housingRoles;

import cs201.agents.PersonAgent.Intention;
import cs201.interfaces.roles.housing.Resident;
import cs201.roles.Role;
import cs201.structures.residence.Residence;

public class ResidentRole extends Role implements Resident {
	enum ResidentState {doingNothing, hungry, eating};
	ResidentState state;
	
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
		if (state == ResidentState.hungry) {
			if (((Residence)(myPerson.getHome())).hasFood()) {
				pickAndEatFromFridge();
				return true;
			}
			else {
				//getPerson().goToMarket();
				return false;
			}	
		}
		return false;
	}
	
	//Actions
	
	private void pickAndEatFromFridge() {
		state = ResidentState.eating;

		//picks food from home's fridge list of Food and eats it.

	}

	@Override
	public void startInteraction(Intention intent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgClosingTime() {
		// TODO Auto-generated method stub
		
	}
	
	//Utility
	
}
