package com.github.rnowling.bps.datagenerator.samplers.transaction;

import com.github.rnowling.bps.datagenerator.builders.transaction.CustomerTransactionParametersBuilder;
import com.github.rnowling.bps.datagenerator.datamodels.PetSpecies;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.CustomerTransactionParameters;
import com.github.rnowling.bps.datagenerator.statistics.samplers.Sampler;

public class CustomerTransactionParametersSampler implements Sampler<CustomerTransactionParameters>
{
	final private Sampler<Integer> nPetsSampler;
	final private Sampler<PetSpecies> petSpeciesSampler;
	final private Sampler<Double> purchaseTriggerTimeSampler;
	final private Sampler<Double> transactionTriggerTimeSampler;

	public CustomerTransactionParametersSampler(Sampler<Integer> nPetsSampler,
			Sampler<PetSpecies> petSpeciesSampler, 
			Sampler<Double> purchaseTriggerTimeSampler,
			Sampler<Double> transactionTriggerTimeSampler)
	{

		this.nPetsSampler = nPetsSampler;
		this.petSpeciesSampler = petSpeciesSampler;
		this.purchaseTriggerTimeSampler = purchaseTriggerTimeSampler;
		this.transactionTriggerTimeSampler = transactionTriggerTimeSampler;
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
	
	public CustomerTransactionParameters sample() throws Exception
	{
		CustomerTransactionParametersBuilder builder = new CustomerTransactionParametersBuilder();
		
		this.generatePets(builder);
		builder.setAveragePurchaseTriggerTime(this.purchaseTriggerTimeSampler.sample());
		builder.setAverageTransactionTriggerTime(this.transactionTriggerTimeSampler.sample());
		
		return builder.build();
	}
}
