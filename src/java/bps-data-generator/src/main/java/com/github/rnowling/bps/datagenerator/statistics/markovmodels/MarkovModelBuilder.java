package com.github.rnowling.bps.datagenerator.statistics.markovmodels;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;

public class MarkovModelBuilder<S>
{
	ImmutableTable.Builder<S, S, Double> transitionWeights;
	ImmutableMap.Builder<S, Double> startWeights;
	
	public MarkovModelBuilder()
	{
		transitionWeights = ImmutableTable.builder();
		startWeights = ImmutableMap.builder();
	}
	
	public static <T> MarkovModelBuilder<T> create()
	{
		return new MarkovModelBuilder<T>();
	}
	
	public void addStartState(S state, double weight)
	{
		startWeights.put(state, weight);
	}
	
	public void addTransition(S state1, S state2, double weight)
	{
		transitionWeights.put(state1, state2, weight);
	}
	
	public MarkovModel<S> build()
	{
		return new MarkovModel<S>(transitionWeights.build(), startWeights.build());
	}
	
	
}
