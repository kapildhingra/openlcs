package com.virtualparadigm.openlcs;

import java.io.File;
import java.util.List;

public class LCS
{
	public static <T extends Comparable<T>> List<LCSElement<T>[]> compute(LCSElement<T>[] xSequence, LCSElement<T>[] ySequence, boolean debug)
	{
		List<LCSElement<T>[]> solutions = null;
		if(xSequence != null && ySequence != null)
		{
			LCSGrid<T> lcsGrid = LCSProcessor.evaluateGrid(xSequence, ySequence);
			if(lcsGrid != null)
			{
				if(debug)
				{
					lcsGrid.printGrid(new File("debug-grid.csv"));
				}
				solutions = LCSProcessor.backtrackGrid(lcsGrid);
			}
		}
		return solutions;
	}
}