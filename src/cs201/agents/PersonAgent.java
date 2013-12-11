package cs201.agents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

import cs201.agents.transit.CarAgent;
import cs201.gui.CityPanel;
import cs201.gui.transit.PassengerGui;
import cs201.helper.CityDirectory;
import cs201.helper.CityTime;
import cs201.interfaces.agents.Person;
import cs201.interfaces.agents.transit.Vehicle;
import cs201.roles.Role;
import cs201.roles.marketRoles.MarketManagerRole.ItemRequest;
import cs201.roles.transit.PassengerRole;
import cs201.roles.transit.PassengerRole.PassengerState;
import cs201.structures.Structure;
import cs201.structures.residence.Residence;
import cs201.trace.AlertLog;
import cs201.trace.AlertTag;

/**
 * The PersonAgent that represents all people in SimCity201
 * @author Matt Pohlmann
 *
 */
public class PersonAgent extends Agent implements Person {
	/**************************************************************************
	 *                              Constants                                 *
	 **************************************************************************/
	private static final int HUNGERPERMINUTE = 2;
	public static final int FULL = 0;
	public static final int HUNGRY = 480;
	public static final int STARVING = 840;
	private static final int INITIALMONEY = 200;
	private static final int MONEYTHRESHOLD = 10;
	private static final int INITIALWAKEUPHOUR = 7;
	private static final int INITIALWAKEUPMINUTE = 0;
	private static final int INITIALSLEEPHOUR = 22;
	private static final int INITIALSLEEPMINUTE = 0;
	private static final int INITIALWORKHOUR = 8;
	private static final int INITIALWORKMINUTE = 0;
	
	
	/**************************************************************************
	 *                                 Data                                   *
	 **************************************************************************/
	private volatile String name;
	private CityPanel panel;
	private volatile PersonState state;
	private volatile Semaphore animation;
	private volatile List<Role> roles;
	private volatile PassengerRole passengerRole;
	private volatile List<Action> planner;
	private volatile Action currentAction;
	private volatile CityTime time;
	private volatile CityTime wakeupTime;
	private volatile CityTime sleepTime;
	private volatile double moneyOnHand;
	private volatile int hungerLevel;
	private volatile boolean hungerEnabled;
	private volatile Vehicle vehicle;
	private volatile Residence home;
	private volatile Structure workplace;
	private volatile Intention job;
	private volatile CityTime workTime;
	private volatile Structure currentLocation;
	private volatile int bankAccountNumber;
	private volatile List<ItemRequest> marketChecklist;
	private volatile List<ItemRequest> inventory;
	
	
	/**************************************************************************
	 *                           Constructors/Setup                           *
	 **************************************************************************/
	public PersonAgent(String name, CityPanel p) {
		super();
		
		this.name = name;
		this.panel = p;
		this.state = PersonState.Sleeping;
		this.animation = new Semaphore(0);
		this.roles = Collections.synchronizedList(new ArrayList<Role>());
		this.passengerRole = new PassengerRole(null);
		if (p != null) {
			PassengerGui pGui = new PassengerGui(passengerRole, panel,0,0);
			this.passengerRole.setGui(pGui);
			this.panel.addGui(pGui);
		}
		this.passengerRole.setPerson(this);
		this.planner = Collections.synchronizedList(new LinkedList<Action>());
		this.currentAction = null;
		this.time = new CityTime();
		this.wakeupTime = new CityTime(INITIALWAKEUPHOUR, INITIALWAKEUPMINUTE);
		this.sleepTime = new CityTime(INITIALSLEEPHOUR, INITIALSLEEPMINUTE);
		this.moneyOnHand = INITIALMONEY;
		this.hungerEnabled = true;
		this.hungerLevel = HUNGRY;
		this.vehicle = null;
		this.home = null;
		this.workplace = null;
		this.job = Intention.None;
		this.workTime = new CityTime(INITIALWORKHOUR, INITIALWORKMINUTE);
		this.currentLocation = null;
		this.bankAccountNumber = -1;
		marketChecklist = new LinkedList<ItemRequest>();
		inventory = new LinkedList<ItemRequest>();
	}
	
