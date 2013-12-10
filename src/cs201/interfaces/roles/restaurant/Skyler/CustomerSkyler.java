package cs201.interfaces.roles.restaurant.Skyler;

import java.util.Map;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface CustomerSkyler {
		public abstract String getName();
        public abstract void msgHereIsYourChange(double change);
        public abstract void msgSitAtTable(Map<String, Double> newMenu);
        public abstract void msgWaiterBack();
        public abstract void msgReOrder(Map<String, Double> newMenu);
        public abstract void msgHereIsYourFood(String choice);
        public abstract void msgHereIsYourCheck(CashierSkyler c, double amt);
        
        public abstract void setWaiter(WaiterSkyler waiter);
        public abstract void setHost(HostSkyler host);
        

}