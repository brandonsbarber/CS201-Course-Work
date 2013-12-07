package cs201.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;


/**
 * The frame shown to select between scenarios to run.
 * @author Ben Doherty
 *
 */
public class ScenarioFrame extends JFrame {

	/* *********
	 * CONSTANTS
	 * *********
	 */
	
	private static final int FRAME_HEIGHT 	= 500;
	private static final int FRAME_WIDTH 	= 500;
	
	/* ***********
	 * COMPONENETS
	 * ***********
	 */
	JPanel panel = new JPanel();
	ButtonGroup buttonGroup = new ButtonGroup();
	
	/*
	 * Managing the scenarios
	 */
	List<String> scenarios;
	List<JToggleButton> scenarioButtons = new ArrayList<JToggleButton>();
	
	/**
	 * Adds a scenario to the list of scenarios displayed.
	 * @param scenarioDescription The String description of the scenario.
	 */
	public void addScenario(String scenarioDescription) {
		scenarios.add(scenarioDescription);
		addScenarioButtons();
	}

	/**
	 * Creates a blank ScenarioFrame with no scenarios.
	 */
	public ScenarioFrame() {
		this(new ArrayList<String>());
	}
	
	/**
	 * Creates a ScenarioFrame with the given scenarios.
	 */
	public ScenarioFrame(List<String> initialScenarios) {
		// Set the initial scenarios
		scenarios = initialScenarios;
		
		// Frame set up
		setBackground(Color.LIGHT_GRAY);
		setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		setMaximumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		this.getContentPane().add(panel);
		panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		setUpComponents();
		
	}
	
	private void setUpComponents() {
		// Add a label
		JLabel label = new JLabel("Select which scenario you would like to run:");
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(label);
		
		// Add any initial scenario buttons
		if (scenarios.size() > 0) addScenarioButtons();
	}
	
	private void addScenarioButtons() {
		// Clear any outstanding buttons
		clearScenarioButtons();
		
		// Go through each scenario and add a button for it
		int num = 1;
		for (String scenario : scenarios) {
			JToggleButton newButton = new JToggleButton(num + ") " + scenario);
			newButton.setAlignmentX(Component.LEFT_ALIGNMENT);
			scenarioButtons.add(newButton);
			panel.add(newButton);
			buttonGroup.add(newButton);
			
			num++;
		}
		
		this.pack();
	}
	
	private void clearScenarioButtons() {
		for (JToggleButton thisButton : scenarioButtons) {
			panel.remove(thisButton);
		}
	}
}
