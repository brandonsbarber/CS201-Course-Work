package cs201.test.mock.bank;

import cs201.interfaces.roles.bank.BankGuard;
import cs201.roles.bankRoles.BankCustomerRole;
import cs201.roles.bankRoles.BankTellerRole;
import cs201.test.mock.Mock;

public class MockBankGuard extends Mock implements BankGuard {
	//================================================================================
    // MOCK: Constructors
    //================================================================================
	
	public MockBankGuard(String name) {
		super(name);
	}
	
	//================================================================================
    // MOCK: Messages
    //================================================================================
	
	public void msgHereToSeeTeller(BankCustomerRole customer) {
	    // TODO
	}
	
	public void msgDoneWithCustomer(BankTellerRole teller) {
	    // TODO
	}
}