package com.github.rnowling.bps.datagenerator.datamodels.simulation;

import java.util.Collection;
import java.util.Set;

import com.github.rnowling.bps.datagenerator.datamodels.PetSpecies;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;

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
	
	public int countPetsBySpecies(Collection<PetSpecies> allSpecies)
	{
		int count = 0;
		Set<PetSpecies> speciesSet = Sets.newHashSet(allSpecies);
		for(PetSpecies species : speciesSet)
		{
			count += countPetsBySpecies(species);
		}
		
		return count;
	}
	
	public int countPets()
	{
		return petCounts.size();
	}
	
}
