package com.virtualparadigm.openlcs;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;

public class LCSProcessor
{
	public static <T extends Comparable<T>> LCSGrid<T> evaluateGrid(LCSElement<T>[] xSequence, LCSElement<T>[] ySequence)
	{
		LCSGrid<T> lcsGrid = null;
		if(xSequence != null && ySequence != null)
		{
			lcsGrid = new LCSGrid<T>(xSequence, ySequence);
			LCSCell leftCell = null;
			LCSCell topCell = null;
			for(int x=0; x<lcsGrid.getSizeX(); x++)
			{
				for(int y=0; y<lcsGrid.getSizeY(); y++)
				{
					if(xSequence[x].compareTo(ySequence[y].getValue()) == 0)
					{
						//is equal
						if(x == 0 || y == 0)
						{
							lcsGrid.setCell(x, y, InheritDirection.DIAGONAL, 1);
						}
						else
						{
							lcsGrid.setCell(x, y,InheritDirection.DIAGONAL, lcsGrid.getCell(x-1, y-1).getValue() + 1);
						}
					}
					else
					{
						//not equal
						leftCell = lcsGrid.getCell(x-1, y);
						topCell = lcsGrid.getCell(x, y-1);
						
						if(leftCell != null)
						{
							if(topCell != null)
							{
								if(leftCell.getValue() == topCell.getValue())
								{
									if(leftCell.getValue() == 0)
									{
										lcsGrid.setCell(x, y, InheritDirection.LEFT, leftCell.getValue());
									}
									else
									{
										lcsGrid.setCell(x, y, InheritDirection.TOP_AND_LEFT, leftCell.getValue());
									}
								}
								else if(leftCell.getValue() > topCell.getValue())
								{
									lcsGrid.setCell(x, y, InheritDirection.LEFT, leftCell.getValue());
								}
								else if(leftCell.getValue() < topCell.getValue())
								{
									lcsGrid.setCell(x, y, InheritDirection.TOP, topCell.getValue());
								}
							}
							else
							{
								lcsGrid.setCell(x, y, InheritDirection.LEFT, leftCell.getValue());
							}
						}
						else
						{
							if(topCell != null)
							{
								lcsGrid.setCell(x, y, InheritDirection.TOP, topCell.getValue());
							}
							else
							{
								lcsGrid.setCell(x, y, InheritDirection.DIAGONAL, 0);
							}
						}
					}
				}
			}
		}
		return lcsGrid;
	}
	
