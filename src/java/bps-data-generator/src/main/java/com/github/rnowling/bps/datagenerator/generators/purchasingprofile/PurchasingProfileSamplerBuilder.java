package com.github.rnowling.bps.datagenerator.generators.purchasingprofile;

import java.util.List;
import java.util.Map;

import com.github.rnowling.bps.datagenerator.SeedFactory;
import com.github.rnowling.bps.datagenerator.algorithms.markovmodels.MarkovModel;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.Product;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.PurchasingProfile;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

public class PurchasingProfileSamplerBuilder
{
	final ImmutableList<ProductCategory> productCategories;
	final SeedFactory seedFactory;
	
	public PurchasingProfileSamplerBuilder(List<ProductCategory> productCategories, SeedFactory seedFactory)
	{
		this.productCategories = ImmutableList.copyOf(productCategories);
		this.seedFactory = seedFactory;
	}
	
	public Sampler<PurchasingProfile> build() throws Exception
	{
		Map<ProductCategory, Sampler<MarkovModel<Product>>> categorySamplers = Maps.newHashMap();
		for(ProductCategory productCategory : productCategories)
		{
			ProductCategoryMarkovModelSampler sampler = new ProductCategoryMarkovModelSampler(productCategory, seedFactory);
			categorySamplers.put(productCategory, sampler);
		}
		
		return new PurchasingProfileSampler(categorySamplers);
	}
}