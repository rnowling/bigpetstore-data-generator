package com.github.rnowling.bps.datagenerator.generators.store;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;

public class TestStoreLocationIncomePDF
{

	@Test
	public void testProbability() throws Exception
	{
		List<ZipcodeRecord> zipcodes = Arrays.asList(new ZipcodeRecord[] {				
				new ZipcodeRecord("11111", Pair.create(1.0, 1.0), "AZ", "Tempte", 30000.0, 100),
				new ZipcodeRecord("22222", Pair.create(2.0, 2.0), "AZ", "Phoenix", 45000.0, 200),
				new ZipcodeRecord("33333", Pair.create(3.0, 3.0), "AZ", "Flagstaff", 60000.0, 300)
				});
		
		StoreLocationIncomePDF pdf = new StoreLocationIncomePDF(zipcodes, 100.0);
		
		for(ZipcodeRecord record : zipcodes)
		{
			assertTrue(pdf.probability(record) > 0.0);
		}
		
	}

}
