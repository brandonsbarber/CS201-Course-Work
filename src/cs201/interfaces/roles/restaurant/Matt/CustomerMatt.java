package cs201.interfaces.roles.restaurant.Matt;

import cs201.gui.roles.restaurant.Matt.CustomerGuiMatt;
import cs201.helper.Matt.MenuMatt;

public interface CustomerMatt {
	/* Utilities ------------------------------------------------------------------------------ */
	/**
	 * Returns this CustomerRole's gui for use elsewhere
	 * @return a CustomerGui from this CustomerRole
	 */
	public abstract CustomerGuiMatt getGui();
	
	
	/* Messages ------------------------------------------------------------------------------- */
	/**
	 * Makes this CustomerRole hungry, starting the normative scenario
	 */
	public abstract void msgIsHungry();
	
	/**
	 * HostRole tells this CustomerRole that he will be seated momentarily.
	 */
	public abstract void msgAboutToBeSeated();
	
	/**
	 * A WaiterRole tells this CustomerRole to go to his table
	 * @param waiter The WaiterRole messaging this CustomerRole
	 * @param menu A Menu given to the CustomerRole so he can choose his food
	 */
	public abstract void msgFollowMeToTable(WaiterMatt waiter, MenuMatt menu);

	/**
	 * A WaiterRole asks this CustomerRole what he would like to eat
	 */
	public abstract void msgWhatWouldYouLike();
	
	/**
	 * A WaiterRole delivers this CustomerRole's food
	 * @param choice The food the CustomerRole ordered previously (For V2 should always be what
	 * the CustomerAgent actually ordered)
	 */
	public abstract void msgHereIsYourFood(String choice);
	
	/** 
	 * A WaiterRole informs this CustomerRole that the kitchen is out of his choice, so he must order again
	 * @param Menu without the CustomerRole's current choice on it
	 */
	public abstract void msgOutOfYourChoice(MenuMatt m);
	
	/**
	 * WaiterRole brings a check to this CustomerRole so he/she can go pay.
	 * @param check The amount this CustomerRole owes
	 */
	public abstract void msgHereIsYourCheck(double check);
	
	/**
	 * CashierRole gives this CustomerRole his/her change.
	 * @param change The amount of change being return to this CustomerRole
	 */
	public abstract void msgHereIsYourChange(double change);
}
