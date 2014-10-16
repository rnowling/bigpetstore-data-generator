package com.github.rnowling.bps.datagenerator.generators.customer;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Store;
import com.github.rnowling.bps.datagenerator.resources.Constants;

public class TestCustomerLocationPDF
{

	@Test
	public void testProbability() throws Exception
	{
		List<ZipcodeRecord> zipcodes = Arrays.asList(new ZipcodeRecord[] {				
				new ZipcodeRecord("11111", Pair.create(1.0, 1.0), 30000.0, 100),
				new ZipcodeRecord("22222", Pair.create(2.0, 2.0), 45000.0, 200),
				new ZipcodeRecord("33333", Pair.create(3.0, 3.0), 60000.0, 300)
				});
		
		List<Store> stores = new ArrayList<Store>();
		for(int i = 0; i < zipcodes.size(); i++)
		{
			Store store = new Store(i, "Store_" + i, zipcodes.get(i));
			stores.add(store);
		}
		
		CustomerLocationPDF customerLocationPDF = new CustomerLocationPDF(zipcodes, stores, 
					Constants.AVERAGE_CUSTOMER_STORE_DISTANCE);
		
		double prob = customerLocationPDF.probability(zipcodes.get(0));
		
		assertTrue(prob > 0.0);
	}

}
