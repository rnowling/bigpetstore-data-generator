package com.github.rnowling.bps.datagenerator.algorithms.samplers;

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.*;

import java.util.Map;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.SeedFactory;
import com.google.common.collect.ImmutableMap;

public class TestRouletteWheelSampler
{

	@Test
	public void testSample()
	{
		Map<String, Double> dataPoints = ImmutableMap.of(
				"a", 0.25,
				"b", 0.25,
				"c", 0.25,
				"d", 0.25
				);
		
		SeedFactory seedFactory = new SeedFactory(1234);
		
		Sampler<String> sampler = new RouletteWheelSampler<String>(dataPoints, seedFactory);
		
		String result = sampler.sample();
		
		assertThat(dataPoints.keySet(), hasItem(result));
	}
	
	@Test
	public void testSampleUnnormalized()
	{
		Map<String, Double> dataPoints = ImmutableMap.of(
				"a", 1.0,
				"b", 1.0,
				"c", 1.0,
				"d", 1.0
				);
		
		SeedFactory seedFactory = new SeedFactory(1234);
		
		Sampler<String> sampler = new RouletteWheelSampler<String>(dataPoints, seedFactory);
		
		String result = sampler.sample();
		
		assertThat(dataPoints.keySet(), hasItem(result));
	}

}
