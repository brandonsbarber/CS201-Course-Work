package cs201.test.housingTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import cs201.agents.PersonAgent;
import cs201.gui.structures.residence.ResidenceAnimationPanel;
import cs201.roles.housingRoles.ResidentRole;
import cs201.structures.residence.Residence;

public class ResidentTestEat {

	PersonAgent person;
	ResidentRole resident;
	Residence residence;
	ResidenceAnimationPanel animationPanel;
	
	@Before
	public void setUp() throws Exception {
		System.out.println("------Begin Test------ \n");
		
		animationPanel = new ResidenceAnimationPanel(0, null);
		residence = new Residence(0, 0, 0, 0, 0, animationPanel, false);
		resident = (ResidentRole)residence.getResident();
		
		person = new PersonAgent("TestPerson");
		person.setHome(residence);
		resident.setResidence(residence);
		resident.setPerson(person);
	}

	@Test
	public void test() {
		assertNotNull("Resident not created properly", resident);
		assertNotNull("Residence not created properly", residence);
		//assertFalse("Residence's fridge incorrectly initiated.", residence.hasFood());
		
		assertTrue("Residence's fridge should have food since we included that in the constructor.", residence.hasFood());
		// I added food by default in the residence's constructor for simplicity. Normally it wouldn't have food.
		
		residence.addFood("Test Food 1", 1);
		
		assertTrue("Residence should now have a food item in the fridge.", residence.hasFood());
		
		resident.msgStartEating();
		assertTrue("Scheduler should return true", resident.pickAndExecuteAnAction());
		resident.msgAnimationDone();
		
		assertFalse("Residence should again not have food in the fridge.", residence.hasFood());
		
		assertFalse("Scheduler should return false", resident.pickAndExecuteAnAction());
	}

}
