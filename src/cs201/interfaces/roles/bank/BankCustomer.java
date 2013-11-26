package cs201.interfaces.roles.bank;

import cs201.roles.bankRoles.BankTellerRole;

public interface BankCustomer {

	//================================================================================
    // Messages: Mock Customers & Customer Role must implement
    //================================================================================

	public void msgSeeTeller(BankTellerRole teller);
	
	public void msgAccountIsOpened(int actNum);
	
	public void msgMoneyIsDeposited(int newBalance);
	
	public void msgMoneyIsWithdrawn(int newBalance);
	
	public void msgLoanIsGranted(int loanAmount);
	
	public void msgClosingTime();
	
	// TODO: Verify no added/changed messages in roles
}
