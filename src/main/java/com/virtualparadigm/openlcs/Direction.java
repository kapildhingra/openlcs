package com.virtualparadigm.openlcs;

import java.util.HashMap;
import java.util.Map;

public enum Direction
{
	NONE, NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST;
	
	private static Map<String, Direction> directionMap = null;
	static
	{
		directionMap = new HashMap<String, Direction>();
		directionMap.put(getKey(0,0), NONE);
		directionMap.put(getKey(0,1), NORTH);
		directionMap.put(getKey(1,1), NORTH_EAST);
		directionMap.put(getKey(1,0), EAST);
		directionMap.put(getKey(1,-1), SOUTH_EAST);
		directionMap.put(getKey(0,-1), SOUTH);
		directionMap.put(getKey(-1,-1), SOUTH_WEST);
		directionMap.put(getKey(-1,0), WEST);
		directionMap.put(getKey(-1,1), NORTH_WEST);
	}
	
	public static Direction getDirection(int x, int y)
	{
		return directionMap.get(getKey(((x == 0) ? 0 : x/Math.abs(x)),((y == 0) ? 0 : y/Math.abs(y))));
	}
	
	public static Direction getDirection(int fromX, int fromY, int toX, int toY)
	{
		Direction d = getDirection((toX-fromX), (toY-fromY));
		return d;
//		return getDirection((toX-fromX), (toY-fromY));
	}
	
	private static String getKey(int x, int y)
	{
		return x + "_" + y;
	}
	
	
	public static void main(String[] args)
	{
		System.out.println(Direction.getDirection(0,0,10,1));
		
		
		
//		System.out.println(Direction.getDirection(10,1));
//		System.out.println(Direction.getDirection(1,-10));
//		System.out.println(Direction.getDirection(-19,-143));
//		System.out.println(Direction.getDirection(-31,81));
		
		
	}
	
}