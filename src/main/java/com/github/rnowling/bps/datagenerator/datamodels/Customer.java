package com.github.rnowling.bps.datagenerator.datamodels;

import java.io.Serializable;

import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;

public class Customer implements Serializable
{
	int id;
	Pair<String, String> name;
	ZipcodeRecord location;
	
	public Customer(int id, Pair<String, String> name, ZipcodeRecord location)
	{
		this.id = id;
		this.name = name;
		this.location = location;
	}

	public int getId()
	{
		return id;
	}

	public Pair<String, String> getName()
	{
		return name;
	}
	
	public ZipcodeRecord getLocation()
	{
		return location;
	}
}
