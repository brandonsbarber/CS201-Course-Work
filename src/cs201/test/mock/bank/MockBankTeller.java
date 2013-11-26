package cs201.test.mock.bank;

import cs201.interfaces.roles.bank.BankTeller;
import cs201.roles.bankRoles.BankCustomerRole;
import cs201.test.mock.Mock;

public class MockBankTeller extends Mock implements BankTeller {
	//================================================================================
    // MOCK: Constructors
    //================================================================================
	
	public MockBankTeller(String name) {
		super(name);
	}
	
	//================================================================================
    // MOCK: Messages
    //================================================================================
	
	public void msgOpenAccount(BankCustomerRole cust, double startingBalance) {
	    // TODO
	}
	
	public void msgDepositMoney(BankCustomerRole cust, int actNum, double amtToDeposit) {
	    // TODO
	}
	
	public void msgWithdrawMoney(BankCustomerRole cust, int actNum, double amtToWithdraw) {
	    // TODO
	}
	
	public void msgRequestMoney(BankCustomerRole cust, int actNum, double amtRequested) {
	    // TODO
	}
}