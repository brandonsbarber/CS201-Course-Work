package cs201.roles.housingRoles;

import java.util.List;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.Gui;
import cs201.gui.roles.residence.ResidentGui;
import cs201.interfaces.roles.housing.Resident;
import cs201.roles.Role;
import cs201.structures.residence.Residence;

public class ResidentRole extends Role implements Resident {
	enum ResidentState {doingNothing, hungry, eating, readyToSleep, sleeping, readyToWakeUp, payingRent, relaxing};
	ResidentState state;
	private Residence residence;
	ResidentGui gui;
	
	public ResidentRole() {
		state = ResidentState.doingNothing;
		//residence = (Residence) myPerson.getHome();  //would be helpful to connect residence to person's home
	}
	
	public ResidentRole(Residence res) {
		residence = res;
		state = ResidentState.doingNothing;
	}
	
	//Messages
	
	public void msgStartEating() {
		state = ResidentState.hungry;
		stateChanged();
	}

	public void msgDoneEating() { //from animation
		state = ResidentState.doingNothing;
		stateChanged();
	}
	
	public void msgAnimationDone() {
		myPerson.animationRelease();
	}
	
	//Scheduler
	
	public boolean pickAndExecuteAnAction() {
		switch (state) {
			case readyToSleep:
					goToSleep();
					return true;
			case hungry: 
				if (residence.hasFood()) {
					pickAndEatFromFridge();
					return true;
				}
				else {
					//getPerson().goToMarket();
					Do("I need to get food from the market. I need to implement some way to do that.");
					return false;
				}
			case relaxing:
				actionFinished();
				return true;
			default: 
				break;
		}
		return false;
	}
	
	//Actions
	
	private void pickAndEatFromFridge() {
		Do("pickAndEatFromFridge called.");
		goToFridge();//animation go to fridge
		state = ResidentState.eating;
		List<String> fridgeContents = residence.getFridgeContents();
		//picks food from home's fridge list of Food and eats it. Temporarily random choice
		int rand = (int)Math.random()*fridgeContents.size();
		String foodToEat = fridgeContents.get(rand);
		residence.removeFood(foodToEat);
		
		//timer, gui animation
		myPerson.setHungerLevel(0); //clear hunger amount
		Do("Finished pickAndEatFromFridge action. I ate one of my "+foodToEat+"s from the fridge.");
		actionFinished();
	}
	
	private void goToSleep() {
		Do("Going to sleep");
		goToBed(); //animation go to bed
		state = ResidentState.sleeping;
		isActive = false;
		//timer/wait for wakeup
	}

	@Override
	public void startInteraction(Intention intent) {
		switch (intent) {
			case ResidenceEat:
				this.msgStartEating();
				break;
			case ResidenceSleep:
				state = ResidentState.readyToSleep;
				stateChanged();
				break;
			case ResidenceRelax:
				state = ResidentState.relaxing;
				stateChanged();
				break;
			default:
				break;
		}
		this.gui.setPresent(true);
		Do("Entering residence");
		gui.enter();
		this.acquireSemaphore();
	}
	
	private void actionFinished() {
		Do("Action finished. Leaving.");
		isActive = false;
		gui.exit(); //animation to leave residence
		this.acquireSemaphore();
		gui.setPresent(false); //gui inactive
	}
	
	private void goToFridge() { //animation
		Do("Going to the fridge.");
		gui.walkToFridge();
		this.acquireSemaphore();
	}
	
	private void goToBed() {
		gui.goToBed();
		this.acquireSemaphore();
	}

	@Override
	public void msgClosingTime() {
		// TODO Auto-generated method stub
		//NOT A JOB. EMPTY
	}
	
	//Utility
	
	public void setResidence(Residence newResidence) {
		residence = newResidence;
	}
	
	public void setGui(ResidentGui newGui) {
		gui = newGui;
	}
	
	public Gui getGui() {
		return gui;
	}
}
