package com.github.rnowling.bps.datagenerator.samplers.transaction;

import java.util.Random;

import com.github.rnowling.bps.datagenerator.statistics.SeedFactory;
import com.github.rnowling.bps.datagenerator.statistics.pdfs.ConditionalProbabilityDensityFunction;
import com.github.rnowling.bps.datagenerator.statistics.samplers.Sampler;


public class TransactionTimeSampler implements Sampler<Double>
{
	private final Sampler<Double> proposedTimeSampler;
	private final Random rng;
	private final ConditionalProbabilityDensityFunction<Double, Double> transactionTimePDF;
	private double lastTransactionTime;
	
	public TransactionTimeSampler(Sampler<Double> proposedTimeSampler,
			ConditionalProbabilityDensityFunction<Double, Double> transactionTimePDF,
			SeedFactory seedFactory)
	{
		this.transactionTimePDF = transactionTimePDF;
		this.proposedTimeSampler = proposedTimeSampler;
		
		rng = new Random(seedFactory.getNextSeed());
		
		this.lastTransactionTime = 0.0;
	}
	
	protected double proposedTimeProbability(double proposedTime)
	{
		return transactionTimePDF.probability(proposedTime, this.lastTransactionTime);
	}
	
	public Double sample() throws Exception
	{
		while(true)
		{
			double proposedTime = this.proposedTimeSampler.sample();
			double probability = this.proposedTimeProbability(proposedTime);
			double r = rng.nextDouble();
			
			if(r < probability)
			{
				this.lastTransactionTime = proposedTime;
				return proposedTime;
			}
		}
	}
	
}
