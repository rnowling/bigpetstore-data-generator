package com.github.rnowling.bps.datagenerator.algorithms.samplers;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import com.github.rnowling.bps.datagenerator.SeedFactory;
import com.google.common.collect.ImmutableList;


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
	
	public UniformDiscreteSampler(Collection<T> data, SeedFactory seedFactory)
	{
		rng = new Random(seedFactory.getNextSeed());
		this.data = ImmutableList.copyOf(data);
	}
	
	public T sample()
	{
		int idx = rng.nextInt(data.size());
		return data.get(idx);
	}
}
