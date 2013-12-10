package cs201.gui.configPanels;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import cs201.agents.PersonAgent;

import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class PersonInfoPanel extends JPanel implements ActionListener {
	private PersonConfigPanel personPanel;
	private boolean editMode = false;
	
	private JTextField nameTextField;
	private JTextField moneyTextField;
	private JTextField hungerTextField;
	private JTextField locationTextField;
	private JTextField stateTextField;
	private JTextField wakeupTextField;
	private JTextField sleepTextField;
	private JTextField actionTextField;
	private JTextField jobTextField;
	private JTextField workTimeTextField;
	private JTextField homeTextField;
	private JCheckBox carCheckBox;
	private JTextField buyTextField;
	private JTextField inventoryTextField;
	private JButton btnNewPerson;
	private JButton btnConfirm;
	private JButton btnCancel;
	private JLabel lblMode;

	/**
	 * Create the panel.
	 */
	public PersonInfoPanel(PersonConfigPanel personPanel) {
		this.personPanel = personPanel;
		
		setBorder(null);
		setForeground(UIManager.getColor("window"));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{300, 0, 0};
		gridBagLayout.columnWeights = new double[]{3.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Person:", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, null));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		add(panel, gbc_panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JPanel leftPanel = new JPanel();
		panel.add(leftPanel);
		GridBagLayout gbl_leftPanel = new GridBagLayout();
		gbl_leftPanel.rowWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
		gbl_leftPanel.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0};
		gbl_leftPanel.columnWeights = new double[]{0.0, 1.0};
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
		gbc_nameTextField.fill = GridBagConstraints.HORIZONTAL;
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
		gbc_moneyTextField.fill = GridBagConstraints.HORIZONTAL;
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
		gbc_hungerTextField.fill = GridBagConstraints.HORIZONTAL;
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
		gbc_locationTextField.fill = GridBagConstraints.HORIZONTAL;
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
		gbc_stateTextField.fill = GridBagConstraints.HORIZONTAL;
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
		gbc_wakeupTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_wakeupTextField.insets = new Insets(0, 0, 5, 0);
		gbc_wakeupTextField.gridx = 1;
		gbc_wakeupTextField.gridy = 5;
		leftPanel.add(wakeupTextField, gbc_wakeupTextField);
		wakeupTextField.setColumns(10);
		
		JLabel lblSleep = new JLabel("Sleep:");
		lblSleep.setFont(new Font("SansSerif", Font.PLAIN, 11));
		GridBagConstraints gbc_lblSleep = new GridBagConstraints();
		gbc_lblSleep.anchor = GridBagConstraints.EAST;
		gbc_lblSleep.insets = new Insets(0, 0, 0, 5);
		gbc_lblSleep.gridx = 0;
		gbc_lblSleep.gridy = 6;
		leftPanel.add(lblSleep, gbc_lblSleep);
		lblSleep.setLabelFor(sleepTextField);
		
		sleepTextField = new JTextField();
		sleepTextField.setFont(new Font("SansSerif", Font.PLAIN, 11));
		sleepTextField.setFocusable(false);
		sleepTextField.setEditable(false);
		GridBagConstraints gbc_sleepTextField = new GridBagConstraints();
		gbc_sleepTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_sleepTextField.gridx = 1;
		gbc_sleepTextField.gridy = 6;
		leftPanel.add(sleepTextField, gbc_sleepTextField);
		sleepTextField.setColumns(10);
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		panel_1.add(horizontalStrut);
		
		JPanel centerPanel = new JPanel();
		panel.add(centerPanel);
		GridBagLayout gbl_centerPanel = new GridBagLayout();
		gbl_centerPanel.rowWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
		gbl_centerPanel.columnWidths = new int[] {0, 0};
		gbl_centerPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_centerPanel.columnWeights = new double[]{0.0, 1.0};
		centerPanel.setLayout(gbl_centerPanel);
		
		JLabel lblAction = new JLabel("Action:");
		GridBagConstraints gbc_lblAction = new GridBagConstraints();
		gbc_lblAction.anchor = GridBagConstraints.EAST;
		gbc_lblAction.insets = new Insets(0, 0, 5, 5);
		gbc_lblAction.gridx = 0;
		gbc_lblAction.gridy = 0;
		centerPanel.add(lblAction, gbc_lblAction);
		lblAction.setFont(new Font("SansSerif", Font.PLAIN, 11));
		
		actionTextField = new JTextField();
		GridBagConstraints gbc_actionTextField = new GridBagConstraints();
		gbc_actionTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_actionTextField.insets = new Insets(0, 0, 5, 0);
		gbc_actionTextField.gridx = 1;
		gbc_actionTextField.gridy = 0;
		centerPanel.add(actionTextField, gbc_actionTextField);
		actionTextField.setFont(new Font("SansSerif", Font.PLAIN, 11));
		actionTextField.setFocusable(false);
		actionTextField.setEditable(false);
		actionTextField.setColumns(10);
		lblAction.setLabelFor(actionTextField);
		
		JLabel lblHasCar = new JLabel("Has Car:");
		lblHasCar.setFont(new Font("SansSerif", Font.PLAIN, 11));
		GridBagConstraints gbc_lblHasCar = new GridBagConstraints();
		gbc_lblHasCar.insets = new Insets(0, 0, 5, 5);
		gbc_lblHasCar.anchor = GridBagConstraints.EAST;
		gbc_lblHasCar.gridx = 0;
		gbc_lblHasCar.gridy = 1;
		centerPanel.add(lblHasCar, gbc_lblHasCar);
		
		carCheckBox = new JCheckBox("");
		carCheckBox.setMargin(new Insets(1, 1, 1, 1));
		carCheckBox.setPreferredSize(new Dimension(28, 28));
		carCheckBox.setFont(new Font("SansSerif", Font.PLAIN, 11));
		carCheckBox.setMinimumSize(new Dimension(28, 28));
		carCheckBox.setMaximumSize(new Dimension(28, 28));
		carCheckBox.setFocusable(false);
		carCheckBox.setEnabled(false);
		lblHasCar.setLabelFor(carCheckBox);
		GridBagConstraints gbc_carCheckBox = new GridBagConstraints();
		gbc_carCheckBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_carCheckBox.insets = new Insets(0, 0, 5, 0);
		gbc_carCheckBox.gridx = 1;
		gbc_carCheckBox.gridy = 1;
		centerPanel.add(carCheckBox, gbc_carCheckBox);
		
		JLabel lblJob = new JLabel("Job:");
		lblJob.setFont(new Font("SansSerif", Font.PLAIN, 11));
		GridBagConstraints gbc_lblJob = new GridBagConstraints();
		gbc_lblJob.anchor = GridBagConstraints.EAST;
		gbc_lblJob.insets = new Insets(0, 0, 5, 5);
		gbc_lblJob.gridx = 0;
		gbc_lblJob.gridy = 2;
		centerPanel.add(lblJob, gbc_lblJob);
		
		jobTextField = new JTextField();
		lblJob.setLabelFor(jobTextField);
		jobTextField.setFont(new Font("SansSerif", Font.PLAIN, 11));
		jobTextField.setFocusable(false);
		jobTextField.setEditable(false);
		GridBagConstraints gbc_jobTextField = new GridBagConstraints();
		gbc_jobTextField.insets = new Insets(0, 0, 5, 0);
		gbc_jobTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_jobTextField.gridx = 1;
		gbc_jobTextField.gridy = 2;
		centerPanel.add(jobTextField, gbc_jobTextField);
		jobTextField.setColumns(10);
		
		JLabel lblWorkTime = new JLabel("Work Time:");
		lblWorkTime.setFont(new Font("SansSerif", Font.PLAIN, 11));
		GridBagConstraints gbc_lblWorkTime = new GridBagConstraints();
		gbc_lblWorkTime.anchor = GridBagConstraints.EAST;
		gbc_lblWorkTime.insets = new Insets(0, 0, 5, 5);
		gbc_lblWorkTime.gridx = 0;
		gbc_lblWorkTime.gridy = 3;
		centerPanel.add(lblWorkTime, gbc_lblWorkTime);
		
		workTimeTextField = new JTextField();
		lblWorkTime.setLabelFor(workTimeTextField);
		workTimeTextField.setFont(new Font("SansSerif", Font.PLAIN, 11));
		workTimeTextField.setFocusable(false);
		workTimeTextField.setEditable(false);
		GridBagConstraints gbc_workTimeTextField = new GridBagConstraints();
		gbc_workTimeTextField.insets = new Insets(0, 0, 5, 0);
		gbc_workTimeTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_workTimeTextField.gridx = 1;
		gbc_workTimeTextField.gridy = 3;
		centerPanel.add(workTimeTextField, gbc_workTimeTextField);
		workTimeTextField.setColumns(10);
		
		JLabel lblHasHome = new JLabel("Home:");
		lblHasHome.setFocusable(false);
		lblHasHome.setFont(new Font("SansSerif", Font.PLAIN, 11));
		GridBagConstraints gbc_lblHasHome = new GridBagConstraints();
		gbc_lblHasHome.anchor = GridBagConstraints.EAST;
		gbc_lblHasHome.insets = new Insets(0, 0, 5, 5);
		gbc_lblHasHome.gridx = 0;
		gbc_lblHasHome.gridy = 4;
		centerPanel.add(lblHasHome, gbc_lblHasHome);
		
		homeTextField = new JTextField();
		homeTextField.setFont(new Font("SansSerif", Font.PLAIN, 11));
		homeTextField.setFocusable(false);
		homeTextField.setEditable(false);
		lblHasHome.setLabelFor(homeTextField);
		GridBagConstraints gbc_homeTextField = new GridBagConstraints();
		gbc_homeTextField.insets = new Insets(0, 0, 5, 0);
		gbc_homeTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_homeTextField.gridx = 1;
		gbc_homeTextField.gridy = 4;
		centerPanel.add(homeTextField, gbc_homeTextField);
		homeTextField.setColumns(10);
		
		JLabel lblToBuy = new JLabel("To Buy:");
		lblToBuy.setFont(new Font("SansSerif", Font.PLAIN, 11));
		GridBagConstraints gbc_lblToBuy = new GridBagConstraints();
		gbc_lblToBuy.anchor = GridBagConstraints.EAST;
		gbc_lblToBuy.insets = new Insets(0, 0, 5, 5);
		gbc_lblToBuy.gridx = 0;
		gbc_lblToBuy.gridy = 5;
		centerPanel.add(lblToBuy, gbc_lblToBuy);
		
		buyTextField = new JTextField();
		lblToBuy.setLabelFor(buyTextField);
		buyTextField.setEditable(false);
		buyTextField.setFocusable(false);
		GridBagConstraints gbc_buyTextField = new GridBagConstraints();
		gbc_buyTextField.insets = new Insets(0, 0, 5, 0);
		gbc_buyTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_buyTextField.gridx = 1;
		gbc_buyTextField.gridy = 5;
		centerPanel.add(buyTextField, gbc_buyTextField);
		buyTextField.setColumns(10);
		
		JLabel lblInventory = new JLabel("Inventory:");
		lblInventory.setFont(new Font("SansSerif", Font.PLAIN, 11));
		GridBagConstraints gbc_lblInventory = new GridBagConstraints();
		gbc_lblInventory.anchor = GridBagConstraints.EAST;
		gbc_lblInventory.insets = new Insets(0, 0, 0, 5);
		gbc_lblInventory.gridx = 0;
		gbc_lblInventory.gridy = 6;
		centerPanel.add(lblInventory, gbc_lblInventory);
		
		inventoryTextField = new JTextField();
		lblInventory.setLabelFor(inventoryTextField);
		inventoryTextField.setFocusable(false);
		inventoryTextField.setEditable(false);
		GridBagConstraints gbc_inventoryTextField = new GridBagConstraints();
		gbc_inventoryTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_inventoryTextField.gridx = 1;
		gbc_inventoryTextField.gridy = 6;
		centerPanel.add(inventoryTextField, gbc_inventoryTextField);
		inventoryTextField.setColumns(10);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setBorder(new TitledBorder(null, "Create Person:", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, null));
		GridBagConstraints gbc_rightPanel = new GridBagConstraints();
		gbc_rightPanel.insets = new Insets(0, 0, 5, 0);
		gbc_rightPanel.fill = GridBagConstraints.BOTH;
		gbc_rightPanel.gridx = 1;
		gbc_rightPanel.gridy = 0;
		add(rightPanel, gbc_rightPanel);
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		
		btnNewPerson = new JButton("New Person");
		btnNewPerson.addActionListener(this);
		
		lblMode = new JLabel("Mode: View");
		lblMode.setFont(new Font("Lucida Grande", Font.BOLD, 14));
		lblMode.setAlignmentX(Component.CENTER_ALIGNMENT);
		rightPanel.add(lblMode);
		btnNewPerson.setAlignmentX(Component.CENTER_ALIGNMENT);
		rightPanel.add(btnNewPerson);
		
		btnConfirm = new JButton("Confirm");
		btnConfirm.addActionListener(this);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		rightPanel.add(verticalStrut);
		btnConfirm.setAlignmentX(Component.CENTER_ALIGNMENT);
		rightPanel.add(btnConfirm);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(this);
		btnCancel.setAlignmentX(Component.CENTER_ALIGNMENT);
		rightPanel.add(btnCancel);

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
		this.carCheckBox.setSelected(false);
		this.jobTextField.setText("");
		this.workTimeTextField.setText("");
		this.homeTextField.setText("");
		this.buyTextField.setText("");
		this.inventoryTextField.setText("");
	}
	
	private void setupEditing(boolean editMode) {
		this.nameTextField.setEditable(editMode);
		this.moneyTextField.setEditable(editMode);
		this.hungerTextField.setEditable(editMode);
		this.locationTextField.setEditable(editMode);
		this.stateTextField.setEditable(editMode);
		this.wakeupTextField.setEditable(editMode);
		this.sleepTextField.setEditable(editMode);
		this.actionTextField.setEditable(editMode);
		this.carCheckBox.setEnabled(editMode);
		this.jobTextField.setEditable(editMode);
		this.workTimeTextField.setEditable(editMode);
		this.homeTextField.setEditable(editMode);
		this.buyTextField.setEditable(editMode);
		this.inventoryTextField.setEditable(editMode);
		
		this.nameTextField.setFocusable(editMode);
		this.moneyTextField.setFocusable(editMode);
		this.hungerTextField.setFocusable(editMode);
		this.locationTextField.setFocusable(editMode);
		this.stateTextField.setFocusable(editMode);
		this.wakeupTextField.setFocusable(editMode);
		this.sleepTextField.setFocusable(editMode);
		this.actionTextField.setFocusable(editMode);
		this.carCheckBox.setFocusable(editMode);
		this.jobTextField.setFocusable(editMode);
		this.workTimeTextField.setFocusable(editMode);
		this.homeTextField.setFocusable(editMode);
		//this.buyTextField.setFocusable(editMode);
		//this.inventoryTextField.setFocusable(editMode);
	}
	
	private void setupDefaults() {
		this.nameTextField.setText("Person");
		this.moneyTextField.setText("$40.00");
		this.hungerTextField.setText("Hungry");
		this.locationTextField.setText("In City");
		this.stateTextField.setText("Sleeping");
		this.wakeupTextField.setText("7:00AM");
		this.sleepTextField.setText("10:00PM");
		this.actionTextField.setText("None");
		this.carCheckBox.setSelected(false);
		this.jobTextField.setText("None");
		this.workTimeTextField.setText("N/A");
		this.homeTextField.setText("None");
		this.buyTextField.setText("");
		this.inventoryTextField.setText("");
	}
	
	private boolean checkIfNewPersonValid() {
		return true;
	}
	
	private void createPerson() {
		
	}
	
	public void updateInfo(PersonAgent p) {
		if (p == null) {
			return;
		}
		
		this.nameTextField.setText(p.getName());
		this.moneyTextField.setText(String.format("$%.2f", p.getMoney()));
		this.hungerTextField.setText(p.getHungerLevel() > PersonAgent.STARVING ? "Starving" : p.getHungerLevel() > PersonAgent.HUNGRY ? "Hungry" : "Full");
		this.locationTextField.setText(p.getCurrentLocation() == null ? "None" : p.getCurrentAction() == null ? "In City" : p.getCurrentLocation().toString());
		this.stateTextField.setText(p.getState().toString());
		this.wakeupTextField.setText(p.getWakeupTime().toString().trim());
		this.sleepTextField.setText(p.getSleepTime().toString().trim());
		this.actionTextField.setText(p.getCurrentAction() == null ? "None" : p.getCurrentAction().toString());
		this.carCheckBox.setSelected(p.getVehicle() != null);
		this.jobTextField.setText(p.getJob() == null ? "None" : p.getJob().toString());
		this.workTimeTextField.setText(p.getJob() == null ? "N/A" : p.getWorkTime() == null ? "Not Set" : p.getWorkTime().toString().trim());
		this.homeTextField.setText(p.getHome() == null ? "None" : p.getHome().toString());
		this.buyTextField.setText(p.getMarketChecklist().toString().substring(1, p.getMarketChecklist().toString().length()-1));
		this.inventoryTextField.setText(p.getInventory().toString().substring(1, p.getInventory().toString().length()-1));
	}
	
	public boolean isInEditMode() {
		return editMode;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.btnConfirm) { // CONFIRM
			if (checkIfNewPersonValid()) {
				this.editMode = false;
				this.setupEditing(false);
				this.lblMode.setText("Mode: View");
				this.createPerson();
			} else {
				JOptionPane.showMessageDialog(null, "Please provide valid input for the new PersonAgent.");
			}
		} else if (e.getSource() == this.btnNewPerson) { // NEW PERSON
			this.editMode = true;
			this.personPanel.deselectPersonList();
			this.resetInfo();
			this.setupEditing(true);
			this.setupDefaults();
			this.lblMode.setText("Mode: Edit");
		} else if (e.getSource() == this.btnCancel) { // CANCEL
			this.editMode = false;
			this.lblMode.setText("Mode: View");
			this.setupEditing(false);
			this.resetInfo();
		}
	}
}
