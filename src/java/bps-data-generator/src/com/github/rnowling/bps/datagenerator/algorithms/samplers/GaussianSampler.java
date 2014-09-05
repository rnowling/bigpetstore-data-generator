package com.github.rnowling.bps.datagenerator.algorithms.samplers;

import java.util.Random;

import com.github.rnowling.bps.datagenerator.SeedFactory;

public class GaussianSampler implements Sampler<Double>
{
	double mean;
	double var;
	Random rng;
	
	public GaussianSampler(double mean, double var, SeedFactory seedFactory)
	{
		rng = new Random(seedFactory.getNextSeed());
		this.mean = mean;
		this.var = var;
	}
	
	public Double sample()
	{
		return rng.nextGaussian() * Math.sqrt(var) + mean;
	}
}
