package cs201.interfaces.roles.bank;

import cs201.roles.bankRoles.BankCustomerRole;
import cs201.roles.bankRoles.BankTellerRole;

public interface BankGuard {
	
	//================================================================================
    // Messages: Mock Guards & Guard Role must implement
    //================================================================================
	
	public void msgHereToSeeTeller(BankCustomerRole customer);
	
	public void msgDoneWithCustomer(BankTellerRole teller);
	
	// TODO: Verify no added/changed messages in roles
	
}
