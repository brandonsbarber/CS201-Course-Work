package cs201.test.mock.housing;

import cs201.interfaces.roles.housing.Renter;
import cs201.test.mock.Mock;

public class MockRenter extends Mock implements Renter {

	public MockRenter(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgRentDueYouOwe(double amt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgRentLateYouOweAdditional(double amt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgStartEating() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDoneEating() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationDone() {
		// TODO Auto-generated method stub
		
	}

}
