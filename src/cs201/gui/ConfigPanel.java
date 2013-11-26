package cs201.gui;

import javax.swing.JPanel;

public class ConfigPanel extends JPanel
{
	public static int INSTANCES = 1;
	
	private int instance;
	
	public ConfigPanel()
	{
		instance = INSTANCES++;
	}
	
	public String toString()
	{
		return "ConfigPanel "+instance;
	}
}
