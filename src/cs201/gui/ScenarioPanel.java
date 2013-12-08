package cs201.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;


/**
 * The frame shown to switch between different SimCity201 scenarios.
 * Also functions as a modal dialog to select which scenario to start with.
 * @author Ben Doherty
 *
 */
public class ScenarioPanel extends JDialog implements ActionListener {	

	/* *********
	 * CONSTANTS
	 * *********
	 */
	
	private static final int BORDER_THICKNESS = 20;
	private static final int FRAME_HEIGHT 	= 500;
	private static final int FRAME_WIDTH 	= 500;
	
	/* ***********
	 * COMPONENETS
	 * ***********
	 */
	JPanel panel = new JPanel();
	ButtonGroup buttonGroup = new ButtonGroup();
	JScrollPane scrollPane = new JScrollPane(panel);
	
	/* *********
	 * VARIABLES
	 * *********
	 */
	private int chosenScenario = 0;
	boolean modalSelectionMode = false;
	private SimCity201 theCity = null;
	
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
	public ScenarioPanel() {
		this(new ArrayList<String>());
	}
	
	/**
	 * Creates a ScenarioFrame with the given scenarios.
	 */
	public ScenarioPanel(List<String> initialScenarios) {
		// Set the initial scenarios
		scenarios = initialScenarios;
		
		// Frame set up
		setBackground(Color.LIGHT_GRAY);
		setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		setMaximumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		this.getContentPane().add(scrollPane);
		panel.setBorder(BorderFactory.createEmptyBorder(BORDER_THICKNESS, BORDER_THICKNESS, BORDER_THICKNESS, BORDER_THICKNESS));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		setUpComponents();
	}
	
	/**
	 * Shows the Frame modally for the user to choose a scenario.
	 * Access the chosen scenario number via getChoosenScenario()
	 */
	public void showModalScenarioSelection() {
		setModalityType(ModalityType.APPLICATION_MODAL);
		modalSelectionMode = true;
		this.setVisible(true);
	}
	
	/**
	 * Shows the scenario panel for the user to choose a new scenario.
	 */
	public void showScenarioPanel() {
		modalSelectionMode = false;
		this.setVisible(true);
	}
	
	public int getChosenScenario() {
		return chosenScenario;
	}
	
	public void setSimCity(SimCity201 city) {
		theCity = city;
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
			// Create the button
			JToggleButton newButton = new JToggleButton(num + ") " + scenario);
			newButton.setAlignmentX(Component.LEFT_ALIGNMENT);
			newButton.addActionListener(this);
			
			// Add it to our list and our panel
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

	@Override
	public void actionPerformed(ActionEvent e) {
		chosenScenario = getButtonIndex((JToggleButton)e.getSource()) + 1;
		this.setVisible(false);
		
		// If we are in modal mode, call the appropriate methods in SimCity201 to reset the stage and initialize a new scenario
		if (!modalSelectionMode && theCity != null) {
			theCity.clearScenario();
			theCity.runScenario(chosenScenario);
		}
	}
	
	private int getButtonIndex(JToggleButton button) {
		for (int i = 0; i < scenarioButtons.size(); i++) {
			if (scenarioButtons.get(i) == button) {
				return i;
			}
		}
		return 0;
	}
}
