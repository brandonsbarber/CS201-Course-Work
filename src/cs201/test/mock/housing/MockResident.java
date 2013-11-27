package cs201.test.mock.housing;

import cs201.interfaces.roles.housing.Resident;
import cs201.test.mock.LoggedEvent;
import cs201.test.mock.Mock;

public class MockResident extends Mock implements Resident {

	public MockResident(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgStartEating() {
		// TODO Auto-generated method stub
		String string = "received msgStartEating";
		System.out.println(string);
		log.add(new LoggedEvent(string));
	}

	@Override
	public void msgDoneEating() {
		// TODO Auto-generated method stub
		String string = "received msgDoneEating";
		System.out.println(string);
		log.add(new LoggedEvent(string));
	}

	@Override
	public void msgAnimationDone() {
		// TODO Auto-generated method stub
		String string = "received msgAnimationDone";
		System.out.println(string);
		log.add(new LoggedEvent(string));
	}

}
