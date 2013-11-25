package cs201.gui;

import javax.swing.JLabel;

public class TransitConfigPanel extends ConfigPanel
{
	public TransitConfigPanel()
	{
		removeAll();
		add(new JLabel("Hello, world!"));
		revalidate();
	}
	public String toString()
	{
		return super.toString()+" but I'm a Transit Panel";
	}
}
