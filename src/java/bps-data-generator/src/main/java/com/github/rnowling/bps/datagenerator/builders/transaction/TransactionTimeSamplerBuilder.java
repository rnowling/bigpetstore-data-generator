package com.github.rnowling.bps.datagenerator.builders.transaction;

import com.github.rnowling.bps.datagenerator.datamodels.simulation.CustomerInventory;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.CustomerTransactionParameters;
import com.github.rnowling.bps.datagenerator.pdfs.transaction.TransactionTimePDF;
import com.github.rnowling.bps.datagenerator.samplers.transaction.ProposedPurchaseTimeSampler;
import com.github.rnowling.bps.datagenerator.samplers.transaction.TransactionTimeSampler;
import com.github.rnowling.bps.datagenerator.statistics.SeedFactory;
import com.github.rnowling.bps.datagenerator.statistics.samplers.ExponentialSampler;
import com.github.rnowling.bps.datagenerator.statistics.samplers.Sampler;

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
	
	public TransactionTimeSampler build()
	{
		double lambda = 1.0 / transactionParameters.getAverageTransactionTriggerTime();
		Sampler<Double> arrivalTimeSampler = new ExponentialSampler(lambda, seedFactory);
		Sampler<Double> proposedTimeSampler = new ProposedPurchaseTimeSampler(customerInventory,
				arrivalTimeSampler);
		
		return new TransactionTimeSampler(proposedTimeSampler, 
				new TransactionTimePDF(),
				seedFactory);
	}
}
