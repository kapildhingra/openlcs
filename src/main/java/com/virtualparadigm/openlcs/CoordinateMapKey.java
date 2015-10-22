package com.virtualparadigm.openlcs;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class CoordinateMapKey implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private int x;
	private int y;
//	private boolean prevSplit;
//	private InheritDirection prevInheritDirection;
	
	public CoordinateMapKey()
	{
		super();
	}
	public CoordinateMapKey(int x, int y)
	{
		super();
		this.x = x;
		this.y = y;
	}
//	public CoordinateMapKey(int x, int y, boolean prevSplit, InheritDirection prevInheritDirection)
//	{
//		super();
//		this.x = x;
//		this.y = y;
//		this.prevSplit = prevSplit;
//		this.prevInheritDirection = prevInheritDirection;
//	}
	
	public int getX()
	{
		return x;
	}
	public void setX(int x)
	{
		this.x = x;
	}
	public int getY()
	{
		return y;
	}
	public void setY(int y)
	{
		this.y = y;
	}
	
	// =============================================================
	// UTILITY METHODS
	// =============================================================
	@Override
	protected Object clone() throws CloneNotSupportedException
	{
		CoordinateMapKey clone = (CoordinateMapKey)super.clone();
		clone.setX(this.x);
		clone.setY(this.y);
//		clone.setPrevSplit(this.prevSplit);
//		clone.setPrevInheritDirection(this.prevInheritDirection);
		return clone;
	}
	
	
	//prev split and prev inherit direction is just extra meta data for backtracking
	// hashcode and equals is only dependent on x and y
    @Override
    public int hashCode()
    {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(this.getX());
        builder.append(this.getY());
        return builder.toHashCode();        
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (obj == this)
        {
            return true;
        }
        if (obj.getClass() != getClass())
        {
            return false;
        }
        CoordinateMapKey that = (CoordinateMapKey)obj;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.getX(), that.getX());
        builder.append(this.getY(), that.getY());
        return builder.isEquals();
    }	
	
	
	
}