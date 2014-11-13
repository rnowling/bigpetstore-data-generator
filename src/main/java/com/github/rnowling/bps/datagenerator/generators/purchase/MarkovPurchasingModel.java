package com.github.rnowling.bps.datagenerator.generators.purchase;

import java.util.Map;

import com.github.rnowling.bps.datagenerator.datamodels.Product;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.markovmodels.MarkovModel;
import com.github.rnowling.bps.datagenerator.framework.markovmodels.MarkovProcess;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

public class MarkovPurchasingModel implements PurchasingModel
{

	private static final long serialVersionUID = 3098355461347511619L;
	ImmutableMap<String, MarkovModel<Product>> productCategoryProfiles;
	
	public MarkovPurchasingModel(Map<String, MarkovModel<Product>> productCategoryProfiles)
	{
		this.productCategoryProfiles = ImmutableMap.copyOf(productCategoryProfiles);
	}
	
	@Override
	public ImmutableSet<String> getProductCategories()
	{
		return productCategoryProfiles.keySet();
	}

	public MarkovModel<Product> getProfile(String productCategory)
	{
		return productCategoryProfiles.get(productCategory);
	}

	@Override
	public PurchasingProcesses buildProcesses(SeedFactory seedFactory)
	{
		Map<String, Sampler<Product>> processes = Maps.newHashMap();
		for(String category : getProductCategories())
		{
			MarkovModel<Product> model = getProfile(category);
			processes.put(category, new MarkovProcess<Product>(model, seedFactory));
		}
		
		return new PurchasingProcesses(processes);
	}
}
