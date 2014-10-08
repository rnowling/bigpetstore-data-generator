package com.github.rnowling.bps.datagenerator.generators.customer;

import com.github.rnowling.bps.datagenerator.SeedFactory;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.RouletteWheelSampler;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.Names;

public class NameSampler implements Sampler<Pair<String, String>>
{
	Sampler<String> firstNameSampler;
	Sampler<String> lastNameSampler;
	
	public NameSampler(Names names, SeedFactory seedFactory)
	{
		firstNameSampler = RouletteWheelSampler.create(names.getFirstNames(), seedFactory);
		lastNameSampler = RouletteWheelSampler.create(names.getLastNames(), seedFactory);
	}
	
	public Pair<String, String> sample() throws Exception
	{
		String firstName = firstNameSampler.sample();
		String lastName = lastNameSampler.sample();
		
		return new Pair<String, String>(firstName, lastName);
	}
}
