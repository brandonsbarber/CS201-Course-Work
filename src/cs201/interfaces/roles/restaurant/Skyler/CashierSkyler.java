package cs201.interfaces.roles.restaurant.Skyler;

import cs201.structures.market.MarketStructure;

public interface CashierSkyler {
	public abstract void msgRequestCheck(CustomerSkyler c, WaiterSkyler w, double amount);
	public abstract void msgHereIsCash(CustomerSkyler cust, double cash);
	
}


