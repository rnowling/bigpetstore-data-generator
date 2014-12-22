package com.github.rnowling.bps.datagenerator.generators.customer;

import java.util.List;

import com.github.rnowling.bps.datagenerator.datamodels.Store;
import com.github.rnowling.bps.datagenerator.framework.pdfs.ProbabilityDensityFunction;

public class CustomerStorePDF implements ProbabilityDensityFunction<Store>
{
	double populationSum = 0.0;
	
	public CustomerStorePDF(List<Store> stores)
	{
		for(Store store : stores)
		{
			populationSum += (double) store.getLocation().getPopulation();
		}
	}
	
	@Override
	public double probability(Store store)
	{
		return ((double) store.getLocation().getPopulation()) / populationSum;
	}

}
