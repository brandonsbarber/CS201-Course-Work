package cs201.gui.configPanels;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cs201.agents.PersonAgent;
import cs201.gui.ConfigPanel;

@SuppressWarnings("serial")
public class PersonConfigPanel extends ConfigPanel {
	RightPanel right;
	PersonInfoPanel left;
	
	public PersonConfigPanel() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 1;
		c.weighty = 1;
		
		right = new RightPanel();
		this.add(right, c);
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.weightx = 4;
		left = new PersonInfoPanel();
		this.add(left, c);
	}
	
	public void addPerson(PersonAgent p) {
		this.right.addPerson(p);
	}
	
	
	private class RightPanel extends JPanel {
		private JScrollPane scrollPane;
		
		private JList<PersonAgent> personList = new JList<PersonAgent>();
		private DefaultListModel<PersonAgent> model = new DefaultListModel<PersonAgent>();
		
		public RightPanel() {
			this.setBorder(BorderFactory.createTitledBorder("SimCity201 People:"));
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			
			personList.setModel(model);
			personList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			personList.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					// TODO Auto-generated method stub
					if (personList.getSelectedIndex() == -1) {
					    //No selection, reset info panel.
						left.resetInfo();
					} else {
					    //Selection, update info panel.
					    left.updateInfo(personList.getModel().getElementAt(personList.getSelectedIndex()));
					}
				}	
			});
			
			scrollPane = new JScrollPane(personList);
			this.add(scrollPane);
		}
		
		public void addPerson(PersonAgent p) {
			int index = model.getSize();
			
			model.add(index, p);
			personList.setSelectedIndex(index);
			personList.ensureIndexIsVisible(index);
		}
	}
}
