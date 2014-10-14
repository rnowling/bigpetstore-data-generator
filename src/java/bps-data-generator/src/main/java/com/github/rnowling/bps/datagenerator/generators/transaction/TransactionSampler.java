package com.github.rnowling.bps.datagenerator.generators.transaction;

import java.util.List;

import com.github.rnowling.bps.datagenerator.datamodels.outputs.Customer;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Store;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Transaction;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.Product;
import com.github.rnowling.bps.datagenerator.framework.samplers.ConditionalSampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;

public class TransactionSampler implements Sampler<Transaction>
{
	private final Sampler<Double> timeSampler;
	private final ConditionalSampler<List<Product>, Double> purchasesSampler;
	private final Sampler<Store> storeSampler;
	private final Sampler<Integer> idSampler;
	private final Customer customer;
	
	public TransactionSampler(Customer customer, Sampler<Double> timeSampler,
			ConditionalSampler<List<Product>, Double> purchasesSampler,
			Sampler<Store> storeSampler, Sampler<Integer> idSampler)
	{
		this.timeSampler = timeSampler;
		this.customer = customer;
		this.purchasesSampler = purchasesSampler;
		this.storeSampler = storeSampler;
		this.idSampler = idSampler;
	}
	
	
	public Transaction sample() throws Exception
	{	
		Double transactionTime = timeSampler.sample();
		List<Product> purchase = purchasesSampler.sample(transactionTime);
		Store store = this.storeSampler.sample();
		Integer id = idSampler.sample();
			
		Transaction transaction = new Transaction(id, customer, store,
				transactionTime, purchase);
		
		return transaction;
	}

}
