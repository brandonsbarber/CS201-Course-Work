package cs201.interfaces.roles.restaurant.Skyler;

public interface WaiterSkyler {
	public abstract String getName();
	public abstract void msgCheckReady(CustomerSkyler c, double amt);
	public abstract void msgDoneEating(CustomerSkyler cust);
	public abstract void msgLeavingTable(CustomerSkyler cust);
	public abstract void msgSeatAtTable(CustomerSkyler customer, int tableNumber);
	public abstract void msgReadyToOrder(CustomerSkyler cust);
	public abstract void msgHereIsMyChoice(CustomerSkyler cust, String choice);
	public abstract void msgOutOf(String choice, int tableNum);
	public abstract void msgOrderReady(String choice, int tableNum);
	public abstract void msgHereIsFood(String choice, int tableNum);
	public abstract void msgBreakApproved();
	public abstract void msgBreakDenied();
	
	public abstract void msgWantBreak();
	public abstract void msgEndBreak();
	public abstract void msgAtTable(int atTableNum);
	public abstract void msgAtFront();
	public abstract void msgAtCook();
	public abstract void msgAtCashier();
	public abstract void msgAtWaitingArea();
	public abstract void msgGoToWork();
	
	
}