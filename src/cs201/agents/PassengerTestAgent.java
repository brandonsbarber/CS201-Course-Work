package cs201.agents;

import cs201.roles.transit.PassengerRole;

public class PassengerTestAgent extends PersonAgent
{
	PassengerRole role;
	
	public PassengerTestAgent(PassengerRole role)
	{
		super("Blah", null);
		this.role = role;
		role.setPerson(this);
	}

	@Override
	protected boolean pickAndExecuteAnAction()
	{
		return role.pickAndExecuteAnAction();
	}
	
}
