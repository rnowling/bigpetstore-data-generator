package com.github.rnowling.bps.datagenerator.generators.purchase;

import java.util.Collection;
import java.util.Map;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.datamodels.Product;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.markovmodels.MarkovModel;
import com.github.rnowling.bps.datagenerator.framework.pdfs.DiscretePDF;
import com.github.rnowling.bps.datagenerator.framework.samplers.BoundedMultiModalGaussianSampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.ExponentialSampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

public class PurchasingModelSamplerBuilder
{
	final ImmutableList<ProductCategory> productCategories;
	final SeedFactory seedFactory;
	
	public PurchasingModelSamplerBuilder(Collection<ProductCategory> productCategories, SeedFactory seedFactory)
	{
		this.productCategories = ImmutableList.copyOf(productCategories);
		this.seedFactory = seedFactory;
	}
	
	public Sampler<StaticPurchasingModel> buildStaticPurchasingModel() throws Exception
	{
		Sampler<Double> fieldWeightSampler;
		Sampler<Double> fieldValueWeightSampler;
		
		if(Constants.STATIC_PURCHASING_MODEL_FIELD_WEIGHT_DISTRIBUTION_TYPE == Constants.DistributionType.BOUNDED_MULTIMODAL_GAUSSIAN)
		{
			fieldWeightSampler = new BoundedMultiModalGaussianSampler(Constants.STATIC_FIELD_WEIGHT_GAUSSIANS, 
					Constants.STATIC_FIELD_WEIGHT_LOWERBOUND, 
					Constants.STATIC_FIELD_WEIGHT_UPPERBOUND,
					seedFactory);
		}
		else
		{
			fieldWeightSampler = new ExponentialSampler(Constants.STATIC_FIELD_WEIGHT_EXPONENTIAL, seedFactory);
		}
		
		if(Constants.STATIC_PURCHASING_MODEL_FIELD_VALUE_WEIGHT_DISTRIBUTION_TYPE == Constants.DistributionType.BOUNDED_MULTIMODAL_GAUSSIAN)
		{
			fieldValueWeightSampler = new BoundedMultiModalGaussianSampler(Constants.STATIC_FIELD_VALUE_WEIGHT_GAUSSIANS, 
					Constants.STATIC_FIELD_VALUE_WEIGHT_LOWERBOUND, 
					Constants.STATIC_FIELD_VALUE_WEIGHT_UPPERBOUND,
					seedFactory);
		}
		else
		{
			fieldValueWeightSampler = new ExponentialSampler(Constants.STATIC_FIELD_VALUE_WEIGHT_EXPONENTIAL, seedFactory);
		}
		
		Map<ProductCategory, Sampler<DiscretePDF<Product>>> categorySamplers = Maps.newHashMap();
		for(ProductCategory productCategory : productCategories)
		{
			Sampler<DiscretePDF<Product>> sampler = new ProductCategoryPDFSampler(productCategory,
					fieldWeightSampler, fieldValueWeightSampler);
			categorySamplers.put(productCategory, sampler);
		}
		
		return new StaticPurchasingModelSampler(categorySamplers);
	}
	
	public Sampler<MarkovPurchasingModel> buildMarkovPurchasingModel() throws Exception
	{
		
		Sampler<Double> fieldWeightSampler = new BoundedMultiModalGaussianSampler(Constants.PRODUCT_MSM_FIELD_WEIGHT_GAUSSIANS, 
				Constants.PRODUCT_MSM_FIELD_WEIGHT_LOWERBOUND, 
				Constants.PRODUCT_MSM_FIELD_WEIGHT_UPPERBOUND,
				seedFactory);
	
		Sampler<Double> fieldSimilarityWeightSampler = new BoundedMultiModalGaussianSampler(Constants.PRODUCT_MSM_FIELD_SIMILARITY_WEIGHT_GAUSSIANS,
				Constants.PRODUCT_MSM_FIELD_SIMILARITY_WEIGHT_LOWERBOUND, 
				Constants.PRODUCT_MSM_FIELD_SIMILARITY_WEIGHT_UPPERBOUND,
				seedFactory);
		
		Sampler<Double> loopbackWeightSampler = new BoundedMultiModalGaussianSampler(Constants.PRODUCT_MSM_LOOPBACK_WEIGHT_GAUSSIANS,
				Constants.PRODUCT_MSM_LOOPBACK_WEIGHT_LOWERBOUND,
				Constants.PRODUCT_MSM_LOOPBACK_WEIGHT_UPPERBOUND,
				seedFactory);
		
		Map<ProductCategory, Sampler<MarkovModel<Product>>> categorySamplers = Maps.newHashMap();
		for(ProductCategory productCategory : productCategories)
		{
			ProductCategoryMarkovModelSampler sampler = new ProductCategoryMarkovModelSampler(productCategory, 
					fieldWeightSampler, fieldSimilarityWeightSampler, loopbackWeightSampler);
			categorySamplers.put(productCategory, sampler);
		}
		
		return new MarkovPurchasingModelSampler(categorySamplers);
	}
	
	public Sampler<? extends PurchasingModel> build() throws Exception
	{
		if(Constants.PURCHASING_MODEL_TYPE == Constants.PurchasingModelType.DYNAMIC)
		{
			return buildMarkovPurchasingModel();
		}
		else
		{
			return buildStaticPurchasingModel();
		}
	}
}
