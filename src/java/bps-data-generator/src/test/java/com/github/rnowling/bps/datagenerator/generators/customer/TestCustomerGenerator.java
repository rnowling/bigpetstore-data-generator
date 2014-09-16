package com.github.rnowling.bps.datagenerator.generators.customer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.SeedFactory;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.InputData;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.Names;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Customer;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Store;
import com.github.rnowling.bps.datagenerator.datareaders.NameReader;
import com.github.rnowling.bps.datagenerator.datareaders.ZipcodeReader;
import com.github.rnowling.bps.datagenerator.generators.store.StoreGenerator;

public class TestCustomerGenerator
{

	@Test
	public void testGenerate() throws Exception
	{
		ZipcodeReader zipcodeReader = new ZipcodeReader();
		zipcodeReader.setCoordinatesFile(Constants.COORDINATES_FILE);
		zipcodeReader.setIncomesFile(Constants.INCOMES_FILE);
		zipcodeReader.setPopulationFile(Constants.POPULATION_FILE);
		List<ZipcodeRecord> zipcodes = zipcodeReader.readData();
		
		NameReader nameReader = new NameReader(Constants.NAMEDB_FILE);
		Names names = nameReader.readData();
		
		InputData inputData = new InputData(zipcodes, names);
		
		SeedFactory factory = new SeedFactory(1234);
		
		StoreGenerator storeGenerator = new StoreGenerator(zipcodes, factory);
		
		List<Store> stores = new ArrayList<Store>();
		for(int i = 0; i < 10; i++)
		{
			Store store = storeGenerator.generate();
			stores.add(store);
		}
		
		CustomerGenerator customerGenerator = new CustomerGenerator(stores, inputData, factory);
		
		Customer customer = customerGenerator.generate();
		
		assertNotNull(customer);
		assertTrue(customer.getId() >= 0);
		assertNotNull(customer.getName());
		assertNotNull(customer.getName().getFirst());
		assertNotNull(customer.getName().getSecond());
		assertNotNull(customer.getLocation());
		
	}

}
