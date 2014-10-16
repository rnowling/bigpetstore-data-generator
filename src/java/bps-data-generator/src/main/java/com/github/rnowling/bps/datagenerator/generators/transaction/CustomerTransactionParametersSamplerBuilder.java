package com.github.rnowling.bps.datagenerator.generators.transaction;

import java.util.Arrays;

import com.github.rnowling.bps.datagenerator.datamodels.PetSpecies;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.samplers.BoundedMultiModalGaussianSampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.RouletteWheelSampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.UniformIntSampler;
import com.github.rnowling.bps.datagenerator.resources.Constants;

public class CustomerTransactionParametersSamplerBuilder
{
	final private SeedFactory seedFactory;

	public CustomerTransactionParametersSamplerBuilder(SeedFactory seedFactory)
	{
		this.seedFactory = seedFactory;
	}
	
	public Sampler<CustomerTransactionParameters> build()
	{
		Sampler<Integer> nPetsSampler = new UniformIntSampler(Constants.MIN_PETS, Constants.MAX_PETS, seedFactory);
		
		Sampler<PetSpecies> petSpeciesSampler = RouletteWheelSampler.createUniform(Arrays.asList(PetSpecies.values()), seedFactory);
		
		Sampler<Double> transactionTriggerTimeSampler = new BoundedMultiModalGaussianSampler(Constants.TRANSACTION_TRIGGER_TIME_GAUSSIANS,
					Constants.TRANSACTION_TRIGGER_TIME_MIN, Constants.TRANSACTION_TRIGGER_TIME_MAX,
					seedFactory);
		
		Sampler<Double> purchaseTriggerTimeSampler = new BoundedMultiModalGaussianSampler(Constants.PURCHASE_TRIGGER_TIME_GAUSSIANS,
				Constants.PURCHASE_TRIGGER_TIME_MIN, Constants.PURCHASE_TRIGGER_TIME_MAX,
				seedFactory);
		
		return new CustomerTransactionParametersSampler(nPetsSampler, petSpeciesSampler,
				transactionTriggerTimeSampler, purchaseTriggerTimeSampler);
	}

}
