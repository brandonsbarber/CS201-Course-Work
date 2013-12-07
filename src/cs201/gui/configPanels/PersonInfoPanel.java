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

public class PersonInfoPanel extends JPanel {
	private JTextField nameTextField;
	private JTextField moneyTextField;
	private JTextField hungerTextField;
	private JTextField locationTextField;

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
		gbl_leftPanel.columnWidths = new int[]{45, 0, 0, 0, 137, 0};
		gbl_leftPanel.rowHeights = new int[]{22, 22, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_leftPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_leftPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		leftPanel.setLayout(gbl_leftPanel);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setFocusable(false);
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.anchor = GridBagConstraints.EAST;
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 0;
		leftPanel.add(lblName, gbc_lblName);
		lblName.setLabelFor(nameTextField);
		
		nameTextField = new JTextField();
		nameTextField.setName("");
		nameTextField.setFocusable(false);
		nameTextField.setEditable(false);
		GridBagConstraints gbc_nameTextField = new GridBagConstraints();
		gbc_nameTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_nameTextField.gridwidth = 4;
		gbc_nameTextField.insets = new Insets(0, 0, 5, 5);
		gbc_nameTextField.gridx = 1;
		gbc_nameTextField.gridy = 0;
		leftPanel.add(nameTextField, gbc_nameTextField);
		nameTextField.setColumns(10);
		
		JLabel lblMoney = new JLabel("Money:");
		GridBagConstraints gbc_lblMoney = new GridBagConstraints();
		gbc_lblMoney.anchor = GridBagConstraints.EAST;
		gbc_lblMoney.insets = new Insets(0, 0, 5, 5);
		gbc_lblMoney.gridx = 0;
		gbc_lblMoney.gridy = 1;
		leftPanel.add(lblMoney, gbc_lblMoney);
		
		moneyTextField = new JTextField();
		moneyTextField.setEditable(false);
		lblMoney.setLabelFor(moneyTextField);
		moneyTextField.setFocusable(false);
		GridBagConstraints gbc_moneyTextField = new GridBagConstraints();
		gbc_moneyTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_moneyTextField.gridwidth = 4;
		gbc_moneyTextField.insets = new Insets(0, 0, 5, 5);
		gbc_moneyTextField.gridx = 1;
		gbc_moneyTextField.gridy = 1;
		leftPanel.add(moneyTextField, gbc_moneyTextField);
		moneyTextField.setColumns(10);
		
		JLabel lblHunger = new JLabel("Hunger:");
		GridBagConstraints gbc_lblHunger = new GridBagConstraints();
		gbc_lblHunger.anchor = GridBagConstraints.EAST;
		gbc_lblHunger.insets = new Insets(0, 0, 5, 5);
		gbc_lblHunger.gridx = 0;
		gbc_lblHunger.gridy = 2;
		leftPanel.add(lblHunger, gbc_lblHunger);
		
		hungerTextField = new JTextField();
		hungerTextField.setFocusable(false);
		lblHunger.setLabelFor(hungerTextField);
		hungerTextField.setEditable(false);
		GridBagConstraints gbc_hungerTextField = new GridBagConstraints();
		gbc_hungerTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_hungerTextField.gridwidth = 4;
		gbc_hungerTextField.insets = new Insets(0, 0, 5, 5);
		gbc_hungerTextField.gridx = 1;
		gbc_hungerTextField.gridy = 2;
		leftPanel.add(hungerTextField, gbc_hungerTextField);
		hungerTextField.setColumns(10);
		
		JLabel lblLocation = new JLabel("Location:");
		GridBagConstraints gbc_lblLocation = new GridBagConstraints();
		gbc_lblLocation.anchor = GridBagConstraints.EAST;
		gbc_lblLocation.insets = new Insets(0, 0, 5, 5);
		gbc_lblLocation.gridx = 0;
		gbc_lblLocation.gridy = 3;
		leftPanel.add(lblLocation, gbc_lblLocation);
		
		locationTextField = new JTextField();
		locationTextField.setEditable(false);
		locationTextField.setFocusable(false);
		GridBagConstraints gbc_locationTextField = new GridBagConstraints();
		gbc_locationTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_locationTextField.gridwidth = 4;
		gbc_locationTextField.insets = new Insets(0, 0, 5, 5);
		gbc_locationTextField.gridx = 1;
		gbc_locationTextField.gridy = 3;
		leftPanel.add(locationTextField, gbc_locationTextField);
		locationTextField.setColumns(10);
		
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
	}

}
