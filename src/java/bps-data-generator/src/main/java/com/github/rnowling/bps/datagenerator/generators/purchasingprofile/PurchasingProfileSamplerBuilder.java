package com.github.rnowling.bps.datagenerator.generators.purchasingprofile;

import java.util.Collection;
import java.util.Map;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.datamodels.Product;
import com.github.rnowling.bps.datagenerator.datamodels.PurchasingProfile;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.markovmodels.MarkovModel;
import com.github.rnowling.bps.datagenerator.framework.samplers.BoundedMultiModalGaussianSampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

public class PurchasingProfileSamplerBuilder
{
	final ImmutableList<ProductCategory> productCategories;
	final SeedFactory seedFactory;
	final Sampler<Double> fieldWeightSampler;
	final Sampler<Double> fieldSimilarityWeightSampler;
	final Sampler<Double> loopbackWeightSampler;
	
	public PurchasingProfileSamplerBuilder(Collection<ProductCategory> productCategories, SeedFactory seedFactory)
	{
		this.productCategories = ImmutableList.copyOf(productCategories);
		this.seedFactory = seedFactory;
		
		this.fieldWeightSampler = new BoundedMultiModalGaussianSampler(Constants.PRODUCT_MSM_FIELD_WEIGHT_GAUSSIANS, 
				Constants.PRODUCT_MSM_FIELD_WEIGHT_LOWERBOUND, 
				Constants.PRODUCT_MSM_FIELD_WEIGHT_UPPERBOUND,
				seedFactory);
	
	this.fieldSimilarityWeightSampler = new BoundedMultiModalGaussianSampler(Constants.PRODUCT_MSM_FIELD_SIMILARITY_WEIGHT_GAUSSIANS,
			Constants.PRODUCT_MSM_FIELD_SIMILARITY_WEIGHT_LOWERBOUND, 
			Constants.PRODUCT_MSM_FIELD_SIMILARITY_WEIGHT_UPPERBOUND,
			seedFactory);
	
	this.loopbackWeightSampler = new BoundedMultiModalGaussianSampler(Constants.PRODUCT_MSM_LOOPBACK_WEIGHT_GAUSSIANS,
			Constants.PRODUCT_MSM_LOOPBACK_WEIGHT_LOWERBOUND,
			Constants.PRODUCT_MSM_LOOPBACK_WEIGHT_UPPERBOUND,
			seedFactory);
	}
	
	public Sampler<PurchasingProfile> build() throws Exception
	{
		Map<ProductCategory, Sampler<MarkovModel<Product>>> categorySamplers = Maps.newHashMap();
		for(ProductCategory productCategory : productCategories)
		{
			ProductCategoryMarkovModelSampler sampler = new ProductCategoryMarkovModelSampler(productCategory, 
					fieldWeightSampler, fieldSimilarityWeightSampler, loopbackWeightSampler);
			categorySamplers.put(productCategory, sampler);
		}
		
		return new PurchasingProfileSampler(categorySamplers);
	}
}
