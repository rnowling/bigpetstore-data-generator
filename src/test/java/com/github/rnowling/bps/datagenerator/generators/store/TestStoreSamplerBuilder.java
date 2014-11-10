package com.github.rnowling.bps.datagenerator.generators.store;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.github.rnowling.bps.datagenerator.datamodels.Store;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;

public class TestStoreSamplerBuilder
{

	@Test
	public void testBuild() throws Exception
	{
		List<ZipcodeRecord> zipcodes = Arrays.asList(new ZipcodeRecord[] {				
				new ZipcodeRecord("11111", Pair.create(1.0, 1.0), 30000.0, 100),
				new ZipcodeRecord("22222", Pair.create(2.0, 2.0), 45000.0, 200),
				new ZipcodeRecord("33333", Pair.create(3.0, 3.0), 60000.0, 300)
				});
		
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
