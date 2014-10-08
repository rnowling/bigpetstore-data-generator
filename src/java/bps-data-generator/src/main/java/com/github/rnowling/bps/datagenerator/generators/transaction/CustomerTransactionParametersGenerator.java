package com.github.rnowling.bps.datagenerator.generators.transaction;

import java.util.Map;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.SeedFactory;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.BoundedMultiModalGaussianSampler;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.RouletteWheelSampler;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.UniformIntSampler;
import com.github.rnowling.bps.datagenerator.datamodels.PetSpecies;
import com.github.rnowling.bps.datagenerator.generators.Generator;
import com.google.common.collect.Maps;

public class CustomerTransactionParametersGenerator implements Generator<CustomerTransactionParameters>
{
	final private Sampler<Integer> nPetsSampler;
	final private Sampler<PetSpecies> petSpeciesSampler;
	final private Sampler<Double> purchaseTriggerTimeSampler;
	final private Sampler<Double> transactionTriggerTimeSampler;

	public CustomerTransactionParametersGenerator(SeedFactory seedFactory)
	{

		this.nPetsSampler = new UniformIntSampler(Constants.MIN_PETS, Constants.MAX_PETS, seedFactory);
		
		Map<PetSpecies, Double> speciesWeights = Maps.newHashMap();
		for(PetSpecies species : PetSpecies.values())
		{
			speciesWeights.put(species, 1.0);
		}
		this.petSpeciesSampler = RouletteWheelSampler.create(speciesWeights, seedFactory);
		
		this.transactionTriggerTimeSampler = new BoundedMultiModalGaussianSampler(Constants.TRANSACTION_TRIGGER_TIME_GAUSSIANS,
					Constants.TRANSACTION_TRIGGER_TIME_MIN, Constants.TRANSACTION_TRIGGER_TIME_MAX,
					seedFactory);
		
		this.purchaseTriggerTimeSampler = new BoundedMultiModalGaussianSampler(Constants.PURCHASE_TRIGGER_TIME_GAUSSIANS,
				Constants.PURCHASE_TRIGGER_TIME_MIN, Constants.PURCHASE_TRIGGER_TIME_MAX,
				seedFactory);
	}
	
	protected void generatePets(CustomerTransactionParametersBuilder builder) throws Exception
	{
		int nPets = this.nPetsSampler.sample();
		
		for(int i = 0; i < nPets; i++)
		{
			PetSpecies species = this.petSpeciesSampler.sample();
			builder.addPet(species);	
		}
	}
	
	public CustomerTransactionParameters generate() throws Exception
	{
		CustomerTransactionParametersBuilder builder = new CustomerTransactionParametersBuilder();
		
		this.generatePets(builder);
		builder.setAveragePurchaseTriggerTime(this.purchaseTriggerTimeSampler.sample());
		builder.setAverageTransactionTriggerTime(this.transactionTriggerTimeSampler.sample());
		
		return builder.build();
	}
}
