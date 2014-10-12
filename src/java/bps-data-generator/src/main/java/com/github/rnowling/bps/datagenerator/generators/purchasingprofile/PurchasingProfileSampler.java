package com.github.rnowling.bps.datagenerator.generators.purchasingprofile;

import java.util.Map;

import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.Product;
import com.github.rnowling.bps.datagenerator.framework.markovmodels.MarkovModel;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;

public class PurchasingProfileSampler implements Sampler<PurchasingProfile>
{
	final Map<ProductCategory, Sampler<MarkovModel<Product>>> categorySamplers;
	
	public PurchasingProfileSampler(Map<ProductCategory, Sampler<MarkovModel<Product>>> categorySamplers)
	{
		this.categorySamplers = categorySamplers;
	}
	
	public PurchasingProfile sample() throws Exception
	{
		PurchasingProfileBuilder builder = new PurchasingProfileBuilder();
		for(ProductCategory productCategory : categorySamplers.keySet())
		{
			Sampler<MarkovModel<Product>> sampler = categorySamplers.get(productCategory);
			builder.addProfile(productCategory.getCategoryLabel(), sampler.sample());
		}
		
		return builder.build();
	}
}
