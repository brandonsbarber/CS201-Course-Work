package cs201.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class BaseSettingsPanel extends JPanel implements ActionListener
{
	private JButton traceButton;
	private JCheckBox debugMode;
	
	public BaseSettingsPanel()
	{
		setLayout(new FlowLayout());
		traceButton = new JButton("Show Trace Panel");
		add(traceButton);
		
		debugMode = new JCheckBox("Debug Mode:");
		debugMode.setHorizontalTextPosition(SwingConstants.LEFT);
		add(debugMode);
		
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		
	}
}
