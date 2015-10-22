package com.virtualparadigm.openlcs;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class LCSStringElement extends LCSElement<String> implements Serializable
{
	private static final long serialVersionUID = 1L;
//	private String value;
	
	public LCSStringElement(String value)
	{
		super(value);
//		this.value = value;
	}

//	public String getValue()
//	{
//		return value;
//	}
//	private void setValue(String value)
//	{
//		this.value = value;
//	}
//
    @Override
    public String asString()
    {
    	return this.getValue();
    }
    
//	@Override
//	public int compareTo(String str)
//	{
//		return this.getValue().compareTo(str);
//	}
    
	// ==============================================
	// UTILITY METHODS
	// ==============================================
    @Override
    public String toString()
    {
        return ReflectionToStringBuilder.toString(this);
    }
    
    @Override
    public int hashCode()
    {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(this.getValue());
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
        LCSStringElement that = (LCSStringElement)obj;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.getValue(), that.getValue());
        return builder.isEquals();
    }
	
	
	
	public static LCSStringElement[] createSequence(String[] strings)
	{
		LCSStringElement[] lcsStringElements = null;
		if(strings != null)
		{
			lcsStringElements = new LCSStringElement[strings.length];
//			lcsStringElements = new LCSStringElement[strings.length+1];
//			lcsStringElements[0] = new LCSStringElement("0");
			for(int i=0; i<strings.length; i++)
			{
				lcsStringElements[i] = new LCSStringElement(strings[i]);
			}
		}
		return lcsStringElements;
	}
	
	public static LCSStringElement[] createCharSequence(String str)
	{
		LCSStringElement[] lcsStringElements = null;
		if(str != null)
		{
			lcsStringElements = new LCSStringElement[str.length()];
//			lcsStringElements = new LCSStringElement[str.length()+1];
//			lcsStringElements[0] = new LCSStringElement("0");
			for(int i=0; i<str.length(); i++)
			{
				lcsStringElements[i] = new LCSStringElement(String.valueOf(str.charAt(i)));
			}
		}
		return lcsStringElements;
	}
	
	public static void main(String[] args)
	{
		LCSGrid<String> lcsGrid = new LCSGrid<String>(LCSStringElement.createCharSequence("ABCDEF"), LCSStringElement.createCharSequence("XYZ"));
		lcsGrid.printGrid(5);
	}

	
}