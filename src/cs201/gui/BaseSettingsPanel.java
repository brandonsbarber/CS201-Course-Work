package cs201.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import cs201.helper.Constants;
import cs201.trace.AlertLog;
import cs201.trace.TraceFrame;
import cs201.trace.TracePanel;

public class BaseSettingsPanel extends JPanel implements ActionListener
{	
	private JButton traceButton;
	private JCheckBox debugMode;
	private TraceFrame traceFrame;
	
	public BaseSettingsPanel()
	{
		setLayout(new FlowLayout());
		
		traceFrame = new TraceFrame();
		
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
		if (e.getSource() == traceButton) {
			traceFrame.setVisible(!traceFrame.isVisible());
		}
	}
}
