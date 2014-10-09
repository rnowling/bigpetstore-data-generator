package com.github.rnowling.bps.datagenerator.generators.transaction;

import java.util.List;
import java.util.Map;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.SeedFactory;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.RouletteWheelSampler;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.Product;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class TransactionPurchasesHiddenMarkovModel implements Sampler<Purchase>
{
	
	protected final static String STOP_STATE = "STOP";
	
	final PurchasingProcesses purchasingProcesses;
	final double averagePurchaseTriggerTime;
	final CustomerInventory inventory;
	final Sampler<Double> transactionTimeSampler;
	
	final SeedFactory seedFactory;
	
	public TransactionPurchasesHiddenMarkovModel(PurchasingProcesses purchasingProcesses,
			double averagePurchaseTriggerTime, CustomerInventory inventory,
				Sampler<Double> transactionTimeSampler, SeedFactory seedFactory)
	{
		this.purchasingProcesses = purchasingProcesses;
		this.inventory = inventory;
		this.averagePurchaseTriggerTime = averagePurchaseTriggerTime;
		this.transactionTimeSampler = transactionTimeSampler;
		
		this.seedFactory = seedFactory;
	}
	
	protected double categoryWeight(double exhaustionTime, double transactionTime)
	{
		double remainingTime = Math.max(0.0, exhaustionTime - transactionTime);
		double triggerTime = this.averagePurchaseTriggerTime;
		double lambda = 1.0 / triggerTime;
		double weight = lambda * Math.exp(-1.0 * lambda * remainingTime);
		
		return weight;
	}
	
	protected String chooseCategory(double transactionTime, int numPurchases) throws Exception
	{
		ImmutableMap<String, Double> exhaustionTimes = this.inventory.getExhaustionTimes();
		Map<String, Double> weights = Maps.newHashMap();
		
		for(Map.Entry<String, Double> entry : exhaustionTimes.entrySet())
		{
			String category = entry.getKey();
			double weight = this.categoryWeight(entry.getValue(), transactionTime);
			weights.put(category, weight);
		}
		
		if(numPurchases > 0)
		{
			weights.put(STOP_STATE, Constants.STOP_CATEGORY_WEIGHT);
		}
		
		Sampler<String> sampler = RouletteWheelSampler.create(weights, seedFactory);
		
		return sampler.sample();
	}
	
	protected Product chooseProduct(String category) throws Exception
	{
		return this.purchasingProcesses.simulatePurchase(category);
	}

	public Purchase sample() throws Exception
	{
		double transactionTime = this.transactionTimeSampler.sample();
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
		
		return new Purchase(transactionTime, purchasedProducts);
	}
	
}
