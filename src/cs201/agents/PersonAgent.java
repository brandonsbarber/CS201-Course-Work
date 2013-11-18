package cs201.agents;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import cs201.helper.CityTime;
import cs201.roles.Role;
import cs201.structures.Structure;

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
	
	
	/**************************************************************************
	 *                                 Data                                   *
	 **************************************************************************/
	private String name;
	private Semaphore animation;
	private List<Role> roles;
	//private PassengerRole passengerRole;
	private List<Action> planner;
	private CityTime time;
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
		this.roles = new ArrayList<Role>();
		//this.passengerRole = new PassengerRole();
		this.planner = new ArrayList<Action>();
		this.time = new CityTime();
		this.moneyOnHand = INITIALMONEY;
		this.hungerLevel = INITIALHUNGER;
		//this.vehicle = null;
		this.home = null;
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
		
		this.home = home;		
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
	
	/**
	 * Received from a PassengerRole to update this Person's location in SimCity201
	 * @param newLocation
	 */
	public void msgDoneMoving(Structure newLocation) {
		currentLocation = newLocation;
	}
	
	
	/**************************************************************************
	 *                               Scheduler                                *
	 **************************************************************************/
	@Override
	protected boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		
		/*if (passengerRole.getActive()) {
			passengerRole.pickAndExecuteAnAction();
			return true;
		}*/
		
		boolean actionPerformed = false;
		for (Role r : roles) {
			if (r.getActive()) {
				actionPerformed = r.pickAndExecuteAnAction() || actionPerformed;
			}
			if (actionPerformed) {
				return true;
			}
		}
		
		if (planner.size() > 0) {
			performAction(planner.get(0));
			return true;
		}
		
		
		return false;
	}
	
	
	/**************************************************************************
	 *                                Actions                                 *
	 **************************************************************************/
	/**
	 * Performs a given Action, including moving to the location of that action
	 * NOTE: I DON'T KNOW IF THIS WORKS YET
	 * 
	 * @param a The Action to be performed
	 */
	private void performAction(Action a) {
		if (currentLocation != a.location) {
			//passengerRole.msgGoTo(a.location);
			//passengerRole.setActive(true);
		}
		
		Role newRole = a.location.getRole(a.intent);
		if (newRole == null) {
			//passengerRole.active = false;
			return;
		}
		
		for (Role r : roles) {
			if (r.getClass().isInstance(newRole.getClass())) {
				r.startInteraction(a.intent);
				r.setActive(true);
				return;
			}
		}
		
		roles.add(newRole);
		newRole.setPerson(this);
		newRole.startInteraction(a.intent);
		newRole.setActive(true);
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
		output.append(this.getClass().getSimpleName());
		output.append(" ");
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
		
		public Action(Structure location, Intention intent) {
			this.location = location;
			this.intent = intent;
		}
	}
}
