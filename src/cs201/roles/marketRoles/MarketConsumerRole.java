package cs201.roles.marketRoles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import cs201.agents.PersonAgent;
import cs201.agents.PersonAgent.Intention;
import cs201.gui.roles.market.MarketConsumerGui;
import cs201.interfaces.agents.transit.Vehicle;
import cs201.interfaces.roles.market.MarketConsumer;
import cs201.interfaces.roles.market.MarketManager;
import cs201.roles.Role;
import cs201.roles.marketRoles.MarketManagerRole.ItemRequest;
import cs201.structures.market.MarketStructure;
import cs201.trace.AlertLog;
import cs201.trace.AlertTag;

public class MarketConsumerRole extends Role implements MarketConsumer {
	
	/*
	 * ********** DATA **********
	 */
	String name = "";
	List<MarketBill> marketBills = Collections.synchronizedList( new ArrayList<MarketBill>() );
	MarketStructure structure;
	boolean leaveMarket = false;
	
	enum MarketBillState {OUTSTANDING, PAID};
	class MarketBill {
		MarketManager manager;
		float amount;
		MarketBillState state;
		
		public MarketBill(MarketManager m, float a, MarketBillState s) {
			manager = m;
			amount = a;
			state = s;
		}
	}
	
	MarketConsumerGui gui = null;
	Semaphore animation = new Semaphore(0, true);
	
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
		
		// If there are any market bills to pay, pay them
		synchronized(marketBills) {
			for (MarketBill bill : marketBills) {
				if (bill.state == MarketBillState.OUTSTANDING) {
					payBill(bill);
					return true;
				}
			}
		}
		
		if (leaveMarket) {
			leaveMarket();
			return true;
		}
		
		return false;
	}

	
	/*
	 * ********** MESSAGES **********
	 */
	
	public void msgHereIsYourTotal(MarketManager manager, float amount) {
		AlertLog.getInstance().logMessage(AlertTag.MARKET, "Market consumer " + name, String.format("Just got my total of $.2f for my market purchase.", amount));
		
		// Add the bill to our list
		marketBills.add(new MarketBill(manager, amount, MarketBillState.OUTSTANDING));
		
		stateChanged();
	}
	
	public void msgHereAreYourItems(List<ItemRequest> items) {
		AlertLog.getInstance().logMessage(AlertTag.MARKET, "Market consumer " + name, "Just receieved my items from the market.");
		
		// We bought everything on our list
		myPerson.getMarketChecklist().clear();
		
		// Add the new items to our inventory
		for (ItemRequest item : items) {
			myPerson.getInventory().add(item);
		}
		
		stateChanged();
	}
	
	public void msgHereIsYourCar(Vehicle car) {
		AlertLog.getInstance().logMessage(AlertTag.MARKET, "Market consumer " + name, "Just got my new car from the market.");
		
		// We now have a car!
		// TODO Ask Brandon how to do this...
		this.getPerson().setVehicle(car);
		
		stateChanged();
	}
	
	public void startInteraction(Intention intent) {
		
		leaveMarket = false;
		
		// Walk to the marketManager
		if (gui != null) {
			gui.setPresent(true);
			gui.doWalkToManager();
			pauseForAnimation();
		}
		
		if (this.structure.isOpen()) {
			
			if (intent == Intention.MarketConsumerGoods) {
				
				// The consumer would like to buy some goods
				structure.getManager().msgHereIsMyOrder(this,  myPerson.getMarketChecklist());
				
			} else if (intent == Intention.MarketConsumerCar) {
				
				// The consumer would like to buy a car
				structure.getManager().msgIWouldLikeACar(this);
			}
			
			
		} else {
			leaveMarket = true;
		}
		
	}

	public void msgClosingTime() {
		// Nothing to do here
	}
	
	/*
	 * ********** ACTIONS **********
	 */
	
	private void payBill(MarketBill mb) {
		// Pay the bill in full
		mb.manager.msgHereIsMyPayment(this, mb.amount);
		
		// Deduct the amount from my funds
		PersonAgent person = this.getPerson();
		if (person != null) {
			this.getPerson().removeMoney(mb.amount);
		}
		
		// Once we've paid we can leave
		leaveMarket();
	}
	
	private void leaveMarket() {
		this.isActive = false;
		gui.doLeaveMarket();
		pauseForAnimation();
		if (gui != null) {
			gui.setPresent(false);
		}
	}
	
	/*
	 * ********** ANIMATION **********
	 */
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
	
	public void setGui(MarketConsumerGui g) {
		gui = g;
	}
	
	public void setStructure(MarketStructure s) {
		structure = s;
	}

}