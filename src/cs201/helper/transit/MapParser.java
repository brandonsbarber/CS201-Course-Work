package cs201.helper.transit;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import cs201.helper.Constants;

public class MapParser
{
	public static String[][] parseMap() throws FileNotFoundException
	{
		MapParser parser = new MapParser();
		
		Scanner in = new Scanner(parser.getClass().getResourceAsStream(Constants.MAP_PATH),"UTF-16");
		System.out.println(in.hasNext());
		
		int xDimen = in.nextInt();
		int yDimen = in.nextInt();
		//clear buffer
		in.nextLine();
		
		String[][] map = new String[yDimen][xDimen];
		
		for(int y = 0; y < yDimen; y++)
		{
			for(int x = 0; x < xDimen; x++)
			{
				String contents = in.next();
				map[y][x] = contents;
			}
			if(in.hasNextLine())
			{
				in.nextLine();
			}
		}
		
		in.close();
		
		return map;
	}
}
