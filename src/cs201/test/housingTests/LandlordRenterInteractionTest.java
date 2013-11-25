package cs201.test.housingTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import cs201.agents.PersonAgent;
import cs201.helper.CityTime.WeekDay;
import cs201.roles.housingRoles.LandlordRole;
import cs201.roles.housingRoles.RenterRole;
import cs201.structures.residence.Residence;

public class LandlordRenterInteractionTest {

	PersonAgent renterPerson;
	PersonAgent landlordPerson;
	RenterRole renterRole;
	LandlordRole landlordRole;
	
	Residence property;
	
	@Before
	public void setUp() throws Exception {
		renterPerson = new PersonAgent("Renter");
		landlordPerson = new PersonAgent("Landlord");
		renterRole = new RenterRole();
		landlordRole = new LandlordRole();
		
		renterRole.setPerson(renterPerson);
		renterRole.setLandlord(landlordRole);
		landlordRole.setPerson(landlordPerson);
		
		
		
		renterRole.getPerson().addMoney(40);
	}

	@Test
	public void test() {
		
		landlordRole.addProperty(new Residence(0,0,0,0,0), renterRole, 30, WeekDay.Monday);
		
		
		
		renterRole.msgRentDueYouOwe(30);
		renterRole.pickAndExecuteAnAction();
		renterRole.msgRentDueYouOwe(30);
		renterRole.pickAndExecuteAnAction();
		renterRole.msgRentDueYouOwe(30);
		renterRole.pickAndExecuteAnAction();
		renterRole.pickAndExecuteAnAction();
	}

}