	@Override
	public void setupPerson(CityTime curTime, Residence home, Structure workplace, Intention job, Structure location, Vehicle vehicle) {
		this.time.day = curTime.day;
		this.time.hour = curTime.hour;
		this.time.minute = curTime.minute;
		
		this.home = home;
		if (this.home != null) {
			this.home.setOccupied(true);
		}
		this.workplace = workplace;
		this.job = job;
		this.currentLocation = location;
		passengerRole.setCurrentLocation(this.currentLocation);
		this.vehicle = vehicle;
		if (vehicle != null) {
			passengerRole.addCar((CarAgent) vehicle);
		}
		
		if (CityTime.timeDifference(curTime, wakeupTime) > 0) {
			this.state = PersonState.Awake;
		}
		if (CityTime.timeDifference(curTime,  sleepTime) > 0 || CityTime.timeDifference(curTime, wakeupTime) < 0) {
			this.state = PersonState.Sleeping;
		}
	}
	
	
	/**************************************************************************
	 *                                Messages                                *
	 **************************************************************************/
	@Override
	public void msgUpdateTime(CityTime newTime) {
		int minutesPassed = CityTime.timeDifference(newTime, this.time);
		if (hungerEnabled) {
			hungerLevel += (state == PersonState.Sleeping) ? HUNGERPERMINUTE / 2 * minutesPassed : HUNGERPERMINUTE * minutesPassed;
		}
		
		time.day = newTime.day;
		time.hour = newTime.hour;
		time.minute = newTime.minute;
		
		stateChanged();
	}
	
	
	/**************************************************************************
	 *                               Scheduler                                *
	 **************************************************************************/
	@Override
	protected boolean pickAndExecuteAnAction() {		
		// If you're going somewhere, that has highest priority
		if (passengerRole.getActive()) {
			passengerRole.pickAndExecuteAnAction();
			return true;
		}
		
		// If you have active roles, those have next highest priority (because you're currently
		// doing something)
		if (state != PersonState.Relaxing) {
			synchronized(roles) {
				for (Role r : roles) {
					if (r.getActive()) {
						return r.pickAndExecuteAnAction();
					}
				}
			}
		}
		
		// If you have an active Action (meaning you decided to perform this action and go to its
		// location, then actually perform that Action)
		synchronized(planner) {
			for (Action a : planner) {
				if (a.active) {
					performAction(a);
					return true;
				}
			}
		}
		
		// If you have an action in your planner, go to its location and make it active)
		synchronized(planner) {
			for (Action a : planner) {
				goToLocation(a);
				return true;
			}
		}
		
		// If you're here in the scheduler, you've completed any current actions, so make current action null
		this.currentAction = null;
		
		// If it's time to wake up in the morning (during the week)
		if (state == PersonState.Sleeping && !time.isWeekend() && time.equalsIgnoreDay(wakeupTime)) {
			this.state = PersonState.Awake;
			
			// If you need to pay rent
			if (home != null && ((Residence) home).isApartment()) {
				this.addActionToPlanner(Intention.ResidencePayRent, home, true);
			}
			// The Residence Role will determine if there's enough time to eat at a Restaurant, or if eating at home is better
			this.addActionToPlanner(Intention.ResidenceEat, home, false);
			return true;
		}
		
		// If it's time to wake up in the morning (on the weekend)
		if (state == PersonState.Sleeping && time.isWeekend() && time.equalsIgnoreDay(wakeupTime.add(90))) {
			this.state = PersonState.Awake;

			// Eat at home
			this.addActionToPlanner(Intention.ResidenceEat, home, false);
			return true;
		}
		
		// If it's time to go to work
		if ((state == PersonState.Awake || state == PersonState.Relaxing) && CityTime.timeDifference(time, workTime) >= 0 && CityTime.timeDifference(time, workTime) <= 90 && !time.isWeekend()) {
			if (this.workplace != null && !this.workplace.isForceClosed()) {
				if (this.addActionToPlanner(job, workplace, true)) {
					this.state = PersonState.AtWork;
					return true;
				}
			}
		}
		
		// If it's time to go to sleep
		if ((state == PersonState.Awake || state == PersonState.Relaxing) && (CityTime.timeDifference(time, sleepTime) >= 0 || CityTime.timeDifference(time, wakeupTime) < 0)) {
			this.planner.clear();
			this.state = PersonState.Sleeping;
			this.addActionToPlanner(Intention.ResidenceSleep, home, false);
			return true;
		}
		
		// If you need to get money from the bank
		if ((state == PersonState.Awake || state == PersonState.Relaxing) && moneyOnHand <= MONEYTHRESHOLD) {
			boolean performAction = checkForExistingAction(Intention.BankWithdrawMoneyCustomer);
			
			if (performAction && CityDirectory.getInstance().getOpenBanks().size() > 0) {
				this.addActionToPlanner(Intention.BankWithdrawMoneyCustomer, CityDirectory.getInstance().getRandomOpenBank(), false);
				this.state = PersonState.Awake;
				return true;
			}
		}
		
		// If you're hungry, but not at home
		if (state == PersonState.Awake && (currentLocation != home || currentLocation == null) && hungerLevel >= HUNGRY) {
			boolean performAction = checkForExistingAction(Intention.RestaurantCustomer);
			
			if (performAction && CityDirectory.getInstance().getOpenRestaurants().size() > 0) {
				boolean starving = hungerLevel >= STARVING;
				this.addActionToPlanner(Intention.RestaurantCustomer, CityDirectory.getInstance().getRandomOpenRestaurant(), starving);
				return true;
			}
		}
		
		// If you're hungry and at home (or there are no open restaurants)
		if ((state == PersonState.Awake || state == PersonState.Relaxing) && home != null && (currentLocation == home || CityDirectory.getInstance().getOpenRestaurants().size() == 0) && hungerLevel >= HUNGRY) {
			boolean performAction = checkForExistingAction(Intention.ResidenceEat);
			
			if (performAction) {
				boolean starving = hungerLevel >= STARVING;
				if (this.addActionToPlanner(Intention.ResidenceEat, home, starving)) {
					this.state = PersonState.Awake;
					return true;
				}
			}
		}
		
		// If you you need to buy something at the market
		if ((state == PersonState.Awake || state == PersonState.Relaxing) && marketChecklist.size() > 0) {
			boolean performAction = checkForExistingAction(Intention.MarketConsumerGoods);
			
			if (performAction && CityDirectory.getInstance().getOpenMarkets().size() > 0) {
				this.addActionToPlanner(Intention.MarketConsumerGoods, CityDirectory.getInstance().getRandomOpenMarket(), false);
				this.state = PersonState.Awake;
				return true;
			}
		}

		// If nothing to do, go home and relax
		if (state == PersonState.Awake) {
			if (this.addActionToPlanner(Intention.ResidenceRelax, home, false)) {
				state = PersonState.Relaxing;
				return true;
			}
		}
		
		// If you don't even have a home to return to
		if (state == PersonState.Awake && passengerRole.state != PassengerState.Roaming) {
			this.currentAction = null;
			passengerRole.setActive(true);
			passengerRole.msgStartRoaming();
			return true;
		}
		
		return false;
	}
	
	
	/**************************************************************************
	 *                                Actions                                 *
	 **************************************************************************/
	/**
	 * Uses a PassengerRole to go to a location in SimCity201 if the PersonAgent is not already there
	 * @param a The associated Action denoting where to go and what to do when getting there
	 */
	public void goToLocation(Action a) {
		a.active = true;
		// if roaming, passengerRole.stopRoaming()
		if (!passengerRole.isAtLocation(a.location)) {
			AlertLog.getInstance().logMessage(AlertTag.PERSON_AGENT, name, "Going to " + a.location);
			passengerRole.msgGoTo(a.location);
			currentLocation = null;
			passengerRole.setActive(true);
		}
	}
	
