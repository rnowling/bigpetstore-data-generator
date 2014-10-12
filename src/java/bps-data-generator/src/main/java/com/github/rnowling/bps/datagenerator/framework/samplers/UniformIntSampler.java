package com.github.rnowling.bps.datagenerator.framework.samplers;

import java.util.Random;

import com.github.rnowling.bps.datagenerator.framework.SeedFactory;

public class UniformIntSampler implements Sampler<Integer>
{
	int lowerbound;
	int upperbound;
	Random rng;
	
	/*
	 * Upperbound is inclusive
	 */
	public UniformIntSampler(int lowerbound, int upperbound, SeedFactory seedFactory)
	{
		this.lowerbound = lowerbound;
		this.upperbound = upperbound;
		rng = new Random(seedFactory.getNextSeed());
	}
	
	public Integer sample()
	{
		int range = upperbound + 1 - lowerbound;
		return rng.nextInt(range) + lowerbound;
	}
}
