package cs201.roles.restaurantRoles.Skyler;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.Gui;
import cs201.gui.roles.restaurant.Skyler.CustomerGuiSkyler;
import cs201.interfaces.roles.restaurant.Skyler.CashierSkyler;
import cs201.interfaces.roles.restaurant.Skyler.CustomerSkyler;
import cs201.interfaces.roles.restaurant.Skyler.HostSkyler;
import cs201.interfaces.roles.restaurant.Skyler.WaiterSkyler;
import cs201.roles.restaurantRoles.RestaurantCustomerRole;

public class RestaurantCustomerRoleSkyler extends RestaurantCustomerRole
		implements CustomerSkyler {
	
	private String name;
	private String choice="";
	private int hungerLevel = 5;        // determines length of meal
	private double money = 0;
	private int randomInt;
	private double amtOwed=0;
	
	Timer timer = new Timer();
	private CustomerGuiSkyler customerGui;
	//private List <String> menu = null;
	private Map <String, Double> menu;

	// agent correspondents
	private CashierSkyler cashier = null;
	private HostSkyler host;
	private WaiterSkyler waiter;

	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, WaitingToOrder, Ordering, WaitingForFood, Eating, DoneEating, WaitingForCheck, Paying, Leaving};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, followHost, seated, decided, notifiedWaiter, waiterBack, madeOrder, gotOrder, doneEating, atCashier, doneLeaving};
	AgentEvent event = AgentEvent.none;

	public RestaurantCustomerRoleSkyler() {
		this("");
	}
	
	public RestaurantCustomerRoleSkyler(String name) {
		super();
		this.name = name;
		
		//money = myPerson.getMoney();
		//if(name.equals("Broke")||name.equals("Flake")) money=0;
	}

	public void setHost(HostSkyler host) {
		this.host = host;
	}
	public void setWaiter(WaiterSkyler waiter) {
		this.waiter = waiter;
	}
	
	public void gotHungry() {//from animation
		money = myPerson.getMoney();
		Do("I'm hungry and I have $"+String.format("%.2f", money)+" to spend.");
		event = AgentEvent.gotHungry;
		stateChanged();
	}

	//public void msgSitAtTable(List<String> newMenu) {
	public void msgSitAtTable(Map<String, Double> newMenu) {
		menu = newMenu;
		Do("Received msgSitAtTable");
		event = AgentEvent.followHost;
		stateChanged();
	}

	public void msgAnimationFinishedGoToSeat() {
		//from animation
		event = AgentEvent.seated;
		stateChanged();
	}
	
	public void msgWaiterBack() {
		//from waiter. Now customer can order.
		event = AgentEvent.waiterBack;
		stateChanged();
	}
	
	//public void msgReOrder(List<String> newMenu) {
	public void msgReOrder(Map<String, Double> newMenu) {
		menu = newMenu;
		state = AgentState.BeingSeated;
		event = AgentEvent.seated;
		stateChanged();
	}
	
	public void msgHereIsYourFood(String choice) { //Pass order along with message? or just in gui??
		event = AgentEvent.gotOrder;
		stateChanged();
	}
	
	public void msgHereIsYourCheck(CashierSkyler c, double amt) {
		amtOwed = amt;
		cashier = c;
		stateChanged();
	}
	
	public void msgAnimationFinishedPay() {
		event = AgentEvent.atCashier;
		stateChanged();
	}
	
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		event = AgentEvent.doneLeaving;
		leaveRestaurant();
	}
	
	@Override
	public void msgHereIsYourChange(double change) {
		money+=change;
	}


	@Override
	public void startInteraction(Intention intent) {
		// TODO Auto-generated method stub
		Do("Interaction started.");
		this.customerGui.setHungry();
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followHost ){
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.seated){
			state = AgentState.Seated;
			BrowseMenu();
			
			return true;
		}
		
		  if (state == AgentState.Seated && event == AgentEvent.decided){
		  state = AgentState.WaitingToOrder;
		  CallWaiter();
		  return true;
		  }
		  
		  if (state == AgentState.WaitingToOrder && event == AgentEvent.waiterBack){
		  state = AgentState.Ordering;
		  MakeOrder();
		  return true;
		  }
		  
		  if (state == AgentState.Ordering && event == AgentEvent.madeOrder){
		  state = AgentState.WaitingForFood;
		  WaitForFood();
		  return true;
		  }
		 
		  if (state == AgentState.WaitingForFood && event == AgentEvent.gotOrder){
		  state = AgentState.Eating;
		  EatFood();
		  return true;
		  }
		 
		if (state == AgentState.Eating && event == AgentEvent.doneEating){
			if (amtOwed == 0) {
				state = AgentState.WaitingForCheck;
				return false;
			}
			else {
				state = AgentState.Paying;
				leaveTable();
				
			}

			return true;
		}
		
		if (state == AgentState.WaitingForCheck) {
			if (amtOwed > 0) {
				state = AgentState.Paying;
				leaveTable();
				return true;
			}
		}
		
		if (state == AgentState.Paying && event == AgentEvent.atCashier) {
			payCashier();
			return true;
		}
		
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
			state = AgentState.DoingNothing;
			//no action
			return true;
		}
		return false;
	}

	
	private void payCashier() {
		state = AgentState.Leaving;
		cashier.msgHereIsCash(this, amtOwed);
		money-=amtOwed;
		myPerson.setMoney(money);
		amtOwed = 0;
		customerGui.DoExitRestaurant();
	}
	
	private void goToRestaurant() {
		Do("Going to restaurant");
		host.msgIWantFood(this);//send our instance, so he can respond to us
	}

	private void SitDown() {
		Do("Being seated. Going to table");
		customerGui.DoGoToSeat();
	}
	
	private void BrowseMenu() {
		Do("Browsing Menu");
		
		if(menu.isEmpty()) {
			leaveTable();
			return;
		}
		
		//Object[] prices = (Object[]) menu.values().toArray();
		Object[] prices = (Object[])menu.values().toArray();
		double cheapest=(Double)prices[0];
		for (int i=1; i<prices.length; i++) {
			if ((Double)prices[i]<cheapest)
				cheapest = (Double)prices[i];
		}
		
		if(money<cheapest && !name.equals("Flake")) {
			//  Leave restaurant
			leaveTable();
			return;
		}
			
			
			
		timer.schedule(new TimerTask() { //Timer wait to simulate time taken before deciding on the order.
			public void run() {
				choice = "";
				Object[] choices = menu.keySet().toArray();  //gets set of food names from map and makes puts them into an array so one can be randomly chosen.
				
				do {
					randomInt = (int)(Math.random()*(menu.size()));
					choice = (String) choices[randomInt];
					if(menu.get(choice)>money && !name.equals("Flake")) { //rechooses if choice is over amt in pocket. 
						Do("I can't afford the "+choice+". I'm going to choose something else.");
						choice = "";
					}

					if(name.equals("Steak")||name.equals("Chicken")||name.equals("Salad")||name.equals("Pizza")){ // HACK to dictate customer's food choice
						Do("!!!!!!! Hack triggered. Ordering same as my name.");
						choice = name;}
				} while(choice == "");
				
				event = AgentEvent.decided;
				stateChanged();
			}
		},
		1000);
	}
	
	private void CallWaiter() {
		Do("Ready to Order!");
		waiter.msgReadyToOrder(this);
		event = AgentEvent.notifiedWaiter; //doesn't immediately prompt an action.
	}
	
	private void MakeOrder() {
		Do("I'll have the "+choice+".");
		waiter.msgHereIsMyChoice(this, choice);
		event = AgentEvent.madeOrder;
		stateChanged();
	}
	
	private void WaitForFood() { //probably unnecessary...
		Do("Now I'm waiting for my food.");
	}

	private void EatFood() {
		Do("Eating Food");
		//This next complicated line creates and starts a timer thread.
		//We schedule a deadline of getHungerLevel()*1000 milliseconds.
		//When that time elapses, it will call back to the run routine
		//located in the anonymous class created right there inline:
		//TimerTask is an interface that we implement right there inline.
		//Since Java does not all us to pass functions, only objects.
		//So, we use Java syntactic mechanism to create an
		//anonymous inner class that has the public method run() in it.
		timer.schedule(new TimerTask() {
			Object cookie = 1;
			public void run() {
				Do("Done eating, cookie=" + cookie);
				event = AgentEvent.doneEating;
				myPerson.setHungerLevel(0);
				stateChanged();
			}
		},
		20000);//getHungerLevel() * 1000);//how long to wait before running task
	}

	private void leaveTable() {
		Do("Leaving.");
		waiter.msgLeavingTable(this);
		if(name.equals("Flake")||amtOwed==0){
			customerGui.DoExitRestaurant();
			state = AgentState.Leaving;
			return;
		}
		customerGui.DoGoPay();
	}
	
	@Override
	public void msgClosingTime() {
		// TODO Auto-generated method stub
		
		
		//HANDLE THIS?
	}
	
	private void leaveRestaurant() {
		this.setActive(false);
		customerGui.setPresent(false);
	}
	
	public String getChoice() {
		return choice;
	}
	
	public boolean isWaitingToBeSeated() {
		if (state==AgentState.WaitingInRestaurant)
			return true;
		else return false;
	}
	
	public AgentState getState() {
		return state;
	}
	
	public CustomerGuiSkyler getGui() {
		return customerGui;
	}

	public void setCashier(RestaurantCashierRoleSkyler newCashier) {
		cashier = newCashier;
	}

	public void setGui(CustomerGuiSkyler newGui) {
		customerGui = newGui;
	}

}
