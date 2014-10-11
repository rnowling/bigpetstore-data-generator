package com.github.rnowling.bps.datagenerator.samplers.store;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Store;
import com.github.rnowling.bps.datagenerator.datareaders.ZipcodeReader;
import com.github.rnowling.bps.datagenerator.samplers.store.StoreSampler;
import com.github.rnowling.bps.datagenerator.statistics.SeedFactory;
import com.github.rnowling.bps.datagenerator.statistics.samplers.RouletteWheelSampler;
import com.github.rnowling.bps.datagenerator.statistics.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.statistics.samplers.SequenceSampler;
import com.google.common.collect.Maps;

public class TestStoreSampler
{

	@Test
	public void testSampler() throws Exception
	{
		ZipcodeReader reader = new ZipcodeReader();
		reader.setCoordinatesFile(Constants.COORDINATES_FILE);
		reader.setIncomesFile(Constants.INCOMES_FILE);
		reader.setPopulationFile(Constants.POPULATION_FILE);
		
		List<ZipcodeRecord> zipcodes = reader.readData();
		
		assertTrue(zipcodes.size() > 0);
		
		SeedFactory factory = new SeedFactory(1234);
		
		Map<ZipcodeRecord, Double> zipcodePDF = Maps.newHashMap();
		for(ZipcodeRecord record : zipcodes)
		{
			zipcodePDF.put(record, 1.0);
		}
		
		Sampler<Store> sampler = new StoreSampler(new SequenceSampler(), 
				RouletteWheelSampler.create(zipcodePDF, factory));
		
		Store store = sampler.sample();
		assertNotNull(store);
		assertTrue(store.getId() >= 0);
		assertNotNull(store.getName());
		assertNotNull(store.getLocation());
		
	}

}
