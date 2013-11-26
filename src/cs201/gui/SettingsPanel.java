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
		
		JPanel topPanels = new JPanel();
		topPanels.setLayout(new GridLayout(2,1,0,0));
		
		categories = new JComboBox();
		categories.addActionListener(this);
		specifics = new JComboBox();
		specifics.addActionListener(this);

		setLayout(new BorderLayout());
		topPanels.add(categories);
		topPanels.add(specifics);
		
		add(topPanels,BorderLayout.NORTH);
		
		add(contentsScrollPane);
	}
	
	public void addPanel (String tabTitle,ConfigPanel panel)
	{
		if(!panelMap.containsKey(tabTitle))
		{
			panelMap.put(tabTitle, new ArrayList<ConfigPanel>());
			categories.addItem(tabTitle);
		}
		panelMap.get(tabTitle).add(panel);
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
		else if(e.getSource() == specifics)
		{
			contentsScrollPane.setViewportView((ConfigPanel)specifics.getSelectedItem());
		}
		revalidate();
		repaint();
	}
	
	
}
