package cs201.roles.bankRoles;

import java.util.ArrayList;
import java.util.Queue;

import cs201.agents.PersonAgent.Intention;
import cs201.interfaces.roles.bank.BankGuard;
import cs201.roles.Role;

public class BankGuardRole extends Role implements BankGuard {
	
	//================================================================================
    // Member Variables
    //================================================================================
	
	String name = "";
	
	Queue<BankCustomerRole> waitingCustomers;
	ArrayList<BankTellerRole> bankTellers = new ArrayList<BankTellerRole>();
	
	//================================================================================
    // Constructor
    //================================================================================
	
	public BankGuardRole(String name) {
		this.name = name;
	}
	
	//================================================================================
    // Scheduler
    //================================================================================
	
	public boolean pickAndExecuteAnAction() {
		for(BankTellerRole teller : bankTellers) {
			escortToTeller(waitingCustomers.element(), teller);
		}
		/*if (!Bank.getOpen()) { // May not need this rule if we just allow customers to finish up
			escortCustomersOut();
		}*/
        return false;
	}
	
	//================================================================================
    // Messages
    //================================================================================
	
	public void msgHereToSeeTeller(BankCustomerRole customer) {
	    // Add customer to queue
	    waitingCustomers.add(customer);
	    stateChanged();
	}
	
	public void msgDoneWithCustomer(BankTellerRole teller) {
	    stateChanged();
	}
	
	//================================================================================
    // Actions
    //================================================================================

	@Override
	public void startInteraction(Intention intent) {
		// TODO Auto-generated method stub
		
	}
	
	private void escortToTeller(BankCustomerRole cust, BankTellerRole teller) {
	    waitingCustomers.peek().msgSeeTeller(teller);
	    waitingCustomers.remove();
	}
	
	private void escortCustomersOut() {
	    for (BankCustomerRole cust : waitingCustomers) {
	        waitingCustomers.peek().msgClosingTime();
	        waitingCustomers.remove();
	    }
	}


	@Override
	public void msgClosingTime() {
		// TODO Auto-generated method stub
		
	}

}
