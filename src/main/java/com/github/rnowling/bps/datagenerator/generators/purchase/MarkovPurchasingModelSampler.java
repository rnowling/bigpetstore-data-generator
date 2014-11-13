package com.github.rnowling.bps.datagenerator.generators.purchase;

import java.util.Map;

import com.github.rnowling.bps.datagenerator.datamodels.Product;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.framework.markovmodels.MarkovModel;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;
import com.google.common.collect.Maps;

public class MarkovPurchasingModelSampler implements Sampler<MarkovPurchasingModel>
{
	final Map<ProductCategory, Sampler<MarkovModel<Product>>> categorySamplers;
	
	public MarkovPurchasingModelSampler(Map<ProductCategory, Sampler<MarkovModel<Product>>> categorySamplers)
	{
		this.categorySamplers = categorySamplers;
	}
	
	public MarkovPurchasingModel sample() throws Exception
	{
		Map<String, MarkovModel<Product>> markovModels = Maps.newHashMap();
		for(ProductCategory productCategory : categorySamplers.keySet())
		{
			Sampler<MarkovModel<Product>> sampler = categorySamplers.get(productCategory);
			markovModels.put(productCategory.getCategoryLabel(), sampler.sample());
		}
		
		return new MarkovPurchasingModel(markovModels);
	}
}
