package cs201.test.mock.Matt;

import cs201.gui.roles.restaurant.Matt.CustomerGuiMatt;
import cs201.helper.Matt.MenuMatt;
import cs201.interfaces.roles.restaurant.Matt.CashierMatt;
import cs201.interfaces.roles.restaurant.Matt.CustomerMatt;
import cs201.interfaces.roles.restaurant.Matt.HostMatt;
import cs201.interfaces.roles.restaurant.Matt.WaiterMatt;
import cs201.test.mock.LoggedEvent;
import cs201.test.mock.Mock;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Matthew Pohlmann
 *
 */
public class MockCustomerMatt extends Mock implements CustomerMatt {

        /**
         * Reference to the Cashier under test that can be set by the unit test.
         */
        public CashierMatt cashier;
        public HostMatt host;

        public MockCustomerMatt(String name) {
                super(name);
        }

		@Override
		public CustomerGuiMatt getGui() {
			log.add(new LoggedEvent("Customer " + this.name + ": getGui(). No GUI to set."));
			return null;
		}

		@Override
		public void msgIsHungry() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgAboutToBeSeated() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgFollowMeToTable(WaiterMatt waiter, MenuMatt menu) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgWhatWouldYouLike() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgHereIsYourFood(String choice) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgOutOfYourChoice(MenuMatt m) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgHereIsYourCheck(double price) {
			log.add(new LoggedEvent("Customer " + this.name + ": Received msgHereIsYourCheck from Waiter. Amount = " + price));
		}

		@Override
		public void msgHereIsYourChange(double change) {
			log.add(new LoggedEvent("Customer " + this.name + ": Received msgHereIsYourChange from Cashier. Change = " + change));
		}
        
        
}