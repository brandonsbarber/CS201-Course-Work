package cs201.roles.marketRoles;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cs201.agents.PersonAgent.Intention;
import cs201.interfaces.roles.market.MarketEmployee;
import cs201.interfaces.roles.market.MarketManager;
import cs201.roles.Role;
import cs201.roles.marketRoles.MarketManagerRole.ItemRequest;

public class MarketEmployeeRole extends Role implements MarketEmployee {
	
	/*
	 * ********** DATA **********
	 */
	String name = "";
	
	enum OrderState {PENDING, PROCESSED};
	private class Order {
		List<String> items;
		int id;
		OrderState state;
		MarketManager manager;
		
		public Order(MarketManager m, List<String> i, int id, OrderState s) {
			items = i;
			id = id;
			state = s;
			manager = m;
		}
	}
	
	
	/*
	 * ********** CONSTRUCTORS **********
	 */
	
	public MarketEmployeeRole() {
		this("");
	}
	
	public MarketEmployeeRole(String n) {
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
	
	public void msgRetrieveItems(MarketManager manager, List<ItemRequest> items, int id) {
		stateChanged();
	}

	public void closingTime() {
		// TODO Auto-generated method stub
		
	}
	
	public void startInteraction(Intention intent) {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * ********** ACTIONS **********
	 */
	
	private void ProcessOrder(Order o) {
		
	}

	/*
	 * ********** UTILITY **********
	 */

}
