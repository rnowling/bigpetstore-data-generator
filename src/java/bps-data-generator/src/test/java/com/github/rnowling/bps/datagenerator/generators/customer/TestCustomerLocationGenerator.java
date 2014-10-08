package com.github.rnowling.bps.datagenerator.generators.customer;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.SeedFactory;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Store;
import com.github.rnowling.bps.datagenerator.datareaders.ZipcodeReader;
import com.github.rnowling.bps.datagenerator.generators.store.StoreSampler;

public class TestCustomerLocationGenerator
{

	@Test
	public void testGenerate() throws Exception
	{
		ZipcodeReader zipcodeReader = new ZipcodeReader();
		zipcodeReader.setCoordinatesFile(Constants.COORDINATES_FILE);
		zipcodeReader.setIncomesFile(Constants.INCOMES_FILE);
		zipcodeReader.setPopulationFile(Constants.POPULATION_FILE);
		List<ZipcodeRecord> zipcodes = zipcodeReader.readData();
		
		SeedFactory factory = new SeedFactory(1234);
		
		StoreSampler storeGenerator = new StoreSampler(zipcodes, factory);
		
		List<Store> stores = new ArrayList<Store>();
		for(int i = 0; i < 10; i++)
		{
			Store store = storeGenerator.sample();
			stores.add(store);
		}
		
		CustomerLocationGenerator customerLocationGenerator = new CustomerLocationGenerator(zipcodes, stores, 
					Constants.AVERAGE_CUSTOMER_STORE_DISTANCE, factory);
		
		ZipcodeRecord location = customerLocationGenerator.generate();
		
		assertNotNull(location);
	}

}
