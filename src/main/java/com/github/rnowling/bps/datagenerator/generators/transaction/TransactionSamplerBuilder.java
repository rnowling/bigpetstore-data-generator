package com.github.rnowling.bps.datagenerator.generators.transaction;

import java.util.Collection;
import java.util.List;

import com.github.rnowling.bps.datagenerator.datamodels.Customer;
import com.github.rnowling.bps.datagenerator.datamodels.Product;
import com.github.rnowling.bps.datagenerator.datamodels.Store;
import com.github.rnowling.bps.datagenerator.datamodels.Transaction;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.samplers.ConditionalSampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.RouletteWheelSampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.SequenceSampler;
import com.github.rnowling.bps.datagenerator.generators.purchase.PurchasingModel;

public class TransactionSamplerBuilder
{
	private final Collection<Store> stores;
	private final Collection<ProductCategory> productCategories;
	private final Customer customer;
	private final PurchasingModel purchasingProfile;
	private final SeedFactory seedFactory;
	
	CustomerTransactionParameters parameters;
	CustomerInventory inventory;
	
	public TransactionSamplerBuilder(Collection<Store> stores,
			Collection<ProductCategory> productCategories, 
			Customer customer,
			PurchasingModel purchasingProfile,
			SeedFactory seedFactory) throws Exception
	{
		this.customer = customer;
		this.seedFactory = seedFactory;
		this.purchasingProfile = purchasingProfile;
		this.productCategories = productCategories;
		this.stores = stores; 	
	}
	
	protected void buildParameters() throws Exception
	{
		CustomerTransactionParametersSamplerBuilder builder = new CustomerTransactionParametersSamplerBuilder(seedFactory);
		parameters = builder.build().sample();
	}
	
	protected Sampler<Store> buildStoreSampler()
	{
		return RouletteWheelSampler.createUniform(stores, seedFactory);
	}
	
	protected ConditionalSampler<List<Product>, Double> buildPurchasesSampler() throws Exception
	{
		TransactionPurchasesSamplerBuilder builder = new TransactionPurchasesSamplerBuilder(productCategories,
				purchasingProfile, seedFactory);
		
		builder.setTransactionParameters(parameters);
		builder.setInventory(inventory);
	
		return builder.build();
	}
	
	protected Sampler<Double> buildTimeSampler()
	{
		TransactionTimeSamplerBuilder builder = new TransactionTimeSamplerBuilder(seedFactory);
		builder.setCustomerTransactionParameters(parameters);
		builder.setCustomerInventory(inventory);
		
		return builder.build();
	}
	
	protected void buildCustomerInventory()
	{
		CustomerInventoryBuilder inventoryBuilder = new CustomerInventoryBuilder(parameters,
				seedFactory);
		inventoryBuilder.addAllProductCategories(productCategories);
		inventory = inventoryBuilder.build();
	}
	
	public Sampler<Transaction> build() throws Exception
	{
		buildParameters();
		buildCustomerInventory();
		
		Sampler<Double> timeSampler = buildTimeSampler();
		
		return new TransactionSampler(customer, timeSampler, buildPurchasesSampler(), buildStoreSampler(), new SequenceSampler());
	}
}
