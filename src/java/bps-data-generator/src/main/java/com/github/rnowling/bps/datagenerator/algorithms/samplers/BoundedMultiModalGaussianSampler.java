package com.github.rnowling.bps.datagenerator.algorithms.samplers;

import java.util.List;
import java.util.Random;

import com.github.rnowling.bps.datagenerator.algorithms.SeedFactory;
import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.google.common.collect.ImmutableList;

public class BoundedMultiModalGaussianSampler implements Sampler<Double>
{
	ImmutableList<Pair<Double, Double>> distributions;
	
	double min;
	double max;
	Random rng;
	
	public BoundedMultiModalGaussianSampler(List<Pair<Double, Double>> distributions, double min, double max, SeedFactory seedFactory)
	{
		rng = new Random(seedFactory.getNextSeed());
		this.distributions = ImmutableList.copyOf(distributions);
		
		this.min = min;
		this.max = max;
	}
	
	public Double sample()
	{
		int idx = rng.nextInt(distributions.size());
		
		double mean = distributions.get(idx).getFirst();
		double var = distributions.get(idx).getSecond();
		
		double value = mean + rng.nextGaussian() * Math.sqrt(var);
		
		value = Math.min(value, this.max);
		value = Math.max(value, this.min);
		
		return value;
	}

}
