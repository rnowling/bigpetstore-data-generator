package com.github.rnowling.bps.datagenerator.builders.transaction;

import java.util.Collection;

import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.PurchasingProfile;
import com.github.rnowling.bps.datagenerator.samplers.transaction.CustomerInventory;
import com.github.rnowling.bps.datagenerator.samplers.transaction.Purchase;
import com.github.rnowling.bps.datagenerator.samplers.transaction.PurchasingProcesses;
import com.github.rnowling.bps.datagenerator.samplers.transaction.TransactionPurchasesHiddenMarkovModel;
import com.github.rnowling.bps.datagenerator.statistics.SeedFactory;
import com.github.rnowling.bps.datagenerator.statistics.samplers.Sampler;

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
