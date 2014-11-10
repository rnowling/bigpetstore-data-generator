package com.github.rnowling.bps.datagenerator;

import com.github.rnowling.bps.datagenerator.datamodels.Store;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.InputData;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.generators.store.StoreSamplerBuilder;

public class StoreGenerator
{
	final Sampler<Store> sampler;
	
	public StoreGenerator(InputData inputData, SeedFactory seedFactory)
	{	
		StoreSamplerBuilder builder = new StoreSamplerBuilder(inputData.getZipcodeTable(), seedFactory);
		sampler = builder.build();
	}
	
	public Store generate() throws Exception
	{
		return sampler.sample();
	}
}
