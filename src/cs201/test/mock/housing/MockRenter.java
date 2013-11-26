package cs201.test.mock.housing;

import cs201.interfaces.roles.housing.Landlord;
import cs201.interfaces.roles.housing.Renter;
import cs201.test.mock.LoggedEvent;

public class MockRenter extends MockResident implements Renter {

	public MockRenter(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgRentDueYouOwe(Landlord l, double amt) {
		// TODO Auto-generated method stub
		String string = "received msgRentDueYouOwe. Amount: "+amt;
		System.out.println(string);
		log.add(new LoggedEvent(string));
		
		//automatically pay
		l.msgHereIsRentPayment(this, amt);
	}

	@Override
	public void msgRentLateYouOweAdditional(Landlord l, double amt) {
		// TODO Auto-generated method stub
		String string = "received msgRentLateYouOweAdditional. Amount: "+amt;
		System.out.println(string);
		log.add(new LoggedEvent(string));
		
		//automaticaly pay
		l.msgHereIsRentPayment(this, amt);
	}

}
