package cs201.test.housingTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import cs201.roles.housingRoles.ResidentRole;
import cs201.structures.residence.Residence;

public class ResidentTestEat {

	ResidentRole resident;
	Residence residence;
	
	
	@Before
	public void setUp() throws Exception {
		System.out.println("------Begin Test------ \n");
		resident = new ResidentRole();
		residence = new Residence(0, 0, 0, 0, 0);
	}

	@Test
	public void test() {
		assertNotNull("Resident not created properly", resident);
		assertNotNull("Residence not created properly", residence);
		assertFalse("Residence's fridge incorrectly initiated.", residence.hasFood());
		
		residence.addFood("Test Food 1", 1);
		
		assertTrue("JK!", residence.hasFood());
		
		
	}

}
