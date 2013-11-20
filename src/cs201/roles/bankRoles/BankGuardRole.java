package cs201.roles.bankRoles;

import java.awt.List;
import java.util.Queue;


public class BankGuardRole extends Role implements BankGuard {
	
	//================================================================================
    // Member Variables
    //================================================================================
	
	Queue<BankCustomerRole> waitingCustomers;
	List<BankTellerRole> bankTellers;
	
	//================================================================================
    // Scheduler
    //================================================================================
	
	public boolean pickAndExecuteAnAction() {
		if (Ǝ BankCustomerRole cust in waitingCustomers && 
				Ǝ BankTellerRole teller in bankTellers ∋ teller.state == TellerState.UNOCCUPIED) {
			    escortToTeller(cust, teller);
		}
		if (!Bank.getOpen) { // May not need this rule if we just allow customers to finish up
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

	private void escortToTeller(cust, teller) {
	    waitingCustomers.peek().msgGoToTeller(teller);
	    waitingCustomers.remove();
	}
	
	private void escortCustomersOut(cust, teller) {
	    for (BankCustomerRole cust : waitingCustomers) {
	        waitingCustomers.peek().msgLeaveBank(cust);
	        waitingCustomers.remove();
	    }
	}
}
