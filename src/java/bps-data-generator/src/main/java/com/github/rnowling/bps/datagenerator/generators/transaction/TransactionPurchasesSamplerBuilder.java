package com.github.rnowling.bps.datagenerator.generators.transaction;

import java.util.List;

import com.github.rnowling.bps.datagenerator.SeedFactory;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.Product;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.PurchasingProfile;

public class TransactionPurchasesSamplerBuilder
{
	final PurchasingProcesses purchasingProcesses;
	final CustomerTransactionParameters transactionParameters;
	final SeedFactory seedFactory;
	final CustomerInventory inventory;
	final double transactionTime;
	
	
	public TransactionPurchasesSamplerBuilder(PurchasingProfile purchasingProfile, 
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
	
	public Sampler<List<Product>> build()
	{
		Sampler<Product> purchasesSampler = new TransactionPurchasesHiddenMarkovModel(
				purchasingProcesses, transactionParameters, inventory,
				transactionTime, seedFactory);
		
		return new TransactionPurchasesSampler(purchasesSampler);
	}
	
	
}
