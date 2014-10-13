package com.github.rnowling.bps.datagenerator.generators.store;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Store;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.samplers.RouletteWheelSampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.SequenceSampler;

public class TestStoreSampler
{

	@Test
	public void testSampler() throws Exception
	{
		Collection<ZipcodeRecord> zipcodes = Arrays.asList(new ZipcodeRecord[] {				
				new ZipcodeRecord("11111", Pair.create(1.0, 1.0), 30000.0, 100),
				new ZipcodeRecord("22222", Pair.create(2.0, 2.0), 45000.0, 200),
				new ZipcodeRecord("33333", Pair.create(3.0, 3.0), 60000.0, 300)
				});
		
		SeedFactory factory = new SeedFactory(1234);
		
		Sampler<Store> sampler = new StoreSampler(new SequenceSampler(), 
				RouletteWheelSampler.createUniform(zipcodes, factory));
		
		Store store = sampler.sample();
		assertNotNull(store);
		assertTrue(store.getId() >= 0);
		assertNotNull(store.getName());
		assertNotNull(store.getLocation());
		
	}

}
