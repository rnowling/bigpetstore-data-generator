package com.github.rnowling.bps.datagenerator.generators.customer;

import java.util.List;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.SeedFactory;
import com.github.rnowling.bps.datagenerator.algorithms.pdfs.ProbabilityDensityFunction;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.RouletteWheelSampler;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.InputData;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Customer;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Store;

public class CustomerSampler implements Sampler<Customer>
{
	SeedFactory seedFactory;
	
	List<Store> stores;
	
	int currentId = 0;
	NameSampler nameSampler;
	Sampler<ZipcodeRecord> locationSampler;
	
	public CustomerSampler(List<Store> stores, InputData inputData, SeedFactory seedFactory)
	{
		this.stores = stores;
		this.seedFactory = seedFactory;
		this.nameSampler = new NameSampler(inputData.getNames(), seedFactory);
		ProbabilityDensityFunction<ZipcodeRecord> locationPDF =
				new CustomerLocationPDF(inputData.getZipcodeTable(), stores,
				Constants.AVERAGE_CUSTOMER_STORE_DISTANCE);
		this.locationSampler = RouletteWheelSampler.create(inputData.getZipcodeTable(),
				locationPDF, seedFactory);
	}
	
	private int generateId()
	{
		int id = currentId;
		currentId += 1;
		
		return id;
	}
	
	private Pair<String, String> generateName() throws Exception
	{
		return this.nameSampler.sample();
	}
	
	private ZipcodeRecord generateLocation() throws Exception
	{
		return locationSampler.sample();
	}
	
	@Override
	public Customer sample() throws Exception
	{
		int id = generateId();
		Pair<String, String> name = generateName();
		ZipcodeRecord location = generateLocation();
		
		return new Customer(id, name, location);
	}

}
