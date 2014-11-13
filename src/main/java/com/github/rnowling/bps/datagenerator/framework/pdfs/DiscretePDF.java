package com.github.rnowling.bps.datagenerator.framework.pdfs;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;

public class DiscretePDF<T> implements ProbabilityDensityFunction<T>
{
	private final ImmutableMap<T, Double> probabilities;
	
	public DiscretePDF(Map<T, Double> probabilities)
	{
		this.probabilities = ImmutableMap.copyOf(probabilities);
	}
	
	public Set<T> getData()
	{
		return probabilities.keySet();
	}
	
	public double probability(T value)
	{
		if(probabilities.containsKey(value))
		{
			return probabilities.get(value);
		}
		
		return 0.0;
	}
}
