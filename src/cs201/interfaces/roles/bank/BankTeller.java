package cs201.interfaces.roles.bank;

import cs201.roles.bankRoles.BankCustomerRole;

public interface BankTeller {

	//================================================================================
    // Messages: Mock Tellers & Teller Role must implement
    //================================================================================
	
	public void msgOpenAccount(BankCustomerRole cust, double startingBalance);
	
	public void msgDepositMoney(BankCustomerRole cust, int actNum, double amtToDeposit);
	
	public void msgWithdrawMoney(BankCustomerRole cust, int actNum, double amtToWithdraw);
	
	public void msgRequestMoney(BankCustomerRole cust, int actNum, double amtRequested);
	
	// TODO: Verify no added/changed messages in roles
	
}
