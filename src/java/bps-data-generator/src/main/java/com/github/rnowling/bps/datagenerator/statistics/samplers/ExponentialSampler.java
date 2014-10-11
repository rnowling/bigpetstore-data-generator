package com.github.rnowling.bps.datagenerator.statistics.samplers;

import java.util.Random;

import com.github.rnowling.bps.datagenerator.statistics.SeedFactory;

public class ExponentialSampler implements Sampler<Double>
{
	final private Random rng;
	final private double lambda;
	
	public ExponentialSampler(double lambda, SeedFactory seedFactory)
	{
		rng = new Random(seedFactory.getNextSeed());
		this.lambda = lambda;
	}
	
	public Double sample()
	{
		return - Math.log(1.0 - rng.nextDouble()) / lambda;
	}
}
