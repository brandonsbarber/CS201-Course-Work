package cs201.roles.housingRoles;

import java.util.ArrayList;
import java.util.List;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.Gui;
import cs201.gui.roles.residence.LandlordGui;
import cs201.gui.roles.residence.ResidentGui;
import cs201.helper.CityTime.WeekDay;
import cs201.interfaces.roles.housing.Landlord;
import cs201.interfaces.roles.housing.Renter;
import cs201.roles.Role;
import cs201.roles.housingRoles.LandlordRole.RentState;
import cs201.structures.residence.Residence;

public class LandlordRole extends Role implements Landlord {
	List<myProperty> myProperties = new ArrayList<myProperty>();
	double latePenalty = 40;
	LandlordGui gui;
	boolean closingTime;
	
	public enum RentState {notDue, dueNotNotified, dueNotified, lateNotNotified, lateNotified, paid};
	
	public class myProperty {
		Residence residence;
	    Renter renter;
	    double amtDue;
	    WeekDay dayDue;
	    RentState state;
		boolean needsMaintenance;

	    public myProperty(Residence res, Renter ren, double amt, WeekDay d) {
	        	residence = res;
	            renter = ren;
	            amtDue = amt;
	            dayDue = d;
	            state = RentState.notDue;
	        }
		
		public boolean needsMaintenance() {
			return needsMaintenance;
		}
		
		public void performMaintenance() {
			residence.performMaintenance();
			needsMaintenance = false;
		}

		public RentState getState() {
			return state;
		}
	}
	
	public LandlordRole() {
		
	}
	
	
	//Messages
	
	public void msgHereIsRentPayment(Renter r, double amt) {
		for (myProperty mP : myProperties) {
			if(mP.renter == r) {
				mP.state = RentState.paid;
				myPerson.addMoney(amt);
			}
		}
		stateChanged();
	}

	public void msgPropertyNeedsMaintenance(Renter r, Residence res) {
		for (myProperty mP : myProperties) {
			if (mP.residence == res) {
				mP.needsMaintenance = true;
			}
		}
		stateChanged();
	}
	
	@Override
	public void msgClosingTime() {
		closingTime=true;
		stateChanged();
	}	
	
	public void msgAnimationDone() {
		myPerson.animationRelease();
	}
	
	// Scheduler
	
	public boolean pickAndExecuteAnAction() {
		if (closingTime) {
			exitOffice();
			return false;
		}
		
		int currentDay = myPerson.getTime().day.ordinal();
		currentDay = (currentDay + 1) % WeekDay.values().length;
		WeekDay tomorrow = WeekDay.values()[currentDay];
		
		for (myProperty mP:myProperties) {
			if (tomorrow != mP.dayDue && mP.state == RentState.paid) {
				mP.state = RentState.notDue;
				return true;
			}
		}
		for (myProperty mP:myProperties) {
			if (tomorrow == mP.dayDue && mP.state == RentState.notDue) {
				mP.state = RentState.dueNotNotified;
				return true;
			}
		}
		
		for (myProperty mP:myProperties) {
			if (myPerson.getTime().day == mP.dayDue && mP.state == RentState.dueNotified) {
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
		Do("Requesting rent of "+mP.amtDue+" from "+mP.renter.toString());
		mP.state = RentState.dueNotified;
        mP.renter.msgRentDueYouOwe(this, mP.amtDue);
	}

	private void RequestLateRent(myProperty mP) {
		Do("Requesting additional late rent penalty of "+latePenalty+" from "+mP.renter.toString());
		mP.state = RentState.lateNotified;
        mP.renter.msgRentLateYouOweAdditional(this, latePenalty);
	}

	private void FixProperty(myProperty mP) {
	//perform maintenance task on property
		Do("Performing maintenance.");
        mP.performMaintenance();
	}
	
	private void goToDesk() { //animation
		Do("Walking to my desk.");
		gui.sitAtDesk();
		this.acquireSemaphore();
	}
	
	private void exitOffice() { // animation
		Do("Exiting my office");
		isActive = false;
		myPerson.goOffWork();
		gui.leaveOffice();
		this.acquireSemaphore();
		gui.setPresent(false);
	}
	
	private void enter() {
		Do("Entering my Apartment Complex Office");
		//isActive = true;
		this.gui.setPresent(true);
		gui.enter();
		this.acquireSemaphore();
		Do("Passed semaphore in role enter()");
	}
	
	// Utilities
	
	public void addProperty(Residence res, Renter renter, double rentAmount, WeekDay rentDueDate) {
		Do("Added a new property to my list of properties.");
        myProperty mP = new myProperty(res, renter, rentAmount, rentDueDate);
        myProperties.add(mP);
	}

	@Override
	public void startInteraction(Intention intent) {
		// TODO Auto-generated method stub
		if (intent == Intention.ResidenceLandLord) {
			//stub
			enter();
			goToDesk();
		}
		
	}

	
	public void setGui(LandlordGui newGui) {
		gui = newGui;
	}
	
	public Gui getGui() {
		return gui;
	}

	public List<myProperty> getMyProperties() {
		return myProperties;
	}
}
