package cs201.gui.configPanels;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import cs201.agents.PersonAgent;
import cs201.agents.transit.CarAgent;
import cs201.gui.CityPanel;
import cs201.helper.CityDirectory;
import cs201.helper.CityTime;
import cs201.interfaces.agents.transit.Vehicle;
import cs201.structures.Structure;
import cs201.structures.residence.Residence;
import cs201.trace.AlertLog;
import cs201.trace.AlertTag;

@SuppressWarnings("serial")
public class PersonInfoPanel extends JPanel implements ActionListener {
	private PersonConfigPanel personPanel;
	private boolean editMode = false;
	
	private JTextField nameTextField;
	private JTextField moneyTextField;
	private JTextField actionTextField;
	private JTextField jobTextField;
	private JTextField workTimeTextField;
	private JCheckBox carCheckBox;
	private JTextField buyTextField;
	private JTextField inventoryTextField;
	private JButton btnNewPerson;
	private JButton btnConfirm;
	private JButton btnCancel;
	private JLabel lblMode;
	private JComboBox<String> hungerComboBox;
	private JComboBox<Structure> locationComboBox;
	private JComboBox<Structure> homeComboBox;
	private JComboBox<CityTime> wakeupComboBox;
	private JComboBox<CityTime> sleepComboBox;
	private JTextField workplaceTextField;

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
		panel.setBorder(BorderFactory.createTitledBorder("Person:"));
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
		nameTextField.setPreferredSize(new Dimension(12, 28));
		nameTextField.setMaximumSize(new Dimension(2147483647, 28));
		nameTextField.setMinimumSize(new Dimension(12, 28));
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
		moneyTextField.setPreferredSize(new Dimension(12, 28));
		moneyTextField.setMaximumSize(new Dimension(2147483647, 28));
		moneyTextField.setMinimumSize(new Dimension(12, 28));
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
		
		hungerComboBox = new JComboBox<String>();
		hungerComboBox.setFont(new Font("SansSerif", Font.PLAIN, 11));
		hungerComboBox.setEnabled(false);
		hungerComboBox.addItem("Full");
		hungerComboBox.addItem("Hungry");
		hungerComboBox.addItem("Starving");
		hungerComboBox.setSelectedIndex(-1);
		hungerComboBox.setFocusable(false);
		lblHunger.setLabelFor(hungerComboBox);
		hungerComboBox.setMaximumSize(new Dimension(32767, 28));
		hungerComboBox.setMinimumSize(new Dimension(52, 28));
		hungerComboBox.setPreferredSize(new Dimension(52, 28));
		GridBagConstraints gbc_hungerComboBox = new GridBagConstraints();
		gbc_hungerComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_hungerComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_hungerComboBox.gridx = 1;
		gbc_hungerComboBox.gridy = 2;
		leftPanel.add(hungerComboBox, gbc_hungerComboBox);
		
		JLabel lblLocation = new JLabel("Location:");
		lblLocation.setFont(new Font("SansSerif", Font.PLAIN, 11));
		GridBagConstraints gbc_lblLocation = new GridBagConstraints();
		gbc_lblLocation.anchor = GridBagConstraints.EAST;
		gbc_lblLocation.insets = new Insets(0, 0, 5, 5);
		gbc_lblLocation.gridx = 0;
		gbc_lblLocation.gridy = 3;
		leftPanel.add(lblLocation, gbc_lblLocation);
		
		locationComboBox = new JComboBox<Structure>();
		locationComboBox.setFont(new Font("SansSerif", Font.PLAIN, 11));
		lblLocation.setLabelFor(locationComboBox);
		locationComboBox.setMaximumSize(new Dimension(32767, 28));
		locationComboBox.setPreferredSize(new Dimension(34, 28));
		locationComboBox.setMinimumSize(new Dimension(34, 28));
		locationComboBox.setFocusable(false);
		locationComboBox.setEnabled(false);
		GridBagConstraints gbc_locationComboBox = new GridBagConstraints();
		gbc_locationComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_locationComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_locationComboBox.gridx = 1;
		gbc_locationComboBox.gridy = 3;
		leftPanel.add(locationComboBox, gbc_locationComboBox);
		
		JLabel lblAction = new JLabel("Action:");
		GridBagConstraints gbc_lblAction = new GridBagConstraints();
		gbc_lblAction.insets = new Insets(0, 0, 5, 5);
		gbc_lblAction.gridx = 0;
		gbc_lblAction.gridy = 4;
		leftPanel.add(lblAction, gbc_lblAction);
		lblAction.setFont(new Font("SansSerif", Font.PLAIN, 11));
		
