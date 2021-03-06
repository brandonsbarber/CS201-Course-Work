package cs201.gui.configPanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import cs201.gui.ConfigPanel;
import cs201.roles.Role;
import cs201.roles.marketRoles.MarketManagerRole.InventoryEntry;
import cs201.structures.market.MarketStructure;

@SuppressWarnings("serial")
public class MarketConfigPanel extends ConfigPanel implements ActionListener {
	
	// The reference to the MarketStructure this config panel will be controlling
	//MarketStructure structure;

	JComboBox marketsComboBox;
	JScrollPane listScroller;
	DefaultListModel listModel;
	JList inventoryList;
	JButton addInventoryButton;
	JButton shutdownButton;
	MarketStructure currentStructure;
	
	public MarketConfigPanel() {
		super();
		
		// Add a market select combo box
		marketsComboBox = new JComboBox();
		this.add(marketsComboBox);
		marketsComboBox.addActionListener(this);
		
		// Add an inventory list box
		listModel = new DefaultListModel();
		inventoryList = new JList(listModel);
		listScroller = new JScrollPane(inventoryList);
		this.add(listScroller);
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(layout);
		
		// Inventory button
		addInventoryButton = new JButton("Add inventory");
		addInventoryButton.addActionListener(this);
		addInventoryButton.setEnabled(true);
		this.add(addInventoryButton);
		
		// Shutdown button
		shutdownButton = new JButton("Shutdown Market");
		shutdownButton.addActionListener(this);
		shutdownButton.setEnabled(true);
		this.add(shutdownButton);
		
		//loadInventoryFromStructure();
	}

	public void setPersonEnabled(Role role) {
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		// The "Add inventory" button was clicked
		if (e.getSource() == addInventoryButton) {
			processAddInventory();
		}
				
		if (e.getSource() == marketsComboBox) {
			MarketStructure selectedStructure = (MarketStructure)marketsComboBox.getSelectedItem();
			loadInventoryFromStructure(selectedStructure);
			currentStructure = selectedStructure;
		}
		
		if (e.getSource() == shutdownButton) {
			// Shut 'er down
			currentStructure.closeMarket();
		}
	}
	
	/**
	 * Call this method to update the config panel's list of inventory.
	 */
	public void updateInventoryList(MarketStructure requestingStructure) {
		if (currentStructure == requestingStructure) {
			loadInventoryFromStructure(requestingStructure);
		} 
	}
	
	/**
	 * Add a market structure to the drop-down list of market structures.
	 */
	public void addMarketStructure(MarketStructure structure) {
		marketsComboBox.addItem(structure);
		if (marketsComboBox.getModel().getSize() == 1) {
			currentStructure = structure;
		}
	}
	
	private void loadInventoryFromStructure(MarketStructure structure) {
		listModel.clear();
		
		if (structure != null) {
			List<InventoryEntry> inventoryList = structure.getInventory();
			for (InventoryEntry thisEntry : inventoryList) {
				String inventoryString = String.format(thisEntry.amount + " " + thisEntry.item + "(s) at $%.2f each", thisEntry.price);
				listModel.addElement(inventoryString);
			}
		}
	}
	
	private void processAddInventory() {
		// Create the add inventory dialog box
		JTextField itemField = new JTextField();
		JTextField quantityField = new JTextField();
		JTextField priceField = new JTextField();
		final JComponent[] inputs = new JComponent[] {
				new JLabel("Item: "),
				itemField,
				new JLabel("Quantity: "),
				quantityField,
				new JLabel("Price: "),
				priceField
		};
		
		// Show the dialog
		JOptionPane.showMessageDialog(null, inputs, "Add inventory", JOptionPane.PLAIN_MESSAGE);
		
		// Get the results
		String item = itemField.getText();
		String quantity = quantityField.getText();
		String price = priceField.getText();
		
		// Get the item type
		if (item.length() == 0) {
			JOptionPane.showMessageDialog(this, "Please enter a name for the item.", "Error", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		// Parse the amount
		int quantityValue;
		try {
			quantityValue = Integer.parseInt(quantity);
		} catch (NumberFormatException exception) {
			JOptionPane.showMessageDialog(this, "Please enter a valid integer for the quantity.", "Error", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		// Parse the price
		float priceValue;
		try {
			priceValue = Float.parseFloat(price);
		} catch (NumberFormatException exception) {
			JOptionPane.showMessageDialog(this, "Please enter a valid floating point decimal for the price.", "Error", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		// Add the new inventory item to the structure
		currentStructure.addInventory(item, quantityValue, priceValue);
		
		// Confirm the add
		String confirmation = "Successfully added " + quantityValue + " " + item + "(s) at $" + String.format("%.2f", priceValue) + " each to the market's inventory!";
		JOptionPane.showMessageDialog(this, confirmation);	
	}

	public void resetCity() {
		currentStructure = null;
		listModel.clear();
		marketsComboBox.removeAllItems();
	}

}
