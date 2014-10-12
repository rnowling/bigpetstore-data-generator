package com.github.rnowling.bps.datagenerator;

import java.util.List;

import com.github.rnowling.bps.datagenerator.builders.customer.CustomerSamplerBuilder;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.InputData;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Customer;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Store;
import com.github.rnowling.bps.datagenerator.statistics.SeedFactory;
import com.github.rnowling.bps.datagenerator.statistics.samplers.Sampler;

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