		actionTextField = new JTextField();
		actionTextField.setPreferredSize(new Dimension(12, 28));
		actionTextField.setMaximumSize(new Dimension(2147483647, 28));
		actionTextField.setMinimumSize(new Dimension(12, 28));
		lblAction.setLabelFor(actionTextField);
		GridBagConstraints gbc_actionTextField = new GridBagConstraints();
		gbc_actionTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_actionTextField.insets = new Insets(0, 0, 5, 0);
		gbc_actionTextField.gridx = 1;
		gbc_actionTextField.gridy = 4;
		leftPanel.add(actionTextField, gbc_actionTextField);
		actionTextField.setFont(new Font("SansSerif", Font.PLAIN, 11));
		actionTextField.setFocusable(false);
		actionTextField.setEditable(false);
		actionTextField.setColumns(10);
		
		JLabel lblWakeup = new JLabel("Wakeup:");
		lblWakeup.setFont(new Font("SansSerif", Font.PLAIN, 11));
		GridBagConstraints gbc_lblWakeup = new GridBagConstraints();
		gbc_lblWakeup.anchor = GridBagConstraints.EAST;
		gbc_lblWakeup.insets = new Insets(0, 0, 5, 5);
		gbc_lblWakeup.gridx = 0;
		gbc_lblWakeup.gridy = 5;
		leftPanel.add(lblWakeup, gbc_lblWakeup);
		
		wakeupComboBox = new JComboBox<CityTime>();
		wakeupComboBox.setFont(new Font("SansSerif", Font.PLAIN, 11));
		wakeupComboBox.setMaximumSize(new Dimension(32767, 28));
		lblWakeup.setLabelFor(wakeupComboBox);
		wakeupComboBox.setFocusable(false);
		wakeupComboBox.setEnabled(false);
		wakeupComboBox.setPreferredSize(new Dimension(34, 28));
		wakeupComboBox.setMinimumSize(new Dimension(34, 28));
		GridBagConstraints gbc_wakeupComboBox = new GridBagConstraints();
		gbc_wakeupComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_wakeupComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_wakeupComboBox.gridx = 1;
		gbc_wakeupComboBox.gridy = 5;
		leftPanel.add(wakeupComboBox, gbc_wakeupComboBox);
		
		JLabel lblSleep = new JLabel("Sleep:");
		lblSleep.setFont(new Font("SansSerif", Font.PLAIN, 11));
		GridBagConstraints gbc_lblSleep = new GridBagConstraints();
		gbc_lblSleep.anchor = GridBagConstraints.EAST;
		gbc_lblSleep.insets = new Insets(0, 0, 0, 5);
		gbc_lblSleep.gridx = 0;
		gbc_lblSleep.gridy = 6;
		leftPanel.add(lblSleep, gbc_lblSleep);
		
		sleepComboBox = new JComboBox<CityTime>();
		sleepComboBox.setFont(new Font("SansSerif", Font.PLAIN, 11));
		lblSleep.setLabelFor(sleepComboBox);
		sleepComboBox.setMaximumSize(new Dimension(32767, 28));
		sleepComboBox.setMinimumSize(new Dimension(34, 28));
		sleepComboBox.setPreferredSize(new Dimension(34, 28));
		sleepComboBox.setFocusable(false);
		sleepComboBox.setEnabled(false);
		GridBagConstraints gbc_sleepComboBox = new GridBagConstraints();
		gbc_sleepComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_sleepComboBox.gridx = 1;
		gbc_sleepComboBox.gridy = 6;
		leftPanel.add(sleepComboBox, gbc_sleepComboBox);
		
		JPanel dividerPanel = new JPanel();
		panel.add(dividerPanel);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		dividerPanel.add(horizontalStrut);
		
		JPanel centerPanel = new JPanel();
		panel.add(centerPanel);
		GridBagLayout gbl_centerPanel = new GridBagLayout();
		gbl_centerPanel.rowWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
		gbl_centerPanel.columnWidths = new int[] {0, 0};
		gbl_centerPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_centerPanel.columnWeights = new double[]{0.0, 1.0};
		centerPanel.setLayout(gbl_centerPanel);
		
