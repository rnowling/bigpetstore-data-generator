package com.github.rnowling.bps.datagenerator.generators.purchase;

import java.util.Map;

import com.github.rnowling.bps.datagenerator.datamodels.Product;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.framework.pdfs.DiscretePDF;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class StaticPurchasingModelSampler implements Sampler<StaticPurchasingModel>
{
	final ImmutableMap<ProductCategory, Sampler<DiscretePDF<Product>>> categorySamplers;
	
	public StaticPurchasingModelSampler(Map<ProductCategory, Sampler<DiscretePDF<Product>>> categorySamplers)
	{
		this.categorySamplers = ImmutableMap.copyOf(categorySamplers);
	}
	
	public StaticPurchasingModel sample() throws Exception
	{
		Map<String, DiscretePDF<Product>> pdfs = Maps.newHashMap();
		for(ProductCategory productCategory : categorySamplers.keySet())
		{
			Sampler<DiscretePDF<Product>> sampler = categorySamplers.get(productCategory);
			pdfs.put(productCategory.getCategoryLabel(), sampler.sample());
		}
		
		return new StaticPurchasingModel(pdfs);
	}
}

