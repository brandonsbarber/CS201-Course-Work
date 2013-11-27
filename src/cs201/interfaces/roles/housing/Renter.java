package cs201.interfaces.roles.housing;

public interface Renter extends Resident{

	// Messages
	
	public abstract void msgRentDueYouOwe (Landlord l, double amt);
	public abstract void msgRentLateYouOweAdditional (Landlord l, double amt);
	
}
