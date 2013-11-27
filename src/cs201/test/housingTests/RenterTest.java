package cs201.test.housingTests;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import cs201.agents.PersonAgent;
import cs201.agents.PersonAgent.Intention;
import cs201.gui.structures.residence.ResidenceAnimationPanel;
import cs201.roles.housingRoles.RenterRole;
import cs201.structures.residence.Residence;
import cs201.test.mock.housing.MockLandlord;

public class RenterTest extends TestCase {

	RenterRole renter;
	MockLandlord landlord;
	ResidenceAnimationPanel animationPanel;
	Residence residence;
	
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		animationPanel = new ResidenceAnimationPanel(0, null);
		renter = new RenterRole();
		landlord = new MockLandlord("Landlord");
		renter.setPerson(new PersonAgent("Renter", null));
		renter.setLandlord(landlord);
		renter.setTest(true);
	}

	/**
	 * This test creates a Renter and gives him a MockLandlord who asks him for rent. The Renter should simply pay
	 * the rent when his scheduler is called next.
	 */
	@Test
	public void test() {
		assertFalse("Renter schedule should return false. Nothing to do.",renter.pickAndExecuteAnAction());
		renter.msgRentDueYouOwe(landlord, 20);
		assertEquals("Renter amtOwed should equal 20.0", renter.getAmtOwed(), 20.0);
		renter.startInteraction(Intention.ResidencePayRent);
		assertTrue("Renter schedule should return true. Renter has rentOwed>0.",renter.pickAndExecuteAnAction());
		assertEquals("Renter amtOwed should equal 0.0", renter.getAmtOwed(), 0.0);
		assertFalse("Renter schedule should return false. Nothing to do.", renter.pickAndExecuteAnAction());
		
	}

}
