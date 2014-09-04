package com.github.rnowling.bps.datagenerator.algorithms.samplers;

import java.util.List;
import java.util.Random;

import com.github.rnowling.bps.datagenerator.SeedFactory;


/**
 * Optimized O(1) sampling for data known to be
 * uniformally distributed.
 * 
 * @author rnowling
 *
 * @param <T>
 */
public class UniformDiscreteSampler<T> implements Sampler<T>
{
	Random rng;
	List<T> data;
	
	public UniformDiscreteSampler(List<T> data, SeedFactory seedFactory)
	{
		rng = new Random(seedFactory.getNextSeed());
		this.data = data;
	}
	
	public T sample() throws Exception
	{
		int idx = rng.nextInt(data.size());
		return data.get(idx);
	}
}