		JLabel lblHasCar = new JLabel("Has Car:");
		lblHasCar.setFont(new Font("SansSerif", Font.PLAIN, 11));
		GridBagConstraints gbc_lblHasCar = new GridBagConstraints();
		gbc_lblHasCar.insets = new Insets(0, 0, 5, 5);
		gbc_lblHasCar.anchor = GridBagConstraints.EAST;
		gbc_lblHasCar.gridx = 0;
		gbc_lblHasCar.gridy = 0;
		centerPanel.add(lblHasCar, gbc_lblHasCar);
		lblHasCar.setLabelFor(carCheckBox);
		
		carCheckBox = new JCheckBox("");
		carCheckBox.setIconTextGap(0);
		carCheckBox.setPreferredSize(new Dimension(28, 28));
		carCheckBox.setFont(new Font("SansSerif", Font.PLAIN, 11));
		carCheckBox.setMinimumSize(new Dimension(28, 28));
		carCheckBox.setMaximumSize(new Dimension(28, 28));
		carCheckBox.setFocusable(false);
		carCheckBox.setEnabled(false);
		GridBagConstraints gbc_carCheckBox = new GridBagConstraints();
		gbc_carCheckBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_carCheckBox.insets = new Insets(0, 0, 5, 0);
		gbc_carCheckBox.gridx = 1;
		gbc_carCheckBox.gridy = 0;
		centerPanel.add(carCheckBox, gbc_carCheckBox);
		carCheckBox.addActionListener(this);
		
		JLabel lblWorkplace = new JLabel("Workplace:");
		lblWorkplace.setFont(new Font("SansSerif", Font.PLAIN, 11));
		GridBagConstraints gbc_lblWorkplace = new GridBagConstraints();
		gbc_lblWorkplace.anchor = GridBagConstraints.EAST;
		gbc_lblWorkplace.insets = new Insets(0, 0, 5, 5);
		gbc_lblWorkplace.gridx = 0;
		gbc_lblWorkplace.gridy = 1;
		centerPanel.add(lblWorkplace, gbc_lblWorkplace);
		
		workplaceTextField = new JTextField();
		workplaceTextField.setPreferredSize(new Dimension(12, 28));
		workplaceTextField.setMaximumSize(new Dimension(2147483647, 28));
		workplaceTextField.setMinimumSize(new Dimension(12, 28));
		lblWorkplace.setLabelFor(workplaceTextField);
		workplaceTextField.setFont(new Font("SansSerif", Font.PLAIN, 11));
		workplaceTextField.setFocusable(false);
		workplaceTextField.setEditable(false);
		GridBagConstraints gbc_workplaceTextField = new GridBagConstraints();
		gbc_workplaceTextField.insets = new Insets(0, 0, 5, 0);
		gbc_workplaceTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_workplaceTextField.gridx = 1;
		gbc_workplaceTextField.gridy = 1;
		centerPanel.add(workplaceTextField, gbc_workplaceTextField);
		workplaceTextField.setColumns(10);
		
		JLabel lblJob = new JLabel("Job:");
		lblJob.setFont(new Font("SansSerif", Font.PLAIN, 11));
		GridBagConstraints gbc_lblJob = new GridBagConstraints();
		gbc_lblJob.anchor = GridBagConstraints.EAST;
		gbc_lblJob.insets = new Insets(0, 0, 5, 5);
		gbc_lblJob.gridx = 0;
		gbc_lblJob.gridy = 2;
		centerPanel.add(lblJob, gbc_lblJob);
		
		jobTextField = new JTextField();
		jobTextField.setPreferredSize(new Dimension(12, 28));
		jobTextField.setMaximumSize(new Dimension(2147483647, 28));
		jobTextField.setMinimumSize(new Dimension(12, 28));
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
		workTimeTextField.setPreferredSize(new Dimension(12, 28));
		workTimeTextField.setMaximumSize(new Dimension(2147483647, 28));
		workTimeTextField.setMinimumSize(new Dimension(12, 28));
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
		
		homeComboBox = new JComboBox<Structure>();
		homeComboBox.setFont(new Font("SansSerif", Font.PLAIN, 11));
		lblHasHome.setLabelFor(homeComboBox);
		homeComboBox.setEnabled(false);
		homeComboBox.setFocusable(false);
		homeComboBox.setMaximumSize(new Dimension(32767, 28));
		homeComboBox.setMinimumSize(new Dimension(34, 28));
		homeComboBox.setPreferredSize(new Dimension(34, 28));
		GridBagConstraints gbc_homeComboBox = new GridBagConstraints();
		gbc_homeComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_homeComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_homeComboBox.gridx = 1;
		gbc_homeComboBox.gridy = 4;
		centerPanel.add(homeComboBox, gbc_homeComboBox);
		
