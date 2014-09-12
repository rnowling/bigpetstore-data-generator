package com.github.rnowling.bps.datagenerator.generators.customer;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.SeedFactory;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.BoundedGaussianSampler;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.UniformIntSampler;
import com.github.rnowling.bps.datagenerator.datamodels.Customer;
import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.github.rnowling.bps.datagenerator.datamodels.PetSpecies;
import com.github.rnowling.bps.datagenerator.datamodels.Store;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.InputData;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;
import com.github.rnowling.bps.datagenerator.generators.Generator;

public class CustomerGenerator implements Generator<Customer>
{
	SeedFactory seedFactory;
	
	List<Store> stores;
	
	int currentId = 0;
	NameGenerator nameGenerator;
	Sampler<Integer> petNumberSampler;
	BoundedGaussianSampler transactionTriggerTimeSampler;
	BoundedGaussianSampler purchaseTriggerTimeSampler;
	CustomerLocationGenerator locationGenerator;
	Random rng;
	
	public CustomerGenerator(List<Store> stores, InputData inputData, SeedFactory seedFactory)
	{
		this.stores = stores;
		this.seedFactory = seedFactory;
		this.nameGenerator = new NameGenerator(inputData.getNames(), seedFactory);
		this.locationGenerator = new CustomerLocationGenerator(inputData.getZipcodeTable(), stores,
				Constants.AVERAGE_CUSTOMER_STORE_DISTANCE, seedFactory);
		this.petNumberSampler = new UniformIntSampler(Constants.MIN_PETS, Constants.MAX_PETS, seedFactory);
		this.transactionTriggerTimeSampler = new BoundedGaussianSampler(Constants.TRANSACTION_TRIGGER_TIME_AVERAGE,
				Constants.TRANSACTION_TRIGGER_TIME_VARIANCE, Constants.TRANSACTION_TRIGGER_TIME_MIN,
				Constants.TRANSACTION_TRIGGER_TIME_MAX, seedFactory);
		this.purchaseTriggerTimeSampler = new BoundedGaussianSampler(Constants.PURCHASE_TRIGGER_TIME_AVERAGE,
				Constants.PURCHASE_TRIGGER_TIME_VARIANCE, Constants.PURCHASE_TRIGGER_TIME_MIN,
				Constants.PURCHASE_TRIGGER_TIME_MAX, seedFactory);
		
		
		rng = new Random(seedFactory.getNextSeed());
	}
	
	private int generateId()
	{
		int id = currentId;
		currentId += 1;
		
		return id;
	}
	
	private Pair<String, String> generateName() throws Exception
	{
		return this.nameGenerator.generate();
	}
	
	private ZipcodeRecord generateLocation() throws Exception
	{
		return locationGenerator.generate();
	}
	
	private Map<PetSpecies, Integer> generatePets() throws Exception
	{
		int numberPets = petNumberSampler.sample();
		int remaining = numberPets;
		Map<PetSpecies, Integer> speciesCounts = new HashMap<PetSpecies, Integer>();
		
		PetSpecies[] allPetSpecies = PetSpecies.values();
		for(int i = 0; i < allPetSpecies.length - 1; i++)
		{
			PetSpecies species = allPetSpecies[i];
			int count = rng.nextInt(remaining + 1);
			remaining -= count;
			speciesCounts.put(species, count);
		}
		speciesCounts.put(allPetSpecies[allPetSpecies.length - 1], remaining);
		
		return Collections.unmodifiableMap(speciesCounts);
	}
	
	private double generateAverageTransactionTriggerTime()
	{
		return this.transactionTriggerTimeSampler.sample();
	}
	
	private double generateAveragePurchaseTriggerTime()
	{
		return this.purchaseTriggerTimeSampler.sample();
	}
	
	@Override
	public Customer generate() throws Exception
	{
		int id = generateId();
		Pair<String, String> name = generateName();
		ZipcodeRecord location = generateLocation();
		Map<PetSpecies, Integer> speciesCounts = generatePets();
		double averageTransactionTriggerTime = generateAverageTransactionTriggerTime();
		double averagePurchaseTriggerTime = generateAveragePurchaseTriggerTime();
		
		return new Customer(id, name, location, speciesCounts, averageTransactionTriggerTime,
				averagePurchaseTriggerTime);
	}

}
