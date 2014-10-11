package com.github.rnowling.bps.datagenerator.datamodels.simulation;

import com.github.rnowling.bps.datagenerator.datamodels.PetSpecies;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class CustomerTransactionParametersBuilder
{
	private Multiset<PetSpecies> petCounts;
	private double averageTransactionTriggerTime;
	private double averagePurchaseTriggerTime;
	
	public CustomerTransactionParametersBuilder()
	{
		this.petCounts = HashMultiset.create();
		this.averagePurchaseTriggerTime = 0.0;
		this.averageTransactionTriggerTime = 0.0;
	}
	
	public void addPet(PetSpecies species)
	{
		this.petCounts.add(species);
	}

	public void setAverageTransactionTriggerTime(
			double averageTransactionTriggerTime)
	{
		this.averageTransactionTriggerTime = averageTransactionTriggerTime;
	}

	public void setAveragePurchaseTriggerTime(double averagePurchaseTriggerTime)
	{
		this.averagePurchaseTriggerTime = averagePurchaseTriggerTime;
	}
	
	public CustomerTransactionParameters build()
	{
		return new CustomerTransactionParameters(this.petCounts,
				this.averageTransactionTriggerTime,
				this.averagePurchaseTriggerTime);
	}
}
