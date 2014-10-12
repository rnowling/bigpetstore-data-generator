package com.github.rnowling.bps.datagenerator.statistics.samplers;

import java.util.Random;

import com.github.rnowling.bps.datagenerator.statistics.SeedFactory;

public class UniformSampler implements Sampler<Double>
{
	final Random rng;
	
	public UniformSampler(SeedFactory seedFactory)
	{
		rng = new Random(seedFactory.getNextSeed());
	}
	
	public Double sample()
	{
		return rng.nextDouble();
	}
}
