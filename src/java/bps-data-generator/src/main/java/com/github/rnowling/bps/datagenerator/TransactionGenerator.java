package com.github.rnowling.bps.datagenerator;

import java.util.Collection;
import java.util.List;

import com.github.rnowling.bps.datagenerator.builders.transaction.TransactionSamplerBuilder;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Customer;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Store;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Transaction;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.PurchasingProfile;
import com.github.rnowling.bps.datagenerator.statistics.SeedFactory;
import com.github.rnowling.bps.datagenerator.statistics.samplers.Sampler;

public class TransactionGenerator
{
	Sampler<Transaction> sampler;
	
	public TransactionGenerator(Customer customer, PurchasingProfile profile, List<Store> stores,
			Collection<ProductCategory> productCategories, SeedFactory seedFactory) throws Exception
	{
		sampler = new TransactionSamplerBuilder(stores, productCategories,
				customer, profile, seedFactory).build();
	}
	
	public Transaction generate() throws Exception
	{
		return sampler.sample();
	}
}
