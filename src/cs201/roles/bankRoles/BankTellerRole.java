package cs201.roles.bankRoles;


public class BankTellerRole extends Role implements BankTeller {
	
	//================================================================================
    // Member Variables
    //================================================================================
	
	MyCustomer currentCustomer;
	TellerState state;
	
	public enum CustomerAction { OpenAccount, WithdrawMoney, DepositMoney, TakeOutLoan }
	private class MyCustomer {
	    BankCustomerRole currentCustomer;
	    CustomerAction action;
	    int accountNumber;
	    double availableBalance;
	    double transactionAmount;
	}
	
	public enum TellerState {
	    Unoccupied,
	    OpeningAccount,
	    WithdrawingMoney,
	    DepositingMoney,
	    AssessingLoan
	}
	
	//================================================================================
    // Scheduler
    //================================================================================
	
	public boolean pickAndExecuteAnAction() {
		if (currentCustomer.state == OpenAccount) {
			openAccount();
		}
		if (currentCustomer.state == WithdrawMoney) {
			withdrawMoney();
		}
		if (currentCustomer.state == DepositMoney) {
			depositMoney();
		}
		if (currentCustomer.state == TakeOutLoan) {
			assessLoan();
		}
        return false;
	}
	
	//================================================================================
    // Messages
    //================================================================================
	
	public void msgOpenAccount(BankCustomerRole cust, double startingBalance) {
	    currentCustomer = new MyCustomer(cust, CustomerAction.OpenAccount, startingBalance);
	    stateChanged();
	}
	
	public void msgDepositMoney(BankCustomerRole cust, int actNum, double amtToDeposit) {
	    currentCustomer = new MyCustomer(cust, CustomerAction.DepositMoney, actNum, amtToDeposit);
	    stateChanged();
	}
	
	public void msgWithdrawMoney(BankCustomerRole cust, int actNum, double amtToWithdraw) {
	    currentCustomer = new MyCustomer(cust, CustomerAction.WithdrawMoney, actNum, amtToWithdraw);
	    stateChanged();
	}
	
	public void msgRequestMoney(BankCustomerRole cust, int actNum, double amtRequested) {
	    currentCustomer = new MyCustomer(cust, CustomerAction.TakeOutLoan, actNum, amtRequested);
	    stateChanged();
	}
	
	//================================================================================
    // Actions
    //================================================================================

	private void openAccount() {
	    // Create an account number
	    
	    Bank.addAccount(actNum, currentCustomer.transactionAmount);
	    currentCustomer.getCustomer.msgAccountIsOpened(actNum); 
	    Bank.getGuard.msgDoneWithCustomer(this);
	}
	
	private void depositMoney() {
	    Bank.addMoneyToAccount(currentCustomer.getAccountNum, currentCustomer.getTransactionAmount);
	    
	    myCustomer.getCustomer.msgMoneyIsDeposited(Bank.getAccountBalance(currentCustomer.getAccountNum)); 
	    Bank.getGuard.msgDoneWithCustomer(this);
	}
	
	private void withdrawMoney() {
	    if (Bank.getAccountBalance(currentCustomer.getAccountNum) > transationAmount) {
	        Bank.subtractMoneyFromAccount(currentCustomer.getAccountNum, currentCustomer.getTransactionAmount);
	        myCustomer.getCustomer.msgMoneyIsWithdrawn(Bank.getAccountBalance(currentCustomer.getAccountNum));
	    }
	    else { // Overdrawn account, give them remaining balance
	        Bank.subtractMoneyFromAccount(currentCustomer.getAccountNum, Bank.getAccountBalance(currentCustomer.getAccountNum));
	        myCustomer.getCustomer.msgOverdrawnAccount();
	    }
	    Bank.getGuard.msgDoneWithCustomer(this);
	}
	
	private void takeOutLoan() {
	    if (Bank.getBankBalance > currentCustomer.getTransactionAmount) {
	        myCustomer.getCustomer.msgLoanGranted();
	    }
	    else {
	         myCustomer.getCustomer.msgLoanDenied();
	    }
	    Bank.getGuard.msgDoneWithCustomer(this);
	}
}
