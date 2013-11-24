package cs201.roles.marketRoles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Semaphore;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.roles.market.MarketEmployeeGui;
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
	
	enum RequestState {PENDING, PROCESSED};
	private class RetrievalRequest {
		List<ItemRequest> items;
		int id;
		RequestState state;
		MarketManager manager;
		
		public RetrievalRequest(MarketManager m, List<ItemRequest> i, int id, RequestState s) {
			items = i;
			id = id;
			state = s;
			manager = m;
		}
	}
	
	MarketEmployeeGui gui;
	Semaphore animation = new Semaphore(0, true);
	
	
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
		
		// If there's nothing else to do, go home
		if (gui != null)
			gui.doGoHome();
		
		return false;
	}

	
	/*
	 * ********** MESSAGES **********
	 */
	
	public void msgRetrieveItems(MarketManager manager, List<ItemRequest> items, int id) {
		System.out.println("Got message msgRetrieveItems");
		
		// Add the new retrieval request to the list of requests
		requests.add(new RetrievalRequest(manager, items, id, RequestState.PENDING));
		
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
		
		// Walk to the manager
		gui.doGoToManager();
		pauseForAnimation();
		
		// Give the items to the manager
		request.manager.msgHereAreItems(this, request.items, request.id);
		
		// The request has now been processed
		request.state = RequestState.PROCESSED;
	}
	
	/*
	 * ********** ANIMATION **********
	 */
	private void doGetItem(ItemRequest item) {
		if (gui != null) {
			/* 
			 * Just to a random shelf location
			 * This works, for now. Remember it's a simulation, not an emulation ;)
			 * In v2 I might store the locations in a map
			 */
			Random generator = new Random();
			gui.doGoToItemOnShelf(generator.nextInt(5), generator.nextInt(5));
			pauseForAnimation();
		}
	}
	
	public void animationFinished() {
		animation.release();
	}
	
	private void pauseForAnimation() {
		try {
			animation.acquire();
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * ********** UTILITY **********
	 */
	
	private RetrievalRequest findNextPendingRetrievalRequest() {
		// Go through our requests list and find the first one that is PENDING
		synchronized(requests) {
			for (RetrievalRequest request : requests) {
				if (request.state == RequestState.PENDING)
					return request;
			}
		}
		
		// If there aren't any PENDING requests, return null
		return null;
	}
	
	public void setGui(MarketEmployeeGui g) {
		gui = g;
	}

}
