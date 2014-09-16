package com.github.rnowling.bps.datagenerator.algorithms.samplers;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.SeedFactory;

public class TestUniformIntSampler
{

	@Test
	public void testSample()
	{
		int upperbound = 10;
		int lowerbound = 1;
		
		SeedFactory seedFactory = new SeedFactory(1234);
		
		Sampler<Integer> sampler = new UniformIntSampler(lowerbound, upperbound, seedFactory);
		
		Integer result = sampler.sample();
		
		assertTrue(result >= lowerbound);
		assertTrue(result <= upperbound);
	}
	
	@Test
	public void testSampleInclusive()
	{
		int upperbound = 2;
		int lowerbound = 1;
		
		SeedFactory seedFactory = new SeedFactory(1234);
		
		Sampler<Integer> sampler = new UniformIntSampler(lowerbound, upperbound, seedFactory);
		
		Integer result = sampler.sample();
		
		assertTrue(result >= lowerbound);
		assertTrue(result <= upperbound);
	}

}
