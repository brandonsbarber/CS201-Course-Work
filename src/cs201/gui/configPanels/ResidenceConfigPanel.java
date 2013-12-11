package cs201.gui.configPanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import cs201.gui.ConfigPanel;
import cs201.roles.marketRoles.MarketManagerRole.InventoryEntry;
import cs201.structures.market.MarketStructure;
import cs201.structures.residence.Residence;
import cs201.structures.residence.Residence.Food;

public class ResidenceConfigPanel extends ConfigPanel implements ActionListener {

	JComboBox residenceComboBox;
	JScrollPane listScroller;
	DefaultListModel listModel;
	JList fridgeList;
	Residence currentStructure;
	
	public ResidenceConfigPanel() {
		super();
		
		residenceComboBox = new JComboBox();
		this.add(residenceComboBox);
		residenceComboBox.addActionListener(this);
		
		listModel = new DefaultListModel();
		fridgeList = new JList(listModel);
		listScroller = new JScrollPane(fridgeList);
		JLabel label = new JLabel("Fridge Contents:");
		this.add(label);
		this.add(listScroller);
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(layout);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == residenceComboBox) {
			Residence selectedStructure = (Residence)residenceComboBox.getSelectedItem();
			getFridgeContents(selectedStructure);
			currentStructure = selectedStructure;
		}

	}
	
	public void updateFridgeContents(Residence requestingStructure) {
		if (currentStructure == requestingStructure) {
			getFridgeContents(requestingStructure);
		} 
	}
	
	private void getFridgeContents(Residence structure) {
		listModel.clear();
		if (structure != null) {
			List<Food> fridgeContents = structure.getFridgeContents();
			if (fridgeContents.isEmpty()) {
				//System.out.println("FRIDGE EMPTY");
				String foodString = new String("Fridge Empty.");
				listModel.addElement(foodString);
			} else {
				for (Food thisFood : fridgeContents) {
					String foodString = String.format(thisFood.getAmount() + " " + thisFood.getType());
					listModel.addElement(foodString);
				}
			}
		}
	}
	
	public void addResidenceStructure(Residence structure) {
		residenceComboBox.addItem(structure);
		if (residenceComboBox.getModel().getSize() == 1) {
			currentStructure = structure;
			updateFridgeContents(currentStructure);
		}
		
	}
	
	public void resetCity() {
		currentStructure = null;
		listModel.clear();
		residenceComboBox.removeAllItems();
	}

}
