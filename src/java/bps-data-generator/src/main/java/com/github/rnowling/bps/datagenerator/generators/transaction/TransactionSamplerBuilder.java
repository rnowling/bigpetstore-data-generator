package com.github.rnowling.bps.datagenerator.generators.transaction;

import java.util.Collection;
import java.util.Map;

import com.github.rnowling.bps.datagenerator.SeedFactory;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.RouletteWheelSampler;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.SequenceSampler;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Customer;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Store;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Transaction;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.PurchasingProfile;
import com.google.common.collect.Maps;

public class TransactionSamplerBuilder
{
	private final Sampler<Store> storeSampler;
	private final CustomerTransactionParameters transactionParameters;
	private final CustomerInventory customerInventory;
	private final Customer customer;
	private final PurchasingProfile purchasingProfile;
	private final SeedFactory seedFactory;
	
	double lastTransactionTime;
	int transactionCount;
	
	public TransactionSamplerBuilder(Collection<Store> stores,
			Collection<ProductCategory> productCategories, 
			Customer customer,
			PurchasingProfile purchasingProfile,
			SeedFactory seedFactory) throws Exception
	{
		this.customer = customer;
		this.seedFactory = seedFactory;
		this.purchasingProfile = purchasingProfile;
		lastTransactionTime = 0.0;
		transactionCount = 0;
		
		Map<Store, Double> storePDF = Maps.newHashMap();
		for(Store store : stores)
		{
			storePDF.put(store, 1.0 / stores.size());
		}
		storeSampler = RouletteWheelSampler.create(storePDF, seedFactory);
		
		CustomerTransactionParametersSamplerBuilder builder = new CustomerTransactionParametersSamplerBuilder(seedFactory);
		transactionParameters = builder.build().sample();
		
		CustomerInventoryBuilder inventoryBuilder = new CustomerInventoryBuilder(transactionParameters,
				seedFactory);
		inventoryBuilder.addAllProductCategories(productCategories);
		customerInventory = inventoryBuilder.build();
		
		
	}
	
	protected PurchasingProcesses buildPurchasingProcesses()
	{
		PurchasingProcessesBuilder builder = new PurchasingProcessesBuilder(seedFactory);
		builder.setPurchasingProfile(purchasingProfile);
		
		PurchasingProcesses processes = builder.build();
		
		return processes;
	}
	
	protected Sampler<Double> buildTransactionTimeSampler() throws Exception
	{
		TransactionTimeSamplerBuilder timeSamplerBuilder = new TransactionTimeSamplerBuilder(this.seedFactory);
		timeSamplerBuilder.setCustomerInventory(this.customerInventory);
		timeSamplerBuilder.setCustomerTransactionParameters(this.transactionParameters);
		
		return timeSamplerBuilder.build();
	}
	
	protected Sampler<Purchase> buildPurchasesSampler() throws Exception
	{
		PurchasingProcesses processes = buildPurchasingProcesses();
		
		Sampler<Purchase> sampler = new TransactionPurchasesHiddenMarkovModel(processes,
				this.transactionParameters, this.customerInventory, buildTransactionTimeSampler(),
				this.seedFactory);
		
		return sampler;
	}
	
	public Sampler<Transaction> build() throws Exception
	{
		return new TransactionSampler(customer, buildPurchasesSampler(), storeSampler, new SequenceSampler());
	}
}
