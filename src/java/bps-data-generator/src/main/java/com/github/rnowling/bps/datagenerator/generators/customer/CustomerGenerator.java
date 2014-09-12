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
	CustomerLocationGenerator locationGenerator;
	
	public CustomerGenerator(List<Store> stores, InputData inputData, SeedFactory seedFactory)
	{
		this.stores = stores;
		this.seedFactory = seedFactory;
		this.nameGenerator = new NameGenerator(inputData.getNames(), seedFactory);
		this.locationGenerator = new CustomerLocationGenerator(inputData.getZipcodeTable(), stores,
				Constants.AVERAGE_CUSTOMER_STORE_DISTANCE, seedFactory);
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
	
	@Override
	public Customer generate() throws Exception
	{
		int id = generateId();
		Pair<String, String> name = generateName();
		ZipcodeRecord location = generateLocation();
		
		return new Customer(id, name, location);
	}

}
