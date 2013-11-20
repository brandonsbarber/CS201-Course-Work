package cs201.interfaces;

public interface Renter {

	// Messages
	
	public abstract void msgRentDueYouOwe (double amt);
	public abstract void msgRentLateYouOweAdditional (double amt);
	
}
