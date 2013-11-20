package cs201.interfaces.roles.housing;

public interface Renter {

	// Messages
	
	public abstract void msgRentDueYouOwe (double amt);
	public abstract void msgRentLateYouOweAdditional (double amt);
	
}