	/**
	 * Performs a given Action
	 * @param a The Action to be performed
	 */
	private void performAction(Action a) {		
		AlertLog.getInstance().logMessage(AlertTag.PERSON_AGENT, name, "Performing Action: " + a);
		
		Role newRole = a.location.getRole(a.intent);
		if (newRole == null) {
			planner.remove(a);
			AlertLog.getInstance().logWarning(AlertTag.PERSON_AGENT, name, "Failed to perform Action: " + a);
			return;
		}
		
		boolean haveRole = false;
		for (Role r : roles) {
			if (r.getClass().isInstance(newRole)) {
				r.setPerson(this);
				r.startInteraction(a.intent);
				r.setActive(true);
				haveRole = true;
				break;
			}
		}
		
		if (!haveRole) {
			roles.add(newRole);
			newRole.setPerson(this);
			newRole.startInteraction(a.intent);
			newRole.setActive(true);
		}
		
		currentAction = a;
		planner.remove(a);
	}
	
	/**
	 * General-use Action for adding something to this PersonAgent's daily planner
	 * @param intent What the PersonAgent will do
	 * @param location Where the PersonAgent should go
	 * @param highPriority Whether this is high enough priority to be put at the front of the planner
	 * @return True if Action added successfully, false if unable to add to planner
	 */
	private boolean addActionToPlanner(Intention intent, Structure location, boolean highPriority) {
		if (intent == null || intent == Intention.None || location == null) {
			return false;
		}
		
		Action temp = new Action();
		temp.location = location;
		temp.intent = intent;
		if (!highPriority) {
			planner.add(temp);
		} else {
			planner.add(0, temp);
		}
		return true;
	}
	
	
	/**************************************************************************
	 *                              Role Related                              *
	 **************************************************************************/
	@Override
	public void removeRole(Role toRemove) {
		roles.remove(toRemove);
	}
	
