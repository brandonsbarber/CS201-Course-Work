package cs201;

import javax.swing.JFrame;

import cs201.gui.SimCity201;

/**
 * Main entry point for the program. Creates the main window and that's it
 * @author Matt Pohlmann
 *
 */
public class Main {

	public static void main(String[] args) {
		SimCity201 app = new SimCity201();
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setResizable(false);
		app.pack();
		app.setVisible(true);
	}

}
