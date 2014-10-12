package com.github.rnowling.bps.datagenerator;

import com.github.rnowling.bps.datagenerator.builders.store.StoreSamplerBuilder;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.InputData;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Store;
import com.github.rnowling.bps.datagenerator.statistics.SeedFactory;
import com.github.rnowling.bps.datagenerator.statistics.samplers.Sampler;

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
