package com.github.rnowling.bps.datagenerator.framework.samplers;

import java.util.Random;

import com.github.rnowling.bps.datagenerator.framework.SeedFactory;

public class UniformSampler implements Sampler<Double>
{
	final Random rng;
	final double lowerbound;
	final double upperbound;
	
	public UniformSampler(SeedFactory seedFactory)
	{
		rng = new Random(seedFactory.getNextSeed());
		lowerbound = 0.0;
		upperbound = 1.0;
	}
	
	public UniformSampler(double lowerbound, double upperbound, SeedFactory seedFactory)
	{
		rng = new Random(seedFactory.getNextSeed());
		this.lowerbound = lowerbound;
		this.upperbound = upperbound;
	}
	
	public Double sample()
	{
		return (upperbound - lowerbound) * rng.nextDouble() + lowerbound;
	}
}
