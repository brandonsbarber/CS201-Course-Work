package cs201.roles.housingRoles;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.roles.residence.RenterGui;
import cs201.interfaces.roles.housing.Landlord;
import cs201.interfaces.roles.housing.Renter;
import cs201.roles.Role;

public class RenterRole extends Role implements Renter {
	Landlord myLandlord;
	double amtRentOwed = 0;
	RenterGui gui;
	
	// Messages
	
	public void msgRentDueYouOwe (double amt) {
		amtRentOwed += amt;
		stateChanged();
	}

	public void msgRentLateYouOweAdditional (double amt) {
		amtRentOwed += amt;
		stateChanged();
	}
	
	// Scheduler
	
	public boolean pickAndExecuteAnAction() {
		if (amtRentOwed>0) {
			if (getPerson().getMoney()>=amtRentOwed) {
				Do("I have "+getPerson().getMoney()+" on hand and I owe "+amtRentOwed+" for rent.");
				PayRent(myLandlord, amtRentOwed);
				return true;
			}
			else
			{
				//WithdrawMoneyPerson(amtRentOwed-(getPerson().getMoney()));
				Do("I don't have enough money on hand to pay my rent due.");
				return false; //temporary
			}
		}
		return false;
	}

	//Actions
	
	private void PayRent (Landlord l, double amt) {
		l.msgHereIsRentPayment(this, amt);
		amtRentOwed -= amt;
		getPerson().removeMoney(amt);
		Do("I paid my rent of "+amt+".");
	}

	@Override
	public void startInteraction(Intention intent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgClosingTime() {
		// TODO Auto-generated method stub
		
	}
	
	public void setLandlord(Landlord l) {
		myLandlord = l;
	}

}
