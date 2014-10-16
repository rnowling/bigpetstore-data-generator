package com.github.rnowling.bps.datagenerator.datareaders;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.Product;
import com.github.rnowling.bps.datagenerator.resources.Constants;

public class TestProductsReader
{

	@Test
	public void testRead() throws Exception
	{
		ProductsReader reader = new ProductsReader(Constants.PRODUCTS_FILE);
		
		List<ProductCategory> productCategories = reader.readData();
		
		assertTrue(productCategories.size() > 0);
		
		ProductCategory category = productCategories.get(0);
		
		assertNotNull(category.getCategoryLabel());
		assertNotNull(category.getApplicableSpecies());
		assertTrue(category.getApplicableSpecies().size() > 0);
		assertNotNull(category.getFieldNames());
		assertTrue(category.getFieldNames().size() > 0);
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
		assertNotNull(category.getProducts());
		assertTrue(category.getProducts().size() > 0);
		
		Product product = category.getProducts().get(0);
		
		assertTrue(product.getFieldNames().size() > 0);
		assertNotNull(product.getFieldValue(Constants.PRODUCT_CATEGORY));
		assertNotNull(product.getFieldValue(Constants.PRODUCT_QUANTITY));
		
	}

}
