package com.github.rnowling.bps.datagenerator.framework.samplers;

import java.util.Random;

import com.github.rnowling.bps.datagenerator.framework.SeedFactory;

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
