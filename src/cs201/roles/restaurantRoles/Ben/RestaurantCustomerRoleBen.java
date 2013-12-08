package cs201.roles.restaurantRoles.Ben;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.roles.restaurant.Ben.CustomerGuiBen;
import cs201.interfaces.roles.restaurant.Ben.CashierBen;
import cs201.interfaces.roles.restaurant.Ben.CustomerBen;
import cs201.interfaces.roles.restaurant.Ben.HostBen;
import cs201.interfaces.roles.restaurant.Ben.WaiterBen;
import cs201.roles.restaurantRoles.RestaurantCustomerRole;
import cs201.roles.restaurantRoles.Ben.RestaurantWaiterRoleBen.Menu;
import cs201.trace.AlertLog;
import cs201.trace.AlertTag;
/**
 * Restaurant customer agent.
 */
public class RestaurantCustomerRoleBen extends RestaurantCustomerRole implements CustomerBen {
	private String name;
	private int hungerLevel = 15;        // determines length of meal
	Timer timer = new Timer();
	private CustomerGuiBen customerGui;
	private float funds = 0.0f;
	
	WaiterBen myWaiter = null;
	Menu myMenu = null;
	String myChoice = null;
	float check = 0.0f;
	Boolean gotCheck = false;
	Boolean firstTimeHungry = true;

	// agent correspondents
	private HostBen host;
	private CashierBen cashier;
	
