package com.github.rnowling.bps.datagenerator.datareaders;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.datamodels.ProductCategory;

public class TestProductsReader
{

	@Test
	public void testRead() throws Exception
	{
		ProductsReader reader = new ProductsReader(Constants.PRODUCTS_FILE);
		
		List<ProductCategory> productCategories = reader.readData();
		
		assertTrue(productCategories.size() > 0);
		
		ProductCategory category = productCategories.get(0);
		
		assertNotNull(category.getCategory());
		assertNotNull(category.getSpecies());
		assertTrue(category.getSpecies().size() > 0);
		assertNotNull(category.getFields());
		assertTrue(category.getFields().size() > 0);
		assertNotNull(category.getTriggerTransaction());
		assertTrue(category.getTriggerTransaction());
		assertNotNull(category.getDailyUsageRate());
		assertTrue(category.getDailyUsageRate() > 0);
		assertNotNull(category.getBaseAmountUsedAverage());
		assertTrue(category.getBaseAmountUsedAverage() > 0);
		assertNotNull(category.getBaseAmountUsedVariance());
		assertTrue(category.getBaseAmountUsedVariance() > 0);
		assertNotNull(category.getPurchaseTriggerRate());
		assertTrue(category.getPurchaseTriggerRate() > 0);
		assertNotNull(category.getTransactionTriggerRate());
		assertTrue(category.getTransactionTriggerRate() > 0);
		assertNotNull(category.getItems());
		assertTrue(category.getItems().size() > 0);
		
		Map<String, Object> product = category.getItems().get(0);
		
		assertTrue(product.containsKey("category"));
		assertNotNull(product.get("category"));
		
	}

}
