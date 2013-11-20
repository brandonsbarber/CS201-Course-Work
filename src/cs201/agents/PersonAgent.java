package cs201.agents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

import cs201.helper.CityDirectory;
import cs201.helper.CityTime;
import cs201.roles.Role;
import cs201.structures.Structure;
import cs201.structures.restaurant.Restaurant;

/**
 * The PersonAgent that represents all people in SimCity201
 * @author Matt Pohlmann
 *
 */
public class PersonAgent extends Agent {
	/**************************************************************************
	 *                              Constants                                 *
	 **************************************************************************/
	private final int INITIALHUNGER = 0;
	private final int HUNGERPERMINUTE = 1;
	private final int INITIALMONEY = 30;
	private final int INITIALWAKEUPHOUR = 7;
	private final int INITIALWAKEUPMINUTE = 0;
	
	
	/**************************************************************************
	 *                                 Data                                   *
	 **************************************************************************/
	private String name;
	private Semaphore animation;
	private List<Role> roles;
	//private PassengerRole passengerRole;
	private List<Action> planner;
	private CityTime time;
	private CityTime wakeupTime;
	private double moneyOnHand;
	private int hungerLevel;
	//private Vehicle vehicle;
	private Structure home;
	private Structure workplace;
	private Intention job;
	private Structure currentLocation;
	private int bankAccountNumber;
	
	
	/**************************************************************************
	 *                            Constructor/Setup                           *
	 **************************************************************************/
	public PersonAgent(String name) {
		super();
		
		this.name = name;
		this.animation = new Semaphore(0);
		this.roles = Collections.synchronizedList(new ArrayList<Role>());
		//this.passengerRole = new PassengerRole();
		//this.passengerRole.setPerson(this);
		this.planner = Collections.synchronizedList(new ArrayList<Action>());
		this.time = new CityTime();
		this.wakeupTime = new CityTime(INITIALWAKEUPHOUR, INITIALWAKEUPMINUTE);
		this.moneyOnHand = INITIALMONEY;
		this.hungerLevel = INITIALHUNGER;
		//this.vehicle = null;
		this.setHome(null);
		this.workplace = null;
		this.job = Intention.None;
		this.currentLocation = null;
		this.bankAccountNumber = -1;
	}
	
	/**
	 * Sets up a PersonAgent with the correct information given.
	 * MUST BE CALLED BEFORE STARTING A PERSON'S THREAD OR STRANGE BEHAVIOR WILL OCCUR
	 * @param curTime The current time in SimCity201
	 * @param home This PersonAgent's home
	 * @param workplace This PersonAgent's workplace (can be null if the PersonAgent doesn't work)
	 * @param job This PersonAgent's job (can be none if this PersonAgent doesn't work)
	 * @param location This PersonAgent's starting location (probably his home)
	 */
	public void setupPerson(CityTime curTime, Structure home, Structure workplace, Intention job, Structure location/*, Vehicle vehicle*/) {
		this.time.day = curTime.day;
		this.time.hour = curTime.hour;
		this.time.minute = curTime.minute;
		
		this.setHome(home);		
		this.workplace = workplace;
		this.job = job;
		this.currentLocation = location;
		//this.vehicle = vehicle;
	}
	
	
	/**************************************************************************
	 *                                Messages                                *
	 **************************************************************************/
	/**
	 * Updates the time so this Person can make appropriate time-based decisions
	 * @param newTime The new time in SimCity201
	 */
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
		/*if (passengerRole.getActive()) {
			passengerRole.pickAndExecuteAnAction();
			return true;
		}*/
		
		// If you have active roles, those have next highest priority (because you're currently
		// doing something)
		boolean actionPerformed = false;
		synchronized(roles) {
			for (Role r : roles) {
				if (r.getActive()) {
					actionPerformed = r.pickAndExecuteAnAction() || actionPerformed;
				}
				if (actionPerformed) {
					return true;
				}
			}
		}
		
		// If you have an active Action (meaning you decided to perform this action and go to its
		// location then actually perform that Action)
		if (planner.get(0).active) {
			performAction(planner.get(0));
			return true;
		}
		
		// If you have an action in your planner, go to its location and make it active)
		if (planner.size() > 0) {
			goToLocation(planner.get(0));
			return true;
		}
		
		// If it's time to wake up in the morning
		if (time.equalsIgnoreDay(this.wakeupTime)) {
			
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
			//passengerRole.msgGoTo(a.location);
			//passengerRole.setActive(true);
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
		
		planner.remove(a);
	}
	
	private void goToWork() {
		Action temp = new Action();
		temp.location = workplace;
		temp.intent = job;
		planner.add(temp);
		Do("Added going to work (" + workplace + ") to Planner");
	}
	