		JLabel lblToBuy = new JLabel("To Buy:");
		lblToBuy.setFont(new Font("SansSerif", Font.PLAIN, 11));
		GridBagConstraints gbc_lblToBuy = new GridBagConstraints();
		gbc_lblToBuy.anchor = GridBagConstraints.EAST;
		gbc_lblToBuy.insets = new Insets(0, 0, 5, 5);
		gbc_lblToBuy.gridx = 0;
		gbc_lblToBuy.gridy = 5;
		centerPanel.add(lblToBuy, gbc_lblToBuy);
		
		buyTextField = new JTextField();
		buyTextField.setPreferredSize(new Dimension(12, 28));
		buyTextField.setMaximumSize(new Dimension(2147483647, 28));
		buyTextField.setMinimumSize(new Dimension(12, 28));
		buyTextField.setFont(new Font("SansSerif", Font.PLAIN, 11));
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
		inventoryTextField.setPreferredSize(new Dimension(12, 28));
		inventoryTextField.setMaximumSize(new Dimension(2147483647, 28));
		inventoryTextField.setMinimumSize(new Dimension(12, 28));
		inventoryTextField.setFont(new Font("SansSerif", Font.PLAIN, 11));
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
		
		JScrollPane scrollPaneInstructions = new JScrollPane();
		scrollPaneInstructions.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneInstructions.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		rightPanel.add(scrollPaneInstructions);
		
