package com.virtualparadigm.openlcs;

import java.io.Serializable;
import java.util.Comparator;

public class CoordinateComparator implements Comparator<Coordinate>, Serializable
{
	private static final long serialVersionUID = 1L;

	@Override
	public int compare(Coordinate c1, Coordinate c2)
	{
		int result = 0;
		if(c1.getX() < c2.getX())
		{
			if(c1.getY() <= c2.getY())
			{
				result = -1;
			}
			else
			{
				// c1.getY() > c2.getY()
				throw new OverlapException("coordinates overlap. c1=" + c1.toString() + " c2=" + c2.toString());
			}
		}
		else if(c1.getX() > c2.getX())
		{
			if(c1.getY() >= c2.getY())
			{
				result = 1;
			}
			else
			{
				// c1.getY() < c2.getY()
				throw new OverlapException("coordinates overlap. c1=" + c1.toString() + " c2=" + c2.toString());
			}
		}
		else
		{
			if(c1.getY() <= c2.getY())
			{
				result = -1;
			}
			else
			{
				result = 0;
			}
		}
		return result;
	}
	
	
	
	
}