package cs201;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.*;

import cs201.gui.SimCity201;

/**
 * Main entry point for the program. Creates the main window and that's it
 * @author Matt Pohlmann
 *
 */
public class Main {

	public static void main(String[] args) {
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}
		
		SimCity201 app = new SimCity201();
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setResizable(false);
		app.pack();
		app.setVisible(true);
	}

}
