package cs201.helper.Matt;

import java.util.*;

/**
 * The Restaurant's Menu
 */
public class MenuMatt {	
	private Map<String, Double> items;
	
	public MenuMatt() {
		items = new Hashtable<String, Double>();
		
		items.put("Steak", 15.99);
		items.put("Pasta", 9.99);
		items.put("Pizza", 8.99);
		items.put("Ice Cream", 4.99);
		items.put("Chicken", 10.99);
		items.put("Salad", 5.99);
	}
	
	/**
	 * Returns the number of items on the Menu
	 * @return An integer representing the number of items on the Menu
	 */
	public int size() {
		return items.size();
	}
	
	/**
	 * Returns the Menu item at location where
	 * @param where The index of the Menu item you are looking for
	 * @return A String representing the Menu item, or null if the index does not exist
	 */
	public String at(int where) {		
		ArrayList<String> keys = new ArrayList<String>(items.keySet());
		
		return (where >= 0 && where < keys.size()) ? keys.get(where) : null;
	}
	
	public Double getPrice(String ofWhat) {
		return items.get(ofWhat);
	}
	
	/**
	 * Gets a random item off the Menu
	 * @return A String representing a random Menu item, or null if there are no items on the Menu
	 */
	public String randomItem() {
		ArrayList<String> keys = new ArrayList<String>(items.keySet());
		Random rand = new Random();
		
		return (items.size() == 0) ? null : keys.get(rand.nextInt(items.size()));
	}
	
	/**
	 * Takes an item off the Menu
	 * @param what A String representing the item to be removed
	 */
	public void removeItem(String what) {
		try {
			items.remove(what);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns an HTML formatted table of all the items on the menu
	 * @return A String containing HTML tags for pretty printing
	 */
	public String toHTML() {
		String output = "<table>";
		Set<String> keys = items.keySet();
		
		for (String menuItem : keys) {
			output += "<tr>";
			output += "<td>" + menuItem + ":</td></td>$" + items.get(menuItem).toString() + "</td>";
			output += "</tr>";
		}
		
		output += "</table>";

		return output;
	}
}
