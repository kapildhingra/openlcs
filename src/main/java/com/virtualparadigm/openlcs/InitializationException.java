package com.virtualparadigm.openlcs;

public class InitializationException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	
	public InitializationException(String message)
	{
		super(message);
	}
	
	public InitializationException(String message, Throwable t)
	{
		super(message, t);
	}
	
}