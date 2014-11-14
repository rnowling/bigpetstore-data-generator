package com.github.rnowling.bps.datagenerator.generators.purchasingprofile;

import java.util.Map;

import com.github.rnowling.bps.datagenerator.datamodels.Product;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.framework.markovmodels.MarkovModel;
import com.github.rnowling.bps.datagenerator.framework.markovmodels.MarkovModelBuilder;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;
import com.google.common.collect.Maps;

public class ProductCategoryMarkovModelSampler implements Sampler<MarkovModel<Product>>
{
	final ProductCategory productCategory;
	final Sampler<Double> fieldWeightSampler;
	final Sampler<Double> fieldSimilarityWeightSampler;
	final Sampler<Double> loopbackWeightSampler;
	
	Map<String, Double> fieldWeights;
	Map<String, Double> fieldSimilarityWeights;
	double loopbackWeight;
	
	public ProductCategoryMarkovModelSampler(ProductCategory productCategory, 
			Sampler<Double> fieldWeightSampler, Sampler<Double> fieldSimilarityWeightSampler,
			Sampler<Double> loopbackWeightSampler)
	{
		this.productCategory = productCategory;
		
		this.fieldSimilarityWeightSampler = fieldSimilarityWeightSampler;
		this.fieldWeightSampler = fieldWeightSampler;
		this.loopbackWeightSampler = loopbackWeightSampler;
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
