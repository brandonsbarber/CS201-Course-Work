package cs201.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import cs201.helper.Constants;

public class BaseSettingsPanel extends JPanel implements ActionListener
{
	private JButton traceButton;
	private JCheckBox debugMode;
	
	public BaseSettingsPanel()
	{
		setLayout(new FlowLayout());
		traceButton = new JButton("Show Trace Panel");
		traceButton.addActionListener(this);
		add(traceButton);
		
		debugMode = new JCheckBox("Debug Mode:");
		debugMode.setSelected(Constants.DEBUG_MODE);
		debugMode.setHorizontalTextPosition(SwingConstants.LEFT);
		debugMode.addActionListener(this);
		add(debugMode);
		
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == debugMode)
		{
			Constants.DEBUG_MODE = debugMode.isSelected();
		}
	}
}
