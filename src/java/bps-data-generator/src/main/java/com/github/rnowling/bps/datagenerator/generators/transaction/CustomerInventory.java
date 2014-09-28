package com.github.rnowling.bps.datagenerator.generators.transaction;

import java.util.Map;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.Product;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class CustomerInventory
{
	final private ImmutableMap<String, ProductCategoryInventory> productCategoryInventories;
	
	public CustomerInventory(Map<String, ProductCategoryInventory> productCategoryInventories)
	{
		this.productCategoryInventories = ImmutableMap.copyOf(productCategoryInventories);
	}
	
	public void simulatePurchase(double time, Product product)
	{
		String category = product.getFieldValueAsString(Constants.PRODUCT_CATEGORY);
		ProductCategoryInventory inventory = productCategoryInventories.get(category);
		inventory.simulatePurchase(time, product);
	}
	
	public ImmutableMap<String, Double> getInventoryAmounts(double time)
	{
		Map<String, Double> amounts = Maps.newHashMap();
		for(String category : productCategoryInventories.keySet())
		{
			double amount = productCategoryInventories.get(category).findRemainingAmount(time);
			amounts.put(category, amount);
		}
		
		return ImmutableMap.copyOf(amounts);
	}
	
	public ImmutableMap<String, Double> getExhaustionTimes()
	{
		Map<String, Double> times = Maps.newHashMap();
		for(String category : productCategoryInventories.keySet())
		{
			double time = productCategoryInventories.get(category).findExhaustionTime();
			times.put(category, time);
		}
		
		return ImmutableMap.copyOf(times);
	}
}
