package cs201.interfaces.roles.restaurant.Skyler;

public interface CookSkyler {
	public void msgHereIsOrder(WaiterSkyler w, String choice, int tableNum);
	
	public void msgImBackFor(int tableNum);
	
	public void msgHereIsDelivery(String item, int amount);
	
	/*public void msgCantFulfill(Market m, String foodType, int amount);
	
	public void msgProcessingOrder(Market m, String foodType, int amount);
	
	public void msgOrderDone(Market m, String foodType, int amount);*/
}
