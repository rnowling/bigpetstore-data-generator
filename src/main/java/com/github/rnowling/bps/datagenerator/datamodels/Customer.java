package com.github.rnowling.bps.datagenerator.datamodels;

import java.io.Serializable;

import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;

public class Customer implements Serializable
{
	private static final long serialVersionUID = 5739806281335931258L;
	
	int id;
	Pair<String, String> name;
	ZipcodeRecord location;
	Store store;
	
	public Customer(int id, Pair<String, String> name, Store store, ZipcodeRecord location)
	{
		this.id = id;
		this.name = name;
		this.location = location;
		this.store = store;
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
	
	public Store getStore()
	{
		return store;
	}
}
