package cs201.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import cs201.helper.CityDirectory;

public class SimCity201 extends JFrame {
	private final int SIZEX = 1500;
	private final int SIZEY	= 700;
	
	CityPanel cityPanel;
	JPanel buildingPanels;
	CardLayout cardLayout;
	
	SettingsPanel settingsPanel;
	
	public SimCity201() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setSize(SIZEX, SIZEY);
		
		JPanel guiPanel = new JPanel();
		
		setLayout(new BorderLayout());
		
		guiPanel.setLayout(new BorderLayout());
		
		cityPanel = new CityPanel();
		cityPanel.setPreferredSize(new Dimension(SIZEX / 3, SIZEY));
		cityPanel.setMaximumSize(new Dimension(SIZEX / 3, SIZEY));
		cityPanel.setMinimumSize(new Dimension(SIZEX / 3, SIZEY));
		
		cardLayout = new CardLayout();
		
		buildingPanels = new JPanel();
		buildingPanels.setLayout(cardLayout);
		buildingPanels.setMinimumSize(new Dimension(SIZEX / 3, SIZEY));
		buildingPanels.setMaximumSize(new Dimension(SIZEX / 3, SIZEY));
		buildingPanels.setPreferredSize(new Dimension(SIZEX / 3, SIZEY));
		buildingPanels.setBackground(Color.YELLOW);

		// Create initial buildings here and add them to cityPanel and buildingPanels
		
		JScrollPane cityScrollPane = new JScrollPane(cityPanel);
		
		cityScrollPane.setMinimumSize(new Dimension(SIZEX / 3, SIZEY));
		cityScrollPane.setMaximumSize(new Dimension(SIZEX / 3, SIZEY));
		cityScrollPane.setPreferredSize(new Dimension(SIZEX / 3, SIZEY));
		
		cityScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		cityScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		guiPanel.add(BorderLayout.WEST, cityScrollPane);
		guiPanel.add(BorderLayout.EAST, buildingPanels);
		
		settingsPanel = new SettingsPanel();
		
		settingsPanel.setMinimumSize(new Dimension(SIZEX / 3, SIZEY));
		settingsPanel.setMaximumSize(new Dimension(SIZEX / 3, SIZEY));
		settingsPanel.setPreferredSize(new Dimension(SIZEX / 3, SIZEY));
		
		add(settingsPanel,BorderLayout.WEST);
		add(guiPanel);
		
		
		
		pack();
		CityDirectory.getInstance().startTime();
	}
	
	public void displayStructurePanel(StructurePanel bp) {
		cardLayout.show(buildingPanels, bp.getName());
	}
}
