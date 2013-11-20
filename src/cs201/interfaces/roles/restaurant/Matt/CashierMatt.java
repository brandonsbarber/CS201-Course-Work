package cs201.interfaces.roles.restaurant.Matt;

public interface CashierMatt {
	/* Messages ------------------------------------------------------------------------------- */
	/**
	 * WaiterRole asks this CashierRole to a compute a check for some CustomerRole
	 * @param w The WaiterRole asking for the check
	 * @param c The CustomerRole the check is being computed for
	 * @param choice The CustomerRole's choice (used to compute how much is owed)
	 */
	public abstract void msgComputeCheck(WaiterMatt w, CustomerMatt c, String choice);
		
	/**
	 * CustomerRole tells this CashierRole he would like to pay his check
	 * @param c The CustomerRole paying
	 * @param customerMoney The amount of money the CustomerRole is giving to this CashierRole
	 * @param check The amount of the check to be paid
	 */
	public abstract void msgPayCheck(CustomerMatt c, double customerMoney, double check);
	
	/**
	 * Market indirectly messages this CashierRole upon delivering an order and asks to be paid
	 * @param market The MarketAgent requesting to be paid
	 * @param amount The amount to be paid
	 * @param order The type of food ordered from the Market
	 * @param quantity The amount of food ordered from the Market
	 */
	public abstract void msgPayBillFromMarket(/*Market market,*/ double amount, String order, double quantity);
	
	/**
	 * CookRole gives this CashierRole an invoice for an order he's made from a Market
	 * @param market Market ordered from
	 * @param amount Price of the order
	 * @param order The type of food ordered from the Market
	 * @param quantity The amount of food ordered from the Market
	 */
	public abstract void msgOrderInvoiceFromCook(/*Market market,*/ double amount, String order, double quantity);
}