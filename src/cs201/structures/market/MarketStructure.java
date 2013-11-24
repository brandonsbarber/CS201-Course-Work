package cs201.structures.market;

import java.util.List;

import cs201.agents.PersonAgent.Intention;
import cs201.interfaces.roles.market.MarketEmployee;
import cs201.interfaces.roles.market.MarketManager;
import cs201.roles.Role;
import cs201.roles.marketRoles.MarketManagerRole;
import cs201.structures.Structure;

public class MarketStructure extends Structure {

	MarketManagerRole manager = null;
	
	/**
	 * Hires a new MarketEmployee at this market. Automatically adds the new employee to the MarketManager's list.
	 * @param newEmployee The new MarketEmployee to be added.
	 */
	public void hireEmployee(MarketEmployee newEmployee) {
		if (manager != null)
			manager.addEmployee(newEmployee);
	}
	
	/**
	 * Fires a MarketEmployee at this market.
	 * @param employee The MarketEmployee to be fired.
	 */
	public void fireEmployee(MarketEmployee employee) {
		if (manager != null)
			manager.removeEmployee(employee);
			
	}
	
	/**
	 * @return A list of MarketEmployees currently employed at this market.
	 */
	public List<MarketEmployee> getEmployees() {
		return manager.getEmployees();
	}
	
	public MarketStructure(int x, int y, int width, int height, int id) {
		super(x, y, width, height, id);
	}

	/**
	 * Based on the given role
	 * @param role the requester's Intention when visiting this Market
	 * @return A Role (usually a MarketManagerRole) representing the contact person for this market
	 */
	public Role getRole(Intention role) {
		return manager;
	}

}
