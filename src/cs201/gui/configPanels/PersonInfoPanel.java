package cs201.gui.configPanels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import cs201.agents.PersonAgent;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Font;
import javax.swing.border.EtchedBorder;

public class PersonInfoPanel extends JPanel {
	private JTextField nameTextField;
	private JTextField moneyTextField;
	private JTextField hungerTextField;
	private JTextField locationTextField;
	private JTextField stateTextField;
	private JTextField wakeupTextField;
	private JTextField sleepTextField;
	private JTextField actionTextField;

	/**
	 * Create the panel.
	 */
	public PersonInfoPanel() {
		setBorder(BorderFactory.createTitledBorder("Selected Person:"));
		setForeground(UIManager.getColor("window"));
		setLayout(new GridLayout(1, 0, 0, 0));
		
		JPanel leftPanel = new JPanel();
		add(leftPanel);
		GridBagLayout gbl_leftPanel = new GridBagLayout();
		gbl_leftPanel.columnWeights = new double[]{0.0, 6.0, 3.0};
		gbl_leftPanel.columnWidths = new int[]{0, 0, 0};
		leftPanel.setLayout(gbl_leftPanel);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		lblName.setFocusable(false);
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.anchor = GridBagConstraints.EAST;
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 0;
		leftPanel.add(lblName, gbc_lblName);
		lblName.setLabelFor(nameTextField);
		
		nameTextField = new JTextField();
		nameTextField.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		nameTextField.setName("");
		nameTextField.setFocusable(false);
		nameTextField.setEditable(false);
		GridBagConstraints gbc_nameTextField = new GridBagConstraints();
		gbc_nameTextField.fill = GridBagConstraints.BOTH;
		gbc_nameTextField.insets = new Insets(0, 0, 5, 5);
		gbc_nameTextField.gridx = 1;
		gbc_nameTextField.gridy = 0;
		leftPanel.add(nameTextField, gbc_nameTextField);
		nameTextField.setColumns(10);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
		gbc_verticalStrut.anchor = GridBagConstraints.WEST;
		gbc_verticalStrut.gridheight = 8;
		gbc_verticalStrut.insets = new Insets(0, 0, 5, 0);
		gbc_verticalStrut.gridx = 2;
		gbc_verticalStrut.gridy = 0;
		leftPanel.add(verticalStrut, gbc_verticalStrut);
		
		JLabel lblMoney = new JLabel("Money:");
		lblMoney.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		GridBagConstraints gbc_lblMoney = new GridBagConstraints();
		gbc_lblMoney.anchor = GridBagConstraints.EAST;
		gbc_lblMoney.insets = new Insets(0, 0, 5, 5);
		gbc_lblMoney.gridx = 0;
		gbc_lblMoney.gridy = 1;
		leftPanel.add(lblMoney, gbc_lblMoney);
		lblMoney.setLabelFor(moneyTextField);
		
		moneyTextField = new JTextField();
		moneyTextField.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		moneyTextField.setEditable(false);
		moneyTextField.setFocusable(false);
		GridBagConstraints gbc_moneyTextField = new GridBagConstraints();
		gbc_moneyTextField.fill = GridBagConstraints.BOTH;
		gbc_moneyTextField.insets = new Insets(0, 0, 5, 5);
		gbc_moneyTextField.gridx = 1;
		gbc_moneyTextField.gridy = 1;
		leftPanel.add(moneyTextField, gbc_moneyTextField);
		moneyTextField.setColumns(10);
		
		JLabel lblHunger = new JLabel("Hunger:");
		lblHunger.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		GridBagConstraints gbc_lblHunger = new GridBagConstraints();
		gbc_lblHunger.anchor = GridBagConstraints.EAST;
		gbc_lblHunger.insets = new Insets(0, 0, 5, 5);
		gbc_lblHunger.gridx = 0;
		gbc_lblHunger.gridy = 2;
		leftPanel.add(lblHunger, gbc_lblHunger);
		lblHunger.setLabelFor(hungerTextField);
		
		hungerTextField = new JTextField();
		hungerTextField.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		hungerTextField.setFocusable(false);
		hungerTextField.setEditable(false);
		GridBagConstraints gbc_hungerTextField = new GridBagConstraints();
		gbc_hungerTextField.fill = GridBagConstraints.BOTH;
		gbc_hungerTextField.insets = new Insets(0, 0, 5, 5);
		gbc_hungerTextField.gridx = 1;
		gbc_hungerTextField.gridy = 2;
		leftPanel.add(hungerTextField, gbc_hungerTextField);
		hungerTextField.setColumns(10);
		
		JLabel lblLocation = new JLabel("Location:");
		lblLocation.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		GridBagConstraints gbc_lblLocation = new GridBagConstraints();
		gbc_lblLocation.anchor = GridBagConstraints.EAST;
		gbc_lblLocation.insets = new Insets(0, 0, 5, 5);
		gbc_lblLocation.gridx = 0;
		gbc_lblLocation.gridy = 3;
		leftPanel.add(lblLocation, gbc_lblLocation);
		lblLocation.setLabelFor(locationTextField);
		
		locationTextField = new JTextField();
		locationTextField.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		locationTextField.setEditable(false);
		locationTextField.setFocusable(false);
		GridBagConstraints gbc_locationTextField = new GridBagConstraints();
		gbc_locationTextField.fill = GridBagConstraints.BOTH;
		gbc_locationTextField.insets = new Insets(0, 0, 5, 5);
		gbc_locationTextField.gridx = 1;
		gbc_locationTextField.gridy = 3;
		leftPanel.add(locationTextField, gbc_locationTextField);
		locationTextField.setColumns(10);
		
		JLabel lblState = new JLabel("State:");
		lblState.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		lblState.setLabelFor(lblState);
		GridBagConstraints gbc_lblState = new GridBagConstraints();
		gbc_lblState.anchor = GridBagConstraints.EAST;
		gbc_lblState.insets = new Insets(0, 0, 5, 5);
		gbc_lblState.gridx = 0;
		gbc_lblState.gridy = 4;
		leftPanel.add(lblState, gbc_lblState);
		
		stateTextField = new JTextField();
		stateTextField.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		stateTextField.setEditable(false);
		stateTextField.setFocusable(false);
		GridBagConstraints gbc_stateTextField = new GridBagConstraints();
		gbc_stateTextField.fill = GridBagConstraints.BOTH;
		gbc_stateTextField.insets = new Insets(0, 0, 5, 5);
		gbc_stateTextField.gridx = 1;
		gbc_stateTextField.gridy = 4;
		leftPanel.add(stateTextField, gbc_stateTextField);
		stateTextField.setColumns(10);
		
		JLabel lblWakeup = new JLabel("Wakeup:");
		lblWakeup.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		GridBagConstraints gbc_lblWakeup = new GridBagConstraints();
		gbc_lblWakeup.anchor = GridBagConstraints.EAST;
		gbc_lblWakeup.insets = new Insets(0, 0, 5, 5);
		gbc_lblWakeup.gridx = 0;
		gbc_lblWakeup.gridy = 5;
		leftPanel.add(lblWakeup, gbc_lblWakeup);
		lblWakeup.setLabelFor(wakeupTextField);
		
		wakeupTextField = new JTextField();
		wakeupTextField.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		wakeupTextField.setEditable(false);
		wakeupTextField.setFocusable(false);
		GridBagConstraints gbc_wakeupTextField = new GridBagConstraints();
		gbc_wakeupTextField.fill = GridBagConstraints.BOTH;
		gbc_wakeupTextField.insets = new Insets(0, 0, 5, 5);
		gbc_wakeupTextField.gridx = 1;
		gbc_wakeupTextField.gridy = 5;
		leftPanel.add(wakeupTextField, gbc_wakeupTextField);
		wakeupTextField.setColumns(10);
		
		JLabel lblSleep = new JLabel("Sleep:");
		lblSleep.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		GridBagConstraints gbc_lblSleep = new GridBagConstraints();
		gbc_lblSleep.anchor = GridBagConstraints.EAST;
		gbc_lblSleep.insets = new Insets(0, 0, 5, 5);
		gbc_lblSleep.gridx = 0;
		gbc_lblSleep.gridy = 6;
		leftPanel.add(lblSleep, gbc_lblSleep);
		lblSleep.setLabelFor(sleepTextField);
		
		sleepTextField = new JTextField();
		sleepTextField.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		sleepTextField.setFocusable(false);
		sleepTextField.setEditable(false);
		GridBagConstraints gbc_sleepTextField = new GridBagConstraints();
		gbc_sleepTextField.fill = GridBagConstraints.BOTH;
		gbc_sleepTextField.insets = new Insets(0, 0, 5, 5);
		gbc_sleepTextField.gridx = 1;
		gbc_sleepTextField.gridy = 6;
		leftPanel.add(sleepTextField, gbc_sleepTextField);
		sleepTextField.setColumns(10);
		
		JLabel lblAction = new JLabel("Action:");
		lblAction.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		GridBagConstraints gbc_lblAction = new GridBagConstraints();
		gbc_lblAction.anchor = GridBagConstraints.EAST;
		gbc_lblAction.insets = new Insets(0, 0, 0, 5);
		gbc_lblAction.gridx = 0;
		gbc_lblAction.gridy = 7;
		leftPanel.add(lblAction, gbc_lblAction);
		lblAction.setLabelFor(actionTextField);
		
		actionTextField = new JTextField();
		actionTextField.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		actionTextField.setFocusable(false);
		actionTextField.setEditable(false);
		GridBagConstraints gbc_actionTextField = new GridBagConstraints();
		gbc_actionTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_actionTextField.insets = new Insets(0, 0, 0, 5);
		gbc_actionTextField.gridx = 1;
		gbc_actionTextField.gridy = 7;
		leftPanel.add(actionTextField, gbc_actionTextField);
		actionTextField.setColumns(10);
		
		JPanel rightPanel = new JPanel();
		add(rightPanel);

	}
	
	public void resetInfo() {
		
	}
	
	public void updateInfo(PersonAgent p) {
		if (p == null) {
			return;
		}
		
		this.nameTextField.setText(p.getName());
		this.moneyTextField.setText(String.format("$%.2f", p.getMoney()));
		this.hungerTextField.setText(p.getHungerLevel() + "");
		this.locationTextField.setText(p.getCurrentLocation() == null ? "NULL" : p.getCurrentLocation().toString());
		this.stateTextField.setText(p.getState().toString());
		this.wakeupTextField.setText(p.getWakeupTime().toString());
		this.sleepTextField.setText(p.getSleepTime().toString());
		this.actionTextField.setText(p.getCurrentAction() == null ? "NULL" : p.getCurrentAction().toString());
	}

}
