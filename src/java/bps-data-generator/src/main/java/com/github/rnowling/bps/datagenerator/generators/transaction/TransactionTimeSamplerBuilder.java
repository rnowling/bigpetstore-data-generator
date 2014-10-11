package com.github.rnowling.bps.datagenerator.generators.transaction;

import com.github.rnowling.bps.datagenerator.statistics.SeedFactory;

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
		return new TransactionTimeSampler(transactionParameters.averageTransactionTriggerTime,
				customerInventory, seedFactory);
	}
}
