package com.github.rnowling.bps.datagenerator.samplers.transaction;

import java.util.Random;

import com.github.rnowling.bps.datagenerator.datamodels.simulation.CustomerInventory;
import com.github.rnowling.bps.datagenerator.statistics.SeedFactory;
import com.github.rnowling.bps.datagenerator.statistics.pdfs.ConditionalProbabilityDensityFunction;
import com.github.rnowling.bps.datagenerator.statistics.samplers.ExponentialSampler;
import com.github.rnowling.bps.datagenerator.statistics.samplers.Sampler;


public class TransactionTimeSampler implements Sampler<Double>
{
	private final ExponentialSampler transactionTimeDiffSampler;
	private final Random rng;
	private final CustomerInventory customerInventory;
	private final ConditionalProbabilityDensityFunction<Double, Double> transactionTimePDF;
	private double lastTransactionTime;
	
	public TransactionTimeSampler(double averageTransactionTriggerTime,
			ConditionalProbabilityDensityFunction<Double, Double> transactionTimePDF,
			CustomerInventory customerInventory,
			SeedFactory seedFactory)
	{
		this.transactionTimePDF = transactionTimePDF;
		
		double lambda = 1.0 / averageTransactionTriggerTime;
		this.transactionTimeDiffSampler = new ExponentialSampler(lambda, seedFactory);
		rng = new Random(seedFactory.getNextSeed());
		
		this.customerInventory = customerInventory;
		this.lastTransactionTime = 0.0;
	}
	
	protected double categoryProposedTime(double exhaustionTime)
	{
		return Math.max(exhaustionTime - transactionTimeDiffSampler.sample(), 0.0);
	}
	
	protected double proposeTransactionTime()
	{
		double minProposedTime = Double.MAX_VALUE;
		for(Double exhaustionTime : this.customerInventory.getExhaustionTimes().values())
		{
			double proposedTime = this.categoryProposedTime(exhaustionTime);
			minProposedTime = Math.min(proposedTime, minProposedTime);
		}
		
		return minProposedTime;
	}
	
	protected double proposedTimeProbability(double proposedTime)
	{
		return transactionTimePDF.probability(proposedTime, this.lastTransactionTime);
	}
	
	public Double sample()
	{
		while(true)
		{
			double proposedTime = this.proposeTransactionTime();
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
