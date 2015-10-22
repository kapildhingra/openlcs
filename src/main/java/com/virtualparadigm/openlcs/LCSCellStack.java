package com.virtualparadigm.openlcs;

import java.util.Stack;

public class LCSCellStack extends Stack<LCSCell>
{
	private static final long serialVersionUID = 1L;
	private InheritDirection prevMove;
	
	public LCSCellStack()
	{
		super();
	}

	public InheritDirection getPrevMove()
	{
		return prevMove;
	}
	public void setPrevMove(InheritDirection prevMove)
	{
		this.prevMove = prevMove;
	}

	
	
}