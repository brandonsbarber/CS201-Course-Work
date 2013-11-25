package cs201.gui.structures.market;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import cs201.gui.SimCity201;
import cs201.gui.StructurePanel;
import cs201.roles.Role;

public class MarketGui extends StructurePanel implements ActionListener {
	
	JButton addInventoryButton;
	

	public MarketGui(Rectangle2D r, int i, SimCity201 sc) {
		super(r, i, sc);
		
		addInventoryButton = new JButton("Add inventory");
		addInventoryButton.addActionListener(this);
		addInventoryButton.setEnabled(true);
		this.add(addInventoryButton);
		
	}

	public void setPersonEnabled(Role role) {
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addInventoryButton) {
			processAddInventory();
		}
	}
	
	private void processAddInventory() {
		JTextField item = new JTextField();
		JTextField amount = new JTextField();
		JTextField price = new JTextField();
		
		final JComponent[] inputs = new JComponent[] {
				new JLabel("Item: "),
				item,
				new JLabel("Quantity: "),
				amount,
				new JLabel("Price: "),
				price
		};
		
		JOptionPane.showMessageDialog(null, inputs, "Add inventory", JOptionPane.PLAIN_MESSAGE);
		
		// TODO message the structure to add the inventory item
		
	}

}
