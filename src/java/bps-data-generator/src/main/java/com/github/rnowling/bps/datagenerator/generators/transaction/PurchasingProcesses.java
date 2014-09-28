package com.github.rnowling.bps.datagenerator.generators.transaction;

import java.util.Map;

import com.github.rnowling.bps.datagenerator.algorithms.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.Product;
import com.google.common.collect.ImmutableMap;

public class PurchasingProcesses
{
	ImmutableMap<String, Sampler<Product>> processes;
	
	public PurchasingProcesses(Map<String, Sampler<Product>> processes)
	{
		this.processes = ImmutableMap.copyOf(processes);
	}
	
	public Product simulatePurchase(String productCategory)
	{
		return this.processes.get(productCategory).sample();
	}
}
