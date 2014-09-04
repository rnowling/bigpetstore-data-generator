package com.github.rnowling.bps.datagenerator.generators.customer;

import java.util.List;
import java.util.Vector;

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
		firstNameSampler = createSampler(names.getFirstNames(), seedFactory);
		lastNameSampler = createSampler(names.getLastNames(), seedFactory);
	}
	
	private Sampler<String> createSampler(List<Pair<String, Double>> nameWeights, SeedFactory seedFactory)
	{
		double normalizationFactor = 0.0;
		for(Pair<String, Double> pair : nameWeights)
		{
			normalizationFactor += pair.getSecond();
		}
		
		List<Pair<String, Double>> probabilities = new Vector<Pair<String, Double>>();
		for(Pair<String, Double> pair : nameWeights)
		{
			double prob = pair.getSecond() / normalizationFactor;
			Pair<String, Double> probPair = new Pair<String, Double>(pair.getFirst(), prob);
			probabilities.add(probPair);
		}
		
		return new RouletteWheelSampler<String>(probabilities, seedFactory);
	}
	
	public Pair<String, String> generate() throws Exception
	{
		String firstName = firstNameSampler.sample();
		String lastName = lastNameSampler.sample();
		
		return new Pair<String, String>(firstName, lastName);
	}
}
