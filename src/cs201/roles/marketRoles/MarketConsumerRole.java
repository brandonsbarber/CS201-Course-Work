package cs201.roles.marketRoles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import cs201.agents.PersonAgent;
import cs201.agents.PersonAgent.Intention;
import cs201.gui.roles.market.MarketConsumerGui;
import cs201.gui.roles.market.MarketEmployeeGui;
import cs201.interfaces.roles.market.MarketConsumer;
import cs201.interfaces.roles.market.MarketEmployee;
import cs201.interfaces.roles.market.MarketManager;
import cs201.roles.Role;
import cs201.roles.marketRoles.MarketManagerRole.ItemRequest;

public class MarketConsumerRole extends Role implements MarketConsumer {
	
	/*
	 * ********** DATA **********
	 */
	String name = "";
	List<MarketBill> marketBills = Collections.synchronizedList( new ArrayList<MarketBill>() );
	
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
		
		return false;
	}

	
	/*
	 * ********** MESSAGES **********
	 */
	
	public void msgHereIsYourTotal(MarketManager manager, float amount) {
		// Add the bill to our list
		marketBills.add(new MarketBill(manager, amount, MarketBillState.OUTSTANDING));
		
		stateChanged();
	}
	
	public void msgHereAreYourItems(List<ItemRequest> items) {
		stateChanged();
	}
	
	public void startInteraction(Intention intent) {
		// Walk to the marketManager
		if (gui != null)
			gui.doWalkToManager();
		pauseForAnimation();
		
		// Go through my inventory and create an ItemRequest for everything that I'm low on
		// TODO
	}

	public void closingTime() {

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

}