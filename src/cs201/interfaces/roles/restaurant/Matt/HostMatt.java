package cs201.interfaces.roles.restaurant.Matt;

public interface HostMatt {
	/* Messages ------------------------------------------------------------------------------- */
	/**
	 * A CustomerRole tells this HostRole that he/she is hungry
	 * @param c The new CustomerRole in the restaurant
	 */
	public abstract void msgIWantToEat(CustomerMatt c);
	
	/**
	 * CustomerRole tells this HostRole that the wait time to be seated was too long, so he/she is leaving.
	 * @param c The CustomerRole leaving the restaurant
	 */
	public abstract void msgWaitTimeTooLong(CustomerMatt c);
		
	/**
	 * The WaiterRole tells this HostRole that he is bringing the CustomerRole to his table.
	 */
	public abstract void msgCustomerRetrievedFromWaitingArea();

	/**
	 * A WaiterRole tells this HostRole that a table has been freed
	 * @param tNum The table number of the freed Table
	 */
	public abstract void msgTableIsFree(WaiterMatt w, int tNum);
	
	/**
	 * A WaiterRole asks this HostRole if he/she can go on break
	 * @param w The WaiterRole asking for a break
	 */
	public abstract void msgPermissionForBreak(WaiterMatt w);
	
	/**
	 * A WaiterRole tells this HostRole that he is off break
	 * @param w The WaiterRole coming off break
	 */
	public abstract void msgOffBreak(WaiterMatt w);
	
	/**
	 * The CashierRole tells this HostRole that he should ban a CustomerRole from the restaurant.
	 * @param c The CustomerRole being banned.
	 */
	public abstract void msgBanThisCustomer(CustomerMatt c);
}
