package com.github.rnowling.bps.datagenerator;

import java.util.Collection;

import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.generators.purchase.PurchasingModel;
import com.github.rnowling.bps.datagenerator.generators.purchase.PurchasingModelSamplerBuilder;

public class PurchasingModelGenerator
{
	final Sampler<? extends PurchasingModel> sampler;
	
	public PurchasingModelGenerator(Collection<ProductCategory> productCategories, SeedFactory seedFactory) throws Exception
	{
		PurchasingModelSamplerBuilder builder = new PurchasingModelSamplerBuilder(productCategories, seedFactory);
		sampler = builder.build();
	}
	
	public PurchasingModel generate() throws Exception
	{
		return sampler.sample();
	}
}
