package cs201.interfaces.roles.restaurant.Brandon;

public interface HostBrandon
{
	/**
	 * Signals that a customer arrived to the restaurant
	 * @param c the customer who arrives
	 */
	public void msgArrived(CustomerBrandon c);
	
	/**
	 * Signals that a customer under the care of the given waiter has left
	 * @param w the waiter who was responsible for the customer
	 * @param table where the customer was sitting
	 */
	public void msgCustomerLeft(WaiterBrandon w, int table);
	
	/**
	 * Message received from a waiter asking to go on break
	 * @param agent the waiter asking to go on break
	 */
	public void msgWantToBreak(WaiterBrandon agent);

	/**
	 * A message received from a waiter saying that they are on break
	 * @param waiterAgent the agent going on break
	 */
	public void msgOnBreak(WaiterBrandon waiterAgent);

	/**
	 * A message indicating that a waiter is ready to work again
	 * @param waiterAgent the waiter coming off of break
	 */
	public void msgOffBreak(WaiterBrandon waiterAgent);
	
	/**
	 * Informed by a customer that they no longer want to wait for a table
	 * @param customerAgent the customer who is no longer waiting
	 */
	public void msgNotWaiting(CustomerBrandon customerAgent);
	
	public void msgLeft(CustomerBrandon cust);
}
