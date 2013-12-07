package cs201.trace;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

@SuppressWarnings("serial")
public class TraceFrame extends JFrame {
	private final int TRACEX = 850;
	private final int TRACEY = 350;
	
	private TracePanel tracePanel;
	private JPanel buttonPanel;
	private JToggleButton messagesButton;
	private JToggleButton infoButton;
	private JToggleButton warningButton;
	private JToggleButton errorButton;
	private JToggleButton debugButton;
	
	public TraceFrame() {
		this.setTitle("Trace Panel");
		this.setPreferredSize(new Dimension(TRACEX, TRACEY));
		this.setResizable(false);
		this.setLocationByPlatform(true);
		this.setVisible(false);
		
		this.setLayout(new BorderLayout());
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.setPreferredSize(new Dimension(TRACEX * 1 / 3, TRACEY));
		buttonPanel.setMaximumSize(new Dimension(TRACEX * 1 / 3, TRACEY));
		buttonPanel.setMinimumSize(new Dimension(TRACEX * 1 / 3, TRACEY));
		this.add(BorderLayout.WEST, buttonPanel);
		
		JLabel levelLabel = new JLabel("Alert Levels:");
		buttonPanel.add(levelLabel);
		
		infoButton = new JToggleButton("INFO");
		infoButton.setSelected(true);
		buttonPanel.add(infoButton);
		infoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (infoButton.isSelected()) {
					tracePanel.showAlertsWithLevel(AlertLevel.INFO);
				} else {
					tracePanel.hideAlertsWithLevel(AlertLevel.INFO);
				}
			}
		});
		
		messagesButton = new JToggleButton("MESSAGES");
		messagesButton.setSelected(true);
		buttonPanel.add(messagesButton);
		messagesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (messagesButton.isSelected()) {
					tracePanel.showAlertsWithLevel(AlertLevel.MESSAGE);
				} else {
					tracePanel.hideAlertsWithLevel(AlertLevel.MESSAGE);
				}
			}
		});
		
		warningButton = new JToggleButton("WARNINGS");
		warningButton.setSelected(true);
		buttonPanel.add(warningButton);
		warningButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (warningButton.isSelected()) {
					tracePanel.showAlertsWithLevel(AlertLevel.WARNING);
				} else {
					tracePanel.hideAlertsWithLevel(AlertLevel.WARNING);
				}
			}
		});
		
		errorButton = new JToggleButton("ERRORS");
		errorButton.setSelected(true);
		buttonPanel.add(errorButton);
		errorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (errorButton.isSelected()) {
					tracePanel.showAlertsWithLevel(AlertLevel.ERROR);
				} else {
					tracePanel.hideAlertsWithLevel(AlertLevel.ERROR);
				}
			}
		});
		
		debugButton = new JToggleButton("DEBUG");
		debugButton.setSelected(false);
		buttonPanel.add(debugButton);
		debugButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (debugButton.isSelected()) {
					tracePanel.showAlertsWithLevel(AlertLevel.DEBUG);
				} else {
					tracePanel.hideAlertsWithLevel(AlertLevel.DEBUG);
				}
			}
		});
		
		tracePanel = new TracePanel();
		AlertLog.getInstance().addAlertListener(tracePanel);
		tracePanel.setPreferredSize(new Dimension(TRACEX * 2 / 3, TRACEY));
		tracePanel.setMaximumSize(new Dimension(TRACEX * 2 / 3, TRACEY));
		tracePanel.setMinimumSize(new Dimension(TRACEX * 2 / 3, TRACEY));
		tracePanel.showAlertsForAllLevels();
		tracePanel.hideAlertsWithLevel(AlertLevel.DEBUG);
		tracePanel.showAlertsForAllTags();
		this.add(BorderLayout.EAST, tracePanel);
		
		this.pack();
	}

}
