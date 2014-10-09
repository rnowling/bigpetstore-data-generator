package com.github.rnowling.bps.datagenerator.generators.transaction;

import java.util.Collection;

import com.github.rnowling.bps.datagenerator.SeedFactory;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.PurchasingProfile;

public class TransactionPurchasesSamplerBuilder
{
	final SeedFactory seedFactory;
	final Collection<ProductCategory> productCategories;
	final PurchasingProfile purchasingProfile;
	
	public TransactionPurchasesSamplerBuilder(Collection<ProductCategory> productCategories,
			PurchasingProfile purchasingProfile,
			SeedFactory seedFactory)
	{
		this.seedFactory = seedFactory;
		this.productCategories = productCategories;
		this.purchasingProfile = purchasingProfile;
	}
	
	public Sampler<Purchase> build() throws Exception
	{
		CustomerTransactionParametersSamplerBuilder builder = new CustomerTransactionParametersSamplerBuilder(seedFactory);
		CustomerTransactionParameters transactionParameters = builder.build().sample();
		
		CustomerInventoryBuilder inventoryBuilder = new CustomerInventoryBuilder(transactionParameters,
				seedFactory);
		inventoryBuilder.addAllProductCategories(productCategories);
		CustomerInventory customerInventory = inventoryBuilder.build();
		
		PurchasingProcessesBuilder processesBuilder = new PurchasingProcessesBuilder(seedFactory);
		processesBuilder.setPurchasingProfile(purchasingProfile);
		PurchasingProcesses processes = processesBuilder.build();
		
		TransactionTimeSamplerBuilder timeSamplerBuilder = new TransactionTimeSamplerBuilder(this.seedFactory);
		timeSamplerBuilder.setCustomerInventory(customerInventory);
		timeSamplerBuilder.setCustomerTransactionParameters(transactionParameters);
		Sampler<Double> timeSampler = timeSamplerBuilder.build();
		
		Sampler<Purchase> sampler = new TransactionPurchasesHiddenMarkovModel(processes,
				transactionParameters.getAveragePurchaseTriggerTime(), customerInventory, timeSampler,
				this.seedFactory);
		
		return sampler;
	}
}
