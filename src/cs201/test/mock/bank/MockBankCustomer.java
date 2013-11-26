package cs201.test.mock.bank;

import cs201.interfaces.roles.bank.BankCustomer;
import cs201.roles.bankRoles.BankTellerRole;
import cs201.test.mock.Mock;

public class MockBankCustomer extends Mock implements BankCustomer {
	
	//================================================================================
    // MOCK: Constructors
    //================================================================================
	
	public MockBankCustomer(String name) {
		super(name);
	}

	//================================================================================
    // MOCK: Messages
    //================================================================================
	
	@Override
	public void msgSeeTeller(BankTellerRole teller) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAccountIsOpened(int actNum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgMoneyIsDeposited() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgMoneyIsWithdrawn() {
		// TODO Auto-generated method stub
		
	}

	/*@Override
	public void msgOverdrawnAccount(double actBalance) {
		// TODO Auto-generated method stub
		
	}*/

	@Override
	public void msgLoanGranted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLoanDenied() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgClosingTime() {
		// TODO Auto-generated method stub
		
	}
	
	
}