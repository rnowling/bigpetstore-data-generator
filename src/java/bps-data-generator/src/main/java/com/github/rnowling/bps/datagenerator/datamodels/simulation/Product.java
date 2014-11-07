package com.github.rnowling.bps.datagenerator.datamodels.simulation;

import java.io.Serializable;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

public class Product implements Serializable
{
	ImmutableMap<String, Object> fields;
	
	public Product(Map<String, Object> fields)
	{
		this.fields = ImmutableMap.copyOf(fields);
	}
	
	public ImmutableSet<String> getFieldNames()
	{
		return fields.keySet();
	}
	
	public Object getFieldValue(String fieldName)
	{
		return fields.get(fieldName);
	}
	
	public String getFieldValueAsString(String fieldName)
	{
		return fields.get(fieldName).toString();
	}
	
	public Double getFieldValueAsDouble(String fieldName)
	{
		Object value = getFieldValue(fieldName);
		try
		{
			Double doubleValue = (Double) value;
			return doubleValue;
		}
		catch(ClassCastException e)
		{
			return null;
		}
	}
	
	public Long getFieldValueAsLong(String fieldName)
	{
		Object value = getFieldValue(fieldName);
		try
		{
			Long longValue = (Long) value;
			return longValue;
		}
		catch(ClassCastException e)
		{
			try
			{
				Integer intValue = (Integer) value;
				return new Long(intValue);
			}
			catch(ClassCastException f)
			{
				return null;
			}
		}
	}
	
	public String toString()
	{
		String str = "";
		for(Map.Entry<String, Object> entry : fields.entrySet())
		{
			str += entry.getKey() + "=" + entry.getValue() + ";";
		}
		
		return str;
	}
}
