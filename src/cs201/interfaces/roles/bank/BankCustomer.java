package cs201.interfaces.roles.bank;

import cs201.roles.bankRoles.BankTellerRole;

public interface BankCustomer {

	//================================================================================
    // Messages: Mock Customers & Customer Role must implement
    //================================================================================

	public void msgSeeTeller(BankTellerRole teller);
	
	public void msgAccountIsOpened(int actNum);
	
	public void msgMoneyIsDeposited(/*int newBalance*/); // TODO
	
	public void msgMoneyIsWithdrawn(/*int newBalance*/); // TODO
	
	//public void msgOverdrawnAccount(double actBalance);
	
	public void msgLoanGranted();

	public void msgLoanDenied();
	
	public void msgClosingTime();
	
	// TODO: Verify no added/changed messages in roles
}
