package cs201.roles.bankRoles;

import cs201.roles.Role;

public class BankCustomerRole extends Role implements BankCustomer {
	
	//================================================================================
    // Member Variables
    //================================================================================
	
	BankTellerRole myTeller;
	int accountNumber;
	double accountBalance;
	Intention intent;
	CustomerState state = CustomerState.EnteringBank;
	
	public enum CustomerState {
	    EnteringBank,
	    Waiting,
	    WalkingToTeller,
	    TalkingToTeller,
	    OpeningAccount,
	    WithdrawingMoney,
	    DepositingMoney,
	    TakingOutLoan,
	    LeavingBank
	};
	
	public enum Intention {
	    OpenAccount,
	    WithdrawMoney,
	    DepositMoney,
	    TakeOutLoan
	};
	
	//================================================================================
    // Scheduler
    //================================================================================
	
	public boolean pickAndExecuteAnAction() {
		if (state == CustomerState.EnteringBank) {
			addToWaitingList();
		}
		if (state == CustomerState.WalkingToTeller) {
			goToTeller();
		}
		if (state == CustomerState.TalkingToTeller && 
		   (myPerson.getAccountNumber() == null || intent == Intention.OpenAccount)) {
			openBankAccount();
		}
		if (state == CustomerState.TalkingToTeller && intent == Intention.WithdrawMoney) {
			withdrawMoney();
		}
		if (state == CustomerState.TalkingToTeller && intent == Intention.DepositMoney) {
			depositMoney();
		}
		if (state == CustomerState.TalkingToTeller && intent == Intention.TakeOutLoan) {
			takeOutLoan();
		}
        return false;
	}
	
	//================================================================================
    // Messages
    //================================================================================
	
	public void msgSeeTeller(BankTellerRole teller) {
	    state = CustomerState.WalkingToTeller;
	    destination = teller.getLocation
	}
	
	public void msgAccountIsOpened(int actNum) {
	    myPerson.setBankAccount(actNum);
	}
	
	public void msgMoneyIsDeposited(int newBalance) {
	    accountBalance = newBalance;
	}
	
	public void msgMoneyIsWithdrawn(int newBalance) {
	    accountBalance = newBalance;
	}
	
	public void msgLoanIsGranted(int loanAmount) {
	    myPerson.setMoney(myPerson.getMoney() + loanAmount);
	}
	
	public void msgLeaveBank() {
	    state = CustomerState.LeavingBank;
	}
	
	//================================================================================
    // Actions
    //================================================================================

	private void addToWaitingList() {
	    Bank.getGuard.msgHereToSeeTeller(this);
	    state = CustomerState.Waiting;
	}
	
	private void goToTeller() {
	    DoGoToTeller(xDest, yDest);
	    state = CustomerState.TalkingToTeller;
	}
	
	private void openBankAccount() {
	    myTeller.msgOpenAccount(this, startingBalance);
	}
	
	private void withdrawMoney() {
	    myTeller.msgWithdrawMoney(accountNumber, amtNeeded);
	}
	
	private void depositMoney() {
	    myTeller.msgDepositMoney(accountNumber, amtToDeposit);
	}
	
	private void takeOutLoan() {
	    myTeller.msgRequestMoney(accountNumber, amtRequested);
	}
}
