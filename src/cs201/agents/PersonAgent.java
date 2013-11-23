package cs201.agents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

import cs201.helper.CityDirectory;
import cs201.helper.CityTime;
import cs201.interfaces.agents.Person;
import cs201.interfaces.agents.transit.Vehicle;
import cs201.roles.Role;
import cs201.roles.transit.PassengerRole;
import cs201.structures.Structure;

/**
 * The PersonAgent that represents all people in SimCity201
 * @author Matt Pohlmann
 *
 */
public class PersonAgent extends Agent implements Person {
	/**************************************************************************
	 *                              Constants                                 *
	 **************************************************************************/
	private final int INITIALHUNGER = 0;
	private final int HUNGERPERMINUTE = 1;
	private final int INITIALMONEY = 30;
	private final int INITIALWAKEUPHOUR = 7;
	private final int INITIALWAKEUPMINUTE = 0;
	private final int INITIALWORKHOUR = 8;
	private final int INITIALWORKMINUTE = 0;
	
	
	/**************************************************************************
	 *                                 Data                                   *
	 **************************************************************************/
	private String name;
	private PersonState state;
	private Semaphore animation;
	private List<Role> roles;
	private PassengerRole passengerRole;
	private List<Action> planner;
	private Action currentAction;
	private CityTime time;
	private CityTime wakeupTime;
	private double moneyOnHand;
	private int hungerLevel;
	private Vehicle vehicle;
	private Structure home;
	private Structure workplace;
	private Intention job;
	private CityTime workTime;
	private Structure currentLocation;
	private int bankAccountNumber;
	
	
	/**************************************************************************
	 *                           Constructors/Setup                           *
	 **************************************************************************/
	public PersonAgent(String name) {
		super();
		
		this.name = name;
		this.state = PersonState.Sleeping;
		this.animation = new Semaphore(0);
		this.roles = Collections.synchronizedList(new ArrayList<Role>());
		this.passengerRole = new PassengerRole(null);
		this.passengerRole.setPerson(this);
		this.planner = Collections.synchronizedList(new LinkedList<Action>());
		this.currentAction = null;
		this.time = new CityTime();
		this.wakeupTime = new CityTime(INITIALWAKEUPHOUR, INITIALWAKEUPMINUTE);
		this.moneyOnHand = INITIALMONEY;
		this.hungerLevel = INITIALHUNGER;
		this.vehicle = null;
		this.home = null;
		this.workplace = null;
		this.job = Intention.None;
		this.workTime = new CityTime(INITIALWORKHOUR, INITIALWORKMINUTE);
		this.currentLocation = null;
		this.bankAccountNumber = -1;
	}
	
	public PersonAgent() {
		Do("WARNING: A PersonAgent should never be created with the default constructor. Use PersonAgent(String) instead.");
	}
	
