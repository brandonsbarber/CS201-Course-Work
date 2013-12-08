package cs201.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
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
	private static final int FRAME_HEIGHT 		= 300;
	private static final int FRAME_WIDTH 		= 500;
	private static final int SLIDER_MIN			= 100;
	private static final int SLIDER_MAX			= 5000;
	private static final int SLIDER_INIT		= 2000;
	
	/* ***********
	 * COMPONENETS
	 * ***********
	 */
	JPanel panel = new JPanel();
	JSlider slider = new JSlider(JSlider.HORIZONTAL, SLIDER_MIN, SLIDER_MAX, SLIDER_INIT);
	
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
	
	/* *****************
	 * SETTERS / GETTERS
	 * *****************
	 */
	
	private void setUpComponents() {
		// Add a slider
		slider.addChangeListener(this);
		slider.setMajorTickSpacing(500);
		slider.setMinorTickSpacing(100);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		panel.add(slider);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
		if (!source.getValueIsAdjusting()) {
			//JOptionPane.showMessageDialog(this, source.getValue());
			CityDirectory.getInstance().setTimerOut(source.getValue());
		}
	}

}
