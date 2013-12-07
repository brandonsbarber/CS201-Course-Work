package cs201.structures.bank;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.StructurePanel;
import cs201.helper.CityTime;
import cs201.roles.Role;
import cs201.roles.bankRoles.BankGuardRole;
import cs201.roles.bankRoles.BankTellerRole;
import cs201.structures.Structure;


public class BankStructure extends Structure {
	
	//================================================================================
    // Member Variables
    //================================================================================
	
	List<BankTellerRole> bankTellers;
	static BankGuardRole bankGuard;
	
	double bankBalance;
	
	public enum AccountType { BUSINESS, PERSONAL }
	Map<Integer, java.lang.Double> personalAccounts = new HashMap<Integer, java.lang.Double>();
	Map<Integer, java.lang.Double> businessAccounts = new HashMap<Integer, java.lang.Double>();
	
	
	//================================================================================
    // Constructors
    //================================================================================
	
	/**
	 * Default constructor for BankStructure. Initializes the bank's position, size in
	 * the GUI, its unique identifier, and the corresponding structure panel
	 * @param x		 x-coordinate within the animation panel
	 * @param y		 y-coordinate within the animation panel
	 * @param width	 width of the structure on the animation panel
	 * @param height height of the structure on the animation panel
	 * @param id	 unique identifier
	 * @param p		 corresponding structure panel
	 */
	public BankStructure(int x, int y, int width, int height, int id, StructurePanel p) {
	    super(x, y, width, height, id, p);
	}
	
	//================================================================================
    // Accessor Methods
    //================================================================================
	
	public void addPersonalAccount(int actNum, double startingBal) {
		// Add account to Hash table
		personalAccounts.put(actNum, startingBal);
		
		// Update bank balance
		this.bankBalance = this.getBankBalance();
	}
	
	public void addBusinessAccount(int actNum, double startingBal) {
		// Add account to Hash table
		businessAccounts.put(actNum, startingBal);
		
		// Update bank balance
		this.bankBalance = this.getBankBalance();
	}
	
	public void addMoneyToAccount(AccountType type, int actNum, double addedBal) {
		if (type == AccountType.PERSONAL && personalAccounts.get(actNum) != null) {
			personalAccounts.put(actNum, addedBal + personalAccounts.get(actNum));
		} 
		
		else if (type == AccountType.BUSINESS && personalAccounts.get(actNum) != null) {
			businessAccounts.put(actNum, addedBal + personalAccounts.get(actNum));
		}
	}
	
	public void takeMoneyFromAccount(AccountType type, int actNum, double subtractedBal) {
		if (type == AccountType.PERSONAL && personalAccounts.get(actNum) != null) {
			personalAccounts.put(actNum, personalAccounts.get(actNum) - subtractedBal);
		} 
		
		else if (type == AccountType.BUSINESS && personalAccounts.get(actNum) != null) {
			businessAccounts.put(actNum, personalAccounts.get(actNum) - subtractedBal);
		}
	}
	
	public Role getRole(Intention role) {
		
		switch(role) {
			case BankTeller : {
				// TODO: Return bank teller role but should there be a limit to the List size?
			}
			case BankGuard : {
				return bankGuard;
			}
			default: {
				Do("Invalid intention provided in getRole(Intention)");
				break;
			}
		}
		
		return null;
	}
	
	public double getActBalance(AccountType type, int actNum) {
		if (type == AccountType.PERSONAL) {
			return personalAccounts.get(actNum);
		} 
		
		else {
			return businessAccounts.get(actNum);
		}
	}
	
	/**
	 * @return list of bank's active tellers
	 */
	public List<BankTellerRole> getTellers() {
	    return bankTellers;
	}
	
	/**
	 * @return bank guard
	 */
	public static BankGuardRole getGuard() {
	    return bankGuard;
	}
	
	/**
	 * @return all personal accounts in SimCity201
	 */
	public Map<Integer, java.lang.Double> getPersonalAccounts() {
	    return personalAccounts;
	}
	
	/**
	 * @return all business accounts in SimCity201
	 */
	public Map<Integer, java.lang.Double> getBusinessAccounts() {
	    return businessAccounts;
	}
	
	/**
	 * @return total amount of money held by the bank (useful for granting loans)
	 */
	public double getBankBalance() {
	    // Add balance of all personal accounts...
		for(int actNum : personalAccounts.keySet()) {
	       bankBalance += personalAccounts.get(actNum);
	    } // ... and all business accounts
	    for(int actNum : businessAccounts.keySet()) {
	       bankBalance += businessAccounts.get(actNum);
	    }
	    
	    return bankBalance;
	}
	
	public void updateTime(CityTime time) {
		// TODO Auto-generated method stub
	}
	
}