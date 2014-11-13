package com.github.rnowling.bps.datagenerator.generators.transaction;

import java.util.Collection;
import java.util.List;

import com.github.rnowling.bps.datagenerator.datamodels.Product;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.samplers.ConditionalSampler;
import com.github.rnowling.bps.datagenerator.framework.wfs.ConditionalWeightFunction;
import com.github.rnowling.bps.datagenerator.generators.purchase.PurchasingProcesses;
import com.github.rnowling.bps.datagenerator.generators.purchase.PurchasingModel;

public class TransactionPurchasesSamplerBuilder
{
	final SeedFactory seedFactory;
	final Collection<ProductCategory> productCategories;
	final PurchasingModel purchasingProfile;
	
	protected CustomerTransactionParameters transactionParameters;
	protected CustomerInventory inventory;
	
	public TransactionPurchasesSamplerBuilder(Collection<ProductCategory> productCategories,
			PurchasingModel purchasingProfile,
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
		PurchasingProcesses processes = purchasingProfile.buildProcesses(seedFactory);
		
		ConditionalWeightFunction<Double, Double> categoryWF =
				new CategoryWeightFunction(transactionParameters.getAveragePurchaseTriggerTime());
		
		ConditionalSampler<List<Product>, Double> sampler = new TransactionPurchasesHiddenMarkovModel(processes,
				categoryWF, inventory, this.seedFactory);
		
		return sampler;
	}
}
