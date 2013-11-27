package cs201.test.housingTests;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import cs201.agents.PersonAgent;
import cs201.agents.PersonAgent.Intention;
import cs201.gui.structures.residence.ResidenceAnimationPanel;
import cs201.roles.housingRoles.ResidentRole;
import cs201.roles.housingRoles.ResidentRole.ResidentState;
import cs201.structures.residence.Residence;

public class ResidentTest extends TestCase {

	ResidentRole resident;
	ResidenceAnimationPanel animationPanel;
	Residence residence;
	
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		animationPanel = new ResidenceAnimationPanel(0, null);
		residence = new Residence(0, 0, 0, 0, 0, animationPanel, false);
		resident = new ResidentRole();
		resident.setPerson(new PersonAgent("Resident", null));
		resident.setTest(true);
		resident.setResidence(residence);
		residence.setResident(resident);
	}

	/**
	 * This test creates a resident and starts interactions for each of the possible scenarios when entering
	 * a residence. Since Resident simply sets, checks, and acts based on states, his actions are very simple.
	 */
	@Test
	public void test() {
		assertTrue("Fridge should have food due to the default additions in the Residence's constructor.",residence.hasFood());
		
		assertFalse("Resident scheduler should return false. Nothing to do.", resident.pickAndExecuteAnAction());
		assertEquals("Resident state should be doingNothing.",resident.getState(), ResidentState.doingNothing);
		resident.startInteraction(Intention.ResidenceEat);
		assertEquals("Resident state should be hungry.",resident.getState(), ResidentState.hungry);
		assertTrue("Resident scheduler should return true. Now hungry.", resident.pickAndExecuteAnAction());
		assertEquals("Resident state should be doingNothing.",resident.getState(), ResidentState.doingNothing);
		assertFalse("Resident scheduler should return false. Nothing to do.", resident.pickAndExecuteAnAction());

		resident.startInteraction(Intention.ResidenceSleep);
		assertEquals("Resident state should be readyToSleep.",resident.getState(), ResidentState.readyToSleep);
		assertTrue("Resident scheduler should return true. Now readyToSleep.", resident.pickAndExecuteAnAction());
		assertFalse("Resident scheduler should return false. Nothing to do.", resident.pickAndExecuteAnAction());
		
		resident.startInteraction(Intention.ResidenceRelax);
		assertEquals("Resident state should be relaxing.",resident.getState(), ResidentState.relaxing);
		assertTrue("Resident scheduler should return true. Now relaxing.", resident.pickAndExecuteAnAction());
		assertEquals("Resident state should be doingNothing.",resident.getState(), ResidentState.doingNothing);
		assertFalse("Resident scheduler should return false. Nothing to do.", resident.pickAndExecuteAnAction());
		
		
	}

}
