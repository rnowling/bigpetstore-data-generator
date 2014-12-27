package com.github.rnowling.bps.datagenerator.generators.customer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.datamodels.Customer;
import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.github.rnowling.bps.datagenerator.datamodels.Store;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.InputData;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.Names;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.WeatherStationParameters;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;
import com.google.common.collect.ImmutableMap;

public class TestCustomerSamplerBuilder
{

	@Test
	public void testSample() throws Exception
	{	
		Map<String, Double> nameList = ImmutableMap.of("Fred", 1.0, "George", 1.0, "Gary", 1.0, "Fiona", 1.0);
		List<ZipcodeRecord> zipcodes = Arrays.asList(new ZipcodeRecord[] {				
				new ZipcodeRecord("11111", Pair.create(1.0, 1.0), "AZ", "Tempte", 30000.0, 100),
				new ZipcodeRecord("22222", Pair.create(2.0, 2.0), "AZ", "Phoenix", 45000.0, 200),
				new ZipcodeRecord("33333", Pair.create(3.0, 3.0), "AZ", "Flagstaff", 60000.0, 300)
				});
		
		Names names = new Names(nameList, nameList);
		
		List<WeatherStationParameters> weatherParameters = Arrays.asList(new WeatherStationParameters[] {
			new WeatherStationParameters("234", "Pompano Beach", "FL", Pair.create(1.0, 2.0), 0.0,
					0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
		});
		
		// don't need product categories for building customers
		InputData inputData = new InputData(zipcodes, names, new ArrayList<ProductCategory>(), weatherParameters);
		
		List<Store> stores = Arrays.asList(new Store(0, "Store_0", zipcodes.get(0)),
				new Store(1, "Store_1", zipcodes.get(1)),
				new Store(2, "Store_2", zipcodes.get(2))
				);
		
		SeedFactory factory = new SeedFactory(1234);
		
		CustomerSamplerBuilder builder = new CustomerSamplerBuilder(stores, inputData, factory);
		Sampler<Customer> sampler = builder.build();
		
		Customer customer = sampler.sample();
		
		assertNotNull(customer);
		assertTrue(customer.getId() >= 0);
		assertNotNull(customer.getName());
		assertNotNull(customer.getName().getFirst());
		assertNotNull(customer.getName().getSecond());
		assertNotNull(customer.getLocation());
		
	}

}
