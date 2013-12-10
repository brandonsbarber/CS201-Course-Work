package cs201.gui.configPanels;

import javax.swing.JLabel;

import cs201.gui.ConfigPanel;

/**
 * 
 * @author Brandon
 *
 */
@SuppressWarnings("serial")
public class TransitConfigPanel extends ConfigPanel
{
	/**
	 * Creates a new transit configuration panel
	 */
	public TransitConfigPanel()
	{
		removeAll();
		add(new JLabel(this.toString()));
		revalidate();
	}
	
	/**
	 * Returns a String of the panel
	 * @return String of the panel
	 */
	public String toString()
	{
		return super.toString()+" but I'm a Transit Panel";
	}
}
