package com.github.rnowling.bps.datagenerator.generators.transaction;

import java.util.List;
import java.util.Map;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.Product;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.samplers.ConditionalSampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.RouletteWheelSampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.framework.wfs.ConditionalWeightFunction;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class TransactionPurchasesHiddenMarkovModel implements Sampler<Purchase>
{
	
	protected final static String STOP_STATE = "STOP";
	
	final ConditionalSampler<Product, String> purchasingProcesses;
	final ConditionalWeightFunction<Double, Double> categoryWF;
	final CustomerInventory inventory;
	final Sampler<Double> transactionTimeSampler;
	
	final SeedFactory seedFactory;
	
	public TransactionPurchasesHiddenMarkovModel(ConditionalSampler<Product, String> purchasingProcesses,
			ConditionalWeightFunction<Double, Double> categoryWF, CustomerInventory inventory,
				Sampler<Double> transactionTimeSampler, SeedFactory seedFactory)
	{
		this.purchasingProcesses = purchasingProcesses;
		this.inventory = inventory;
		this.categoryWF = categoryWF;
		this.transactionTimeSampler = transactionTimeSampler;
		
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

	public Purchase sample() throws Exception
	{
		double transactionTime = this.transactionTimeSampler.sample();
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
		
		return new Purchase(transactionTime, purchasedProducts);
	}
	
}
