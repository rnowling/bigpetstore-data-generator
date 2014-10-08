package com.github.rnowling.bps.datagenerator.generators.customer;

import com.github.rnowling.bps.datagenerator.algorithms.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Customer;

public class CustomerSampler implements Sampler<Customer>
{
	private final Sampler<Integer> idSampler;
	private final Sampler<Pair<String, String>> nameSampler;
	private final Sampler<ZipcodeRecord> locationSampler;
	
	
	public CustomerSampler(Sampler<Integer> idSampler, Sampler<Pair<String, String>> nameSampler,
			Sampler<ZipcodeRecord> locationSampler)
	{
		this.idSampler = idSampler;
		this.nameSampler = nameSampler;
		this.locationSampler = locationSampler;
	}

	public Customer sample() throws Exception
	{
		Integer id = idSampler.sample();
		Pair<String, String> name = nameSampler.sample();
		ZipcodeRecord location = locationSampler.sample();
		
		return new Customer(id, name, location);
	}

}
