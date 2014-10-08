package com.github.rnowling.bps.datagenerator.generators.purchasingprofile;

import java.util.List;

import com.github.rnowling.bps.datagenerator.SeedFactory;
import com.github.rnowling.bps.datagenerator.algorithms.markovmodels.MarkovModel;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.Product;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.PurchasingProfile;
import com.google.common.collect.ImmutableList;

public class PurchasingProfileSampler implements Sampler<PurchasingProfile>
{
	ImmutableList<ProductCategory> productCategories;
	SeedFactory seedFactory;
	
	public PurchasingProfileSampler(List<ProductCategory> productCategories, SeedFactory seedFactory)
	{
		this.productCategories = ImmutableList.copyOf(productCategories);
		this.seedFactory = seedFactory;
	}
	
	public PurchasingProfile sample() throws Exception
	{
		PurchasingProfileBuilder builder = new PurchasingProfileBuilder();
		for(ProductCategory productCategory : productCategories)
		{
			ProductCategoryMarkovModelGenerator generator = new ProductCategoryMarkovModelGenerator(productCategory, seedFactory);
			MarkovModel<Product> markovModel = generator.generate();
			builder.addProfile(productCategory.getCategoryLabel(), markovModel);
		}
		
		return builder.build();
	}
}
