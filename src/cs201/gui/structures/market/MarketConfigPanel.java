package cs201.gui.structures.market;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cs201.roles.Role;
import cs201.structures.market.MarketStructure;

public class MarketConfigPanel extends JPanel implements ActionListener {
	
	// The reference to the MarketStructure this config panel will be controlling
	MarketStructure structure;
	
	JButton addInventoryButton;
	
	public MarketConfigPanel(MarketStructure s) {
		// Hook up this config panel to a certain structure
		structure = s;
		
		// GUI Elements
		addInventoryButton = new JButton("Add inventory");
		addInventoryButton.addActionListener(this);
		addInventoryButton.setEnabled(true);
		this.add(addInventoryButton);
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
	
	private void processAddInventory() {
		// Create the add inventory dialog box
		JTextField itemField = new JTextField();
		JTextField amountField = new JTextField();
		JTextField priceField = new JTextField();
		final JComponent[] inputs = new JComponent[] {
				new JLabel("Item: "),
				itemField,
				new JLabel("Quantity: "),
				amountField,
				new JLabel("Price: "),
				priceField
		};
		
		// Show the dialog
		JOptionPane.showMessageDialog(null, inputs, "Add inventory", JOptionPane.PLAIN_MESSAGE);
		
		// Get the results
		String item = itemField.getText();
		String amount = amountField.getText();
		String price = priceField.getText();
		
		// Get the item type
		if (item.length() == 0) {
			JOptionPane.showMessageDialog(this, "Please enter a name for the item.", "Error", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		// Parse the amount
		int amountValue;
		try {
			amountValue = Integer.parseInt(amount);
		} catch (NumberFormatException exception) {
			JOptionPane.showMessageDialog(this, "Please enter a valid integer for the amount.", "Error", JOptionPane.WARNING_MESSAGE);
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
		structure.addInventory(item, amountValue, priceValue);
		
		// Confirm the add
		String confirmation = "Successfully added " + amountValue + " " + item + "(s) at $" + String.format("%.2f", amountValue) + " each to the market's inventory!";
		JOptionPane.showMessageDialog(this, confirmation);
	}

}
