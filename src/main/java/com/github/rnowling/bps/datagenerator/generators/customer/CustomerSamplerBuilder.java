package com.github.rnowling.bps.datagenerator.generators.customer;

import java.util.List;
import java.util.Map;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.datamodels.Customer;
import com.github.rnowling.bps.datagenerator.datamodels.Store;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.InputData;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.pdfs.ProbabilityDensityFunction;
import com.github.rnowling.bps.datagenerator.framework.samplers.ConditionalSampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.RouletteWheelSampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.SequenceSampler;
import com.google.common.collect.Maps;

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
	
	protected ConditionalSampler<ZipcodeRecord, Store> buildLocationSampler()
	{
		final Map<Store, Sampler<ZipcodeRecord>> locationSamplers = Maps.newHashMap();
		for(Store store : stores)
		{
			ProbabilityDensityFunction<ZipcodeRecord> locationPDF = new CustomerLocationPDF(inputData.getZipcodeTable(),
					store, Constants.AVERAGE_CUSTOMER_STORE_DISTANCE);
			Sampler<ZipcodeRecord> locationSampler = RouletteWheelSampler.create(inputData.getZipcodeTable(), locationPDF, seedFactory);
			locationSamplers.put(store, locationSampler);
		}
			
		return new ConditionalSampler<ZipcodeRecord, Store>()
				{
					public ZipcodeRecord sample(Store store) throws Exception
					{
						return locationSamplers.get(store).sample();
					}
					
					public Sampler<ZipcodeRecord> fixConditional(Store store)
					{
						return locationSamplers.get(store);
					}
				};
	}
	
	public Sampler<Customer> build()
	{
		
		
		Sampler<Integer> idSampler = new SequenceSampler();
		Sampler<String> firstNameSampler = RouletteWheelSampler.create(inputData.getNames().getFirstNames(), seedFactory);
		Sampler<String> lastNameSampler = RouletteWheelSampler.create(inputData.getNames().getLastNames(), seedFactory);
		Sampler<Store> storeSampler = RouletteWheelSampler.createUniform(stores, seedFactory);
		
		return new CustomerSampler(idSampler, firstNameSampler, lastNameSampler, storeSampler, buildLocationSampler());
	}

}
