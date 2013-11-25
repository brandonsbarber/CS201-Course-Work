package cs201.roles.marketRoles;

import java.util.ArrayList;
import java.util.List;

import cs201.agents.PersonAgent.Intention;
import cs201.interfaces.roles.market.MarketConsumer;
import cs201.interfaces.roles.market.MarketEmployee;
import cs201.interfaces.roles.market.MarketManager;
import cs201.roles.Role;
import cs201.roles.marketRoles.MarketManagerRole.Item;

public class MarketConsumerRole extends Role implements MarketConsumer {
	
	/*
	 * ********** DATA **********
	 */
	String name = "";
	List<MarketBill> marketBills;
	
	enum MarketBillState {OUTSTANDING, PAID};
	class MarketBill {
		MarketManager manager;
		float amount;
		MarketBillState state;
	}
	
	/*
	 * ********** CONSTRUCTORS **********
	 */
	
	public MarketConsumerRole() {
		this("");
	}
	
	public MarketConsumerRole(String n) {
		name = n;
	}
	
	/*
	 * ********** SCHEDULER **********
	 */
	
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	
	/*
	 * ********** MESSAGES **********
	 */
	
	public void msgHereIsYourTotal(MarketManager manager, float amount) {
		stateChanged();
	}
	
	public void msgHereAreYourItems(List<Item> items) {
		stateChanged();
	}
	
	public void startInteraction(Intention intent) {
		// TODO Auto-generated method stub
		
	}

	public void msgClosingTime() {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * ********** ACTIONS **********
	 */
	
	private void PayBill(MarketBill mb) {
		
	}

	/*
	 * ********** UTILITY **********
	 */

}