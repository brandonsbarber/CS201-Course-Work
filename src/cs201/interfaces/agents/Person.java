package cs201.interfaces.agents;


import java.util.LinkedList;

import cs201.agents.PersonAgent.Intention;
import cs201.helper.CityTime;
import cs201.interfaces.agents.transit.Vehicle;
import cs201.roles.Role;
import cs201.structures.Structure;

public interface Person {
	/**************************************************************************
	 *                                 Setup                                  *
	 **************************************************************************/
	/**
	 * Sets up a PersonAgent with the correct information given.
	 * MUST BE CALLED BEFORE STARTING A PERSON'S THREAD OR STRANGE BEHAVIOR WILL OCCUR
	 * @param curTime The current time in SimCity201
	 * @param home This PersonAgent's home
	 * @param workplace This PersonAgent's workplace (can be null if the PersonAgent doesn't work)
	 * @param job This PersonAgent's job (can be none if this PersonAgent doesn't work)
	 * @param location This PersonAgent's starting location (probably his home)
	 * @param vehicle This PersonAgent's vehicle (can be null if he/she doesn't have a vehicle initially)
	 */
	public abstract void setupPerson(CityTime curTime, Structure home, Structure workplace, Intention job, Structure location, Vehicle vehicle);
	
	/**************************************************************************
	 *                                Messages                                *
	 **************************************************************************/
	/**
	 * Updates the time so this Person can make appropriate time-based decisions
	 * @param newTime The new time in SimCity201
	 */
	public abstract void msgUpdateTime(CityTime newTime);
	
	/**************************************************************************
	 *                              Role Related                              *
	 **************************************************************************/
	/**
	 * Any work-related Role must be removed from a person who is leaving work so it can be assigned to someone else
	 * @param toRemove The Role being removed from this PersonAgent
	 */
	public abstract void removeRole(Role toRemove);
	
	/**
	 * Tells the PersonAgent that he is no longer working. Should be called by a work-related Role right before it
	 * deactivates itself
	 */
	public abstract void goOffWork();
	
	/**
	 * Any Role where a PersonAgent eats (RestaurantCustomerRole/ResidenceRole) should call this so the Person is no longer hungry
	 */
	public abstract void justAte();
	
	/**
	 * Roles can tell the PersonAgent that he/she should immediately go do another action.
	 * For example: A ResidentRole might need to go to the bank before paying rent, so it would
	 * 				call this method with Intention.BankWithdrawMoneyCustomer and true as the
	 * 				parameters. The ResidentRole is deactivated, the PersonAgent gets a new Action
	 * 				with highest priority and immediately goes to the bank, returning to his 
	 *  			ResidentRole after getting money from the bank.
	 * @param from The Role calling this method
	 * @param intents A list of things to do (i.e. go to Bank, go to Market)
	 * @param returnToCurrentAction Whether the PersonAgent should return to the previous Role after completing the new Action
	 */
	public abstract void addIntermediateActions(Role from, LinkedList<Intention> intents, boolean returnToCurrentAction);
	
	/**
	 * Adds money to this PersonAgent's money on hand
	 * @param amount How much money to add
	 */
	public abstract void addMoney(double amount);
	
	/**
	 * Removes money from this PersonAgent's money on hand
	 * @param amount How much money to remove
	 */
	public abstract void removeMoney(double amount);
	
	/**
	 * Received from a PassengerRole to update this Person's location in SimCity201
	 * @param newLocation
	 */
	public abstract void doneMoving(Structure newLocation);
	
	/**************************************************************************
	 *                                Utility                                 *
	 **************************************************************************/
	
}
