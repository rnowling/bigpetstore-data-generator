package com.github.rnowling.bps.datagenerator.generators.customer;

import com.github.rnowling.bps.datagenerator.SeedFactory;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.RouletteWheelSampler;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.Names;
import com.github.rnowling.bps.datagenerator.generators.Generator;

public class NameGenerator implements Generator<Pair<String, String>>
{
	Sampler<String> firstNameSampler;
	Sampler<String> lastNameSampler;
	
	public NameGenerator(Names names, SeedFactory seedFactory)
	{
		firstNameSampler = RouletteWheelSampler.create(names.getFirstNames(), seedFactory);
		lastNameSampler = RouletteWheelSampler.create(names.getLastNames(), seedFactory);
	}
	
	public Pair<String, String> generate() throws Exception
	{
		String firstName = firstNameSampler.sample();
		String lastName = lastNameSampler.sample();
		
		return new Pair<String, String>(firstName, lastName);
	}
}
