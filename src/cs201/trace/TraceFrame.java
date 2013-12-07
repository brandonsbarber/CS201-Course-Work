package cs201.trace;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
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
	
	private JToggleButton personButton;
	private JToggleButton bankButton;
	private JToggleButton busStopButton;
	private JToggleButton restaurantButton;
	private JToggleButton marketButton;
	private JToggleButton residenceButton;
	private JToggleButton apartmentButton;
	private JToggleButton generalButton;
	private JToggleButton transitButton;
	
	public TraceFrame() {
		this.setTitle("Trace Panel");
		this.setPreferredSize(new Dimension(TRACEX, TRACEY));
		this.setResizable(false);
		this.setLocationByPlatform(true);
		this.setVisible(false);
		
		this.setLayout(new BorderLayout());
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());
		buttonPanel.setPreferredSize(new Dimension(TRACEX * 1 / 3, TRACEY));
		buttonPanel.setMaximumSize(new Dimension(TRACEX * 1 / 3, TRACEY));
		buttonPanel.setMinimumSize(new Dimension(TRACEX * 1 / 3, TRACEY));
		this.add(BorderLayout.WEST, buttonPanel);
		
		JPanel leftButtonPanel = new JPanel();
		leftButtonPanel.setLayout(new GridLayout(0, 1));
		leftButtonPanel.setBorder(BorderFactory.createTitledBorder("Alert Levels:"));
		buttonPanel.add(BorderLayout.WEST, leftButtonPanel);
		
		JPanel rightButtonPanel = new JPanel();
		rightButtonPanel.setLayout(new GridLayout(0, 1));
		rightButtonPanel.setBorder(BorderFactory.createTitledBorder("Tags:"));
		buttonPanel.add(BorderLayout.EAST, rightButtonPanel);
		
		infoButton = new JToggleButton("INFO");
		infoButton.setSelected(true);
		leftButtonPanel.add(infoButton);
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
		//messagesButton.setsi
		leftButtonPanel.add(messagesButton);
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
		leftButtonPanel.add(warningButton);
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
		leftButtonPanel.add(errorButton);
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
		leftButtonPanel.add(debugButton);
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
		
		personButton = new JToggleButton("PersonAgent");
		personButton.setSelected(true);
		rightButtonPanel.add(personButton);
		personButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (personButton.isSelected()) {
					tracePanel.showAlertsWithTag(AlertTag.PERSON_AGENT);
				} else {
					tracePanel.hideAlertsWithTag(AlertTag.PERSON_AGENT);
				}
			}
		});
		
		bankButton = new JToggleButton("Bank");
		bankButton.setSelected(true);
		rightButtonPanel.add(bankButton);
		bankButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (bankButton.isSelected()) {
					tracePanel.showAlertsWithTag(AlertTag.BANK);
				} else {
					tracePanel.hideAlertsWithTag(AlertTag.BANK);
				}
			}
		});
		
		busStopButton = new JToggleButton("Bus Stop");
		busStopButton.setSelected(true);
		rightButtonPanel.add(busStopButton);
		busStopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (busStopButton.isSelected()) {
					tracePanel.showAlertsWithTag(AlertTag.BUS_STOP);
				} else {
					tracePanel.hideAlertsWithTag(AlertTag.BUS_STOP);
				}
			}
		});
		
		restaurantButton = new JToggleButton("Restaurant");
		restaurantButton.setSelected(true);
		rightButtonPanel.add(restaurantButton);
		restaurantButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (restaurantButton.isSelected()) {
					tracePanel.showAlertsWithTag(AlertTag.RESTAURANT);
				} else {
					tracePanel.hideAlertsWithTag(AlertTag.RESTAURANT);
				}
			}
		});
		
		marketButton = new JToggleButton("Market");
		marketButton.setSelected(true);
		rightButtonPanel.add(marketButton);
		marketButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (marketButton.isSelected()) {
					tracePanel.showAlertsWithTag(AlertTag.MARKET);
				} else {
					tracePanel.hideAlertsWithTag(AlertTag.MARKET);
				}
			}
		});
		
		residenceButton = new JToggleButton("Residence");
		residenceButton.setSelected(true);
		rightButtonPanel.add(residenceButton);
		residenceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (residenceButton.isSelected()) {
					tracePanel.showAlertsWithTag(AlertTag.RESIDENCE);
				} else {
					tracePanel.hideAlertsWithTag(AlertTag.RESIDENCE);
				}
			}
		});
		
		apartmentButton = new JToggleButton("Apartment");
		apartmentButton.setSelected(true);
		rightButtonPanel.add(apartmentButton);
		apartmentButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (apartmentButton.isSelected()) {
					tracePanel.showAlertsWithTag(AlertTag.APARTMENT_COMPLEX);
				} else {
					tracePanel.hideAlertsWithTag(AlertTag.APARTMENT_COMPLEX);
				}
			}
		});
		
		generalButton = new JToggleButton("City");
		generalButton.setSelected(true);
		rightButtonPanel.add(generalButton);
		generalButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (generalButton.isSelected()) {
					tracePanel.showAlertsWithTag(AlertTag.GENERAL_CITY);
				} else {
					tracePanel.hideAlertsWithTag(AlertTag.GENERAL_CITY);
				}
			}
		});
		
		transitButton = new JToggleButton("Transit");
		transitButton.setSelected(true);
		rightButtonPanel.add(transitButton);
		transitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (transitButton.isSelected()) {
					tracePanel.showAlertsWithTag(AlertTag.TRANSIT);
				} else {
					tracePanel.hideAlertsWithTag(AlertTag.TRANSIT);
				}
			}
		});
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		//rightPanel.setPreferredSize(new Dimension(TRACEX * 2 / 3, TRACEY));
		//rightPanel.setMaximumSize(new Dimension(TRACEX * 2 / 3, TRACEY));
		//rightPanel.setMinimumSize(new Dimension(TRACEX * 2 / 3, TRACEY));
		tracePanel = new TracePanel();
		AlertLog.getInstance().addAlertListener(tracePanel);
		//tracePanel.setPreferredSize(new Dimension(0, 0));
		//tracePanel.setMaximumSize(new Dimension(TRACEX * 2 / 3, 0));
		//tracePanel.setMinimumSize(new Dimension(TRACEX * 2 / 3, 0));
		tracePanel.showAlertsForAllLevels();
		tracePanel.hideAlertsWithLevel(AlertLevel.DEBUG);
		tracePanel.showAlertsForAllTags();
		rightPanel.add(tracePanel);
		this.add(BorderLayout.EAST, rightPanel);
		
		this.pack();
	}

}
