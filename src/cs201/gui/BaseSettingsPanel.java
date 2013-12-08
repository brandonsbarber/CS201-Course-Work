package cs201.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import cs201.helper.Constants;
import cs201.trace.TraceFrame;

@SuppressWarnings("serial")
public class BaseSettingsPanel extends JPanel implements ActionListener
{	
	private JButton traceButton;
	private JButton scenarioButton;
	private JCheckBox debugMode;
	private TraceFrame traceFrame;
	private ScenarioPanel scenarioPanel;
	
	public BaseSettingsPanel()
	{
		setLayout(new FlowLayout());
		
		traceFrame = new TraceFrame();
		
		traceButton = new JButton("Show Trace Panel");
		traceButton.addActionListener(this);
		add(traceButton);
		
		scenarioButton = new JButton("Show Scenario Panel");
		scenarioButton.addActionListener(this);
		add(scenarioButton);
		
		debugMode = new JCheckBox("Debug Mode:");
		debugMode.setSelected(Constants.DEBUG_MODE);
		debugMode.setHorizontalTextPosition(SwingConstants.LEFT);
		debugMode.addActionListener(this);
		add(debugMode);
		
	}
	
	public void setScenarioPanel(ScenarioPanel panel) {
		scenarioPanel = panel;
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
		if (e.getSource() == scenarioButton) {
			if (scenarioPanel != null) {
				scenarioPanel.showScenarioPanel();
			}
		}
	}
}
