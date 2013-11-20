package cs201.roles.housingRoles;

import cs201.interfaces.Landlord;
import cs201.interfaces.Renter;
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
			}
			else {
			WithdrawMoneyPerson(amtRentOwed-(getPerson().getMoney()));
			}
		}
	}

	//Actions
	
	private void PayRent (Landlord l, double amt) {
		l.msgHereIsRentPayment(this, amt);
		amtRentOwed -= amt;
		getPerson().removeMoney(amt);
	}

}
