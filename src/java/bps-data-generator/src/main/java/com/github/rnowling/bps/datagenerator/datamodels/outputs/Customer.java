package com.github.rnowling.bps.datagenerator.datamodels.outputs;

import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;

public class Customer
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