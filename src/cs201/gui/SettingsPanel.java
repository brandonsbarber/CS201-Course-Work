package cs201.gui;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class SettingsPanel extends JPanel
{
	private JTabbedPane tabs;
	
	public SettingsPanel()
	{
		tabs = new JTabbedPane();
	}
	
	public void addPanel (String tabTitle)
	{
		tabs.add(tabTitle, new JPanel());
	}
}
