package cs201.roles.bankRoles;

import cs201.agents.PersonAgent.Intention;
import cs201.interfaces.roles.bank.BankTeller;
import cs201.roles.Role;
import cs201.structures.bank.BankStructure;
import cs201.structures.bank.BankStructure.AccountType;


public class BankTellerRole extends Role implements BankTeller {
	
	//================================================================================
    // Member Variables
    //================================================================================
	
	MyCustomer currentCustomer;
	
	BankStructure centralBank;
	
	public enum CustomerAction { OpenAccount, WithdrawMoney, DepositMoney, TakeOutLoan }
	private class MyCustomer {
	    BankCustomerRole customer;
	    CustomerAction action;
	    int accountNumber;
	    AccountType accountType;
	    double availableBalance;
	    double transactionAmount;
	    
	    public MyCustomer(BankCustomerRole cust, AccountType actType, int actNum, 
	    		CustomerAction action, double amount) {
	    	this.customer = cust;
	    	this.accountType = actType;
	    	this.accountNumber = actNum;
	    	this.action = action;
	    	this.transactionAmount = amount;
	    }
	    
	    public CustomerAction getAction() {
	    	return action;
	    }
	    
	    public int getActNumber() {
	    	return accountNumber;
	    }
	    
	    public double getTransAmount() {
	    	return transactionAmount;
	    }
	    
	    public AccountType getActType() {
	    	return accountType;
	    }

		public BankCustomerRole getCustomer() {
			return customer;
		}
	}
	
	//================================================================================
    // Scheduler
    //================================================================================
	
	public boolean pickAndExecuteAnAction() {
		if (currentCustomer.getAction() == CustomerAction.OpenAccount) {
			openAccount();
		}
		if (currentCustomer.getAction() == CustomerAction.WithdrawMoney) {
			withdrawMoney();
		}
		if (currentCustomer.getAction() == CustomerAction.DepositMoney) {
			depositMoney();
		}
		if (currentCustomer.getAction() == CustomerAction.TakeOutLoan) {
			assessLoan();
		}
        return false;
	}
	
	//================================================================================
    // Messages
    //================================================================================

	public void msgOpenAccount(BankCustomerRole cust, int actNum, double startingBalance) {
	    currentCustomer = new MyCustomer(cust, AccountType.PERSONAL, actNum, CustomerAction.OpenAccount, startingBalance);
	    stateChanged();
	}
	
	public void msgOpenAccount(/* Params for business opening account */) {
		
	}
	
	public void msgDepositMoney(BankCustomerRole cust, int actNum, double amtToDeposit) {
	    currentCustomer = new MyCustomer(cust, AccountType.PERSONAL, actNum, CustomerAction.DepositMoney, amtToDeposit);
	    stateChanged();
	}
	
	public void msgWithdrawMoney(BankCustomerRole cust, int actNum, double amtToWithdraw) {
	    currentCustomer = new MyCustomer(cust, AccountType.PERSONAL, actNum, CustomerAction.WithdrawMoney, amtToWithdraw);
	    stateChanged();
	}
	
	public void msgRequestMoney(BankCustomerRole cust, int actNum, double amtRequested) {
	    currentCustomer = new MyCustomer(cust, AccountType.PERSONAL, actNum, CustomerAction.TakeOutLoan, amtRequested);
	    stateChanged();
	}
	
	public void msgClosingTime() {
		// TODO: Figure out what to do with this function
		
	}
	
	//================================================================================
    // Actions
    //================================================================================

	public void startInteraction(Intention intent) {
		// TODO: Figure out what to do with this function
	}
	
	private void openAccount() {
	    // (1) Create an account number
	    int newActNumber = (int)(1000 * Math.random());
		
		// (2) Open account in central bank using this account number & balance
	    if(currentCustomer.getActType() == AccountType.PERSONAL) {
	    	centralBank.addPersonalAccount(  newActNumber, currentCustomer.getTransAmount()  );
	    } else if (currentCustomer.getActType() == AccountType.BUSINESS) {
	    	centralBank.addBusinessAccount(  newActNumber, currentCustomer.getTransAmount()  );
	    }
	    
	    // (3) Let customer know their account has been opened
	    currentCustomer.getCustomer().msgAccountIsOpened(  currentCustomer.getActNumber()  );
	    
	    // (4) Let guard know you are ready for next customer
	    BankStructure.getGuard().msgDoneWithCustomer(this);
	}
	
	private void depositMoney() {
	    centralBank.addMoneyToAccount(  
	    		currentCustomer.getActType(), 
	    		currentCustomer.getActNumber(), 
	    		currentCustomer.getTransAmount()  
	    );
	    
	    currentCustomer.getCustomer().msgMoneyIsDeposited();
	    BankStructure.getGuard().msgDoneWithCustomer(this);
	}
	
	private void withdrawMoney() {
	    if (centralBank.getActBalance( currentCustomer.getActType(), currentCustomer.getActNumber() ) >= currentCustomer.getTransAmount()) {
	        centralBank.takeMoneyFromAccount( currentCustomer.getActType(), currentCustomer.getActNumber(), 
	        		currentCustomer.getTransAmount() );
	        currentCustomer.getCustomer().msgMoneyIsWithdrawn();
	    }
	    else { // Overdrawn account, give them remaining balance
	    	centralBank.takeMoneyFromAccount( currentCustomer.getActType(), currentCustomer.getActNumber(), 
	    			centralBank.getActBalance( currentCustomer.getActType(), currentCustomer.getActNumber()) );
	    	currentCustomer.getCustomer().msgOverdrawnAccount(centralBank.getActBalance(currentCustomer.getActType(), 
	    			currentCustomer.getActNumber()));
	    }
	    
	    BankStructure.getGuard().msgDoneWithCustomer(this);
	}
	
	private void assessLoan() {
	    if (centralBank.getBankBalance() > currentCustomer.getTransAmount()) {
	        currentCustomer.getCustomer().msgLoanGranted();
	    }
	    
	    else {
	    	currentCustomer.getCustomer().msgLoanDenied();
	    }
	    
	    BankStructure.getGuard().msgDoneWithCustomer(this);
	}
	
}
