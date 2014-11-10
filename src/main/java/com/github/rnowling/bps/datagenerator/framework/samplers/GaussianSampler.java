package com.github.rnowling.bps.datagenerator.framework.samplers;

import java.util.Random;

import com.github.rnowling.bps.datagenerator.framework.SeedFactory;

public class GaussianSampler implements Sampler<Double>
{
	double mean;
	double std;
	Random rng;
	
	public GaussianSampler(double mean, double std, SeedFactory seedFactory)
	{
		rng = new Random(seedFactory.getNextSeed());
		this.mean = mean;
		this.std = std;
	}
	
	public Double sample()
	{
		return rng.nextGaussian() * std + mean;
	}
}
