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
	List<MarketEmployee> employees = null;
	
	public MarketStructure(int x, int y, int width, int height, int id) {
		super(x, y, width, height, id);
	}

	public Role getRole(Intention role) {
		return manager;
	}

}
