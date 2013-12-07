package cs201.gui;

import javax.swing.JPanel;

/**
 * 
 * @author Brandon
 *
 */
@SuppressWarnings("serial")
public class ConfigPanel extends JPanel
{
	public static int INSTANCES = 1;
	
	private int instance;
	
	/**
	 * Keeps track of instance counting
	 */
	public ConfigPanel()
	{
		instance = INSTANCES++;
	}
	
	/**
	 * Returns a String of the Config Panel
	 * @param string of the Config Panel
	 */
	public String toString()
	{
		return "ConfigPanel "+instance;
	}
}
