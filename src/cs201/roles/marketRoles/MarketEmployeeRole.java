package cs201.roles.marketRoles;

import java.util.ArrayList;
import java.util.Collections;
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
	List<RetrievalRequest> requests = Collections.synchronizedList(new ArrayList<RetrievalRequest>());
	
	enum RetrievalState {PENDING, PROCESSED};
	private class RetrievalRequest {
		List<ItemRequest> items;
		int id;
		RetrievalState state;
		MarketManager manager;
		
		public RetrievalRequest(MarketManager m, List<ItemRequest> i, int id, RetrievalState s) {
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
		// If there's a PENDING retrieval request, process it
		RetrievalRequest pendingRequest = null;
		pendingRequest = findNextPendingRetrievalRequest();
		if (pendingRequest != null) {
			processRequest(pendingRequest);
			return true;
		}
		
		return false;
	}

	
	/*
	 * ********** MESSAGES **********
	 */
	
	public void msgRetrieveItems(MarketManager manager, List<ItemRequest> items, int id) {
		// Add the new retrieval request to the list of requests
		requests.add(new RetrievalRequest(manager, items, id, RetrievalState.PENDING));
		
		stateChanged();
	}

	public void closingTime() {
		
	}
	
	public void startInteraction(Intention intent) {
		
	}
	
	/*
	 * ********** ACTIONS **********
	 */
	
	private void processRequest(RetrievalRequest request) {
		// For each item in the request, go "retrieve" it, through animation
		for (ItemRequest item : request.items) {
			doGetItem(item);
		}
	}
	
	/*
	 * ********** ANIMATION **********
	 */
	private void doGetItem(ItemRequest item) {
		/*
		 * animation code to retrieve the item
		 */
	}

	/*
	 * ********** UTILITY **********
	 */
	
	private RetrievalRequest findNextPendingRetrievalRequest() {
		// Go through our requests list and find the first one that is PENDING
		synchronized(requests) {
			for (RetrievalRequest request : requests) {
				if (request.state == RetrievalState.PENDING)
					return request;
			}
		}
		
		// If there aren't any PENDING request, return null
		return null;
	}

}
