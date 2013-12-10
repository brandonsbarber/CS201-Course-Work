package cs201.helper.transit;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import cs201.helper.Constants;

public class MapParser
{
	public static String[][] parseMap() throws FileNotFoundException
	{
		Scanner in = new Scanner(new File(Constants.MAP_PATH),"UTF-16");
		System.out.println(in.hasNext());
		
		File f = new File(Constants.MAP_PATH);
		System.out.println(f.getAbsolutePath());
		System.out.println(f.canRead());
		System.out.println(f.isFile());
		System.out.println(f.length());
		
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
				System.out.printf("%3s",contents);
				map[y][x] = contents;
			}
			if(in.hasNextLine())
			{
				in.nextLine();
				System.out.println();
			}
		}
		
		in.close();
		
		for(int y = 0; y < yDimen; y++)
		{
			for(int x = 0; x < xDimen; x++)
			{
				System.out.printf("%3s",map[y][x]);
			}
			System.out.println();
		}
		
		return map;
	}
}
