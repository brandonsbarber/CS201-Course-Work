package cs201.gui.configPanels;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import cs201.gui.ConfigPanel;
import cs201.roles.restaurantRoles.RestaurantWaiterRole;
import cs201.structures.restaurant.Restaurant;

@SuppressWarnings("serial")
public class RestaurantConfigPanel extends ConfigPanel implements ActionListener {
	private JComboBox<Restaurant> comboBoxRestaurants;
	private JButton btnCloseRestaurant;
	private JButton btnEmptyAllInventory;
	private JButton btnEmptySelectedInventory;
	private JLabel lblStatus;
	private JPanel staffInfopanel;
	private JPanel cookInventoryPanel;
	private JPanel panel_2;
	private JLabel lblStaff;
	private JLabel lblHost;
	private JCheckBox chckbxHost;
	private JLabel lblCook;
	private JCheckBox checkBoxCook;
	private JLabel lblCashier;
	private JCheckBox checkBoxCashier;
	private JLabel lblWaiters;
	private JLabel labelNumWaiters;
	private JLabel lblMoney;
	private JLabel lblCookInventory;
	private JScrollPane scrollPane;
	private JList<String> listInventory;
	private DefaultListModel<String> modelInventory;

	public RestaurantConfigPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{220, 10, 0};
		gridBagLayout.rowHeights = new int[]{10, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 9.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JPanel selectionPanel = new JPanel();
		selectionPanel.setBorder(new TitledBorder(null, "Select a Restaurant:", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, null));
		GridBagConstraints gbc_selectionPanel = new GridBagConstraints();
		gbc_selectionPanel.insets = new Insets(0, 0, 0, 5);
		gbc_selectionPanel.fill = GridBagConstraints.BOTH;
		gbc_selectionPanel.gridx = 0;
		gbc_selectionPanel.gridy = 0;
		add(selectionPanel, gbc_selectionPanel);
		GridBagLayout gbl_selectionPanel = new GridBagLayout();
		gbl_selectionPanel.columnWidths = new int[]{231, 0};
		gbl_selectionPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_selectionPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_selectionPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		selectionPanel.setLayout(gbl_selectionPanel);
		
		comboBoxRestaurants = new JComboBox<Restaurant>();
		comboBoxRestaurants.setPreferredSize(new Dimension(0, 28));
		comboBoxRestaurants.setMaximumSize(new Dimension(0, 28));
		comboBoxRestaurants.setMinimumSize(new Dimension(0, 28));
		comboBoxRestaurants.setMaximumRowCount(10);
		comboBoxRestaurants.setAlignmentY(Component.TOP_ALIGNMENT);
		comboBoxRestaurants.setToolTipText("Restaurants");
		comboBoxRestaurants.setFont(new Font("SansSerif", Font.PLAIN, 11));
		GridBagConstraints gbc_comboBoxRestaurants = new GridBagConstraints();
		gbc_comboBoxRestaurants.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxRestaurants.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxRestaurants.gridx = 0;
		gbc_comboBoxRestaurants.gridy = 0;
		selectionPanel.add(comboBoxRestaurants, gbc_comboBoxRestaurants);
		comboBoxRestaurants.addActionListener(this);
		
		lblStatus = new JLabel("Status: Closed");
		lblStatus.setMaximumSize(new Dimension(81, 28));
		lblStatus.setMinimumSize(new Dimension(81, 28));
		lblStatus.setPreferredSize(new Dimension(81, 28));
		GridBagConstraints gbc_lblStatus = new GridBagConstraints();
		gbc_lblStatus.insets = new Insets(0, 0, 5, 0);
		gbc_lblStatus.gridx = 0;
		gbc_lblStatus.gridy = 1;
		selectionPanel.add(lblStatus, gbc_lblStatus);
		
		lblMoney = new JLabel("Money: $0.00");
		lblMoney.setHorizontalAlignment(SwingConstants.CENTER);
		lblMoney.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblMoney.setFont(new Font("SansSerif", Font.PLAIN, 11));
		lblMoney.setMaximumSize(new Dimension(100, 28));
		lblMoney.setMinimumSize(new Dimension(100, 28));
		lblMoney.setPreferredSize(new Dimension(100, 28));
		GridBagConstraints gbc_lblMoney = new GridBagConstraints();
		gbc_lblMoney.insets = new Insets(0, 0, 5, 0);
		gbc_lblMoney.gridx = 0;
		gbc_lblMoney.gridy = 2;
		selectionPanel.add(lblMoney, gbc_lblMoney);
		
		btnCloseRestaurant = new JButton("Close Restaurant");
		btnCloseRestaurant.setMaximumSize(new Dimension(172, 28));
		btnCloseRestaurant.setPreferredSize(new Dimension(172, 28));
		btnCloseRestaurant.setMinimumSize(new Dimension(100, 28));
		GridBagConstraints gbc_btnCloseRestaurant = new GridBagConstraints();
		gbc_btnCloseRestaurant.insets = new Insets(0, 0, 5, 0);
		gbc_btnCloseRestaurant.gridx = 0;
		gbc_btnCloseRestaurant.gridy = 3;
		selectionPanel.add(btnCloseRestaurant, gbc_btnCloseRestaurant);
		btnCloseRestaurant.addActionListener(this);
		
		btnEmptyAllInventory = new JButton("Empty All Inventory");
		btnEmptyAllInventory.setMaximumSize(new Dimension(172, 28));
		btnEmptyAllInventory.setPreferredSize(new Dimension(172, 28));
		btnEmptyAllInventory.setMinimumSize(new Dimension(100, 28));
		GridBagConstraints gbc_btnEmptyAllInventory = new GridBagConstraints();
		gbc_btnEmptyAllInventory.insets = new Insets(0, 0, 5, 0);
		gbc_btnEmptyAllInventory.gridx = 0;
		gbc_btnEmptyAllInventory.gridy = 4;
		selectionPanel.add(btnEmptyAllInventory, gbc_btnEmptyAllInventory);
		btnEmptyAllInventory.addActionListener(this);
		
		btnEmptySelectedInventory = new JButton("Empty Selected Inventory");
		btnEmptySelectedInventory.setMaximumSize(new Dimension(172, 28));
		btnEmptySelectedInventory.setPreferredSize(new Dimension(172, 28));
		btnEmptySelectedInventory.setMinimumSize(new Dimension(100, 28));
		GridBagConstraints gbc_btnEmptySelectedInventory = new GridBagConstraints();
		gbc_btnEmptySelectedInventory.gridx = 0;
		gbc_btnEmptySelectedInventory.gridy = 5;
		selectionPanel.add(btnEmptySelectedInventory, gbc_btnEmptySelectedInventory);
		
		JPanel infoPanel = new JPanel();
		infoPanel.setBorder(new TitledBorder(null, "Info:", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, null));
		GridBagConstraints gbc_infoPanel = new GridBagConstraints();
		gbc_infoPanel.fill = GridBagConstraints.BOTH;
		gbc_infoPanel.gridx = 1;
		gbc_infoPanel.gridy = 0;
		add(infoPanel, gbc_infoPanel);
		infoPanel.setLayout(new GridLayout(0, 3, 0, 0));
		
		staffInfopanel = new JPanel();
		infoPanel.add(staffInfopanel);
		GridBagLayout gbl_staffInfopanel = new GridBagLayout();
		gbl_staffInfopanel.columnWidths = new int[]{0, 0, 0};
		gbl_staffInfopanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_staffInfopanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_staffInfopanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		staffInfopanel.setLayout(gbl_staffInfopanel);
		
		lblStaff = new JLabel("Staff Present:");
		lblStaff.setPreferredSize(new Dimension(76, 28));
		lblStaff.setMinimumSize(new Dimension(76, 28));
		lblStaff.setMaximumSize(new Dimension(76, 28));
		lblStaff.setFont(new Font("SansSerif", Font.BOLD, 11));
		GridBagConstraints gbc_lblStaff = new GridBagConstraints();
		gbc_lblStaff.insets = new Insets(0, 0, 5, 5);
		gbc_lblStaff.anchor = GridBagConstraints.EAST;
		gbc_lblStaff.gridx = 0;
		gbc_lblStaff.gridy = 0;
		staffInfopanel.add(lblStaff, gbc_lblStaff);
		
		lblHost = new JLabel("Host:");
		lblHost.setMaximumSize(new Dimension(30, 28));
		lblHost.setMinimumSize(new Dimension(30, 28));
		lblHost.setPreferredSize(new Dimension(30, 28));
		lblHost.setFont(new Font("SansSerif", Font.PLAIN, 11));
		GridBagConstraints gbc_lblHost = new GridBagConstraints();
		gbc_lblHost.insets = new Insets(0, 0, 5, 5);
		gbc_lblHost.anchor = GridBagConstraints.EAST;
		gbc_lblHost.gridx = 0;
		gbc_lblHost.gridy = 1;
		staffInfopanel.add(lblHost, gbc_lblHost);
		
		chckbxHost = new JCheckBox("");
		chckbxHost.setEnabled(false);
		chckbxHost.setFocusable(false);
		chckbxHost.setMinimumSize(new Dimension(18, 28));
		chckbxHost.setMaximumSize(new Dimension(18, 28));
		chckbxHost.setFont(new Font("SansSerif", Font.PLAIN, 11));
		chckbxHost.setPreferredSize(new Dimension(18, 28));
		GridBagConstraints gbc_chckbxHost = new GridBagConstraints();
		gbc_chckbxHost.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxHost.anchor = GridBagConstraints.WEST;
		gbc_chckbxHost.gridx = 1;
		gbc_chckbxHost.gridy = 1;
		staffInfopanel.add(chckbxHost, gbc_chckbxHost);
		
		lblCook = new JLabel("Cook:");
		lblCook.setMinimumSize(new Dimension(33, 28));
		lblCook.setMaximumSize(new Dimension(33, 28));
		lblCook.setPreferredSize(new Dimension(33, 28));
		lblCook.setFont(new Font("SansSerif", Font.PLAIN, 11));
		GridBagConstraints gbc_lblCook = new GridBagConstraints();
		gbc_lblCook.anchor = GridBagConstraints.EAST;
		gbc_lblCook.insets = new Insets(0, 0, 5, 5);
		gbc_lblCook.gridx = 0;
		gbc_lblCook.gridy = 2;
		staffInfopanel.add(lblCook, gbc_lblCook);
		
		checkBoxCook = new JCheckBox("");
		checkBoxCook.setMaximumSize(new Dimension(18, 28));
		checkBoxCook.setMinimumSize(new Dimension(18, 28));
		checkBoxCook.setPreferredSize(new Dimension(18, 28));
		checkBoxCook.setFont(new Font("SansSerif", Font.PLAIN, 11));
		checkBoxCook.setEnabled(false);
		checkBoxCook.setFocusable(false);
		GridBagConstraints gbc_checkBoxCook = new GridBagConstraints();
		gbc_checkBoxCook.insets = new Insets(0, 0, 5, 0);
		gbc_checkBoxCook.anchor = GridBagConstraints.WEST;
		gbc_checkBoxCook.gridx = 1;
		gbc_checkBoxCook.gridy = 2;
		staffInfopanel.add(checkBoxCook, gbc_checkBoxCook);
		
		lblCashier = new JLabel("Cashier:");
		lblCashier.setFont(new Font("SansSerif", Font.PLAIN, 11));
		lblCashier.setMaximumSize(new Dimension(47, 28));
		lblCashier.setMinimumSize(new Dimension(47, 28));
		lblCashier.setPreferredSize(new Dimension(47, 28));
		GridBagConstraints gbc_lblCashier = new GridBagConstraints();
		gbc_lblCashier.anchor = GridBagConstraints.EAST;
		gbc_lblCashier.insets = new Insets(0, 0, 5, 5);
		gbc_lblCashier.gridx = 0;
		gbc_lblCashier.gridy = 3;
		staffInfopanel.add(lblCashier, gbc_lblCashier);
		
		checkBoxCashier = new JCheckBox("");
		checkBoxCashier.setFont(new Font("SansSerif", Font.PLAIN, 11));
		checkBoxCashier.setEnabled(false);
		checkBoxCashier.setFocusable(false);
		checkBoxCashier.setMinimumSize(new Dimension(18, 28));
		checkBoxCashier.setMaximumSize(new Dimension(18, 28));
		checkBoxCashier.setPreferredSize(new Dimension(18, 28));
		GridBagConstraints gbc_checkBoxCashier = new GridBagConstraints();
		gbc_checkBoxCashier.insets = new Insets(0, 0, 5, 0);
		gbc_checkBoxCashier.anchor = GridBagConstraints.WEST;
		gbc_checkBoxCashier.gridx = 1;
		gbc_checkBoxCashier.gridy = 3;
		staffInfopanel.add(checkBoxCashier, gbc_checkBoxCashier);
		
		lblWaiters = new JLabel("Waiters:");
		lblWaiters.setPreferredSize(new Dimension(46, 28));
		lblWaiters.setMaximumSize(new Dimension(46, 28));
		lblWaiters.setMinimumSize(new Dimension(46, 28));
		lblWaiters.setFont(new Font("SansSerif", Font.PLAIN, 11));
		GridBagConstraints gbc_lblWaiters = new GridBagConstraints();
		gbc_lblWaiters.anchor = GridBagConstraints.EAST;
		gbc_lblWaiters.insets = new Insets(0, 0, 0, 5);
		gbc_lblWaiters.gridx = 0;
		gbc_lblWaiters.gridy = 4;
		staffInfopanel.add(lblWaiters, gbc_lblWaiters);
		
		labelNumWaiters = new JLabel("0");
		labelNumWaiters.setFont(new Font("SansSerif", Font.PLAIN, 11));
		labelNumWaiters.setFocusable(false);
		labelNumWaiters.setMinimumSize(new Dimension(8, 28));
		labelNumWaiters.setMaximumSize(new Dimension(8, 28));
		labelNumWaiters.setPreferredSize(new Dimension(8, 28));
		GridBagConstraints gbc_labelNumWaiters = new GridBagConstraints();
		gbc_labelNumWaiters.insets = new Insets(0, 6, 0, 0);
		gbc_labelNumWaiters.anchor = GridBagConstraints.WEST;
		gbc_labelNumWaiters.gridx = 1;
		gbc_labelNumWaiters.gridy = 4;
		staffInfopanel.add(labelNumWaiters, gbc_labelNumWaiters);
		
		cookInventoryPanel = new JPanel();
		infoPanel.add(cookInventoryPanel);
		GridBagLayout gbl_cookInventoryPanel = new GridBagLayout();
		gbl_cookInventoryPanel.columnWidths = new int[]{0, 0};
		gbl_cookInventoryPanel.rowHeights = new int[]{0, 0, 0};
		gbl_cookInventoryPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_cookInventoryPanel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		cookInventoryPanel.setLayout(gbl_cookInventoryPanel);
		
		lblCookInventory = new JLabel("Cook Inventory:");
		lblCookInventory.setFont(new Font("SansSerif", Font.BOLD, 11));
		lblCookInventory.setMaximumSize(new Dimension(100, 28));
		lblCookInventory.setMinimumSize(new Dimension(100, 28));
		lblCookInventory.setPreferredSize(new Dimension(100, 28));
		GridBagConstraints gbc_lblCookInventory = new GridBagConstraints();
		gbc_lblCookInventory.insets = new Insets(0, 0, 5, 0);
		gbc_lblCookInventory.anchor = GridBagConstraints.WEST;
		gbc_lblCookInventory.gridx = 0;
		gbc_lblCookInventory.gridy = 0;
		cookInventoryPanel.add(lblCookInventory, gbc_lblCookInventory);
		
		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		cookInventoryPanel.add(scrollPane, gbc_scrollPane);
		
		listInventory = new JList<String>();
		listInventory.setFont(new Font("SansSerif", Font.PLAIN, 11));
		modelInventory = new DefaultListModel<String>();
		listInventory.setModel(modelInventory);
		listInventory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(listInventory);
		
		panel_2 = new JPanel();
		infoPanel.add(panel_2);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.comboBoxRestaurants) {
			if (this.comboBoxRestaurants.getSelectedIndex() != -1) {
				Restaurant r = (Restaurant) this.comboBoxRestaurants.getSelectedItem();
				updateInfo(r);
			}
		} else if (e.getSource() == this.btnCloseRestaurant) {
			if (this.comboBoxRestaurants.getSelectedIndex() != -1) {
				Restaurant r = (Restaurant) this.comboBoxRestaurants.getSelectedItem();
				if (r.getOpen()) {
					r.closeRestaurant();
					updateInfo(r);
				}
			}
		} else if (e.getSource() == this.btnEmptyAllInventory) {
			if (this.comboBoxRestaurants.getSelectedIndex() != -1) {
				Restaurant r = (Restaurant) this.comboBoxRestaurants.getSelectedItem();
				r.emptyEntireCookInventory();
				updateInfo(r);
			}
		}
	}
	
	public void updateInfo(Restaurant r) {
		if (this.comboBoxRestaurants.getSelectedItem() != r) {
			return;
		}
		
		this.lblStatus.setText(r.getOpen() ? "Status: Open" : "Status: Closed");
		this.chckbxHost.setSelected(r.getHost().getPerson() != null);
		this.checkBoxCook.setSelected(r.getCook().getPerson() != null);
		this.checkBoxCashier.setSelected(r.getCashier().getPerson() != null);
		int numWaiters = 0;
		for (RestaurantWaiterRole w :r.getWaiters()) {
			if (w.getPerson() != null) {
				numWaiters++;
			}
		}
		this.labelNumWaiters.setText(numWaiters + "");
		this.lblMoney.setText("Money: " + String.format("$%.2f", r.getCurrentRestaurantMoney()));
		this.modelInventory.removeAllElements();
		for (String s : r.getCookInventory()) {
			this.modelInventory.addElement(s);
		}
	}
	
	private void resetInfo() {
		this.lblStatus.setText("Status: Closed");
		this.chckbxHost.setSelected(false);
		this.checkBoxCook.setSelected(false);
		this.checkBoxCashier.setSelected(false);
		this.labelNumWaiters.setToolTipText("0");
		this.lblMoney.setText("Money: $0.00");
		this.modelInventory.removeAllElements();
	}

	public void resetCity() {
		this.comboBoxRestaurants.removeAllItems();
		this.resetInfo();
	}
	
	public void addRestaurant(Restaurant newRestaurant) {
		this.comboBoxRestaurants.addItem(newRestaurant);
	}

}
