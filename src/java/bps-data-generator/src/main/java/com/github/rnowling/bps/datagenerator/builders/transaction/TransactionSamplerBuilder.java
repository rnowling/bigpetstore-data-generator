package com.github.rnowling.bps.datagenerator.builders.transaction;

import java.util.Collection;

import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Customer;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Store;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Transaction;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.PurchasingProfile;
import com.github.rnowling.bps.datagenerator.samplers.transaction.Purchase;
import com.github.rnowling.bps.datagenerator.samplers.transaction.TransactionSampler;
import com.github.rnowling.bps.datagenerator.statistics.SeedFactory;
import com.github.rnowling.bps.datagenerator.statistics.samplers.RouletteWheelSampler;
import com.github.rnowling.bps.datagenerator.statistics.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.statistics.samplers.SequenceSampler;

public class TransactionSamplerBuilder
{
	private final Collection<Store> stores;
	private final Collection<ProductCategory> productCategories;
	private final Customer customer;
	private final PurchasingProfile purchasingProfile;
	private final SeedFactory seedFactory;
	
	public TransactionSamplerBuilder(Collection<Store> stores,
			Collection<ProductCategory> productCategories, 
			Customer customer,
			PurchasingProfile purchasingProfile,
			SeedFactory seedFactory) throws Exception
	{
		this.customer = customer;
		this.seedFactory = seedFactory;
		this.purchasingProfile = purchasingProfile;
		this.productCategories = productCategories;
		this.stores = stores; 	
	}
	
	protected Sampler<Store> buildStoreSampler()
	{
		return RouletteWheelSampler.createUniform(stores, seedFactory);
	}
	
	protected Sampler<Purchase> buildPurchasesSampler() throws Exception
	{
		TransactionPurchasesSamplerBuilder builder = new TransactionPurchasesSamplerBuilder(productCategories,
				purchasingProfile, seedFactory);
	
		return builder.build();
	}
	
	public Sampler<Transaction> build() throws Exception
	{
		return new TransactionSampler(customer, buildPurchasesSampler(), buildStoreSampler(), new SequenceSampler());
	}
}
