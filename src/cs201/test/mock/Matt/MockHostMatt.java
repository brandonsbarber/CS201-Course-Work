package cs201.test.mock.Matt;

import java.util.ArrayList;
import java.util.List;

import cs201.interfaces.roles.restaurant.Matt.CustomerMatt;
import cs201.interfaces.roles.restaurant.Matt.HostMatt;
import cs201.interfaces.roles.restaurant.Matt.WaiterMatt;
import cs201.test.mock.LoggedEvent;
import cs201.test.mock.Mock;

public class MockHostMatt extends Mock implements HostMatt {

	public List<CustomerMatt> bannedCustomers = new ArrayList<CustomerMatt>();
	
	public MockHostMatt(String name) {
		super(name);
	}

	@Override
	public void msgIWantToEat(CustomerMatt c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWaitTimeTooLong(CustomerMatt c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgTableIsFree(WaiterMatt w, int tNum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPermissionForBreak(WaiterMatt w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOffBreak(WaiterMatt w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgBanThisCustomer(CustomerMatt c) {
		bannedCustomers.add(c);
		log.add(new LoggedEvent("Host " + this.name + ": Received msgBanThisCustomer from Cashier."));
	}

	@Override
	public void msgCustomerRetrievedFromWaitingArea() {
		// TODO Auto-generated method stub
		
	}

}
