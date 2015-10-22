package com.virtualparadigm.openlcs;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class VisitedLCSCell extends LCSCell
{
	private boolean prevSplit;
	private InheritDirection prevInheritDirection;
	
	public VisitedLCSCell()
	{
		super();
	}
	public VisitedLCSCell(boolean prevSplit, InheritDirection prevInheritDirection)
	{
		super();
		this.prevSplit = prevSplit;
		this.prevInheritDirection = prevInheritDirection;
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
	
	@Override
	protected Object clone() throws CloneNotSupportedException
	{
		VisitedLCSCell clone = (VisitedLCSCell)super.clone();
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
        return builder.isEquals();
    }		
}