package com.github.rnowling.bps.datagenerator.generators.transaction;

import java.util.List;

import com.github.rnowling.bps.datagenerator.SeedFactory;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.Product;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.PurchasingProfile;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class TransactionPurchasesSampler implements Sampler<ImmutableList<Product>>
{
	final PurchasingProcesses purchasingProcesses;
	final CustomerTransactionParameters transactionParameters;
	final SeedFactory seedFactory;
	final CustomerInventory inventory;
	final double transactionTime;
	
	
	public TransactionPurchasesSampler(PurchasingProfile purchasingProfile, 
			CustomerTransactionParameters transactionParameters, 
			CustomerInventory inventory, double transactionTime, SeedFactory seedFactory)
	{
		PurchasingProcessesBuilder builder = new PurchasingProcessesBuilder(seedFactory);
		builder.setPurchasingProfile(purchasingProfile);
		this.purchasingProcesses = builder.build();
		
		this.transactionParameters = transactionParameters;
		this.seedFactory = seedFactory;
		
		this.inventory = inventory;
		this.transactionTime = transactionTime;
	}
	
	public ImmutableList<Product> sample() throws Exception
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
