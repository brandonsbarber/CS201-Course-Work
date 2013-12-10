package cs201.helper.Brandon;

import java.util.HashMap;
import java.util.Map;

/**
 * A Menu class for use with picking food in the restaurant
 * @author Brandon
 *
 */
public class MenuBrandon
{
	private Map<String,Double> food;
	
	/**
	 * Creates a new Menu object with the given choices
	 * @param menuTemplate choices for the menu
	 */
	public MenuBrandon(Map<String,Double> menuTemplate)
	{
		food = new HashMap<String,Double>();
		
		for(String item : menuTemplate.keySet())
		{
			food.put(item, menuTemplate.get(item));
		}
	}
	
	/**
	 * Gets the current menu selection
	 * @return current menu selection
	 */
	public Map<String,Double> getFood()
	{
		return food;
	}
	
	/**
	 * Pseudo clone method.
	 * @return a clone of the menu
	 */
	public MenuBrandon clone()
	{
		MenuBrandon m = new MenuBrandon(food);
		return m;
	}
}
