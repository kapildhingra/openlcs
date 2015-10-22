package com.virtualparadigm.openlcs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.Map.Entry;

public class BacktrackSolver
{
//	private Map<CoordinateMapKey, List<Stack<LCSCell>>> walkedStackMap;
	private Map<Coordinate, List<Stack<LCSCell>>> walkedStackMap;
	
	public BacktrackSolver()
	{
		this.walkedStackMap = new HashMap<Coordinate, List<Stack<LCSCell>>>();
	}
	
	public void processCells(LCSCell[] lcsCells)
	{
		if(lcsCells != null)
		{
			List<Stack<LCSCell>> currentStackList = null;
			Stack<LCSCell> newStack = null;
			Direction movedDirection = null;
			for(LCSCell lcsCell : lcsCells)
			{
//				if(lcsCell != null)
				currentStackList = this.walkedStackMap.get(lcsCell.getCoordinate());
//				if(this.walkedStackMap.size() == 0 && lcsCell.getCoordinate() != null)
				if(currentStackList == null)
				{
					//first element
					currentStackList = new ArrayList<Stack<LCSCell>>();
					newStack = new Stack<LCSCell>();
					switch(lcsCell.getInheritDirection())
					{
						case DIAGONAL:
						{
							newStack.push(lcsCell);
							currentStackList.add(newStack);
							this.walkedStackMap.put(BacktrackSolver.createDiagnalCoordinate(lcsCell.getCoordinate()), currentStackList);
							break;
						}
						case TOP:
						{
							currentStackList.add(newStack);
							this.walkedStackMap.put(BacktrackSolver.createTopCoordinate(lcsCell.getCoordinate()), currentStackList);
							break;
						}
						case LEFT:
						{
							currentStackList.add(newStack);
							this.walkedStackMap.put(BacktrackSolver.createLeftCoordinate(lcsCell.getCoordinate()), currentStackList);
							break;
						}
						case TOP_AND_LEFT:
						{
							currentStackList.add(newStack);
							this.walkedStackMap.put(BacktrackSolver.createTopCoordinate(lcsCell.getCoordinate()), currentStackList);
							this.walkedStackMap.put(BacktrackSolver.createLeftCoordinate(lcsCell.getCoordinate()), BacktrackSolver.shallowCloneStackList(currentStackList));
							break;
						}
						default:
						{
							break;
						}
					}
				}
				else
				{
					//ONLY PUSH MATCHES (DIAGONAL) AND SPLITS (TOP AND LEFT) ON STACK
					switch(lcsCell.getInheritDirection())
					{
						case DIAGONAL:
						{
							BacktrackSolver.pushOnStacks(lcsCell, currentStackList);
							this.moveCellStacksDiagonal(lcsCell.getCoordinate());
							break;
						}
						case TOP:
						{
							this.moveCellStacksTop(lcsCell.getCoordinate());
							break;
						}
						case LEFT:
						{
							this.moveCellStacksLeft(lcsCell.getCoordinate());
							break;
						}
						case TOP_AND_LEFT:
						{
							
							this.walkedStackMap.remove(BacktrackSolver.getCurrentCellKey(lcsCell));
							
							

							if(this.walkedStackMap.containsKey(BacktrackSolver.getTopCellKey(lcsCell)))
							{
								// add all of my cell stacks to that list as well, since the next cell is relevant to all of us now
//								cellStackMap.get(BacktrackSolver.getTopCellHash(lcsCell)).addAll(cellStackList);
								this.walkedStackMap.get(BacktrackSolver.getTopCellKey(lcsCell)).addAll(BacktrackSolver.shallowCloneStackList(currentStackList));
							}
							else
							{
//								this.walkedStackMap.put(BacktrackSolver.getTopCellKey(lcsCell), BacktrackSolver.shallowCloneStackList(currentStackList));
//								cellStackMap.put(BacktrackSolver.getTopCellHash(lcsCell), cellStackList);
							}
							
							if(this.walkedStackMap.containsKey(BacktrackSolver.getLeftCellKey(lcsCell)))
							{
								// add all of my cell stacks to that list as well, since the next cell is relevant to all of us now
//								cellStackMap.get(BacktrackSolver.getLeftCellHash(lcsCell)).addAll(cellStackList);
								this.walkedStackMap.get(BacktrackSolver.getLeftCellKey(lcsCell)).addAll(BacktrackSolver.shallowCloneStackList(currentStackList));
							}
							else
							{
//								this.walkedStackMap.put(BacktrackSolver.getLeftCellKey(lcsCell), BacktrackSolver.shallowCloneStackList(currentStackList));
//								cellStackMap.put(BacktrackSolver.getLeftCellHash(lcsCell),(List<Stack<LCSCell>>)((ArrayList<Stack<LCSCell>>)cellStackList).clone());
							}
							break;
						}
						default:
						{
							break;
						}
					}// end switch
				}
			}
		}
		
	}
	
