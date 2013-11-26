package cs201.test.housingTests;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import cs201.agents.PersonAgent;
import cs201.gui.structures.residence.ResidenceAnimationPanel;
import cs201.helper.CityTime.WeekDay;
import cs201.roles.housingRoles.LandlordRole;
import cs201.roles.housingRoles.LandlordRole.RentState;
import cs201.structures.residence.Residence;
import cs201.test.mock.housing.MockRenter;

public class LandlordRenterInteractionTest extends TestCase {

	LandlordRole landlord;
	MockRenter renter;
	ResidenceAnimationPanel animationPanel;
	Residence residence;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		animationPanel = new ResidenceAnimationPanel(0, null);
		landlord = new LandlordRole();
		landlord.setPerson(new PersonAgent("Landlord", null));
		residence = new Residence(0, 0, 0, 0, 0, animationPanel, true);
		renter = new MockRenter("Renter");
	}

	@Test
	public void test() {
		assertFalse("Landlord scheduler should return false. Nothing to do.", landlord.pickAndExecuteAnAction());
		assertTrue("Landlord should have no properties.", landlord.getMyProperties().isEmpty());
		landlord.addProperty(residence, renter, 30, WeekDay.Tuesday);
		assertFalse("Landlord should have a property now.", landlord.getMyProperties().isEmpty());
		assertTrue("RentState should be notDue now.", landlord.getMyProperties().get(0).getState()==RentState.notDue);
		assertTrue("Landlord scheduler should return true. It's Monday and rent for the renter is due Tuesday.", landlord.pickAndExecuteAnAction());
		assertTrue("RentState should be dueNotNotified now.", landlord.getMyProperties().get(0).getState()==RentState.dueNotNotified);
		assertTrue("Landlord scheduler should return true. Now that rent is due, he should request the rent.", landlord.pickAndExecuteAnAction());
		assertNotNull("Renter log should contain received msgRentDueYouOwe", renter.log.getFirstEventWhichContainsString("received msgRentDueYouOwe. Amount: "+30.0));
		assertTrue("RentState should be paid now.", landlord.getMyProperties().get(0).getState()==RentState.paid);
		assertFalse("Landlord scheduler should return false. Rent payment has been received and state should be set to paid until the next day (to avoid repeated requests).", landlord.pickAndExecuteAnAction());

	}

}
