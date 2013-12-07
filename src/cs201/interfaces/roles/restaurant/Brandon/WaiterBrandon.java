package cs201.interfaces.roles.restaurant.Brandon;

public interface WaiterBrandon
{
	/**
	 * Message from checkbox on GUI signaling to go on break
	 */
	public abstract void msgGotBreak();
	
	/**
	 * Message to seat a customer
	 * @param c the customer to seat
	 * @param table the table to go to
	 */
	public abstract void msgSeatCustomer(CustomerBrandon c, int table);
	
	/**
	 * Signals that a customer is ready to order
	 * @param c the customer who is ready
	 */
	public abstract void msgReadyToOrder(CustomerBrandon c);
	
	/**
	 * Messages the order from the customer
	 * @param c the customer who is ordering
	 * @param choice the choice of the customer
	 */
	public abstract void msgGiveOrder(CustomerBrandon c, String choice);
	
	/**
	 * Signals that a given food order is done cooking
	 * @param table the table at which the ordering customer sits
	 */
	public abstract void msgOrderDone(int table);
	
	/**
	 * Signals that the given customer is done eating
	 * @param c the customer who ate
	 */
	public abstract void msgDoneEating(CustomerBrandon c);
	
	/**
	 * Message from the GUI that the waiter reached his destination
	 */
	public abstract void msgReachedDestination();
	
	/**
	 * Told that a customer's order is out
	 * @param choice the order that has run out
	 * @param table the table where the order occured
	 */
	public abstract void msgOutOfFood(String choice, int table);
	
	/**
	 * Told whether he can go on break
	 * @param b whether he can go on break
	 */
	public abstract void msgCanGoOnBreak(boolean b);

	/**
	 * Message from the GUI to end break
	 */
	public abstract void msgGoOffBreak();
	
	/**
	 * Presents a bill to the waiter for giving to a customer
	 * @param cust the customer to give the bill to
	 * @param price the price to be paid
	 */
	public abstract void msgGiveWaiterBill(CustomerBrandon cust, double price);
	
	/**
	 * Informed that a customer is leaving
	 * @param cust the customer that has been given a bill
	 */
	public abstract void msgCustomerLeaving(CustomerBrandon cust);
	
	/**
	 * Gets the name of the waiter
	 * @return the name of the waiter
	 */
	public String getName();
}
