package cs201.interfaces.roles.restaurant.Skyler;

public interface HostSkyler {
	public void msgWaiterReady(WaiterSkyler w);
	
	public void msgIWantFood(CustomerSkyler cust);
	
	public void msgIWantABreak(WaiterSkyler w);
	
	public void msgBackFromBreak(WaiterSkyler w);

	public void msgTableFree(int tableNum);
}