	@Override
	public void goOffWork() {
		this.state = PersonState.Awake;
	}
	
	@Override
	public void justAte() {
		this.hungerLevel = FULL;
	}
	
	@Override
	public void addIntermediateActions(Role from, LinkedList<Intention> intents, boolean returnToCurrentAction) {		
		// Deactivate sending Role
		if (from != null) {
			from.setActive(false);
		}
		int numActivities = intents.size();
		
		while (intents.size() > 0) {
			// Create a new action with high priority and put it at front of planner
			Intention intent = intents.removeLast();
			switch (intent) {
			case ResidenceSleep: this.addActionToPlanner(intent, home, true); break;
			case ResidenceEat: this.addActionToPlanner(intent, home, true); break;
			case BankWithdrawMoneyCustomer: this.addActionToPlanner(intent, CityDirectory.getInstance().getRandomBank(), true); break;
			case BankDepositMoneyCustomer: this.addActionToPlanner(intent, CityDirectory.getInstance().getRandomBank(), true); break;
			case BankTakeOutLoan: this.addActionToPlanner(intent, CityDirectory.getInstance().getRandomBank(), true); break;
			case BankWithdrawMoneyBusiness: this.addActionToPlanner(intent, CityDirectory.getInstance().getRandomBank(), true); break;
			case BankDepositMoneyBusiness: this.addActionToPlanner(intent, CityDirectory.getInstance().getRandomBank(), true); break;
			case MarketConsumerGoods: this.addActionToPlanner(intent, CityDirectory.getInstance().getRandomMarket(), true); break;
			case MarketConsumerCar: this.addActionToPlanner(intent, CityDirectory.getInstance().getRandomMarket(), true); break;
			case RestaurantCustomer: this.addActionToPlanner(intent, CityDirectory.getInstance().getRandomRestaurant(), true); break;
			default: {
				AlertLog.getInstance().logWarning(AlertTag.PERSON_AGENT, name, "addIntermediateAction(Role, LinkedList<Intention>, boolean):: Provided bad Intention");
				return;
			}
			}
		}
		
		// If Role requests that the PersonAgent return after, add that action back at the correct position
		if (returnToCurrentAction) {
			currentAction.active = false;
			planner.add(numActivities, currentAction);
		}
	}
	
	@Override
	public void addMoney(double amount) {
		this.moneyOnHand += amount;
	}
	
	@Override
	public void removeMoney(double amount) {
		this.moneyOnHand -= amount;
	}
	
	@Override
	public void doneMoving(Structure newLocation) {
		currentLocation = newLocation;
	}
	
	
	/**************************************************************************
	 *                             Getters/Setters                            *
	 **************************************************************************/
	/**
	 * Gets what this PersonAgent is currently doing
	 * @return An Action (contains an Intention intent and Structure location)
	 */
	public Action getCurrentAction() {
		return this.currentAction;
	}
	
	/**
	 * Sets how much money this person has. Could be used by GUI to force this person to go to the Bank
	 * @param newMoney The quantity of money this person has
	 */
	public void setMoney(double newMoney) {
		this.moneyOnHand = newMoney;
	}
	
	/**
	 * Returns how much money this person has on hand
	 * @return How much money this PersonAgent has right now (not including in the Bank)
	 */
	public double getMoney() {
		return this.moneyOnHand;
	}
	
