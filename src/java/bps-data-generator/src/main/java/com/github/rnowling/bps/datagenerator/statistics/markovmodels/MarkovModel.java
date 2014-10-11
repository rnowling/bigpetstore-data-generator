package com.github.rnowling.bps.datagenerator.statistics.markovmodels;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;

public class MarkovModel<T>
{
	final ImmutableTable<T, T, Double> transitionWeights;
	final ImmutableMap<T, Double> startWeights;
	
	public MarkovModel(Table<T, T, Double> transitionWeights, Map<T, Double> startWeights)
	{
		this.transitionWeights = ImmutableTable.copyOf(transitionWeights);
		this.startWeights = ImmutableMap.copyOf(startWeights);
	}

	public ImmutableTable<T, T, Double> getTransitionWeights()
	{
		return transitionWeights;
	}

	public ImmutableMap<T, Double> getStartWeights()
	{
		return startWeights;
	}
}