	@Override
	public void setupPerson(CityTime curTime, Structure home, Structure workplace, Intention job, Structure location, Vehicle vehicle) {
		this.time.day = curTime.day;
		this.time.hour = curTime.hour;
		this.time.minute = curTime.minute;
		
		this.home = home;		
		this.workplace = workplace;
		this.job = job;
		this.currentLocation = location;
		this.vehicle = vehicle;
		
		if (CityTime.timeDifference(curTime, wakeupTime) > 0) {
			this.state = PersonState.Awake;
		}
	}
	
	
	/**************************************************************************
	 *                                Messages                                *
	 **************************************************************************/
	@Override
	public void msgUpdateTime(CityTime newTime) {
		int minutesPassed = (newTime.hour - time.hour) * 60 + (newTime.minute - time.minute);
		hungerLevel += HUNGERPERMINUTE * minutesPassed;
		
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
		// TODO Auto-generated method stub
		
		// If you're going somewhere, that has highest priority
		if (passengerRole.getActive()) {
			passengerRole.pickAndExecuteAnAction();
			return true;
		}
		
		// If you have active roles, those have next highest priority (because you're currently
		// doing something)
		boolean actionPerformed = false;
		synchronized(roles) {
			for (Role r : roles) {
				if (r.getActive()) {
					actionPerformed = r.pickAndExecuteAnAction() || actionPerformed;
				}
			}
			if (actionPerformed) {
				return true;
			}
		}
		
		// If you have an active Action (meaning you decided to perform this action and go to its
		// location, then actually perform that Action)
		synchronized(planner) {
			for (Action a : planner) {
				if (a.active) {
					performAction(planner.get(0));
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
		
		// If it's time to wake up in the morning
		if (state == PersonState.Sleeping && time.equalsIgnoreDay(this.wakeupTime)) {
			this.state = PersonState.Awake;
			
			// If you have an hour or more, eat at a Restaurant
			if (CityTime.timeDifference(workTime, time) >= 60) {
				this.addActionToPlanner(Intention.RestaurantCustomer, CityDirectory.getInstance().getRandomRestaurant(), false);
			}
			// If you have less than an hour, eat at home
			else {
				this.addActionToPlanner(Intention.ResidenceEat, home, false);
			}
			return true;
		}
		
		// If it's time to go to work
		if (state == PersonState.Awake && time.equalsIgnoreDay(this.workTime)) {
			this.state = PersonState.AtWork;
			this.addActionToPlanner(job, workplace, true);
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
	private void goToLocation(Action a) {
		a.active = true;
		if (currentLocation != a.location) {
			passengerRole.msgGoTo(a.location);
			currentLocation = null;
			passengerRole.setActive(true);
			Do("Going to " + a.location);
		}
	}
	
	/**
	 * Performs a given Action
	 * NOTE: I DON'T KNOW IF THIS WORKS YET, but it should
	 * 
	 * @param a The Action to be performed
	 */
	private void performAction(Action a) {		
		Do("Performing Action: " + a.intent + " at " + a.location);
		
		Role newRole = a.location.getRole(a.intent);
		Do("Received new Role: " + newRole);
		
		if (newRole == null) {
			return;
		}
		
		for (Role r : roles) {
			if (r.getClass().isInstance(newRole)) {
				r.setPerson(this);
				r.startInteraction(a.intent);
				r.setActive(true);
				return;
			}
		}
		
		roles.add(newRole);
		newRole.setPerson(this);
		newRole.startInteraction(a.intent);
		newRole.setActive(true);
		
		currentAction = a;
		planner.remove(a);
	}
	
	/**
	 * General-use Action for adding something to this PersonAgent's daily planner
	 * @param intent What the PersonAgent will do
	 * @param location Where the PersonAgent should go
	 * @param highPriority Whether this is high enough priority to be put at the front of the planner
	 */
	private void addActionToPlanner(Intention intent, Structure location, boolean highPriority) {
		if (intent == null || location == null) {
			Do("Call to addActionToPlanner had null argument(s).");
			return;
		}
		
		Action temp = new Action();
		temp.location = location;
		temp.intent = intent;
		if (!highPriority) {
			planner.add(temp);
		} else {
			planner.add(0, temp);
		}
		Do("Added action " + temp.intent + " at " + temp.location + " to planner.");
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
	public void addIntermediateAction(Role from, Intention intent, boolean returnToCurrentAction) {		
		// Deactivate sending Role
		from.setActive(false);
		
		// Create a new action with high priority and put it at front of planner
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
			Do("addIntermediateAction(Intention, boolean):: Provided bad Intention");
			return;
		}
		}
		
		// If Role requests that the PersonAgent return after, add that action back at position 1
		if (returnToCurrentAction) {
			planner.add(1, currentAction);
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
	}
	
	/**
	 * Gets this PersonAgent's vehicle if they have one
	 * @return The Vehicle, or null if this PersonAgent has no vehicle
	 */
	public Vehicle getVehicle() {
		return vehicle;
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
	public void setHome(Structure home)
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
	
	
	/**************************************************************************
	 *                                Utility                                 *
	 **************************************************************************/
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
	 * Represents anything a PersonAgent might want to do in SimCity201
	 * @author Matt Pohlmann
	 *
	 */
	public enum Intention {
		None,
		ResidenceSleep,
		ResidenceEat,
		ResidenceLandLord,
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
	private class Action {
		Structure location;
		Intention intent;
		boolean active;
		
		public Action() {
			this.location = null;
			this.intent = null;
			this.active = false;
		}
	}
	
	private enum PersonState {
		Sleeping,
		Awake,
		AtWork
	}
}
