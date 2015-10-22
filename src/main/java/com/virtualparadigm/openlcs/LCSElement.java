package com.virtualparadigm.openlcs;

public abstract class LCSElement<T extends Comparable<T>> implements Comparable<T>
{
	private T value;
	
	public LCSElement()
	{
		super();
	}
	public LCSElement(T data)
	{
		super();
		this.value = data;
	}

	public T getValue()
	{
		return value;
	}
	
	@Override
	public int compareTo(T t)
	{
		return this.getValue().compareTo(t);
	}
	
//	private void setData(T data)
//	{
//		this.data = data;
//	}
	
//    @Override
//    public String toString()
//    {
//        return ReflectionToStringBuilder.toString(this);
//    }
//	
//    @Override
//    public int hashCode()
//    {
//        HashCodeBuilder builder = new HashCodeBuilder();
//        builder.append(this.getValue());
//        return builder.toHashCode();        
//    }
//
//    @Override
//    public boolean equals(Object obj)
//    {
//        if (obj == null)
//        {
//            return false;
//        }
//        if (obj == this)
//        {
//            return true;
//        }
//        if (obj.getClass() != getClass())
//        {
//            return false;
//        }
//        LCSElement<T> that = (LCSElement<T>)obj;
//        EqualsBuilder builder = new EqualsBuilder();
//        builder.append(this.getValue(), that.getValue());
//        return builder.isEquals();
//    }	
	
	public abstract String asString();
}
