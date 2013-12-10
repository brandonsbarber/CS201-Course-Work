package cs201.interfaces.roles.restaurant.Skyler;

public interface CashierSkyler {
	public abstract void msgRequestCheck(CustomerSkyler c, WaiterSkyler w, double amount);
	public abstract void msgHereIsCash(CustomerSkyler cust, double cash);
	//public abstract void msgHereIsMarketBill(Market m, double cash);
}


