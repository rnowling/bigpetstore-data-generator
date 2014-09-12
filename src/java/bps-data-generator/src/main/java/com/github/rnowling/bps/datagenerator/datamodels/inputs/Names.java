package com.github.rnowling.bps.datagenerator.datamodels.inputs;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class Names
{
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
