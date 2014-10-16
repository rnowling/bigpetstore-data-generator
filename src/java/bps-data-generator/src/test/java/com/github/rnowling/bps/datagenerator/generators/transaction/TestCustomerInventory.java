package com.github.rnowling.bps.datagenerator.generators.transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.datamodels.PetSpecies;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategoryBuilder;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.Product;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.generators.transaction.CustomerInventory;
import com.github.rnowling.bps.datagenerator.generators.transaction.CustomerTransactionParameters;
import com.github.rnowling.bps.datagenerator.generators.transaction.CustomerTransactionParametersSamplerBuilder;
import com.github.rnowling.bps.datagenerator.generators.transaction.ProductCategoryInventory;
import com.github.rnowling.bps.datagenerator.resources.Constants;
import com.google.common.collect.Maps;

public class TestCustomerInventory
{
	
	@Test
	public void testPurchase() throws Exception
	{
		SeedFactory seedFactory = new SeedFactory(1234);
		
		CustomerTransactionParametersSamplerBuilder transParamsBuilder = new CustomerTransactionParametersSamplerBuilder(seedFactory);
		Sampler<CustomerTransactionParameters> sampler = transParamsBuilder.build();
		
		CustomerTransactionParameters parameters = sampler.sample();
		
		ProductCategoryBuilder builder = new ProductCategoryBuilder();
		builder.addApplicableSpecies(PetSpecies.DOG);
		builder.setAmountUsedPetPetAverage(1.0);
		builder.setAmountUsedPetPetVariance(1.0);
		builder.setDailyUsageRate(2.0);
		builder.setCategory("dog food");
		
		
		ProductCategory category = builder.build();
		
		ProductCategoryInventory productInventory = new ProductCategoryInventory(category, parameters, seedFactory);
		
		Map<String, ProductCategoryInventory> inventories = Maps.newHashMap();
		inventories.put("dog food", productInventory);
		
		CustomerInventory inventory = new CustomerInventory(inventories);
		
		for(Map.Entry<String, Double> entry : inventory.getExhaustionTimes().entrySet())
		{
			assertEquals(entry.getValue(), 0.0, 0.0001);
		}
		
		for(Map.Entry<String, Double> entry : inventory.getInventoryAmounts(0.0).entrySet())
		{
			assertEquals(entry.getValue(), 0.0, 0.0001);
		}
		
		Map<String, Object> fields = Maps.newHashMap();
		fields.put(Constants.PRODUCT_CATEGORY, "dog food");
		fields.put(Constants.PRODUCT_QUANTITY, 30.0);
		Product product = new Product(fields);
		
		inventory.simulatePurchase(1.0, product);
		
		Map<String, Double> exhaustionTimes = inventory.getExhaustionTimes();
		assertTrue(exhaustionTimes.containsKey("dog food"));
		assertTrue(exhaustionTimes.get("dog food") > 0.0);
		
		Map<String, Double> amounts = inventory.getInventoryAmounts(2.0);
		assertTrue(amounts.containsKey("dog food"));
		assertTrue(amounts.get("dog food") > 0.0);
	}

}
