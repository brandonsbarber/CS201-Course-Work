package cs201.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class SettingsPanel extends JPanel implements ActionListener
{
	private JComboBox categories;
	
	private JComboBox specifics;
	
	private Map<String,ArrayList<ConfigPanel>> panelMap;
	
	private JScrollPane contentsScrollPane;
	
	public SettingsPanel()
	{
		panelMap = new HashMap<String,ArrayList<ConfigPanel>>();
		contentsScrollPane = new JScrollPane();
		
		
		categories = new JComboBox();
		categories.addActionListener(this);
		specifics = new JComboBox();

		setLayout(new FlowLayout());
		add(categories);
		add(specifics);
	}
	
	public void addPanel (String tabTitle)
	{
		if(!panelMap.containsKey(tabTitle))
		{
			panelMap.put(tabTitle, new ArrayList<ConfigPanel>());
			categories.addItem(tabTitle);
		}
		ConfigPanel newPanel = new ConfigPanel();
		panelMap.get(tabTitle).add(newPanel);
	}

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
		revalidate();
		repaint();
	}
	
	
}
