package com.github.rnowling.bps.datagenerator.generators.transaction;

import java.util.Collection;
import java.util.List;

import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.Product;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.samplers.ConditionalSampler;
import com.github.rnowling.bps.datagenerator.framework.wfs.ConditionalWeightFunction;
import com.github.rnowling.bps.datagenerator.generators.purchasingprofile.PurchasingProfile;

public class TransactionPurchasesSamplerBuilder
{
	final SeedFactory seedFactory;
	final Collection<ProductCategory> productCategories;
	final PurchasingProfile purchasingProfile;
	
	protected CustomerTransactionParameters transactionParameters;
	protected CustomerInventory inventory;
	
	public TransactionPurchasesSamplerBuilder(Collection<ProductCategory> productCategories,
			PurchasingProfile purchasingProfile,
			SeedFactory seedFactory)
	{
		this.seedFactory = seedFactory;
		this.productCategories = productCategories;
		this.purchasingProfile = purchasingProfile;
	}
	
	public void setTransactionParameters(
			CustomerTransactionParameters transactionParameters)
	{
		this.transactionParameters = transactionParameters;
	}

	public void setInventory(CustomerInventory inventory)
	{
		this.inventory = inventory;
	}

	public ConditionalSampler<List<Product>, Double> build() throws Exception
	{
		PurchasingProcessesBuilder processesBuilder = new PurchasingProcessesBuilder(seedFactory);
		processesBuilder.setPurchasingProfile(purchasingProfile);
		PurchasingProcesses processes = processesBuilder.build();
		
		ConditionalWeightFunction<Double, Double> categoryWF =
				new CategoryWeightFunction(transactionParameters.getAveragePurchaseTriggerTime());
		
		ConditionalSampler<List<Product>, Double> sampler = new TransactionPurchasesHiddenMarkovModel(processes,
				categoryWF, inventory, this.seedFactory);
		
		return sampler;
	}
}
