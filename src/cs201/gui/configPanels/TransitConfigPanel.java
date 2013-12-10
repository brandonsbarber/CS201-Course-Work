package cs201.gui.configPanels;

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
import cs201.agents.transit.VehicleAgent;
import cs201.gui.ConfigPanel;
import cs201.interfaces.agents.transit.Vehicle;

/**
 * 
 * @author Brandon
 *
 */
@SuppressWarnings("serial")
public class TransitConfigPanel extends ConfigPanel
{
	private TransitInfoPanel infoPanel;
	private ListPanel<VehicleAgent> listPanel;
	
	/**
	 * Creates a new transit configuration panel
	 */
	public TransitConfigPanel()
	{
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 1;
		c.weighty = 1;
		
		listPanel = new ListPanel<VehicleAgent>();
		this.add(listPanel, c);
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 4;
		infoPanel = new TransitInfoPanel(this);
		this.add(infoPanel, c);
	}
	
	public void addVehicle(VehicleAgent p) {
		this.listPanel.addElement(p);
	}
	
	public void deselectVehicleList() {
		this.listPanel.deselect();
	}
	
	public void resetCity()
	{
		listPanel.reset();
	}
	
	private class ListPanel<T> extends JPanel
	{
		private JScrollPane scrollPane;
		
		private JList<T> list = new JList<T>();
		private DefaultListModel<T> model = new DefaultListModel<T>();
		
		public ListPanel()
		{
			this.setBorder(BorderFactory.createTitledBorder("SimCity201 Vehicles:"));
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			
			list.setModel(model);
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list.addListSelectionListener(new ListSelectionListener()
			{
				@Override
				public void valueChanged(ListSelectionEvent e)
				{
					if (list.getSelectedIndex() == -1)
					{
					    //No selection, reset info panel.
						infoPanel.resetInfo();
					}
				}	
			});
			
			scrollPane = new JScrollPane(list);
			this.add(scrollPane);
		}
		
		public void showInfo()
		{
			if(list.getModel().getElementAt(list.getSelectedIndex()) instanceof VehicleAgent)
			{
				infoPanel.updateInfo((VehicleAgent)list.getModel().getElementAt(list.getSelectedIndex()));
			}
		}
		
		public void addElement(T p)
		{
			int index = model.getSize();
			
			model.add(index, p);
			list.setSelectedIndex(index);
			list.ensureIndexIsVisible(index);
		}
		
		public void deselect() {
			this.list.clearSelection();
		}
		
		public void reset() {
			this.model.clear();
		}
	}
}
