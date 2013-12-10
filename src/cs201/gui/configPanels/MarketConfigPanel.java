package cs201.gui.configPanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
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

public class MarketConfigPanel extends ConfigPanel implements ActionListener {
	
	// The reference to the MarketStructure this config panel will be controlling
	MarketStructure structure;

	JScrollPane listScroller;
	DefaultListModel listModel;
	JList inventoryList;
	JButton addInventoryButton;
	
	public MarketConfigPanel() {
		super();
		
		// Add an inventory list box
		listModel = new DefaultListModel();
		inventoryList = new JList(listModel);
		listScroller = new JScrollPane(inventoryList);
		this.add(listScroller);
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(layout);
		
		// GUI Elements
		addInventoryButton = new JButton("Add inventory");
		addInventoryButton.addActionListener(this);
		addInventoryButton.setEnabled(true);
		this.add(addInventoryButton);
		
		loadInventoryFromStructure();
	}
	
	public void setStructure(MarketStructure s) {
		// Hook up this config panel to a certain structure
		structure = s;
	}

	public void setPersonEnabled(Role role) {
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		// The "Add inventory" button was clicked
		if (e.getSource() == addInventoryButton) {
			processAddInventory();
		}
	}
	
	/**
	 * Call this method to update the config panel's list of inventory.
	 */
	public void updateInventoryList() {
		loadInventoryFromStructure();
	}
	
	private void loadInventoryFromStructure() {
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
		structure.addInventory(item, quantityValue, priceValue);
		
		// Confirm the add
		String confirmation = "Successfully added " + quantityValue + " " + item + "(s) at $" + String.format("%.2f", priceValue) + " each to the market's inventory!";
		JOptionPane.showMessageDialog(this, confirmation);	
	}

}
