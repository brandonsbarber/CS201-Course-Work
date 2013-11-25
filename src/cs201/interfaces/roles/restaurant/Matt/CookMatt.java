package cs201.interfaces.roles.restaurant.Matt;

import cs201.structures.market.MarketStructure;

public interface CookMatt {
	/* Messages ------------------------------------------------------------------------------- */
	/**
	 * WaiterRole wants to give this CookRole a new order
	 * @param w Reference to which WaiterRole is requesting the order
	 * @param choice The CustomerRole's choice
	 * @param tableNum The table at which the CustomerRole is sitting
	 */
	public abstract void msgHereIsAnOrder(WaiterMatt w, String choice, int tableNum);
	
	/**
	 * CashierRole, after double checking an invoice from a Market, gives the CookRole the food he ordered
	 * @param type The food type being fulfilled
	 * @param amount The amount of food being fulfilled (might not be the full amount actually ordered)
	 */
	public abstract void msgFulfillSupplyOrder(String type, int amount, MarketStructure from);
	
	/**
	 * WaiterRole tells this CookRole that a plated order has been picked up
	 * @param type The food that was picked up
	 */
	public abstract void msgOrderPickedUp(String type);
}