package com.github.rnowling.bps.datagenerator.generators.transaction;

import java.util.Map;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.SeedFactory;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.RouletteWheelSampler;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.Product;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class TransactionPurchasesHiddenMarkovModel implements Sampler<Product>
{
	
	final PurchasingProcesses purchasingProcesses;
	final CustomerTransactionParameters transactionParameters;
	final CustomerInventory inventory;
	final double transactionTime;
	
	int numPurchases;
	
	final SeedFactory seedFactory;
	
	
	protected final static String STOP_STATE = "STOP";
	
	public TransactionPurchasesHiddenMarkovModel(PurchasingProcesses purchasingProcesses,
			CustomerTransactionParameters transactionParameters, CustomerInventory inventory,
				double transactionTime, SeedFactory seedFactory)
	{
		this.purchasingProcesses = purchasingProcesses;
		this.inventory = inventory;
		this.transactionParameters = transactionParameters;
		this.transactionTime = transactionTime;
		
		this.seedFactory = seedFactory;
		
		numPurchases = 0;
	}
	
	protected double categoryWeight(double exhaustionTime)
	{
		double remainingTime = Math.max(0.0, exhaustionTime - this.transactionTime);
		double triggerTime = this.transactionParameters.averagePurchaseTriggerTime;
		double lambda = 1.0 / triggerTime;
		double weight = lambda * Math.exp(-1.0 * lambda * remainingTime);
		
		return weight;
	}
	
	protected String chooseCategory()
	{
		ImmutableMap<String, Double> exhaustionTimes = this.inventory.getExhaustionTimes();
		Map<String, Double> weights = Maps.newHashMap();
		
		for(Map.Entry<String, Double> entry : exhaustionTimes.entrySet())
		{
			String category = entry.getKey();
			double weight = this.categoryWeight(entry.getValue());
			weights.put(category, weight);
		}
		
		if(numPurchases > 0)
		{
			weights.put(STOP_STATE, Constants.STOP_CATEGORY_WEIGHT);
		}
		
		Sampler<String> sampler = RouletteWheelSampler.create(weights, seedFactory);
		
		return sampler.sample();
	}
	
	protected Product chooseProduct(String category)
	{
		return this.purchasingProcesses.simulatePurchase(category);
	}

	public Product sample()
	{
		String category = this.chooseCategory();
		
		if(category.equals(STOP_STATE))
		{
			return null;
		}
		
		Product product = this.chooseProduct(category);
		
		this.inventory.simulatePurchase(transactionTime, product);
		this.numPurchases += 1;
		
		return product;
	}
	
}
