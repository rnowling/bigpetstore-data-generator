package com.github.rnowling.bps.datagenerator.framework.markovmodels;

import java.io.Serializable;
import java.util.Map;

public class MarkovModel<T> implements Serializable
{
	final Map<T, Map<T, Double>> transitionWeights;
	final Map<T, Double> startWeights;
	
	public MarkovModel(Map<T, Map<T, Double>> transitionWeights, Map<T, Double> startWeights)
	{
		this.transitionWeights = transitionWeights;
		this.startWeights = startWeights;
	}

	public Map<T, Map<T, Double>> getTransitionWeights()
	{
		return transitionWeights;
	}

	public Map<T, Double> getStartWeights()
	{
		return startWeights;
	}
}
