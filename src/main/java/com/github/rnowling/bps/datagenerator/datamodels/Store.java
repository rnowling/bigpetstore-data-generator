package com.github.rnowling.bps.datagenerator.datamodels;

import java.io.Serializable;

import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;

public class Store implements Serializable
{
	private static final long serialVersionUID = 2347066623022747969L;
	
	int id;
	String name;
	ZipcodeRecord location;
	
	public Store(int id, String name, ZipcodeRecord location)
	{
		this.id = id;
		this.name = name;
		this.location = location;
	}
	
	public int getId()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public ZipcodeRecord getLocation()
	{
		return location;
	}
}
