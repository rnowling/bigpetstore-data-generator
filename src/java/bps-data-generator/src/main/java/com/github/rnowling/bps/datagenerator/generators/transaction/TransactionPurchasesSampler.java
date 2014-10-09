package com.github.rnowling.bps.datagenerator.generators.transaction;

import java.util.List;

import com.github.rnowling.bps.datagenerator.algorithms.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.Product;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class TransactionPurchasesSampler implements Sampler<List<Product>>
{
	final private Sampler<Product> purchasesSampler;
	
	
	public TransactionPurchasesSampler(Sampler<Product> purchasesSampler)
	{
		this.purchasesSampler = purchasesSampler;
	}
	
	public ImmutableList<Product> sample() throws Exception
	{
		List<Product> purchasedProducts = Lists.newArrayList();
		
		while(true)
		{
			Product product = purchasesSampler.sample();
			if(product == null)
			{
				return ImmutableList.copyOf(purchasedProducts);
			}
			
			purchasedProducts.add(product);
		}
	}
	
}
