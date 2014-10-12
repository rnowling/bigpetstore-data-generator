package com.github.rnowling.bps.datagenerator;

import java.util.List;

import com.github.rnowling.bps.datagenerator.datamodels.inputs.InputData;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Customer;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Store;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.generators.customer.CustomerSamplerBuilder;

public class CustomerGenerator
{
	final Sampler<Customer> sampler;
	
	public CustomerGenerator(InputData inputData, List<Store> stores, SeedFactory seedFactory)
	{
		CustomerSamplerBuilder builder = new CustomerSamplerBuilder(stores, inputData, seedFactory);
		sampler = builder.build();
	}
	
	public Customer generate() throws Exception
	{
		return sampler.sample();
	}
}
