package com.github.rnowling.bps.datagenerator.datamodels.outputs;

import java.io.Serializable;

import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;

public class Store implements Serializable
{
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
