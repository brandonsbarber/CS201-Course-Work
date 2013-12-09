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
	private JButton timeButton;
	private JCheckBox debugMode;
	private TraceFrame traceFrame;
	private ScenarioPanel scenarioPanel;
	private TimePanel timePanel;
	
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
		
		timeButton = new JButton("Show Time Panel");
		timeButton.addActionListener(this);
		add(timeButton);
		
		debugMode = new JCheckBox("Debug Mode:");
		debugMode.setSelected(Constants.DEBUG_MODE);
		debugMode.setHorizontalTextPosition(SwingConstants.LEFT);
		debugMode.addActionListener(this);
		add(debugMode);
		
	}
	
	public void setScenarioPanel(ScenarioPanel panel) {
		scenarioPanel = panel;
	}
	
	public void setTimePanel(TimePanel panel) {
		timePanel = panel;
	}
	
	/**
	 * Used to reset any settings/the trace panel
	 */
	public void resetCity() {
		this.traceFrame.resetCity();
		this.debugMode.setSelected(false);
		Constants.DEBUG_MODE = false;
		this.timePanel.resetCity();
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
		if (e.getSource() == timeButton) {
			if (timePanel != null) {
				timePanel.showTimePanel();
			}
		}
	}
}