	private static void pushOnStacks(LCSCell lcsCell, List<Stack<LCSCell>> stackList)
	{
		if(lcsCell != null && stackList != null)
		{
			for(Stack<LCSCell> stack : stackList)
			{
				stack.push(lcsCell);
			}
		}
	}
	
	public static Direction getMovedDirection(LCSCell from, LCSCell to)
	{
		Direction direction = null;
		if(from != null && to != null)
		{
			direction = Direction.getDirection(from.getXCoordinate(), from.getYCoordinate(), to.getXCoordinate(), to.getYCoordinate());
		}
		return direction;
	}
	
	public void moveCellStacksTop(Coordinate coordinate)
	{
		if(this.walkedStackMap != null && coordinate != null)
		{
			List<Stack<LCSCell>> stackList = this.walkedStackMap.get(coordinate);
			if(stackList != null)
			{
				this.walkedStackMap.remove(coordinate);
				Coordinate topCoordinate = BacktrackSolver.createTopCoordinate(coordinate);
				stackList = walkedStackMap.get(topCoordinate);
				if(stackList == null)
				{
					stackList = new ArrayList<Stack<LCSCell>>();
				}
//				this.walkedStackMap.put(topCoordinate, (clone) ? BacktrackSolver.shallowCloneStackList(stackList) : stackList);
				this.walkedStackMap.put(topCoordinate, stackList);
			}
		}
	}
	
	public void moveCellStackTop(Coordinate coordinate, int index)
	{
		if(this.walkedStackMap != null && coordinate != null)
		{
			List<Stack<LCSCell>> stackList = this.walkedStackMap.get(coordinate);
			if(stackList != null)
			{
				Stack<LCSCell> lcsStack = stackList.get(index);
				if(lcsStack != null)
				{
					stackList.remove(index);
					Coordinate topCoordinate = BacktrackSolver.createTopCoordinate(coordinate);
					List<Stack<LCSCell>> newStackList = this.walkedStackMap.get(topCoordinate);
					if(newStackList == null)
					{
						newStackList = new ArrayList<Stack<LCSCell>>();
					}
					this.walkedStackMap.put(topCoordinate, newStackList);
				}
			}
		}
	}
	
	public void moveCellStacksLeft(Coordinate coordinate)
	{
		if(this.walkedStackMap != null && coordinate != null)
		{
			List<Stack<LCSCell>> stackList = this.walkedStackMap.get(coordinate);
			if(stackList != null)
			{
				this.walkedStackMap.remove(coordinate);
				Coordinate leftCoordinate = BacktrackSolver.createLeftCoordinate(coordinate);
				stackList = walkedStackMap.get(leftCoordinate);
				if(stackList == null)
				{
					stackList = new ArrayList<Stack<LCSCell>>();
				}
				this.walkedStackMap.put(leftCoordinate, stackList);
			}
		}
	}
	
	public void moveCellStackLeft(Coordinate coordinate, int index)
	{
		if(this.walkedStackMap != null && coordinate != null)
		{
			List<Stack<LCSCell>> stackList = this.walkedStackMap.get(coordinate);
			if(stackList != null)
			{
				Stack<LCSCell> lcsStack = stackList.get(index);
				if(lcsStack != null)
				{
					stackList.remove(index);
					Coordinate leftCoordinate = BacktrackSolver.createLeftCoordinate(coordinate);
					List<Stack<LCSCell>> newStackList = this.walkedStackMap.get(leftCoordinate);
					if(newStackList == null)
					{
						newStackList = new ArrayList<Stack<LCSCell>>();
					}
					this.walkedStackMap.put(leftCoordinate, newStackList);
				}
			}
		}
	}
	
	
	public void moveCellStacksDiagonal(Coordinate coordinate)
	{
		if(this.walkedStackMap != null && coordinate != null)
		{
			List<Stack<LCSCell>> stackList = this.walkedStackMap.get(coordinate);
			if(stackList != null)
			{
				this.walkedStackMap.remove(coordinate);
				Coordinate diagonalCoordinate = BacktrackSolver.createDiagnalCoordinate(coordinate);
				stackList = walkedStackMap.get(diagonalCoordinate);
				if(stackList == null)
				{
					stackList = new ArrayList<Stack<LCSCell>>();
				}
				this.walkedStackMap.put(diagonalCoordinate, stackList);
			}
		}
	}
	
	
	public Set<Stack<LCSCell>> getSolutionStacks()
	{
		Set<Stack<LCSCell>> resultSet = null;
		if(this.walkedStackMap != null)
		{
			TreeMap<Integer, Set<Stack<LCSCell>>> consolidatedMap = new TreeMap<Integer, Set<Stack<LCSCell>>>();
			Set<Stack<LCSCell>> consolidatedSet = null;
			for(Entry<Coordinate,List<Stack<LCSCell>>> cellStackListEntry : this.walkedStackMap.entrySet())
			{
				for(Stack<LCSCell> lcsStack : cellStackListEntry.getValue())
				{
					consolidatedSet = consolidatedMap.get(lcsStack.size());
					if(consolidatedSet == null)
					{
						consolidatedSet = new HashSet<Stack<LCSCell>>();
						consolidatedMap.put(lcsStack.size(), consolidatedSet);
					}
					consolidatedSet.add(lcsStack);
				}
			}
			
			if(consolidatedMap.size() > 0)
			{
				resultSet = consolidatedMap.get(consolidatedMap.lastKey());
			}
		}
		return resultSet;
	}
		
	
	
