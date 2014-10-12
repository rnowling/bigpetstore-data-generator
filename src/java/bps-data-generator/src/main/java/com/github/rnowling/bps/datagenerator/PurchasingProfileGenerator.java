package com.github.rnowling.bps.datagenerator;

import java.util.Collection;

import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.generators.purchasingprofile.PurchasingProfile;
import com.github.rnowling.bps.datagenerator.generators.purchasingprofile.PurchasingProfileSamplerBuilder;

public class PurchasingProfileGenerator
{
	final Sampler<PurchasingProfile> sampler;
	
	public PurchasingProfileGenerator(Collection<ProductCategory> productCategories, SeedFactory seedFactory) throws Exception
	{
		PurchasingProfileSamplerBuilder builder = new PurchasingProfileSamplerBuilder(productCategories, seedFactory);
		sampler = builder.build();
	}
	
	public PurchasingProfile generate() throws Exception
	{
		return sampler.sample();
	}
}
