package com.github.rnowling.bps.datagenerator.generators.purchasingprofile;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.SeedFactory;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.PurchasingProfile;
import com.github.rnowling.bps.datagenerator.datareaders.ProductsReader;

public class TestPurchasingProfileGenerator
{

	@Test
	public void testGenerate() throws Exception
	{
		SeedFactory seedFactory = new SeedFactory(1245);
		
		ProductsReader reader = new ProductsReader(Constants.PRODUCTS_FILE);
		
		List<ProductCategory> productCategories = reader.readData();
		
		PurchasingProfileGenerator generator = new PurchasingProfileGenerator(productCategories, seedFactory);
		
		PurchasingProfile profile = generator.generate();
		
		assertNotNull(profile);
		assertNotNull(profile.getProductCategories());
		assertTrue(profile.getProductCategories().size() > 0);
		
		for(String label : profile.getProductCategories())
		{
			assertNotNull(profile.getProfile(label));
			assertNotNull(profile.getProfile(label).getStartWeights());
			assertTrue(profile.getProfile(label).getStartWeights().size() > 0);
			assertNotNull(profile.getProfile(label).getTransitionWeights());
			assertTrue(profile.getProfile(label).getTransitionWeights().size() > 0);
		}
	}

}
