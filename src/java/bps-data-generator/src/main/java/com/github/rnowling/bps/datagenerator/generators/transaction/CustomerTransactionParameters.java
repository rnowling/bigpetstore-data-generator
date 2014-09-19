package com.github.rnowling.bps.datagenerator.generators.transaction;

import com.github.rnowling.bps.datagenerator.datamodels.PetSpecies;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;

public class CustomerTransactionParameters
{
	final ImmutableMultiset<PetSpecies> petCounts;
	final double averageTransactionTriggerTime;
	final double averagePurchaseTriggerTime;
	
	public CustomerTransactionParameters(Multiset<PetSpecies> petCounts,
			double averageTransactionTriggerTime, double averagePurchaseTriggerTime)
	{
		this.petCounts = ImmutableMultiset.copyOf(petCounts);
		this.averageTransactionTriggerTime = averageTransactionTriggerTime;
		this.averagePurchaseTriggerTime = averagePurchaseTriggerTime;
	}

	public double getAverageTransactionTriggerTime()
	{
		return averageTransactionTriggerTime;
	}

	public double getAveragePurchaseTriggerTime()
	{
		return averagePurchaseTriggerTime;
	}
	
	public int countPetsBySpecies(PetSpecies species)
	{
		return petCounts.count(species);
	}
	
	public int countPets()
	{
		return petCounts.size();
	}
	
}
