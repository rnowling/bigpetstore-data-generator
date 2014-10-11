package com.github.rnowling.bps.datagenerator.generators.customer;

import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Customer;
import com.github.rnowling.bps.datagenerator.statistics.samplers.Sampler;

public class CustomerSampler implements Sampler<Customer>
{
	private final Sampler<Integer> idSampler;
	private final Sampler<String> firstNameSampler;
	private final Sampler<String> lastNameSampler;
	private final Sampler<ZipcodeRecord> locationSampler;
	
	
	public CustomerSampler(Sampler<Integer> idSampler, Sampler<String> firstNameSampler,
			Sampler<String> lastNameSampler,
			Sampler<ZipcodeRecord> locationSampler)
	{
		this.idSampler = idSampler;
		this.firstNameSampler = firstNameSampler;
		this.lastNameSampler = lastNameSampler;
		this.locationSampler = locationSampler;
	}

	public Customer sample() throws Exception
	{
		Integer id = idSampler.sample();
		Pair<String, String> name = Pair.create(firstNameSampler.sample(),
				lastNameSampler.sample());
		ZipcodeRecord location = locationSampler.sample();
		
		return new Customer(id, name, location);
	}

}
