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
		
		ArtManager manager = new ArtManager();

		//Brandon's Sprites
		atlas.put("Person_Up", ImageIO.read(manager.getClass().getResourceAsStream("/data/TransitSprites/Walk_North.png")));
		atlas.put("Person_Down", ImageIO.read(manager.getClass().getResourceAsStream("/data/TransitSprites/Walk_South.png")));
		atlas.put("Person_Left", ImageIO.read(manager.getClass().getResourceAsStream("/data/TransitSprites/Walk_West.png")));
		atlas.put("Person_Right", ImageIO.read(manager.getClass().getResourceAsStream("/data/TransitSprites/Walk_East.png")));
		
		atlas.put("Truck_Up", ImageIO.read(manager.getClass().getResourceAsStream("/data/TransitSprites/Truck_Up.png")));
		atlas.put("Truck_Down", ImageIO.read(manager.getClass().getResourceAsStream("/data/TransitSprites/Truck_Down.png")));
		atlas.put("Truck_Left", ImageIO.read(manager.getClass().getResourceAsStream("/data/TransitSprites/Truck_Left.png")));
		atlas.put("Truck_Right", ImageIO.read(manager.getClass().getResourceAsStream("/data/TransitSprites/Truck_Right.png")));
		
		atlas.put("Bus_Up", ImageIO.read(manager.getClass().getResourceAsStream("/data/TransitSprites/Bus_North.png")));
		atlas.put("Bus_Down", ImageIO.read(manager.getClass().getResourceAsStream("/data/TransitSprites/Bus_South.png")));
		atlas.put("Bus_Left", ImageIO.read(manager.getClass().getResourceAsStream("/data/TransitSprites/Bus_West.png")));
		atlas.put("Bus_Right", ImageIO.read(manager.getClass().getResourceAsStream("/data/TransitSprites/Bus_East.png")));
		
		atlas.put("Car_Empty_Up", ImageIO.read(manager.getClass().getResourceAsStream("/data/TransitSprites/Car_Empty_Up.png")));
		atlas.put("Car_Empty_Down", ImageIO.read(manager.getClass().getResourceAsStream("/data/TransitSprites/Car_Empty_Down.png")));
		atlas.put("Car_Empty_Left", ImageIO.read(manager.getClass().getResourceAsStream("/data/TransitSprites/Car_Empty_Left.png")));
		atlas.put("Car_Empty_Right", ImageIO.read(manager.getClass().getResourceAsStream("/data/TransitSprites/Car_Empty_Right.png")));
		
		atlas.put("Car_Occupied_Up", ImageIO.read(manager.getClass().getResourceAsStream("/data/TransitSprites/Car_Occupied_Up.png")));
		atlas.put("Car_Occupied_Down", ImageIO.read(manager.getClass().getResourceAsStream("/data/TransitSprites/Car_Occupied_Down.png")));
		atlas.put("Car_Occupied_Left", ImageIO.read(manager.getClass().getResourceAsStream("/data/TransitSprites/Car_Occupied_Left.png")));
		atlas.put("Car_Occupied_Right", ImageIO.read(manager.getClass().getResourceAsStream("/data/TransitSprites/Car_Occupied_Right.png")));
		
		atlas.put("Restaurant_Brandon_Open",ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/Brandon/RestaurantBrandonOpen.png")));
		atlas.put("Restaurant_Brandon_Closed",ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/Brandon/RestaurantBrandonClosed.png")));
		atlas.put("Restaurant_Brandon_Floor",ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/Brandon/RestaurantBrandonFloor.png")));
		atlas.put("Restaurant_Brandon_Table",ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/Brandon/RestaurantBrandonTable.png")));
		
		atlas.put("Restaurant_Brandon_Grill_Open",ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/Brandon/RestaurantBrandonGrillEmpty.png")));
		atlas.put("Restaurant_Brandon_Grill_Filled",ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/Brandon/RestaurantBrandonGrillFilled.png")));
		atlas.put("Restaurant_Brandon_Pokeball",ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/Brandon/RestaurantBrandonPokeball.png")));
		atlas.put("Restaurant_Brandon_Kitchen_Counter",ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/Brandon/RestaurantBrandonKitchenCounter.png")));
		atlas.put("Restaurant_Brandon_Kitchen_Counter_Top",ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/Brandon/RestaurantBrandonKitchenCounterTop.png")));
		atlas.put("Restaurant_Brandon_Kitchen_Counter_Bottom",ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/Brandon/RestaurantBrandonKitchenCounterBottom.png")));
		atlas.put("Restaurant_Brandon_Cash_Register",ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/Brandon/RestaurantBrandonCashRegister.png")));
		atlas.put("Restaurant_Brandon_Rug",ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/Brandon/RestaurantBrandonRug.png")));
		
		atlas.put("Grass_Tile",ImageIO.read(manager.getClass().getResourceAsStream("/data/TransitSprites/Tiles/GrassTile.png")));
		atlas.put("Sidewalk_Tile",ImageIO.read(manager.getClass().getResourceAsStream("/data/TransitSprites/Tiles/SidewalkTile.png")));
		atlas.put("Road_Tile",ImageIO.read(manager.getClass().getResourceAsStream("/data/TransitSprites/Tiles/RoadTile.png")));
		
		atlas.put("Bus_Stop_Bench", ImageIO.read(manager.getClass().getResourceAsStream("/data/TransitSprites/BusStop_Bench.png")));
		
		atlas.put("Explosion_0", ImageIO.read(manager.getClass().getResourceAsStream("/data/TransitSprites/Explosion_0.png")));
		atlas.put("Explosion_1", ImageIO.read(manager.getClass().getResourceAsStream("/data/TransitSprites/Explosion_1.png")));
		atlas.put("Explosion_2", ImageIO.read(manager.getClass().getResourceAsStream("/data/TransitSprites/Explosion_2.png")));
		atlas.put("Explosion_3", ImageIO.read(manager.getClass().getResourceAsStream("/data/TransitSprites/Explosion_3.png")));
		atlas.put("Explosion_4", ImageIO.read(manager.getClass().getResourceAsStream("/data/TransitSprites/Explosion_4.png")));
		atlas.put("Explosion_5", ImageIO.read(manager.getClass().getResourceAsStream("/data/TransitSprites/Explosion_5.png")));
		atlas.put("Explosion_6", ImageIO.read(manager.getClass().getResourceAsStream("/data/TransitSprites/Explosion_6.png")));
		atlas.put("Explosion_7", ImageIO.read(manager.getClass().getResourceAsStream("/data/TransitSprites/Explosion_7.png")));
		
		//Ben's Sprites
		atlas.put("Market_Floor", ImageIO.read(manager.getClass().getResourceAsStream("/data/Market/Floor.png")));
		atlas.put("Market_Front_Desk", ImageIO.read(manager.getClass().getResourceAsStream("/data/Market/Front_Desk.png")));
		atlas.put("Market_Shelf", ImageIO.read(manager.getClass().getResourceAsStream("/data/Market/Shelf.png")));
		atlas.put("Market_Shelf_Top", ImageIO.read(manager.getClass().getResourceAsStream("/data/Market/Shelf_Top.png")));
		atlas.put("Market_Shelf_Bottom_Left", ImageIO.read(manager.getClass().getResourceAsStream("/data/Market/Shelf_Bottom_Left.png")));
		atlas.put("Market_Shelf_Bottom_Right", ImageIO.read(manager.getClass().getResourceAsStream("/data/Market/Shelf_Bottom_Right.png")));
		
		atlas.put("Market_Employee_Up", ImageIO.read(manager.getClass().getResourceAsStream("/data/Market/Employee_North.png")));
		atlas.put("Market_Employee_Down", ImageIO.read(manager.getClass().getResourceAsStream("/data/Market/Employee_South.png")));
		atlas.put("Market_Employee_Left", ImageIO.read(manager.getClass().getResourceAsStream("/data/Market/Employee_West.png")));
		atlas.put("Market_Employee_Right", ImageIO.read(manager.getClass().getResourceAsStream("/data/Market/Employee_East.png")));
		atlas.put("Market_Manager_Down", ImageIO.read(manager.getClass().getResourceAsStream("/data/Market/Manager_South.png")));
		
		atlas.put("Restaurant_Ben_Floor", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/Ben/Floor.png")));
		atlas.put("Restaurant_Ben_Table", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/Ben/Table.png")));
		atlas.put("Restaurant_Ben_Waiter_Up", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/Ben/Waiter_North.png")));
		atlas.put("Restaurant_Ben_Waiter_Down", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/Ben/Waiter_South.png")));
		atlas.put("Restaurant_Ben_Waiter_Left", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/Ben/Waiter_West.png")));
		atlas.put("Restaurant_Ben_Waiter_Right", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/Ben/Waiter_East.png")));
		
		atlas.put("Market_Car", ImageIO.read(manager.getClass().getResourceAsStream("/data/Market/Car.png")));
		
		//Matt's Sprites
		atlas.put("Default_Walker_Up", ImageIO.read(manager.getClass().getResourceAsStream("/data/DefaultSprites/Up.png")));
		atlas.put("Default_Walker_Down", ImageIO.read(manager.getClass().getResourceAsStream("/data/DefaultSprites/Down.png")));
		atlas.put("Default_Walker_Left", ImageIO.read(manager.getClass().getResourceAsStream("/data/DefaultSprites/Left.png")));
		atlas.put("Default_Walker_Right", ImageIO.read(manager.getClass().getResourceAsStream("/data/DefaultSprites/Right.png")));
		
		atlas.put("Restaurant_Matt_Open", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/RestaurantMattOpen.png")));
		atlas.put("Restaurant_Matt_Closed", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/RestaurantMattClosed.png")));
		
		atlas.put("Waiter_Up", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/WaiterUp.png")));
		atlas.put("Waiter_Down", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/WaiterDown.png")));
		atlas.put("Waiter_Left", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/WaiterLeft.png")));
		atlas.put("Waiter_Right", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/WaiterRight.png")));
		
		atlas.put("Cashier_Up", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/CashierUp.png")));
		atlas.put("Cashier_Down", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/CashierDown.png")));
		atlas.put("Cashier_Left", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/CashierLeft.png")));
		atlas.put("Cashier_Right", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/CashierRight.png")));
		
		atlas.put("Host_Up", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/HostUp.png")));
		atlas.put("Host_Down", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/HostDown.png")));
		atlas.put("Host_Left", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/HostLeft.png")));
		atlas.put("Host_Right", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/HostRight.png")));
		
		atlas.put("Cook_Up", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/CookUp.png")));
		atlas.put("Cook_Down", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/CookDown.png")));
		atlas.put("Cook_Left", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/CookLeft.png")));
		atlas.put("Cook_Right", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/CookRight.png")));
		
		atlas.put("Kitchen", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/Kitchen.png")));
		atlas.put("Table", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/Table.png")));
		atlas.put("Cash_Register", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/CashRegister.png")));
		atlas.put("Floor", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/Floor.png")));
		
		atlas.put("Stand_0", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/Stand0.png")));
		atlas.put("Stand_1", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/Stand1.png")));
		atlas.put("Stand_2", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/Stand2.png")));
		atlas.put("Stand_3", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/Stand3.png")));
		atlas.put("Stand_4", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/Stand4.png")));
		
		
		//James's Sprites
		
		
		//Skyler's Sprites
		
		atlas.put("Skyler_Wood_Restaurant_Floor",ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/Skyler/floor1.png")));
		atlas.put("Skyler_Restaurant_Table",ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/Skyler/table.png")));
		atlas.put("Skyler_Waiter_Left",ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/Skyler/waiter-left.png")));
		atlas.put("Skyler_Waiter_Right",ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/Skyler/waiter-right.png")));
		atlas.put("Skyler_Waiter_Up",ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/Skyler/waiter-up.png")));
		atlas.put("Skyler_Waiter_Down",ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/Skyler/waiter-down.png")));
		atlas.put("Skyler_Counter_Vert", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/Skyler/counter-vert.png")));
		atlas.put("Skyler_Counter_Horiz", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/Skyler/counter-horiz.png")));
		atlas.put("Skyler_Kitchen_Floor", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/Skyler/kitchen-floor.png")));
		atlas.put("Skyler_Host", ImageIO.read(manager.getClass().getResourceAsStream("/data/Restaurant/Skyler/host.png")));
		
		atlas.put("Residence_Floor",ImageIO.read(manager.getClass().getResourceAsStream("/data/Housing/Residence-floor.png")));
		atlas.put("Residence_Floor2",ImageIO.read(manager.getClass().getResourceAsStream("/data/Housing/Residence-floor2.png")));
		atlas.put("Residence_Floor3",ImageIO.read(manager.getClass().getResourceAsStream("/data/Housing/Residence-floor3.png")));
		atlas.put("Apartment_Complex_Floor",ImageIO.read(manager.getClass().getResourceAsStream("/data/Housing/Apartment-floor.png")));
		atlas.put("Apartment_Complex_Floor2",ImageIO.read(manager.getClass().getResourceAsStream("/data/Housing/Apartment-floor2.png")));
		atlas.put("Apartment_Complex_Floor3",ImageIO.read(manager.getClass().getResourceAsStream("/data/Housing/Apartment-floor3.png")));
		atlas.put("Residence_Fridge",ImageIO.read(manager.getClass().getResourceAsStream("/data/Housing/Fridge.png")));
		atlas.put("Residence_Dining_Table",ImageIO.read(manager.getClass().getResourceAsStream("/data/Housing/Dining-table.png")));
		atlas.put("Residence_Dining_Table_Blank",ImageIO.read(manager.getClass().getResourceAsStream("/data/Housing/Dining-table-blank.png")));
		atlas.put("Residence_Bed",ImageIO.read(manager.getClass().getResourceAsStream("/data/Housing/Bed.png")));
		atlas.put("Residence_Couch",ImageIO.read(manager.getClass().getResourceAsStream("/data/Housing/Couch.png")));
		atlas.put("Apartment_Complex_Desk",ImageIO.read(manager.getClass().getResourceAsStream("/data/Housing/Desk.png")));
		atlas.put("Apartment_Complex_Chair",ImageIO.read(manager.getClass().getResourceAsStream("/data/Housing/Chair-up.png")));
		atlas.put("Resident_Sleeping",ImageIO.read(manager.getClass().getResourceAsStream("/data/Housing/Resident-sleeping.png")));
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