	public static <T extends Comparable<T>> List<LCSElement<T>[]> backtrackGrid(LCSGrid<T> lcsGrid)
	{
		List<LCSElement<T>[]> elementSolutionList = null;
		if(lcsGrid != null)
		{
			//Key is hashed value of NEXT cells x,y coordinates
//			Map<String, List<Stack<LCSCell>>> walkedStackMap = new HashMap<String, List<Stack<LCSCell>>>();
			Map<CoordinateMapKey, List<Stack<LCSCell>>> walkedStackMap = new HashMap<CoordinateMapKey, List<Stack<LCSCell>>>();
			
//			List<Stack<LCSCell>> stackList = new ArrayList<Stack<LCSCell>>();
			List<Stack<LCSCell>> stackList = null;
			
			LCSGridWalker gridWalker = new LCSGridWalker(lcsGrid, new Coordinate(lcsGrid.getSizeX()-1,lcsGrid.getSizeY()-1));
			LCSCell[] currentCells = null;
			Stack<LCSCell> newStack = null;
			
//			System.out.println("");
			while((currentCells=gridWalker.nextCells()) != null)
			{
//				System.out.print("[" + lcsGrid.getXElement(lcsCell.getCoordinate().getX()).getValue() + "," + lcsGrid.getYElement(lcsCell.getCoordinate().getY()).getValue() + " (" + lcsCell.getCoordinate().getX() + "," + lcsCell.getCoordinate().getY() + ")] ");
				for(LCSCell lcsCell : currentCells)
				{
					if(walkedStackMap.size() == 0)
					{
						//first element
						stackList = new ArrayList<Stack<LCSCell>>();
						newStack = new Stack<LCSCell>();
						switch(lcsCell.getInheritDirection())
						{
							case DIAGONAL:
							{
								newStack.push(lcsCell);
								stackList.add(newStack);
								walkedStackMap.put(LCSProcessor.getDiagnalCellKey(lcsCell), stackList);
								break;
							}
							case TOP:
							{
								stackList.add(newStack);
								walkedStackMap.put(LCSProcessor.getTopCellKey(lcsCell), stackList);
								break;
							}
							case LEFT:
							{
								stackList.add(newStack);
								walkedStackMap.put(LCSProcessor.getLeftCellKey(lcsCell), stackList);
								break;
							}
							case TOP_AND_LEFT:
							{
								stackList.add(newStack);
								walkedStackMap.put(LCSProcessor.getTopCellKey(lcsCell), stackList);
								walkedStackMap.put(LCSProcessor.getLeftCellKey(lcsCell), LCSProcessor.shallowCloneStackList(stackList));
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
						stackList = walkedStackMap.get(LCSProcessor.getCurrentCellKey(lcsCell));
						if(stackList != null)
						{
							switch(lcsCell.getInheritDirection())
							{
								case DIAGONAL:
								{
									LCSProcessor.moveCellStacksDiagonal(walkedStackMap, lcsCell, false);
									break;
								}
								case TOP:
								{
									LCSProcessor.moveCellStacksTop(walkedStackMap, lcsCell, false);
									break;
								}
								case LEFT:
								{
									LCSProcessor.moveCellStacksLeft(walkedStackMap, lcsCell, false);
									break;
								}
								case TOP_AND_LEFT:
								{
									//If my prev cell was a split, then keep going in the direction of the previous move until 
									// i reach a cell that is not a split (all split cells should eventually reach a non split - top, left or diagonal)
									for(Stack<LCSCell> cellStack : stackList)
									{
										LCSCell topLCSCell = cellStack.peek();
//										if(topLCSCell.getInheritDirection() == InheritDirection.TOP_AND_LEFT)
										
										
									}
									
									walkedStackMap.remove(LCSProcessor.getCurrentCellKey(lcsCell));
									
									

									if(walkedStackMap.containsKey(LCSProcessor.getTopCellKey(lcsCell)))
									{
										// add all of my cell stacks to that list as well, since the next cell is relevant to all of us now
//										cellStackMap.get(LCSProcessor.getTopCellHash(lcsCell)).addAll(cellStackList);
										walkedStackMap.get(LCSProcessor.getTopCellKey(lcsCell)).addAll(LCSProcessor.shallowCloneStackList(stackList));
									}
									else
									{
										walkedStackMap.put(LCSProcessor.getTopCellKey(lcsCell), LCSProcessor.shallowCloneStackList(stackList));
//										cellStackMap.put(LCSProcessor.getTopCellHash(lcsCell), cellStackList);
									}
									
									if(walkedStackMap.containsKey(LCSProcessor.getLeftCellKey(lcsCell)))
									{
										// add all of my cell stacks to that list as well, since the next cell is relevant to all of us now
//										cellStackMap.get(LCSProcessor.getLeftCellHash(lcsCell)).addAll(cellStackList);
										walkedStackMap.get(LCSProcessor.getLeftCellKey(lcsCell)).addAll(LCSProcessor.shallowCloneStackList(stackList));
									}
									else
									{
										walkedStackMap.put(LCSProcessor.getLeftCellKey(lcsCell), LCSProcessor.shallowCloneStackList(stackList));
//										cellStackMap.put(LCSProcessor.getLeftCellHash(lcsCell),(List<Stack<LCSCell>>)((ArrayList<Stack<LCSCell>>)cellStackList).clone());
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
			}// end while
			
			Set<Stack<LCSCell>> solutionStacks = LCSProcessor.getSolutionStacks(walkedStackMap);
			
			elementSolutionList = LCSProcessor.createElementSolutionList(solutionStacks, lcsGrid);
		}

		return elementSolutionList;
	}
	
	
	// =========================================================
	// UTILITY METHODS
	// =========================================================
	
	private static void moveCellStacksTop(Map<CoordinateMapKey, List<Stack<LCSCell>>> walkedStackMap, LCSCell lcsCell, boolean clone)
	{
		if(walkedStackMap != null && lcsCell != null)
		{
			List<Stack<LCSCell>> stackList = walkedStackMap.get(LCSProcessor.getCurrentCellKey(lcsCell));
			if(stackList != null)
			{			
				walkedStackMap.remove(LCSProcessor.getCurrentCellKey(lcsCell));
				if(walkedStackMap.containsKey(LCSProcessor.getTopCellKey(lcsCell)))
				{
					// add all of my cell stacks to that list as well, since the next cell is relevant to all of us now
					walkedStackMap.get(LCSProcessor.getTopCellKey(lcsCell)).addAll((clone) ? LCSProcessor.shallowCloneStackList(stackList) : stackList);
				}
				else
				{
					walkedStackMap.put(LCSProcessor.getTopCellKey(lcsCell), (clone) ? LCSProcessor.shallowCloneStackList(stackList) : stackList);
				}
			}
		}
	}
	private static void moveCellStackTop(Map<CoordinateMapKey, List<Stack<LCSCell>>> walkedStackMap, LCSCell lcsCell, int stackListIndex, boolean clone)
	{
		if(walkedStackMap != null && lcsCell != null)
		{
			List<Stack<LCSCell>> stackList = walkedStackMap.get(LCSProcessor.getCurrentCellKey(lcsCell));
			if(stackList != null)
			{			
				walkedStackMap.remove(LCSProcessor.getCurrentCellKey(lcsCell));
				if(walkedStackMap.containsKey(LCSProcessor.getTopCellKey(lcsCell)))
				{
					// add all of my cell stacks to that list as well, since the next cell is relevant to all of us now
					walkedStackMap.get(LCSProcessor.getTopCellKey(lcsCell)).addAll((clone) ? LCSProcessor.shallowCloneStackList(stackList) : stackList);
				}
				else
				{
					walkedStackMap.put(LCSProcessor.getTopCellKey(lcsCell), (clone) ? LCSProcessor.shallowCloneStackList(stackList) : stackList);
				}
			}
		}
	}
	
	
	
	private static void moveCellStacksLeft(Map<CoordinateMapKey, List<Stack<LCSCell>>> walkedStackMap, LCSCell lcsCell, boolean clone)
	{
		if(walkedStackMap != null && lcsCell != null)
		{
			List<Stack<LCSCell>> stackList = walkedStackMap.get(LCSProcessor.getCurrentCellKey(lcsCell));
			if(stackList != null)
			{
				walkedStackMap.remove(LCSProcessor.getCurrentCellKey(lcsCell));
				if(walkedStackMap.containsKey(LCSProcessor.getLeftCellKey(lcsCell)))
				{
					// add all of my cell stacks to that list as well, since the next cell is relevant to all of us now
					walkedStackMap.get(LCSProcessor.getLeftCellKey(lcsCell)).addAll((clone) ? LCSProcessor.shallowCloneStackList(stackList) : stackList);
				}
				else
				{
					walkedStackMap.put(LCSProcessor.getLeftCellKey(lcsCell), (clone) ? LCSProcessor.shallowCloneStackList(stackList) : stackList);
				}
			}
		}
	}
	
	private static void moveCellStacksDiagonal(Map<CoordinateMapKey, List<Stack<LCSCell>>> walkedStackMap, LCSCell lcsCell, boolean clone)
	{
		if(walkedStackMap != null && lcsCell != null)
		{
			List<Stack<LCSCell>> stackList = walkedStackMap.get(LCSProcessor.getCurrentCellKey(lcsCell));
			if(stackList != null)
			{
				for(Stack<LCSCell> stack : stackList)
				{
					stack.push(lcsCell);
				}
				
				walkedStackMap.remove(LCSProcessor.getCurrentCellKey(lcsCell));
				if(walkedStackMap.containsKey(LCSProcessor.getDiagnalCellKey(lcsCell)))
				{
					// add all of my cell stacks to that list as well, since the next cell is relevant to all of us now
					walkedStackMap.get(LCSProcessor.getDiagnalCellKey(lcsCell)).addAll((clone) ? LCSProcessor.shallowCloneStackList(stackList) : stackList);
				}
				else
				{
					walkedStackMap.put(LCSProcessor.getDiagnalCellKey(lcsCell), (clone) ? LCSProcessor.shallowCloneStackList(stackList) : stackList);
				}
			}
		}
	}
	
	
	private static Set<Stack<LCSCell>> getSolutionStacks(Map<CoordinateMapKey, List<Stack<LCSCell>>> walkedStackMap)
	{
		Set<Stack<LCSCell>> resultSet = null;
		if(walkedStackMap != null)
		{
			TreeMap<Integer, Set<Stack<LCSCell>>> consolidatedMap = new TreeMap<Integer, Set<Stack<LCSCell>>>();
			Set<Stack<LCSCell>> consolidatedSet = null;
			for(Entry<CoordinateMapKey,List<Stack<LCSCell>>> cellStackListEntry : walkedStackMap.entrySet())
			{
				for(Stack<LCSCell> lcsStack : cellStackListEntry.getValue())
				{
					consolidatedSet = consolidatedMap.get(lcsStack.size());
					if(consolidatedSet == null)
					{
						consolidatedSet = new HashSet<Stack<LCSCell>>();
						consolidatedMap.put(lcsStack.size(), consolidatedSet);
					}
					consolidatedSet.add(LCSProcessor.removeNonSolutionCells(lcsStack));
				}
			}
			
			if(consolidatedMap.size() > 0)
			{
				resultSet = consolidatedMap.get(consolidatedMap.lastKey());
			}
		}
		return resultSet;
	}
	
	private static Stack<LCSCell> removeNonSolutionCells(Stack<LCSCell> lcsStack)
	{
		if(lcsStack != null)
		{
			LCSCell lcsCell = null;
			for(Iterator<LCSCell> it=lcsStack.iterator(); it.hasNext(); )
			{
				lcsCell = it.next();
				if(!lcsCell.isMatchCell())
				{
					it.remove();
				}
			}
		}
		return lcsStack;
	}
	
	private static <T extends Comparable<T>> List<LCSElement<T>[]> createElementSolutionList(Set<Stack<LCSCell>> stackSet, LCSGrid<T> lcsGrid)
	{
		List<LCSElement<T>[]> elementSolutionList = null;
		if(stackSet != null && lcsGrid != null)
		{
			List<LCSElement<T>> tempList = null;
			elementSolutionList = new ArrayList<LCSElement<T>[]>();
			for(Stack<LCSCell> stack : stackSet)
			{
				tempList = new ArrayList<LCSElement<T>>();
				while(!stack.empty())
				{
					tempList.add(lcsGrid.getXElement(stack.pop().getXCoordinate()));
				}
				elementSolutionList.add((LCSElement<T>[])tempList.toArray(new LCSElement[tempList.size()]));
			}
		}
		return elementSolutionList;
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
	
	
	public static void main(String[] args)
	{
		
//		LCSGrid<String> lcsGrid = 
//				LCSProcessor.evaluateGrid(
//						LCSStringElement.createCharSequence("banana"), 
//						LCSStringElement.createCharSequence("atana"));
//		
//		lcsGrid.printGrid(10);
//		
//		System.out.println("");
//		System.out.println("----------------");
//		System.out.println("");
//		
//		LCSGrid<String> g2 = 
//				LCSProcessor.evaluateGrid(
//						LCSStringElement.createCharSequence("dictionary"), 
//						LCSStringElement.createCharSequence("pictionary"));
//		
//		g2.printGrid(10);
//
//		System.out.println("");
//		System.out.println("----------------");
//		System.out.println("");

		LCSGrid<String> g3 = 
				LCSProcessor.evaluateGrid(
						LCSStringElement.createCharSequence("bazctcrazck"), 
						LCSStringElement.createCharSequence("abczrtckz"));

		
//		LCSGrid<String> g3 = 
//				LCSProcessor.evaluateGrid(
//						LCSStringElement.createCharSequence("america"), 
//						LCSStringElement.createCharSequence("armenia"));
		
//		LCSGrid<String> g3 = 
//				LCSProcessor.evaluateGrid(
//						LCSStringElement.createCharSequence("viewsonic"), 
//						LCSStringElement.createCharSequence("viewpoint"));
		
		//chael
		
//		LCSGrid<String> g3 = 
//				LCSProcessor.evaluateGrid(
//						LCSStringElement.createCharSequence("bactcrack"), 
//						LCSStringElement.createCharSequence("abcrtck"));
		
		g3.printGrid(new File("testa.csv"));
		g3.printGrid(10);
		System.out.println("");
		
		 
		LCSGridWalker gridWalker = new LCSGridWalker(g3);
		
		LCSCell[] currentCells = null;
		while((currentCells=gridWalker.nextCells()) != null)
		{
			for(LCSCell lcsCell : currentCells)
			{
				System.out.print("[" + g3.getXElement(lcsCell.getCoordinate().getX()).getValue() + "," + g3.getYElement(lcsCell.getCoordinate().getY()).getValue() + " (" + lcsCell.getCoordinate().getX() + "," + lcsCell.getCoordinate().getY() + ")] ");
			}
			System.out.print("\n");
			
		}
		
		gridWalker.reset();
		List<LCSElement<String>[]> solutions = LCSProcessor.backtrackGrid(g3);
		System.out.println("");
		LCSProcessor.printSolution(solutions);
		
	}
	
	
	
	
	
	
	
	
	
	// ==========================================================
	// MISC
	// ==========================================================
	
	private static <T extends Comparable<T>> T printSolution(List<LCSElement<T>[]> lcsElementList)
	{
		if(lcsElementList != null)
		{
			for(LCSElement<T>[] lcsElements : lcsElementList)
			{
				for(LCSElement lcsElement : lcsElements)
				{
					System.out.print(lcsElement.asString());
				}
				System.out.print("\n");
			}
		}
		return null;
	}
	
	
	
	private static void printCellStackMap(Map<String, List<Stack<LCSCell>>> cellStackMap, LCSGrid lcsGrid)
	{
		if(cellStackMap != null && lcsGrid != null)
		{
			for(Entry<String,List<Stack<LCSCell>>> cellStackListEntry : cellStackMap.entrySet())
			{
				System.out.print("[" + cellStackListEntry.getKey() + "] ");
				for(Stack<LCSCell> lcsStack : cellStackListEntry.getValue())
				{
					for(int i=0; i<lcsStack.size(); i++)
					{
						System.out.print(lcsGrid.getXElement(lcsStack.get(i).getXCoordinate()).asString());
					}
					System.out.print("\n");
				}
			}
			System.out.println("");
		}
	}
	
	private static void printCellStacks(Collection<Stack<LCSCell>> cellStacks, LCSGrid lcsGrid)
	{
		if(cellStacks != null && lcsGrid != null)
		{
			for(Stack<LCSCell> cellStack : cellStacks)
			{
				for(int i=0; i<cellStack.size(); i++)
				{
					System.out.print(lcsGrid.getXElement(cellStack.get(i).getXCoordinate()).asString());
				}
				System.out.print("\n");
			}
			System.out.println("");
		}
	}
	
	private static void printLCSCells(Map<String, List<Stack<LCSCell>>> cellStackMap, LCSGrid lcsGrid)
	{

		if(cellStackMap != null && lcsGrid != null)
		{
			LCSCell cell = null;
			for(Entry<String,List<Stack<LCSCell>>> cellStackListEntry : cellStackMap.entrySet())
			{
				for(Stack<LCSCell> lcsStack : cellStackListEntry.getValue())
				{
					for(int i=0; i<lcsStack.size(); i++)
					{
						System.out.print(lcsGrid.getXElement(lcsStack.get(i).getXCoordinate()).asString());
					}
//					while((!lcsStack.isEmpty()) && (cell=lcsStack.pop()) != null)
//					{
//						System.out.print(lcsGrid.getXElement(cell.getXCoordinate()).asString());
//					}
					System.out.print("\n");
				}
			}
			System.out.println("");
		}
	}
	

}