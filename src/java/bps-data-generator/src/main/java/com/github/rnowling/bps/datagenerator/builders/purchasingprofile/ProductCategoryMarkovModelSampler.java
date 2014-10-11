package com.github.rnowling.bps.datagenerator.builders.purchasingprofile;

import java.util.Map;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.Product;
import com.github.rnowling.bps.datagenerator.statistics.SeedFactory;
import com.github.rnowling.bps.datagenerator.statistics.markovmodels.MarkovModel;
import com.github.rnowling.bps.datagenerator.statistics.markovmodels.MarkovModelBuilder;
import com.github.rnowling.bps.datagenerator.statistics.samplers.BoundedMultiModalGaussianSampler;
import com.github.rnowling.bps.datagenerator.statistics.samplers.Sampler;
import com.google.common.collect.Maps;

public class ProductCategoryMarkovModelSampler implements Sampler<MarkovModel<Product>>
{
	ProductCategory productCategory;
	Sampler<Double> fieldWeightSampler;
	Sampler<Double> fieldSimilarityWeightSampler;
	Sampler<Double> loopbackWeightSampler;
	
	Map<String, Double> fieldWeights;
	Map<String, Double> fieldSimilarityWeights;
	double loopbackWeight;
	
	public ProductCategoryMarkovModelSampler(ProductCategory productCategory, SeedFactory seedFactory)
	{
		this.productCategory = productCategory;
		
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
	
	protected void generateWeights() throws Exception
	{
		fieldWeights = Maps.newHashMap();
		fieldSimilarityWeights = Maps.newHashMap();
		
		for(String fieldName : productCategory.getFieldNames())
		{
			fieldWeights.put(fieldName, fieldWeightSampler.sample());
			fieldSimilarityWeights.put(fieldName,fieldSimilarityWeightSampler.sample());
		}
		
		loopbackWeight = loopbackWeightSampler.sample();
	}
	
	protected <T> Map<T, Double> normalize(Map<T, Double> weights)
	{
		Map<T, Double> normalized = Maps.newHashMap();
		
		double weightSum = 0.0;
		for(double weight : weights.values())
		{
			weightSum += weight;
		}
		
		for(Map.Entry<T, Double> entry : weights.entrySet())
		{
			normalized.put(entry.getKey(), entry.getValue() / weightSum);
		}
		
		return normalized;
	}
	
	protected double productPairWeight(Product product1, Product product2)
	{
		double weightSum = 0.0;
		for(String fieldName : productCategory.getFieldNames())
		{
			double fieldWeight = this.fieldWeights.get(fieldName);
			
			if(product1.getFieldValue(fieldName).equals(product2.getFieldValue(fieldName)))
			{
				fieldWeight *= this.fieldSimilarityWeights.get(fieldName);
			}
			else
			{
				fieldWeight *= (1.0 - this.fieldSimilarityWeights.get(fieldName));
			}
			
			weightSum += fieldWeight;
		}
		return weightSum;
	}
	
	public MarkovModel<Product> sample() throws Exception
	{
		generateWeights();
		fieldWeights = normalize(fieldWeights);
		fieldSimilarityWeights = normalize(fieldSimilarityWeights);
		
		MarkovModelBuilder<Product> builder = new MarkovModelBuilder<Product>();
		
		for(Product product1 : productCategory.getProducts())
		{
			builder.addStartState(product1, 1.0);
			
			double weightSum = 0.0;
			for(Product product2 : productCategory.getProducts())
			{
				if(!product1.equals(product2))
				{
					weightSum += productPairWeight(product1, product2);
				}
			}
			
			for(Product product2 : productCategory.getProducts())
			{
				double weight = 0.0;
				if(!product1.equals(product2))
				{
					weight = (1.0 - loopbackWeight) * productPairWeight(product1, product2) / weightSum;
				}
				else
				{	weight = loopbackWeight;
					
				}
				
				builder.addTransition(product1, product2, weight);
			}
		}
		
		return builder.build();
	}
}
