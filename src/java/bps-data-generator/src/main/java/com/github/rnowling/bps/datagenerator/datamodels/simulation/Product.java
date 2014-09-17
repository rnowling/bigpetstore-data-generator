package com.github.rnowling.bps.datagenerator.datamodels.simulation;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

public class Product
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
