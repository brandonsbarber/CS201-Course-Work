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
		gbl_leftPanel.columnWidths = new int[]{45, 137, 0};
		gbl_leftPanel.rowHeights = new int[]{22, 22, 0, 0, 0};
		gbl_leftPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_leftPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		leftPanel.setLayout(gbl_leftPanel);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setFocusable(false);
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.anchor = GridBagConstraints.WEST;
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 0;
		leftPanel.add(lblName, gbc_lblName);
		lblName.setLabelFor(nameTextField);
		
		nameTextField = new JTextField();
		nameTextField.setOpaque(true);
		nameTextField.setName("");
		nameTextField.setFocusable(false);
		nameTextField.setEditable(false);
		GridBagConstraints gbc_nameTextField = new GridBagConstraints();
		gbc_nameTextField.anchor = GridBagConstraints.NORTHWEST;
		gbc_nameTextField.insets = new Insets(0, 0, 5, 0);
		gbc_nameTextField.gridx = 1;
		gbc_nameTextField.gridy = 0;
		leftPanel.add(nameTextField, gbc_nameTextField);
		nameTextField.setColumns(10);
		
		JLabel lblMoney = new JLabel("Money:");
		GridBagConstraints gbc_lblMoney = new GridBagConstraints();
		gbc_lblMoney.anchor = GridBagConstraints.WEST;
		gbc_lblMoney.insets = new Insets(0, 0, 5, 5);
		gbc_lblMoney.gridx = 0;
		gbc_lblMoney.gridy = 1;
		leftPanel.add(lblMoney, gbc_lblMoney);
		
		moneyTextField = new JTextField();
		moneyTextField.setOpaque(true);
		moneyTextField.setEditable(false);
		lblMoney.setLabelFor(moneyTextField);
		moneyTextField.setFocusable(false);
		GridBagConstraints gbc_moneyTextField = new GridBagConstraints();
		gbc_moneyTextField.insets = new Insets(0, 0, 5, 0);
		gbc_moneyTextField.anchor = GridBagConstraints.NORTHWEST;
		gbc_moneyTextField.gridx = 1;
		gbc_moneyTextField.gridy = 1;
		leftPanel.add(moneyTextField, gbc_moneyTextField);
		moneyTextField.setColumns(10);
		
		JLabel lblHunger = new JLabel("Hunger:");
		GridBagConstraints gbc_lblHunger = new GridBagConstraints();
		gbc_lblHunger.anchor = GridBagConstraints.WEST;
		gbc_lblHunger.insets = new Insets(0, 0, 5, 5);
		gbc_lblHunger.gridx = 0;
		gbc_lblHunger.gridy = 2;
		leftPanel.add(lblHunger, gbc_lblHunger);
		
		hungerTextField = new JTextField();
		lblHunger.setLabelFor(hungerTextField);
		hungerTextField.setEditable(false);
		hungerTextField.setOpaque(true);
		GridBagConstraints gbc_hungerTextField = new GridBagConstraints();
		gbc_hungerTextField.anchor = GridBagConstraints.NORTHWEST;
		gbc_hungerTextField.insets = new Insets(0, 0, 5, 0);
		gbc_hungerTextField.gridx = 1;
		gbc_hungerTextField.gridy = 2;
		leftPanel.add(hungerTextField, gbc_hungerTextField);
		hungerTextField.setColumns(10);
		
		JPanel rightPanel = new JPanel();
		add(rightPanel);

	}
	
	public void resetInfo() {
		
	}
	
	public void updateInfo(PersonAgent p) {
		this.nameTextField.setText(p.getName());
		this.moneyTextField.setText(String.format("$%.2f", p.getMoney()));
		this.hungerTextField.setText(p.getHungerLevel() + "");
	}

}
