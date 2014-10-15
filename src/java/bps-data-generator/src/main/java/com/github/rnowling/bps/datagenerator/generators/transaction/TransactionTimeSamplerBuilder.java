package com.github.rnowling.bps.datagenerator.generators.transaction;

import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.samplers.ExponentialSampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.StatefulMonteCarloSampler;

public class TransactionTimeSamplerBuilder
{
	private final SeedFactory seedFactory;
	private CustomerInventory customerInventory;
	private CustomerTransactionParameters transactionParameters;
	
	public TransactionTimeSamplerBuilder(SeedFactory seedFactory)
	{
		this.seedFactory = seedFactory;
	}
	
	public void setCustomerInventory(CustomerInventory inventory)
	{
		this.customerInventory = inventory;
	}
	
	public void setCustomerTransactionParameters(CustomerTransactionParameters parameters)
	{
		this.transactionParameters = parameters;
	}
	
	public Sampler<Double> build()
	{
		double lambda = 1.0 / transactionParameters.getAverageTransactionTriggerTime();
		Sampler<Double> arrivalTimeSampler = new ExponentialSampler(lambda, seedFactory);
		Sampler<Double> proposedTimeSampler = new ProposedPurchaseTimeSampler(customerInventory,
				arrivalTimeSampler);
		
		return new StatefulMonteCarloSampler<Double>(proposedTimeSampler, 
				new TransactionTimePDF(),
				0.0,
				seedFactory);
	}
}
