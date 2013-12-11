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
		
		atlas.put("Car_Empty_Up", ImageIO.read(new File("data/TransitSprites/Car_Empty_Up.png")));
		atlas.put("Car_Empty_Down", ImageIO.read(new File("data/TransitSprites/Car_Empty_Down.png")));
		atlas.put("Car_Empty_Left", ImageIO.read(new File("data/TransitSprites/Car_Empty_Left.png")));
		atlas.put("Car_Empty_Right", ImageIO.read(new File("data/TransitSprites/Car_Empty_Right.png")));
		
		atlas.put("Car_Occupied_Up", ImageIO.read(new File("data/TransitSprites/Car_Occupied_Up.png")));
		atlas.put("Car_Occupied_Down", ImageIO.read(new File("data/TransitSprites/Car_Occupied_Down.png")));
		atlas.put("Car_Occupied_Left", ImageIO.read(new File("data/TransitSprites/Car_Occupied_Left.png")));
		atlas.put("Car_Occupied_Right", ImageIO.read(new File("data/TransitSprites/Car_Occupied_Right.png")));
		
		atlas.put("Restaurant_Brandon_Open",ImageIO.read(new File("data/Restaurant/Brandon/RestaurantBrandonOpen.png")));
		atlas.put("Restaurant_Brandon_Closed",ImageIO.read(new File("data/Restaurant/Brandon/RestaurantBrandonClosed.png")));
		atlas.put("Restaurant_Brandon_Floor",ImageIO.read(new File("data/Restaurant/Brandon/RestaurantBrandonFloor.png")));
		atlas.put("Restaurant_Brandon_Table",ImageIO.read(new File("data/Restaurant/Brandon/RestaurantBrandonTable.png")));
		
		atlas.put("Restaurant_Brandon_Grill_Open",ImageIO.read(new File("data/Restaurant/Brandon/RestaurantBrandonGrillEmpty.png")));
		atlas.put("Restaurant_Brandon_Grill_Filled",ImageIO.read(new File("data/Restaurant/Brandon/RestaurantBrandonGrillFilled.png")));
		atlas.put("Restaurant_Brandon_Pokeball",ImageIO.read(new File("data/Restaurant/Brandon/RestaurantBrandonPokeball.png")));
		atlas.put("Restaurant_Brandon_Kitchen_Counter",ImageIO.read(new File("data/Restaurant/Brandon/RestaurantBrandonKitchenCounter.png")));
		atlas.put("Restaurant_Brandon_Kitchen_Counter_Top",ImageIO.read(new File("data/Restaurant/Brandon/RestaurantBrandonKitchenCounterTop.png")));
		atlas.put("Restaurant_Brandon_Kitchen_Counter_Bottom",ImageIO.read(new File("data/Restaurant/Brandon/RestaurantBrandonKitchenCounterBottom.png")));
		atlas.put("Restaurant_Brandon_Cash_Register",ImageIO.read(new File("data/Restaurant/Brandon/RestaurantBrandonCashRegister.png")));
		atlas.put("Restaurant_Brandon_Rug",ImageIO.read(new File("data/Restaurant/Brandon/RestaurantBrandonRug.png")));
		
		atlas.put("Grass_Tile",ImageIO.read(new File("data/TransitSprites/Tiles/GrassTile.png")));
		atlas.put("Sidewalk_Tile",ImageIO.read(new File("data/TransitSprites/Tiles/SidewalkTile.png")));
		atlas.put("Road_Tile",ImageIO.read(new File("data/TransitSprites/Tiles/RoadTile.png")));
		
		
		
		//Ben's Sprites
		atlas.put("Market_Floor", ImageIO.read(new File("data/Market/Floor.png")));
		atlas.put("Market_Front_Desk", ImageIO.read(new File("data/Market/Front_Desk.png")));
		atlas.put("Market_Shelf", ImageIO.read(new File("data/Market/Shelf.png")));
		atlas.put("Market_Shelf_Top", ImageIO.read(new File("data/Market/Shelf_Top.png")));
		atlas.put("Market_Shelf_Bottom_Left", ImageIO.read(new File("data/Market/Shelf_Bottom_Left.png")));
		atlas.put("Market_Shelf_Bottom_Right", ImageIO.read(new File("data/Market/Shelf_Bottom_Right.png")));
		
		atlas.put("Market_Employee_Up", ImageIO.read(new File("data/Market/Employee_North.png")));
		atlas.put("Market_Employee_Down", ImageIO.read(new File("data/Market/Employee_South.png")));
		atlas.put("Market_Employee_Left", ImageIO.read(new File("data/Market/Employee_West.png")));
		atlas.put("Market_Employee_Right", ImageIO.read(new File("data/Market/Employee_East.png")));
		atlas.put("Market_Manager_Down", ImageIO.read(new File("data/Market/Manager_South.png")));
		
		atlas.put("Restaurant_Ben_Floor", ImageIO.read(new File("data/Restaurant/Ben/Floor.png")));
		atlas.put("Restaurant_Ben_Table", ImageIO.read(new File("data/Restaurant/Ben/Table.png")));
		atlas.put("Restaurant_Ben_Waiter_Up", ImageIO.read(new File("data/Restaurant/Ben/Waiter_North.png")));
		atlas.put("Restaurant_Ben_Waiter_Down", ImageIO.read(new File("data/Restaurant/Ben/Waiter_South.png")));
		atlas.put("Restaurant_Ben_Waiter_Left", ImageIO.read(new File("data/Restaurant/Ben/Waiter_West.png")));
		atlas.put("Restaurant_Ben_Waiter_Right", ImageIO.read(new File("data/Restaurant/Ben/Waiter_East.png")));
		
		atlas.put("Market_Car", ImageIO.read(new File("data/Market/Car.png")));
		
		//Matt's Sprites
		atlas.put("Default_Walker_Up", ImageIO.read(new File("data/DefaultSprites/Up.png")));
		atlas.put("Default_Walker_Down", ImageIO.read(new File("data/DefaultSprites/Down.png")));
		atlas.put("Default_Walker_Left", ImageIO.read(new File("data/DefaultSprites/Left.png")));
		atlas.put("Default_Walker_Right", ImageIO.read(new File("data/DefaultSprites/Right.png")));
		
		atlas.put("Restaurant_Matt_Open", ImageIO.read(new File("data/Restaurant/RestaurantMattOpen.png")));
		atlas.put("Restaurant_Matt_Closed", ImageIO.read(new File("data/Restaurant/RestaurantMattClosed.png")));
		
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
		
		atlas.put("Cook_Up", ImageIO.read(new File("data/Restaurant/CookUp.png")));
		atlas.put("Cook_Down", ImageIO.read(new File("data/Restaurant/CookDown.png")));
		atlas.put("Cook_Left", ImageIO.read(new File("data/Restaurant/CookLeft.png")));
		atlas.put("Cook_Right", ImageIO.read(new File("data/Restaurant/CookRight.png")));
		
		atlas.put("Kitchen", ImageIO.read(new File("data/Restaurant/Kitchen.png")));
		atlas.put("Table", ImageIO.read(new File("data/Restaurant/Table.png")));
		atlas.put("Cash_Register", ImageIO.read(new File("data/Restaurant/CashRegister.png")));
		atlas.put("Floor", ImageIO.read(new File("data/Restaurant/Floor.png")));
		
		atlas.put("Stand_0", ImageIO.read(new File("data/Restaurant/Stand0.png")));
		atlas.put("Stand_1", ImageIO.read(new File("data/Restaurant/Stand1.png")));
		atlas.put("Stand_2", ImageIO.read(new File("data/Restaurant/Stand2.png")));
		atlas.put("Stand_3", ImageIO.read(new File("data/Restaurant/Stand3.png")));
		atlas.put("Stand_4", ImageIO.read(new File("data/Restaurant/Stand4.png")));
		
		
		//James's Sprites
		
		
		//Skyler's Sprites
		
		atlas.put("Skyler_Wood_Restaurant_Floor",ImageIO.read(new File("data/Restaurant/Skyler/floor1.png")));
		atlas.put("Skyler_Restaurant_Table",ImageIO.read(new File("data/Restaurant/Skyler/table.png")));
		atlas.put("Skyler_Waiter_Left",ImageIO.read(new File("data/Restaurant/Skyler/waiter-left.png")));
		atlas.put("Skyler_Waiter_Right",ImageIO.read(new File("data/Restaurant/Skyler/waiter-right.png")));
		atlas.put("Skyler_Waiter_Up",ImageIO.read(new File("data/Restaurant/Skyler/waiter-up.png")));
		atlas.put("Skyler_Waiter_Down",ImageIO.read(new File("data/Restaurant/Skyler/waiter-down.png")));
		atlas.put("Skyler_Counter_Vert", ImageIO.read(new File("data/Restaurant/Skyler/counter-vert.png")));
		atlas.put("Skyler_Counter_Horiz", ImageIO.read(new File("data/Restaurant/Skyler/counter-horiz.png")));
		atlas.put("Skyler_Kitchen_Floor", ImageIO.read(new File("data/Restaurant/Skyler/kitchen-floor.png")));
		
		atlas.put("Residence_Floor",ImageIO.read(new File("data/Housing/Residence-floor.png")));
		atlas.put("Residence_Floor2",ImageIO.read(new File("data/Housing/Residence-floor2.png")));
		atlas.put("Residence_Floor3",ImageIO.read(new File("data/Housing/Residence-floor3.png")));
		atlas.put("Apartment_Complex_Floor",ImageIO.read(new File("data/Housing/Apartment-floor.png")));
		atlas.put("Apartment_Complex_Floor2",ImageIO.read(new File("data/Housing/Apartment-floor2.png")));
		atlas.put("Apartment_Complex_Floor3",ImageIO.read(new File("data/Housing/Apartment-floor3.png")));
		atlas.put("Residence_Fridge",ImageIO.read(new File("data/Housing/Fridge.png")));
		atlas.put("Residence_Dining_Table",ImageIO.read(new File("data/Housing/Dining-table.png")));
		atlas.put("Residence_Dining_Table_Blank",ImageIO.read(new File("data/Housing/Dining-table-blank.png")));
		atlas.put("Residence_Bed",ImageIO.read(new File("data/Housing/Bed.png")));
		atlas.put("Residence_Couch",ImageIO.read(new File("data/Housing/Couch.png")));
		atlas.put("Apartment_Complex_Desk",ImageIO.read(new File("data/Housing/Desk.png")));
		atlas.put("Apartment_Complex_Chair",ImageIO.read(new File("data/Housing/Chair-up.png")));
		atlas.put("Resident_Sleeping",ImageIO.read(new File("data/Housing/Resident-sleeping.png")));
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
