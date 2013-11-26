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
	
	public void msgSeeTeller(BankTellerRole teller) {
	    // (1) Log the message
		String msg = "BankCustomer: " + this.name + ": "; // TODO
	}
	
	public void msgAccountIsOpened(int actNum) {
	    //myPerson.setBankAccount(actNum); // TODO
	}
	
	public void msgMoneyIsDeposited(int newBalance) {
	    //accountBalance = newBalance; // TODO
	}
	
	public void msgMoneyIsWithdrawn(int newBalance) {
	    //accountBalance = newBalance; // TODO
	}
	
	public void msgLoanIsGranted(int loanAmount) {
	    //myPerson.setMoney(myPerson.getMoney() + loanAmount); // TODO
	}
	
	public void msgClosingTime() {
		// TODO
	}
	
}