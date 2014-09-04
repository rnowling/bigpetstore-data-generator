package com.github.rnowling.bps.datagenerator.datamodels;

import java.util.Collections;
import java.util.Map;

import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;

public class Customer
{
	int id;
	Pair<String, String> name;
	ZipcodeRecord location;
	
	int numberPets;
	
	Map<PetSpecies, Integer> petSpeciesCounts;
	
	double averageTransactionTriggerTime;
	double averagePurchaseTriggerTime;
	
	public Customer(int id, Pair<String, String> name, ZipcodeRecord location, Map<PetSpecies, Integer> pets,
			double averageTransactionTriggerTime, double averagePurchaseTriggerTime)
	{
		this.id = id;
		this.name = name;
		this.location = location;
		this.petSpeciesCounts = pets;
		
		this.numberPets = 0;
		for(int count : pets.values())
			numberPets += count;
		this.averagePurchaseTriggerTime = averagePurchaseTriggerTime;
		this.averageTransactionTriggerTime = averageTransactionTriggerTime;
	}

	public int getId()
	{
		return id;
	}

	public Pair<String, String> getName()
	{
		return name;
	}

	public int getNumberPets()
	{
		return numberPets;
	}

	public Map<PetSpecies, Integer> getPetSpeciesCounts()
	{
		return Collections.unmodifiableMap(petSpeciesCounts);
	}

	public double getAverageTransactionTriggerTime()
	{
		return averageTransactionTriggerTime;
	}

	public double getAveragePurchaseTriggerTime()
	{
		return averagePurchaseTriggerTime;
	}
	
	
}
