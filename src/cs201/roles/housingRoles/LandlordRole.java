package cs201.roles.housingRoles;

import java.util.List;

import cs201.agents.PersonAgent.Intention;
import cs201.interfaces.roles.housing.Landlord;
import cs201.interfaces.roles.housing.Renter;
import cs201.roles.Role;
import cs201.structures.Residence;

public class LandlordRole extends Role implements Landlord {
	List<myProperty> myProperties;
	double latePenalty;
	
	enum RentState {notDue, dueNotNotified, dueNotified, lateNotNotified, lateNotified, paid};
	
	private class myProperty {
		Residence residence;
	    Renter renter;
	    double amtDue;
	    WeekDay dayDue;
	    RentState state;
		boolean needsMaintenance;

	        myProperty(Residence res, Renter ren, double a, Date d) {
	        	residence = res;
	            renter = ren;
	            amtDue = a;
	            dayDue = d;
	        }
		
		public boolean needsMaintenance() {
			return needsMaintenance;
		}
	}
	
	//Messages
	
	public void msgHereIsRentPayment(Renter r, double amt) {
		for (myProperty mP : myProperties) {
			if(mP.renter == r) {
				mP.state = RentState.paid;
			}
		}
	}

	public void msgPropertyNeedsMaintenance(Renter r, Residence res) {
		for (myProperty mP : myProperties) {
			if (mP.residence == res) {
				mP.needsMaintenance = true;
			}
		}
	}
	
	// Scheduler
	
	public boolean pickAndExecuteAnAction() {
		for (myProperty mP:myProperties) {
			if (mP.state == RentState.paid) {
				mP.state = RentState.notDue;
				return true;
			}
		}
		for (myProperty mP:myProperties) {
			if (getTomorrow() == mP.dayDue && mP.state == RentState.notDue) {
				mP.state = RentState.dueNotNotified;
				return true;
			}
		}
		
		for (myProperty mP:myProperties) {
			if (getToday() == mP.dayDue && mP.state == RentState.dueNotified) {
				mP.state = RentState.lateNotNotified;
				return true;
			}
		}
		
		for (myProperty mP:myProperties) {
			if (mP.state == RentState.dueNotNotified) {
				RequestRent(mP);
				return true;
			}
		}

		for (myProperty mP:myProperties) {
			if (mP.state == RentState.lateNotNotified) {
				RequestLateRent(mP);
				return true;
			}
		}

		for (myProperty mP:myProperties) {
			if (mP.needsMaintenance()) {
				FixProperty(mP);
				return true;
			}
		}
		
		return false;
	}
	
	//Actions
	
	private void RequestRent(myProperty mP) {
        mP.renter.msgRentDueYouOwe(mP.amtDue);
        mP.state = RentState.dueNotified;
	}

	private void RequestLateRent(myProperty mP) {
        mP.renter.msgRentLateYouOweAdditional(latePenalty);
        mP.state = RentState.lateNotified;
	}

	private void FixProperty(myProperty mP) {
	//perform maintenance task on property
        mP.residence.performMaintenance(); //will likely be more specific
	}
	
	// Utilities
	
	public void addProperty(Residence res, Renter renter, double rentAmount, Date rentDueDate) {
        myProperty mP = new myProperty(res, renter, rentAmount, rentDueDate);
        myProperties.add(mP);
	}

	@Override
	public void startInteraction(Intention intent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closingTime() {
		// TODO Auto-generated method stub
		
	}
}
