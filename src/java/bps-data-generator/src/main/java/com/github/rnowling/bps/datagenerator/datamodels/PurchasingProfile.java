package com.github.rnowling.bps.datagenerator.datamodels;

import java.io.Serializable;
import java.util.Map;

import com.github.rnowling.bps.datagenerator.framework.markovmodels.MarkovModel;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

public class PurchasingProfile implements Serializable
{
	ImmutableMap<String, MarkovModel<Product>> productCategoryProfiles;
	
	public PurchasingProfile(Map<String, MarkovModel<Product>> productCategoryProfiles)
	{
		this.productCategoryProfiles = ImmutableMap.copyOf(productCategoryProfiles);
	}
	
	public ImmutableSet<String> getProductCategories()
	{
		return productCategoryProfiles.keySet();
	}
	
	public MarkovModel<Product> getProfile(String productCategory)
	{
		return productCategoryProfiles.get(productCategory);
	}
}
