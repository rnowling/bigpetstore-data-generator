package com.github.rnowling.bps.datagenerator.generators.transaction;

import java.util.Map;

import com.github.rnowling.bps.datagenerator.datamodels.Product;
import com.github.rnowling.bps.datagenerator.framework.samplers.ConditionalSampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;
import com.google.common.collect.ImmutableMap;

public class PurchasingProcesses implements ConditionalSampler<Product, String>
{
	ImmutableMap<String, Sampler<Product>> processes;
	
	public PurchasingProcesses(Map<String, Sampler<Product>> processes)
	{
		this.processes = ImmutableMap.copyOf(processes);
	}
	
	public Product sample(String productCategory) throws Exception
	{
		return this.processes.get(productCategory).sample();
	}
	
	public Sampler<Product> fixConditional(String productCategory) throws Exception
	{
		return this.processes.get(productCategory);
	}
}
