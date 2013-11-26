package cs201.test.housingTests;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import cs201.agents.PersonAgent;
import cs201.gui.structures.residence.ResidenceAnimationPanel;
import cs201.helper.CityTime.WeekDay;
import cs201.roles.housingRoles.LandlordRole;
import cs201.structures.residence.Residence;
import cs201.test.mock.LoggedEvent;
import cs201.test.mock.housing.MockRenter;

public class LandlordRenterInteractionTest extends TestCase {

	LandlordRole landlord;
	MockRenter renter;
	ResidenceAnimationPanel animationPanel;
	Residence residence;
	
	@Before
	public void setUp() throws Exception {
		animationPanel = new ResidenceAnimationPanel(0, null);
		landlord = new LandlordRole();
		landlord.setPerson(new PersonAgent("Landlord", null));
		residence = new Residence(0, 0, 0, 0, 0, animationPanel, true);
		renter = new MockRenter("Renter");
	}

	@Test
	public void test() {
		assertTrue("Landlord should have no properties.", landlord.getMyProperties().isEmpty());
		landlord.addProperty(residence, renter, 30, WeekDay.Tuesday);
		assertFalse("Landlord should have a property now.", landlord.getMyProperties().isEmpty());
		assertTrue("Landlord scheduler should return true. It's Monday and rent for the renter is due Tuesday.", landlord.pickAndExecuteAnAction());
		assertTrue("Landlord scheduler should return true. Now that rent is due, he should request the rent.", landlord.pickAndExecuteAnAction());
		assertNotNull("Renter log should contain received msgRentDueYouOwe", renter.log.getFirstEventWhichContainsString("received msgRentDueYouOwe. Amount: "+30.0));

	}

}
