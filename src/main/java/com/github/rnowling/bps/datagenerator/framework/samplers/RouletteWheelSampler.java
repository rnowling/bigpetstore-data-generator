package com.github.rnowling.bps.datagenerator.framework.samplers;

import java.util.Collection;
import java.util.Map;
import java.util.Random;

import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.pdfs.DiscretePDF;
import com.github.rnowling.bps.datagenerator.framework.pdfs.ProbabilityDensityFunction;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

public class RouletteWheelSampler<T> implements Sampler<T>
{
	Random rng;
	final ImmutableList<Pair<T, Double>> wheel;
	
	public static <T> RouletteWheelSampler<T> create(Map<T, Double> domainWeights, SeedFactory factory)
	{
		return new RouletteWheelSampler<T>(domainWeights, factory);
	}
	
	public static <T> RouletteWheelSampler<T> create(DiscretePDF<T> pdf, SeedFactory factory)
	{
		return new RouletteWheelSampler<T>(pdf.getData(), pdf, factory);
	}
	
	public static <T> RouletteWheelSampler<T> create(Collection<T> data, ProbabilityDensityFunction<T> pdf, SeedFactory factory)
	{
		return new RouletteWheelSampler<T>(data, pdf, factory);
	}
	
	public static <T> RouletteWheelSampler<T> createUniform(Collection<T> data, SeedFactory factory)
	{
		Map<T, Double> pdf = Maps.newHashMap();
		for(T datum : data)
		{
			pdf.put(datum, 1.0);
		}
		
		return create(pdf, factory);
	}
	
	public RouletteWheelSampler(Map<T, Double> domainWeights, SeedFactory factory)
	{
		this.rng = new Random(factory.getNextSeed());
		this.wheel = this.normalize(domainWeights);
	}
	
	public RouletteWheelSampler(Collection<T> data, ProbabilityDensityFunction<T> pdf, SeedFactory factory)
	{
		this.rng = new Random(factory.getNextSeed());
		
		Map<T, Double> domainWeights = Maps.newHashMap();
		for(T datum : data)
		{
			double prob = pdf.probability(datum);
			domainWeights.put(datum, prob);
		}
		
		this.wheel = this.normalize(domainWeights);
	}
	
	private ImmutableList<Pair<T, Double>> normalize(Map<T, Double> domainWeights)
	{
		double weightSum = 0.0;
		for(Map.Entry<T, Double> entry : domainWeights.entrySet())
		{
			weightSum += entry.getValue();
		}
		
		double cumProb = 0.0;
		ImmutableList.Builder<Pair<T, Double>> builder = ImmutableList.builder();
		for(Map.Entry<T, Double> entry : domainWeights.entrySet())
		{
			double prob = entry.getValue() / weightSum;
			cumProb += prob;
			
			builder.add(Pair.create(entry.getKey(), cumProb));
		}
		
		return builder.build();
	}
	
	public T sample()
	{
		double r = rng.nextDouble();
		for(Pair<T, Double> cumProbPair : wheel)
			if(r < cumProbPair.getSecond())
				return cumProbPair.getFirst();
		
		throw new IllegalStateException("Invalid state -- RouletteWheelSampler should never fail to sample!");
	}
}
