package cs201.test.mock.housing;

import cs201.interfaces.roles.housing.Landlord;
import cs201.interfaces.roles.housing.Renter;
import cs201.structures.residence.Residence;
import cs201.test.mock.LoggedEvent;
import cs201.test.mock.Mock;

public class MockLandlord extends Mock implements Landlord {

	public MockLandlord(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgHereIsRentPayment(Renter r, double amt) {
		// TODO Auto-generated method stub
		String string = "received msgHereIsRentPayment. Renter: "+r.toString()+" Amount: "+amt;
		System.out.println(string);
		log.add(new LoggedEvent(string));
	}

}