		JTextArea textAreaInstructions = new JTextArea();
		textAreaInstructions.setAutoscrolls(false);
		textAreaInstructions.setLineWrap(true);
		textAreaInstructions.setWrapStyleWord(true);
		textAreaInstructions.setText("Instructions: The panel to the left displays information about the currently selected person to the right when in VIEW mode. Click \"New Person\" to enter EDIT mode to create a new Person.");
		textAreaInstructions.setFont(new Font("SansSerif", Font.ITALIC, 12));
		textAreaInstructions.setTabSize(4);
		textAreaInstructions.setFocusable(false);
		textAreaInstructions.setEditable(false);
		scrollPaneInstructions.setViewportView(textAreaInstructions);

	}
	
	public void resetInfo() {
		this.nameTextField.setText("");
		this.moneyTextField.setText("");
		this.hungerComboBox.setSelectedIndex(-1);
		this.locationComboBox.removeAllItems();
		this.wakeupComboBox.removeAllItems();
		this.sleepComboBox.removeAllItems();
		this.actionTextField.setText("");
		this.carCheckBox.setSelected(false);
		this.workplaceTextField.setText("");
		this.jobTextField.setText("");
		this.workTimeTextField.setText("");
		this.homeComboBox.removeAllItems();
		this.buyTextField.setText("");
		this.inventoryTextField.setText("");
	}
	
	private void setupEditing(boolean editMode) {
		this.nameTextField.setEditable(editMode);
		this.moneyTextField.setEditable(editMode);
		this.hungerComboBox.setEnabled(editMode);
		this.locationComboBox.setEnabled(editMode);
		this.wakeupComboBox.setEnabled(editMode);
		this.sleepComboBox.setEnabled(editMode);
		this.carCheckBox.setEnabled(editMode);
		this.homeComboBox.setEnabled(editMode);
		
		this.nameTextField.setFocusable(editMode);
		this.moneyTextField.setFocusable(editMode);
		this.hungerComboBox.setFocusable(editMode);
		this.locationComboBox.setFocusable(editMode);
		this.wakeupComboBox.setFocusable(editMode);
		this.sleepComboBox.setFocusable(editMode);
		this.carCheckBox.setFocusable(editMode);
		this.homeComboBox.setFocusable(editMode);
	}
	
	private void setupDefaults() {
		this.nameTextField.setText("Person");
		
		this.moneyTextField.setText("$200.00");
		
		this.hungerComboBox.setSelectedIndex(1);
		
		List<Structure> buildings = CityDirectory.getInstance().getAllBuildings();
		this.locationComboBox.addItem(null);
		for (Structure s : buildings) {
			this.locationComboBox.addItem(s);
		}
		
		this.wakeupComboBox.addItem(new CityTime(5, 00));
		this.wakeupComboBox.addItem(new CityTime(5, 30));
		this.wakeupComboBox.addItem(new CityTime(6, 00));
		this.wakeupComboBox.addItem(new CityTime(6, 30));
		this.wakeupComboBox.addItem(new CityTime(7, 00));
		this.wakeupComboBox.addItem(new CityTime(7, 30));
		this.wakeupComboBox.addItem(new CityTime(8, 00));
		this.wakeupComboBox.setSelectedIndex(4);
		
		this.sleepComboBox.addItem(new CityTime( 9 + 12, 00));
		this.sleepComboBox.addItem(new CityTime( 9 + 12, 30));
		this.sleepComboBox.addItem(new CityTime(10 + 12, 00));
		this.sleepComboBox.addItem(new CityTime(10 + 12, 30));
		this.sleepComboBox.addItem(new CityTime(11 + 12, 00));
		this.sleepComboBox.addItem(new CityTime(11 + 12, 30));
		this.sleepComboBox.setSelectedIndex(2);
		
		this.actionTextField.setText("None - Cannot Edit");
		
		this.carCheckBox.setSelected(false);
		
		this.workplaceTextField.setText("None - Cannot Edit");
		
		this.jobTextField.setText("None - Cannot Edit");
		
		this.workTimeTextField.setText("N/A - Cannot Edit");
		
		List<Residence> homes = CityDirectory.getInstance().getUnoccupiedResidences();
		this.homeComboBox.addItem(null);
		for (Residence r : homes) {
			this.homeComboBox.addItem(r);
		}
		
		this.buyTextField.setText("Cannot Edit");
		
		this.inventoryTextField.setText("Cannot Edit");
	}
	
	/**
	 * Creates a new PersonAgent with the values in the Person panel
	 * @return True if Person created successfully, false if something failed
	 */
	private boolean createPerson() {
		StringBuffer output = new StringBuffer();
		output.append("\nCreated a new PersonAgent:");
		
		String name = this.nameTextField.getText().trim();
		if (name.equals("")) {
			// FAILED
			AlertLog.getInstance().logError(AlertTag.GENERAL_CITY, "PersonCreationPanel", "Failed to create Person. Provide input for 'Name' field.");
			return false;
		}
		output.append("\n\tName: " + name);
		PersonAgent p = new PersonAgent(name, CityPanel.INSTANCE);
		
		double money;
		try {
			String sMoney = this.moneyTextField.getText();
			sMoney = sMoney.replace('$', ' ');
			money = Double.parseDouble(sMoney);
		} catch(NumberFormatException e) {
			// FAILED
			AlertLog.getInstance().logError(AlertTag.GENERAL_CITY, "PersonCreationPanel", "Failed to create Person. Provide correct input for 'Money' field.");
			return false;
		}
		output.append("\n\tMoney: " + money);
		p.setMoney(money);
		
		int hunger;
		switch(this.hungerComboBox.getSelectedIndex()) {
		case -1: System.out.println("FAILED IN HUNGER"); return false; // FAILED
		case  0: hunger = PersonAgent.FULL; break;
		case  1: hunger = PersonAgent.HUNGRY; break;
		case  2: hunger = PersonAgent.STARVING; break;
		default: System.out.println("FAILED IN HUNGER"); return false; // FAILED
		}
		output.append("\n\tHunger: " + hunger);
		p.setHungerLevel(hunger);
		
		Structure location = null;
		if (this.locationComboBox.getSelectedIndex() == -1 || this.locationComboBox.getSelectedIndex() == 0) {
			location = null;
		} else {
			location = (Structure) this.locationComboBox.getSelectedItem();
		}
		output.append("\n\tLocation: " + (location == null ? "None" : location.toString()));
		p.setCurrentLocation(location);
		
		CityTime wakeup = null;
		if (this.wakeupComboBox.getSelectedIndex() == -1) {
			wakeup = new CityTime(7, 00);
		} else {
			wakeup = (CityTime) this.wakeupComboBox.getSelectedItem();
		}
		output.append("\n\tWakeup Time: " + wakeup);
		p.setWakeupTime(wakeup);
		
		CityTime sleep = null;
		if (this.sleepComboBox.getSelectedIndex() == -1) {
			sleep = new CityTime(10, 00);
		} else {
			sleep = (CityTime) this.sleepComboBox.getSelectedItem();
		}
		output.append("\n\tSleep Time: " + sleep);
		p.setSleepTime(sleep);
		
		Vehicle car = null;
		if (this.carCheckBox.isSelected()) {
			car = new CarAgent();
		} else {
			car = null;
		}
		output.append("\n\tCar: " + (car == null ? "None" : car.toString()));
		p.setVehicle(car);
		
		Residence home = null;
		if (this.homeComboBox.getSelectedIndex() == -1 || this.homeComboBox.getSelectedIndex() == 0) {
			home = null;
		} else {
			home = (Residence) this.homeComboBox.getSelectedItem();
			home.setOccupied(true);
		}
		output.append("\n\tHome: " + (home == null ? "None" : home.toString()));
		p.setHome(home);
		
		// If we got here, all tests succeeded
		// add the person to the CityDirectory, etc. and start its thread
		AlertLog.getInstance().logInfo(AlertTag.GENERAL_CITY, "PersonCreationPanel", output.toString());
		p.setWakeupTime(wakeup);
		p.setMoney(money);
		p.setHungerLevel(hunger);
		p.setSleepTime(sleep);
		p.setupPerson(CityDirectory.getInstance().getTime(), home, null, null, location, car);
		personPanel.addPerson(p);
		CityDirectory.getInstance().addPerson(p);
		p.startThread();
		return true;
	}
	
	public void updateInfo(PersonAgent p) {
		if (p == null) {
			return;
		}
		
		this.nameTextField.setText(p.getName() + "  [" + p.getState() + "]");
		this.moneyTextField.setText(String.format("$%.2f", p.getMoney()));
		this.hungerComboBox.setSelectedIndex(p.getHungerLevel() > PersonAgent.STARVING ? 2 : p.getHungerLevel() > PersonAgent.HUNGRY ? 1 : 0);
		this.locationComboBox.removeAllItems();
		this.locationComboBox.addItem(p.getCurrentAction() == null ? null : p.getCurrentLocation());
		this.locationComboBox.setSelectedIndex(0);
		this.wakeupComboBox.removeAllItems();
		this.wakeupComboBox.addItem(p.getWakeupTime());
		this.wakeupComboBox.setSelectedIndex(0);
		this.sleepComboBox.removeAllItems();
		this.sleepComboBox.addItem(p.getSleepTime());
		this.sleepComboBox.setSelectedIndex(0);
		this.actionTextField.setText(p.getCurrentAction() == null ? "None" : p.getCurrentAction().toString());
		this.carCheckBox.setSelected(p.getVehicle() != null);
		this.carCheckBox.setEnabled(p.getVehicle() == null);
		this.workplaceTextField.setText(p.getWorkplace() == null ? "None" : p.getWorkplace().toString());
		this.jobTextField.setText(p.getJob() == null ? "None" : p.getJob().toString());
		this.workTimeTextField.setText(p.getJob() == null ? "N/A" : p.getWorkTime() == null ? "Not Set" : p.getWorkTime().toString().trim());
		this.homeComboBox.removeAllItems();
		this.homeComboBox.addItem(p.getHome() == null ? null : p.getHome());
		this.homeComboBox.setSelectedIndex(0);
		this.buyTextField.setText(p.getMarketChecklist().toString().substring(1, p.getMarketChecklist().toString().length()-1));
		this.inventoryTextField.setText(p.getInventory().toString().substring(1, p.getInventory().toString().length()-1));
	}
	
	public boolean isInEditMode() {
		return editMode;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.btnConfirm) { // CONFIRM
			if (editMode) {
				if (createPerson()) {
					this.editMode = false;
					this.setupEditing(false);
					this.lblMode.setText("Mode: View");
					this.personPanel.showInfo();
				}
			}
		} else if (e.getSource() == this.btnNewPerson) { // NEW PERSON
			if (!editMode) {
				this.editMode = true;
				this.personPanel.deselectPersonList();
				this.resetInfo();
				this.setupEditing(true);
				this.setupDefaults();
				this.lblMode.setText("Mode: Edit");
			}
		} else if (e.getSource() == this.btnCancel) { // CANCEL
			this.editMode = false;
			this.lblMode.setText("Mode: View");
			this.setupEditing(false);
			this.resetInfo();
		} else if (e.getSource() == this.carCheckBox) { // CAR CHECK BOX
			if (!editMode && this.carCheckBox.isEnabled() && this.carCheckBox.isSelected()) {
				//
			}
		}
	}
}
