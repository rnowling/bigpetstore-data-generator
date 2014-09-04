package com.github.rnowling.bps.datagenerator.algorithms.samplers;

import java.util.List;
import java.util.Random;
import java.util.Vector;

import com.github.rnowling.bps.datagenerator.SeedFactory;
import com.github.rnowling.bps.datagenerator.algorithms.pdfs.ProbabilityDensityFunction;
import com.github.rnowling.bps.datagenerator.datamodels.Pair;

public class RouletteWheelSampler<T> implements Sampler<T>
{
	Random rng;
	List<Pair<T, Double>> domainCumProb;
	
	public RouletteWheelSampler(List<Pair<T, Double>> domainWeights, SeedFactory factory)
	{
		this.rng = new Random(factory.getNextSeed());
		this.domainCumProb = this.normalize(domainWeights);
	}
	
	public RouletteWheelSampler(List<T> data, ProbabilityDensityFunction<T> pdf, SeedFactory factory)
	{
		this.rng = new Random(factory.getNextSeed());
		
		List<Pair<T, Double>> domainWeights = new Vector<Pair<T, Double>>();
		for(T datum : data)
		{
			double prob = pdf.probability(datum);
			domainWeights.add(new Pair<T, Double>(datum, prob));
		}
		
		this.domainCumProb = this.normalize(domainWeights);
	}
	
	private List<Pair<T, Double>> normalize(List<Pair<T, Double>> domainWeights)
	{
		double weightSum = 0.0;
		for(Pair<T, Double> pair : domainWeights)
		{
			weightSum += pair.getSecond();
		}
		
		double cumProb = 0.0;
		List<Pair<T, Double>> domainCumProb = new Vector<Pair<T, Double>>();
		for(Pair<T, Double> weightPair : domainWeights)
		{
			double prob = weightPair.getSecond() / weightSum;
			cumProb += prob;
			
			Pair<T, Double> cumProbPair = new Pair<T, Double>(weightPair.getFirst(), cumProb);
			domainCumProb.add(cumProbPair);
		}
		
		return domainCumProb;
	}
	
	public T sample() throws Exception
	{
		double r = rng.nextDouble();
		for(Pair<T, Double> cumProbPair : domainCumProb)
			if(r < cumProbPair.getSecond())
				return cumProbPair.getFirst();
		
		throw new Exception("Invalid state -- RouletteWheelSampler should never fail to sample!");
	}
}
