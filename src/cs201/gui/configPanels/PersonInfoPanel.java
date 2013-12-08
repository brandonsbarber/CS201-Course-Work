package cs201.gui.configPanels;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import cs201.agents.PersonAgent;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class PersonInfoPanel extends JPanel {
	private JTextField nameTextField;
	private JTextField moneyTextField;
	private JTextField hungerTextField;
	private JTextField locationTextField;
	private JTextField stateTextField;
	private JTextField wakeupTextField;
	private JTextField sleepTextField;
	private JTextField actionTextField;
	private JTextField carTextField;
	private JTextField jobTextField;
	private JTextField workTimeTextField;

	/**
	 * Create the panel.
	 */
	public PersonInfoPanel() {
		setBorder(null);
		setForeground(UIManager.getColor("window"));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 225, 0};
		gridBagLayout.rowHeights = new int[]{300, 0, 0};
		gridBagLayout.columnWeights = new double[]{3.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Selected Person:", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, null));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		add(panel, gbc_panel);
		panel.setLayout(new GridLayout(0, 2, 0, 0));
		
		JPanel leftPanel = new JPanel();
		panel.add(leftPanel);
		GridBagLayout gbl_leftPanel = new GridBagLayout();
		gbl_leftPanel.columnWeights = new double[]{0.0, 7.0};
		gbl_leftPanel.columnWidths = new int[]{0, 0};
		leftPanel.setLayout(gbl_leftPanel);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setFont(new Font("SansSerif", Font.PLAIN, 11));
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.anchor = GridBagConstraints.EAST;
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 0;
		leftPanel.add(lblName, gbc_lblName);
		lblName.setLabelFor(nameTextField);
		
		nameTextField = new JTextField();
		nameTextField.setFont(new Font("SansSerif", Font.PLAIN, 11));
		nameTextField.setName("");
		nameTextField.setFocusable(false);
		nameTextField.setEditable(false);
		GridBagConstraints gbc_nameTextField = new GridBagConstraints();
		gbc_nameTextField.fill = GridBagConstraints.BOTH;
		gbc_nameTextField.insets = new Insets(0, 0, 5, 0);
		gbc_nameTextField.gridx = 1;
		gbc_nameTextField.gridy = 0;
		leftPanel.add(nameTextField, gbc_nameTextField);
		nameTextField.setColumns(10);
		
		JLabel lblMoney = new JLabel("Money:");
		lblMoney.setFont(new Font("SansSerif", Font.PLAIN, 11));
		GridBagConstraints gbc_lblMoney = new GridBagConstraints();
		gbc_lblMoney.anchor = GridBagConstraints.EAST;
		gbc_lblMoney.insets = new Insets(0, 0, 5, 5);
		gbc_lblMoney.gridx = 0;
		gbc_lblMoney.gridy = 1;
		leftPanel.add(lblMoney, gbc_lblMoney);
		lblMoney.setLabelFor(moneyTextField);
		
		moneyTextField = new JTextField();
		moneyTextField.setFont(new Font("SansSerif", Font.PLAIN, 11));
		moneyTextField.setEditable(false);
		moneyTextField.setFocusable(false);
		GridBagConstraints gbc_moneyTextField = new GridBagConstraints();
		gbc_moneyTextField.fill = GridBagConstraints.BOTH;
		gbc_moneyTextField.insets = new Insets(0, 0, 5, 0);
		gbc_moneyTextField.gridx = 1;
		gbc_moneyTextField.gridy = 1;
		leftPanel.add(moneyTextField, gbc_moneyTextField);
		moneyTextField.setColumns(10);
		
		JLabel lblHunger = new JLabel("Hunger:");
		lblHunger.setFont(new Font("SansSerif", Font.PLAIN, 11));
		GridBagConstraints gbc_lblHunger = new GridBagConstraints();
		gbc_lblHunger.anchor = GridBagConstraints.EAST;
		gbc_lblHunger.insets = new Insets(0, 0, 5, 5);
		gbc_lblHunger.gridx = 0;
		gbc_lblHunger.gridy = 2;
		leftPanel.add(lblHunger, gbc_lblHunger);
		lblHunger.setLabelFor(hungerTextField);
		
		hungerTextField = new JTextField();
		hungerTextField.setFont(new Font("SansSerif", Font.PLAIN, 11));
		hungerTextField.setFocusable(false);
		hungerTextField.setEditable(false);
		GridBagConstraints gbc_hungerTextField = new GridBagConstraints();
		gbc_hungerTextField.fill = GridBagConstraints.BOTH;
		gbc_hungerTextField.insets = new Insets(0, 0, 5, 0);
		gbc_hungerTextField.gridx = 1;
		gbc_hungerTextField.gridy = 2;
		leftPanel.add(hungerTextField, gbc_hungerTextField);
		hungerTextField.setColumns(10);
		
		JLabel lblLocation = new JLabel("Location:");
		lblLocation.setFont(new Font("SansSerif", Font.PLAIN, 11));
		GridBagConstraints gbc_lblLocation = new GridBagConstraints();
		gbc_lblLocation.anchor = GridBagConstraints.EAST;
		gbc_lblLocation.insets = new Insets(0, 0, 5, 5);
		gbc_lblLocation.gridx = 0;
		gbc_lblLocation.gridy = 3;
		leftPanel.add(lblLocation, gbc_lblLocation);
		lblLocation.setLabelFor(locationTextField);
		
		locationTextField = new JTextField();
		locationTextField.setFont(new Font("SansSerif", Font.PLAIN, 11));
		locationTextField.setEditable(false);
		locationTextField.setFocusable(false);
		GridBagConstraints gbc_locationTextField = new GridBagConstraints();
		gbc_locationTextField.fill = GridBagConstraints.BOTH;
		gbc_locationTextField.insets = new Insets(0, 0, 5, 0);
		gbc_locationTextField.gridx = 1;
		gbc_locationTextField.gridy = 3;
		leftPanel.add(locationTextField, gbc_locationTextField);
		locationTextField.setColumns(10);
		
		JLabel lblState = new JLabel("State:");
		lblState.setFont(new Font("SansSerif", Font.PLAIN, 11));
		lblState.setLabelFor(lblState);
		GridBagConstraints gbc_lblState = new GridBagConstraints();
		gbc_lblState.anchor = GridBagConstraints.EAST;
		gbc_lblState.insets = new Insets(0, 0, 5, 5);
		gbc_lblState.gridx = 0;
		gbc_lblState.gridy = 4;
		leftPanel.add(lblState, gbc_lblState);
		
		stateTextField = new JTextField();
		stateTextField.setFont(new Font("SansSerif", Font.PLAIN, 11));
		stateTextField.setEditable(false);
		stateTextField.setFocusable(false);
		GridBagConstraints gbc_stateTextField = new GridBagConstraints();
		gbc_stateTextField.fill = GridBagConstraints.BOTH;
		gbc_stateTextField.insets = new Insets(0, 0, 5, 0);
		gbc_stateTextField.gridx = 1;
		gbc_stateTextField.gridy = 4;
		leftPanel.add(stateTextField, gbc_stateTextField);
		stateTextField.setColumns(10);
		
		JLabel lblWakeup = new JLabel("Wakeup:");
		lblWakeup.setFont(new Font("SansSerif", Font.PLAIN, 11));
		GridBagConstraints gbc_lblWakeup = new GridBagConstraints();
		gbc_lblWakeup.anchor = GridBagConstraints.EAST;
		gbc_lblWakeup.insets = new Insets(0, 0, 5, 5);
		gbc_lblWakeup.gridx = 0;
		gbc_lblWakeup.gridy = 5;
		leftPanel.add(lblWakeup, gbc_lblWakeup);
		lblWakeup.setLabelFor(wakeupTextField);
		
		wakeupTextField = new JTextField();
		wakeupTextField.setFont(new Font("SansSerif", Font.PLAIN, 11));
		wakeupTextField.setEditable(false);
		wakeupTextField.setFocusable(false);
		GridBagConstraints gbc_wakeupTextField = new GridBagConstraints();
		gbc_wakeupTextField.fill = GridBagConstraints.BOTH;
		gbc_wakeupTextField.insets = new Insets(0, 0, 5, 0);
		gbc_wakeupTextField.gridx = 1;
		gbc_wakeupTextField.gridy = 5;
		leftPanel.add(wakeupTextField, gbc_wakeupTextField);
		wakeupTextField.setColumns(10);
		
		JLabel lblSleep = new JLabel("Sleep:");
		lblSleep.setFont(new Font("SansSerif", Font.PLAIN, 11));
		GridBagConstraints gbc_lblSleep = new GridBagConstraints();
		gbc_lblSleep.anchor = GridBagConstraints.EAST;
		gbc_lblSleep.insets = new Insets(0, 0, 5, 5);
		gbc_lblSleep.gridx = 0;
		gbc_lblSleep.gridy = 6;
		leftPanel.add(lblSleep, gbc_lblSleep);
		lblSleep.setLabelFor(sleepTextField);
		
		sleepTextField = new JTextField();
		sleepTextField.setFont(new Font("SansSerif", Font.PLAIN, 11));
		sleepTextField.setFocusable(false);
		sleepTextField.setEditable(false);
		GridBagConstraints gbc_sleepTextField = new GridBagConstraints();
		gbc_sleepTextField.fill = GridBagConstraints.BOTH;
		gbc_sleepTextField.insets = new Insets(0, 0, 5, 0);
		gbc_sleepTextField.gridx = 1;
		gbc_sleepTextField.gridy = 6;
		leftPanel.add(sleepTextField, gbc_sleepTextField);
		sleepTextField.setColumns(10);
		
		JLabel lblAction = new JLabel("Action:");
		lblAction.setFont(new Font("SansSerif", Font.PLAIN, 11));
		GridBagConstraints gbc_lblAction = new GridBagConstraints();
		gbc_lblAction.anchor = GridBagConstraints.EAST;
		gbc_lblAction.insets = new Insets(0, 0, 0, 5);
		gbc_lblAction.gridx = 0;
		gbc_lblAction.gridy = 7;
		leftPanel.add(lblAction, gbc_lblAction);
		lblAction.setLabelFor(actionTextField);
		
		actionTextField = new JTextField();
		actionTextField.setFont(new Font("SansSerif", Font.PLAIN, 11));
		actionTextField.setFocusable(false);
		actionTextField.setEditable(false);
		GridBagConstraints gbc_actionTextField = new GridBagConstraints();
		gbc_actionTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_actionTextField.gridx = 1;
		gbc_actionTextField.gridy = 7;
		leftPanel.add(actionTextField, gbc_actionTextField);
		actionTextField.setColumns(10);
		
		JPanel centerPanel = new JPanel();
		panel.add(centerPanel);
		GridBagLayout gbl_centerPanel = new GridBagLayout();
		gbl_centerPanel.columnWidths = new int[]{0, 0, 0};
		gbl_centerPanel.rowHeights = new int[]{0, 0, 0, 0};
		gbl_centerPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_centerPanel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		centerPanel.setLayout(gbl_centerPanel);
		
		JLabel lblHasCar = new JLabel("Has Car:");
		lblHasCar.setFont(new Font("SansSerif", Font.PLAIN, 11));
		GridBagConstraints gbc_lblHasCar = new GridBagConstraints();
		gbc_lblHasCar.insets = new Insets(0, 0, 5, 5);
		gbc_lblHasCar.anchor = GridBagConstraints.EAST;
		gbc_lblHasCar.gridx = 0;
		gbc_lblHasCar.gridy = 0;
		centerPanel.add(lblHasCar, gbc_lblHasCar);
		
		carTextField = new JTextField();
		carTextField.setEditable(false);
		carTextField.setFocusable(false);
		lblHasCar.setLabelFor(carTextField);
		carTextField.setFont(new Font("SansSerif", Font.PLAIN, 11));
		GridBagConstraints gbc_carTextField = new GridBagConstraints();
		gbc_carTextField.insets = new Insets(0, 0, 5, 0);
		gbc_carTextField.fill = GridBagConstraints.BOTH;
		gbc_carTextField.gridx = 1;
		gbc_carTextField.gridy = 0;
		centerPanel.add(carTextField, gbc_carTextField);
		carTextField.setColumns(10);
		
		JLabel lblJob = new JLabel("Job:");
		lblJob.setFont(new Font("SansSerif", Font.PLAIN, 11));
		GridBagConstraints gbc_lblJob = new GridBagConstraints();
		gbc_lblJob.anchor = GridBagConstraints.EAST;
		gbc_lblJob.insets = new Insets(0, 0, 5, 5);
		gbc_lblJob.gridx = 0;
		gbc_lblJob.gridy = 1;
		centerPanel.add(lblJob, gbc_lblJob);
		
		jobTextField = new JTextField();
		lblJob.setLabelFor(jobTextField);
		jobTextField.setFont(new Font("SansSerif", Font.PLAIN, 11));
		jobTextField.setFocusable(false);
		jobTextField.setEditable(false);
		GridBagConstraints gbc_jobTextField = new GridBagConstraints();
		gbc_jobTextField.insets = new Insets(0, 0, 5, 0);
		gbc_jobTextField.fill = GridBagConstraints.BOTH;
		gbc_jobTextField.gridx = 1;
		gbc_jobTextField.gridy = 1;
		centerPanel.add(jobTextField, gbc_jobTextField);
		jobTextField.setColumns(10);
		
		JLabel lblWorkTime = new JLabel("Work Time:");
		lblWorkTime.setFont(new Font("SansSerif", Font.PLAIN, 11));
		GridBagConstraints gbc_lblWorkTime = new GridBagConstraints();
		gbc_lblWorkTime.anchor = GridBagConstraints.EAST;
		gbc_lblWorkTime.insets = new Insets(0, 0, 0, 5);
		gbc_lblWorkTime.gridx = 0;
		gbc_lblWorkTime.gridy = 2;
		centerPanel.add(lblWorkTime, gbc_lblWorkTime);
		
		workTimeTextField = new JTextField();
		lblWorkTime.setLabelFor(workTimeTextField);
		workTimeTextField.setFont(new Font("SansSerif", Font.PLAIN, 11));
		workTimeTextField.setFocusable(false);
		workTimeTextField.setEditable(false);
		GridBagConstraints gbc_workTimeTextField = new GridBagConstraints();
		gbc_workTimeTextField.fill = GridBagConstraints.BOTH;
		gbc_workTimeTextField.gridx = 1;
		gbc_workTimeTextField.gridy = 2;
		centerPanel.add(workTimeTextField, gbc_workTimeTextField);
		workTimeTextField.setColumns(10);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setBorder(new TitledBorder(null, "Create Person:", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, null));
		GridBagConstraints gbc_rightPanel = new GridBagConstraints();
		gbc_rightPanel.insets = new Insets(0, 0, 5, 0);
		gbc_rightPanel.fill = GridBagConstraints.BOTH;
		gbc_rightPanel.gridx = 1;
		gbc_rightPanel.gridy = 0;
		add(rightPanel, gbc_rightPanel);

	}
	
	public void resetInfo() {
		this.nameTextField.setText("");
		this.moneyTextField.setText("");
		this.hungerTextField.setText("");
		this.locationTextField.setText("");
		this.stateTextField.setText("");
		this.wakeupTextField.setText("");
		this.sleepTextField.setText("");
		this.actionTextField.setText("");
		this.carTextField.setText("");
		this.jobTextField.setText("");
		this.workTimeTextField.setText("");
	}
	
	public void updateInfo(PersonAgent p) {
		if (p == null) {
			return;
		}
		
		this.nameTextField.setText(p.getName());
		this.moneyTextField.setText(String.format("$%.2f", p.getMoney()));
		this.hungerTextField.setText(p.getHungerLevel() > PersonAgent.STARVING ? "Starving" : p.getHungerLevel() > PersonAgent.HUNGRY ? "Hungry" : "Full");
		this.locationTextField.setText(p.getCurrentLocation() == null ? "None" : p.getCurrentLocation().toString());
		this.stateTextField.setText(p.getState().toString());
		this.wakeupTextField.setText(p.getWakeupTime().toString());
		this.sleepTextField.setText(p.getSleepTime().toString());
		this.actionTextField.setText(p.getCurrentAction() == null ? "None" : p.getCurrentAction().toString());
		this.carTextField.setText(p.getVehicle() == null ? "No" : "Yes");
		this.jobTextField.setText(p.getJob() == null ? "None" : p.getJob().toString());
		this.workTimeTextField.setText(p.getWorkTime() == null ? "N/A" : p.getWorkTime().toString());
	}

}
