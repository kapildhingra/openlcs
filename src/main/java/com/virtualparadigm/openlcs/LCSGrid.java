package com.virtualparadigm.openlcs;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class LCSGrid<T extends Comparable<T>> // "is a" some kind of decision matrix?
{
//	private Class<T> tClass;
	private LCSElement<T>[] xSequence;
	private LCSElement<T>[] ySequence;
	private Matrix<LCSCell> matrix;
//	private LCSCell[][] lcsCells;
	
//	public LCSGrid(LCSElement<T>[] xSequence, LCSElement<T>[] ySequence, Class<T> tClass)
	public LCSGrid(LCSElement<T>[] xSequence, LCSElement<T>[] ySequence)
	{
		this.xSequence = xSequence;
		this.ySequence = ySequence;
		
		if(xSequence != null && ySequence != null)
		{
			this.matrix = new Matrix<LCSCell>(xSequence.length, ySequence.length, LCSCell.class);
			LCSMatrixUtil.fillCells(matrix, null, 0);
		}
	}
	
	public LCSGrid(LCSElement<T>[] xSequence, LCSElement<T>[] ySequence, InheritDirection inheritDirection, int value)
	{
		this.xSequence = xSequence;
		this.ySequence = ySequence;
		
		if(xSequence != null && ySequence != null)
		{
			this.matrix = new Matrix<LCSCell>(xSequence.length, ySequence.length, LCSCell.class);
			LCSMatrixUtil.fillCells(matrix, inheritDirection, value);
		}
	}
	
//	public LCSGrid(LCSElement<T>[] xSequence, LCSElement<T>[] ySequence, Matrix<LCSCell> matrix, Class<T> tClass)
	public LCSGrid(LCSElement<T>[] xSequence, LCSElement<T>[] ySequence, Matrix<LCSCell> matrix)
	{
		if(matrix == null || xSequence == null || ySequence == null || xSequence.length != matrix.getXSize() || ySequence.length != matrix.getYSize())
		{
			throw new InitializationException("size incompatibility between xSequence/ySequence and matrix sizes");
		}
		
//		this.tClass = tClass;
		this.xSequence = xSequence;
		this.ySequence = ySequence;
		this.matrix = matrix;
	}
	
	public LCSElement<T> getXElement(int index)
	{
		return this.xSequence[index];
	}
	
	public LCSElement<T> getYElement(int index)
	{
		return this.ySequence[index];
	}
	
	public LCSCell getCell(int x, int y)
	{
		LCSCell retCell = null;
		LCSCell lcsCell = this.matrix.getCell(x, y);
		if(lcsCell != null)
		{
			try
			{
				retCell = (LCSCell)lcsCell.clone();
			}
			catch(CloneNotSupportedException cnse)
			{
			}
		}
		return retCell;
	}
	public LCSCell getCell(Coordinate coordinate)
	{
		LCSCell lcsCell = null;
		if(coordinate != null)
		{
			lcsCell = this.getCell(coordinate.getX(), coordinate.getY());
		}
		return lcsCell;
	}
	public LCSCell[] getCells(Coordinate[] coordinates)
	{
		List<LCSCell> lcsCellList = new ArrayList<LCSCell>();
		if(coordinates != null)
		{
			LCSCell lcsCell = null;
			for(Coordinate coordinate : coordinates)
			{
				lcsCell = this.getCell(coordinate);
				if(lcsCell != null)
				{
					try
					{
						lcsCellList.add((LCSCell)lcsCell.clone());
					}
					catch(CloneNotSupportedException cnse)
					{
					}
//					lcsCellList.add(lcsCell);
				}
			}
		}
		return lcsCellList.toArray(new LCSCell[lcsCellList.size()]);
	}
	
	public LCSCell[] getCells(Collection<Coordinate> coordinateList)
	{
		List<LCSCell> lcsCellList = new ArrayList<LCSCell>();
		if(coordinateList != null)
		{
			LCSCell lcsCell = null;
			for(Coordinate coordinate : coordinateList)
			{
				lcsCell = this.getCell(coordinate);
				if(lcsCell != null)
				{
					try
					{
						lcsCellList.add((LCSCell)lcsCell.clone());
					}
					catch(CloneNotSupportedException cnse)
					{
					}
				}
			}
		}
		return (lcsCellList.size() > 0) ? lcsCellList.toArray(new LCSCell[lcsCellList.size()]) : null;
	}
	
//	public void setCell(int x, int y, LCSCell lcsCell)
	public void setCell(int x, int y, InheritDirection inheritDirection, int value)
	{
		this.matrix.setCell(x, y, new LCSCell(inheritDirection, value, new Coordinate(x,y)));
	}
	
	public int getSizeX()
	{
		int result = 0;
		if(this.xSequence != null)
		{
			result = this.xSequence.length;
		}
		return result;
	}
	
	public int getSizeY()
	{
		int result = 0;
		if(this.ySequence != null)
		{
			result = this.ySequence.length;
		}
		return result;
	}
	
	public boolean isWthinBounds(Coordinate[] coordinates)
	{
		boolean status = false;
		if(coordinates != null)
		{
			status = true;
			for(Coordinate coordinate : coordinates)
			{
				status &= this.isWthinBounds(coordinate);
			}
		}
		return status;
	}
	public boolean isWthinBounds(Coordinate coordinate)
	{
		boolean status = false;
		if(coordinate != null)
		{
			status = this.isWthinBounds(coordinate.getX(), coordinate.getY());
		}
		return status;
	}
	public boolean isWthinBounds(int x, int y)
	{
		return (x >= 0 && x < this.xSequence.length && y >= 0 && y <this.ySequence.length);
	}
	public boolean isWthinXBounds(int x)
	{
		return (x >= 0 && x < this.xSequence.length);
	}
	public boolean isWthinYBounds(int y)
	{
		return (y >= 0 && y <this.ySequence.length);
	}	
	
	
	
	public LCSGrid<T>[] split(Coordinate[] coordinates)
	{
		List<LCSGrid<T>> lcsGridList = new ArrayList<LCSGrid<T>>();
		if(coordinates != null)
		{
			try
			{
				Arrays.sort(coordinates, new CoordinateComparator());
				
				// if overlaps exist in the coordinates, then we wont get here due to 
				// runtime overlap exception thrown by CoordinateComparator
				
				Matrix<LCSCell> lcsMatrix = null;
				Coordinate prevCoordinate = new Coordinate(0,0);
				for(int i=0; i<coordinates.length; i++)
				{
					lcsMatrix = this.matrix.getSubMatrix(prevCoordinate, coordinates[i]);
					if(lcsMatrix != null)
					{
						lcsGridList.add(
								new LCSGrid<T>(
										Arrays.copyOfRange(this.xSequence, prevCoordinate.getX(), coordinates[i].getX()+1), 
										Arrays.copyOfRange(this.ySequence, prevCoordinate.getY(), coordinates[i].getY()+1), 
										lcsMatrix));
					}
//					coordinates[i].incrementX();
//					coordinates[i].incrementY();
//					prevCoordinate = coordinates[i];
					prevCoordinate = new Coordinate(coordinates[i].getX()+1, coordinates[i].getY()+1);
				}
				Coordinate lastCoordinate = new Coordinate(this.matrix.getXSize()-1, this.matrix.getYSize()-1);
				if(prevCoordinate.getX() <= lastCoordinate.getX() && prevCoordinate.getY() <= lastCoordinate.getY())
				{
					lcsMatrix = this.matrix.getSubMatrix(prevCoordinate, lastCoordinate);
					lcsGridList.add(
							new LCSGrid<T>(
									Arrays.copyOfRange(this.xSequence, prevCoordinate.getX(), lastCoordinate.getX()+1), 
									Arrays.copyOfRange(this.ySequence, prevCoordinate.getY(), lastCoordinate.getY()+1), 
									lcsMatrix));
				}
			}
			catch(OverlapException oe)
			{
			}
		}
		return (LCSGrid<T>[])lcsGridList.toArray(new LCSGrid[lcsGridList.size()]);
	}
	
//	public static LCSGrid merge(LCSGrid[] lcsGrids)
//	{
//		LCSGrid lcsGrid = null;
//		
//		return lcsGrid;
//	}

	public void printGrid(int cellSize)
	{
		StringBuffer strRow = new StringBuffer();
//		StringBuffer row = new StringBuffer();
		String[] rowOutput = new String[this.xSequence.length + 1];

		strRow.append("%"+cellSize+"s");
		rowOutput[0] = " ";
		for(int x=0; x<this.xSequence.length; x++)
		{
			strRow.append("%"+cellSize+"s");
			rowOutput[x+1] = this.xSequence[x].asString();
		}
		System.out.printf(strRow.toString() + "\n", (Object[])rowOutput);
		strRow.setLength(0);
		
		for(int y=0; y<ySequence.length; y++)
		{
			strRow.append("%"+cellSize+"s");
			rowOutput[0] = this.ySequence[y].asString();
			for(int x=0; x<this.xSequence.length; x++)
			{
				strRow.append("%"+cellSize+"s");
				rowOutput[x+1] = LCSMatrixUtil.getLCSCellString(this.matrix.getCell(x, y), false);
			}
			System.out.printf(strRow.toString() + "\n", (Object[])rowOutput);
			strRow.setLength(0);
		}
	}	
	
	public void printGrid(File file)
	{
		StringBuffer strRow = new StringBuffer();
		try
		{
			for(int x=0; x<this.xSequence.length; x++)
			{
				strRow.append("," + this.xSequence[x].asString());
			}
			strRow.append("\n");
			FileUtils.writeStringToFile(file, strRow.toString(), true);
			
			for(int y=0; y<ySequence.length; y++)
			{
				strRow.setLength(0);
				strRow.append(this.ySequence[y].asString());
				for(int x=0; x<this.xSequence.length; x++)
				{
					strRow.append("," + LCSMatrixUtil.getLCSCellString(this.matrix.getCell(x, y), false));
				}
				strRow.append("\n");
				FileUtils.writeStringToFile(file, strRow.toString(), true);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}	
	
	
	public static void main(String[] args)
	{
		
//		LCSGrid<String> lcsGrid = LCSGrid.createTraversalTestGrid();
//		
//		lcsGrid.printGrid(10);
//		System.out.println("");
//		System.out.println("");
		
//		LCSGrid<String>[] splitGrids = lcsGrid.split(new Coordinate[]{new Coordinate(4,3)});
//		if(splitGrids != null)
//		{
//			for(LCSGrid<String> splitGrid : splitGrids)
//			{
//				splitGrid.printGrid(10);
//				System.out.println("");
//			}
//			System.out.println("");
//		}
		
//		LCSElement<T>[] xSequence, LCSElement<T>[] ySequence
		
		
		
//		Coordinate[] coordinates = new Coordinate[]{new Coordinate(0, 0), new Coordinate(2, 2), new Coordinate(1, 1), new Coordinate(3, 1)};
	}
	
	public static void printCoordinates(Coordinate[] coordinates)
	{
		if(coordinates != null)
		{
			for(Coordinate coordinate : coordinates)
			{
				System.out.println(coordinate.toString());
			}
		}
	}
	
	
	
	private static void testSplit()
	{
		LCSGrid<String> lcsGrid = 
				new LCSGrid<String>(
						LCSStringElement.createCharSequence("banana"), 
						LCSStringElement.createCharSequence("atana"));
		
		lcsGrid.printGrid(10);
		System.out.println("");
		System.out.println("");
		
		LCSGrid<String>[] splitGrids = lcsGrid.split(new Coordinate[]{new Coordinate(4,3)});
		if(splitGrids != null)
		{
			for(LCSGrid<String> splitGrid : splitGrids)
			{
				splitGrid.printGrid(10);
				System.out.println("");
			}
			System.out.println("");
		}
		
	}
	
}