	/**
	 * Sets this PersonAgent's hunger level to level. Possibly used by GUI to force a PersonAgent to go eat
	 * @param level The new hunger level. What constitutes 'hungry' TBD later
	 */
	public void setHungerLevel(int level) {
		this.hungerLevel = level;
	}
	
	/**
	 * Gets how hungry a person is, represented as an integer where 0 = not hungry at all
	 * @return How hungry this PersonAgent is
	 */
	public int getHungerLevel() {
		return this.hungerLevel;
	}
	
	/**
	 * Sets whether this person should get hungry over time
	 * @param hungerEnable boolean
	 */
	public void setHungerEnabled(boolean hungerEnable) {
		hungerEnabled = hungerEnable;
	}
	
	/**
	 * Sets this PersonAgent's workplace to workplace
	 * @param workplace Structure representing where this PersonAgent works
	 */
	public void setWorkplace(Structure workplace) {
		this.workplace = workplace;
	}
	
	/**
	 * Returns this PersonAgent's workplace. Could be null if they don't work
	 * @return A Structure representing where this PersonAgent works, or null if they don't work or it wasn't set properly
	 */
	public Structure getWorkplace() {
		return workplace;
	}
	
	/**
	 * Sets this PersonAgent's bank account number
	 * @param newNumber The new bank account number
	 */
	public void setBankAccountNumber(int newNumber) {
		this.bankAccountNumber = newNumber;
	}
	
	/**
	 * Gets this PersonAgent's bank account number
	 * @return A positive integer representing this PersonAgent's bank account number. -1 means it was never set up
	 */
	public int getBankAccountNumber() {
		return this.bankAccountNumber;
	}
	
	/**
	 * Sets this PersonAgent's Vehicle to a new Vehicle (basically only used when they buy a car)
	 * @param newVehicle The new Vehicle
	 */
	public void setVehicle(Vehicle newVehicle) {
		this.vehicle = newVehicle;
		if (vehicle != null) {
			passengerRole.addCar((CarAgent) vehicle);
		}
	}
	
	/**
	 * Gets this PersonAgent's vehicle if they have one
	 * @return The Vehicle, or null if this PersonAgent has no vehicle
	 */
	public Vehicle getVehicle() {
		return vehicle;
	}
	
	/**
	 * Returns what time this PersonAgent thinks it is
	 * @return CityTime representation of the current time
	 */
	public CityTime getTime() {
		return time;
	}
	
	/**
	 * Returns the time at which this PersonAgent wakes up every morning
	 * @return a CityTime object representing this person's morning wakeup time
	 */
	public CityTime getWakeupTime() {
		return wakeupTime;
	}
	
	/**
	 * Sets this PersonAgent's wakeup time
	 * @param time The new wakeup time for this PersonAgent
	 */
	public void setWakeupTime(CityTime time) {
		this.wakeupTime.hour = time.hour;
		this.wakeupTime.minute = time.minute;
	}
	
	/**
	 * Returns the time at which this PersonAgent sleeps every night
	 * @return a CityTime object representing this person's bedtime
	 */
	public CityTime getSleepTime() {
		return sleepTime;
	}
	
	/**
	 * Sets this PersonAgent's bedtime
	 * @param time The new bedtime for this PersonAgent
	 */
	public void setSleepTime(CityTime time) {
		this.sleepTime.hour = time.hour;
		this.sleepTime.minute = time.minute;
	}
	
	/**
	 * Returns the time at which this PersonAgent goes to work
	 * @return a CityTime object representing when this PersonAgent starts his/her work shift
	 */
	public CityTime getWorkTime() {
		return workTime;
	}
	
	/**
	 * Sets when this PersonAgent goes to work
	 * @param time The new work time for this PersonAgent
	 */
	public void setWorkTime(CityTime time) {
		this.workTime.hour = time.hour;
		this.workTime.minute = time.minute;
	}
	
	/**
	 * Returns where this PersonAgent currently is (null if the PersonAgent is traveling)
	 * @return a Structure representing where this PersonAgent is
	 */
	public Structure getCurrentLocation() {
		return this.currentLocation;
	}
	
	/**
	 * Returns this PersonAgent's home
	 * @return Structure (should be a Residence) where this PersonAgent lives
	 */
	public Structure getHome()
	{
		return this.home;
	}
	
	/**
	 * Sets a new home for this PersonAgent
	 * @param home The new Structure where this PersonAgent lives
	 */
	public void setHome(Residence home)
	{
		this.home = home;
	}
	
