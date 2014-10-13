package com.github.rnowling.bps.datagenerator.generators.customer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.InputData;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.Names;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Customer;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Store;
import com.github.rnowling.bps.datagenerator.datareaders.NameReader;
import com.github.rnowling.bps.datagenerator.datareaders.ZipcodeReader;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;

public class TestCustomerSamplerBuilder
{

	@Test
	public void testSample() throws Exception
	{
		ZipcodeReader zipcodeReader = new ZipcodeReader();
		zipcodeReader.setCoordinatesFile(Constants.COORDINATES_FILE);
		zipcodeReader.setIncomesFile(Constants.INCOMES_FILE);
		zipcodeReader.setPopulationFile(Constants.POPULATION_FILE);
		List<ZipcodeRecord> zipcodes = zipcodeReader.readData();
		
		NameReader nameReader = new NameReader(Constants.NAMEDB_FILE);
		Names names = nameReader.readData();
		
		InputData inputData = new InputData(zipcodes, names);
		
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
