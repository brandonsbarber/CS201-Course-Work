package cs201.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * 
 * @author Brandon
 *
 */
@SuppressWarnings("serial")
public class SettingsPanel extends JPanel implements ActionListener
{
	//private JComboBox<String> categories;
	
	private JTabbedPane categories;
	
	private Map<String,ArrayList<ConfigPanel>> panelMap;
	
	/**
	 * Creates a SettingsPanel for holding ConfigPanel subclasses
	 */
	public SettingsPanel()
	{
		setLayout(new BorderLayout());
		panelMap = new HashMap<String,ArrayList<ConfigPanel>>();
		
		categories = new JTabbedPane();
		
		add(categories);
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
			categories.add(tabTitle,panel);
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
		
	}
	
	
}
