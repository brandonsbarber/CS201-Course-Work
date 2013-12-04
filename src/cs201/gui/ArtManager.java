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
		
		//Ben's Sprites
		
		
		//Matt's Sprites
		
		
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
