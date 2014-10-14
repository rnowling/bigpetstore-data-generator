package com.github.rnowling.bps.datagenerator.generators.transaction;

import java.util.Collection;

import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.framework.wfs.ConditionalWeightFunction;
import com.github.rnowling.bps.datagenerator.generators.purchasingprofile.PurchasingProfile;

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
		
		System.out.println("Number of pets: " + transactionParameters.countPets());
		System.out.println("Average Transaction Trigger Time: " + transactionParameters.getAverageTransactionTriggerTime());
		System.out.println("Average Purchase Trigger Time: " + transactionParameters.getAveragePurchaseTriggerTime());
		
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
		
		ConditionalWeightFunction<Double, Double> categoryWF =
				new CategoryWeightFunction(transactionParameters.getAveragePurchaseTriggerTime());
		
		Sampler<Purchase> sampler = new TransactionPurchasesHiddenMarkovModel(processes,
				categoryWF, customerInventory, timeSampler,
				this.seedFactory);
		
		return sampler;
	}
}
