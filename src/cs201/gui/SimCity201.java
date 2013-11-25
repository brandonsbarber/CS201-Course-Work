package cs201.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import cs201.gui.structures.market.MarketAnimationPanel;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelMatt;
import cs201.helper.CityDirectory;
import cs201.roles.marketRoles.MarketEmployeeRole;
import cs201.roles.marketRoles.MarketManagerRole.ItemRequest;
import cs201.structures.market.MarketStructure;
import cs201.structures.restaurant.RestaurantMatt;

public class SimCity201 extends JFrame {
	private final int SIZEX = 1200;
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
		
		add(BorderLayout.WEST, settingsPanel);
		add(BorderLayout.EAST, guiPanel);
		
		settingsPanel.addPanel("Restaurants",new ConfigPanel());
		settingsPanel.addPanel("Transit",new TransitConfigPanel());
		settingsPanel.addPanel("Transit",new TransitConfigPanel());
		settingsPanel.addPanel("Banks",new ConfigPanel());
		settingsPanel.addPanel("Markets",new ConfigPanel());
		settingsPanel.addPanel("Housing",new ConfigPanel());
		settingsPanel.addPanel("Housing",new ConfigPanel());
		settingsPanel.addPanel("Housing",new ConfigPanel());
		settingsPanel.addPanel("Restaurants",new ConfigPanel());
		settingsPanel.addPanel("Restaurants",new ConfigPanel());
		
		RestaurantAnimationPanelMatt g = new RestaurantAnimationPanelMatt(0,this);
		RestaurantMatt r = new RestaurantMatt(100,100,50,50,0,g);
		r.setStructurePanel(g);
		buildingPanels.add(g,""+0);
		cityPanel.addStructure(r);
		CityDirectory.getInstance().addRestaurant(r);
		
		MarketAnimationPanel mG = new MarketAnimationPanel(1,this, 50, 50);
		MarketStructure m = new MarketStructure(225,100,50,50,1,mG);
		m.setStructurePanel(mG);
		buildingPanels.add(mG,""+1);
		cityPanel.addStructure(m);
		CityDirectory.getInstance().addMarket(m);
		
		pack();
		CityDirectory.getInstance().startTime();
			
		m.addInventory("Pizza", 20, 20);
		m.getManager().msgHereIsMyOrderForDelivery(r, new ItemRequest("Pizza",1));
		m.getManager().pickAndExecuteAnAction();
		((MarketEmployeeRole)m.getEmployees().get(0)).pickAndExecuteAnAction();
		m.getManager().pickAndExecuteAnAction();
	}
	
	public void displayStructurePanel(StructurePanel bp) {
		cardLayout.show(buildingPanels, bp.getName());
	}
}