	public static CoordinateMapKey getCurrentCellKey(LCSCell lcsCell)
	{
		CoordinateMapKey key = null;
		if(lcsCell != null)
		{
			key = new CoordinateMapKey(lcsCell.getXCoordinate(), lcsCell.getYCoordinate());
		}
		return key;
	}
	public static CoordinateMapKey getTopCellKey(LCSCell lcsCell)
	{
		CoordinateMapKey key = null;
		if(lcsCell != null)
		{
			key = new CoordinateMapKey(lcsCell.getXCoordinate(), lcsCell.getYCoordinate()-1);
		}
		return key;
	}
	public static CoordinateMapKey getLeftCellKey(LCSCell lcsCell)
	{
		CoordinateMapKey key = null;
		if(lcsCell != null)
		{
			key = new CoordinateMapKey(lcsCell.getXCoordinate()-1, lcsCell.getYCoordinate());
		}
		return key;
	}	
	public static CoordinateMapKey getDiagnalCellKey(LCSCell lcsCell)
	{
		CoordinateMapKey key = null;
		if(lcsCell != null)
		{
			key = new CoordinateMapKey(lcsCell.getXCoordinate()-1, lcsCell.getYCoordinate()-1);
		}
		return key;
	}
	
	public static Coordinate createDiagnalCoordinate(Coordinate coordinate)
	{
		Coordinate retCoordinate = null;
		if(coordinate != null)
		{
			retCoordinate = new Coordinate(coordinate.getX()-1, coordinate.getY()-1);
		}
		return retCoordinate;
	}
	public static Coordinate createTopCoordinate(Coordinate coordinate)
	{
		Coordinate retCoordinate = null;
		if(coordinate != null)
		{
			retCoordinate = new Coordinate(coordinate.getX(), coordinate.getY()-1);
		}
		return retCoordinate;
	}
	public static Coordinate createLeftCoordinate(Coordinate coordinate)
	{
		Coordinate retCoordinate = null;
		if(coordinate != null)
		{
			retCoordinate = new Coordinate(coordinate.getX()-1, coordinate.getY());
		}
		return retCoordinate;
	}
	
	
	public static Stack<LCSCell> deepCloneCellStack(Stack<LCSCell> cellStack)
	{
		Stack<LCSCell> retStack = null;
		if(cellStack != null)
		{
			retStack = (Stack<LCSCell>)cellStack.clone();
			try
			{
				for(int i=0; i<cellStack.size(); i++)
				{
					retStack.add((LCSCell)cellStack.get(i).clone());
				}
			}
			catch(CloneNotSupportedException cnse)
			{
			}
		}
		return retStack;
	}
	public static List<Stack<LCSCell>> shallowCloneStackList(List<Stack<LCSCell>> stackList)
	{
		List<Stack<LCSCell>> retStackList = null;
		if(stackList != null)
		{
			retStackList = new ArrayList<Stack<LCSCell>>();
			for(int i=0; i<stackList.size(); i++)
			{
				//we are cloning the stack. not necessarily every element in the stack.
				retStackList.add((Stack<LCSCell>)stackList.get(i).clone());
			}
		}
		return retStackList;
	}
	
	
	
//	//If my prev cell was a split, then keep going in the direction of the previous move until 
//	// i reach a cell that is not a split (all split cells should eventually reach a non split - top, left or diagonal)
//	for(Stack<LCSCell> cellStack : currentStackList)
//	{
//		LCSCell topLCSCell = cellStack.pop();
//		if(topLCSCell.getInheritDirection() == InheritDirection.TOP_AND_LEFT)
//		{
//			LCSCell secondLCSCell = cellStack.pop();
//			
//			movedDirection = BacktrackSolver.getMovedDirection(secondLCSCell, topLCSCell);
//			
//			if(movedDirection != null)
//			{
//				switch(movedDirection)
//				{
//					
//					case NORTH:
//					{
//						
//						break;
//					}
//					case WEST:
//					{
//						
//						break;
//					}
//					default:
//					{
//						
//					}
//					
//				}
//				
//				
//				
//			}
//			
//			
//			cellStack.push(secondLCSCell);
//			
//		}
//		cellStack.push(topLCSCell);
//	}
//	
//	
//	
//	
//	

	
	
}