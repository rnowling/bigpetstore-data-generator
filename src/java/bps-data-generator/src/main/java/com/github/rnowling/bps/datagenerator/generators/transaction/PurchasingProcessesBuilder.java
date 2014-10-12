package com.github.rnowling.bps.datagenerator.generators.transaction;

import java.util.Map;

import com.github.rnowling.bps.datagenerator.datamodels.simulation.Product;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.markovmodels.MarkovModel;
import com.github.rnowling.bps.datagenerator.framework.markovmodels.MarkovProcess;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.generators.purchasingprofile.PurchasingProfile;
import com.google.common.collect.Maps;

public class PurchasingProcessesBuilder
{
	PurchasingProfile purchasingProfile;
	SeedFactory seedFactory;
	
	public PurchasingProcessesBuilder(SeedFactory seedFactory)
	{
		this.seedFactory = seedFactory;
	}
	
	public void setPurchasingProfile(PurchasingProfile purchasingProfile)
	{
		this.purchasingProfile = purchasingProfile;
	}
	
	public PurchasingProcesses build()
	{
		Map<String, Sampler<Product>> processes = Maps.newHashMap();
		for(String category : purchasingProfile.getProductCategories())
		{
			MarkovModel<Product> model = purchasingProfile.getProfile(category);
			processes.put(category, new MarkovProcess<Product>(model, seedFactory));
		}
		
		return new PurchasingProcesses(processes);
	}
}
