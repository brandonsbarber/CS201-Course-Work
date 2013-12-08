package cs201.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class ArtManager
{
	private static Map<String,BufferedImage> atlas;
	
	public static void load() throws IOException
	{
		atlas = Collections.synchronizedMap(new HashMap<String,BufferedImage>());
		
		//Brandon's Sprites
		atlas.put("Person_Up", ImageIO.read(new File("data/TransitSprites/Walk_North.png")));
		atlas.put("Person_Down", ImageIO.read(new File("data/TransitSprites/Walk_South.png")));
		atlas.put("Person_Left", ImageIO.read(new File("data/TransitSprites/Walk_West.png")));
		atlas.put("Person_Right", ImageIO.read(new File("data/TransitSprites/Walk_East.png")));
		
		atlas.put("Truck_Up", ImageIO.read(new File("data/TransitSprites/Truck.png")));
		atlas.put("Truck_Down", ImageIO.read(new File("data/TransitSprites/Truck.png")));
		atlas.put("Truck_Left", ImageIO.read(new File("data/TransitSprites/Truck.png")));
		atlas.put("Truck_Right", ImageIO.read(new File("data/TransitSprites/Truck.png")));
		
		atlas.put("Bus_Up", ImageIO.read(new File("data/TransitSprites/Bus_North.png")));
		atlas.put("Bus_Down", ImageIO.read(new File("data/TransitSprites/Bus_South.png")));
		atlas.put("Bus_Left", ImageIO.read(new File("data/TransitSprites/Bus_West.png")));
		atlas.put("Bus_Right", ImageIO.read(new File("data/TransitSprites/Bus_East.png")));
		
		//Ben's Sprites
		
		
		//Matt's Sprites
		atlas.put("Default_Walker_Up", ImageIO.read(new File("data/DefaultSprites/Up.png")));
		atlas.put("Default_Walker_Down", ImageIO.read(new File("data/DefaultSprites/Down.png")));
		atlas.put("Default_Walker_Left", ImageIO.read(new File("data/DefaultSprites/Left.png")));
		atlas.put("Default_Walker_Right", ImageIO.read(new File("data/DefaultSprites/Right.png")));
		atlas.put("Restaurant_Matt", ImageIO.read(new File("data/Restaurant/RestaurantMatt.png")));
		atlas.put("Waiter_Up", ImageIO.read(new File("data/Restaurant/WaiterUp.png")));
		atlas.put("Waiter_Down", ImageIO.read(new File("data/Restaurant/WaiterDown.png")));
		atlas.put("Waiter_Left", ImageIO.read(new File("data/Restaurant/WaiterLeft.png")));
		atlas.put("Waiter_Right", ImageIO.read(new File("data/Restaurant/WaiterRight.png")));
		atlas.put("Cashier_Up", ImageIO.read(new File("data/Restaurant/CashierUp.png")));
		atlas.put("Cashier_Down", ImageIO.read(new File("data/Restaurant/CashierDown.png")));
		atlas.put("Cashier_Left", ImageIO.read(new File("data/Restaurant/CashierLeft.png")));
		atlas.put("Cashier_Right", ImageIO.read(new File("data/Restaurant/CashierRight.png")));
		atlas.put("Host_Up", ImageIO.read(new File("data/Restaurant/HostUp.png")));
		atlas.put("Host_Down", ImageIO.read(new File("data/Restaurant/HostDown.png")));
		atlas.put("Host_Left", ImageIO.read(new File("data/Restaurant/HostLeft.png")));
		atlas.put("Host_Right", ImageIO.read(new File("data/Restaurant/HostRight.png")));
		
		//James's Sprites
		
		
		//Skyler's Sprites
		
		
	}
	
	public static BufferedImage getImage(String key)
	{
		if(atlas.containsKey(key))
		{
			return atlas.get(key);
		}
		return null;
	}
	
}
