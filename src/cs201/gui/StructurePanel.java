package cs201.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public abstract class StructurePanel extends JPanel implements ActionListener {
	private String name;
	SimCity201 city;
	
    private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
    private final int ANIMATIONLENGTH = 10;
    private Timer timer;

	public StructurePanel(int i, SimCity201 sc) {
		
		name = "" + i;
		city = sc;
		
		setBackground(Color.LIGHT_GRAY);
		setMinimumSize(new Dimension(1000, 250));
		setMaximumSize(new Dimension(1000, 250));
		setPreferredSize(new Dimension(1000, 250));
		
		JLabel j = new JLabel(this.getClass().getSimpleName() + " " + name);
		add(j);
		
        timer = new Timer(ANIMATIONLENGTH, this);
        timer.setRepeats(true);
    	timer.start();
	}

	public String getName() {
		return name;
	}
	
	public void toggleTimer() {
		if (timer.isRunning()) {
			timer.stop();
		} else {
			timer.start();
		}
	}
	
	public void displayStructurePanel() {
		city.displayStructurePanel(this);
	}
	
	public void addGui(Gui g) {
		guis.add(g);
	}
	
	public void removeGui(Gui g) {
		guis.remove(g);
	}
	
	public void paintComponent(Graphics g) { 
    	Graphics2D g2 = (Graphics2D)g;
    	
    	try {
	        for(Gui gui : guis) {
	            if (gui.isPresent()) {
	                gui.draw(g2);
	            }
	        }
    	} catch (ConcurrentModificationException e) {
    		return;
    	}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			for(Gui gui : guis) {
	            if (gui.isPresent()) {
	                gui.updatePosition();
	            }
	        }
		} catch (ConcurrentModificationException e) {
    		return;
    	}
		
		repaint();  //Will have paintComponent called
	}
}
