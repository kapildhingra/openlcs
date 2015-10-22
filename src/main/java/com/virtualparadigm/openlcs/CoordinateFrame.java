package com.virtualparadigm.openlcs;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

// CoordinateFrame must be completely immutable
public class CoordinateFrame implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Coordinate currentCoordinate;
	private Coordinate previousCoordinate;
	
	public CoordinateFrame()
	{
		super();
	}
	
	public CoordinateFrame(Coordinate currentCoordinate)
	{
		super();
		this.currentCoordinate = currentCoordinate;
	}

	public CoordinateFrame(Coordinate currentCoordinate, Coordinate previousCoordinate)
	{
		super();
		this.currentCoordinate = currentCoordinate;
		this.previousCoordinate = previousCoordinate;
	}

	public Coordinate getCurrentCoordinate()
	{
		return currentCoordinate;
	}

//	public void setCurrentCoordinate(Coordinate currentCoordinate)
//	{
//		this.currentCoordinate = currentCoordinate;
//	}

	public Coordinate getPreviousCoordinate()
	{
		return previousCoordinate;
	}

//	public void setPreviousCoordinate(Coordinate previousCoordinate)
//	{
//		this.previousCoordinate = previousCoordinate;
//	}
	
	public int getCurrentX()
	{
		int value = -1;
		if(this.currentCoordinate != null)
		{
			value = this.currentCoordinate.getX();
		}
		return value;
	}
	public int getCurrentY()
	{
		int value = -1;
		if(this.currentCoordinate != null)
		{
			value = this.currentCoordinate.getY();
		}
		return value;
	}
	
//	public void incrementCurrentX()
//	{
//		if(this.currentCoordinate != null)
//		{
//			this.currentCoordinate.incrementX();
//		}
//	}
//	public void decrementCurrentX()
//	{
//		if(this.currentCoordinate != null)
//		{
//			this.currentCoordinate.decrementX();
//		}
//	}
//	public void incrementCurrentY()
//	{
//		if(this.currentCoordinate != null)
//		{
//			this.currentCoordinate.incrementY();
//		}
//	}
//	public void decrementCurrentY()
//	{
//		if(this.currentCoordinate != null)
//		{
//			this.currentCoordinate.decrementY();
//		}
//	}
	
	
	public int getPreviousX()
	{
		int value = -1;
		if(this.previousCoordinate != null)
		{
			value = this.previousCoordinate.getX();
		}
		return value;
	}
	public int getPreviousY()
	{
		int value = -1;
		if(this.previousCoordinate != null)
		{
			value = this.previousCoordinate.getY();
		}
		return value;
	}
	
//	public void incrementPreviousX()
//	{
//		if(this.previousCoordinate != null)
//		{
//			this.previousCoordinate.incrementX();
//		}
//	}
//	public void decrementPreviousX()
//	{
//		if(this.previousCoordinate != null)
//		{
//			this.previousCoordinate.decrementX();
//		}
//	}
//	public void incrementPreviousY()
//	{
//		if(this.previousCoordinate != null)
//		{
//			this.previousCoordinate.incrementY();
//		}
//	}
//	public void decrementPreviousY()
//	{
//		if(this.previousCoordinate != null)
//		{
//			this.previousCoordinate.decrementY();
//		}
//	}

	// =============================================================
	// UTILITY METHODS
	// =============================================================
	@Override
	public String toString()
	{
		return "(" + this.getCurrentCoordinate() + "," + this.getPreviousCoordinate() + ")";
	}
	
    @Override
    public int hashCode()
    {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(this.getCurrentCoordinate());
//        builder.append(this.getPreviousCoordinate());
        int hash = builder.toHashCode();  
//        System.out.println("  hash="+hash);
        return hash;
//        return builder.toHashCode();        
    }

    public static void main(String[] args)
    {
    	CoordinateFrame c1 = new CoordinateFrame(new Coordinate(1,1), new Coordinate(1,2));
    	CoordinateFrame c2 = new CoordinateFrame(new Coordinate(1,1), new Coordinate(2,1));
    	
    	
    	System.out.println(c1.hashCode());
    	System.out.println(c2.hashCode());
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
        CoordinateFrame that = (CoordinateFrame)obj;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.getCurrentCoordinate(), that.getCurrentCoordinate());
//        builder.append(this.getPreviousCoordinate(), that.getPreviousCoordinate());
        return builder.isEquals();
    }	
}