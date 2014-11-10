package com.github.rnowling.bps.datagenerator.framework.samplers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.SequenceSampler;

public class TestSequenceSampler
{

	@Test
	public void testSample() throws Exception
	{
		Sampler<Integer> sampler = new SequenceSampler(0, 10, 1);
		
		for(int i = 0; i < 10; i++)
		{
			Integer value = sampler.sample();
			assertEquals( (int) value, i);
		}
	}
}