	private int destinationTable = 0;

	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, ArrivedAtRestaurant, waitingInRestaurant, BeingSeated, ReadingMenu, AboutToOrder, Ordered, Eating, DoneEating, WaitingForCheck, Leaving};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, theresAWait, followHost, seated, readyToOrder, waiterAsksForOrder, foodArrived, checkArrived, doneEating, doneLeaving, outOfFood};
	AgentEvent event = AgentEvent.none;

	public RestaurantCustomerRoleBen() {
		this("");
	}
	
	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public RestaurantCustomerRoleBen(String name){
		super();
		this.name = name;
		
		// Give the customer an allowance
		funds += 15.00f;
		
		// HACK - if the name of the customer is flake or poor, he'll start out with only 3 dollars;
		if (name.toLowerCase().equals("flake") ||
			name.toLowerCase().equals("poor") )
			funds = 3.00f;
		
		// HACK - if the name of the customer is "collegekid" he'll start out with only 8 bucks, just enough for some chicken
		if (name.toLowerCase().equals("collegekid"))
			funds = 8.00f;
		
		// HACK - if the name of the customer is "rich" he'll start out with 100 dollars
		if (name.toLowerCase().equals("rich"))
			funds = 100.00f;
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setHost(HostBen host) {
		this.host = host;
	}
	
	public void setCashier(CashierBen cashier) {
		this.cashier = cashier;
	}

	public String getCustomerName() {
		return name;
	}
	
	/**
	 * Messages
	 */

	public void gotHungry() {//from animation
		// Reset variables
		check = 0.0f;
		gotCheck = false;
		
		// HACK - after this customer has eaten once, give him more money if he's a flake
		if (firstTimeHungry) {
			firstTimeHungry = false;
		} else {
			if (name.toLowerCase().equals("flake")) funds += 100.0f;
		}

		event = AgentEvent.gotHungry;
		stateChanged();
	}
	
	// Sent from host to let the customer know he'll have to wait
	public void msgItWillBeAwhile() {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, "Customer " + name, "I was just told it will be awhile from the host.");
		
		event = AgentEvent.theresAWait;
		
		stateChanged();
	}

	public void msgFollowMeToTable(int tablenumber, WaiterBen waiter, Menu menu) {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, "Cook " + name, "I was just told to follow the host to a table.");
		
		// Grab the menu and record who our waiter is
		myMenu = menu;
		myWaiter = waiter;
		
		// Set our destination to the correct table number
		destinationTable = tablenumber;
		
		// Follow the host
		event = AgentEvent.followHost;
		
		stateChanged();
	}
	
	public void msgWhatWouldYouLike() {
		event = AgentEvent.waiterAsksForOrder;
		stateChanged();
	}
	
	public void msgHereIsYourFood() {
		event = AgentEvent.foodArrived;
		stateChanged();
	}
	
	public void msgOutOf(Menu newMenu) {
		// The waiter gives us a new menu
		myMenu = newMenu;
		
		// Let's think it over first, then order food again
		event = AgentEvent.outOfFood;
		customerGui.removeIcon();
		
		stateChanged();
	}
	
	public void msgHereIsCheck(float amount) {
		check = amount;
		
		event = AgentEvent.checkArrived;
		gotCheck = true;
		
		stateChanged();
	}

	public void msgAnimationFinishedGoToSeat() {
		//from animation
		event = AgentEvent.seated;
		stateChanged();
	}
	
	public void msgAnimationFinishedLeaveRestaurant() {
		// From animation
		event = AgentEvent.doneLeaving;
		stateChanged();
		
		// Reset our state machine
		state = AgentState.DoingNothing;
		event = AgentEvent.none;
		
		// Now, deactivate our role
		deactivateRole();
	}
	
	public void msgClosingTime() {
		// Nothing to do here
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 * CustomerAgent is a finite state machine.
	 */
	public boolean pickAndExecuteAnAction() {
		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ) {
			state = AgentState.ArrivedAtRestaurant;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.ArrivedAtRestaurant && event == AgentEvent.theresAWait) {
			decideIfIShouldLeave();
			return true;
		}
		if ((state == AgentState.waitingInRestaurant || state == AgentState.ArrivedAtRestaurant)
			&& event == AgentEvent.followHost ) {
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.seated) {
			state = AgentState.ReadingMenu;
			ReadOverMenu();
			return true;
		}
		
		if (state == AgentState.ReadingMenu && event == AgentEvent.readyToOrder) {
			state = AgentState.AboutToOrder;
			TellWaiterImReady();
			return true;
		}
		
		if (state == AgentState.AboutToOrder && event == AgentEvent.waiterAsksForOrder) {
			state = AgentState.Ordered;
			GiveWaiterOrder();
			return true;
		}
		
		if (state == AgentState.Ordered && event == AgentEvent.foodArrived) {
			state = AgentState.Eating;
			EatFood();
			return true;
		}
		
		if (state == AgentState.Eating && event == AgentEvent.doneEating) {
			if (gotCheck) {
				state = AgentState.Leaving;
				PayForMeal();
				leaveRestaurant();
				return true;
			} else {
				state = AgentState.WaitingForCheck;
			}
		}
		if (state == AgentState.WaitingForCheck && event == AgentEvent.checkArrived) {
			state = AgentState.Leaving;
			PayForMeal();
			leaveRestaurant();
			return true;
		}
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving) {
			state = AgentState.DoingNothing;
			return true;
		}
		if (state == AgentState.Ordered && event == AgentEvent.outOfFood) {
			state = AgentState.ReadingMenu;
			ReadOverMenu();
			return true;
		}
		return false;
	}

	/**
	 * Actions
	 */
	
	private void goToRestaurant() {
		host.msgIWantFood(this);//send our instance, so he can respond to us
	}
	
	private void decideIfIShouldLeave() {
		// HACK - if the customer is named "inahurry" he won't want to wait
		// HACK - if the customer is named "hastime" he will wait
		Boolean waiting = false;
		if(name.toLowerCase().equals("hastime"))
			waiting = true;
		else if(name.toLowerCase().equals("inahurry"))
			waiting = false;
		else {
			Random random = new Random();
			waiting = random.nextInt(2) == 0 ? true : false; 
		}
		
		if (!waiting) {
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, "Customer " + name, "Sorry, I don't have time to wait. Leaving...");

			host.msgICantWait(this);
			state = AgentState.DoingNothing;
			customerGui.DoExitRestaurant();
		}
		else {
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, "Customer " + name, "I don't mind waiting for a table.");
			
			state = AgentState.waitingInRestaurant;
		}
	}

	private void SitDown() {
		customerGui.DoGoToSeat(destinationTable);//hack; only one table
	}
	
	private void ReadOverMenu() {
		// Choose a random item on the menu
		myChoice = myMenu.getRandom();
		
		// Decide if we can afford it, and if not, choose something we can afford
		// HACK - unless we're named 'flake'
		Boolean leaving = false;
		if (myMenu.getPrice(myChoice) > funds && !name.toLowerCase().equals("flake")) {
			myChoice = myMenu.itemUnderPrice(funds);
			if (myChoice == null) {
				// I can't afford anything, so I should leave
				leaving = true;
			}
		}
		
		// Leave if necessary
		if (leaving) {
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, "Customer " + name, "I can't afford anything, so I'm leaving.");
			
			state = AgentState.Leaving;
			leaveRestaurant();
			return;
		}
		
		// HACK - if the customer's name is Steak or Chicken, choose that
		if (name.toLowerCase().equals("chicken") && myMenu.itemOnMenu("Chicken"))
			myChoice = "Chicken";
		else if (name.toLowerCase().equals("steak") && myMenu.itemOnMenu("Steak"))
			myChoice = "Steak";
		
		// HACK - THIS LINE CAUSES HIM TO ALWAYS ORDER CHICKEN, TAKE IT OUT AS SOON AS YOU'RE DONE TESTING
		//myChoice = "Chicken";
				
		// Create a timer to simulate the customer reading over the menu
		timer.schedule(new TimerTask() {
			public void run() {
				event = AgentEvent.readyToOrder;
				AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, "Customer " + name, "I'll have the " + myChoice);
				stateChanged();
			}
		}, 2000);
	}
	
	private void TellWaiterImReady() {
		// Let the waiter know!
		myWaiter.msgReadyToOrder(this);
	}
	
	private void GiveWaiterOrder() {
		// Let the waiter know what we'll be having
		myWaiter.msgHereIsChoice(this, myChoice);
		
		// Set our Icon to the food we want + a '?'
		customerGui.setIconText(myChoice == "Chicken" ? "C?" : "ST?");
	}

	private void EatFood() {
		// Remove '?' from the customer's icon
		customerGui.setIconText(myChoice == "Chicken" ? "C" : "ST");
		
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, "Customer " + name, "Eating Food");
		
		/**
		 * This next complicated line creates and starts a timer thread.
		 * We schedule a dealine of getHungerLevel() * 1000 milliseconds.
		 * When that time elapses, it will call back to the run routine
		 * located in the anonymous class created right there inline:
		 * TimerTask is an interface that we implement right there inline.
		 * Since Java does not allow us to pass functions, only objects.
		 * So, we use Java syntactic mechanism to create an anynymous
		 * inner class that has the public method run() in it.
		 */

		timer.schedule(new TimerTask() {
			Object cookie = 1;
			public void run() {
				event = AgentEvent.doneEating;
				//isHungry = false;
				stateChanged();
			}
		},
		getHungerLevel() * 1000);//how long to wait before running task
	}
	
	private void PayForMeal() {
		// Message the cashier with the payment
		if (funds >= check) {
			// Remove the money from our wallet
			funds -= check;
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, "Customer " + name, String.format("Here's my payment of $%.2f. I have $%.2f left.", check, funds));
			
			// Give it to the cashier
			cashier.msgHereIsPayment(this, check);
		}
		else {
			// Let the cashier know we can't pay
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, "Customer " + name, "Oops, looks like I don't have enough money to pay.");
			cashier.msgICantPay(this);
		}
	}

	private void leaveRestaurant() {
		// Remove the customer's icon
		customerGui.removeIcon();
		
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, "Customer " + name, "Leaving.");
		//host.msgLeavingTable(this);
		if (myWaiter != null)
			myWaiter.msgLeavingTable(this);
		customerGui.DoExitRestaurant();
	}
	
	private void deactivateRole() {
		this.isActive = false;
		this.customerGui.setPresent(false);
	}

	/**
	 * Utilities
	 */

	public String getName() {
		return name;
	}
	
	public int getHungerLevel() {
		return hungerLevel;
	}

	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
	}

	public String toString() {
		return "customer " + getName();
	}

	public void startInteraction(Intention intent) {
		if (intent == Intention.RestaurantCustomer) {
			// TODO animate customer walking into restaurant
			this.customerGui.setPresent(true);
			this.gotHungry();
		}
	}

	public void setGui(CustomerGuiBen g) {
		customerGui = g;
	}

	public CustomerGuiBen getGui() {
		return customerGui;
	}
}

