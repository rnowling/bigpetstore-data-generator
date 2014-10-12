package com.github.rnowling.bps.datagenerator.generators.store;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Store;
import com.github.rnowling.bps.datagenerator.datareaders.ZipcodeReader;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.generators.store.StoreSamplerBuilder;

public class TestStoreSamplerBuilder
{

	@Test
	public void testBuild() throws Exception
	{
		ZipcodeReader reader = new ZipcodeReader();
		reader.setCoordinatesFile(Constants.COORDINATES_FILE);
		reader.setIncomesFile(Constants.INCOMES_FILE);
		reader.setPopulationFile(Constants.POPULATION_FILE);
		
		List<ZipcodeRecord> zipcodes = reader.readData();
		
		assertTrue(zipcodes.size() > 0);
		
		SeedFactory factory = new SeedFactory(1234);
		
		StoreSamplerBuilder builder = new StoreSamplerBuilder(zipcodes, factory);
		Sampler<Store> sampler = builder.build();
		
		Store store = sampler.sample();
		assertNotNull(store);
		assertTrue(store.getId() >= 0);
		assertNotNull(store.getName());
		assertNotNull(store.getLocation());
		
	}

}
