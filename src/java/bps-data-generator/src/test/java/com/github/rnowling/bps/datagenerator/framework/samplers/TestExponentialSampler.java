package com.github.rnowling.bps.datagenerator.framework.samplers;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.samplers.ExponentialSampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;

public class TestExponentialSampler
{

	@Test
	public void testSample() throws Exception
	{
		double lambda = 1.0 / 2.0;
		
		SeedFactory seedFactory = new SeedFactory(1234);
		
		Sampler<Double> sampler = new ExponentialSampler(lambda, seedFactory);
		
		Double result = sampler.sample();
		
		assertTrue(result >= 0.0);
	}
}
