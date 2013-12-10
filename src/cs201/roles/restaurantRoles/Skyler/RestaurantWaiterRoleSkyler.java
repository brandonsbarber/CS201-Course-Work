package cs201.roles.restaurantRoles.Skyler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.roles.restaurant.Skyler.WaiterGuiSkyler;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelBen;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelSkyler;
import cs201.interfaces.roles.restaurant.Skyler.CashierSkyler;
import cs201.interfaces.roles.restaurant.Skyler.CookSkyler;
import cs201.interfaces.roles.restaurant.Skyler.CustomerSkyler;
import cs201.interfaces.roles.restaurant.Skyler.HostSkyler;
import cs201.interfaces.roles.restaurant.Skyler.WaiterSkyler;
import cs201.roles.restaurantRoles.RestaurantWaiterRole;

public class RestaurantWaiterRoleSkyler extends RestaurantWaiterRole implements
		WaiterSkyler {
	
	static final int NTABLES = 3;//a global for the number of tables.

	public List<myCustomer> myCustomers = Collections.synchronizedList(new ArrayList<myCustomer>());

	
	private Map<String, Double> menu = new HashMap<String, Double>();
	private String name;
	private Semaphore goingSomewhere = new Semaphore(0,true);
	private Semaphore waitingForResponse = new Semaphore(0,true);
	private HostSkyler host = null;
	private CookSkyler cook = null;
	private CashierSkyler cashier = null;
	private boolean closingTime = false;
	private int currentTable = -1; //non-existent table. at table -1 when at front. at table -2 when at cook. at table -3 when moving in between.
	
	public enum BreakState {noRequest, wantBreak, requested, approved, denied, onBreak, backFromBreak}
	public BreakState breakRequest = BreakState.noRequest;
	
	public enum AgentState
	{Free, Busy};
	public AgentState state = AgentState.Free;
	
	public enum CustomerState
	{unseated, seated, waitingToOrder, waitingForFood, eating, done, doneNoCheck, hasCheck, gone};
	
	public enum OrderState {justOrdered, givenToCook, rejected, ready, beingDelivered, delivered}; //Should state options be fewer for waiter? different options for cook perhaps?
	
	public WaiterGuiSkyler waiterGui = null;
	private RestaurantAnimationPanelSkyler animPanel = null;

	public RestaurantWaiterRoleSkyler() {
		this("");
	}
	
	public RestaurantWaiterRoleSkyler(String name) {
		super();
		
		this.name = name;
		
		menu.put("Steak", 16.0);
		menu.put("Chicken", 11.0);
		menu.put("Salad", 6.0);
		menu.put("Pizza", 9.0);
	}
	
	public String getName() {
		return name;
	}
	
	public void msgWantBreak() {//from animation
		Do("I want a break");
		breakRequest = BreakState.wantBreak;
		stateChanged();
	}
	
	public void msgEndBreak() {
		Do("Coming back from my break");
		breakRequest = BreakState.backFromBreak;
		stateChanged();
	}

	public void msgDoneEating(CustomerSkyler cust) {
		Do("Received msgDoneEating from customer "+cust.getName());
		synchronized(myCustomers) {
			for (myCustomer myC : myCustomers) {
				if (myC.customer == cust) {
					if (myC.amtOwed==-1)
						myC.state = CustomerState.done;
					else
						myC.state = CustomerState.doneNoCheck;
				}
			}
		}
		stateChanged();
	}
	
	public void msgLeavingTable(CustomerSkyler cust) {
		for (int i=0; i<myCustomers.size(); i++) {
			if (myCustomers.get(i).customer == cust) {
				Do(cust + " leaving table " + myCustomers.get(i).tableNumber);
				host.msgTableFree(myCustomers.get(i).tableNumber);
				myCustomers.remove(i);
				stateChanged();
			}
		}
	}

	public void msgAtTable(int atTableNum) {//from animation
		Do("msgAtTable() called");
		currentTable = atTableNum;
		goingSomewhere.release();
	}
	
	public void msgAtFront() {
		Do("Back at the front of the restaurant.");
		currentTable = -1;
		goingSomewhere.release();
		state = AgentState.Free;
	}
	
	public void msgAtCook() {
		Do("At Cook");
		currentTable = -2;
		goingSomewhere.release();
		state = AgentState.Busy;
	}
	
	public void msgAtCashier() {
		Do("At cashier");
		currentTable = -3;
		goingSomewhere.release();
		state = AgentState.Busy;
	}
	
	public void msgAtWaitingArea() {
		Do("At waiting area");
		currentTable = -3;
		goingSomewhere.release();
		state = AgentState.Busy;
	}
	
	public void msgSeatAtTable(CustomerSkyler customer, int tableNumber) {
		myCustomers.add(new myCustomer(customer, tableNumber));
		//print("added "+customer.getName());
		customer.setWaiter(this);
		stateChanged();
	}
	
	public void msgGoToWork() {
		host.msgWaiterReady(this);
		Do("I'm ready to work.");
		stateChanged();
	}
	
	public void msgReadyToOrder(CustomerSkyler cust) {
		synchronized(myCustomers) {
			for(myCustomer myCust : myCustomers) {
		
			if (myCust.customer==cust){
				myCust.state = CustomerState.waitingToOrder;
				//DoReturnForOrder(myCust.tableNumber); //gui action.
				Do("I'll be right there, "+myCust.customer.getName());
				if (state == AgentState.Free && waiterGui.inMotion()) { goingSomewhere.release();}
				stateChanged();
			}
		 }
		}
	}
	
	public void msgHereIsMyChoice(CustomerSkyler cust, String choice) {
		synchronized(myCustomers) {
			for(myCustomer myCust : myCustomers) {
				if (myCust.customer==cust){
					myCust.setOrder(choice);
					myCust.order.state = OrderState.justOrdered;
					myCust.state = CustomerState.waitingForFood;
					//function will give cook necessary info after gui action.
				}
			}
		}
		Do("waitingforresponse released. hereismychoice.");
		waitingForResponse.release();
	}
	public void msgOutOf(String choice, int tableNum) {
		synchronized(myCustomers) {
			for(myCustomer myCust : myCustomers) {
				if (myCust.tableNumber == tableNum) {
					myCust.order.state = OrderState.rejected;
					if (state == AgentState.Free && waiterGui.inMotion()) {
						goingSomewhere.release(); 
					}
					stateChanged();
				}
			}
		}
	}
	
	public void msgOrderReady(String choice, int tableNum) {
		synchronized(myCustomers) {
		for(myCustomer myCust : myCustomers) {
			if (myCust.tableNumber == tableNum) {
				myCust.order.state = OrderState.ready;
				if (state == AgentState.Free && waiterGui.inMotion()) {
					goingSomewhere.release(); 
				}
				stateChanged();
			}
		}
		}
	}
	
	public void msgHereIsFood(String choice, int tableNum) {
		synchronized(myCustomers) {
		for(myCustomer myCust : myCustomers) {
			if (myCust.tableNumber == tableNum) {
				myCust.order.state = OrderState.beingDelivered;
			}
		}
		}
		Do("waitingforresponse released. hereisfood.");
		waitingForResponse.release();
	}
	
	@Override
	public void msgCheckReady(CustomerSkyler c, double amt) {
		synchronized(myCustomers) {
		for (myCustomer myC : myCustomers) {
			if (myC.customer==c) 
				myC.amtOwed=amt;
		}
		}
		Do("waitingforresponse released. CheckReady.");
		waitingForResponse.release();
	}
	
	public void msgBreakApproved() {
		breakRequest = BreakState.approved;
		stateChanged();
	}
	
	public void msgBreakDenied() {
		breakRequest = BreakState.denied;
		stateChanged();
	}

	@Override
	public void startInteraction(Intention intent) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean pickAndExecuteAnAction() {
		if(closingTime) {
			leaveRestaurant();
			return true;
		}
		try{
			switch(breakRequest) {
			case wantBreak: requestBreak();
				break;
			case denied: waiterGui.breakDenied();
					Do("Fine, I won't go on break.");
					breakRequest = BreakState.noRequest;
				break;
			case backFromBreak: returnFromBreak();
				break;
			default:
				break;
			}
			
			
			synchronized(myCustomers) {
			for (myCustomer cust : myCustomers) {
				if (cust.state == CustomerState.done) {
					cust.state = CustomerState.gone;
					host.msgTableFree(cust.tableNumber);
					return true;
				}
				else if (cust.state==CustomerState.unseated) {
					state = AgentState.Busy;
					//if (currentTable!=-1) {
						
						//DoGoBackToFront();
						DoGoToWaitingArea();
						try {
							currentTable=-3;
							goingSomewhere.acquire();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					//}
					DoSeatCustomer(cust.customer, cust.tableNumber);
					cust.customer.msgSitAtTable(menu);
					cust.state = CustomerState.seated;
				
					try {
						currentTable=-3;
						goingSomewhere.acquire();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return true;
				}
			}
			}//end synchronized
			
			for (myCustomer cust : myCustomers) {
				if (cust.order!=null && cust.state == CustomerState.waitingForFood && cust.order.state == OrderState.rejected) {
					state = AgentState.Busy;
					if (currentTable!=cust.tableNumber){
						DoGoToTable(cust.tableNumber);
						try {
							currentTable=-3;
							goingSomewhere.acquire();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Do(cust.customer+", sorry, but we are out of "+cust.order.orderChoice+". Please reorder.");
						//menu.remove(cust.order.orderChoice);
						//List<String> adjustedMenu = menu;
						Map<String, Double> adjustedMenu = menu;
						adjustedMenu.remove(cust.order.orderChoice);
						cust.customer.msgReOrder(adjustedMenu);
						cust.state=CustomerState.seated;
						return true;
						}
				}
			}
			for (myCustomer cust : myCustomers) {
				if (cust.order!=null && cust.order.state==OrderState.beingDelivered) {
					state = AgentState.Busy;
					DoDeliverFood(cust);
					try {
						goingSomewhere.acquire();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Do("Customer "+cust.customer.getName()+", here is your food");
					cust.order.state = OrderState.delivered;
					cust.state = CustomerState.eating;
					cust.customer.msgHereIsYourFood(cust.order.orderChoice);
					return true;
				  }
				}
			
			for (myCustomer cust : myCustomers) {
				if (cust.order!=null && cust.order.state==OrderState.ready) {
					state = AgentState.Busy;
					DoGoGetFood(cust.tableNumber);
					try {
						goingSomewhere.acquire();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Do("Ok chef, I'm back for the order for table "+cust.tableNumber);
					cook.msgImBackFor(cust.tableNumber);
					try {
						waitingForResponse.acquire();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return true;
				}
			}
				
			
			for (myCustomer cust : myCustomers) {
				if (cust.state==CustomerState.waitingToOrder) {
					state = AgentState.Busy;
					if(currentTable!=cust.tableNumber){
						DoGoToTable(cust.tableNumber); //gui action.
						try {
							currentTable=-3;
							goingSomewhere.acquire();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					Do("What would you like, "+cust.customer.getName()+"?");
					cust.customer.msgWaiterBack();
					try {
						
						waitingForResponse.acquire();
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					DoBringOrderToCook(cust.order.orderChoice);
					try {
						currentTable=-3;
						goingSomewhere.acquire();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					TellCookChoice(cust);
					return true;
				}
			}
			for (myCustomer cust : myCustomers) {
				if(cust.state != CustomerState.hasCheck) {
					if(cust.amtOwed>1) {
						deliverCheckToCustomer(cust);
						return true;
					}
					if(cust.amtOwed==0 && (cust.state == CustomerState.eating || cust.state == CustomerState.doneNoCheck)) {
						getCheck(cust);
						return true;
						}
				
				}
				
			}
				
			if(currentTable!=-1){ //nothing to do. Waiter goes back to front to wait.
				DoGoBackToFront();
				state = AgentState.Free;
				
					try {
						currentTable=-3;
						goingSomewhere.acquire();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			state = AgentState.Free;
			
			if(breakRequest == BreakState.approved) {
				for (myCustomer c : myCustomers) {
					if (c.state != CustomerState.gone ) 
						return false;
				}
				goOnBreak();
			}
			
			return false;
			//we have tried all our rules and found
			//nothing to do. So return false to main loop of abstract agent
			//and wait.
		 }catch(ConcurrentModificationException e) {
			return true;
		 }
	}

	@Override
	public void msgClosingTime() {
		closingTime = true;
		stateChanged();
	}
	
	private void leaveRestaurant() {
		this.isActive = false;
		this.myPerson.goOffWork();
		this.myPerson.removeRole(this);
		this.myPerson = null;
		this.myCustomers.clear();
		this.waiterGui.setPresent(false);
	}
	
	
	private void returnFromBreak() {
		host.msgBackFromBreak(this);
		breakRequest = BreakState.noRequest;
	}
	
	private void deliverCheckToCustomer(myCustomer c) {
		waiterGui.DoGoToTable(c.tableNumber);
		state = AgentState.Busy;
		try {
			currentTable=-3;
			goingSomewhere.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Do("Giving "+c.customer.getName()+" check.");
		c.customer.msgHereIsYourCheck(cashier, c.amtOwed);
		c.amtOwed=0; //resets customer balance to 0 in myCustomers list. check is out of the hands of the waiter, now responsibility of customer & cashier.
		c.state = CustomerState.hasCheck;
	}
	
	private void getCheck(myCustomer c) {
		Do("Getting check for "+c.customer.getName());
		waiterGui.DoGoToCashier();
		state = AgentState.Busy;
		try {
			currentTable=-3;
			goingSomewhere.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Do(cashier.getName()+", can you make a bill for "+c.customer.getName()+"?");
		cashier.msgRequestCheck(c.customer, this, menu.get(c.order.orderChoice));
		try {
			waitingForResponse.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void requestBreak() {
		host.msgIWantABreak(this);
		breakRequest = BreakState.requested;
	}
	
	private void goOnBreak() {
		breakRequest = BreakState.onBreak;
		Do("Going on break now!");
		//host.msgImGoingOnBreak(this);
		//DoBreakAnimation();
	}
	
	// The animation DoXYZ() routines
	private void DoSeatCustomer(CustomerSkyler customer, int tableNumber) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		
		Do("Seating " + customer + " at table " + tableNumber+". Follow me!");
		waiterGui.DoBringToTable(customer, tableNumber);
	}
	
	private void DoGoBackToFront() {
		Do("Going back to front");
		waiterGui.DoLeaveCustomer();
	}
	
	private void DoGoToWaitingArea() {
		Do("Going to get a customer from the waiting area");
		waiterGui.DoGoToWaitingArea();
	}
	
	private void DoGoToTable(int tableNum) {
		Do("Going to table "+tableNum);
		waiterGui.DoGoToTable(tableNum);
	}
	
	private void DoBringOrderToCook(String choice) {
		Do("One "+choice+" coming right up. Telling Cook.");
		waiterGui.DoBringOrderToCook(choice);
	}
	
	private void TellCookChoice(myCustomer cust) {
		Do("One "+cust.order.orderChoice+" please, Chef.");
		cust.order.state = OrderState.givenToCook;
		cook.msgHereIsOrder(this, cust.order.orderChoice, cust.tableNumber);
	}
	
	private void DoGoGetFood(int tableNum) {
		Do("Getting food for table "+tableNum);
		waiterGui.DoGetFood();
	}
	
	private void DoDeliverFood(myCustomer cust) {
		Do("Bringing food to table "+cust.tableNumber);
		waiterGui.DoDeliverFood(cust.order.orderChoice, cust.tableNumber);
	}
	
	public void setGui(WaiterGuiSkyler gui) {
		waiterGui = gui;
	}

	public void setHost(HostSkyler h) {
		host = h;
	}
	public void setCashier(CashierSkyler c) {
		cashier = c;
	}
	public void setCook(CookSkyler c) {
		cook = c;
	}
	
	public void setAnimPanel(RestaurantAnimationPanelSkyler panel) {
		animPanel = panel;
	}
	
	public int getNumCustomers() {
		return myCustomers.size();
		
	}
	
	private class myCustomer {
		CustomerSkyler customer;
		int tableNumber;
		double amtOwed = 0;
		Order order;
		CustomerState state;
		
		myCustomer (CustomerSkyler c, int tableNum) {
			customer = c;
			tableNumber = tableNum;
			state = CustomerState.unseated;
			order = null;
		}
		
		void setOrder(String choice) {
			order = new Order(choice);
		}
		
		class Order {
			String orderChoice;
			OrderState state;
		
			Order (String choice) {
				orderChoice = choice;
				state = OrderState.justOrdered;
			}
		}
		
	}

	public WaiterGuiSkyler getGui() {
		return waiterGui;
	}

}
