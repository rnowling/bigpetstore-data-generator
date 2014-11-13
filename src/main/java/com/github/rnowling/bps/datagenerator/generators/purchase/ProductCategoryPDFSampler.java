package com.github.rnowling.bps.datagenerator.generators.purchase;

import java.util.Map;
import java.util.Set;

import com.github.rnowling.bps.datagenerator.datamodels.Product;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.framework.pdfs.DiscretePDF;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class ProductCategoryPDFSampler implements Sampler<DiscretePDF<Product>>
{
	private final ProductCategory productCategory;
	private final Sampler<Double> fieldWeightSampler;
	private final Sampler<Double> fieldValueWeightSampler;
	
	public ProductCategoryPDFSampler(ProductCategory productCategory,
			Sampler<Double> fieldWeightSampler,
			Sampler<Double> fieldValueWeightSampler)
	{
		this.productCategory = productCategory;
		this.fieldWeightSampler = fieldWeightSampler;
		this.fieldValueWeightSampler = fieldValueWeightSampler;
	}
	
	protected <T> Map<T, Double> normalize(Map<T, Double> weights)
	{
		double weightSum = 0.0;
		for(double w : weights.values())
		{
			weightSum += w;
		}
		
		Map<T, Double> normalized = Maps.newHashMap();
		for(Map.Entry<T, Double> entry : weights.entrySet())
		{
			normalized.put(entry.getKey(), entry.getValue() / weightSum);
		}
		
		return normalized;
	}
	
	protected Map<Product, Double> generateProductWeights() throws Exception
	{
		Map<String, Double> fieldWeights = Maps.newHashMap();
		for(String fieldName : productCategory.getFieldNames())
		{
			double weight = fieldWeightSampler.sample();
			fieldWeights.put(fieldName, weight);
		}
		fieldWeights = normalize(fieldWeights);
		
		Map<String, Set<Object>> allFieldValues = Maps.newHashMap();
		for(String fieldName : productCategory.getFieldNames())
		{
			Set<Object> fieldValues = Sets.newHashSet();
			for(Product p : productCategory.getProducts())
			{
				Object fieldValue = p.getFieldValue(fieldName); 
				fieldValues.add(fieldValue);
			}
			allFieldValues.put(fieldName, fieldValues);
		}
		
		Map<String, Map<Object, Double>> allFieldValueWeights = Maps.newHashMap();
		for(String fieldName : productCategory.getFieldNames())
		{
			Map<Object, Double> fieldValueWeights = Maps.newHashMap();
			for(Object fieldValue : allFieldValues.get(fieldName))
			{
				double fieldValueWeight = fieldValueWeightSampler.sample();
				fieldValueWeights.put(fieldValue, fieldValueWeight);
			}

			allFieldValueWeights.put(fieldName, normalize(fieldValueWeights));
		}
		
		Map<Product, Double> productWeights = Maps.newHashMap();
		for(Product p : productCategory.getProducts())
		{
			double weight = 0.0;
			for(String fieldName : productCategory.getFieldNames())
			{
				Object fieldValue = p.getFieldValue(fieldName);
				weight += fieldWeights.get(fieldName) * allFieldValueWeights.get(fieldName).get(fieldValue);
			}
			productWeights.put(p, weight);
		}
		productWeights = normalize(productWeights);
		
		return productWeights;
	}
	
	public DiscretePDF<Product> sample() throws Exception
	{
		Map<Product, Double> probs = generateProductWeights();
		return new DiscretePDF<Product>(probs);
	}
	
}
