package com.virtualparadigm.openlcs;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class LCSCell implements Cloneable
{
	private int value;
	private InheritDirection inheritDirection;
	private Coordinate coordinate;

	private boolean prevSplit;
	private InheritDirection prevInheritDirection;
	
	public LCSCell()
	{
		super();
	}
	public LCSCell(InheritDirection inheritDirection, int value, Coordinate coordinate)
	{
		super();
		this.value = value;
		this.inheritDirection = inheritDirection;
		this.coordinate = coordinate;
	}
	
	public LCSCell(int value, InheritDirection inheritDirection, Coordinate coordinate, boolean prevSplit, InheritDirection prevInheritDirection)
	{
		super();
		this.value = value;
		this.inheritDirection = inheritDirection;
		this.coordinate = coordinate;
		this.prevSplit = prevSplit;
		this.prevInheritDirection = prevInheritDirection;
	}

	public int getValue()
	{
		return value;
	}
	public void setValue(int value)
	{
		this.value = value;
	}
	public InheritDirection getInheritDirection()
	{
		return inheritDirection;
	}
	public void setInheritDirection(InheritDirection inheritDirection)
	{
		this.inheritDirection = inheritDirection;
	}
	public boolean isMatchCell()
	{
		boolean status = false;
		if(this.inheritDirection == InheritDirection.DIAGONAL)
		{
			status = true;
		}
		return status;
	}
	public Coordinate getCoordinate()
	{
		return coordinate;
	}
	public void setCoordinate(Coordinate coordinate)
	{
		this.coordinate = coordinate;
	}
	public int getXCoordinate()
	{
		int value = -1;
		if(this.coordinate != null)
		{
			value = this.coordinate.getX();
		}
		return value;
	}
	public int getYCoordinate()
	{
		int value = -1;
		if(this.coordinate != null)
		{
			value = this.coordinate.getY();
		}
		return value;
	}
	
	
	public boolean isPrevSplit()
	{
		return prevSplit;
	}
	public void setPrevSplit(boolean prevSplit)
	{
		this.prevSplit = prevSplit;
	}
	public InheritDirection getPrevInheritDirection()
	{
		return prevInheritDirection;
	}
	public void setPrevInheritDirection(InheritDirection prevInheritDirection)
	{
		this.prevInheritDirection = prevInheritDirection;
	}
	
	// ===============================================
	// UTILITY METHODS
	// ===============================================
	@Override
	protected Object clone() throws CloneNotSupportedException
	{
		LCSCell clone = (LCSCell)super.clone();
		clone.setInheritDirection(this.inheritDirection);
		clone.setValue(this.value);
		clone.setCoordinate(new Coordinate(this.coordinate));
		clone.setPrevSplit(this.prevSplit);
		clone.setPrevInheritDirection(this.prevInheritDirection);
		return clone;
	}
	
    @Override
    public int hashCode()
    {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(this.getValue());
        builder.append(this.getInheritDirection());
        builder.append(this.getCoordinate());
        builder.append(this.isPrevSplit());
        builder.append(this.getPrevInheritDirection());
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
        LCSCell that = (LCSCell)obj;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.getValue(), that.getValue());
        builder.append(this.getInheritDirection(), that.getInheritDirection());
        builder.append(this.getCoordinate(), that.getCoordinate());
        builder.append(this.isPrevSplit(), that.isPrevSplit());
        builder.append(this.getPrevInheritDirection(), that.getPrevInheritDirection());
        return builder.isEquals();
    }	
	
}