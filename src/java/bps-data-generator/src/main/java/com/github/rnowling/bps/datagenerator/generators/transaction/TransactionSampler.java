package com.github.rnowling.bps.datagenerator.generators.transaction;

import com.github.rnowling.bps.datagenerator.datamodels.outputs.Customer;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Store;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Transaction;
import com.github.rnowling.bps.datagenerator.statistics.samplers.Sampler;

public class TransactionSampler implements Sampler<Transaction>
{
	private final Sampler<Purchase> purchasesSampler;
	private final Sampler<Store> storeSampler;
	private final Sampler<Integer> idSampler;
	private final Customer customer;
	
	public TransactionSampler(Customer customer, Sampler<Purchase> purchasesSampler,
			Sampler<Store> storeSampler, Sampler<Integer> idSampler)
	{
		this.customer = customer;
		this.purchasesSampler = purchasesSampler;
		this.storeSampler = storeSampler;
		this.idSampler = idSampler;
	}
	
	
	public Transaction sample() throws Exception
	{		
		Purchase purchase = purchasesSampler.sample();
		Store store = this.storeSampler.sample();
		Integer id = idSampler.sample();
			
		Transaction transaction = new Transaction(id, customer, store, purchase.getPurchaseTime(),
					purchase.getPurchasedProducts());
		
		return transaction;
	}

}
