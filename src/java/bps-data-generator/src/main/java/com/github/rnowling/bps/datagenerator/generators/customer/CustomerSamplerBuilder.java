package com.github.rnowling.bps.datagenerator.generators.customer;

import java.util.List;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.InputData;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Customer;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Store;
import com.github.rnowling.bps.datagenerator.statistics.SeedFactory;
import com.github.rnowling.bps.datagenerator.statistics.pdfs.ProbabilityDensityFunction;
import com.github.rnowling.bps.datagenerator.statistics.samplers.RouletteWheelSampler;
import com.github.rnowling.bps.datagenerator.statistics.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.statistics.samplers.SequenceSampler;

public class CustomerSamplerBuilder
{
	private final List<Store> stores;
	private final InputData inputData;
	private final SeedFactory seedFactory;
	
	public CustomerSamplerBuilder(List<Store> stores, InputData inputData, SeedFactory seedFactory)
	{
		this.stores = stores;
		this.seedFactory = seedFactory;
		this.inputData = inputData;
		
	}
	
	public Sampler<Customer> build()
	{
		ProbabilityDensityFunction<ZipcodeRecord> locationPDF = new CustomerLocationPDF(inputData.getZipcodeTable(), stores,
				Constants.AVERAGE_CUSTOMER_STORE_DISTANCE);
		
		Sampler<Integer> idSampler = new SequenceSampler();
		Sampler<String> firstNameSampler = RouletteWheelSampler.create(inputData.getNames().getFirstNames(), seedFactory);
		Sampler<String> lastNameSampler = RouletteWheelSampler.create(inputData.getNames().getLastNames(), seedFactory);
		Sampler<ZipcodeRecord> locationSampler = RouletteWheelSampler.create(inputData.getZipcodeTable(), 
				locationPDF, seedFactory);
		
		return new CustomerSampler(idSampler, firstNameSampler, lastNameSampler, locationSampler);
	}

}
