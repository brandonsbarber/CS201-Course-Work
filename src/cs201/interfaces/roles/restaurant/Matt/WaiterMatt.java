package cs201.interfaces.roles.restaurant.Matt;


public interface WaiterMatt {
	/* Utilities ------------------------------------------------------------------------------ */

	
	/* Messages ------------------------------------------------------------------------------- */
	/**
	 * HostRole tells this WaiterRole to seat a CustomerRole at a Table
	 * @param tNum The table number
	 * @param c The CustomerRole to be seated
	 */
	public abstract void msgSeatCustomer(int tNum, CustomerMatt c);
	
	/**
	 * CustomerRole tells this WaiterRole that he is ready to order
	 * @param c The CustomerRole trying to order
	 */
	public abstract void msgReadyToOrder(CustomerMatt c);
	
	/**
	 * CustomerRole tells this WaiterRole his choice
	 * @param c The CustomerRole ordering
	 * @param choice The String representation of the CustomerRole's choice
	 */
	public abstract void msgHereIsMyChoice(CustomerMatt c, String choice);
	
	/**
	 * CookAgentRole tells this WaiterRole that an order is ready to be brought out to a CustomerRole
	 * @param choice The String representing what was ordered
	 * @param tableNum The Table number where the food should be going to
	 */
	public abstract void msgOrderIsReady(String choice, int tableNum);
	
	/**
	 * CookRole tells this WaitRole that the kitchen cannot fulfill an Order
	 * @param choice String representing the food that the kitchen is out of
	 * @param tableNum The Table number from which the food was ordered
	 */
	public abstract void msgOutOfFood(String choice, int tableNum);
	
	/**
	 * CustomerRole tells this WaiterRole that he is done eating and leaving the restaurant
	 * @param c The CustomerRole leaving
	 */
	public abstract void msgDoneEatingAndNeedCheck(CustomerMatt c);
	
	/**
	 * CashierRole computes a check and return is to this WaiterRole
	 * @param c The CustomerRole the check belongs to
	 * @param check The amount of the check to be paid
	 */
	public abstract void msgHereIsCheck(CustomerMatt c, double check);
	
	/**
	 * CustomerRole tells this WaiterRole that he's paying his checking and leaving the restaurant
	 * @param c The CustomerRole leaving
	 */
	public abstract void msgPayingAndLeaving(CustomerMatt c);
	
	/**
	 * Program GUI tells this WaiterRole it should ask to go on break
	 */
	public abstract void msgAskToGoOnBreak();
	
	/**
	 * HostRole responds to this WaiterRole about whether or not he/she is allowed to go on break
	 * @param breakAllowed Whether or not the WaiterRole is allowed to go on break
	 */
	public abstract void msgBreakAllowed(boolean breakAllowed);
}
