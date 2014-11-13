package com.github.rnowling.bps.datagenerator.generators.purchase;

import java.util.Map;

import com.github.rnowling.bps.datagenerator.datamodels.Product;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.pdfs.DiscretePDF;
import com.github.rnowling.bps.datagenerator.framework.samplers.RouletteWheelSampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

public class StaticPurchasingModel implements PurchasingModel
{

	private static final long serialVersionUID = 5863830733003282570L;
	
	private final ImmutableMap<String, DiscretePDF<Product>> productPDFs;
	
	public StaticPurchasingModel(Map<String, DiscretePDF<Product>> productPDFs)
	{
		this.productPDFs = ImmutableMap.copyOf(productPDFs);
	}

	@Override
	public ImmutableSet<String> getProductCategories()
	{
		return productPDFs.keySet();
	}

	@Override
	public PurchasingProcesses buildProcesses(SeedFactory seedFactory)
	{
		Map<String, Sampler<Product>> processes = Maps.newHashMap();
		for(String category : getProductCategories())
		{
			DiscretePDF<Product> pdf = productPDFs.get(category);
			processes.put(category, RouletteWheelSampler.create(pdf, seedFactory));
		}
		
		return new PurchasingProcesses(processes);
	}

}
