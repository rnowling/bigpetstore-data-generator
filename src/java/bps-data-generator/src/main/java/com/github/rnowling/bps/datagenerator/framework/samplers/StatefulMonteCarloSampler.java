package com.github.rnowling.bps.datagenerator.framework.samplers;

import java.util.Random;

import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.pdfs.ConditionalProbabilityDensityFunction;


public class StatefulMonteCarloSampler<T> implements Sampler<T>
{
	private final Sampler<T> stateSampler;
	private final Random rng;
	private final ConditionalProbabilityDensityFunction<T, T> acceptancePDF;
	private T currentState;
	
	public StatefulMonteCarloSampler(Sampler<T> stateGenerator,
			ConditionalProbabilityDensityFunction<T, T> acceptancePDF,
			T initialState,
			SeedFactory seedFactory)
	{
		this.acceptancePDF = acceptancePDF;
		this.stateSampler = stateGenerator;
		
		rng = new Random(seedFactory.getNextSeed());
		
		this.currentState = initialState;
	}

	public T sample() throws Exception
	{
		while(true)
		{
			T proposedState = this.stateSampler.sample();
			double probability = acceptancePDF.probability(proposedState, currentState);
			double r = rng.nextDouble();
			
			if(r < probability)
			{
				this.currentState = proposedState;
				return proposedState;
			}
		}
	}
	
}
