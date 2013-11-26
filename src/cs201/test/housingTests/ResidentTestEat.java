package cs201.test.housingTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import cs201.agents.PersonAgent;
import cs201.roles.housingRoles.ResidentRole;
import cs201.structures.residence.Residence;

public class ResidentTestEat {

	PersonAgent person;
	ResidentRole resident;
	Residence residence;
	
	
	@Before
	public void setUp() throws Exception {
		System.out.println("------Begin Test------ \n");
		resident = new ResidentRole();
		
		residence = new Residence(0, 0, 0, 0, 0, null, false);
		person = new PersonAgent("TestPerson");
		person.setHome(residence);
		resident.setResidence(residence);
		resident.setPerson(person);
	}

	@Test
	public void test() {
		assertNotNull("Resident not created properly", resident);
		assertNotNull("Residence not created properly", residence);
		assertFalse("Residence's fridge incorrectly initiated.", residence.hasFood());
		
		residence.addFood("Test Food 1", 1);
		
		assertTrue("Residence should now have a food item in the fridge.", residence.hasFood());
		
		resident.msgStartEating();
		assertTrue("Scheduler should return true", resident.pickAndExecuteAnAction());
		
		assertFalse("Residence should again not have food in the fridge.", residence.hasFood());
		
		assertFalse("Scheduler should return false", resident.pickAndExecuteAnAction());
	}

}
