package cs201.gui.roles.bank;

import java.awt.Graphics2D;

import cs201.gui.Gui;
import cs201.interfaces.roles.bank.BankTeller;
import cs201.roles.bankRoles.BankTellerRole;

public class BankTellerGui implements Gui
{
	int x;
	int y;
	int destX;
	int destY;
	
	BankTellerRole agent;
	
	public BankTellerGui(BankTellerRole teller)
	{
		agent = teller;
	}
	
	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setPresent(boolean present) {
		// TODO Auto-generated method stub
		
	}

}
