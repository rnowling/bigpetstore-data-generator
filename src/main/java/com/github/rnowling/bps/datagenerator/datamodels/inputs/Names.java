package com.github.rnowling.bps.datagenerator.datamodels.inputs;

import java.io.Serializable;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class Names implements Serializable
{
	private static final long serialVersionUID = 2731634747628534453L;
	
	final ImmutableMap<String, Double> firstNames;
	final ImmutableMap<String, Double> lastNames;
	
	public Names(Map<String, Double> firstNames,
			Map<String, Double> lastNames)
	{
		this.firstNames = ImmutableMap.copyOf(firstNames);
		this.lastNames = ImmutableMap.copyOf(lastNames);
	}

	public ImmutableMap<String, Double> getFirstNames()
	{
		return firstNames;
	}

	public ImmutableMap<String, Double> getLastNames()
	{
		return lastNames;
	}
}
