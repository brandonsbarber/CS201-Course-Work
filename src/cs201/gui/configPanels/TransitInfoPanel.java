package cs201.gui.configPanels;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import cs201.agents.transit.BusAgent;
import cs201.agents.transit.CarAgent;
import cs201.agents.transit.TruckAgent;
import cs201.agents.transit.VehicleAgent;

@SuppressWarnings("serial")
public class TransitInfoPanel extends JPanel
{

	private TransitConfigPanel transitPanel;
	private JTextField nameTextField,passTextField,currentTextField,destinationTextField;
	
	
	public TransitInfoPanel(TransitConfigPanel transitConfigPanel)
	{
		transitPanel = transitConfigPanel;
		
		setBorder(null);
		setForeground(UIManager.getColor("window"));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{300, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Vehicle:"));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		add(panel, gbc_panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JPanel leftPanel = new JPanel();
		panel.add(leftPanel);
		GridBagLayout gbl_leftPanel = new GridBagLayout();
		gbl_leftPanel.rowWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
		gbl_leftPanel.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0};
		gbl_leftPanel.columnWeights = new double[]{0.0, 1.0};
		gbl_leftPanel.columnWidths = new int[]{0, 0};
		leftPanel.setLayout(gbl_leftPanel);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setFont(new Font("SansSerif", Font.PLAIN, 11));
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.anchor = GridBagConstraints.EAST;
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 0;
		leftPanel.add(lblName, gbc_lblName);
		lblName.setLabelFor(nameTextField);
		
		nameTextField = new JTextField();
		nameTextField.setPreferredSize(new Dimension(12, 28));
		nameTextField.setMaximumSize(new Dimension(2147483647, 28));
		nameTextField.setMinimumSize(new Dimension(12, 28));
		nameTextField.setFont(new Font("SansSerif", Font.PLAIN, 11));
		nameTextField.setName("");
		nameTextField.setFocusable(false);
		nameTextField.setEditable(false);
		GridBagConstraints gbc_nameTextField = new GridBagConstraints();
		gbc_nameTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_nameTextField.insets = new Insets(0, 0, 5, 0);
		gbc_nameTextField.gridx = 1;
		gbc_nameTextField.gridy = 0;
		leftPanel.add(nameTextField, gbc_nameTextField);
		nameTextField.setColumns(10);
		
		JLabel lblPassenger = new JLabel("Passengers/Deliveries:");
		lblPassenger.setFont(new Font("SansSerif", Font.PLAIN, 11));
		GridBagConstraints gbc_lblPassenger = new GridBagConstraints();
		gbc_lblPassenger.anchor = GridBagConstraints.EAST;
		gbc_lblPassenger.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassenger.gridx = 0;
		gbc_lblPassenger.gridy = 1;
		leftPanel.add(lblPassenger, gbc_lblPassenger);
		lblPassenger.setLabelFor(passTextField);
		
		passTextField = new JTextField();
		passTextField.setPreferredSize(new Dimension(12, 28));
		passTextField.setMaximumSize(new Dimension(2147483647, 28));
		passTextField.setMinimumSize(new Dimension(12, 28));
		passTextField.setFont(new Font("SansSerif", Font.PLAIN, 11));
		passTextField.setName("");
		passTextField.setFocusable(false);
		passTextField.setEditable(false);
		GridBagConstraints gbc_passTextField = new GridBagConstraints();
		gbc_passTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passTextField.insets = new Insets(0, 0, 5, 0);
		gbc_passTextField.gridx = 1;
		gbc_passTextField.gridy = 1;
		leftPanel.add(passTextField, gbc_passTextField);
		passTextField.setColumns(10);
		
		JLabel lblCurrent = new JLabel("Current Location:");
		lblCurrent.setFont(new Font("SansSerif", Font.PLAIN, 11));
		GridBagConstraints gbc_lblCurrent = new GridBagConstraints();
		gbc_lblCurrent.anchor = GridBagConstraints.EAST;
		gbc_lblCurrent.insets = new Insets(0, 0, 5, 5);
		gbc_lblCurrent.gridx = 0;
		gbc_lblCurrent.gridy = 2;
		leftPanel.add(lblCurrent, gbc_lblCurrent);
		lblCurrent.setLabelFor(currentTextField);
		
		currentTextField = new JTextField();
		currentTextField.setPreferredSize(new Dimension(12, 28));
		currentTextField.setMaximumSize(new Dimension(2147483647, 28));
		currentTextField.setMinimumSize(new Dimension(12, 28));
		currentTextField.setFont(new Font("SansSerif", Font.PLAIN, 11));
		currentTextField.setName("");
		currentTextField.setFocusable(false);
		currentTextField.setEditable(false);
		GridBagConstraints gbc_currentTextField = new GridBagConstraints();
		gbc_currentTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_currentTextField.insets = new Insets(0, 0, 5, 0);
		gbc_currentTextField.gridx = 1;
		gbc_currentTextField.gridy = 2;
		leftPanel.add(currentTextField, gbc_currentTextField);
		currentTextField.setColumns(10);
		
		JLabel lblDestination = new JLabel("Destination:");
		lblDestination.setFont(new Font("SansSerif", Font.PLAIN, 11));
		GridBagConstraints gbc_lblDestination = new GridBagConstraints();
		gbc_lblDestination.anchor = GridBagConstraints.EAST;
		gbc_lblDestination.insets = new Insets(0, 0, 5, 5);
		gbc_lblDestination.gridx = 0;
		gbc_lblDestination.gridy = 3;
		leftPanel.add(lblDestination, gbc_lblDestination);
		lblDestination.setLabelFor(destinationTextField);
		
		destinationTextField = new JTextField();
		destinationTextField.setPreferredSize(new Dimension(12, 28));
		destinationTextField.setMaximumSize(new Dimension(2147483647, 28));
		destinationTextField.setMinimumSize(new Dimension(12, 28));
		destinationTextField.setFont(new Font("SansSerif", Font.PLAIN, 11));
		destinationTextField.setName("");
		destinationTextField.setFocusable(false);
		destinationTextField.setEditable(false);
		GridBagConstraints gbc_destinationTextField = new GridBagConstraints();
		gbc_destinationTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_destinationTextField.insets = new Insets(0, 0, 5, 0);
		gbc_destinationTextField.gridx = 1;
		gbc_destinationTextField.gridy = 3;
		leftPanel.add(destinationTextField, gbc_destinationTextField);
		destinationTextField.setColumns(10);
	}

	public void resetInfo()
	{
		passTextField.setText("");
		nameTextField.setText("");
		currentTextField.setText("");
		destinationTextField.setText("");
	}
	
	public void updateInfo(VehicleAgent vehicle)
	{
		nameTextField.setText(vehicle.toString());
		
		if(vehicle instanceof BusAgent)
		{
			passTextField.setText(""+((BusAgent)vehicle).getNumPassengers());
		}
		else if(vehicle instanceof CarAgent)
		{
			passTextField.setText(""+((((CarAgent)vehicle).p == null)?0:1));
		}
		else if(vehicle instanceof TruckAgent)
		{
			passTextField.setText(""+((TruckAgent)vehicle).deliveries.size());
		}
		else
		{
			passTextField.setText(""+0);
		}
		
		currentTextField.setText(""+vehicle.currentLocation);
		destinationTextField.setText(""+vehicle.destination);
	}

}
