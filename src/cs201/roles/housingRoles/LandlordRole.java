package cs201.roles.housingRoles;

import java.util.ArrayList;
import java.util.List;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.roles.residence.LandlordGui;
import cs201.helper.CityTime.WeekDay;
import cs201.interfaces.roles.housing.Landlord;
import cs201.interfaces.roles.housing.Renter;
import cs201.roles.Role;
import cs201.structures.residence.Residence;

public class LandlordRole extends Role implements Landlord {
	List<myProperty> myProperties = new ArrayList<myProperty>();
	double latePenalty;
	LandlordGui gui;
	
	enum RentState {notDue, dueNotNotified, dueNotified, lateNotNotified, lateNotified, paid};
	
	private class myProperty {
		Residence residence;
	    Renter renter;
	    double amtDue;
	    WeekDay dayDue;
	    RentState state;
		boolean needsMaintenance;

	    public myProperty(Residence res, Renter ren, double a, WeekDay d) {
	        	residence = res;
	            renter = ren;
	            amtDue = a;
	            dayDue = d;
	        }
		
		public boolean needsMaintenance() {
			return needsMaintenance;
		}
		
		public void performMaintenance() {
			residence.performMaintenance();
			needsMaintenance = false;
		}
	}
	
	//Messages
	
	public void msgHereIsRentPayment(Renter r, double amt) {
		for (myProperty mP : myProperties) {
			if(mP.renter == r) {
				mP.state = RentState.paid;
				myPerson.addMoney(amt);
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
			if (WeekDay.Sunday == mP.dayDue && mP.state == RentState.notDue) {
				mP.state = RentState.dueNotNotified;
				return true;
			}
		}
		
		for (myProperty mP:myProperties) {
			if (/*getToday()*/WeekDay.Sunday == mP.dayDue && mP.state == RentState.dueNotified) {
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
        mP.performMaintenance();
	}
	
	private void goToDesk() { //animation
		gui.walkToDesk();
	}
	
	private void exitOffice() { // animation
		gui.leaveOffice();
	}
	
	// Utilities
	
	public void addProperty(Residence res, Renter renter, double rentAmount, WeekDay rentDueDate) {
        myProperty mP = new myProperty(res, renter, rentAmount, rentDueDate);
        myProperties.add(mP);
	}

	@Override
	public void startInteraction(Intention intent) {
		// TODO Auto-generated method stub
		if (intent == Intention.ResidenceLandLord) {
			//stub
		}
		
	}

	@Override
	public void closingTime() {
		// TODO Auto-generated method stub
		
	}
}
