package com.github.rnowling.bps.datagenerator.generators.transaction;

import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.datamodels.Product;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.samplers.RouletteWheelSampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.generators.transaction.PurchasingProcesses;
import com.google.common.collect.Maps;

public class TestPurchasingProcesses
{
	
	@Test
	public void testSimulatePurchase() throws Exception
	{
		Map<Product, Double> productPDF = Maps.newHashMap();
		
		for(int i = 0; i < 10; i++)
		{
			Map<String, Object> fields = Maps.newHashMap();
			fields.put(Constants.PRODUCT_CATEGORY, "dog food");
			fields.put(Constants.PRODUCT_QUANTITY, (double) (i + 1));
			Product product = new Product(fields);
			productPDF.put(product, 0.1);
		}
		
		SeedFactory seedFactory = new SeedFactory(1234);
		Sampler<Product> sampler = RouletteWheelSampler.create(productPDF, seedFactory);
		
		
		Map<String, Sampler<Product>> processesMap = Maps.newHashMap();
		processesMap.put("dog food", sampler);
		PurchasingProcesses processes = new PurchasingProcesses(processesMap);
		
		Product product = processes.sample("dog food");
		
		assertNotNull(product);
		assertNotNull(product.getFieldValue(Constants.PRODUCT_CATEGORY));
		assertNotNull(product.getFieldValue(Constants.PRODUCT_QUANTITY));
		
		product = processes.sample("dog food");
		
		assertNotNull(product);
		assertNotNull(product.getFieldValue(Constants.PRODUCT_CATEGORY));
		assertNotNull(product.getFieldValue(Constants.PRODUCT_QUANTITY));
	}

}
