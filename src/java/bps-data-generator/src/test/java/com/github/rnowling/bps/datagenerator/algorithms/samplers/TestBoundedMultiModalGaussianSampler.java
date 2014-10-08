package com.github.rnowling.bps.datagenerator.algorithms.samplers;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.SeedFactory;
import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.google.common.collect.Lists;

public class TestBoundedMultiModalGaussianSampler
{

	@Test
	public void testSample() throws Exception
	{
		double upperbound = 10.0;
		double lowerbound = 1.0;
		
		List<Pair<Double, Double>> distributions = Lists.newArrayList(Pair.create(2.0, 2.0), Pair.create(7.5, 2.0));
		
		SeedFactory seedFactory = new SeedFactory(1234);
		
		Sampler<Double> sampler = new BoundedMultiModalGaussianSampler(distributions, lowerbound, upperbound, seedFactory);
		
		Double result = sampler.sample();
		
		assertTrue(result >= lowerbound);
		assertTrue(result <= upperbound);
	}
}
