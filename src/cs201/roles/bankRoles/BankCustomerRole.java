package cs201.roles.bankRoles;

import cs201.interfaces.roles.bank.BankCustomer;
import cs201.roles.Role;

public class BankCustomerRole extends Role implements BankCustomer {
	
	//================================================================================
    // Member Variables
    //================================================================================
	
	String name = "";
	
	//BankCustomerGui gui = new BankCustomerGui; // TODO: Implement Customer Gui and instantiate
	
	int accountNumber;
	double transactionAmount;
	//double accountBalance;
	
	BankTellerRole myTeller;
	CustomerAction intent;
	CustomerState state;
	
	public enum CustomerState {
	    EnteringBank,
	    Waiting,
	    WalkingToTeller,
	    TalkingToTeller,
	    LeavingBank
	};
	
	public enum CustomerAction {
	    OpenAccount,
	    WithdrawMoney,
	    DepositMoney,
	    TakeOutLoan
	};
	
//	public class DirectDeposit {
//		Restaurant restaurant;
//		int businessActNumber;
//		List<Payment> deposits = new ArrayList<Payment>();
//		
//		private class Payment {
//			PersonAgent employee;
//			double paymentAmount;
//			
//			public Payment(PersonAgent employee, double paymentAmt) {
//				this.employee = employee;
//				this.paymentAmount = paymentAmt;
//			}
//		}
//		
//		public DirectDeposit(Restaurant r, )
//	}
	
	//================================================================================
    // Constructors
    //================================================================================
	
	public BankCustomerRole(String name, CustomerAction action, double transAmt) {
		this.name = name;
		this.state = CustomerState.EnteringBank;
		this.intent = action;
		this.accountNumber = myPerson.getBankAccountNumber();
		this.transactionAmount = transAmt;
	}
	
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
		if (state == CustomerState.TalkingToTeller && intent == CustomerAction.OpenAccount) {
			openBankAccount();
		}
		if (state == CustomerState.TalkingToTeller && intent == CustomerAction.WithdrawMoney) {
			withdrawMoney();
		}
		if (state == CustomerState.TalkingToTeller && intent == CustomerAction.DepositMoney) {
			depositMoney();
		}
		if (state == CustomerState.TalkingToTeller && intent == CustomerAction.TakeOutLoan) {
			takeOutLoan();
		}
		if (state == CustomerState.LeavingBank) {
			leaveBank();
		}
        return false;
	}
	
	//================================================================================
    // Messages
    //================================================================================
	
	public void msgSeeTeller(BankTellerRole teller) {
	    state = CustomerState.WalkingToTeller;
	    //destination = teller.getLocation(); // TODO: Gui code that assigns destination and begins animation
	    stateChanged();
	}
	
	public void msgAccountIsOpened(int actNum) {
	    myPerson.setBankAccountNumber(actNum);
	    state = CustomerState.LeavingBank;
	    stateChanged();
	}
	
	public void msgMoneyIsDeposited() {
	    //accountBalance = newBalance; // TODO: Decide whether or not the customer needs to store his bank balance
		state = CustomerState.LeavingBank;
	    stateChanged();
	}
	
	public void msgMoneyIsWithdrawn(/*int newBalance*/) {
	    //accountBalance = newBalance; // TODO: Decide whether or not the customer needs to store his bank balance
		state = CustomerState.LeavingBank;
	    stateChanged();
	}
	
	/*public void msgOverdrawnAccount(double actBalance) {
		// TODO: (v2/non-norm) Figure out how we're handling overdrawn accounts
	}*/
	
	public void msgLoanGranted() {
		//accountBalance = newBalance; // TODO: Decide whether or not the customer needs to store his bank balance
		state = CustomerState.LeavingBank;
		stateChanged();
	}

	public void msgLoanDenied() {
		state = CustomerState.LeavingBank;
		stateChanged();
	}
	
	public void msgClosingTime() {
		state = CustomerState.LeavingBank;
		stateChanged();
	}
	
	
	//================================================================================
    // Actions
    //================================================================================

	public void startInteraction(cs201.agents.PersonAgent.Intention intent) {
		// TODO Auto-generated method stub
		
	}
	
	private void addToWaitingList() {
	    //Bank.getGuard().msgHereToSeeTeller(this);
	    state = CustomerState.Waiting;
	}
	
	private void goToTeller() {
	    //DoGoToTeller(xDest, yDest); // TODO: Gui code for going to specified teller booth (given by guard)
	    state = CustomerState.TalkingToTeller;
	}
	
	private void openBankAccount() {
	    myTeller.msgOpenAccount(this, transactionAmount);
	}
	
	private void withdrawMoney() {
	    myTeller.msgWithdrawMoney(this, accountNumber, transactionAmount);
	}
	
	private void depositMoney() {
	    myTeller.msgDepositMoney(this, accountNumber, transactionAmount);
	}
	
	private void takeOutLoan() {
	    myTeller.msgRequestMoney(this, accountNumber, transactionAmount);
	}
	
	private void leaveBank() {
		//gui.DoLeaveBank(); // TODO: Add gui method
	}

}
