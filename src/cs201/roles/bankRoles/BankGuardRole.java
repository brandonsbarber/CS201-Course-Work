package cs201.roles.bankRoles;

import java.awt.List;
import java.util.Queue;

import cs201.agents.PersonAgent.Intention;
import cs201.interfaces.roles.bank.BankGuard;
import cs201.roles.Role;
import cs201.structures.bank.Bank;


public class BankGuardRole extends Role implements BankGuard {
	
	//================================================================================
    // Member Variables
    //================================================================================
	
	Queue<BankCustomerRole> waitingCustomers;
	//List<BankTellerRole> bankTellers;
	
	//================================================================================
    // Scheduler
    //================================================================================
	
	public boolean pickAndExecuteAnAction() {
		for(BankCustomerRole cust : waitingCustomers) {
			// Do action
		}
		if (!Bank.getOpen()) { // May not need this rule if we just allow customers to finish up
			    escortCustomersOut();
		}
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
	public void startInteraction(Intention intent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgClosingTime() {
		// TODO Auto-generated method stub
		
	}}
