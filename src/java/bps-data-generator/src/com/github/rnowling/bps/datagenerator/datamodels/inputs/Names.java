package com.github.rnowling.bps.datagenerator.datamodels.inputs;

import java.util.Collections;
import java.util.List;

import com.github.rnowling.bps.datagenerator.datamodels.Pair;

public class Names
{
	List<Pair<String, Double>> firstNames;
	List<Pair<String, Double>> lastNames;
	
	public Names(List<Pair<String, Double>> firstNames,
			List<Pair<String, Double>> lastNames)
	{
		this.firstNames = Collections.unmodifiableList(firstNames);
		this.lastNames = Collections.unmodifiableList(lastNames);
	}

	public List<Pair<String, Double>> getFirstNames()
	{
		return firstNames;
	}

	public List<Pair<String, Double>> getLastNames()
	{
		return lastNames;
	}
}
