package com.github.rnowling.bps.datagenerator.algorithms.markovmodels;

import java.util.Map;
import java.util.Set;

import com.github.rnowling.bps.datagenerator.SeedFactory;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.RouletteWheelSampler;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.UniformDiscreteSampler;
import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

public class MarkovModelBuilder<S>
{
	HashBasedTable<S, S, Double> transitionWeights;
	SeedFactory seedFactory;
	Set<S> states;
	
	public MarkovModelBuilder(SeedFactory seedFactory)
	{
		transitionWeights = HashBasedTable.create();
		this.seedFactory = seedFactory;
		this.states = Sets.newHashSet();
	}
	
	public void addTransition(S state1, S state2, double weight)
	{
		states.add(state1);
		states.add(state2);
		transitionWeights.put(state1, state2, weight);
	}
	
	public MarkovModel<S> build()
	{
		ImmutableMap.Builder<S, Sampler<S>> transitionSamplerBuilder = new ImmutableMap.Builder<S, Sampler<S>>();
		
		for(Map.Entry<S, Map<S, Double>> row : transitionWeights.rowMap().entrySet())
		{
			// RouletteWheelSampler normalizes for us
			Sampler<S> rowSampler = RouletteWheelSampler.create(row.getValue(), seedFactory);
			transitionSamplerBuilder.put(row.getKey(), rowSampler);
		}
		Sampler<S> startSampler = new UniformDiscreteSampler<S>(states, seedFactory);
		
		return new MarkovModel<S>(transitionSamplerBuilder.build(), startSampler);
	}
	
	
}
