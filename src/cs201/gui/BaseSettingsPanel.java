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
import cs201.trace.TracePanel;

public class BaseSettingsPanel extends JPanel implements ActionListener
{
	private final int TRACEX = 800;
	private final int TRACEY = 300;
	
	private JButton traceButton;
	private JCheckBox debugMode;
	private JFrame traceFrame;
	private TracePanel tracePanel;
	
	public BaseSettingsPanel()
	{
		setLayout(new FlowLayout());
		
		tracePanel = new TracePanel();
		tracePanel.setPreferredSize(new Dimension(TRACEX, TRACEY));
		traceFrame = new JFrame();
		traceFrame.setResizable(false);
		traceFrame.add(tracePanel);
		traceFrame.pack();
		traceFrame.setVisible(false);
		
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
