package cs201.roles.housingRoles;

import cs201.agents.PersonAgent.Intention;
import cs201.interfaces.roles.housing.Landlord;
import cs201.interfaces.roles.housing.Renter;
import cs201.structures.residence.Residence;

public class RenterRole extends ResidentRole implements Renter {
	Landlord myLandlord;
	double amtRentOwed = 0;
	
	public RenterRole() {
		super();
	}
	
	public RenterRole(Residence r) {
		super(r);
	}
	
	// Messages
	
	public void msgRentDueYouOwe (Landlord l, double amt) {
		amtRentOwed += amt;
		stateChanged();
	}

	public void msgRentLateYouOweAdditional (Landlord l, double amt) {
		amtRentOwed += amt;
		stateChanged();
	}
	
	// Scheduler
	
	public boolean pickAndExecuteAnAction() {
		if(state==ResidentState.payingRent) {
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
			else {
				Do("I don't need to pay any rent today.");
			}
		}
		return super.pickAndExecuteAnAction();
	}

	//Actions
	
	/**
	 * Sends rent payment message to landlord and subtracts from person's money supply.
	 * @param l Landlord to pay rent to
	 * @param amt Amount of rent to pay
	 */
	private void PayRent (Landlord l, double amt) {
		l.msgHereIsRentPayment(this, amt);
		amtRentOwed -= amt;
		getPerson().removeMoney(amt);
		Do("I paid my rent of "+amt+".");
	}
	
	@Override
	public void startInteraction(Intention intent) {
		if (intent == Intention.ResidencePayRent){
			state = ResidentState.payingRent;
			stateChanged();
		}
		super.startInteraction(intent);
	}

	
	public void setLandlord(Landlord l) {
		myLandlord = l;
	}
	
	public Landlord getLandlord() {
		return myLandlord;
	}

	public double getAmtOwed() {
		return amtRentOwed;
	}

}
