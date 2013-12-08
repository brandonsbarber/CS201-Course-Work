package cs201.roles.marketRoles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.roles.market.MarketEmployeeGui;
import cs201.interfaces.roles.market.MarketEmployee;
import cs201.interfaces.roles.market.MarketManager;
import cs201.roles.Role;
import cs201.roles.marketRoles.MarketManagerRole.ItemRequest;
import cs201.trace.AlertLog;
import cs201.trace.AlertTag;

public class MarketEmployeeRole extends Role implements MarketEmployee {
	
	/*
	 * ********** DATA **********
	 */
	String name = "";
	List<RetrievalRequest> requests = Collections.synchronizedList(new ArrayList<RetrievalRequest>());
	MarketEmployeeGui gui;
	Semaphore animation = new Semaphore(0, true);
	boolean timeToGoHome = false;
	
	enum RequestState {PENDING, PROCESSED};
	enum RequestType  {ITEMS, CAR};
	private class RetrievalRequest {
		List<ItemRequest>	items = null;
		int 				id = -1;
		RequestState 		state = RequestState.PENDING;
		MarketManager 		manager = null;
		RequestType 		type = RequestType.ITEMS;
		
		/**
		 * Constructs a RetrievalRequest for items (as opposed to a car, see below)
		 */
		public RetrievalRequest(MarketManager m, List<ItemRequest> i, int id, RequestState s) {
			items = i;
			this.id = id;
			state = s;
			manager = m;
			type = RequestType.ITEMS;
		}
		
		/**
		 * Constructs a RetrievalRequest for a car
		 */
		public RetrievalRequest(MarketManager m, int id, RequestState s) {
			manager = m;
			this.id = id;
			type = RequestType.CAR;
			state = s;
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
		// Time to go home
		if (timeToGoHome) {
			leaveMarket();
			return true;
		}
		
		// If there's a PENDING retrieval request, process it
		RetrievalRequest pendingRequest = null;
		pendingRequest = findNextPendingRetrievalRequest();
		if (pendingRequest != null) {
			processRequest(pendingRequest);
			return true;
		}
		
		// If there's nothing else to do, go home
		if (gui != null) {
			gui.doGoHome();
			pauseForAnimation();
		}
		
		return false;
	}

	
	/*
	 * ********** MESSAGES **********
	 */
	
	public void msgRetrieveItems(MarketManager manager, List<ItemRequest> items, int id) {
		AlertLog.getInstance().logMessage(AlertTag.MARKET, "Market employee " + name, "Was just told to retrieve items for a consumer.");
		
		// Add the new retrieval request to the list of requests
		requests.add(new RetrievalRequest(manager, items, id, RequestState.PENDING));
		
		stateChanged();
	}
	
	public void msgRetrieveCar(MarketManager manager, int id) {
		AlertLog.getInstance().logMessage(AlertTag.MARKET, "Market employee " + name, "Was just told to retrieve a car for a consumer.");
		
		// Add the new retrieval request to the list of requests
		// Because we don't specify any items, the RetrievalRequest constructor makes it a CAR request
		requests.add(new RetrievalRequest(manager, id, RequestState.PENDING));
		
		stateChanged();
	}

	public void msgClosingTime() {
		
		timeToGoHome = true;
		
		stateChanged();
	}
	
	/**
	 * The person has entered the building.
	 */
	public void startInteraction(Intention intent) {
		// Set their gui to present
		gui.setPresent(true);
		timeToGoHome = false;
	}
	
	/*
	 * ********** ACTIONS **********
	 */
	
	private void leaveMarket() {
		this.isActive = false;
		this.myPerson.goOffWork();
		this.myPerson.removeRole(this);
		this.myPerson = null;
		if (gui != null) {
			gui.doLeaveMarket();
			pauseForAnimation();
			gui.setPresent(false);
		}
	}
	
	private void processRequest(RetrievalRequest request) {
		if (request.type == RequestType.ITEMS) {
		
			// For each item in the request, go "retrieve" it, through animation
			for (ItemRequest item : request.items) {
				doGetItem(item);
			}
			
			// Walk to the manager
			if (gui != null) {
				gui.doGoToManager();
				pauseForAnimation();
			}
			
			// Give the items to the manager
			request.manager.msgHereAreItems(this, request.items, request.id);
			
			// The request has now been processed
			request.state = RequestState.PROCESSED;
			
		} else if (request.type == RequestType.CAR) {
			
			// Go "retrieve" a car, through animation
			doGetCar();
			
			// Walk to the manager
			if (gui != null) {
				gui.doGoToManager();
				pauseForAnimation();
			}
			
			// Let the manager know we got the car
			request.manager.msgHereIsCar(this, request.id);
			
			// The request has now been processed
			request.state = RequestState.PROCESSED;
		}
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
	
	private void doGetCar() {
		gui.doWalkToCarLot();
		pauseForAnimation();
		gui.setHasCar(true);
		gui.setMovingCarIn(true);
		gui.doBringCarOut();
		pauseForAnimation();
		gui.setMovingCarIn(false);
		gui.setMovingCarOut(true);
	}
	
	public void animationFinished() {
		animation.release();
	}
	
	private void pauseForAnimation () {
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
	
	public MarketEmployeeGui getGui() {
		return gui;
	}
	
	/**
	 * Returns true if this role has a backing PersonAgent, false if it's currently set to null.
	 */
	public boolean hasAPerson() {
		return this.getPerson() != null;	
	}

}