	private void sleepAtHome() {
		Action temp = new Action();
		temp.location = getHome();
		temp.intent = Intention.ResidenceSleep;
		planner.add(temp);
		Do("Added going home (" + getHome() + ") to sleep to Planner");
	}
	
	private void eatAtHome() {
		Action temp = new Action();
		temp.location = getHome();
		temp.intent = Intention.ResidenceEat;
		planner.add(temp);
		Do("Added eating at home (" + getHome() + ") to Planner");
	}
	
	/*
	private void withdrawMoneyAsCustomer() {
		Action temp = new Action();
		// Pick a random bank to perform the transaction at
		Random randGenerator = new Random();
		int num = randGenerator.nextInt(CityDirectory.getInstance().getBanks().size());
		temp.location = CityDirectory.getInstance().getBanks().get(num);
		temp.intent = Intention.BankWithdrawMoneyCustomer;
		planner.add(temp);
		Do("Added withdrawing money as customer at " + temp.location + " to Planner");
	}
	
	private void depositMoneyAsCustomer() {
		Action temp = new Action();
		// Pick a random bank to perform the transaction at
		Random randGenerator = new Random();
		int num = randGenerator.nextInt(CityDirectory.getInstance().getBanks().size());
		temp.location = CityDirectory.getInstance().getBanks().get(num);
		temp.intent = Intention.BankDepositMoneyCustomer;
		planner.add(temp);
		Do("Added depositing money as customer at " + temp.location + " to Planner");
	}
	
	private void withdrawMoneyAsBusiness() {
		Action temp = new Action();
		// Pick a random bank to perform the transaction at
		Random randGenerator = new Random();
		int num = randGenerator.nextInt(CityDirectory.getInstance().getBanks().size());
		temp.location = CityDirectory.getInstance().getBanks().get(num);
		temp.intent = Intention.BankWithdrawMoneyBusiness;
		planner.add(temp);
		Do("Added withdrawing money as business at " + temp.location + " to Planner");
	}
	
	private void depositMoneyAsBusiness() {
		Action temp = new Action();
		// Pick a random bank to perform the transaction at
		Random randGenerator = new Random();
		int num = randGenerator.nextInt(CityDirectory.getInstance().getBanks().size());
		temp.location = CityDirectory.getInstance().getBanks().get(num);
		temp.intent = Intention.BankDepositMoneyBusiness;
		planner.add(temp);
		Do("Added depositing money as business at " + temp.location + " to Planner");
	}
	
	private void goToMarket() {
		Action temp = new Action();
		// Pick a random market
		Random randGenerator = new Random();
		int num = randGenerator.nextInt(CityDirectory.getInstance().getMarkets().size());
		temp.location = CityDirectory.getInstance().getMarkets().get(num);
		temp.intent = Intention.MarketConsumer;
		planner.add(temp);
		Do("Added a market run at " + temp.location + " to Planner");
	}*/
	
	private void eatAtRestaurant() {
		Action temp = new Action();
		// Pick a random restaurant to eat at
		Random randGenerator = new Random();
		int num = randGenerator.nextInt(CityDirectory.getInstance().getRestaurants().size());
		temp.location = CityDirectory.getInstance().getRestaurants().get(num);
		temp.intent = Intention.RestaurantCustomer;
		planner.add(temp);
		Do("Added eating at " + temp.location + " to Planner");
	}
	
	/**************************************************************************
	 *                                Utility                                 *
	 **************************************************************************/	
	/**
	 * Any work-related Role must be removed from a person who is leaving work so it can be assigned to someone else
	 * @param toRemove The Role being removed from this PersonAgent
	 */
	public void removeRole(Role toRemove) {
		roles.remove(toRemove);
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
	 * Adds money to this PersonAgent's money on hand
	 * @param amount How much money to add
	 */
	public void addMoney(double amount) {
		this.moneyOnHand += amount;
	}
	
	/**
	 * Removes money from this PersonAgent's money on hand
	 * @param amount How much money to remove
	 */
	public void removeMoney(double amount) {
		this.moneyOnHand -= amount;
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
	 * Received from a PassengerRole to update this Person's location in SimCity201
	 * @param newLocation
	 */
	public void doneMoving(Structure newLocation) {
		currentLocation = newLocation;
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
	protected void Do(String msg) {
		StringBuffer output = new StringBuffer();
		output.append(this.getClass().getSimpleName());
		output.append(" ");
		output.append(this.name);
		output.append(": ");
		output.append(msg);
		
		System.out.println(output.toString());
	}
	
	public Structure getHome()
	{
		return home;
	}

	public void setHome(Structure home)
	{
		this.home = home;
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
		BankWithdrawMoneyBusiness,
		BankDepositMoneyBusiness,
		MarketManager,
		MarketEmployee,
		MarketConsumer,
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
		
		public Action(Structure location, Intention intent) {
			this.location = location;
			this.intent = intent;
			this.active = false;
		}
	}
}
