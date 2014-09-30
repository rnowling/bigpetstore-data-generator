package com.github.rnowling.bps.datagenerator.generators.transaction;

import java.util.List;

import com.github.rnowling.bps.datagenerator.SeedFactory;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.Product;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.PurchasingProfile;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class TransactionPurchasesGenerator
{
	final PurchasingProcesses purchasingProcesses;
	final CustomerTransactionParameters transactionParameters;
	final SeedFactory seedFactory;
	
	
	public TransactionPurchasesGenerator(PurchasingProfile purchasingProfile, 
			CustomerTransactionParameters transactionParameters, SeedFactory seedFactory)
	{
		PurchasingProcessesBuilder builder = new PurchasingProcessesBuilder(seedFactory);
		builder.setPurchasingProfile(purchasingProfile);
		this.purchasingProcesses = builder.build();
		
		this.transactionParameters = transactionParameters;
		this.seedFactory = seedFactory;
	}
	
	public ImmutableList<Product> simulate(CustomerInventory inventory, double transactionTime)
	{
		Sampler<Product> productSampler = new TransactionPurchasesHiddenMarkovModel(purchasingProcesses,
				transactionParameters, inventory, transactionTime, seedFactory);
		
		List<Product> purchasedProducts = Lists.newArrayList();
		
		while(true)
		{
			Product product = productSampler.sample();
			if(product == null)
			{
				return ImmutableList.copyOf(purchasedProducts);
			}
			
			purchasedProducts.add(product);
		}
	}
	
}
