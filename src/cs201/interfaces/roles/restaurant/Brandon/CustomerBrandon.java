package cs201.interfaces.roles.restaurant.Brandon;

import cs201.gui.roles.restaurant.Brandon.CustomerGuiBrandon;
import cs201.helper.Brandon.MenuBrandon;

public interface CustomerBrandon
{
	/**
	 * Signals for the customer to follow the given waiter
	 * @param w the waiter to follow
	 * @param m the menu for the customer to use
	 */
	public abstract void msgFollowMe(WaiterBrandon w, MenuBrandon m);
	
	/**
	 * Signals that the waiter is ready to take an order
	 */
	public abstract void msgReadyToTakeOrder();
	
	/**
	 * Signals that a customer can receive their order
	 */
	public abstract void msgPresentFood();
	
	/**
	 * Message from the GUI where customer reaches destination
	 */
	public abstract void msgReachedDestination();
	
	/**
	 * The customer must reorder from the new menu
	 * @param newMenu the new menu to order from
	 */
	public abstract void msgHereIsMenu(MenuBrandon newMenu);
	

	/**
	 * The customer is given the bill and told to go to the cashier
	 * @param billAmount the amount to be paid
	 * @param cash the cashier to pay the bill to
	 */
	public abstract void msgPresentBill(double billAmount,CashierBrandon cash);

	/**
	 * The customer receives change from the cashier
	 * @param change the amount of change received
	 */
	public abstract void msgGiveChange(double change);
	
	/**
	 * The customer is informed that the restaurant is full
	 */
	public abstract void msgInformedFull();
	
	/**
	 * Gets the name of the customer
	 * @return the name of the customer
	 */
	public String getName();
	
	/**
	 * Gets the GUI of the customer
	 * @return GUI for the customer
	 */
	public abstract CustomerGuiBrandon getGui();
}