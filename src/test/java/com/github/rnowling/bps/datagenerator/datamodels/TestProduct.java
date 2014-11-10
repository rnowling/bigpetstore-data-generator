package com.github.rnowling.bps.datagenerator.datamodels;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Map;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.datamodels.Product;
import com.google.common.collect.Maps;

public class TestProduct
{

	@Test
	public void testString()
	{
		Map<String, Object> fields = Maps.newHashMap();
		fields.put(Constants.PRODUCT_CATEGORY, "poop bags");
		fields.put(Constants.PRODUCT_QUANTITY, 120);
		fields.put("price", 12.80);
		
		Product product = new Product(fields);
		
		assertEquals(product.getFieldValueAsString(Constants.PRODUCT_CATEGORY), "poop bags");
		assertEquals(product.getFieldValueAsString("price"), "12.8");
		assertEquals(product.getFieldValueAsString(Constants.PRODUCT_QUANTITY), "120");
	}
	
	@Test
	public void testDouble()
	{
		Map<String, Object> fields = Maps.newHashMap();
		fields.put(Constants.PRODUCT_CATEGORY, "poop bags");
		fields.put(Constants.PRODUCT_QUANTITY, 120);
		fields.put("price", 12.80);
		
		Product product = new Product(fields);
		
		assertNull(product.getFieldValueAsDouble(Constants.PRODUCT_CATEGORY));
		assertEquals(product.getFieldValueAsDouble("price"), 12.80, 1e-5);
		assertNull(product.getFieldValueAsDouble(Constants.PRODUCT_QUANTITY));
	}
	
	@Test
	public void testLong()
	{
		Map<String, Object> fields = Maps.newHashMap();
		fields.put(Constants.PRODUCT_CATEGORY, "poop bags");
		fields.put(Constants.PRODUCT_QUANTITY, 120);
		fields.put("price", 12.80);
		
		Product product = new Product(fields);
		
		assertNull(product.getFieldValueAsLong(Constants.PRODUCT_CATEGORY));
		assertNull(product.getFieldValueAsLong("price"));
		assertEquals((long) product.getFieldValueAsLong(Constants.PRODUCT_QUANTITY), 120L);
	}

}
