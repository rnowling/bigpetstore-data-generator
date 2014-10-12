package com.github.rnowling.bps.datagenerator.framework.samplers;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.UniformIntSampler;

public class TestUniformIntSampler
{

	@Test
	public void testSample() throws Exception
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
	public void testSampleInclusive() throws Exception
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
