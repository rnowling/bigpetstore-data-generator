package com.github.rnowling.bps.datagenerator.generators.transaction;

import java.util.List;
import java.util.Map;

import com.github.rnowling.bps.datagenerator.datamodels.simulation.Product;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.samplers.ConditionalSampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.RouletteWheelSampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.framework.wfs.ConditionalWeightFunction;
import com.github.rnowling.bps.datagenerator.resources.Constants;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class TransactionPurchasesHiddenMarkovModel implements ConditionalSampler<List<Product>, Double>
{
	
	protected final static String STOP_STATE = "STOP";
	
	final ConditionalSampler<Product, String> purchasingProcesses;
	final ConditionalWeightFunction<Double, Double> categoryWF;
	final CustomerInventory inventory;
	
	final SeedFactory seedFactory;
	
	public TransactionPurchasesHiddenMarkovModel(ConditionalSampler<Product, String> purchasingProcesses,
			ConditionalWeightFunction<Double, Double> categoryWF, CustomerInventory inventory,
				SeedFactory seedFactory)
	{
		this.purchasingProcesses = purchasingProcesses;
		this.inventory = inventory;
		this.categoryWF = categoryWF;
		
		this.seedFactory = seedFactory;
	}
	
	protected String chooseCategory(double transactionTime, int numPurchases) throws Exception
	{
		ImmutableMap<String, Double> exhaustionTimes = this.inventory.getExhaustionTimes();
		Map<String, Double> weights = Maps.newHashMap();
		
		String fieldWeights = "";
		for(Map.Entry<String, Double> entry : exhaustionTimes.entrySet())
		{
			String category = entry.getKey();
			double weight = this.categoryWF.weight(entry.getValue(), transactionTime);
			weights.put(category, weight);
			fieldWeights += weight + " ";
		}
		System.out.println("Category Weights: " + fieldWeights);
		
		if(numPurchases > 0)
		{
			weights.put(STOP_STATE, Constants.STOP_CATEGORY_WEIGHT);
		}
		
		Sampler<String> sampler = RouletteWheelSampler.create(weights, seedFactory);
		
		return sampler.sample();
	}
	
	protected Product chooseProduct(String category) throws Exception
	{
		return this.purchasingProcesses.sample(category);
	}

	public List<Product> sample(Double transactionTime) throws Exception
	{
		System.out.println("Transaction Time: " + transactionTime);
		int numPurchases = 0;
		
		List<Product> purchasedProducts = Lists.newArrayList();
		
		String category;
		while(true)
		{
			category = this.chooseCategory(transactionTime, numPurchases);
			
			if(category.equals(STOP_STATE))
			{
				break;
			}
			
			Product product = this.chooseProduct(category);
			
			purchasedProducts.add(product);
			
			this.inventory.simulatePurchase(transactionTime, product);
			numPurchases += 1;
		}
		
		System.out.println("Number of products purchased: " + purchasedProducts.size());
		
		return purchasedProducts;
	}
	
	public Sampler<List<Product>> fixConditional(final Double transactionTime)
	{
		final ConditionalSampler<List<Product>, Double> sampler = this;
		return new Sampler<List<Product>>()
		{
			public List<Product> sample() throws Exception
			{
				return sampler.sample(transactionTime);
			}
		};
	}
	
}
