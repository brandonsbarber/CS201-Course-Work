package cs201.roles.housingRoles;

import cs201.interfaces.roles.housing.Resident;
import cs201.roles.Role;

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
			if (getPerson().home.hasFood()) {
				pickAndEatFromFridge();
			}
			else {
				getPerson().goToMarket();
			}	
		}
			
	}
	
	//Actions
	
	private void pickAndEatFromFridge() {
		state = ResidentState.eating;

		//picks food from home's fridge list of Food and eats it.

	}
	
	//Utility
	
}
