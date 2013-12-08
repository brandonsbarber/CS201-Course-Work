package cs201.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cs201.helper.CityDirectory;


/**
 * The frame shown to manipulate time in SimCity201.
 * @author Ben Doherty
 *
 */
public class TimePanel extends JDialog implements ChangeListener {	

	/* *********
	 * CONSTANTS
	 * *********
	 */
	
	private static final int BORDER_THICKNESS 	= 20;
	private static final int FRAME_HEIGHT 		= 200;
	private static final int FRAME_WIDTH 		= 500;
	private static final int SLIDER_MIN			= 100;
	private static final int SLIDER_MAX			= 5000;
	private static final int SLIDER_INIT		= 2000;
	private static final int MAJOR_TICK 		= 500;
	private static final int MINOR_TICK 		= 100;
	
	/* ***********
	 * COMPONENETS
	 * ***********
	 */
	JPanel panel = new JPanel();
	JSlider slider = new JSlider(JSlider.HORIZONTAL, SLIDER_MIN, SLIDER_MAX, SLIDER_INIT);
	JLabel timeLabel = new JLabel("Hello, world");
	
	/* ***********
	 * CONNECTIONS
	 * ***********
	 */
	
	/* *********
	 * VARIABLES
	 * *********
	 */
	
	/* ************
	 * CONSTRUCTORS
	 * ************
	 */
	
	public TimePanel() {
		// Frame set up
		setBackground(Color.LIGHT_GRAY);
		setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		setMaximumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		this.getContentPane().add(panel);
		panel.setBorder(BorderFactory.createEmptyBorder(BORDER_THICKNESS, BORDER_THICKNESS, BORDER_THICKNESS, BORDER_THICKNESS));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		setUpComponents();
	}
	
	/* ****************
	 * PUBLIC FUNCTIONS
	 * ****************
	 */
	
	/**
	 * Shows the time panel.
	 */
	public void showTimePanel() {
		this.setVisible(true);
	}
	
	/* *****************
	 * PRIVATE FUNCTIONS
	 * *****************
	 */
	
	private void setUpComponents() {
		// Add a slider
		slider.addChangeListener(this);
		slider.setMajorTickSpacing(MAJOR_TICK);
		slider.setMinorTickSpacing(MINOR_TICK);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		panel.add(slider);
		
		// Add some space
		panel.add(Box.createRigidArea(new Dimension(0, 20)));
		
		// Add a label
		panel.add(timeLabel);
	}
	
	private void setTimeLabel(int value) {
		double realSeconds = value / 1000.0;		// convert from milliseconds to seconds
		String text = String.format("%.2f seconds of real time = 15 minutes in SimCity201", realSeconds);
		timeLabel.setText(text);
	}
	
	/* *****************
	 * SETTERS / GETTERS
	 * *****************
	 */

	
	/* *********
	 * CALLBACKS
	 * *********
	 */
	
	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
		
		// Update the label
		setTimeLabel(source.getValue());
		
		// Once the user has stopped adjusting the slider, update SimCity time
		if (!source.getValueIsAdjusting()) {
			CityDirectory.getInstance().setTimerOut(source.getValue());
		}
	}
	

}
