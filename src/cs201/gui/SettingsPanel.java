package cs201.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * 
 * @author Brandon
 *
 */
@SuppressWarnings("serial")
public class SettingsPanel extends JPanel implements ActionListener
{
	private JComboBox<String> categories;
	
	private JComboBox<ConfigPanel> specifics;
	
	private Map<String,ArrayList<ConfigPanel>> panelMap;
	
	private JScrollPane contentsScrollPane;
	
	/**
	 * Creates a SettingsPanel for holding ConfigPanel subclasses
	 */
	public SettingsPanel()
	{
		panelMap = new HashMap<String,ArrayList<ConfigPanel>>();
		contentsScrollPane = new JScrollPane();
		
		JPanel topPanels = new JPanel();
		topPanels.setLayout(new GridLayout(2,1,0,0));
		
		categories = new JComboBox<String>();
		categories.addActionListener(this);
		specifics = new JComboBox<ConfigPanel>();
		specifics.addActionListener(this);

		setLayout(new BorderLayout());
		topPanels.add(categories);
		topPanels.add(specifics);
		
		add(topPanels,BorderLayout.NORTH);
		
		add(contentsScrollPane);
	}
	
	/**
	 * Adds a panel with the given name type. If the given name already exists, it is placed with the others. Otherwise, it creates a new tab.
	 * @param tabTitle the "category" title
	 * @param panel the panel to be shown
	 */
	public void addPanel (String tabTitle,ConfigPanel panel)
	{
		if(!panelMap.containsKey(tabTitle))
		{
			panelMap.put(tabTitle, new ArrayList<ConfigPanel>());
			categories.addItem(tabTitle);
		}
		panelMap.get(tabTitle).add(panel);
	}

	/**
	 * Processes button presses
	 * @param e event
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		
		if(e.getSource() == categories)
		{
			specifics.removeAllItems();
			for(ConfigPanel panel : panelMap.get((String)categories.getSelectedItem()))
			{
				specifics.addItem(panel);
			}
		}
		else if(e.getSource() == specifics)
		{
			contentsScrollPane.setViewportView((ConfigPanel)specifics.getSelectedItem());
		}
		revalidate();
		repaint();
	}
	
	
}