	/**
	 * Returns this PersonAgent's job in SimCity201
	 * @return An Intention representing this PersonAgent's job
	 */
	public Intention getJob() {
		return this.job;
	}
	
	/**
	 * Sets this PersonAgent's job in SimCity201. Note, this function does not check whether the
	 * job provided is actually a job, and not something like Intention.ResidenceSleep
	 * @param job The PersonAgent's new job
	 */
	public void setJob(Intention job) {
		this.job = job;
	}
	
	/**
	 * Returns what this PersonAgent needs to buy at the Market
	 * @return List<ItemRequest>
	 */
	public List<ItemRequest> getMarketChecklist() {
		return marketChecklist;
	}
	
	/**
	 * Returns what items this PersonAgent has on him right now
	 * @return List<ItemRequest>
	 */
	public List<ItemRequest> getInventory() {
		return inventory;
	}

	/**
	 * Gets this PersonAgent's passenger role
	 * @return PassengerRole
	 */
	public PassengerRole getPassengerRole()
	{
		return passengerRole;
	}
	
	/**
	 * Gets this PersonAgent's state
	 * @return PersonState
	 */
	public PersonState getState() {
		return this.state;
	}
	
	/**
	 * Sets this PersonAgent's current location. SHOULD ONLY BE CALLED WHEN THE THREAD IS NOT RUNNING.
	 * @param location The new location.
	 */
	public void setCurrentLocation(Structure location) {
		this.currentLocation = location;
		this.passengerRole.setCurrentLocation(location);
	}
	
	
	/**************************************************************************
	 *                                Utility                                 *
	 **************************************************************************/
	/**
	 * Checks if an Action with a given intent already exists in the planner
	 * @param intent Intention
	 * @return boolean
	 */
	private boolean checkForExistingAction(Intention intent) {
		synchronized(planner) {
			for (Action a : planner) {
				if (a.intent == intent) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Gets the name of this PersonAgent
	 * @return This PersonAgent's name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * An active Role should call this to wake the PersonAgent's scheduler back up after receiving a message
	 */
	public void stateChanged() {
		super.stateChanged();
	}
	
	/**
	 * Used to resume this PersonAgent when done animating
	 */
	public void animationRelease() {
		animation.release();
	}

	/**
	 * Used to pause this PersonAgent for animation
	 */
	public void animationAcquire() {
		try {
			animation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * General-purpose function for printing to the terminal. Format: "PersonAgent Matt: Going to work"
	 * @param msg The message that should be printed (i.e. Going to work)
	 */
	@Deprecated
	protected void Do(String msg) {
		StringBuffer output = new StringBuffer();
		output.append("[");
		output.append(this.getClass().getSimpleName());
		output.append("] ");
		output.append(this.name);
		output.append(": ");
		output.append(msg);
		
		System.out.println(output.toString());
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return this.name;
	}
	
	/**
	 * Represents anything a PersonAgent might want to do in SimCity201
	 * @author Matt Pohlmann
	 *
	 */
	public enum Intention {
		None,
		ResidenceSleep,
		ResidenceEat,
		ResidenceRelax,
		ResidenceLandLord,
		ResidencePayRent,
		BankTeller,
		BankGuard,
		BankWithdrawMoneyCustomer,
		BankDepositMoneyCustomer,
		BankTakeOutLoan,
		BankWithdrawMoneyBusiness,
		BankDepositMoneyBusiness,
		MarketManager,
		MarketEmployee,
		MarketConsumerGoods,
		MarketConsumerCar,
		RestaurantCook,
		RestaurantHost,
		RestaurantWaiter,
		RestaurantCashier,
		RestaurantCustomer
	}
	
	/**
	 * Represents one thing that a PersonAgent will do and its location
	 * @author Matt Pohlmann
	 *
	 */
	public class Action {
		public Structure location;
		public Intention intent;
		public boolean active;
		
		public Action() {
			this.location = null;
			this.intent = null;
			this.active = false;
		}
		
		public String toString() {
			return intent + " at " + location;
		}
	}
	
	/**
	 * Enum representing the state of a PersonAgent
	 * @author Matthew Pohlmann
	 *
	 */
	public enum PersonState {
		Sleeping,
		Awake,
		AtWork,
		Relaxing;
	}
}
