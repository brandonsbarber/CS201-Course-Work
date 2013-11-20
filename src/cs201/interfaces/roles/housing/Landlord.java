package cs201.interfaces.roles.housing;

import cs201.structures.residence.Residence;

public interface Landlord {

	//messages
	
	public abstract void msgHereIsRentPayment(Renter r, double amt);
	public abstract void msgPropertyNeedsMaintenance(Renter r, Residence res);
	
}
