package cs201.roles;

import cs201.agents.PersonAgent;

/**
 * Base class for all Roles in SimCity201
 * @author Matt Pohlmann
 *
 */
public abstract class Role {
	PersonAgent myPerson;
	boolean isActive;
	
	/**
	 * Starts an interaction in the Role based upon the PersonAgent's intent
	 * @param intent What the PersonAgent intends to do with this Role
	 */
	public abstract void startInteraction(PersonAgent.Intention intent);
	
	/**
	 * Releases the semaphore in PersonAgent so that its scheduler begins to run again
	 */
	protected void stateChanged() {
		myPerson.stateChanged();
	}
	
	/**
	 * Acquires the semaphore in PersonAgent so that its thread will pause (usually used for GUI animation)
	 */
	protected void acquireSemaphore() {
		myPerson.animationAcquire();
	}
	
	/**
	 * Scheduler for roles is non-threaded (it is called manually by the PersonAgent's scheduler)
	 * @return True if an action was performed, false otherwise
	 */
	public abstract boolean pickAndExecuteAnAction();
	
	/**
	 * Any work-related Role must know when it's time to close and leave work. Others can simply implement
	 * then leave this function blank. (Or maybe print)
	 */
	public abstract void closingTime();
	
	/**
	 * Returns this role's PersonAgent
	 * @return A PersonAgent
	 */
	public PersonAgent getPerson() {
		return myPerson;
	}
	
	/**
	 * Sets the PersonAgent that currently has this Role
	 * @param newPerson The new PersonAgent
	 */
	public void setPerson(PersonAgent newPerson) {
		myPerson = newPerson;
	}
	
	/**
	 * Makes this Role active or inactive
	 * @param newActive True to make Role active, false to deactivate it
	 */
	public void setActive(boolean newActive) {
		isActive = newActive;
	}
	
	/**
	 * Returns whether or not this Role is currently active
	 * @return True for an active Role, false otherwise
	 */
	public boolean getActive() {
		return isActive;
	}
	
	/**
	 * General-purpose function for printing to the terminal. Format: "RestaurantCustomerRole Matt: Ordered steak"
	 * @param msg The message that should be printed (i.e. Ordered Steak)
	 */
	protected void Do(String msg) {
		StringBuffer output = new StringBuffer();
		output.append(this.getClass().getSimpleName());
		output.append(" ");
		output.append(this.myPerson.getName());
		output.append(": ");
		output.append(msg);
		
		System.out.println(output.toString());
	}
	
}
