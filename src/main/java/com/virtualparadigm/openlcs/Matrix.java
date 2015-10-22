package com.virtualparadigm.openlcs;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Matrix<T>
{
	private Class<T> tClass;
	private Object[][] cells;
	
	public Matrix(int xsize, int ysize, Class<T> tClass)
	{
		this.cells = new Object[xsize][ysize];
		this.tClass = tClass;
	}
	
	public Matrix(T[][] cells, Class<T> tClass)
	{
		this.cells = cells;
		this.tClass = tClass;
	}
	
	public T getCell(int x, int y)
	{
		T cell = null;
		if(this.cells != null && this.isWthinBounds(x, y))
		{
			cell = (T)cells[x][y];
		}
		return cell;
	}
	
	public void setCell(int x, int y, T cell)
	{
		if(this.cells != null && this.isWthinBounds(x, y))
		{
			cells[x][y] = cell;
		}
	}
	
	public T[] getColumn(int x)
	{
		T[] lcsColumn = null;
		if(this.cells != null && this.isWthinXBounds(x))
		{
			lcsColumn = (T[])cells[x];
		}
		return lcsColumn;
	}
	public void setColumn(int x, T cell)
	{
		if(this.cells != null && this.isWthinXBounds(x))
		{
			for(int i=0; i<this.cells[x].length; i++)
			{
				cells[x][i] = cell;
			}
		}
	}
	public int getXSize()
	{
		int size = 0;
		if(this.cells != null)
		{
			size = this.cells.length;
		}
		return size;
	}

	public T[] getRow(int y)
	{
		T[] row = null;
		if(this.cells != null && this.isWthinYBounds(y))
		{
			row = (T[])Array.newInstance(tClass, cells.length);
			for(int i=0; i<cells.length; i++)
			{
				row[i] = (T)this.cells[i][y];
			}
		}
		return row;
	}
	public void setRow(int y, T cell)
	{
		if(this.cells != null && this.isWthinYBounds(y))
		{
			for(int i=0; i<this.cells[0].length; i++)
			{
				cells[i][y] = cell;
			}
		}
	}
	public int getYSize()
	{
		int size = 0;
		if(this.cells != null && this.cells[0] != null)
		{
			size = this.cells[0].length;
		}
		return size;
	}
	
	//inclusive for both from and to
	public T[][] getCells(Coordinate from, Coordinate to)
	{
		Object[][] result = null;
		if(this.isWthinBounds(new Coordinate[]{from, to}))
		{
			result = new Object[to.getX()-from.getX()+1][to.getY()-from.getY()+1];
			for(int x=0; x<=(to.getX()-from.getX()); x++)
			{
				for(int y=0; y<=(to.getY()-from.getY()); y++)
				{
					result[x][y] = this.getCell(from.getX()+x, from.getY()+y);
				}
			}
		}
		return (T[][])result;
	}
	
	public T[][] getCells()
	{
		return this.getCells(new Coordinate(0,0), new Coordinate(this.getXSize()-1,this.getYSize()-1));
	}
	
	
	public Matrix<T>[] split(Coordinate[] coordinates)
	{
		List<Matrix<T>> matrixList = new ArrayList<Matrix<T>>();
		if(coordinates == null)
		{
			matrixList.add(this);
		}
		else
		{
			Arrays.sort(coordinates, new CoordinateComparator());
			// if overlaps exist in the coordinates, then we wont get here due to 
			// runtime overlap exception thrown by CoordinateComparator
			
			if(this.isWthinBounds(coordinates))
			{
				Coordinate prevCoordinate = new Coordinate(0, 0);
				for(int i=0; i<coordinates.length; i++)
				{
					if(prevCoordinate.getX() < coordinates[i].getX() && prevCoordinate.getY() < coordinates[i].getY())
					{
						matrixList.add(new Matrix<T>(this.getCells(prevCoordinate, coordinates[i]), this.tClass));
					}
//					coordinates[i].incrementX();
//					coordinates[i].incrementY();
//					prevCoordinate = coordinates[i];
					prevCoordinate = new Coordinate(coordinates[i].getX()+1, coordinates[i].getY()+1);
				}
				Coordinate lastCoordinate = new Coordinate(this.cells.length-1, this.cells[0].length-1);
				if(prevCoordinate.getX() <= lastCoordinate.getX() && prevCoordinate.getY() <= lastCoordinate.getY())
				{
					matrixList.add(new Matrix<T>(this.getCells(prevCoordinate, lastCoordinate), tClass));
				}
			}
		}
		System.out.println("matrix length=" + matrixList.size());
		return (Matrix<T>[])matrixList.toArray(new Matrix[matrixList.size()]);
	}
	
	public Matrix<T> getSubMatrix(Coordinate from, Coordinate to)
	{
		Matrix<T> result = null;
		T[][] cells = this.getCells(from, to);
		if(cells != null)
		{
			result = new Matrix<T>(this.getCells(from, to), tClass);
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
		return (x >= 0 && x < this.cells.length && y >= 0 && y <this.cells[0].length);
	}
	public boolean isWthinXBounds(int x)
	{
		return (x >= 0 && x < this.cells.length);
	}
	public boolean isWthinYBounds(int y)
	{
		return (y >= 0 && y <this.cells[0].length);
	}
	
	
	public void printMatrix(int cellSize)
	{
		StringBuffer row = new StringBuffer();
		String[] rowOutput = new String[this.getXSize()];
		for(int y=0; y<this.getYSize(); y++)
		{
			for(int x=0; x<this.getXSize(); x++)
			{
				row.append("%"+cellSize+"s");
				if(this.getCell(x, y) != null)
				{
					rowOutput[x] = this.getCell(x,y).toString();
				}
				else
				{
					rowOutput[x] = "nil";
				}
			}
			System.out.printf(row.toString() + "\n", (Object[])rowOutput);
			row.setLength(0);
		}
		
//		if(this.cells != null)
//		{
//			String[] rowOutput = new String[this.cells.length];
//			for(int y=0; y<this.cells[0].length; y++)
//			{
//				for(int x=0; x<this.cells.length; x++)
//				{
//					row.append("%"+cellSize+"s");
//					if(cells[x][y] != null)
//					{
//						rowOutput[x] = cells[x][y].toString();
//					}
//					else
//					{
//						rowOutput[x] = "nil";
//					}
//				}
//				System.out.printf(row.toString() + "\n", (Object[])rowOutput);
//				row.setLength(0);
//			}
//		}
	}
	
	
	public static void main(String[] args)
	{
		
		Matrix<Integer> m1 = new Matrix<Integer>(10, 10, Integer.class);
		int count = 0;
		for(int i=0; i<10; i++)
		{
			for(int j=0; j<10; j++)
			{
				m1.setCell(i, j, count++);
			}
			
		}
		m1.printMatrix(5);
		
		System.out.println("");
		
		
//		LCSMatrix[] lcsMatrices = m1.split(new Coordinate[]{new Coordinate(0,0), new Coordinate(2,9)});
//		Matrix<Integer>[] lcsMatrices = m1.split(new Coordinate[]{new Coordinate(2,3), new Coordinate(6,7)});
//		Matrix<Integer>[] lcsMatrices = m1.split(new Coordinate[]{new Coordinate(0,0)});
//		Matrix<Integer>[] lcsMatrices = m1.split(new Coordinate[]{new Coordinate(10,10)});
//		Matrix<Integer>[] lcsMatrices = m1.split(new Coordinate[]{new Coordinate(9,9)});
		Matrix<Integer>[] lcsMatrices = m1.split(new Coordinate[]{new Coordinate(8,8)});
//		Matrix<Integer>[] lcsMatrices = m1.split(new Coordinate[]{new Coordinate(9,9), new Coordinate(8,8)});
//		Matrix<Integer>[] lcsMatrices = m1.split(new Coordinate[]{});
		for(int i=0; i<lcsMatrices.length; i++)
		{
			lcsMatrices[i].printMatrix(5);
			System.out.println("");
		}
		
//		LCSMatrix m2 = new LCSMatrix(m1.getCells(new Coordinate(0,0), new Coordinate(2,4)));
//		m2.printMatrix(5);
		
	}
		
	
}