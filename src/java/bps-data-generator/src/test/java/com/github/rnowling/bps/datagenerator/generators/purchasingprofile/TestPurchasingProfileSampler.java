package com.github.rnowling.bps.datagenerator.generators.purchasingprofile;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.datareaders.ProductsReader;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.generators.purchasingprofile.PurchasingProfileSamplerBuilder;
import com.github.rnowling.bps.datagenerator.resources.Constants;

public class TestPurchasingProfileSampler
{

	@Test
	public void testSample() throws Exception
	{
		SeedFactory seedFactory = new SeedFactory(1245);
		
		ProductsReader reader = new ProductsReader(Constants.PRODUCTS_FILE);
		
		List<ProductCategory> productCategories = reader.readData();
		
		PurchasingProfileSamplerBuilder builder = new PurchasingProfileSamplerBuilder(productCategories, seedFactory);
		Sampler<PurchasingProfile> sampler = builder.build();
		PurchasingProfile profile = sampler.sample();
		
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
