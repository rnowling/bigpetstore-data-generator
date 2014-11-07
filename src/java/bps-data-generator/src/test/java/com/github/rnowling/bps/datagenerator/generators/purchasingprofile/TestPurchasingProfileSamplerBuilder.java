package com.github.rnowling.bps.datagenerator.generators.purchasingprofile;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.datamodels.PetSpecies;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategoryBuilder;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.Product;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

public class TestPurchasingProfileSamplerBuilder
{
	
	private List<ProductCategory> createProducts()
	{
		List<ProductCategory> productCategories = Lists.newArrayList();
		
		ProductCategoryBuilder foodBuilder = new ProductCategoryBuilder();
		foodBuilder.addApplicableSpecies(PetSpecies.DOG);
		foodBuilder.setAmountUsedPetPetAverage(1.0);
		foodBuilder.setAmountUsedPetPetVariance(1.0);
		foodBuilder.setDailyUsageRate(2.0);
		foodBuilder.addFieldName(Constants.PRODUCT_CATEGORY);
		foodBuilder.addFieldName(Constants.PRODUCT_QUANTITY);
		foodBuilder.addFieldName("Flavor");
		foodBuilder.setCategory("dogFood");
		foodBuilder.addProduct(new Product(ImmutableMap.of(Constants.PRODUCT_CATEGORY, (Object) "dogFood",
				Constants.PRODUCT_QUANTITY, (Object) 60.0, "Flavor", "Fish & Potato")));
		foodBuilder.addProduct(new Product(ImmutableMap.of(Constants.PRODUCT_CATEGORY, (Object) "dogFood",
				Constants.PRODUCT_QUANTITY, (Object) 30.0, "Flavor", "Chicken & Rice")));
		foodBuilder.addProduct(new Product(ImmutableMap.of(Constants.PRODUCT_CATEGORY, (Object) "dogFood",
				Constants.PRODUCT_QUANTITY, (Object) 15.0, "Flavor", "Lamb & Barley")));
		productCategories.add(foodBuilder.build());
		
		ProductCategoryBuilder bagBuilder = new ProductCategoryBuilder();
		bagBuilder.addApplicableSpecies(PetSpecies.DOG);
		bagBuilder.setAmountUsedPetPetAverage(1.0);
		bagBuilder.setAmountUsedPetPetVariance(1.0);
		bagBuilder.setDailyUsageRate(2.0);
		bagBuilder.addFieldName(Constants.PRODUCT_CATEGORY);
		bagBuilder.addFieldName(Constants.PRODUCT_QUANTITY);
		bagBuilder.addFieldName("Color");
		bagBuilder.setCategory("Poop Bags");
		bagBuilder.addProduct(new Product(ImmutableMap.of(Constants.PRODUCT_CATEGORY, (Object) "Poop Bags",
				Constants.PRODUCT_QUANTITY, (Object) 60.0, "Color", "Blue")));
		bagBuilder.addProduct(new Product(ImmutableMap.of(Constants.PRODUCT_CATEGORY, (Object) "Poop Bags",
				Constants.PRODUCT_QUANTITY, (Object) 30.0, "Color", "Red")));
		bagBuilder.addProduct(new Product(ImmutableMap.of(Constants.PRODUCT_CATEGORY, (Object) "Poop Bags",
				Constants.PRODUCT_QUANTITY, (Object) 120.0, "Color", "Multicolor")));
		productCategories.add(bagBuilder.build());
		
		return productCategories;
	}

	@Test
	public void testBuild() throws Exception
	{
		SeedFactory seedFactory = new SeedFactory(1245);
		
		List<ProductCategory> productCategories = createProducts();
		
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
