package cs201.test.mock.Matt;

import cs201.interfaces.roles.restaurant.Matt.CashierMatt;
import cs201.interfaces.roles.restaurant.Matt.CustomerMatt;
import cs201.interfaces.roles.restaurant.Matt.HostMatt;
import cs201.interfaces.roles.restaurant.Matt.WaiterMatt;
import cs201.test.mock.LoggedEvent;
import cs201.test.mock.Mock;

public class MockWaiterMatt extends Mock implements WaiterMatt {

	public CashierMatt cashier;
    public HostMatt host;

    public MockWaiterMatt(String name) {
            super(name);
    }

	@Override
	public void msgSeatCustomer(int tNum, CustomerMatt c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReadyToOrder(CustomerMatt c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsMyChoice(CustomerMatt c, String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderIsReady(String choice, int tableNum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOutOfFood(String choice, int tableNum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDoneEatingAndNeedCheck(CustomerMatt c) {
		log.add(new LoggedEvent("Waiter " + this.name + ": Received msgDoneEatingAndNeedCheck from Customer."));
		cashier.msgComputeCheck(this, c, "Steak");
	}

	@Override
	public void msgHereIsCheck(CustomerMatt c, double check) {
		log.add(new LoggedEvent("Waiter " + this.name + ": Received msgHereIsCheck from Cashier."));
		c.msgHereIsYourCheck(check);
	}

	@Override
	public void msgPayingAndLeaving(CustomerMatt c) {
		log.add(new LoggedEvent("Waiter " + this.name + ": Received msgPayingAndLeaving from Customer."));
	}

	@Override
	public void msgAskToGoOnBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgBreakAllowed(boolean breakAllowed) {
		// TODO Auto-generated method stub
		
	}

}
