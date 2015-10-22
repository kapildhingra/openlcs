package com.virtualparadigm.openlcs;

public class OverlapException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	
	public OverlapException(String message)
	{
		super(message);
	}
	
	public OverlapException(String message, Throwable t)
	{
		super(message, t);
	}
	
}