package com.virtualparadigm.openlcs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LCSGridWalker
{
	private LCSGrid<?> lcsGrid;
	private Coordinate startCoordinate;
	private Set<CoordinateFrame> currentCoordinateFrameSet;
	

	public static void main(String[] args)
	{
		LCSGrid<String> lcsGrid = createTraversalTestGrid2();
		lcsGrid.printGrid(10);
		System.out.println("");
		System.out.println("");
		LCSGridWalker gridWalker = new LCSGridWalker(lcsGrid, new Coordinate(4,4));
		LCSCell[] lcsCells = null;
		while((lcsCells=gridWalker.nextCells()) != null)
		{
			printLCSCells(lcsCells);
		}
		System.out.println("");
	}
	
	public LCSGridWalker(LCSGrid<?> lcsGrid, Coordinate startCoordinate)
	{
		this.lcsGrid = lcsGrid;
		//use copy constructor
		this.startCoordinate = new Coordinate(startCoordinate);
		this.currentCoordinateFrameSet = new HashSet<CoordinateFrame>();
		this.currentCoordinateFrameSet.clear();
	}
	
	public LCSGridWalker(LCSGrid<?> lcsGrid)
	{
		this.lcsGrid = lcsGrid;
		this.startCoordinate = new Coordinate(lcsGrid.getSizeX(), lcsGrid.getSizeY());
		this.currentCoordinateFrameSet = new HashSet<CoordinateFrame>();
		this.currentCoordinateFrameSet.clear();
	}
	
	public void reset()
	{
		this.currentCoordinateFrameSet.clear();
	}
	
	public LCSCell[] nextCells()
	{
		LCSCell[] nextCells = null;
		Set<CoordinateFrame> workingCoordinateFrameSet = new HashSet<CoordinateFrame>();
		if(this.currentCoordinateFrameSet.size() > 0)
		{
			LCSCell currentLCSCell = null;
			LCSCell previousLCSCell = null;

			Coordinate workingCurrCoordinate = null;
			Coordinate workingPrevCoordinate = null;
			for(CoordinateFrame currentCoordinateFrame : this.currentCoordinateFrameSet)
			{
				// move first
				// then return the current working set
				currentLCSCell = this.lcsGrid.getCell(currentCoordinateFrame.getCurrentCoordinate());
				previousLCSCell = this.lcsGrid.getCell(currentCoordinateFrame.getPreviousCoordinate());
				
				workingPrevCoordinate = new Coordinate(currentCoordinateFrame.getCurrentCoordinate());
				
				//move current
				if(previousLCSCell != null && previousLCSCell.getInheritDirection() == InheritDirection.TOP_AND_LEFT && 
					currentLCSCell.getInheritDirection() == InheritDirection.TOP_AND_LEFT)
				{
					//go in the same direction
					Direction direction = LCSGridWalker.getMovedDirection(previousLCSCell, currentLCSCell);
					if(direction != null)
					{
						if(direction == Direction.NORTH)
						{
							workingCurrCoordinate = new Coordinate(currentCoordinateFrame.getCurrentX(), currentCoordinateFrame.getCurrentY()-1);
						}
						else if(direction == Direction.WEST)
						{
							workingCurrCoordinate = new Coordinate(currentCoordinateFrame.getCurrentX()-1, currentCoordinateFrame.getCurrentY());
						}
					}
				}
				else
				{
					switch(currentLCSCell.getInheritDirection())
					{
						case LEFT:
						{
							workingCurrCoordinate = new Coordinate(currentCoordinateFrame.getCurrentX()-1, currentCoordinateFrame.getCurrentY());
							break;
						}
						case TOP:
						{
							workingCurrCoordinate = new Coordinate(currentCoordinateFrame.getCurrentX(), currentCoordinateFrame.getCurrentY()-1);
							break;
						}
						case TOP_AND_LEFT:
						{
							//add the coordinate above to the added set
							workingCoordinateFrameSet.add(
									new CoordinateFrame(
											new Coordinate(currentCoordinateFrame.getCurrentCoordinate().getX(), currentCoordinateFrame.getCurrentCoordinate().getY()-1), 
											new Coordinate(currentCoordinateFrame.getCurrentCoordinate())));
							
							//change current to left of current
							workingCurrCoordinate = new Coordinate(currentCoordinateFrame.getCurrentX()-1, currentCoordinateFrame.getCurrentY());
							break;
						}
						case DIAGONAL:
						{
							workingCurrCoordinate = new Coordinate(currentCoordinateFrame.getCurrentX()-1, currentCoordinateFrame.getCurrentY()-1);
							break;
						}
						default:
						{
							//nothing right now
							break;
						}
					}
				}
				
				workingCoordinateFrameSet.add(new CoordinateFrame(workingCurrCoordinate, workingPrevCoordinate));
				
			} // end for
			
		}
		else 
		{
			if(this.lcsGrid != null && this.startCoordinate != null || this.lcsGrid.isWthinBounds(this.startCoordinate))
			{
				workingCoordinateFrameSet.add(new CoordinateFrame(this.startCoordinate));
			}
		}
		
		if(workingCoordinateFrameSet != null && workingCoordinateFrameSet.size() > 0)
		{
			nextCells = this.lcsGrid.getCells(LCSGridWalker.getCurrentCoordinates(workingCoordinateFrameSet));
			this.currentCoordinateFrameSet = workingCoordinateFrameSet;
		}
		
		return nextCells;
	}
	
	
	public LCSCell[] currentCells()
	{
		return this.lcsGrid.getCells(LCSGridWalker.getCurrentCoordinates(this.currentCoordinateFrameSet));
	}
	
	
	private static Collection<Coordinate> getCurrentCoordinates(Set<CoordinateFrame> coordinateFrameSet)
	{
		List<Coordinate> coordinateList = null;
		if(coordinateFrameSet != null)
		{
			coordinateList = new ArrayList<Coordinate>();
			for(CoordinateFrame coordinateFrame : coordinateFrameSet)
			{
				coordinateList.add(coordinateFrame.getCurrentCoordinate());
			}
		}
		return coordinateList;
	}
	
	
	// =====================================================
	// UTILITY METHODS
	// =====================================================
	private static Direction getMovedDirection(LCSCell from, LCSCell to)
	{
		Direction direction = null;
		if(from != null && to != null)
		{
			//NOT VERY INTUITIVE: the Y axis is actually flipped on this grid since as Y grows larger, it moves DOWN the grid.
			// we are flipping "to" and "from" for the y coordinate here for this.
			direction = Direction.getDirection(from.getXCoordinate(), to.getYCoordinate(), to.getXCoordinate(), from.getYCoordinate());
//			direction = Direction.getDirection(from.getXCoordinate(), from.getYCoordinate(), to.getXCoordinate(), to.getYCoordinate());
		}
		return direction;
	}
	
	
	public static void printCoordinates(Set<Coordinate> coordinateSet)
	{
		for(Coordinate coordinate : coordinateSet)
		{
			System.out.println(coordinate.getX() + "," + coordinate.getY());
		}
		
	}
	
	public static void printLCSCells(LCSCell[] lcsCells)
	{
		System.out.print("result: ");
		if(lcsCells != null)
		{
			for(LCSCell lcsCell : lcsCells)
			{
				System.out.print("[" + LCSMatrixUtil.getLCSCellString(lcsCell, true) + "] ");
			}
		}
		else
		{
			System.out.print("null");
		}
		System.out.print("\n");
	}
	
	
	private static LCSGrid<String> createTraversalTestGrid1()
	{
		LCSGrid<String> lcsGrid = 
				new LCSGrid<String>(
						LCSStringElement.createCharSequence("agcat"), 
						LCSStringElement.createCharSequence("gac"));
		
		lcsGrid.setCell(0, 0, InheritDirection.TOP_AND_LEFT, 0);
		lcsGrid.setCell(1, 0, InheritDirection.DIAGONAL, 1);
		lcsGrid.setCell(2, 0, InheritDirection.LEFT, 1);
		lcsGrid.setCell(3, 0, InheritDirection.LEFT, 1);
		lcsGrid.setCell(4, 0, InheritDirection.LEFT, 1);

		lcsGrid.setCell(0, 1, InheritDirection.DIAGONAL, 1);
		lcsGrid.setCell(1, 1, InheritDirection.TOP_AND_LEFT, 1);
		lcsGrid.setCell(2, 1, InheritDirection.TOP_AND_LEFT, 1);
		lcsGrid.setCell(3, 1, InheritDirection.DIAGONAL, 2);
		lcsGrid.setCell(4, 1, InheritDirection.LEFT, 2);
		
		lcsGrid.setCell(0, 2, InheritDirection.TOP, 1);
		lcsGrid.setCell(1, 2, InheritDirection.TOP_AND_LEFT, 1);
		lcsGrid.setCell(2, 2, InheritDirection.DIAGONAL, 2);
		lcsGrid.setCell(3, 2, InheritDirection.TOP_AND_LEFT, 2);
		lcsGrid.setCell(4, 2, InheritDirection.TOP_AND_LEFT, 2);
		
		return lcsGrid;
	}
	
	private static LCSGrid<String> createTraversalTestGrid2()
	{
		LCSGrid<String> lcsGrid = 
				new LCSGrid<String>(
						LCSStringElement.createCharSequence("fghij"), 
						LCSStringElement.createCharSequence("abcde"));
		
		lcsGrid.setCell(0, 0, InheritDirection.DIAGONAL, 1);
		lcsGrid.setCell(1, 0, InheritDirection.LEFT, 1);
		lcsGrid.setCell(2, 0, InheritDirection.LEFT, 1);
		lcsGrid.setCell(3, 0, InheritDirection.LEFT, 1);
		lcsGrid.setCell(4, 0, InheritDirection.LEFT, 1);

		
		lcsGrid.setCell(0, 1, InheritDirection.LEFT, 2);
		lcsGrid.setCell(1, 1, InheritDirection.DIAGONAL, 2);
		lcsGrid.setCell(2, 1, InheritDirection.LEFT, 3);
		lcsGrid.setCell(3, 1, InheritDirection.LEFT, 3);
		lcsGrid.setCell(4, 1, InheritDirection.LEFT, 3);
		
		lcsGrid.setCell(0, 2, InheritDirection.LEFT, 2);
		lcsGrid.setCell(1, 2, InheritDirection.TOP, 3);
		lcsGrid.setCell(2, 2, InheritDirection.TOP_AND_LEFT, 3);
		lcsGrid.setCell(3, 2, InheritDirection.TOP_AND_LEFT, 3);
		lcsGrid.setCell(4, 2, InheritDirection.TOP_AND_LEFT, 3);
		
		lcsGrid.setCell(0, 3, InheritDirection.LEFT, 2);
		lcsGrid.setCell(1, 3, InheritDirection.TOP, 3);
		lcsGrid.setCell(2, 3, InheritDirection.TOP_AND_LEFT, 3);
		lcsGrid.setCell(3, 3, InheritDirection.TOP_AND_LEFT, 3);
		lcsGrid.setCell(4, 3, InheritDirection.TOP_AND_LEFT, 3);
		
		lcsGrid.setCell(0, 4, InheritDirection.LEFT, 2);
		lcsGrid.setCell(1, 4, InheritDirection.TOP, 3);
		lcsGrid.setCell(2, 4, InheritDirection.TOP_AND_LEFT, 3);
		lcsGrid.setCell(3, 4, InheritDirection.TOP_AND_LEFT, 3);
		lcsGrid.setCell(4, 4, InheritDirection.TOP_AND_LEFT, 3);
		
		return lcsGrid;
	}
	
	
	
}