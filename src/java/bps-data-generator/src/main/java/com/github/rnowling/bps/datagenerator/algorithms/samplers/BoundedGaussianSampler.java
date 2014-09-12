package com.github.rnowling.bps.datagenerator.algorithms.samplers;

import java.util.Random;

import com.github.rnowling.bps.datagenerator.SeedFactory;

public class BoundedGaussianSampler implements Sampler<Double>
{
	double mean;
	double var;
	double min;
	double max;
	Random rng;
	
	public BoundedGaussianSampler(double mean, double var, double min, double max, SeedFactory seedFactory)
	{
		rng = new Random(seedFactory.getNextSeed());
		
		this.mean = mean;
		this.var = var;
		this.min = min;
		this.max = max;
	}
	
	public Double sample()
	{
		double v = mean + rng.nextGaussian() * Math.sqrt(var);
		v = Math.min(v, this.max);
		v = Math.max(v, this.min);
		
		return v;
	}

}
