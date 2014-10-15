package com.github.rnowling.bps.datagenerator.framework.samplers;

import java.util.Random;

import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.pdfs.ProbabilityDensityFunction;


public class MonteCarloSampler<T> implements Sampler<T>
{
	private final Sampler<T> stateSampler;
	private final Random rng;
	private final ProbabilityDensityFunction<T> acceptancePDF;
	
	public MonteCarloSampler(Sampler<T> stateGenerator,
			ProbabilityDensityFunction<T> acceptancePDF,
			SeedFactory seedFactory)
	{
		this.acceptancePDF = acceptancePDF;
		this.stateSampler = stateGenerator;
		
		rng = new Random(seedFactory.getNextSeed());
	}

	public T sample() throws Exception
	{
		while(true)
		{
			T proposedState = this.stateSampler.sample();
			double probability = acceptancePDF.probability(proposedState);
			double r = rng.nextDouble();
			
			if(r < probability)
			{
				return proposedState;
			}
		}
	}
	
}
