package cs201.roles.housingRoles;

import cs201.agents.PersonAgent.Intention;
import cs201.interfaces.roles.housing.Landlord;
import cs201.interfaces.roles.housing.Renter;
import cs201.roles.Role;

public class RenterRole extends Role implements Renter {
	Landlord myLandlord;
	double amtRentOwed;
	
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
				PayRent(myLandlord, amtRentOwed);
				return true;
			}
			else
			{
				//WithdrawMoneyPerson(amtRentOwed-(getPerson().getMoney()));
				return false;
			}
		}
		return false;
	}

	//Actions
	
	private void PayRent (Landlord l, double amt) {
		l.msgHereIsRentPayment(this, amt);
		amtRentOwed -= amt;
		getPerson().removeMoney(amt);
	}

	@Override
	public void startInteraction(Intention intent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgClosingTime() {
		// TODO Auto-generated method stub
		
	}

}
