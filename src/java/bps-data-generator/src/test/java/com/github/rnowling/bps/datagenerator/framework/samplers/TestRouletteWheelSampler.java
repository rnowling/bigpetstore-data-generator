package com.github.rnowling.bps.datagenerator.framework.samplers;

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

import java.util.Map;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.samplers.RouletteWheelSampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;
import com.google.common.collect.ImmutableMap;

public class TestRouletteWheelSampler
{

	@Test
	public void testSample() throws Exception
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
	public void testSampleUnnormalized() throws Exception
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
